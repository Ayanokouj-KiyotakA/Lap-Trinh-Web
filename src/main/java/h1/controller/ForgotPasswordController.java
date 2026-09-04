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

// Buoc 1 cua quen mat khau: nhap email da dang ky -> gui OTP xac nhan qua mail
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String email = req.getParameter("email");
		email = email == null ? "" : email.trim();

		if (email.isEmpty()) {
			req.setAttribute("alert", "Vui lòng nhập email");
			req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
			return;
		}

		AppUser user = userService.findByEmail(email);
		if (user == null) {
			req.setAttribute("alert", "Không tìm thấy tài khoản với email này");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
			return;
		}

		user.setOtpCode(OtpUtil.generate());
		user.setOtpExpiredAt(OtpUtil.expiredAtFromNow());
		userService.update(user);

		try {
			MailUtil.sendOtpMail(email, user.getOtpCode(), "dat lai mat khau");
		} catch (MessagingException e) {
			e.printStackTrace();
			req.setAttribute("alert", "Gửi email OTP thất bại, vui lòng thử lại sau.");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
			return;
		}

		HttpSession session = req.getSession(true);
		session.setAttribute("resetEmail", email);
		resp.sendRedirect(req.getContextPath() + "/reset-password");
	}
}
