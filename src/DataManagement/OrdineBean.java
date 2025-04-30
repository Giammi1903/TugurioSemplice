package DataManagement;

import java.io.Serializable;
import java.util.ArrayList;
public class OrdineBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
		ArrayList<ProdottoBean> prodotti = new ArrayList<ProdottoBean>();
		private int idOrdine;
		private int idCliente;
		private String dataOrdine;
		private double prezzoOrdine;
		private String statoOrdine;
		private PagamentoBean pagamento;
		private SpedizioneBean spedizione;
		private InseritoBean inserito;
	
		public OrdineBean() {
			
		}
		
		public ArrayList<ProdottoBean> getProdotti() { return prodotti;}
		public void setProdotti(ArrayList<ProdottoBean> prodotti) { this.prodotti = prodotti;}
		public String getDataOrdine() { return dataOrdine;}
		public void setDataOrdine(String dataOrdine) { this.dataOrdine = dataOrdine;}
		public double getPrezzoOrdine() { return prezzoOrdine;}
		public void setPrezzoOrdine(double prezzoOrdine) { this.prezzoOrdine = prezzoOrdine;}
		public String getStatoOrdine() { return statoOrdine;}
		public void setStatoOrdine(String statoOrdine) { this.statoOrdine = statoOrdine;}
		public int getIdOrdine() { return idOrdine;}
		public void setIdOrdine(int idOrdine) {this.idOrdine = idOrdine;}
		public int getIdCliente() { return idCliente;}
		public void setIdCliente(int idCliente) {this.idCliente = idCliente;}
		public void setPagamento(PagamentoBean pagamento) {this.pagamento = pagamento;}
		public PagamentoBean getPagamento() {return this.pagamento;}
		public void setSpedizione(SpedizioneBean spedizione) {this.spedizione = spedizione;}
		public SpedizioneBean getSpedizione() {return this.spedizione;}
		public void setInserito(InseritoBean inserito) {this.inserito = inserito;}
		public InseritoBean getInserito() {return this.inserito;}


	@Override
	public String toString(){
		return "Id Ordine: " + this.idOrdine + "\tId Cliente: " + this.idCliente + "\t Data Ordine: " + this.dataOrdine + "\t Prezzo ordine: " +this.prezzoOrdine + "\t Stato ordine: " + statoOrdine + "\n";  
	}
	
	
}