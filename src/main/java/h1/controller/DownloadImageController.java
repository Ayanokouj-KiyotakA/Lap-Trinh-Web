package h1.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import h1.config.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/image") // ?fname=abc.png
public class DownloadImageController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fname");
		resp.setContentType("image/jpeg");

		if (fileName == null || fileName.isEmpty()) {
			return;
		}

		// Chi lay ten file (bo moi thanh phan thu muc) de chan path traversal
		// (vd fname=../../../../Windows/win.ini) truy cap file ngoai Constant.DIR
		fileName = Paths.get(fileName).getFileName().toString();

		File file = new File(Constant.DIR + "/" + fileName);
		if (file.exists()) {
			try (FileInputStream in = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {
				byte[] buffer = new byte[4096];
				int len;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			}
		}
	}
}
