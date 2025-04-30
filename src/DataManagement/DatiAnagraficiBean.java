package DataManagement;

import java.io.Serializable;

public class DatiAnagraficiBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
		    private String nome;  
		    private String cognome; 
		    private String cf;
		    private String telefono;

		    public DatiAnagraficiBean(){
		    	
		    }


	public String getNome() { return this.nome;}
	public void setNome(String nome) { this.nome = nome;}
	public String getCognome() { return this.cognome;}
	public void setCognome(String cognome) { this.cognome = cognome;}
	public String getCf() { return this.cf;}
	public void setCf(String cf) { this.cf = cf;}
	public String getTelefono() { return this.telefono;}
	public void setTelefono(String telefono) { this.telefono = telefono;}


	@Override
	public String toString() {
		return "Nome: " + this.nome + "\tCognome: " + this.cognome + "\tCF: " + this.cf + "\tTelefono: " + this.telefono + "\n";
	}
}