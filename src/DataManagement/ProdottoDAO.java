package DataManagement;

import java.sql.*;
import java.util.ArrayList;

public class ProdottoDAO extends DAO<ProdottoBean> {

	public ProdottoDAO() {
		super();
	}

	@Override
	public void doSave(ProdottoBean prodotto) throws SQLException {
		String sql = "INSERT INTO prodotto \r\n" +
				"(Nome, Descrizione, Quantita, Disponibilita, Prezzo_Base, Iva, Immagine) \r\n" +
				"VALUES \r\n" +
				"(?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, prodotto.getNome());
		stmt.setString(2, prodotto.getDescrizione());
		stmt.setInt(3, prodotto.getQuantita());
		stmt.setBoolean(4, prodotto.getDisponibilita());
		stmt.setDouble(5, prodotto.getPrezzoBase());
		stmt.setDouble(6, prodotto.getIva());
		stmt.setString(7, prodotto.getImmagine());

		stmt.executeUpdate();
	}

	@Override
	public void doUpdate(ProdottoBean item) throws SQLException {
		String sql = "UPDATE prodotto \r\n" +
				"SET Nome = ?, \r\n" +
				"    Descrizione = ?, \r\n" +
				"    Quantita = ?, \r\n" +
				"    Disponibilita = ?, \r\n" +
				"    Prezzo_Base = ?, \r\n" +
				"    Iva = ?, \r\n" +
				"	 Immagine = ? \r\n" +
				"WHERE ID_Prodotto = ?;";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, item.getNome());
		stmt.setString(2, item.getDescrizione());
		stmt.setInt(3, item.getQuantita());
		stmt.setBoolean(4, item.getDisponibilita());
		stmt.setDouble(5, item.getPrezzoBase());
		stmt.setDouble(6, item.getIva());
		stmt.setString(7, item.getImmagine());
		stmt.setInt(8, item.getIdProdotto());

