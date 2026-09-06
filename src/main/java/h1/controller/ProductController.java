package h1.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import h1.config.Constant;
import h1.entity.Category;
import h1.entity.Product;
import h1.service.CategoryServiceImpl;
import h1.service.ICategoryService;
import h1.service.IProductService;
import h1.service.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

// CRUD Product cho admin (mirror CategoryController)
@MultipartConfig
@WebServlet(urlPatterns = { "/admin/products", "/admin/product/add", "/admin/product/insert", "/admin/product/edit",
		"/admin/product/update", "/admin/product/delete" })
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public IProductService productService = new ProductServiceImpl();
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
		if (url.contains("/admin/products")) {
			List<Product> list = productService.findAll();
			req.setAttribute("listproduct", list);
			req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);
		} else if (url.contains("/admin/product/add")) {
			req.setAttribute("listcate", cateService.findAll());
			req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
		} else if (url.contains("/admin/product/edit")) {
			Integer id = parseIntOrNull(req.getParameter("id"));
			Product product = id == null ? null : productService.findById(id);
			if (product == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/products");
				return;
			}
			req.setAttribute("product", product);
			req.setAttribute("listcate", cateService.findAll());
			req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
		} else {
			Integer id = parseIntOrNull(req.getParameter("id"));
			if (id != null) {
				productService.delete(id);
			}
			resp.sendRedirect(req.getContextPath() + "/admin/products");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (requireLogin(req, resp)) {
			return;
		}
		String url = req.getRequestURI();
		if (url.contains("/admin/product/insert")) {
			String productname = getPartValue(req.getPart("productname"));
			String description = getPartValue(req.getPart("description"));
			String priceParam = getPartValue(req.getPart("price"));
			String statusParam = req.getParameter("status");
			String categoryidParam = getPartValue(req.getPart("categoryid"));
			String images = getPartValue(req.getPart("images")); // link ảnh (nếu người dùng nhập thay vì upload)

			Map<String, String> errors = validate(productname, priceParam, statusParam, categoryidParam);
			if (!errors.isEmpty()) {
				req.setAttribute("errors", errors);
				req.setAttribute("productname", productname);
				req.setAttribute("description", description);
				req.setAttribute("price", priceParam);
				req.setAttribute("status", statusParam);
				req.setAttribute("categoryid", categoryidParam);
				req.setAttribute("listcate", cateService.findAll());
				req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
				return;
			}

			Product product = new Product();
			product.setProductname(productname.trim());
			product.setDescription(description);
			product.setPrice(Double.parseDouble(priceParam.trim()));
			product.setStatus(Integer.parseInt(statusParam));
			product.setCreatedDate(new Date());

			Category category = new Category();
			category.setCategoryId(Integer.parseInt(categoryidParam.trim()));
			product.setCategory(category);

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
					product.setImages(fname);
				} else if (images != null && !images.isEmpty()) {
					// Không upload file nhưng có nhập link -> dùng link
					product.setImages(images);
				} else {
					// Không có gì -> ảnh mặc định
					product.setImages("avatar.png");
				}
			} catch (FileNotFoundException fne) {
				fne.printStackTrace();
			}

			productService.insert(product);
			resp.sendRedirect(req.getContextPath() + "/admin/products");
		}

		if (url.contains("/admin/product/update")) {
			Integer productId = parseIntOrNull(getPartValue(req.getPart("productid")));
			String productname = getPartValue(req.getPart("productname"));
			String description = getPartValue(req.getPart("description"));
			String priceParam = getPartValue(req.getPart("price"));
			String statusParam = req.getParameter("status");
			String categoryidParam = getPartValue(req.getPart("categoryid"));
			String images = getPartValue(req.getPart("images"));

			Product product = productId == null ? null : productService.findById(productId);
			if (product == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/products");
				return;
			}

			Map<String, String> errors = validate(productname, priceParam, statusParam, categoryidParam);
			if (!errors.isEmpty()) {
				// Giu lai gia tri vua nhap (chua luu) de hien thi lai form
				product.setProductname(productname);
				product.setDescription(description);
				req.setAttribute("errors", errors);
				req.setAttribute("product", product);
				req.setAttribute("listcate", cateService.findAll());
				req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
				return;
			}

			String fileold = product.getImages();
			product.setProductname(productname.trim());
			product.setDescription(description);
			product.setPrice(Double.parseDouble(priceParam.trim()));
			product.setStatus(Integer.parseInt(statusParam));

			Category category = new Category();
			category.setCategoryId(Integer.parseInt(categoryidParam.trim()));
			product.setCategory(category);

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
					product.setImages(fname);
				} else if (images != null && !images.isEmpty()) {
					product.setImages(images);
				} else {
					product.setImages(fileold);
				}
			} catch (FileNotFoundException fne) {
				fne.printStackTrace();
			}

			productService.update(product);
			resp.sendRedirect(req.getContextPath() + "/admin/products");
		}
	}

	// Bai tap 04 - yeu cau 2: validate form them/sua Product
	private Map<String, String> validate(String productname, String priceParam, String statusParam,
			String categoryidParam) {
		Map<String, String> errors = new LinkedHashMap<>();

		if (productname == null || productname.trim().isEmpty()) {
			errors.put("productname", "Tên sản phẩm không được để trống");
		} else if (productname.trim().length() > 255) {
			errors.put("productname", "Tên sản phẩm tối đa 255 ký tự");
		}

		if (priceParam == null || priceParam.trim().isEmpty()) {
			errors.put("price", "Giá không được để trống");
		} else {
			try {
				double price = Double.parseDouble(priceParam.trim());
				if (price < 0) {
					errors.put("price", "Giá không được âm");
				}
			} catch (NumberFormatException e) {
				errors.put("price", "Giá không hợp lệ");
			}
		}

		if (!"0".equals(statusParam) && !"1".equals(statusParam)) {
			errors.put("status", "Vui lòng chọn trạng thái hợp lệ");
		}

		Integer categoryid = categoryidParam == null ? null : parseIntOrNull(categoryidParam.trim());
		if (categoryid == null) {
			errors.put("categoryid", "Vui lòng chọn danh mục");
		} else if (cateService.findById(categoryid) == null) {
			errors.put("categoryid", "Danh mục không tồn tại");
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
