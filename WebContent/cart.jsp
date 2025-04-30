<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<%
Cart cart = (Cart) session.getAttribute("cart");
if (cart == null) {
    cart = new Cart(); // Nuovo carrello per ogni sessione
    session.setAttribute("cart", cart);
}

List<Cart.CartItem> cartItems = cart.getItems();
%>

<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TugurioSemplice - Carrello</title>
</head>
<body>
    <%@ include file="header.jsp" %>
    <br><br><br>
    
    <div class="container">
        <h1>Il Tuo Carrello</h1>
        
        <% if (cartItems != null && !cartItems.isEmpty()) { %>
            <table class="cart-table">
                    <tr>
                        <th>Prodotto</th>
                        <th>Immagine</th>
                        <th>Prezzo</th>
                        <th>Quantità</th>
                        <th>Totale</th>
                        <th>Azioni</th>
                    </tr>
                    <% 
                    for (Cart.CartItem item : cartItems) {
                    	ProdottoBean product = item.getProduct();
                    	int itemQuantity = item.getQuantity();
                    	double itemTotal = item.getTotalPrice();
                    %>
                    <tr>
                        <td><a href="DisplayProductDetail?action=details&id=<%= product.getIdProdotto() %>"><%= product.getNome() %></a></td>
                        <td><img alt="foto prodotto" width="50%" height="30%" src="<%= product.getImmagine() %>"></td>
                        <td>€<%= String.format("%.2f", product.getPrezzoBase()) %></td>
                        <td>
                            <form action="CartController" method="post">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="<%= product.getIdProdotto() %>">
                                <input type="number" class="quantity-input" name="quantity" value="<%= itemQuantity %>" min="1" max = "<%= product.getQuantita() %>" onchange="this.form.submit()">
                            </form>
                        </td>
                        <td>€<%= String.format("%.2f", itemTotal) %></td>
                        <td>
                            <form action="CartController" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= product.getIdProdotto() %>">
                                <button type="submit" class="remove-button">Rimuovi</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    <tr>
                        <td colspan="4" style="text-align: right;"><strong>Totale:</strong></td>
                        <td><strong>€<%= String.format("%.2f", cart.getTotalPrice()) %></strong></td>
                        <td></td>
                    </tr>
            </table>
            
            <div class="cart-actions">
                <form action="CartController" method="post" style="display: inline-block;">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="cart-button" style="background-color: #f44336;">Svuota Carrello</button>
                </form>
                <form action="CheckoutController" method="get" style="display: inline-block;">
                    <button type="submit" class="cart-button">Procedi all'acquisto</button>
                </form>
            </div>
        <% } else { %>
            <div class="empty-cart">
                <p>Il tuo carrello è vuoto</p>
                <a href="product" class="cart-button" style="display: inline-block; text-decoration: none;">Continua lo shopping</a>
            </div>
        <% } %>
    </div>
    
    <%@ include file="footer.jsp" %>
</body>
</html>