<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<%
	ProdottoBean product = (ProdottoBean) request.getAttribute("product");
	if(product == null) {
		response.sendRedirect("./product");
		return;
	}
	
	String error = (String)request.getAttribute("error");
	if(error != null) {
		out.println("<div class=\"error-message\">" + error + "</div>");
	}
%>

<!DOCTYPE html>
<html lang="it">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>TugurioSemplice - <%= product.getNome() %></title>
		<style>
			.product-container {
				display: flex;
				flex-wrap: wrap;
				max-width: 1200px;
				margin: 0 auto;
				padding: 20px;
			}
			
			.product-image {
				flex: 1;
				min-width: 300px;
				text-align: center;
				padding: 20px;
			}
			
			.product-image img {
				max-width: 100%;
				height: auto;
				border-radius: 8px;
				box-shadow: 0 4px 8px rgba(0,0,0,0.1);
			}
			
			.product-info {
				flex: 1;
				min-width: 300px;
				padding: 20px;
			}
			
			.product-title {
				font-size: 24px;
				font-weight: bold;
				margin-bottom: 10px;
			}
			
			.product-price {
				font-size: 22px;
				font-weight: bold;
				color: #333;
				margin: 15px 0;
			}
			
			.product-description {
				margin: 20px 0;
				line-height: 1.6;
			}
			
			.product-availability {
				font-weight: bold;
				margin: 10px 0;
			}
			
			.available {
				color: green;
			}
			
			.not-available {
				color: red;
			}
			
			.quantity-selector {
				margin: 20px 0;
			}
			
			.quantity-selector input {
				width: 60px;
				text-align: center;
				padding: 8px;
				margin: 0 10px;
				border: 1px solid #ddd;
				border-radius: 4px;
			}
			
			.add-to-cart {
				background-color: #8d847a;
				color: white;
				border: none;
				padding: 12px 24px;
				font-size: 16px;
				border-radius: 4px;
				cursor: pointer;
				margin-top: 20px;
			}
			
			.add-to-cart:hover {
				background-color: #333;
			}
			
			.add-to-cart:disabled {
				background-color: #cccccc;
				cursor: not-allowed;
			}
			
			.product-details {
				margin-top: 40px;
			}
			
			.product-details h3 {
				border-bottom: 1px solid #ddd;
				padding-bottom: 10px;
				margin-bottom: 20px;
			}
		</style>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		
		<div class="product-container">
			<div class="product-image">
				<img src="<%= product.getImmagine() %>" alt="<%= product.getNome() %>" onerror="this.src='photo/placeholder.jpg'; this.onerror=null;">
			</div>
			
			<div class="product-info">
				<h1 class="product-title"><%= product.getNome() %></h1>
				
				<div class="product-price">
					€<%= String.format("%.2f", product.getPrezzoBase()) %>
				</div>
				
				<div class="product-availability">
					<span class="<%= (product.getQuantita() > 0 && product.getDisponibilita()) ? "available" : "not-available" %>">
						<%= (product.getQuantita() > 0 && product.getDisponibilita()) ? "Disponibile" : "Non disponibile" %>
					</span>
					<% if(product.getQuantita() > 0 && product.getDisponibilita()) { %>
						<span> - <%= product.getQuantita() %> in magazzino</span>
					<% } %>
				</div>
				
				<div class="product-description">
					<%= product.getDescrizione() %>
				</div>
				
				<form action="CartController" method="post">
					<input type="hidden" name="action" value="addC">
					<input type="hidden" name="id" value="<%= product.getIdProdotto() %>">
					
					<div class="quantity-selector">
						<label for="quantity">Quantità:</label>
						<input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= product.getQuantita() %>" <%= (product.getQuantita() > 0 && product.getDisponibilita()) ? "" : "disabled" %>>
					</div>
					
					<button class="add-to-cart" type="submit" <%= (product.getQuantita() > 0 && product.getDisponibilita()) ? "" : "disabled" %>>
						Aggiungi al Carrello
					</button>
				</form>
				
				<div class="product-details">
					<h3>Dettagli prodotto</h3>
					<p>IVA: <%= product.getIva() %>%</p>
					<p>Codice prodotto: <%= product.getIdProdotto() %></p>
				</div>
			</div>
		</div>
		
		<%@ include file="footer.jsp" %>
	</body>
</html>