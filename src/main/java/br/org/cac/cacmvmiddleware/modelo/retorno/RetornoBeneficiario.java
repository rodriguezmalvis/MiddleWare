package br.org.cac.cacmvmiddleware.modelo.retorno;

public class RetornoBeneficiario {
	
	private String status;
	
	private Beneficiario beneficiario;
	
	private String mensagem;
	
	private String entidadeId;

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

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public RetornoBeneficiario() {
		super();
	}
	
	

}
