package h1.controller;

import java.io.IOException;
import java.util.List;

import h1.entity.Product;
import h1.service.IProductService;
import h1.service.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Trang cong khai (khong yeu cau dang nhap): danh sach san pham phan trang
// va trang chi tiet 1 san pham
@WebServlet(urlPatterns = { "/product", "/product/detail" })
public class ProductPublicController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final int PAGE_SIZE = 6;

	private final IProductService productService = new ProductServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		if (url.contains("/product/detail")) {
			showDetail(req, resp);
		} else {
			showList(req, resp);
		}
	}

	private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int page = parsePage(req.getParameter("page"));

		int total = productService.countActive();
		int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
		if (totalPages < 1) {
			totalPages = 1;
		}
		if (page > totalPages) {
			page = totalPages;
		}

		List<Product> list = productService.findAllActive(page - 1, PAGE_SIZE);

		req.setAttribute("listproduct", list);
		req.setAttribute("page", page);
		req.setAttribute("totalPages", totalPages);
		req.getRequestDispatcher("/views/product-list.jsp").forward(req, resp);
	}

	private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idParam = req.getParameter("id");
		Product product = null;
		if (idParam != null) {
			try {
				product = productService.findById(Integer.parseInt(idParam));
			} catch (NumberFormatException e) {
				product = null;
			}
		}
		if (product == null) {
			resp.sendRedirect(req.getContextPath() + "/product");
			return;
		}
		req.setAttribute("product", product);
		req.getRequestDispatcher("/views/product-detail.jsp").forward(req, resp);
	}

	private int parsePage(String value) {
		try {
			int page = Integer.parseInt(value);
			return Math.max(page, 1);
		} catch (Exception e) {
			return 1;
		}
	}
}
