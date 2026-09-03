package h1.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import h1.config.Constant;
import h1.entity.AppUser;
import h1.service.IUserService;
import h1.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

// Chuc nang Profile: cho phep user dang nhap cap nhat fullname, phone, avatar (upload file)
@MultipartConfig
@WebServlet(urlPatterns = "/profile")
public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final IUserService userService = new UserServiceImpl();

	private AppUser requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("account") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return null;
		}
		return (AppUser) session.getAttribute("account");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AppUser account = requireLogin(req, resp);
		if (account == null) {
			return;
		}

		// Doc lai tu DB de luon hien thi du lieu moi nhat
		AppUser user = userService.findById(account.getId());
		if (user == null) {
			user = account;
		}
		req.setAttribute("user", user);

		HttpSession session = req.getSession();
		Object success = session.getAttribute("profileUpdated");
		if (success != null) {
			req.setAttribute("success", success);
			session.removeAttribute("profileUpdated");
		}

		req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AppUser account = requireLogin(req, resp);
		if (account == null) {
			return;
		}

		String fullName = getPartValue(req.getPart("fullname"));
		String phone = getPartValue(req.getPart("phone"));
		fullName = fullName == null ? "" : fullName.trim();
		phone = phone == null ? "" : phone.trim();

		Map<String, String> errors = validate(fullName, phone);

		AppUser user = userService.findById(account.getId());
		if (user == null) {
			user = account;
		}

		if (!errors.isEmpty()) {
			// Giu lai gia tri vua nhap de nguoi dung khong phai go lai tu dau
			user.setFullName(fullName);
			user.setPhone(phone);
			req.setAttribute("errors", errors);
			req.setAttribute("user", user);
			req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
			return;
		}

		user.setFullName(fullName);
		user.setPhone(phone);

		String uploadPath = Constant.DIR;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		try {
			Part part = req.getPart("avatar1");
			if (part != null && part.getSize() > 0) {
				String oldAvatar = user.getAvatar();
				String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
				int index = filename.lastIndexOf(".");
				String ext = index >= 0 ? filename.substring(index + 1) : "png";
				String fname = System.currentTimeMillis() + "." + ext;
				part.write(uploadPath + "/" + fname);
				user.setAvatar(fname);

				// Xoa file avatar cu (neu la file local, khong phai link http)
				if (oldAvatar != null && oldAvatar.length() >= 5 && !oldAvatar.substring(0, 5).equals("https")) {
					deleteFileQuietly(uploadPath + "\\" + oldAvatar);
				}
			}
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		}

		userService.update(user);

		// Cap nhat lai session de cac trang khac (navbar...) thay du lieu moi ngay
		HttpSession session = req.getSession();
		session.setAttribute("account", user);
		session.setAttribute("profileUpdated", "Cập nhật hồ sơ thành công");

		resp.sendRedirect(req.getContextPath() + "/profile");
	}

	private Map<String, String> validate(String fullName, String phone) {
		Map<String, String> errors = new LinkedHashMap<>();
		if (fullName.isEmpty()) {
			errors.put("fullname", "Họ tên không được để trống");
		} else if (fullName.length() > 255) {
			errors.put("fullname", "Họ tên tối đa 255 ký tự");
		}
		if (!phone.isEmpty() && !phone.matches("^[0-9]{8,15}$")) {
			errors.put("phone", "Số điện thoại chỉ gồm 8-15 chữ số");
		}
		return errors;
	}

	// Doc field text trong multipart form dung UTF-8
	// (req.setCharacterEncoding("UTF-8") khong co tac dung voi multipart tren Tomcat)
	private String getPartValue(Part part) throws IOException {
		if (part == null) {
			return null;
		}
		try (var is = part.getInputStream()) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	private static void deleteFileQuietly(String filePath) {
		try {
			Path path = Paths.get(filePath);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
