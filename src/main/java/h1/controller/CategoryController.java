package h1.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import h1.config.Constant;
import h1.entity.Category;
import h1.service.CategoryServiceImpl;
import h1.service.ICategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig
@WebServlet(urlPatterns = { "/admin/categories", "/admin/category/add", "/admin/category/insert",
		"/admin/category/edit", "/admin/category/update", "/admin/category/delete" })
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public ICategoryService cateService = new CategoryServiceImpl();

	private boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("account") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (requireLogin(req, resp)) {
			return;
		}
		String url = req.getRequestURI();
		if (url.contains("/admin/categories")) {
			List<Category> list = cateService.findAll();
			req.setAttribute("listcate", list);
			req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
		} else if (url.contains("/admin/category/add")) {
			req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
		} else if (url.contains("/admin/category/edit")) {
			Integer id = parseIntOrNull(req.getParameter("id"));
			if (id == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/categories");
				return;
			}
			Category category = cateService.findById(id);
			if (category == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/categories");
				return;
			}
			req.setAttribute("cate", category);
			req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
		} else {
			Integer id = parseIntOrNull(req.getParameter("id"));
			if (id != null) {
				try {
					cateService.delete(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			resp.sendRedirect(req.getContextPath() + "/admin/categories");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (requireLogin(req, resp)) {
			return;
		}
		String url = req.getRequestURI();
		if (url.contains("/admin/category/insert")) {
			String categoryname = getPartValue(req.getPart("categoryname"));
			String statusParam = req.getParameter("status");
			String images = getPartValue(req.getPart("images")); // link ảnh (nếu người dùng nhập thay vì upload)

			Map<String, String> errors = validate(categoryname, statusParam);
			if (errors.isEmpty() && cateService.findByCategoryname(categoryname.trim()) != null) {
				errors.put("categoryname", "Tên category đã tồn tại");
			}
			if (!errors.isEmpty()) {
				req.setAttribute("errors", errors);
				req.setAttribute("categoryname", categoryname);
				req.setAttribute("images", images);
				req.setAttribute("status", statusParam);
				req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
				return;
			}

			Category category = new Category();
			category.setCategoryname(categoryname.trim());
			category.setStatus(Integer.parseInt(statusParam));

			String uploadPath = Constant.DIR;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			try {
				Part part = req.getPart("images1");
				if (part != null && part.getSize() > 0) {
					// Có upload file -> ưu tiên dùng file
					String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
					int index = filename.lastIndexOf(".");
					String ext = filename.substring(index + 1);
					String fname = System.currentTimeMillis() + "." + ext;
					part.write(uploadPath + "/" + fname);
					category.setImages(fname);
				} else if (images != null && !images.isEmpty()) {
					// Không upload file nhưng có nhập link -> dùng link
					category.setImages(images);
				} else {
					// Không có gì -> ảnh mặc định
					category.setImages("avatar.png");
				}
			} catch (FileNotFoundException fne) {
				fne.printStackTrace();
			}

			cateService.insert(category);
			resp.sendRedirect(req.getContextPath() + "/admin/categories");
		}

		if (url.contains("/admin/category/update")) {
			Integer categoryid = parseIntOrNull(getPartValue(req.getPart("categoryid")));
			String categoryname = getPartValue(req.getPart("categoryname"));
			String statusParam = req.getParameter("status");
			String images = getPartValue(req.getPart("images"));

			if (categoryid == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/categories");
				return;
			}
			Category category = cateService.findById(categoryid);
			if (category == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/categories");
				return;
			}

			Map<String, String> errors = validate(categoryname, statusParam);
			Category duplicate = cateService.findByCategoryname(categoryname == null ? "" : categoryname.trim());
			if (errors.isEmpty() && duplicate != null && duplicate.getCategoryId() != categoryid) {
				errors.put("categoryname", "Tên category đã tồn tại");
			}
			if (!errors.isEmpty()) {
				// Giu lai gia tri vua nhap (chua luu) de hien thi lai form, khong mat
				// du lieu category goc trong DB
				category.setCategoryname(categoryname);
				req.setAttribute("errors", errors);
				req.setAttribute("cate", category);
				req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
				return;
			}

			String fileold = category.getImages();
			category.setCategoryname(categoryname.trim());
			category.setStatus(Integer.parseInt(statusParam));

			String uploadPath = Constant.DIR;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			try {
				Part part = req.getPart("images1");
				if (part != null && part.getSize() > 0) {
					// xóa file cũ trên thư mục (nếu là file local, không phải link http)
					if (fileold != null && fileold.length() >= 5 && !fileold.substring(0, 5).equals("https")) {
						deleteFileQuietly(uploadPath + "\\" + fileold);
					}
					String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
					int index = filename.lastIndexOf(".");
					String ext = filename.substring(index + 1);
					String fname = System.currentTimeMillis() + "." + ext;
					part.write(uploadPath + "/" + fname);
					category.setImages(fname);
				} else if (images != null && !images.isEmpty()) {
					category.setImages(images);
				} else {
					category.setImages(fileold);
				}
			} catch (FileNotFoundException fne) {
				fne.printStackTrace();
			}

			cateService.update(category);
			resp.sendRedirect(req.getContextPath() + "/admin/categories");
		}
	}

	// Bai tap 04 - yeu cau 2: validate form them/sua Category
	private Map<String, String> validate(String categoryname, String statusParam) {
		Map<String, String> errors = new LinkedHashMap<>();
		if (categoryname == null || categoryname.trim().isEmpty()) {
			errors.put("categoryname", "Tên category không được để trống");
		} else if (categoryname.trim().length() > 255) {
			errors.put("categoryname", "Tên category tối đa 255 ký tự");
		}
		if (!"0".equals(statusParam) && !"1".equals(statusParam)) {
			errors.put("status", "Vui lòng chọn trạng thái hợp lệ");
		}
		return errors;
	}

	private Integer parseIntOrNull(String value) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return null;
		}
	}

	// Đọc field text trong multipart form đúng UTF-8
	// (req.setCharacterEncoding("UTF-8") không có tác dụng với multipart trên Tomcat)
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
