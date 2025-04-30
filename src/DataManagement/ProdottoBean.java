package DataManagement;

import java.io.Serializable;

public class ProdottoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
		    private int idProdotto;  
		    private String nome; 
		    private String descrizione;
		    private int quantita;
		    private boolean disponibilita;
		    private double prezzoBase;
		    private double iva;
		    private String immagine;

		    public ProdottoBean(){
		    	
		    }

	public int getIdProdotto() { return this.idProdotto;}
	public void setIdProdotto(int idProdotto) { this.idProdotto = idProdotto;}
	public String getNome() { return this.nome;}
	public void setNome(String nome) { this.nome = nome;}
	public String getDescrizione() { return this.descrizione;}
	public void setDescrizione(String description) { this.descrizione = description;}
	public int getQuantita() { return this.quantita;}
	public void setQuantita(int quantita) { this.quantita = quantita;}
	public void setPrezzoBase(double prezzoBase) { this.prezzoBase = prezzoBase;}	
	public double getPrezzoBase() { return this.prezzoBase;}
	public void setDisponibilita(boolean disponibilita) { this.disponibilita = disponibilita;}
	public boolean getDisponibilita() { return this.disponibilita;}
	public void setIva(double iva) { this.iva = iva;}
	public double getIva() { return this.iva;}
	public void setImmagine(String immagine) { this.immagine = immagine;}
	public String getImmagine() { return this.immagine;}


	@Override
	public String toString() {
		return "Id: " + this.idProdotto + "\tNome: " + this.nome + "\tDescrizione: " + this.descrizione + "\tQuantità: " + this.quantita + "\tDisponibilità: " + this.disponibilita + "\t Prezzo base: " + this.prezzoBase + "\tPercorso immagine: " + this.immagine + "\n";
	}

}
