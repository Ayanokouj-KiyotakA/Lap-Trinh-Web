package h1.controller;

import java.io.IOException;

import h1.service.IProductService;
import h1.service.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/admin/home")
public class AdminHomeController extends HttpServlet {

	private final IProductService productService = new ProductServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("account") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		// Cung view home.jsp voi HomeController nen cung can nap latestProducts,
		// khong thi grid "San pham moi nhat" se luon rong khi admin xem trang nay
		req.setAttribute("latestProducts", productService.findLatestActive(10));
		req.getRequestDispatcher("/views/home.jsp").forward(req, resp);
	}
}
