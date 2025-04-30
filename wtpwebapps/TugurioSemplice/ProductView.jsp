<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*" session="true" %>

<%
	Collection<?> products = (Collection<?>) request.getAttribute("products");
	if(products == null) {
		response.sendRedirect("./product");
		return;
	}
	
	ProdottoBean product = (ProdottoBean) request.getAttribute("product");
	
	String error = (String)request.getAttribute("error");
	if(error != null) {
		out.println("<div class=\"error-message\">" + error + "</div>");
	}
	
	String currentSort = request.getParameter("sort");
	if(currentSort == null) currentSort = "";
	
%>

<!DOCTYPE html>
<html lang="it">
	<head>
		<meta charset="UTF-8">
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    	<link rel="stylesheet" type="text/css" href="css/product.css">
    	<link rel="stylesheet" type="text/css" href="css/scrollbar.css">
		<link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
		<title>TugurioSemplice - Catalogo Prodotti</title>
		
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<div class="main-content">
		<p style="font-size: 50px;" class="page-title">Catalogo Prodotti</p>
		<div class="filters">
			<form action="product" method="get" id="filterForm">
				<div class="filter-group">
					<label for="sort">Ordina per:</label>
					<select name="sort" id="sort" onchange="document.getElementById('filterForm').submit();">
						<option value="Nome" <%= currentSort.equals("Nome") ? "selected" : "" %>>Nome</option>
						<option value="Prezzo_Base ASC" <%= currentSort.equals("Prezzo_Base ASC") ? "selected" : "" %>>Prezzo (crescente)</option>
						<option value="Prezzo_Base DESC" <%= currentSort.equals("Prezzo_Base DESC") ? "selected" : "" %>>Prezzo (decrescente)</option>
					</select>
				</div>
			</form>
			<% 
				if(utente != null && utente.getIsAmministratore())
				{
			%>	
			<form action="AdminAction/InsertAdmin.jsp" method="get">
    			<input type="hidden" name="action" value="insert">
    			<button type="submit">Aggiungi un prodotto</button>
    		</form>			
			<%
				}
			%>
		</div>
		
		<div class="product-grid">
			<%
			if (products != null && products.size() != 0) {
				Iterator<?> it = products.iterator();
				
				while (it.hasNext()) {
					ProdottoBean bean = (ProdottoBean) it.next();
			%>
			<div class="product-card">
				<a class="product-card-link" href="DisplayProductDetail?action=details&id=<%= bean.getIdProdotto() %>"></a>
				<img class="product-image" alt="foto prodotto" src="<%= bean.getImmagine()%>">
				<%System.out.println(bean.getImmagine()); %>
				<p class="product-name"><%= bean.getNome() %></p>
				<div class="product-price">€<%= String.format("%.2f", bean.getPrezzoBase()) %></div>
				<div class="product-stock">
					<% if(bean.getQuantita() > 0 && bean.getDisponibilita()) { %>
						<span class="available">Disponibile</span>
					<% } else { %>
						<span class="unavailable">Non disponibile</span>
					<% } %>
				</div>
				<div class="product-actions">
					<% 
						if(utente != null && utente.getIsAmministratore())
						{
					%>
					<div class="admin-actions">
						<form action="UpdateProductController" method="get">
							<input type="hidden" name="action" value="update">
							<input type="hidden" name="id" value="<%= bean.getIdProdotto() %>">
							<button type="submit">Modifica</button>
						</form>
						<form action="DeleteProductController" method="get">
							<input type="hidden" name="action" value="delete">
							<input type="hidden" name="id" value="<%= bean.getIdProdotto() %>">
							<button type="submit">Elimina</button>
						</form>
					</div>
					<%
						}
					%>									
				</div>
			</div>
			<%
				}
			} else {
			%>
			<div style="width: 100%; text-align: center; padding: 20px;">
				<p>Nessun prodotto disponibile</p>
			</div>
			<%
			}
			%>
			</div>
		</div>
		<%@ include file="footer.jsp" %>
	</body>
</html>