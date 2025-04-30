<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
    <title>TugurioSemplice - Checkout</title>
    <style>
                body {
            font-family: 'Josefin Sans', sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 0;
            color: #333;
            background-color: #f5f5f5;
        }
        
        .container {
            max-width: 1000px;
            margin: 20px auto;
            padding: 20px;
            background: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            color: #444;
            text-align: center;
            font-weight: 900;
            margin-bottom: 30px;
        }
        
        .checkout-section {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        
        .checkout-form {
            flex: 1;
            min-width: 300px;
        }
        
        .order-summary {
            flex: 1;
            min-width: 300px;
            background: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 700;
        }
        
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-family: inherit;
        }
        
        button {
            background: #333;
            color: white;
            border: none;
            padding: 12px 20px;
            cursor: pointer;
            font-family: inherit;
            font-weight: 700;
            width: 100%;
            margin-top: 10px;
            transition: background 0.3s;
        }
        
        button:hover {
            background: #555;
        }
        
        .order-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        
        .total {
            font-weight: 700;
            font-size: 1.2em;
            margin-top: 20px;
            padding-top: 10px;
            border-top: 2px solid #ddd;
        }
        
        .user-info {
            background: #f0f8ff;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
        <h1>Completa il tuo ordine</h1>
        
        <div class="checkout-section">
            <div class="checkout-form">
                <div class="user-info">
    				<h2>Informazioni personali</h2>
    				<% 
    					ClienteBean user = (ClienteBean) request.getAttribute("user");
    					if (user != null) {
    				%>
        			<p><%= user.getAnagrafia().getNome() %> <%= user.getAnagrafia().getCognome() %></p>
        			<p><%= user.getEmail() %></p>
        			<p><%= user.getIndirizzoSpedizione()%></p>
    				<% } else { %>
        			<p>Informazioni utente non disponibili.</p>
    				<% } %>
				</div>    
                <form action="OrdineController" method="post">
                    <h2>Metodo di pagamento</h2>
                    
                    <div class="form-group">
                        <label for="pagamento">Scegli il pagamento</label>
                        <select id="pagamento" name="pagamento" required>
                            <option value="">-- Seleziona --</option>
                            <option value="Carta">Carta di credito</option>
                            <option value="Contanti al tabacchino">Contanti al tabacchino</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="spedizione">Scegli il metodo di spedizione</label>
                        <select id="spedizione" name="spedizione" required>
                            <option value="">-- Seleziona --</option>
                            <option value="Trasporto funebre">Trasporto funebre</option>
                            <option value="Consegna a domicilio">Consegna a domicilio</option>
                            <option value="Ritiro in negozio">Ritiro in negozio</option>
                        </select>
                    </div>
                                       
                    <button type="submit">Conferma ordine</button>
                </form>
            </div>
			<div class="order-summary">
			    <h2>Riepilogo ordine</h2>
			    <% 
				    List<ProdottoBean> cartItems = (List<ProdottoBean>) request.getAttribute("cartItems");
				    Map<Integer, Integer> quantities = (Map<Integer, Integer>) request.getAttribute("quantities");
				    Double totalAmount = (Double) request.getAttribute("totalAmount");
				    
				    if (cartItems != null && !cartItems.isEmpty()) {
				        for (ProdottoBean product : cartItems) {
				            int quantity = quantities.get(product.getIdProdotto());
			    %>
			    <div class="order-item">
			    <div>
			    	<%= product.getNome() %> x <%= quantity %>
			    </div>
			    <div>
			    	€ <%= String.format("%.2f", product.getPrezzoBase() * quantity) %></div>
			    </div>
			    <% 
			        }
			    %>
			    <div class="total">
			    	<div>Totale:</div>
			        <div>€ <%= String.format("%.2f", totalAmount) %></div>
			    </div>
			    <% } else { %>
			        <p>Il carrello è vuoto.</p>
			    <% } %>
			</div>
        </div>
    </div>
       
    <%@ include file="footer.jsp" %>                
</body>
</html>