package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DataManagement.*;
@WebServlet("/CheckoutController")
public class CheckoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    public CheckoutController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        prodottoDAO = new ProdottoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Verifica se l'utente è autenticato
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("AuthSites/Login.jsp");
            return;
        }
        
        ClienteBean user = (ClienteBean) session.getAttribute("user");
        request.setAttribute("user", user);
        
        // Recupera il carrello dalla sessione
        Cart cart = (Cart) session.getAttribute("cart");
        
        // Se il carrello non esiste o è vuoto, reindirizza al carrello
        if (cart == null || cart.getProducts().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }
        
        try {
            // Ottieni i dettagli dei prodotti nel carrello
            List<ProdottoBean> cartItems = new ArrayList<>();
            Map<Integer, Integer> quantities = new HashMap<>();
            double totalAmount = 0.0;
            
            // Recupera tutti i prodotti nel carrello e le relative quantità
            for (ProdottoBean item : cart.getProducts()) {
                int productId = item.getIdProdotto();
                int quantity = cart.getQuantity(productId);
                
                // Recupera informazioni aggiornate sul prodotto
                ProdottoBean product = prodottoDAO.doRetrieveByKey(productId);
                if (product != null) {
                    cartItems.add(product);
                    quantities.put(productId, quantity);
                    totalAmount += product.getPrezzoBase() * quantity;
                }
            }
            
            // Passa i dati alla JSP
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("quantities", quantities);
            request.setAttribute("totalAmount", totalAmount);
            
            // Forward alla pagina JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/500.html");
            dispatcher.forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
    
}