<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="css/error.css">
	<link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
    <title>Errore 404 - Pagina non trovata</title>
</head>
<body>
    <img src="photo/logo.png" alt="logo" class="logo">
    <div class="container">
        <img src="photo/404.jpg" alt="Immagine di errore 404" class="error-image">
        <br>
        <center><b>Errore 404</b></center>
        <br>
        Ci dispiace, la pagina che stai cercando non è stata trovata.
        <br><br>
         <a class="button" href="index.jsp" >Torna alla homepage</a>
    </div>
    <%@ include file="footer.jsp" %>   
</body>
</html>