package DataManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdineDAO extends DAO<OrdineBean> {

	public OrdineDAO() {
		super();
	}

	@Override
	public void doSave(OrdineBean ordine) throws SQLException {
		String sql = "INSERT INTO ordine \r\n" +
				"(IdCliente, Prezzo_Ordine, Stato_Ordine) \r\n" +
				"VALUES \r\n" +
				"(?, ?, ?);";
		PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setInt(1, ordine.getIdCliente());
		stmt.setDouble(2, ordine.getPrezzoOrdine());
		stmt.setString(3, ordine.getStatoOrdine());

		stmt.executeUpdate();
		
		ResultSet generatedKeys = stmt.getGeneratedKeys();
		if (generatedKeys.next()) {
			int orderId = generatedKeys.getInt(1);
			
			insertProducts(orderId, ordine.getProdotti());
			
			if (ordine.getPagamento() != null) {
				insertPayment(orderId, ordine.getPagamento());
			}
			
			if (ordine.getSpedizione() != null) {
				insertShipping(orderId, ordine.getSpedizione());
			}
		}
	}

	private void insertProducts(int orderId, ArrayList<ProdottoBean> products) throws SQLException {
	    String sql = "INSERT INTO Inserito (IdOrdine, IdProdotto, Prezzo, Quantita) VALUES (?, ?, ?, ?)";
	    
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    for (ProdottoBean product : products) {
	        stmt.setInt(1, orderId);
	        stmt.setInt(2, product.getIdProdotto());
	        // Convert to int as expected by the Inserito table
	        stmt.setInt(3, (int)product.getPrezzoBase());
	        // Use the quantity attribute from the ProdottoBean
	        stmt.setInt(4, product.getQuantita());
	        stmt.executeUpdate();
	    }
	}
	
	private void insertPayment(int orderId, PagamentoBean payment) throws SQLException {
		String sql = "INSERT INTO Pagamento (ID_Ordine, Importo, Metodo) VALUES (?, ?, ?)";
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, orderId);
		stmt.setDouble(2, payment.getImporto());
		stmt.setString(3, payment.getMetodo());
		stmt.executeUpdate();
	}
	
	private void insertShipping(int orderId, SpedizioneBean shipping) throws SQLException {
		String sql = "INSERT INTO Spedizione (ID_Ordine, Spese, Metodo) VALUES (?, ?, ?)";
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, orderId);
		stmt.setDouble(2, shipping.getSpese());
		stmt.setString(3, shipping.getMetodo());
		stmt.executeUpdate();
	}

	@Override
	public void doUpdate(OrdineBean item) throws SQLException {
		String sql = "UPDATE ordine \r\n" +
				"SET Data_Ordine = ?, \r\n" +
				"    Prezzo_Ordine = ?, \r\n" +
				"    Stato_Ordine = ? \r\n" +
				"WHERE ID_Ordine = ?;";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, item.getDataOrdine());
		stmt.setDouble(2, item.getPrezzoOrdine());
		stmt.setString(3, item.getStatoOrdine());
		stmt.setInt(4, item.getIdOrdine());

		stmt.executeUpdate();
	}

	@Override
	public boolean doDelete(int id) throws SQLException {
		String sql1 = "DELETE FROM inserito\r\n" +
				"WHERE IdOrdine = ?;";

		PreparedStatement stmt1 = connection.prepareStatement(sql1);
		stmt1.setInt(1, id);
		stmt1.executeUpdate();
		
		String sql2 = "DELETE FROM pagamento\r\n" +
				"WHERE ID_Ordine = ?;";

		PreparedStatement stmt2 = connection.prepareStatement(sql2);
		stmt2.setInt(1, id);
		stmt2.executeUpdate();
		
		String sql3 = "DELETE FROM spedizione\r\n" +
				"WHERE ID_Ordine = ?;";

		PreparedStatement stmt3 = connection.prepareStatement(sql3);
		stmt3.setInt(1, id);
		stmt3.executeUpdate();

		String sql4 = "DELETE FROM ordine\r\n" +
				"WHERE ID_Ordine = ?;";

		PreparedStatement stmt4 = connection.prepareStatement(sql4);
		stmt4.setInt(1, id);
		int result = stmt4.executeUpdate();

		return result > 0;
	}

	@Override
	public OrdineBean doRetrieveByKey(int id) throws SQLException {
		String sql = "SELECT * FROM ordine \r\n" +
				"WHERE ID_Ordine = ?;";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();

		OrdineBean ordine = new OrdineBean();

		if (rs.next()) {
			ordine.setIdOrdine(rs.getInt("ID_Ordine"));
			ordine.setIdCliente(rs.getInt("IdCliente"));
			ordine.setDataOrdine(rs.getString("Data_Ordine"));
			ordine.setPrezzoOrdine(rs.getDouble("Prezzo_Ordine"));
			ordine.setStatoOrdine(rs.getString("Stato_Ordine"));
			
			ordine.setProdotti(retrieveOrderProducts(id));
			
			PagamentoBean pagamento = retrievePayment(id);
			if (pagamento != null) {
				ordine.setPagamento(pagamento);
			}
			
			SpedizioneBean spedizione = retrieveShipping(id);
			if (spedizione != null) {
				ordine.setSpedizione(spedizione);
			}
		}
		
		return ordine;
	}
	
	private ArrayList<ProdottoBean> retrieveOrderProducts(int orderId) throws SQLException {
		String sql = "SELECT p.*, i.Quantita as OrderQuantita FROM prodotto p " +
				"JOIN inserito i ON p.ID_Prodotto = i.IdProdotto " +
				"WHERE i.IdOrdine = ?";
				
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, orderId);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<ProdottoBean> products = new ArrayList<>();
		
		while (rs.next()) {
			ProdottoBean prodotto = new ProdottoBean();
			prodotto.setIdProdotto(rs.getInt("ID_Prodotto"));
			prodotto.setNome(rs.getString("Nome"));
			prodotto.setDescrizione(rs.getString("Descrizione"));
			prodotto.setQuantita(rs.getInt("OrderQuantita"));
			prodotto.setDisponibilita(rs.getBoolean("Disponibilita"));
			prodotto.setPrezzoBase(rs.getDouble("Prezzo_Base"));
			prodotto.setIva(rs.getDouble("Iva"));
			prodotto.setImmagine(rs.getString("Immagine"));
			
			products.add(prodotto);
		}
		
		return products;
	}
	
	public PagamentoBean retrievePayment(int orderId) throws SQLException {
		String sql = "SELECT * FROM pagamento WHERE ID_Ordine = ?";
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, orderId);
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			PagamentoBean pagamento = new PagamentoBean();
			pagamento.setIdOrdine(rs.getInt("ID_Ordine"));
			pagamento.setImporto(rs.getDouble("Importo"));
			pagamento.setDataPagamento(rs.getDate("Data_Pagamento"));
			pagamento.setMetodo(rs.getString("Metodo"));
			
			return pagamento;
		}
		
		return null;
	}
	
	public SpedizioneBean retrieveShipping(int orderId) throws SQLException {
		String sql = "SELECT * FROM spedizione WHERE ID_Ordine = ?";
		
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, orderId);
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			SpedizioneBean spedizione = new SpedizioneBean();
			spedizione.setIdOrdine(rs.getInt("ID_Ordine"));
			spedizione.setSpese(rs.getDouble("Spese"));
			spedizione.setDataConsegna(rs.getDate("Data_Consegna"));
			spedizione.setMetodo(rs.getString("Metodo"));
			
			return spedizione;
		}
		
		return null;
	}

	@Override
	public ArrayList<OrdineBean> doRetrieveAll(String ordine) throws SQLException {
		String sql = "SELECT * FROM ordine";

		if (ordine != null && !ordine.trim().isEmpty()) {
			if (ordine.equals("Data_Ordine")) {
				sql += " ORDER BY Data_Ordine";
			} else if (ordine.equals("Prezzo_Ordine ASC")) {
				sql += " ORDER BY Prezzo_Ordine ASC";
			} else if (ordine.equals("Prezzo_Ordine DESC")) {
				sql += " ORDER BY Prezzo_Ordine DESC";
			} else {
				sql += " ORDER BY ID_Ordine DESC";
			}
		} else {
			sql += " ORDER BY ID_Ordine DESC";
		}

		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ArrayList<OrdineBean> listaOrdini = new ArrayList<OrdineBean>();

		while (rs.next()) {
			int id = rs.getInt("ID_Ordine");
			int idCliente = rs.getInt("IdCliente");
			String dataOrdine = rs.getString("Data_Ordine");
			double prezzoOrdine = rs.getDouble("Prezzo_Ordine");
			String statoOrdine = rs.getString("Stato_Ordine");

			OrdineBean ordineb = new OrdineBean();
			ordineb.setIdOrdine(id);
			ordineb.setIdCliente(idCliente);
			ordineb.setDataOrdine(dataOrdine);
			ordineb.setPrezzoOrdine(prezzoOrdine);
			ordineb.setStatoOrdine(statoOrdine);
			
			listaOrdini.add(ordineb);
		}
		return listaOrdini;
	}

	public ArrayList<OrdineBean> doRetrieveByCliente(int idCliente) throws SQLException {
		String sql = "SELECT * FROM ordine WHERE IdCliente = ? ORDER BY Data_Ordine DESC";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, idCliente);
		ResultSet rs = stmt.executeQuery();
		ArrayList<OrdineBean> listaOrdini = new ArrayList<OrdineBean>();

		while (rs.next()) {
			int id = rs.getInt("ID_Ordine");
			String dataOrdine = rs.getString("Data_Ordine");
			double prezzoOrdine = rs.getDouble("Prezzo_Ordine");
			String statoOrdine = rs.getString("Stato_Ordine");

			OrdineBean ordine = new OrdineBean();
			ordine.setIdOrdine(id);
			ordine.setIdCliente(idCliente);
			ordine.setDataOrdine(dataOrdine);
			ordine.setPrezzoOrdine(prezzoOrdine);
			ordine.setStatoOrdine(statoOrdine);
			
			listaOrdini.add(ordine);
		}
		return listaOrdini;
	}
	
	public ArrayList<OrdineBean> doRetrieveFiltered(String statoFiltro, String dataInizioFiltro, String dataFineFiltro, Double prezzoMin, Double prezzoMax, Integer idClienteFiltro, String ordine) throws SQLException {
		String sql = "SELECT * FROM ordine WHERE 1=1";

		if (statoFiltro != null && !statoFiltro.trim().isEmpty()) {
			sql += " AND Stato_Ordine = ?";
		}

		if (dataInizioFiltro != null && !dataInizioFiltro.trim().isEmpty()) {
			sql += " AND Data_Ordine >= ?";
		}

		if (dataFineFiltro != null && !dataFineFiltro.trim().isEmpty()) {
			sql += " AND Data_Ordine <= ?";
		}

		if (prezzoMin != null) {
			sql += " AND Prezzo_Ordine >= ?";
		}

		if (prezzoMax != null) {
			sql += " AND Prezzo_Ordine <= ?";
		}
		
		if (idClienteFiltro != null) {
			sql += " AND IdCliente = ?";
		}

		if (ordine != null && !ordine.trim().isEmpty()) {
			if (ordine.equals("Data_Ordine ASC")) {
				sql += " ORDER BY Data_Ordine ASC";
			} else if (ordine.equals("Data_Ordine DESC")) {
				sql += " ORDER BY Data_Ordine DESC";
			} else if (ordine.equals("Prezzo_Ordine ASC")) {
				sql += " ORDER BY Prezzo_Ordine ASC";
			} else if (ordine.equals("Prezzo_Ordine DESC")) {
				sql += " ORDER BY Prezzo_Ordine DESC";
			} else {
				sql += " ORDER BY ID_Ordine DESC";
			}
		} else {
			sql += " ORDER BY ID_Ordine DESC";
		}

		PreparedStatement stmt = connection.prepareStatement(sql);
		int parameterIndex = 1;

		if (statoFiltro != null && !statoFiltro.trim().isEmpty()) {
			stmt.setString(parameterIndex++, statoFiltro);
		}

		if (dataInizioFiltro != null && !dataInizioFiltro.trim().isEmpty()) {
			stmt.setString(parameterIndex++, dataInizioFiltro);
		}

		if (dataFineFiltro != null && !dataFineFiltro.trim().isEmpty()) {
			stmt.setString(parameterIndex++, dataFineFiltro);
		}

		if (prezzoMin != null) {
			stmt.setDouble(parameterIndex++, prezzoMin);
		}

		if (prezzoMax != null) {
			stmt.setDouble(parameterIndex++, prezzoMax);
		}
		
		if (idClienteFiltro != null) {
			stmt.setInt(parameterIndex++, idClienteFiltro);
		}

		ResultSet rs = stmt.executeQuery();
		ArrayList<OrdineBean> listaOrdini = new ArrayList<>();

		while (rs.next()) {
			int id = rs.getInt("ID_Ordine");
			int idCliente = rs.getInt("IdCliente");
			String dataOrdine = rs.getString("Data_Ordine");
			double prezzoOrdine = rs.getDouble("Prezzo_Ordine");
			String statoOrdine = rs.getString("Stato_Ordine");

			OrdineBean ordineb = new OrdineBean();
			ordineb.setIdOrdine(id);
			ordineb.setIdCliente(idCliente);
			ordineb.setDataOrdine(dataOrdine);
			ordineb.setPrezzoOrdine(prezzoOrdine);
			ordineb.setStatoOrdine(statoOrdine);
			
			listaOrdini.add(ordineb);
		}

		return listaOrdini;
	}
	
	public ArrayList<Map<String, Object>> getDettagliOrdine(int idOrdine) throws SQLException {
	    ArrayList<Map<String, Object>> dettagli = new ArrayList<>();
	    
	    String query = "SELECT p.Nome, i.Quantita, i.Prezzo " +
	                   "FROM Inserito i " +
	                   "JOIN Prodotto p ON i.IdProdotto = p.ID_Prodotto " +
	                   "WHERE i.IdOrdine = ?";
	    
	    PreparedStatement ps = connection.prepareStatement(query); 
	    ps.setInt(1, idOrdine);
	    try (ResultSet rs = ps.executeQuery()) {
		    while (rs.next()) {
		    	Map<String, Object> dettaglio = new HashMap<>();
		        dettaglio.put("nomeProdotto", rs.getString("Nome"));
		        dettaglio.put("quantita", rs.getInt("Quantita"));
		        dettaglio.put("prezzo", rs.getDouble("Prezzo"));
		        dettagli.add(dettaglio);
		    }
	    }
	    return dettagli;
	}
}