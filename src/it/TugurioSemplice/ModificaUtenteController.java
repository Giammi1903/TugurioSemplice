package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DataManagement.ClienteBean;
import DataManagement.ClienteDAO;
import DataManagement.DatiAnagraficiBean;
import DataManagement.IndirizzoSpedizioneBean;

@WebServlet("/ModificaUtenteController")
public class ModificaUtenteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public ModificaUtenteController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ClienteBean currentUser = (ClienteBean) session.getAttribute("user");
        
        // Verificare che l'utente sia loggato
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/AuthSites/Login.jsp");
            return;
        }
        
        // Recuperare i dati dal form
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Dati anagrafici
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String codiceFiscale = request.getParameter("codiceFiscale");
        String telefono = request.getParameter("telefono");
        
        // Indirizzo di spedizione
        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        
        // Controllo che l'utente stia modificando i propri dati
        if (currentUser.getIdCliente() != idCliente) {
            response.sendRedirect(request.getContextPath() + "/error.jsp?message=Non hai i permessi per modificare questi dati");
            return;
        }
        
        // Preparare l'oggetto ClienteBean aggiornato
        ClienteBean updatedUser = new ClienteBean();
        updatedUser.setIdCliente(idCliente);
        updatedUser.setUsername(username);
        updatedUser.setEmail(email);
        
        if (password != null && !password.trim().isEmpty()) {
            updatedUser.setPasskey(password);
        } else {
            updatedUser.setPasskey(currentUser.getPasskey());
        }
        updatedUser.setIsAmministratore(currentUser.getIsAmministratore());
        
        // Aggiornare i dati anagrafici
        DatiAnagraficiBean datiAnagrafici = new DatiAnagraficiBean();
        datiAnagrafici.setNome(nome);
        datiAnagrafici.setCognome(cognome);
        datiAnagrafici.setCf(codiceFiscale);
        datiAnagrafici.setTelefono(telefono);
        updatedUser.setAnagrafia(datiAnagrafici);
        
        // Aggiornare l'indirizzo di spedizione
        IndirizzoSpedizioneBean indirizzo = new IndirizzoSpedizioneBean();
        indirizzo.setVia(via); // Assume che la colonna Via contenga sia via che civico
        indirizzo.setCitta(citta);
        indirizzo.setCap(cap);
        indirizzo.setProvincia(provincia);
        updatedUser.setIndirizzoSpedizione(indirizzo);
        
        // Salvare i dati nel database
        ClienteDAO clienteDAO = new ClienteDAO();
        try {
            clienteDAO.doUpdate(updatedUser);
            
            // Aggiornare la sessione con i nuovi dati
            session.setAttribute("user", updatedUser);
            
            // Reindirizzare alla pagina di area utente con un messaggio di successo
            response.sendRedirect(request.getContextPath() + "/AreaUtente.jsp?message=Dati aggiornati con successo");
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Reindirizzare a una pagina di errore
            response.sendRedirect(request.getContextPath() + "/500.jsp?message=Errore durante l'aggiornamento dei dati: " + e.getMessage());
        } finally {
            clienteDAO.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Reindirizzare alla pagina di modifica dati utente
        response.sendRedirect(request.getContextPath() + "/AreaUtente.jsp");
    }
}
