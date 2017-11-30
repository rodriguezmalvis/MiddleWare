package br.org.cac.integrador.modelo.infraestrutura;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.org.cac.integrador.modelo.FormaPagamento;

@Entity
@Table(name="ICM_MENSALIDADE_TRABALHO")
public class MensalidadeTrabalho {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mensalidade_trabalho")
	private Integer idMensalidadeTrabalho;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@ManyToOne
	@JoinColumn(name="id_status_processamento")
	private StatusProcessamento statusProcessamento;
	
	@Column(name="id_titular")
	private Integer idTitular;
	
	@Column(name="d_forma_pagamento")
	private FormaPagamento formaPagamento;
	
	@Column(name="dt_emissao")
	private Date dtEmissao;
	
	@Column(name="dt_referencia_prevista")
	private Date dtReferenciaPrevista;
	
	@Column(name="dt_vencimento")
	private Date dtVencimento;
	
	@OneToOne(fetch=FetchType.LAZY)	
	@JoinColumn(name="id_mensalidade_reprocessando")
	private MensalidadeTrabalho mensalidadeReprocessando;
	
	@OneToMany(mappedBy="mensalidadeTrabalho", fetch=FetchType.LAZY)
	private List<ItemMensalidadeTrabalho> itensMensalidadeTrabalho;
	
	@OneToMany(mappedBy="mensalidadeTrabalho", fetch=FetchType.LAZY)
	private List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalho;
	
	public MensalidadeTrabalho(){
		
	}

	/** Getter para idMensalidadeTrabalho.
	 * @return o valor de idMensalidadeTrabalho.
	 */
	public Integer getIdMensalidadeTrabalho() {
		return idMensalidadeTrabalho;
	}

	/** Setter para idMensalidadeTrabalho.
	 * @param idMensalidadeTrabalho o novo valor de idMensalidadeTrabalho.
	 */
	public void setIdMensalidadeTrabalho(Integer idMensalidadeTrabalho) {
		this.idMensalidadeTrabalho = idMensalidadeTrabalho;
	}

	/** Getter para processamento.
	 * @return o valor de processamento.
	 */
	public Processamento getProcessamento() {
		return processamento;
	}

	/** Setter para processamento.
	 * @param processamento o novo valor de processamento.
	 */
	public void setProcessamento(Processamento processamento) {
		this.processamento = processamento;
	}

	/** Getter para statusProcessamento.
	 * @return o valor de statusProcessamento.
	 */
	public StatusProcessamento getStatusProcessamento() {
		return statusProcessamento;
	}

	/** Setter para statusProcessamento.
	 * @param statusProcessamento o novo valor de statusProcessamento.
	 */
	public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
		this.statusProcessamento = statusProcessamento;
	}

	/** Getter para idTitular.
	 * @return o valor de idTitular.
	 */
	public Integer getIdTitular() {
		return idTitular;
	}

	/** Setter para idTitular.
	 * @param idTitular o novo valor de idTitular.
	 */
	public void setIdTitular(Integer idTitular) {
		this.idTitular = idTitular;
	}

	/** Getter para formaPagamento.
	 * @return o valor de formaPagamento.
	 */
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	/** Setter para formaPagamento.
	 * @param formaPagamento o novo valor de formaPagamento.
	 */
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	/** Getter para dtEmissao.
	 * @return o valor de dtEmissao.
	 */
	public Date getDtEmissao() {
		return dtEmissao;
	}

	/** Setter para dtEmissao.
	 * @param dtEmissao o novo valor de dtEmissao.
	 */
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	/** Getter para dtReferenciaPrevista.
	 * @return o valor de dtReferenciaPrevista.
	 */
	public Date getDtReferenciaPrevista() {
		return dtReferenciaPrevista;
	}

	/** Setter para dtReferenciaPrevista.
	 * @param dtReferenciaPrevista o novo valor de dtReferenciaPrevista.
	 */
	public void setDtReferenciaPrevista(Date dtReferenciaPrevista) {
		this.dtReferenciaPrevista = dtReferenciaPrevista;
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

	/** Getter para mensalidadeReprocessando.
	 * @return o valor de mensalidadeReprocessando.
	 */
	public MensalidadeTrabalho getMensalidadeReprocessando() {
		return mensalidadeReprocessando;
	}

	/** Setter para mensalidadeReprocessando.
	 * @param mensalidadeReprocessando o novo valor de mensalidadeReprocessando.
	 */
	public void setMensalidadeReprocessando(MensalidadeTrabalho mensalidadeReprocessando) {
		this.mensalidadeReprocessando = mensalidadeReprocessando;
	}

	/** Getter para itensMensalidadeTrabalho.
	 * @return o valor de itensMensalidadeTrabalho.
	 */
	public List<ItemMensalidadeTrabalho> getItensMensalidadeTrabalho() {
		return itensMensalidadeTrabalho;
	}

	/** Setter para itensMensalidadeTrabalho.
	 * @param itensMensalidadeTrabalho o novo valor de itensMensalidadeTrabalho.
	 */
	public void setItensMensalidadeTrabalho(List<ItemMensalidadeTrabalho> itensMensalidadeTrabalho) {
		this.itensMensalidadeTrabalho = itensMensalidadeTrabalho;
	}

	/** Getter para gruposItemMensalidadeTrabalho.
	 * @return o valor de gruposItemMensalidadeTrabalho.
	 */
	public List<GrupoItemMensalidadeTrabalho> getGruposItemMensalidadeTrabalho() {
		return gruposItemMensalidadeTrabalho;
	}

	/** Setter para gruposItemMensalidadeTrabalho.
	 * @param gruposItemMensalidadeTrabalho o novo valor de gruposItemMensalidadeTrabalho.
	 */
	public void setGruposItemMensalidadeTrabalho(List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalho) {
		this.gruposItemMensalidadeTrabalho = gruposItemMensalidadeTrabalho;
	}

}
