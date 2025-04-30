<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="css/error.css">
	<link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
    <title>Errore 500 - Errore interno del server</title>
</head>
	<body>
     <img src="photo/logo.png" alt="logo" class="logo">
        <div class="container">
            <img src="photo/500.png" alt="Immagine di errore 500" class="error-image"> 
            <center><b>Errore 500</b></center>
            Si è verificato un errore interno del server.
            <br>
            Ci scusiamo per l'inconveniente.
            <br><br>
            <a class="button" href="index.jsp" >Torna alla homepage</a>
        </div>
        <%@ include file="footer.jsp" %>   
    </body>
</html>