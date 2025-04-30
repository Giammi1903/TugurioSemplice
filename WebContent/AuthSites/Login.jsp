<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/TugurioSemplice/css/login.css">
    <link rel="stylesheet" type="text/css" href="/TugurioSemplice/css/footer.css">
<link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <title>Login</title>
</head>
<body>
  <img src="/TugurioSemplice/photo/logo.png" alt="logo" class="logo">
  <div class="container">
 <%
ClienteBean utente = (ClienteBean) session.getAttribute("user");
%>

<% if(utente == null) { %>
  <h2>Accedi al tuo account</h2>
  <%
    String error = (String) request.getAttribute("error");
    if(error != null) {
  %>
    <div class="error"><i class="fas fa-exclamation-circle"></i> <%= error %></div>
  <%
      session.removeAttribute("error");
    }
  %>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <div class="form-group">
        <label for="usemail">Nome utente o Email:</label>
        <input type="text" id="usemail" name="usemail" required>
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
      </div>
      <button type="submit">Accedi</button>
    </form>
    <p class="signup-link">Non hai un account? <a href="SignIn.jsp">Registrati</a></p>
    <p class="signup-link">Vuoi recuperare la password? <a href="Recupero.jsp">Recupero Password</a></p>
    <%
     } else {
    %>
    <h2>Logout al tuo account</h2>
    <form method="get" action="${pageContext.request.contextPath}/LogoutController">
      <div class="form-group">
        <h3>Vuoi effettuare il logout?</h3>
      </div>
      <button type="submit">Logout</button>
    </form>
    <br>
    <% 
     }
    %>
    <form method="get" action="../index.jsp">
      <button type="submit">Torna alla home</button>
    </form>
  </div>
   <%@ include file="../footer.jsp" %>
</body>
</html>