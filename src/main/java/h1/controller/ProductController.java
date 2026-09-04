package h1.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

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
			int id = Integer.parseInt(req.getParameter("id"));
			Product product = productService.findById(id);
			req.setAttribute("product", product);
			req.setAttribute("listcate", cateService.findAll());
			req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
		} else {
			int id = Integer.parseInt(req.getParameter("id"));
			productService.delete(id);
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
			double price = parseDouble(getPartValue(req.getPart("price")));
			int status = Integer.parseInt(req.getParameter("status"));
			int categoryid = Integer.parseInt(getPartValue(req.getPart("categoryid")));
			String images = getPartValue(req.getPart("images")); // link ảnh (nếu người dùng nhập thay vì upload)

			Product product = new Product();
			product.setProductname(productname);
			product.setDescription(description);
			product.setPrice(price);
			product.setStatus(status);
			product.setCreatedDate(new Date());

			Category category = new Category();
			category.setCategoryId(categoryid);
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
			int productId = Integer.parseInt(getPartValue(req.getPart("productid")));
			String productname = getPartValue(req.getPart("productname"));
			String description = getPartValue(req.getPart("description"));
			double price = parseDouble(getPartValue(req.getPart("price")));
			int status = Integer.parseInt(req.getParameter("status"));
			int categoryid = Integer.parseInt(getPartValue(req.getPart("categoryid")));
			String images = getPartValue(req.getPart("images"));

			Product product = productService.findById(productId);
			String fileold = product.getImages();
			product.setProductname(productname);
			product.setDescription(description);
			product.setPrice(price);
			product.setStatus(status);

			Category category = new Category();
			category.setCategoryId(categoryid);
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

	private double parseDouble(String value) {
		try {
			return value == null ? 0 : Double.parseDouble(value.trim());
		} catch (NumberFormatException e) {
			return 0;
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
