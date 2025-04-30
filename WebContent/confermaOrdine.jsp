<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, java.text.SimpleDateFormat"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;700;900&display=swap" rel="stylesheet">
    <title>TugurioSemplice - Conferma Ordine</title>
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
            text-align: center;
        }
        
        h1 {
            color: #444;
            text-align: center;
            font-weight: 900;
            margin-bottom: 30px;
        }
        
        .confirmation-message {
            padding: 30px;
            margin: 30px 0;
            border-radius: 5px;
            font-size: 1.2em;
        }
        
        .success {
            background-color: #e8f5e9;
            color: #2e7d32;
            border: 1px solid #c8e6c9;
        }
        
        .error {
            background-color: #ffebee;
            color: #c62828;
            border: 1px solid #ffcdd2;
        }
        
        .delivery-info {
            margin: 30px 0;
            padding: 20px;
            background: #f0f8ff;
            border-radius: 5px;
        }
        
        .info-box {
            display: inline-block;
            margin: 10px;
            padding: 15px;
            background: #f9f9f9;
            border-radius: 5px;
            min-width: 200px;
        }
        
        .info-label {
            font-weight: 300;
            font-size: 0.9em;
        }
        
        .info-value {
            font-weight: 700;
            font-size: 1.2em;
            margin-top: 5px;
        }
        
        .action-button {
            display: inline-block;
            background: #333;
            color: white;
            text-decoration: none;
            padding: 12px 25px;
            margin: 20px 10px;
            border-radius: 4px;
            font-weight: 700;
            transition: background 0.3s;
        }
        
        .action-button:hover {
            background: #555;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
        <%
            // Recupera eventuali messaggi di errore dalla request
            String errorMessage = (String) request.getAttribute("error");
            OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");
            
            if (errorMessage != null) {
        %>
            <div class="confirmation-message error">
                <h2>Ordine non confermato</h2>
                <p><%= errorMessage %></p>
                <p>Si prega di riprovare più tardi o contattare l'assistenza.</p>
            </div>
            
            <div class="actions">
                <a href="checkout.jsp" class="action-button">Riprova il checkout</a>
                <a href="cart.jsp" class="action-button">Torna al carrello</a>
                <a href="index.jsp" class="action-button">Torna alla home</a>
            </div>
        <%
            } else if (ordine != null) {
                session.removeAttribute("cart");
        %>
            <div class="confirmation-message success">
                <h2>Ordine Confermato!</h2>
                <p>Grazie per il tuo acquisto. Ecco i dettagli del tuo ordine:</p>
            </div>
            
            <div class="delivery-info">
                <div class="info-box">
                    <div class="info-label">Data Consegna Prevista</div>
                    <div class="info-value"><%= ordine.getSpedizione().getDataConsegna() %></div>
                </div>
                
                <div class="info-box">
                    <div class="info-label">Metodo Spedizione</div>
                    <div class="info-value"><%= ordine.getSpedizione().getMetodo() %></div>
                </div>
                
                <div class="info-box">
                    <div class="info-label">Totale Ordine</div>
                    <div class="info-value">€ <%= String.format("%.2f", ordine.getPrezzoOrdine() + ordine.getSpedizione().getSpese()) %></div>
                </div>
            </div>
            
            <div class="actions">
                <a href="index.jsp" class="action-button">Torna alla home</a>
            </div>
        <%
            } 
        %>
    </div>
       
    <%@ include file="footer.jsp" %>                
</body>
</html>