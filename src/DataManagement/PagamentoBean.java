package DataManagement;

import java.io.Serializable;
import java.util.Date;

public class PagamentoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idOrdine;
	private double importo;
	private Date dataPagamento;
	private String metodo;
	
	public PagamentoBean() {
		
	}
	
	public int getIdOrdine() { return idOrdine;}
	public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine;}
	public double getImporto() { return importo;}
	public void setImporto(double importo) { this.importo = importo;}
	public Date getDataPagamento() { return dataPagamento;}
	public void setDataPagamento(Date dataPagamento) { this.dataPagamento = dataPagamento;}
	public String getMetodo() { return metodo;}
	public void setMetodo(String metodo) { this.metodo = metodo;}



	@Override
	public String toString(){
		return "Id Ordine: " + this.idOrdine + "\tId Importo: " + this.importo + "\tData pagamento: " + this.dataPagamento + "\tMetodo Pagamento: " + this.metodo + "\n";  
	}
	
	
}	