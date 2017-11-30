package br.org.cac.integrador.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaProjecoesMensalidadesTrabalho", 
							   procedureName = "SPR_ICM_LISTA_MENSALIDADES_TRABALHO", 
							   resultClasses = {ProjecaoMensalidadeTrabalho.class }, 
							   parameters = {
								@StoredProcedureParameter(name = "dt_referencia_atual", type = Date.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "dt_geracao_inicial", type = Date.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "dt_geracao_final", type = Date.class, mode = ParameterMode.IN)
							}),
	@NamedStoredProcedureQuery(name = "listaProjecoesMensalidadesTrabalhoPorDiops", 
	   procedureName = "SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS", 
	   resultClasses = {ProjecaoMensalidadeTrabalho.class }, 
	   parameters = {
			   @StoredProcedureParameter(name = "dt_referencia_diops", type = Date.class, mode = ParameterMode.IN),
			   @StoredProcedureParameter(name = "dt_vencimento_limite", type = Date.class, mode = ParameterMode.IN)
	}),
	@NamedStoredProcedureQuery(name = "listaProjecoesMensalidadesTrabalhoBoleto", 
	   procedureName = "SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO", 
	   resultClasses = {ProjecaoMensalidadeTrabalho.class }, 
	   parameters = {
			   @StoredProcedureParameter(name = "dt_referencia", type = Date.class, mode = ParameterMode.IN),
			   @StoredProcedureParameter(name = "dt_vencimento_base", type = Date.class, mode = ParameterMode.IN),
			   @StoredProcedureParameter(name = "dt_vencimento_limite", type = Date.class, mode = ParameterMode.IN)
	}),
	@NamedStoredProcedureQuery(name = "listaProjecoesMensalidadesTrabalhoFolha", 
	   procedureName = "SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA", 
	   resultClasses = {ProjecaoMensalidadeTrabalho.class }, 
	   parameters = {
			   @StoredProcedureParameter(name = "dt_referencia", type = Date.class, mode = ParameterMode.IN)
	})		
})
public class ProjecaoMensalidadeTrabalho {
	
	@Column(name="seq_mensalidade")
	private Integer seqMensalidade;
	
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
	
	@Id
	@Column(name="id_comando")
	private Integer idComando;
		
	public ProjecaoMensalidadeTrabalho(){
		
	}

	/** Getter para seqMensalidade.
	 * @return o valor de seqMensalidade.
	 */
	public Integer getSeqMensalidade() {
		return seqMensalidade;
	}

	/** Setter para seqMensalidade.
	 * @param seqMensalidade o novo valor de seqMensalidade.
	 */
	public void setSeqMensalidade(Integer seqMensalidade) {
		this.seqMensalidade = seqMensalidade;
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

	/** Getter para idComando.
	 * @return o valor de idComando.
	 */
	public Integer getIdComando() {
		return idComando;
	}

	/** Setter para idComando.
	 * @param idComando o novo valor de idComando.
	 */
	public void setIdComando(Integer idComando) {
		this.idComando = idComando;
	}
	
}
