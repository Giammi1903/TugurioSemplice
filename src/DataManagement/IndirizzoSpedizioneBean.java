package DataManagement;

import java.util.ArrayList;

public class IndirizzoSpedizioneBean {
	private String via;
	private String cap;
	private String citta;
	private String provincia;
	
	public IndirizzoSpedizioneBean() {
		
	}

	public String getVia() { return via;}
	public void setVia(String via) { this.via = via;}
	public String getCap() { return cap;}
	public void setCap(String cap) { this.cap = cap;}
	public String getCitta() { return citta;}
	public void setCitta(String citta) { this.citta = citta;}
	public String getProvincia() { return provincia;}
	public void setProvincia(String provincia) { this.provincia = provincia;}

	@Override
	public String toString() {
		return this.via + ", " + this.cap + ", " + this.citta + ", " + this.provincia + "\n";
	}
	
	

}