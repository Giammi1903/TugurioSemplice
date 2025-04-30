package it.TugurioSemplice;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import DataManagement.*;

@WebServlet("/RecuperoController")
public class RecuperoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public RecuperoController() {
        super();
    }
    
    private String toHash(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e);
            return null;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usemail = request.getParameter("usemail");
        String password = toHash(request.getParameter("NewPassword"));
        String password2 = toHash(request.getParameter("NewPassword2"));
        
        ClienteDAO clienteDAO = null;
        
        try {
            clienteDAO = new ClienteDAO();
            ClienteBean user = null;
            
            // Controllo se è email o username e recupero l'utente
            if (usemail != null && usemail.contains("@")) {
                user = clienteDAO.doRetrieveByEmail(usemail); // Recupera per email
            } else if (usemail != null && !usemail.isEmpty()) {
                user = clienteDAO.doRetrieveByUsername(usemail); // Recupera per username
            }
            
            // Verifica se l'utente esiste
            if (user != null && password.equals(password2)) {
            	user.setPasskey(password);
            	clienteDAO.doUpdate(user);
            } else {
                // Credenziali non valide
                request.setAttribute("error", "Utente non trovato");
                RequestDispatcher dispatcher = request.getRequestDispatcher("./AuthSites/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            // Gestione degli errori nel caso di problemi con il database
            System.err.println("Errore SQL durante il login: " + e.getMessage());
            e.printStackTrace(); 
            request.setAttribute("error", "Errore durante il recupero. Riprova più tardi.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("./AuthSites/Login.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (clienteDAO != null) {
                clienteDAO.close();
            }
        }
    }
    
    // Metodo per gestire la richiesta GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("./AutSites/Login.jsp"); // In caso di richiesta GET, rimanda al login
    }
}