		stmt.executeUpdate();
	}

	@Override
	public boolean doDelete(int id) throws SQLException {
		String sql1 = "DELETE FROM acquista\r\n" +
				"WHERE IdProdotto = ?;";

		PreparedStatement stmt1 = connection.prepareStatement(sql1);
		stmt1.setInt(1, id);
		stmt1.executeUpdate();

		String sql2 = "DELETE FROM prodotto\r\n" +
				"WHERE ID_Prodotto = ?;";

		PreparedStatement stmt2 = connection.prepareStatement(sql2);
		stmt2.setInt(1, id);
		int result = stmt2.executeUpdate();

		return result > 0;
	}

	@Override
	public ProdottoBean doRetrieveByKey(int id) throws SQLException {
		String sql = "SELECT * FROM prodotto \r\n" +
				"WHERE ID_Prodotto = ?;";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();

		ProdottoBean prodotto = new ProdottoBean();
		prodotto.setIdProdotto(id);

		while (rs.next()) {
			String nome = rs.getString(2);
			String descrizione = rs.getString(3);
			int quantita = rs.getInt(4);
			boolean disponibilita = rs.getBoolean(5);
			double prezzoBase = rs.getDouble(6);
			double iva = rs.getDouble(7);
			String immagine = rs.getString(8);

			prodotto.setDescrizione(descrizione);
			prodotto.setNome(nome);
			prodotto.setQuantita(quantita);
			prodotto.setDisponibilita(disponibilita);
			prodotto.setPrezzoBase(prezzoBase);
			prodotto.setIva(iva);
			prodotto.setImmagine(immagine);
		}
		return prodotto;
	}

	@Override
	public ArrayList<ProdottoBean> doRetrieveAll(String ordine) throws SQLException {
		String sql = "SELECT * FROM prodotto";

		if (ordine != null && !ordine.trim().isEmpty()) {
			if (ordine.equals("Nome")) {
				sql += " ORDER BY Nome";
			} else if (ordine.equals("Prezzo_Base ASC")) {
				sql += " ORDER BY Prezzo_Base ASC";
			} else if (ordine.equals("Prezzo_Base DESC")) {
				sql += " ORDER BY Prezzo_Base DESC";
			} else {
				sql += " ORDER BY Nome";
			}
		}

		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ArrayList<ProdottoBean> listaProdotti = new ArrayList<ProdottoBean>();

		while (rs.next()) {
			int id = rs.getInt(1);
			String nome = rs.getString(2);
			String descrizione = rs.getString(3);
			int quantita = rs.getInt(4);
			boolean disponibilita = rs.getBoolean(5);
			double prezzoBase = rs.getDouble(6);
			double iva = rs.getDouble(7);
			String immagine = rs.getString(8);

			ProdottoBean prodotto = new ProdottoBean();

			prodotto.setIdProdotto(id);
			prodotto.setDescrizione(descrizione);
			prodotto.setNome(nome);
			prodotto.setQuantita(quantita);
			prodotto.setDisponibilita(disponibilita);
			prodotto.setPrezzoBase(prezzoBase);
			prodotto.setIva(iva);
			prodotto.setImmagine(immagine);

			listaProdotti.add(prodotto);
		}
		return listaProdotti;
	}

	public ArrayList<ProdottoBean> doRetrieveFiltered(String nomeFiltro, String categoriaFiltro, Double prezzoMin, Double prezzoMax, String disponibilitaFiltro, String ordine) throws SQLException {
		String sql = "SELECT * FROM prodotto WHERE 1=1";

		if (nomeFiltro != null && !nomeFiltro.trim().isEmpty()) {
			sql += " AND Nome LIKE ?";
		}

		if (categoriaFiltro != null && !categoriaFiltro.trim().isEmpty()) {
			sql += " AND Categoria LIKE ?";
		}

		if (prezzoMin != null) {
			sql += " AND Prezzo_Base >= ?";
		}

		if (prezzoMax != null) {
			sql += " AND Prezzo_Base <= ?";
		}

		if (disponibilitaFiltro != null && !disponibilitaFiltro.trim().isEmpty()) {
			boolean disponibile = disponibilitaFiltro.equalsIgnoreCase("true");
			sql += " AND Disponibilita = ?";
		}

		if (ordine != null && !ordine.trim().isEmpty()) {
			if (ordine.equals("Nome")) {
				sql += " ORDER BY Nome";
			} else if (ordine.equals("Prezzo_Base ASC")) {
				sql += " ORDER BY Prezzo_Base ASC";
			} else if (ordine.equals("Prezzo_Base DESC")) {
				sql += " ORDER BY Prezzo_Base DESC";
			} else {
				sql += " ORDER BY Nome";
			}
		}

		PreparedStatement stmt = connection.prepareStatement(sql);
		int parameterIndex = 1;

		if (nomeFiltro != null && !nomeFiltro.trim().isEmpty()) {
			stmt.setString(parameterIndex++, "%" + nomeFiltro + "%");
		}

		if (categoriaFiltro != null && !categoriaFiltro.trim().isEmpty()) {
			stmt.setString(parameterIndex++, "%" + categoriaFiltro + "%");
		}

		if (prezzoMin != null) {
			stmt.setDouble(parameterIndex++, prezzoMin);
		}

		if (prezzoMax != null) {
			stmt.setDouble(parameterIndex++, prezzoMax);
		}

		if (disponibilitaFiltro != null && !disponibilitaFiltro.trim().isEmpty()) {
			stmt.setBoolean(parameterIndex++, disponibilitaFiltro.equalsIgnoreCase("true"));
		}

		ResultSet rs = stmt.executeQuery();
		ArrayList<ProdottoBean> listaProdotti = new ArrayList<>();

		while (rs.next()) {
			int id = rs.getInt(1);
			String nome = rs.getString(2);
			String descrizione = rs.getString(3);
			int quantita = rs.getInt(4);
			boolean disponibilita = rs.getBoolean(5);
			double prezzoBase = rs.getDouble(6);
			double iva = rs.getDouble(7);
			String immagine = rs.getString(8);

			ProdottoBean prodotto = new ProdottoBean();
			prodotto.setIdProdotto(id);
			prodotto.setNome(nome);
			prodotto.setDescrizione(descrizione);
			prodotto.setQuantita(quantita);
			prodotto.setDisponibilita(disponibilita);
			prodotto.setPrezzoBase(prezzoBase);
			prodotto.setIva(iva);
			prodotto.setImmagine(immagine);
			listaProdotti.add(prodotto);
		}

		return listaProdotti;
	}
}