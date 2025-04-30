package DataManagement;

import java.io.Serializable;

public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

			private IndirizzoSpedizioneBean indirizzoSpedizione;
			private DatiAnagraficiBean anagrafia;
		    private int idCliente;  
		    private String username; 
		    private String email;
		    private String passkey;
		    private boolean isAmministratore;

		    public ClienteBean(){
		    	
		    }
		    
	public IndirizzoSpedizioneBean getIndirizzoSpedizione() { return this.indirizzoSpedizione;}
	public void setIndirizzoSpedizione(IndirizzoSpedizioneBean indirizzoSpedizione) { this.indirizzoSpedizione = indirizzoSpedizione; }
    public DatiAnagraficiBean getAnagrafia() { return this.anagrafia;}
    public void setAnagrafia(DatiAnagraficiBean anagrafia) { this.anagrafia = anagrafia;}
	public int getIdCliente() { return this.idCliente;}
	public void setIdCliente(int idCliente) { this.idCliente = idCliente;}
	public String getUsername() { return this.username;}
	public void setUsername(String username) { this.username = username;}
	public String getEmail() { return this.email;}
	public void setEmail(String email) { this.email = email;}
	public String getPasskey() { return this.passkey;}
	public void setPasskey(String passkey) { this.passkey = passkey;}
	public boolean getIsAmministratore() { return this.isAmministratore;}
	public void setIsAmministratore(boolean isAmministratore) { this.isAmministratore = isAmministratore;}


	@Override
	public String toString() {
		return "Id: " + this.idCliente + "\tUsername: " + this.username + "\tEmail: " + this.email + "\tPasskey: " + this.passkey + "\tAmministratore: " + this.isAmministratore + "\tAnagrafia\n" + anagrafia.toString() + "\tIndirizzoSpedizione\n" + this.indirizzoSpedizione.toString();
	}

}