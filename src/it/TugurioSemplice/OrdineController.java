package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DataManagement.*;

@WebServlet("/OrdineController")
public class OrdineController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static OrdineDAO model = new OrdineDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verifica sessione e autenticazione
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/AuthSites/Login.jsp");
            return;
        }
        
        // Verifica presenza carrello
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getProducts().isEmpty()) {
            request.setAttribute("error", "Il carrello è vuoto");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }
        
        try {
            ClienteBean user = (ClienteBean) session.getAttribute("user");
            String metodo = request.getParameter("spedizione");
            String metodoP = request.getParameter("pagamento");
            
            // Creazione spedizione bean
            SpedizioneBean spedizione = new SpedizioneBean();
            spedizione.setMetodo(metodo);
            spedizione.setSpese(50.00);
            
            // Creazione pagamento bean
            PagamentoBean pagamento = new PagamentoBean();
            pagamento.setImporto(cart.getTotalPrice() + 50.00);
            pagamento.setMetodo(metodoP);
            
            // Creazione e configurazione dell'ordine
            OrdineBean ordine = new OrdineBean();
            ordine.setIdCliente(user.getIdCliente());
            ordine.setSpedizione(spedizione);
            ordine.setPagamento(pagamento);
            ordine.setStatoOrdine("non consegnato");
            ordine.setPrezzoOrdine(cart.getTotalPrice());
            
            // Preparazione prodotti per l'ordine
            ArrayList<ProdottoBean> orderProducts = new ArrayList<>();
            for (ProdottoBean prodotto : cart.getProducts()) {
                // Creiamo una copia del prodotto per l'ordine
                ProdottoBean orderProduct = new ProdottoBean();
                orderProduct.setIdProdotto(prodotto.getIdProdotto());
                orderProduct.setNome(prodotto.getNome());
                orderProduct.setDescrizione(prodotto.getDescrizione());
                orderProduct.setPrezzoBase(prodotto.getPrezzoBase());
                orderProduct.setIva(prodotto.getIva());
                orderProduct.setImmagine(prodotto.getImmagine());
                
                // Impostiamo la quantità dal carrello
                int cartQuantity = cart.getQuantity(prodotto.getIdProdotto());
                orderProduct.setQuantita(cartQuantity);
                
                orderProducts.add(orderProduct);
            }
            
            // Assegnazione prodotti all'ordine
            ordine.setProdotti(orderProducts);
            
            // Impostazione data consegna
            Date oggi = new Date();
            Date dataConsegna = new Date(oggi.getTime() + (7L * 24 * 60 * 60 * 1000));
            ordine.getSpedizione().setDataConsegna(dataConsegna);
            
            // Aggiornamento scorte prodotti nel database
            ProdottoDAO prodottoDAO = new ProdottoDAO();
            for (ProdottoBean prodotto : cart.getProducts()) {
                // Otteniamo il prodotto corrente dal database
                ProdottoBean dbProduct = prodottoDAO.doRetrieveByKey(prodotto.getIdProdotto());
                
                // Riduciamo la quantità disponibile
                int newQuantity = dbProduct.getQuantita() - cart.getQuantity(prodotto.getIdProdotto());
                dbProduct.setQuantita(newQuantity);
                
                // Aggiorniamo disponibilità se necessario
                if (newQuantity <= 0) {
                    dbProduct.setDisponibilita(false);
                }
                
                // Salviamo l'aggiornamento nel database
                prodottoDAO.doUpdate(dbProduct);
            }
            
            // Salvataggio ordine nel database
            model.doSave(ordine);
            
            // Pulizia del carrello dopo l'ordine completato
            session.removeAttribute("cart");
            
            // Impostazione attributo per la pagina di conferma
            request.setAttribute("ordine", ordine);
            
            // Reindirizzamento a pagina di conferma
            request.getRequestDispatcher("/confermaOrdine.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore durante il salvataggio dell'ordine: " + e.getMessage());
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore generico: " + e.getMessage());
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method not supported");
    }
}