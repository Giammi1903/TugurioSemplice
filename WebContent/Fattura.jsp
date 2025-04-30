<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<%
// Recupera l'ID dall'URL
int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));

// Recupera l'utente dalla sessione
ClienteBean utente = (ClienteBean) session.getAttribute("user");

// Recupera l'ordine e i dettagli dal database
OrdineDAO ordineDAO = new OrdineDAO();
OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
ArrayList<Map<String, Object>> dettagliOrdine = ordineDAO.getDettagliOrdine(idOrdine);

// Recupera pagamento e spedizione se esistono
PagamentoBean pagamento = ordineDAO.retrievePayment(idOrdine);
SpedizioneBean spedizione = ordineDAO.retrieveShipping(idOrdine);
%>
<!DOCTYPE html>
<html>
  <head>
    <title>Fattura</title>
  </head>
  <body>
    <%
    if(utente != null && ordine != null) 
    {
    %>
      <div>
        <h1>FATTURA</h1>
        <p>Numero Fattura: FAT-<%= ordine.getIdOrdine() %></p>
        <p>Data: <%= ordine.getDataOrdine() %></p>
        
        <h2>Venditore</h2>
        <p>Tugurio Semplice S.r.l.</p>
        <p>Via Esempio, 123</p>
        <p>00123 Roma (RM)</p>
        <p>P.IVA: 12345678901</p>
        
        <h2>Cliente</h2>
        <p>Nome: <%= utente.getAnagrafia() != null ? utente.getAnagrafia().getNome() + " " + utente.getAnagrafia().getCognome() : "N/D" %></p>
        <p>Codice Fiscale: <%= utente.getAnagrafia() != null ? utente.getAnagrafia().getCf() : "N/D" %></p>
        <% if(utente.getIndirizzoSpedizione() != null) { %>
          <p>Indirizzo: <%= utente.getIndirizzoSpedizione().getVia() %></p>
          <p>Città: <%= utente.getIndirizzoSpedizione().getCitta() %> (<%= utente.getIndirizzoSpedizione().getProvincia() %>)</p>
          <p>CAP: <%= utente.getIndirizzoSpedizione().getCap() %></p>
        <% } else { %>
          <p>Indirizzo: N/D</p>
        <% } %>
        
        <h2>Dettagli Ordine</h2>
        <table border="1">
            <tr>
                <th>Articolo</th>
                <th>Quantità</th>
                <th>Prezzo Unitario</th>
                <th>Totale</th>
            </tr>
            <%
            double subtotale = 0;
            if(dettagliOrdine != null && !dettagliOrdine.isEmpty()) {
                for(Map<String, Object> dettaglio : dettagliOrdine) {
                    String nomeProdotto = (String) dettaglio.get("nomeProdotto");
                    int quantita = (Integer) dettaglio.get("quantita");
                    double prezzo = (Double) dettaglio.get("prezzo");
                    double totaleRiga = quantita * prezzo;
                    subtotale += totaleRiga;
            %>
                <tr>
                    <td><%= nomeProdotto %></td>
                    <td><%= quantita %></td>
                    <td><%= String.format("%.2f", prezzo) %> €</td>
                    <td><%= String.format("%.2f", totaleRiga) %> €</td>
                </tr>
            <% 
                }
            }
            %>
        </table>
        
        <!-- Sezione pagamento -->
        <% if(pagamento != null) { %>
        <h2>Metodo di Pagamento</h2>
        <p>Metodo: <%= pagamento.getMetodo() %></p>
        <p>Importo: <%= String.format("%.2f", pagamento.getImporto()) %> €</p>
        <p>Data Pagamento: <%= pagamento.getDataPagamento() %></p>
        <% } %>
        
        <!-- Sezione spedizione -->
        <% if(spedizione != null) { %>
        <h2>Spedizione</h2>
        <p>Metodo: <%= spedizione.getMetodo() %></p>
        <p>Spese: <%= String.format("%.2f", spedizione.getSpese()) %> €</p>
        <p>Data Consegna Prevista: <%= spedizione.getDataConsegna() %></p>
        <% } %>
        <BR>
        <p>Fattura emessa elettronicamente</p>
        <p>Documento non valido ai fini fiscali</p>
        
        <div>
          <button onclick="window.print()">Stampa Fattura</button>
          <a href="ProfiloUtente.jsp">Torna al profilo</a>
        </div>
      </div>
    <%
    } else {
    %>
      <div>
        <p>Impossibile generare la fattura. Dati mancanti o utente non autenticato.</p>
        <a href="index.jsp">Torna alla Home</a>
      </div>
    <%
    }
    %>
  </body>
</html>