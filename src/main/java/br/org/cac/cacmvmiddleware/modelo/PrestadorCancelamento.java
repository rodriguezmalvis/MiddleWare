package br.org.cac.cacmvmiddleware.modelo;

public class PrestadorCancelamento {
	
	private String cdPrestador;
	private String dtCancelamento;
	private String dtReativacao;
	private String cdMotivoDesligamento;
	public String getCdPrestador() {
		return cdPrestador;
	}
	public void setCdPrestador(String cdPrestador) {
		this.cdPrestador = cdPrestador;
	}
	public String getDtCancelamento() {
		return dtCancelamento;
	}
	public void setDtCancelamento(String dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}
	public String getDtReativacao() {
		return dtReativacao;
	}
	public void setDtReativacao(String dtReativacao) {
		this.dtReativacao = dtReativacao;
	}
	public String getCdMotivoDesligamento() {
		return cdMotivoDesligamento;
	}
	public void setCdMotivoDesligamento(String cdMotivoDesligamento) {
		this.cdMotivoDesligamento = cdMotivoDesligamento;
	}
	public PrestadorCancelamento() {
		super();
	}
	
	

}
