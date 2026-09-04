package h1.controller;

import java.io.IOException;

import h1.entity.AppUser;
import h1.service.IUserService;
import h1.service.UserServiceImpl;
import h1.util.MailUtil;
import h1.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Buoc 2 cua quen mat khau: nhap OTP + mat khau moi
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String email = session == null ? null : (String) session.getAttribute("resetEmail");
		if (email == null) {
			resp.sendRedirect(req.getContextPath() + "/forgot-password");
			return;
		}
		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession(false);
		String email = session == null ? null : (String) session.getAttribute("resetEmail");
		if (email == null) {
			resp.sendRedirect(req.getContextPath() + "/forgot-password");
			return;
		}

		AppUser user = userService.findByEmail(email);
		if (user == null) {
			session.removeAttribute("resetEmail");
			resp.sendRedirect(req.getContextPath() + "/forgot-password");
			return;
		}

		String action = req.getParameter("action");
		if ("resend".equals(action)) {
			resendOtp(req, resp, user, email);
			return;
		}

		String otp = req.getParameter("otp");
		String newPassword = req.getParameter("newPassword");
		String confirmPassword = req.getParameter("confirmPassword");
		newPassword = newPassword == null ? "" : newPassword;
		confirmPassword = confirmPassword == null ? "" : confirmPassword;

		if (!OtpUtil.isValid(user.getOtpCode(), user.getOtpExpiredAt(), otp)) {
			req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn. Vui lòng thử lại hoặc gửi lại mã.");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
			return;
		}

		if (newPassword.length() < 6) {
			req.setAttribute("alert", "Mật khẩu mới tối thiểu 6 ký tự");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			req.setAttribute("alert", "Mật khẩu nhập lại không khớp");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
			return;
		}

		// Giu nguyen cach luu/so sanh mat khau plain-text dang dung o
		// UserServiceImpl.login() de khong lam gay dang nhap cac tai khoan khac
		user.setPassWord(newPassword);
		user.setOtpCode(null);
		user.setOtpExpiredAt(null);
		userService.update(user);

		session.removeAttribute("resetEmail");
		session.setAttribute("notice", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập bằng mật khẩu mới.");
		resp.sendRedirect(req.getContextPath() + "/login");
	}

	private void resendOtp(HttpServletRequest req, HttpServletResponse resp, AppUser user, String email)
			throws IOException, ServletException {
		user.setOtpCode(OtpUtil.generate());
		user.setOtpExpiredAt(OtpUtil.expiredAtFromNow());
		userService.update(user);

		try {
			MailUtil.sendOtpMail(email, user.getOtpCode(), "dat lai mat khau");
			req.setAttribute("success", "Đã gửi lại mã OTP, vui lòng kiểm tra email.");
		} catch (MessagingException e) {
			e.printStackTrace();
			req.setAttribute("alert", "Gửi lại OTP thất bại, vui lòng thử lại sau.");
		}
		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
	}
}
