<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>

<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
        <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;400;700;900&display=swap" rel="stylesheet">
        <title>TugurioSemplice - Home</title>
    </head>
    <body>
        <header class="menu">
            <div class="top-bar">
                <span class="logo-text">TUGURIOSEMPLICE</span>
                <a class="logo-link" href="${pageContext.request.contextPath}/index.jsp"><img src="${pageContext.request.contextPath}/photo/logo.png" width="25%" alt="TugurioSemplice Logo"></a>
                <%
                	ClienteBean utente = (ClienteBean) session.getAttribute("user");
                	if(utente != null) { 
                %>
                <a class="login-link" href="${pageContext.request.contextPath}/AreaUtente.jsp"><img src="${pageContext.request.contextPath}/photo/access.png" width="15%" alt="login"></a>
                <% 
                } else {
                %>
                <a class="login-link" href="${pageContext.request.contextPath}/AuthSites/Login.jsp"><img src="${pageContext.request.contextPath}/photo/access.png" width="15%" alt="login"></a>
                <%
                } 
                %>
                <a class="cart-link" href="${pageContext.request.contextPath}/cart.jsp"><img src="${pageContext.request.contextPath}/photo/cart.png" width="15%" alt="carrello"></a>
            </div>  
            <div class="line">   
                <a class="menu-item <%= request.getRequestURI().contains("index.jsp") ? "active" : "" %>" href="index.jsp">HOME</a>
                <a class="menu-item <%= request.getRequestURI().contains("about.jsp") ? "active" : "" %>" href="about.jsp">CHI SIAMO</a>
                <a class="menu-item <%= request.getRequestURI().contains("ProductView.jsp") ? "active" : "" %>" href="ProductView.jsp">CATALOGO</a>
                <a class="menu-item <%= request.getRequestURI().contains("contact.jsp") ? "active" : "" %>" href="contact.jsp">CONTATTI</a>  
            </div>
        </header>    
    </body>
</html>
