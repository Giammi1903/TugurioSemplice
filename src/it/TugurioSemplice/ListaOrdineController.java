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
import javax.servlet.http.HttpSession;
import DataManagement.*;

@WebServlet("/ListaOrdineController")
public class ListaOrdineController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO model = new OrdineDAO();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        // Verifica sessione e autenticazione
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/AuthSites/Login.jsp");
            return;
        }
        
        try {
            ClienteBean user = (ClienteBean) session.getAttribute("user");
            int idClienteParam = Integer.parseInt(request.getParameter("idCliente"));
            
            // Verifica che l'idCliente richiesto corrisponda all'utente loggato
            if (user.getIdCliente() != idClienteParam) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
                return;
            }
            
            // Ottieni tutti gli ordini dell'utente
            ArrayList<OrdineBean> listaOrdini = model.doRetrieveByCliente(idClienteParam);
            
            // Per ogni ordine, carica i dettagli completi
            ArrayList<OrdineBean> ordiniCompleti = new ArrayList<>();
            for (OrdineBean ordine : listaOrdini) {
                // Carica l'ordine completo con tutti i dettagli (prodotti, pagamento, spedizione)
                OrdineBean ordineCompleto = model.doRetrieveByKey(ordine.getIdOrdine());
                ordiniCompleti.add(ordineCompleto);
            }
            
            session.setAttribute("listaOrdini", ordiniCompleti);
            
            // Reindirizzamento a pagina di visualizzazione ordini
            RequestDispatcher dispatcher = request.getRequestDispatcher("/AuthSites/OrdiniUtente.jsp");
            dispatcher.forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID cliente non valido");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Errore durante il caricamento degli ordini: " + e.getMessage());
            request.getRequestDispatcher("/500.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Si è verificato un errore imprevisto: " + e.getMessage());
            request.getRequestDispatcher("/500.jsp").forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Per debug potresti voler permettere il GET in sviluppo
        if ("true".equals(request.getServletContext().getInitParameter("debug"))) {
            doPost(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method not supported");
        }
    }
}