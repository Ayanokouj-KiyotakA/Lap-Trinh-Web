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

// Xac nhan OTP kich hoat tai khoan vua dang ky (hoac gui lai OTP)
@WebServlet(urlPatterns = "/verify-otp")
public class VerifyOtpController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String email = session == null ? null : (String) session.getAttribute("pendingEmail");
		if (email == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}
		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String email = session == null ? null : (String) session.getAttribute("pendingEmail");
		if (email == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		AppUser user = userService.findByEmail(email);
		if (user == null) {
			session.removeAttribute("pendingEmail");
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		String action = req.getParameter("action");
		if ("resend".equals(action)) {
			resendOtp(req, resp, user, email);
			return;
		}

		String otp = req.getParameter("otp");
		if (!OtpUtil.isValid(user.getOtpCode(), user.getOtpExpiredAt(), otp)) {
			req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn. Vui lòng thử lại hoặc gửi lại mã.");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
			return;
		}

		user.setActive(1);
		user.setOtpCode(null);
		user.setOtpExpiredAt(null);
		userService.update(user);

		session.removeAttribute("pendingEmail");
		session.setAttribute("notice", "Kích hoạt tài khoản thành công. Bạn có thể đăng nhập ngay.");
		resp.sendRedirect(req.getContextPath() + "/login");
	}

	private void resendOtp(HttpServletRequest req, HttpServletResponse resp, AppUser user, String email)
			throws IOException, ServletException {
		user.setOtpCode(OtpUtil.generate());
		user.setOtpExpiredAt(OtpUtil.expiredAtFromNow());
		userService.update(user);

		try {
			MailUtil.sendOtpMail(email, user.getOtpCode(), "kich hoat tai khoan");
			req.setAttribute("success", "Đã gửi lại mã OTP, vui lòng kiểm tra email.");
		} catch (MessagingException e) {
			e.printStackTrace();
			req.setAttribute("alert", "Gửi lại OTP thất bại, vui lòng thử lại sau.");
		}
		req.setAttribute("email", email);
		req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
	}
}
