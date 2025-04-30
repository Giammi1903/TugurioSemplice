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
  <meta charset="UTF-8">
  <title>Login</title>
</head>
<body>
  <img src="/TugurioSemplice/photo/logo.png" alt="logo" class="logo">
  <div class="container">
  	<form method="post" action="${pageContext.request.contextPath}/RecuperoController">
      <div class="form-group">
        <label for="usemail">Nome utente o Email:</label>
        <input type="text" id="usemail" name="usemail" required>
      </div>
      <div class="form-group">
        <label for="NewPassword">Nuova Password:</label>
        <input type="password" id="NewPassword" name="NewPassword" required>
      </div>
      <div class="form-group">
        <label for="NewPassword2">Conferma la nuova Password:</label>
        <input type="password" id="NewPassword2" name="NewPassword2" required>
      </div>
      <button type="submit">Recupera Account</button>
    </form>
    <br>
    <form method="get" action="../index.jsp">
      <button type="submit">Torna alla home</button>
    </form>
  </div>
  <%@ include file="../footer.jsp" %>
</body>
</html>