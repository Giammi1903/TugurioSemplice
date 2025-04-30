package it.TugurioSemplice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DataManagement.Cart;
import DataManagement.ProdottoBean;
import DataManagement.ProdottoDAO;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO model;
    private static final String CART_COOKIE_NAME = "cartId";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 giorni in secondi
    
    @Override
    public void init() throws ServletException {
        super.init();
        model = new ProdottoDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Gestione del carrello con cookie
        String cartId = getOrCreateCartId(request, response);
        
        // 2. Recupera o crea il carrello (prima dalla sessione, poi dal cookie/database)
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }

        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action.toLowerCase()) {
                    case "addc":
                        addProductToCart(request, cart);
                        break;
                    case "deletec":
                    case "delete":
                        deleteProductFromCart(request, cart);
                        break;
                    case "update":
                        updateProductQuantity(request, cart);
                        break;
                    case "clear":
                        cart.clear();
                        break;
                    case "read":
                        readProduct(request);
                        break;
                    case "insert":
                        insertNewProduct(request);
                        break;
                    default:
                        break;
                }
            }            
        } catch (SQLException e) {
            handleError("Database error: " + e.getMessage(), e, request, response);
            return;
        } catch (NumberFormatException e) {
            handleError("Invalid number format: " + e.getMessage(), e, request, response);
            return;
        }

        // 3. Salva il carrello aggiornato sia in sessione
        request.getSession().setAttribute("cart", cart);
        request.setAttribute("cart", cart);
        
        // Recupera e mostra i prodotti (ordinati se necessario)
        String sort = request.getParameter("sort");
        try {
            request.setAttribute("products", model.doRetrieveAll(sort));
        } catch (SQLException e) {
            handleError("Error retrieving products: " + e.getMessage(), e, request, response);
            return;
        }

        // Forward alla pagina JSP
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/cart.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // ======= METODI PER LA GESTIONE DEI COOKIE =======

    private String getOrCreateCartId(HttpServletRequest request, HttpServletResponse response) {
        // Cerca il cookie esistente
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CART_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        // Se non esiste, crea un nuovo ID e imposta il cookie
        String newCartId = UUID.randomUUID().toString();
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, newCartId);
        cartCookie.setMaxAge(COOKIE_MAX_AGE);
        cartCookie.setPath("/");
        cartCookie.setHttpOnly(true);
        response.addCookie(cartCookie);
        
        return newCartId;
    }

    // ======= METODI AUSILIARI PRIVATI =======

    private void addProductToCart(HttpServletRequest request, Cart cart) throws SQLException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            ProdottoBean product = model.doRetrieveByKey(id);
            if (product != null) {
                cart.addProduct(product);
            }
        }
    }

    private void deleteProductFromCart(HttpServletRequest request, Cart cart) throws SQLException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            ProdottoBean product = model.doRetrieveByKey(id);
            if (product != null) {
                cart.deleteProduct(product);
            }
        }
    }

    private void updateProductQuantity(HttpServletRequest request, Cart cart) throws SQLException {
        String idParam = request.getParameter("id");
        String quantityParam = request.getParameter("quantity");
        if (idParam != null && quantityParam != null) {
            int id = Integer.parseInt(idParam);
            int quantity = Integer.parseInt(quantityParam);
            cart.updateQuantity(id, quantity);
        }
    }

    private void readProduct(HttpServletRequest request) throws SQLException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            request.setAttribute("product", model.doRetrieveByKey(id));
        }
    }

    private void insertNewProduct(HttpServletRequest request) throws SQLException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceParam = request.getParameter("price");
        String quantityParam = request.getParameter("quantity");

        if (name != null && description != null && priceParam != null && quantityParam != null) {
            double price = Double.parseDouble(priceParam);
            int quantity = Integer.parseInt(quantityParam);

            ProdottoBean product = new ProdottoBean();
            product.setNome(name);
            product.setDescrizione(description);
            product.setPrezzoBase(price);
            product.setQuantita(quantity);
            model.doSave(product);
        }
    }

    private void handleError(String message, Exception e, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.err.println(message);
        request.setAttribute("errorMessage", message);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/500.html");
        dispatcher.forward(request, response);
    }
}