<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Dati Utente</title>
  </head>
  <body>
  <%@ include file = "../header.jsp" %>
  <br><br><br><br><br><br><br><br>
  
  <%
    if(utente != null) 
    {
      DatiAnagraficiBean anagrafica = utente.getAnagrafia();
      IndirizzoSpedizioneBean indirizzo = utente.getIndirizzoSpedizione();
  %>
    <h2>Modifica i tuoi dati</h2>
    
    <form action="${pageContext.request.contextPath}/ModificaUtenteController" method="post">
      <!-- Dati di accesso -->
      <fieldset>
        <legend>Dati di accesso</legend>
        
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="<%= utente.getUsername() %>" required><br><br>
        
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<%= utente.getEmail() %>" required><br><br>
        
        <label for="password">Nuova Password:</label>
        <input type="password" id="password" name="password" placeholder="Lascia vuoto per non modificare"><br><br>
        
        <label for="confirmPassword">Conferma Password:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Conferma la nuova password"><br><br>
      </fieldset>
      
      <!-- Dati anagrafici -->
      <fieldset>
        <legend>Dati anagrafici</legend>
        
        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" value="<%= anagrafica != null ? anagrafica.getNome() : "" %>" required><br><br>
        
        <label for="cognome">Cognome:</label>
        <input type="text" id="cognome" name="cognome" value="<%= anagrafica != null ? anagrafica.getCognome() : "" %>" required><br><br>
        
        <label for="telefono">Telefono:</label>
        <input type="text" id="telefono" name="telefono" value="<%= anagrafica != null ? anagrafica.getTelefono() : "" %>" required><br><br>
        
      </fieldset>
      
      <!-- Indirizzo di spedizione -->
      <fieldset>
        <legend>Indirizzo di spedizione</legend>
        
        <label for="via">Via:</label>
        <input type="text" id="via" name="via" value="<%= indirizzo != null ? indirizzo.getVia() : "" %>" required><br><br>
        
        <label for="citta">Città:</label>
        <input type="text" id="citta" name="citta" value="<%= indirizzo != null ? indirizzo.getCitta() : "" %>" required><br><br>
        
        <label for="cap">CAP:</label>
        <input type="text" id="cap" name="cap" value="<%= indirizzo != null ? indirizzo.getCap() : "" %>" required><br><br>
        
        <label for="provincia">Provincia:</label>
        <input type="text" id="provincia" name="provincia" value="<%= indirizzo != null ? indirizzo.getProvincia() : "" %>" required><br><br>
      </fieldset>
      
      <input type="hidden" name="idCliente" value="<%= utente.getIdCliente() %>">
      <input type="hidden" name="codiceFiscale" value="<%= anagrafica.getCf() %>">
      <br>
      <input type="submit" value="Salva Modifiche">
    </form>
  <% 
    } else {
  %>
    <h2>Accesso negato</h2>
    <p>Devi effettuare il login per visualizzare questa pagina.</p>
    <a href="${pageContext.request.contextPath}/AuthSites/Login.jsp">Vai al Login</a>
  <% 
    }
  %>
  
  <%@ include file = "../footer.jsp" %>
  </body>
</html>