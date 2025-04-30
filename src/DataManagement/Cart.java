package DataManagement;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;
    
    public Cart() {
        items = new ArrayList<>();
    }
    
    // Classe interna per gestire prodotto + quantità
    public static class CartItem {
        private ProdottoBean product;
        private int quantity;
        
        CartItem(ProdottoBean product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        // Add these getter methods
        public ProdottoBean getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        // Add this method to fix the error
        public double getTotalPrice() {
            return product.getPrezzoBase() * quantity;
        }
    }
    
    // Rest of your class remains unchanged
    public void addProduct(ProdottoBean product) {
        addProduct(product, 1);
    }
    
    public void addProduct(ProdottoBean product, int quantity) {
        for (CartItem item : items) {
            if (item.product.getIdProdotto() == product.getIdProdotto()) {
                item.quantity += quantity;
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }
    
    public void deleteProduct(ProdottoBean product) {
        items.removeIf(item -> item.product.getIdProdotto() == product.getIdProdotto());
    }
    
    public void updateQuantity(int productId, int quantity) {
        for (CartItem item : items) {
            if (item.product.getIdProdotto() == productId) {
                if (quantity <= 0) {
                    deleteProduct(item.product);
                } else {
                    item.quantity = quantity;
                }
                return;
            }
        }
    }
    
    public void clear() {
        items.clear();
    }
    
    public List<ProdottoBean> getProducts() {
        List<ProdottoBean> products = new ArrayList<>();
        for (CartItem item : items) {
            products.add(item.product);
        }
        return products;
    }
    
    public int getQuantity(int productId) {
        for (CartItem item : items) {
            if (item.product.getIdProdotto() == productId) {
                return item.quantity;
            }
        }
        return 0;
    }
    
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.product.getPrezzoBase() * item.quantity;
        }
        return total;
    }
    
    // Metodo aggiuntivo per ottenere tutti gli items (utile per la JSP)
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }
}