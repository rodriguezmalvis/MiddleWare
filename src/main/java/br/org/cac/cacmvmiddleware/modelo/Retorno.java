package br.org.cac.cacmvmiddleware.modelo;

public class Retorno {
	
	private String status;
	
	private String entidadeId;
	
	private String contrato;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(String entidadeId) {
		this.entidadeId = entidadeId;
	}

	public Retorno(String status, String entidadeId) {
		super();
		this.status = status;
		
		this.entidadeId = entidadeId;
	}

	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	public Retorno() {
		super();
	}
	
	

}
