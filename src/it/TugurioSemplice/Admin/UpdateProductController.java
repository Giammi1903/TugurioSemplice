package it.TugurioSemplice.Admin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DataManagement.ProdottoBean;
import DataManagement.ProdottoDAO;

@WebServlet("/UpdateProductController")
@MultipartConfig
public class UpdateProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static ProdottoDAO model;
	
	static {
		model = new ProdottoDAO();
	}
	
	public UpdateProductController() {
		super();
	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			ProdottoBean bean = model.doRetrieveByKey(id);
			request.setAttribute("prodotto", bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		getServletContext().getRequestDispatcher("/AdminAction/ModificaAdmin.jsp")
		.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		String SAVE_DIR = "photo";
		Part image = request.getPart("image");
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + SAVE_DIR;
		File fileSaveDir = new File(savePath);
		String fileName;
		
		if(image != null || image.getSize() > 0) {
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
			}
			
			fileName = extractFileName(image);
			if (fileName != null && !fileName.equals("")) {
				image.write(savePath + File.separator + fileName);
			}
		} else {
			image = request.getPart("currentImage");
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
			}
			
			fileName = extractFileName(image);
			if (fileName != null && !fileName.equals("")) {
				image.write(savePath + File.separator + fileName);
			}			
		}
		
		ProdottoBean bean = new ProdottoBean();
		bean.setIdProdotto(id);
		bean.setNome(name);
		bean.setDescrizione(description);
		bean.setPrezzoBase(price);
		bean.setQuantita(quantity);
		bean.setDisponibilita(quantity > 0);
		bean.setIva(22);
		bean.setImmagine(savePath + File.separator + fileName);
		try {
			model.doUpdate(bean);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error:" + e.getMessage());
			request.setAttribute("error", "Si è verificato un errore nel database: " + e.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/ProductDetail.jsp")
		.forward(request, response);
		
	}

}
