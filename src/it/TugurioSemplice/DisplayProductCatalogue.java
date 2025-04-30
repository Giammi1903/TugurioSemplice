package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DataManagement.ProdottoDAO;
import DataManagement.ProdottoBean;

@WebServlet("/DisplayProductCatalogue")
public class DisplayProductCatalogue extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static ProdottoDAO model;

	static {
		model = new ProdottoDAO();
	}

	public DisplayProductCatalogue() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("./ProductView.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
}
