<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<!DOCTYPE html>
<html>

<head>
  <style>
    body {
      font-family: sans-serif;
      background-color: #f0f0f0;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      margin: 0;
    }

	p{
	  color: red;
	}
	
    .login-container {
      background-color: #ffffff;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    h2 {
      color: #74292c;
      margin-bottom: 20px;
    }

    .form-group {
      margin-bottom: 15px;
      text-align: left;
    }

    label {
      display: block;
      margin-bottom: 5px;
      color: #8d847a;
    }

    input[type="text"],
    input[type="password"] {
      width: calc(100% - 22px);
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-sizing: border-box;
    }

    button {
      background-color: #000000;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }

    button:hover {
      opacity: 0.9;
    }

    .signup-link {
      margin-top: 15px;
      color: #74292c;
    }

    a {
      color: #000000;
      text-decoration: none;
    }

    a:hover {
      text-decoration: underline;
    }
  </style>
  <meta charset="UTF-8">
  <title>Login</title>
</head>

<body>
<% 
	ClienteBean utente = (ClienteBean) session.getAttribute("user");
	if(utente == null){
%>
	<p>Utente non trovato. Effettua il <a href="Login.jsp">Login</a></p>
<%
	} else {
%>
<h2>Inserimento il prodotto</h2>
		<form action="${pageContext.request.contextPath}/InsertProductController" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td><label for="name">Nome:</label></td>
						<td><input type="text" name="name" id="name" required></td>
					</tr>
					<tr>
						<td><label for="description">Descrizione:</label></td>
						<td><textarea name="description" id="description" rows="3" required></textarea></td>
					</tr>
					<tr>
						<td><label for="price">Prezzo Base:</label></td>
						<td><input type="number" name="price" id="price" min="0" step="0.01" required></td>
					</tr>
					<tr>
						<td><label for="quantity">Quantità:</label></td>
						<td><input type="number" name="quantity" id="quantity" min="0" required></td>
					</tr>
					<tr>
						<td><label for="image">Immagine:</label></td>
						<td><input type="file" name="image" id="image"></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<button type="submit">Aggiungi Prodotto</button>
						</td>
					</tr>
				</table>
			</form>		
<% } %>
</body>

</html>