package br.org.cac.integrador.modelo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MensalidadeDevolucao {
	
	private String tpReembolso;
	
	private Integer cdReembolso;
	
	private String cdControleInterno;
	
	private Integer cdMatricula;
	
	private Integer cdMensContrato;

	private Double vlTotalOriginal;
	
	private Integer cdFornecedor;
	
	private Date dtVencimento;
	
	private Date dtInclusao;
	
	private String cdUsuarioInclusao;
	
	private Integer cdTipDoc;
	
	private Integer cdMultiEmpresa;
	
	private SimNao snNotificado;
	
	private SimNao snRecusado;
	
	private Integer nrAno;
	
	private Integer nrMes;
	
	@JsonProperty("itreembolsos")
	private List<ItemMensalidadeDevolucao> itensMensalidadeDevolucao;
	
	public MensalidadeDevolucao(){
		
	}

	/** Getter para tpReembolso.
	 * @return o valor de tpReembolso.
	 */
	public String getTpReembolso() {
		return tpReembolso;
	}

	/** Setter para tpReembolso.
	 * @param tpReembolso o novo valor de tpReembolso.
	 */
	public void setTpReembolso(String tpReembolso) {
		this.tpReembolso = tpReembolso;
	}

	/** Getter para cdReembolso.
	 * @return o valor de cdReembolso.
	 */
	public Integer getCdReembolso() {
		return cdReembolso;
	}

	/** Setter para cdReembolso.
	 * @param cdReembolso o novo valor de cdReembolso.
	 */
	public void setCdReembolso(Integer cdReembolso) {
		this.cdReembolso = cdReembolso;
	}

	/** Getter para cdControleInterno.
	 * @return o valor de cdControleInterno.
	 */
	public String getCdControleInterno() {
		return cdControleInterno;
	}

	/** Setter para cdControleInterno.
	 * @param cdControleInterno o novo valor de cdControleInterno.
	 */
	public void setCdControleInterno(String cdControleInterno) {
		this.cdControleInterno = cdControleInterno;
	}

	/** Getter para cdMatricula.
	 * @return o valor de cdMatricula.
	 */
	public Integer getCdMatricula() {
		return cdMatricula;
	}

	/** Setter para cdMatricula.
	 * @param cdMatricula o novo valor de cdMatricula.
	 */
	public void setCdMatricula(Integer cdMatricula) {
		this.cdMatricula = cdMatricula;
	}

	/** Getter para cdMensContrato.
	 * @return o valor de cdMensContrato.
	 */
	public Integer getCdMensContrato() {
		return cdMensContrato;
	}

	/** Setter para cdMensContrato.
	 * @param cdMensContrato o novo valor de cdMensContrato.
	 */
	public void setCdMensContrato(Integer cdMensContrato) {
		this.cdMensContrato = cdMensContrato;
	}

	/** Getter para vlTotalOriginal.
	 * @return o valor de vlTotalOriginal.
	 */
	public Double getVlTotalOriginal() {
		return vlTotalOriginal;
	}

	/** Setter para vlTotalOriginal.
	 * @param vlTotalOriginal o novo valor de vlTotalOriginal.
	 */
	public void setVlTotalOriginal(Double vlTotalOriginal) {
		this.vlTotalOriginal = vlTotalOriginal;
	}

	/** Getter para cdFornecedor.
	 * @return o valor de cdFornecedor.
	 */
	public Integer getCdFornecedor() {
		return cdFornecedor;
	}

	/** Setter para cdFornecedor.
	 * @param cdFornecedor o novo valor de cdFornecedor.
	 */
	public void setCdFornecedor(Integer cdFornecedor) {
		this.cdFornecedor = cdFornecedor;
	}

	/** Getter para dtVencimento.
	 * @return o valor de dtVencimento.
	 */
	public Date getDtVencimento() {
		return dtVencimento;
	}

	/** Setter para dtVencimento.
	 * @param dtVencimento o novo valor de dtVencimento.
	 */
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	/** Getter para dtInclusao.
	 * @return o valor de dtInclusao.
	 */
	public Date getDtInclusao() {
		return dtInclusao;
	}

	/** Setter para dtInclusao.
	 * @param dtInclusao o novo valor de dtInclusao.
	 */
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	/** Getter para cdUsuarioInclusao.
	 * @return o valor de cdUsuarioInclusao.
	 */
	public String getCdUsuarioInclusao() {
		return cdUsuarioInclusao;
	}

	/** Setter para cdUsuarioInclusao.
	 * @param cdUsuarioInclusao o novo valor de cdUsuarioInclusao.
	 */
	public void setCdUsuarioInclusao(String cdUsuarioInclusao) {
		this.cdUsuarioInclusao = cdUsuarioInclusao;
	}

	/** Getter para cdTipDoc.
	 * @return o valor de cdTipDoc.
	 */
	public Integer getCdTipDoc() {
		return cdTipDoc;
	}

	/** Setter para cdTipDoc.
	 * @param cdTipDoc o novo valor de cdTipDoc.
	 */
	public void setCdTipDoc(Integer cdTipDoc) {
		this.cdTipDoc = cdTipDoc;
	}

	/** Getter para cdMultiEmpresa.
	 * @return o valor de cdMultiEmpresa.
	 */
	public Integer getCdMultiEmpresa() {
		return cdMultiEmpresa;
	}

	/** Setter para cdMultiEmpresa.
	 * @param cdMultiEmpresa o novo valor de cdMultiEmpresa.
	 */
	public void setCdMultiEmpresa(Integer cdMultiEmpresa) {
		this.cdMultiEmpresa = cdMultiEmpresa;
	}

	/** Getter para snNotificado.
	 * @return o valor de snNotificado.
	 */
	public SimNao getSnNotificado() {
		return snNotificado;
	}

	/** Setter para snNotificado.
	 * @param snNotificado o novo valor de snNotificado.
	 */
	public void setSnNotificado(SimNao snNotificado) {
		this.snNotificado = snNotificado;
	}

	/** Getter para snRecusado.
	 * @return o valor de snRecusado.
	 */
	public SimNao getSnRecusado() {
		return snRecusado;
	}

	/** Setter para snRecusado.
	 * @param snRecusado o novo valor de snRecusado.
	 */
	public void setSnRecusado(SimNao snRecusado) {
		this.snRecusado = snRecusado;
	}

	/** Getter para itensMensalidadeDevolucao.
	 * @return o valor de itensMensalidadeDevolucao.
	 */
	public List<ItemMensalidadeDevolucao> getItensMensalidadeDevolucao() {
		return itensMensalidadeDevolucao;
	}

	/** Setter para itensMensalidadeDevolucao.
	 * @param itensMensalidadeDevolucao o novo valor de itensMensalidadeDevolucao.
	 */
	public void setItensMensalidadeDevolucao(List<ItemMensalidadeDevolucao> itensMensalidadeDevolucao) {
		this.itensMensalidadeDevolucao = itensMensalidadeDevolucao;
	}

	/** Getter para nrAno.
	 * @return o valor de nrAno.
	 */
	public Integer getNrAno() {
		return nrAno;
	}

	/** Setter para nrAno.
	 * @param nrAno o novo valor de nrAno.
	 */
	public void setNrAno(Integer nrAno) {
		this.nrAno = nrAno;
	}

	/** Getter para nrMes.
	 * @return o valor de nrMes.
	 */
	public Integer getNrMes() {
		return nrMes;
	}

	/** Setter para nrMes.
	 * @param nrMes o novo valor de nrMes.
	 */
	public void setNrMes(Integer nrMes) {
		this.nrMes = nrMes;
	}
}
