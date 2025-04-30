package DataManagement;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO extends DAO<ClienteBean> {

    public ClienteDAO() {
        super();
    }

    @Override
    public void doSave(ClienteBean cliente) throws SQLException {
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        
        try {
            String sql1 = "INSERT INTO cliente \r\n" + 
                    "(Username, Email, Passkey, Amministratore) \r\n" + 
                    "VALUES \r\n" + 
                    "(?, ?, ?, ?);";
            stmt1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1, cliente.getUsername());
            stmt1.setString(2, cliente.getEmail());
            stmt1.setString(3, cliente.getPasskey());
            stmt1.setBoolean(4, cliente.getIsAmministratore());
            
            stmt1.executeUpdate();
         // Recupera l'ID generato automaticamente
            ResultSet generatedKeys = stmt1.getGeneratedKeys();
            if (generatedKeys.next()) {
                cliente.setIdCliente(generatedKeys.getInt(1)); // Imposta l'ID nell'oggetto ClienteBean
            }
            
            String sql2 = "INSERT INTO dati_anagrafici \r\n" + 
                    "(IdCliente, Nome, Cognome, CF, Telefono) \r\n" + 
                    "VALUES \r\n" + 
                    "(?, ?, ?, ?, ?);";
            stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, cliente.getIdCliente());
            stmt2.setString(2, cliente.getAnagrafia().getNome());
            stmt2.setString(3, cliente.getAnagrafia().getCognome());
            stmt2.setString(4, cliente.getAnagrafia().getCf());
            stmt2.setString(5, cliente.getAnagrafia().getTelefono());
            
            stmt2.executeUpdate();
            
            String sql3 = "INSERT INTO indirizzo_spedizione \r\n" +
            		"(Via, CAP, Citta, Provincia, IdCliente) \r\n" +
            		"VALUES \r\n" +
            		"(?, ?, ?, ?, ?);";
            stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, cliente.getIndirizzoSpedizione().getVia());
            stmt3.setString(2, cliente.getIndirizzoSpedizione().getCap());
            stmt3.setString(3, cliente.getIndirizzoSpedizione().getCitta());
            stmt3.setString(4, cliente.getIndirizzoSpedizione().getProvincia());
            stmt3.setInt(5, cliente.getIdCliente());
            
            stmt3.executeUpdate();
            
        } finally {
            if (stmt1 != null) try { stmt1.close(); } catch (SQLException e) { }
            if (stmt2 != null) try { stmt2.close(); } catch (SQLException e) { }
            if (stmt3 != null) try { stmt3.close(); } catch (SQLException e) { }
        }
    }
    
    @Override
    public void doUpdate(ClienteBean item) throws SQLException {
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        try {
            String sql1 = "UPDATE cliente \r\n" +  
                    "SET Username = ?, \r\n" + 
                    "    Email = ?, \r\n" + 
                    "    Passkey = ?, \r\n" + 
                    "    Amministratore = ? \r\n" +
                    "WHERE ID_Cliente = ?;";
            
            stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, item.getUsername());
            stmt1.setString(2, item.getEmail());
            stmt1.setString(3, item.getPasskey());
            stmt1.setBoolean(4, item.getIsAmministratore());
            stmt1.setInt(5, item.getIdCliente());
            
            stmt1.executeUpdate();
            
            String sql2 = "UPDATE dati_anagrafici \r\n" + 
                    "SET Nome = ?, \r\n" + 
                    "    Cognome = ?, \r\n" + 
                    "    CF = ?, \r\n" + 
                    "    Telefono = ? \r\n" +
                    "WHERE IDCliente = ?;";
            
            stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, item.getAnagrafia().getNome());
            stmt2.setString(2, item.getAnagrafia().getCognome());
            stmt2.setString(3, item.getAnagrafia().getCf());
            stmt2.setString(4, item.getAnagrafia().getTelefono());
            stmt2.setInt(5, item.getIdCliente());
            
            stmt2.executeUpdate();
            
            String sql3 = "UPDATE indirizzo_spedizione \r\n" + 
                    "SET Via = ?, \r\n" + 
                    "    CAP = ?, \r\n" + 
                    "    Citta = ?, \r\n" + 
                    "    Provincia = ? \r\n" +
                    "WHERE IDCliente = ?;";
            
            stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, item.getIndirizzoSpedizione().getVia());
            stmt3.setString(2, item.getIndirizzoSpedizione().getCap());
            stmt3.setString(3, item.getIndirizzoSpedizione().getCitta());
            stmt3.setString(4, item.getIndirizzoSpedizione().getProvincia());
            stmt3.setInt(5, item.getIdCliente());
            
            stmt3.executeUpdate();
            
        } finally {
            if (stmt1 != null) try { stmt1.close(); } catch (SQLException e) { }
            if (stmt2 != null) try { stmt2.close(); } catch (SQLException e) { }
            if (stmt3 != null) try { stmt3.close(); } catch (SQLException e) { }
        }
    }

    @Override
    public boolean doDelete(int id) throws SQLException {
        PreparedStatement stmt = null;
        
        try {
            String sql = "DELETE FROM cliente\r\n" + 
                    "WHERE ID_Cliente = ?;";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            
            return result > 0;
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }

    @Override
    public ClienteBean doRetrieveByKey(int id) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM cliente JOIN dati_anagrafici ON cliente.ID_Cliente = dati_anagrafici.IdCliente JOIN indirizzo_spedizione ON cliente.ID_Cliente = indirizzo_spedizione.IdCliente\r\n" + 
                    "WHERE ID_Cliente = ?;";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            ClienteBean cliente = null;
            
            if(rs.next()) {
                cliente = new ClienteBean();
                cliente.setIdCliente(rs.getInt("ID_Cliente"));
                cliente.setUsername(rs.getString("Username"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setPasskey(rs.getString("Passkey"));
                cliente.setIsAmministratore(rs.getBoolean("Amministratore"));
                
                cliente.setAnagrafia(new DatiAnagraficiBean());
                cliente.getAnagrafia().setCf(rs.getString("CF"));
                cliente.getAnagrafia().setNome(rs.getString("Nome"));
                cliente.getAnagrafia().setCognome(rs.getString("Cognome"));
                cliente.getAnagrafia().setTelefono(rs.getString("Telefono"));
                
                cliente.setIndirizzoSpedizione(new IndirizzoSpedizioneBean());
                cliente.getIndirizzoSpedizione().setCap(rs.getString("CAP"));
                cliente.getIndirizzoSpedizione().setCitta(rs.getString("Citta"));
                cliente.getIndirizzoSpedizione().setProvincia(rs.getString("Provincia"));
                cliente.getIndirizzoSpedizione().setVia(rs.getString("Via"));
            }
            return cliente;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }
    
    public ClienteBean doRetrieveByUsername(String username) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM cliente JOIN dati_anagrafici ON cliente.ID_Cliente = dati_anagrafici.IdCliente JOIN indirizzo_spedizione ON cliente.ID_Cliente = indirizzo_spedizione.IdCliente " +
                    "WHERE Username = ?;";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            ClienteBean cliente = null;

            if(rs.next()) {
                cliente = new ClienteBean();
                cliente.setIdCliente(rs.getInt("ID_Cliente"));
                cliente.setUsername(rs.getString("Username"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setPasskey(rs.getString("Passkey"));
                cliente.setIsAmministratore(rs.getBoolean("Amministratore"));

                cliente.setAnagrafia(new DatiAnagraficiBean());
                cliente.getAnagrafia().setCf(rs.getString("CF"));
                cliente.getAnagrafia().setNome(rs.getString("Nome"));
                cliente.getAnagrafia().setCognome(rs.getString("Cognome"));
                cliente.getAnagrafia().setTelefono(rs.getString("Telefono"));
                
                cliente.setIndirizzoSpedizione(new IndirizzoSpedizioneBean());
                cliente.getIndirizzoSpedizione().setCap(rs.getString("Cap"));
                cliente.getIndirizzoSpedizione().setCitta(rs.getString("Citta"));
                cliente.getIndirizzoSpedizione().setProvincia(rs.getString("Provincia"));
                cliente.getIndirizzoSpedizione().setVia(rs.getString("Via"));
            }
            return cliente;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }

    public ClienteBean doRetrieveByEmail(String email) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM cliente JOIN dati_anagrafici ON cliente.ID_Cliente = dati_anagrafici.IdCliente JOIN indirizzo_spedizione ON cliente.ID_Cliente = indirizzo_spedizione.IdCliente" +
                    "WHERE Email = ?;";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            ClienteBean cliente = null;
            
            if(rs.next()) {
                cliente = new ClienteBean();
                cliente.setIdCliente(rs.getInt("ID_Cliente"));
                cliente.setUsername(rs.getString("Username"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setPasskey(rs.getString("Passkey"));
                cliente.setIsAmministratore(rs.getBoolean("Amministratore"));

                cliente.setAnagrafia(new DatiAnagraficiBean());
                cliente.getAnagrafia().setCf(rs.getString("CF"));
                cliente.getAnagrafia().setNome(rs.getString("Nome"));
                cliente.getAnagrafia().setCognome(rs.getString("Cognome"));
                cliente.getAnagrafia().setTelefono(rs.getString("Telefono"));
                
                cliente.setIndirizzoSpedizione(new IndirizzoSpedizioneBean());
                cliente.getIndirizzoSpedizione().setCap(rs.getString("Cap"));
                cliente.getIndirizzoSpedizione().setCitta(rs.getString("Citta"));
                cliente.getIndirizzoSpedizione().setProvincia(rs.getString("Provincia"));
                cliente.getIndirizzoSpedizione().setVia(rs.getString("Via"));
            }
            return cliente;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }

    @Override
    public ArrayList<ClienteBean> doRetrieveAll(String ordine) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM cliente JOIN dati_anagrafici ON cliente.ID_Cliente = dati_anagrafici.IdCliente JOIN indirizzo_spedizione ON cliente.ID_Cliente = indirizzo_spedizione.IdCliente";
            
            if (ordine != null && !ordine.isEmpty()) {
                sql += " ORDER BY " + ordine;
            }
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            ArrayList<ClienteBean> listaClienti = new ArrayList<ClienteBean>();
            
            while(rs.next()) {
                ClienteBean cliente = new ClienteBean();
                    
                cliente.setIdCliente(rs.getInt("ID_Cliente"));
                cliente.setUsername(rs.getString("Username"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setPasskey(rs.getString("Passkey"));
                cliente.setIsAmministratore(rs.getBoolean("Amministratore"));
                
                DatiAnagraficiBean anagrafia = new DatiAnagraficiBean();
                anagrafia.setNome(rs.getString("Nome"));
                anagrafia.setCognome(rs.getString("Cognome"));
                anagrafia.setCf(rs.getString("CF"));
                anagrafia.setTelefono(rs.getString("Telefono"));
                
                
                cliente.setAnagrafia(anagrafia);
                
                IndirizzoSpedizioneBean indirizzoSpedizione = new IndirizzoSpedizioneBean();
                indirizzoSpedizione.setCap(rs.getString("CAP"));
                indirizzoSpedizione.setCitta(rs.getString("Citta"));
                indirizzoSpedizione.setProvincia(rs.getString("Provincia"));
                indirizzoSpedizione.setVia(rs.getString("Via"));
                
                listaClienti.add(cliente);
            }
            return listaClienti;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { }
        }
    }
}