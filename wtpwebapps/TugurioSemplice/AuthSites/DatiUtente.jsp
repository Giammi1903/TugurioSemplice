<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Dati Utente</title>
  </head>
  <body>
    <%@ include file="../header.jsp" %>
    <br><br><br><br><br><br><br><br>
    
    <% if(utente != null) { %>
      <h2>Dati Utente</h2>
      <p>ID Cliente: <%= utente.getIdCliente() %></p>
      <p>Username: <%= utente.getUsername() %></p>
      <p>Email: <%= utente.getEmail() %></p>
      
      <h3>Dati Anagrafici</h3>
      <% if(utente.getAnagrafia() != null) { %>
        <p>Nome: <%= utente.getAnagrafia().getNome() %></p>
        <p>Cognome: <%= utente.getAnagrafia().getCognome() %></p>
        <p>Codice Fiscale: <%= utente.getAnagrafia().getCf() %></p>
        <p>Telefono: <%= utente.getAnagrafia().getTelefono() %></p>
      <% } else { %>
        <p>Nessun dato anagrafico disponibile</p>
      <% } %>
      
      <h3>Indirizzo di Spedizione</h3>
      <% if(utente.getIndirizzoSpedizione() != null) { %>
        <p>Via: <%= utente.getIndirizzoSpedizione().getVia() %></p>
        <p>Città: <%= utente.getIndirizzoSpedizione().getCitta() %></p>
        <p>CAP: <%= utente.getIndirizzoSpedizione().getCap() %></p>
        <p>Provincia: <%= utente.getIndirizzoSpedizione().getProvincia() %></p>
      <% } else { %>
        <p>Nessun indirizzo di spedizione disponibile</p>
      <% } %>
      
      <form action="ModificaDatiUtente.jsp" method="post">
        <button type="submit">Modifica i tuoi dati</button>
      </form>
      
    <% } else { %>
      <p>Utente non trovato. Effettua il <a href="Login.jsp">Login</a></p>
    <% } %>
    
    <%@ include file="../footer.jsp" %>
  </body>
</html>