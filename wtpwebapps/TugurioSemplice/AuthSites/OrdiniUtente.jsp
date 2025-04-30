<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<!DOCTYPE html>
<html>
  <head>
  <title>Profilo Utente - I Tuoi Ordini</title>
  </head>
  <body>
  <%@ include file="../header.jsp" %>
  <br><br><br><br><br><br><br><br>
    <% 
    if(utente != null) 
    {
      // Recupero gli ordini del cliente dalla sessione
      ArrayList<OrdineBean> listaOrdini = (ArrayList<OrdineBean>) session.getAttribute("listaOrdini");
    %>
      
      <h2>I Tuoi Ordini</h2>
      <% 
      if(listaOrdini != null && !listaOrdini.isEmpty()) { 
        for(OrdineBean ordine : listaOrdini) {
      %>
        <div>
          <h3>Ordine #<%= ordine.getIdOrdine() %></h3>
          <p>Data: <%= ordine.getDataOrdine() %></p>
          <p>Stato:<%= ordine.getStatoOrdine() %></p>
          <p>Totale: <%= ordine.getPrezzoOrdine() %> €</p>
          
          <% if(ordine.getPagamento() != null) { %>
          <h4>Dettagli Pagamento</h4>
          <p>Importo: <%= ordine.getPagamento().getImporto() %> €</p>
          <p>Data Pagamento: <%= ordine.getPagamento().getDataPagamento() %></p>
          <p>Metodo: <%= ordine.getPagamento().getMetodo() %></p>
          <% } %>
          
          <% if(ordine.getSpedizione() != null) { %>
          <h4>Dettagli Spedizione</h4>
          <p>Metodo:<%= ordine.getSpedizione().getMetodo() %></p>
          <p>Spese: <%= ordine.getSpedizione().getSpese() %> €</p>
          <p>Data Consegna Prevista: <%= ordine.getSpedizione().getDataConsegna() %></p>
          <% } %>
          
          <h4>Prodotti Acquistati</h4>
          <table border="1">
            <tr>
              <th>Nome</th>
              <th>Prezzo</th>
              <th>Quantità</th>
              <th>Subtotale</th>
            </tr>
            <% 
            if(ordine.getProdotti() != null) {
              for(ProdottoBean prodotto : ordine.getProdotti()) {
                double subtotale = prodotto.getPrezzoBase() * prodotto.getQuantita();
            %>
              <tr>
                <td><%= prodotto.getNome() %></td>
                <td><%= prodotto.getPrezzoBase() %> €</td>
                <td><%= prodotto.getQuantita() %></td>
                <td><%= subtotale %> €</td>
              </tr>
            <% 
              }
            } 
            %>
          </table>
          <div>
			<a href="Fattura.jsp?idOrdine=<%= ordine.getIdOrdine() %>" target="_blank"> Visualizza Fattura</a>
		  </div>
          <hr>
        </div>
      <% 
        }
      } else { 
      %>
        <p>Non hai ancora effettuato ordini.</p>
      <% 
      } 
      %>
    <%
    } else {
    %>
      <p>Utente non autenticato. Effettua il <a href="AuthSites/Login.jsp">login</a> per visualizzare il tuo profilo.</p>
    <%
    }
    %>
    <%@ include file="../footer.jsp" %>
  </body>
</html>