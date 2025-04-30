package DataManagement;

import java.io.Serializable;

public class InseritoBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idOrdine;
    private int idProdotto;
    private double prezzo;
    private int quantita;
    
    public InseritoBean() {
    }
    
    public int getIdOrdine() {return idOrdine;}
    public void setIdOrdine(int idOrdine) {this.idOrdine = idOrdine;}    
    public int getIdProdotto() {return idProdotto;}
    public void setIdProdotto(int idProdotto) {this.idProdotto = idProdotto;}  
    public double getPrezzo() {return prezzo;}    
    public void setPrezzo(double d) {this.prezzo = d;}   
    public int getQuantita() {return quantita;}
    public void setQuantita(int quantita) {this.quantita = quantita;}
    
    @Override
    public String toString() {
        return "IdOrdine: " + idOrdine + ", IdProdotto: " + idProdotto + 
               ", Prezzo: " + prezzo + ", Quantita: " + quantita;
    }
}