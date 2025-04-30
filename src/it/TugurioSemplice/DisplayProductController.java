package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DataManagement.ProdottoDAO;

@WebServlet("/DisplayProductController")
public class DisplayProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static ProdottoDAO model;
	
	static {
		model = new ProdottoDAO();
	}
	
	public DisplayProductController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			request.setAttribute("product", model.doRetrieveByKey(id));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error:" + e.getMessage());
			request.setAttribute("error", "Si è verificato un errore nel database: " + e.getMessage());
		}
		
		String nome = request.getParameter("filtroNome");
		String categoria = request.getParameter("filtroCategoria");
		String prezzoMinStr = request.getParameter("prezzoMin");
		String prezzoMaxStr = request.getParameter("prezzoMax");
		String disponibilita = request.getParameter("filtroDisponibilita");
		
		Double prezzoMin = null;
		Double prezzoMax = null;
		
		if (prezzoMinStr != null && !prezzoMinStr.isEmpty()) {
			try {
				prezzoMin = Double.parseDouble(prezzoMinStr);
			} catch (NumberFormatException e) {
				System.out.println("Formato prezzo minimo non valido: " + e.getMessage());
			}
		}
		
		if (prezzoMaxStr != null && !prezzoMaxStr.isEmpty()) {
			try {
				prezzoMax = Double.parseDouble(prezzoMaxStr);
			} catch (NumberFormatException e) {
				System.out.println("Formato prezzo massimo non valido: " + e.getMessage());
			}
		}
		
		String sort = request.getParameter("sort");
		if(sort == null || sort.isEmpty()) {
			sort = "Nome"; 
		}

		try {
			request.setAttribute("products", model.doRetrieveFiltered(nome, categoria, prezzoMin, prezzoMax, disponibilita, sort));
			request.setAttribute("filtroNome", nome);
			request.setAttribute("filtroCategoria", categoria);
			request.setAttribute("prezzoMin", prezzoMinStr);
			request.setAttribute("prezzoMax", prezzoMaxStr);
			request.setAttribute("filtroDisponibilita", disponibilita);
			request.setAttribute("sortAttuale", sort);
			
		} catch (SQLException e) {
			System.out.println("Error:" + e.getMessage());
			request.setAttribute("error", "Si è verificato un errore nel recupero dei prodotti: " + e.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/ProductDetail.jsp")
			.forward(request, response);
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

}
