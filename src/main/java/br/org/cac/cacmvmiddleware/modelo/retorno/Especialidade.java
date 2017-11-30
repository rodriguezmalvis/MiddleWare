package br.org.cac.cacmvmiddleware.modelo.retorno;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWPRESTADORESESPECIALIDADES")
public class Especialidade {
	
	private Id id;
	private String snPrincipal;
	public Id getId() {
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public String getSnPrincipal() {
		return snPrincipal;
	}
	public void setSnPrincipal(String snPrincipal) {
		this.snPrincipal = snPrincipal;
	}
	public Especialidade() {
		super();
	}
	

}
