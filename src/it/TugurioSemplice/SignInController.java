package it.TugurioSemplice;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import DataManagement.*;

@WebServlet("/SignInController")
public class SignInController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static ClienteDAO model = new ClienteDAO();
    
    public SignInController() {
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
    	String username = request.getParameter("username");
    	String email = request.getParameter("email");
    	String passkey = toHash(request.getParameter("passkey"));
    	String passkey2 = toHash(request.getParameter("passkey2"));
    	String nome = request.getParameter("nome");
    	String cognome = request.getParameter("cognome");
    	String cf = request.getParameter("cf");
    	String tel = request.getParameter("telefono");
    	String city = request.getParameter("city");
    	String CAP = request.getParameter("CAP");
    	String street = request.getParameter("street");
    	String Provincia = request.getParameter("provincia");
    	
    	if(passkey.equals(passkey2)) {	
	    	DatiAnagraficiBean dati = new DatiAnagraficiBean();
	    	dati.setNome(nome);
	    	dati.setCognome(cognome);
	    	dati.setCf(cf);
	    	dati.setTelefono(tel);
	    	
	    	IndirizzoSpedizioneBean spedizione = new IndirizzoSpedizioneBean();
	    	spedizione.setCitta(city);
	    	spedizione.setCap(CAP);
	    	spedizione.setProvincia(Provincia);
	    	spedizione.setVia(street);
	    	
	    	ClienteBean cliente = new ClienteBean();
	    	cliente.setUsername(username);
	    	cliente.setIsAmministratore(false);
	    	cliente.setEmail(email);
	    	cliente.setPasskey(passkey);
	    	cliente.setAnagrafia(dati);
	    	cliente.setIndirizzoSpedizione(spedizione);
    	
			try {
				model.doSave(cliente);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error:" + e.getMessage());
				request.setAttribute("error", "Si è verificato un errore nel database: " + e.getMessage());
			}
			
			getServletContext().getRequestDispatcher("/AuthSites/SignInSuccess.html")
			.forward(request, response);
    	} else {
            // Credenziali non valide
            request.setAttribute("error", "Le due password non coincidono");
            RequestDispatcher dispatcher = request.getRequestDispatcher("./AuthSites/SignIn.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
