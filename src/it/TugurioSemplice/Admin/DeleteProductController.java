package it.TugurioSemplice.Admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DataManagement.ProdottoDAO;

@WebServlet("/DeleteProductController")
public class DeleteProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static ProdottoDAO model;
	
	static {
		model = new ProdottoDAO();
	}
	
	public DeleteProductController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			int id = Integer.parseInt(request.getParameter("id"));
			try {
				model.doDelete(id);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error:" + e.getMessage());
				request.setAttribute("error", "Si è verificato un errore nel database: " + e.getMessage());
			}
			
			request.getRequestDispatcher("./ProductView.jsp")
				.forward(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

}
