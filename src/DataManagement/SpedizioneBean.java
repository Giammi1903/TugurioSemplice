package DataManagement;

import java.io.Serializable;
import java.util.Date;


public class SpedizioneBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idOrdine;
	private double spese;
	private Date dataConsegna;
	private String metodo;
	
	public SpedizioneBean() {
		
	}
	
	public int getIdOrdine() { return idOrdine;}
	public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine;}
	public double getSpese() { return spese;}
	public void setSpese(double spese) { this.spese = spese;}
	public Date getDataConsegna() { return dataConsegna;}
	public void setDataConsegna(Date dataConsegna) { this.dataConsegna = dataConsegna;}
	public String getMetodo() { return metodo;}
	public void setMetodo(String metodo) { this.metodo = metodo;}


	@Override
	public String toString(){
		return "Id Ordine: " + this.idOrdine + "\tId Spese: " + this.spese + "\tData consegna: " + this.dataConsegna + "\tMetodo Pagamento: " + this.metodo + "\n";  
	}
}	