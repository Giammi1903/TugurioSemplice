<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, it.TugurioSemplice.*, DataManagement.*, it.TugurioSemplice.Admin.*"%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
        <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@300;400;700;900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <title>TugurioSemplice - Home</title>
    </head>
    <body>
    <% ClienteBean utente = (ClienteBean) session.getAttribute("user"); %>
        <header class="menu">
            <div class="top-bar">
                <button class="mobile-menu" aria-label="Toggle menu">
                    <i class="fas fa-bars"></i>
                </button>
                <span class="logo-text">TUGURIOSEMPLICE</span>
                <a class="logo-link" href="${pageContext.request.contextPath}/index.jsp">
                    <img src="${pageContext.request.contextPath}/photo/logo.png" alt="TugurioSemplice Logo">
                </a>
                <% if(utente != null) { %>
                <a class="login-link" href="${pageContext.request.contextPath}/AreaUtente.jsp">
                    <img src="${pageContext.request.contextPath}/photo/Connesso.jpg" alt="login">
                </a>
                <% } else { %>
                <a class="login-link" href="${pageContext.request.contextPath}/AuthSites/Login.jsp">
                    <img src="${pageContext.request.contextPath}/photo/access.png" alt="login">
                </a>
                <% } %>
                <a class="cart-link" href="${pageContext.request.contextPath}/cart.jsp">
                    <img src="${pageContext.request.contextPath}/photo/cart.png" alt="carrello">
                </a>
            </div>  
            <nav id="main-nav">  
    			<a class="menu-item <%= request.getRequestURI().contains("index.jsp") ? "active" : "" %>" href="index.jsp">HOME</a>
    			<a class="menu-item <%= request.getRequestURI().contains("about.jsp") ? "active" : "" %>" href="about.jsp">CHI SIAMO</a>
    			<a class="menu-item <%= request.getRequestURI().contains("ProductView.jsp") ? "active" : "" %>" href="ProductView.jsp">CATALOGO</a>
    			<a class="menu-item <%= request.getRequestURI().contains("contact.jsp") ? "active" : "" %>" href="contact.jsp">CONTATTI</a>
    			<% if(utente != null) { %>
    				<a class="menu-item mobile-only <%= request.getRequestURI().contains("AreaUtente.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/AreaUtente.jsp">AREA UTENTE</a>
    			<% } else { %>
    				<a class="menu-item mobile-only <%= request.getRequestURI().contains("Login.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/AuthSites/Login.jsp">ACCEDI</a>
    			<% } %>
    		<a class="menu-item mobile-only <%= request.getRequestURI().contains("cart.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/cart.jsp">CARRELLO</a>
			</nav>
        </header>
        
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const mobileMenuToggle = document.querySelector('.mobile-menu');
                const mainNav = document.getElementById('main-nav');
                
                mobileMenuToggle.addEventListener('click', function() {
                    mainNav.classList.toggle('mobile-active');
                });
            });
        </script>
    </body>
</html>