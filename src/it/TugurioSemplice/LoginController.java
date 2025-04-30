package it.TugurioSemplice;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import DataManagement.*;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public LoginController() {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        String usemail = request.getParameter("usemail");
        String password = toHash(request.getParameter("password"));
        
        ClienteDAO clienteDAO = null;
        
        try {
            clienteDAO = new ClienteDAO();
            ClienteBean user = null;
            
            if (usemail != null && usemail.contains("@")) {
                user = clienteDAO.doRetrieveByEmail(usemail);
            } else if (usemail != null && !usemail.isEmpty()) {
                user = clienteDAO.doRetrieveByUsername(usemail);
            }
            
            if (user != null && user.getPasskey().equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                
                // (Opzionale) Mantieni il cookie del carrello se esiste
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("carrello")) {
                        // Rinnova il cookie per evitare che scada
                        cookie.setMaxAge(7 * 24 * 60 * 60); // Es. 7 giorni
                        response.addCookie(cookie);
                        break;
                    }
                }
                
                response.sendRedirect("./ProductView.jsp");
            } else {
            	request.setAttribute("error", "Accesso fallito. Controlla le credenziali");
                RequestDispatcher dispatcher = request.getRequestDispatcher("./AuthSites/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL durante il login: " + e.getMessage());
            e.printStackTrace(); 
            request.setAttribute("error", "Errore durante il login. Riprova più tardi.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("./AuthSites/Login.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (clienteDAO != null) clienteDAO.close();
        }
    }
    
    // Metodo per gestire la richiesta GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("./AutSites/Login.jsp"); // In caso di richiesta GET, rimanda al login
    }
}
