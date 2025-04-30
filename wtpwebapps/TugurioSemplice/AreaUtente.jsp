<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<!DOCTYPE html>
<html>
  <head>
  <title>Profilo Utente</title>
  </head>
  <body>
  <%@ include file="header.jsp" %>
  <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  <a href = "AuthSites/DatiUtente.jsp"> Dati utente </a><br>
   <form action="ListaOrdineController" method="post">
   		<input type="hidden" name="idCliente" value="<%= utente.getIdCliente() %>">
    	<button type="submit">I miei ordini</button>
	</form>
    <form action="AuthSites/Login.jsp" method="get">
    	<button type="submit">Logout</button>
	</form>
  <%@ include file="footer.jsp" %>
  </body>
</html> 