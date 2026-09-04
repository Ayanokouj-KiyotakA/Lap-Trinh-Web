package h1.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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

// Dang ky tai khoan moi: tao AppUser voi active=0, gui OTP kich hoat qua email
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IUserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String username = trim(req.getParameter("username"));
		String email = trim(req.getParameter("email"));
		String fullname = trim(req.getParameter("fullname"));
		String phone = trim(req.getParameter("phone"));
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");
		password = password == null ? "" : password;
		confirmPassword = confirmPassword == null ? "" : confirmPassword;

		Map<String, String> errors = validate(username, email, password, confirmPassword);

		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			setFormAttributes(req, username, email, fullname, phone);
			req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
			return;
		}

		AppUser user = new AppUser();
		user.setUserName(username);
		user.setEmail(email);
		user.setFullName(fullname);
		user.setPhone(phone);
		user.setPassWord(password);
		user.setRoleid(3);
		user.setActive(0);
		user.setCreatedDate(new Date());
		user.setOtpCode(OtpUtil.generate());
		user.setOtpExpiredAt(OtpUtil.expiredAtFromNow());

		userService.insert(user);

		try {
			MailUtil.sendOtpMail(email, user.getOtpCode(), "kich hoat tai khoan");
		} catch (MessagingException e) {
			e.printStackTrace();
			req.setAttribute("alert", "Tài khoản đã được tạo nhưng gửi email OTP thất bại. "
					+ "Vui lòng dùng chức năng gửi lại OTP ở bước xác nhận.");
		}

		HttpSession session = req.getSession(true);
		session.setAttribute("pendingEmail", email);

		resp.sendRedirect(req.getContextPath() + "/verify-otp");
	}

	private Map<String, String> validate(String username, String email, String password, String confirmPassword) {
		Map<String, String> errors = new LinkedHashMap<>();

		if (username.isEmpty()) {
			errors.put("username", "Tài khoản không được để trống");
		} else if (userService.findByUserName(username) != null) {
			errors.put("username", "Tài khoản đã tồn tại");
		}

		if (email.isEmpty()) {
			errors.put("email", "Email không được để trống");
		} else if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
			errors.put("email", "Email không hợp lệ");
		} else if (userService.findByEmail(email) != null) {
			errors.put("email", "Email đã được sử dụng");
		}

		if (password.isEmpty()) {
			errors.put("password", "Mật khẩu không được để trống");
		} else if (password.length() < 6) {
			errors.put("password", "Mật khẩu tối thiểu 6 ký tự");
		} else if (!password.equals(confirmPassword)) {
			errors.put("confirmPassword", "Mật khẩu nhập lại không khớp");
		}

		return errors;
	}

	private void setFormAttributes(HttpServletRequest req, String username, String email, String fullname,
			String phone) {
		req.setAttribute("username", username);
		req.setAttribute("email", email);
		req.setAttribute("fullname", fullname);
		req.setAttribute("phone", phone);
	}

	private String trim(String value) {
		return value == null ? "" : value.trim();
	}
}
