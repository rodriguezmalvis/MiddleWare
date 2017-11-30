package br.org.cac.integrador.modelo.infraestrutura;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.org.cac.integrador.modelo.FormaPagamento;

@Entity
@Table(name="MV_CAC_DE_PARA_MENSALIDADE")
public class DeParaMensalidade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_mensalidade")
	private Integer idDeParaMensalidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@Column(name="id_titular")
	private Integer idTitular;
	
	@Column(name="d_forma_pagamento")
	private FormaPagamento formaPagamento;
	
	@Column(name="dt_emissao")
	private Date dtEmissao;
	
	@Column(name="dt_referencia_efetiva")
	private Date dtReferenciaEfetiva;
	
	@Column(name="dt_vencimento")
	private Date dtVencimento;
	
	@OneToMany(mappedBy="deParaMensalidade", fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	private List<DeParaGrupoItemMensalidade> deParaGruposItensMensalidade;
	
	@Transient
	private DeParaMensalidadeCobranca deParaMensalidadeCobranca;
	
	@Transient
	private List<DeParaMensalidadeDevolucao> deParaMensalidadeDevolucao;
	
	public DeParaMensalidade(){
		
	}

	/** Getter para idDeParaMensalidade.
	 * @return o valor de idDeParaMensalidade.
	 */
	public Integer getIdDeParaMensalidade() {
		return idDeParaMensalidade;
	}

	/** Setter para idDeParaMensalidade.
	 * @param idDeParaMensalidade o novo valor de idDeParaMensalidade.
	 */
	public void setIdDeParaMensalidade(Integer idDeParaMensalidade) {
		this.idDeParaMensalidade = idDeParaMensalidade;
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

	/** Getter para dtReferenciaEfetiva.
	 * @return o valor de dtReferenciaEfetiva.
	 */
	public Date getDtReferenciaEfetiva() {
		return dtReferenciaEfetiva;
	}

	/** Setter para dtReferenciaEfetiva.
	 * @param dtReferenciaEfetiva o novo valor de dtReferenciaEfetiva.
	 */
	public void setDtReferenciaEfetiva(Date dtReferenciaEfetiva) {
		this.dtReferenciaEfetiva = dtReferenciaEfetiva;
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

	/** Getter para deParaGruposItensMensalidade.
	 * @return o valor de deParaGruposItensMensalidade.
	 */
	public List<DeParaGrupoItemMensalidade> getDeParaGruposItensMensalidade() {
		return deParaGruposItensMensalidade;
	}

	/** Setter para deParaGruposItensMensalidade.
	 * @param deParaGruposItensMensalidade o novo valor de deParaGruposItensMensalidade.
	 */
	public void setDeParaGruposItensMensalidade(List<DeParaGrupoItemMensalidade> deParaGruposItensMensalidade) {
		this.deParaGruposItensMensalidade = deParaGruposItensMensalidade;
	}

	/** Getter para deParaMensalidadeCobranca.
	 * @return o valor de deParaMensalidadeCobranca.
	 */
	public DeParaMensalidadeCobranca getDeParaMensalidadeCobranca() {
		return deParaMensalidadeCobranca;
	}

	/** Setter para deParaMensalidadeCobranca.
	 * @param deParaMensalidadeCobranca o novo valor de deParaMensalidadeCobranca.
	 */
	public void setDeParaMensalidadeCobranca(DeParaMensalidadeCobranca deParaMensalidadeCobranca) {
		this.deParaMensalidadeCobranca = deParaMensalidadeCobranca;
	}

	/** Getter para deParaMensalidadeDevolucao.
	 * @return o valor de deParaMensalidadeDevolucao.
	 */
	public List<DeParaMensalidadeDevolucao> getDeParaMensalidadeDevolucao() {
		return deParaMensalidadeDevolucao;
	}

	/** Setter para deParaMensalidadeDevolucao.
	 * @param deParaMensalidadeDevolucao o novo valor de deParaMensalidadeDevolucao.
	 */
	public void setDeParaMensalidadeDevolucao(List<DeParaMensalidadeDevolucao> deParaMensalidadeDevolucao) {
		this.deParaMensalidadeDevolucao = deParaMensalidadeDevolucao;
	}

}
