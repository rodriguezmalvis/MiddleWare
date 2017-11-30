package br.org.cac.integrador.modelo;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import br.org.cac.integrador.util.NumberUtil;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaMensalidadesContrato",
							   procedureName = "SPR_ICM_LISTA_MENSALIDADES_CONTRATO",
							   resultClasses = {ProjecaoMensalidadeContrato.class },
							   parameters = {
									   @StoredProcedureParameter(name = "id_grupo_item_mensalidade_trabalho", type = Integer.class, mode = ParameterMode.IN)
							})			
})
public class ProjecaoMensalidadeContrato {
	
	@Column(name="seq_item_mensalidade")
	private Integer seqItemMensalidade;
	
	@Column(name="id_titular")
	private Integer idTitular;
	
	@Column(name="d_forma_pagamento")
	private FormaPagamento formaPagamento;
	
	@Column(name="tp_receita", length = 1)
	private String tpReceita;
	
	@Column(name="dt_referencia_prevista")
	private Date dtReferenciaPrevista;
	
	@Column(name="dt_vencimento")
	private Date dtVencimento;
	
	@Id
	@Column(name="id_comando")
	private Integer idComando;
	
	@Column(name="cd_contrato")
	private Integer cdContrato;
	
	@Column(name="cd_motivo_cancelamento")
	private Integer cdMotivoCancelamento;
	
	@Column(name="cd_nosso_numero")
	private String cdNossoNumero;
	
	@Column(name="ds_observacao")
	private String dsObservacao;
	
	@Column(name="dt_cancelamento")
	private Date dtCancelamento;
	
	@Column(name="dt_emissao")
	private Date dtEmissao;
	
	@Column(name="dt_vencimento_original")
	private Date dtVencimentoOriginal;
	
	@Column(name="dt_referencia_original")
	private Date dtReferenciaOriginal;
	
	@Column(name="nr_ano")
	private Integer nrAno;
	
	@Column(name="nr_mes")
	private Integer nrMes;
	
	@Column(name="nr_agencia")
	private String nrAgencia;
	
	@Column(name="nr_banco")
	private Integer nrBanco;
	
	@Column(name="nr_conta")
	private String nrConta;
	
	@Column(name="nr_documento")
	private String nrDocumento;
	
	@Column(name="nr_parcela")
	private Integer nrParcela;
	
	@Column(name="tp_quitacao", length=1)
	private String tpQuitacao;
	
	@Column(name="vl_juros_mora")
	@Access(AccessType.PROPERTY)
	private Double vlJurosMora;
	
	@Column(name="vl_mensalidade")
	@Access(AccessType.PROPERTY)
	private Double vlMensalidade;
	
	@Column(name="vl_pago")
	@Access(AccessType.PROPERTY)
	private Double vlPago;
	
	@Column(name="vl_percentual_multa")
	@Access(AccessType.PROPERTY)
	private Double vlPercentualMulta;
	
	@Column(name="cd_lcto_mensalidade")
	private Integer cdLctoMensalidade;
	
	@Column(name="f_provento", columnDefinition="bit")
	private Boolean fProvento;
	
	@Column(name="id_pessoa")
	private Integer idPessoa;
	
	@Column(name="cd_matricula")
	private Integer cdMatricula;
	
	@Column(name="ds_observacao_item_mensalidade")
	private String dsObservacaoItemMensalidade;
	
	@Column(name="vl_lancamento")
	@Access(AccessType.PROPERTY)
	private Double vlLancamento;
	
	@Column(name="tipo_rubrica_cac")
	private TipoRubricaCac tipoRubricaCac;
	
	public ProjecaoMensalidadeContrato(){
		
	}

	/** Getter para seqItemMensalidade.
	 * @return o valor de seqItemMensalidade.
	 */
	public Integer getSeqItemMensalidade() {
		return seqItemMensalidade;
	}

	/** Setter para seqItemMensalidade.
	 * @param seqItemMensalidade o novo valor de seqItemMensalidade.
	 */
	public void setSeqItemMensalidade(Integer seqItemMensalidade) {
		this.seqItemMensalidade = seqItemMensalidade;
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

	/** Getter para tpReceita.
	 * @return o valor de tpReceita.
	 */
	public String getTpReceita() {
		return tpReceita;
	}

	/** Setter para tpReceita.
	 * @param tpReceita o novo valor de tpReceita.
	 */
	public void setTpReceita(String tpReceita) {
		this.tpReceita = tpReceita;
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

	/** Getter para cdContrato.
	 * @return o valor de cdContrato.
	 */
	public Integer getCdContrato() {
		return cdContrato;
	}

	/** Setter para cdContrato.
	 * @param cdContrato o novo valor de cdContrato.
	 */
	public void setCdContrato(Integer cdContrato) {
		this.cdContrato = cdContrato;
	}

	/** Getter para cdMotivoCancelamento.
	 * @return o valor de cdMotivoCancelamento.
	 */
	public Integer getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}

	/** Setter para cdMotivoCancelamento.
	 * @param cdMotivoCancelamento o novo valor de cdMotivoCancelamento.
	 */
	public void setCdMotivoCancelamento(Integer cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
	}

	/** Getter para cdNossoNumero.
	 * @return o valor de cdNossoNumero.
	 */
	public String getCdNossoNumero() {
		return cdNossoNumero;
	}

	/** Setter para cdNossoNumero.
	 * @param cdNossoNumero o novo valor de cdNossoNumero.
	 */
	public void setCdNossoNumero(String cdNossoNumero) {
		this.cdNossoNumero = cdNossoNumero;
	}

	/** Getter para dsObservacao.
	 * @return o valor de dsObservacao.
	 */
	public String getDsObservacao() {
		return dsObservacao;
	}

	/** Setter para dsObservacao.
	 * @param dsObservacao o novo valor de dsObservacao.
	 */
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	/** Getter para dtCancelamento.
	 * @return o valor de dtCancelamento.
	 */
	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	/** Setter para dtCancelamento.
	 * @param dtCancelamento o novo valor de dtCancelamento.
	 */
	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
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

	/** Getter para dtVencimentoOriginal.
	 * @return o valor de dtVencimentoOriginal.
	 */
	public Date getDtVencimentoOriginal() {
		return dtVencimentoOriginal;
	}

	/** Setter para dtVencimentoOriginal.
	 * @param dtVencimentoOriginal o novo valor de dtVencimentoOriginal.
	 */
	public void setDtVencimentoOriginal(Date dtVencimentoOriginal) {
		this.dtVencimentoOriginal = dtVencimentoOriginal;
	}

	/** Getter para dtReferenciaOriginal.
	 * @return o valor de dtReferenciaOriginal.
	 */
	public Date getDtReferenciaOriginal() {
		return dtReferenciaOriginal;
	}

	/** Setter para dtReferenciaOriginal.
	 * @param dtReferenciaOriginal o novo valor de dtReferenciaOriginal.
	 */
	public void setDtReferenciaOriginal(Date dtReferenciaOriginal) {
		this.dtReferenciaOriginal = dtReferenciaOriginal;
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

	/** Getter para nrAgencia.
	 * @return o valor de nrAgencia.
	 */
	public String getNrAgencia() {
		return nrAgencia;
	}

	/** Setter para nrAgencia.
	 * @param nrAgencia o novo valor de nrAgencia.
	 */
	public void setNrAgencia(String nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	/** Getter para nrBanco.
	 * @return o valor de nrBanco.
	 */
	public Integer getNrBanco() {
		return nrBanco;
	}

	/** Setter para nrBanco.
	 * @param nrBanco o novo valor de nrBanco.
	 */
	public void setNrBanco(Integer nrBanco) {
		this.nrBanco = nrBanco;
	}

	/** Getter para nrConta.
	 * @return o valor de nrConta.
	 */
	public String getNrConta() {
		return nrConta;
	}

	/** Setter para nrConta.
	 * @param nrConta o novo valor de nrConta.
	 */
	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}

	/** Getter para nrDocumento.
	 * @return o valor de nrDocumento.
	 */
	public String getNrDocumento() {
		return nrDocumento;
	}

	/** Setter para nrDocumento.
	 * @param nrDocumento o novo valor de nrDocumento.
	 */
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	/** Getter para nrParcela.
	 * @return o valor de nrParcela.
	 */
	public Integer getNrParcela() {
		return nrParcela;
	}

	/** Setter para nrParcela.
	 * @param nrParcela o novo valor de nrParcela.
	 */
	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	/** Getter para tpQuitacao.
	 * @return o valor de tpQuitacao.
	 */
	public String getTpQuitacao() {
		return tpQuitacao;
	}

	/** Setter para tpQuitacao.
	 * @param tpQuitacao o novo valor de tpQuitacao.
	 */
	public void setTpQuitacao(String tpQuitacao) {
		this.tpQuitacao = tpQuitacao;
	}

	/** Getter para vlJurosMora.
	 * @return o valor de vlJurosMora.
	 */
	public Double getVlJurosMora() {
		return vlJurosMora;
	}

	/** Setter para vlJurosMora.
	 * @param vlJurosMora o novo valor de vlJurosMora.
	 */
	public void setVlJurosMora(Double vlJurosMora) {
		this.vlJurosMora = NumberUtil.roundToScaleTwo(vlJurosMora);
	}

	/** Getter para vlMensalidade.
	 * @return o valor de vlMensalidade.
	 */
	public Double getVlMensalidade() {
		return vlMensalidade;
	}

	/** Setter para vlMensalidade.
	 * @param vlMensalidade o novo valor de vlMensalidade.
	 */
	public void setVlMensalidade(Double vlMensalidade) {
		this.vlMensalidade = NumberUtil.roundToScaleTwo(vlMensalidade);
	}

	/** Getter para vlPago.
	 * @return o valor de vlPago.
	 */
	public Double getVlPago() {
		return vlPago;
	}

	/** Setter para vlPago.
	 * @param vlPago o novo valor de vlPago.
	 */
	public void setVlPago(Double vlPago) {
		this.vlPago = NumberUtil.roundToScaleTwo(vlPago);
	}

	/** Getter para vlPercentualMulta.
	 * @return o valor de vlPercentualMulta.
	 */
	public Double getVlPercentualMulta() {
		return vlPercentualMulta;
	}

	/** Setter para vlPercentualMulta.
	 * @param vlPercentualMulta o novo valor de vlPercentualMulta.
	 */
	public void setVlPercentualMulta(Double vlPercentualMulta) {
		this.vlPercentualMulta = NumberUtil.roundToScaleTwo(vlPercentualMulta);
	}

	/** Getter para cdLctoMensalidade.
	 * @return o valor de cdLctoMensalidade.
	 */
	public Integer getCdLctoMensalidade() {
		return cdLctoMensalidade;
	}

	/** Setter para cdLctoMensalidade.
	 * @param cdLctoMensalidade o novo valor de cdLctoMensalidade.
	 */
	public void setCdLctoMensalidade(Integer cdLctoMensalidade) {
		this.cdLctoMensalidade = cdLctoMensalidade;
	}

	/** Getter para idPessoa.
	 * @return o valor de idPessoa.
	 */
	public Integer getIdPessoa() {
		return idPessoa;
	}

	/** Setter para idPessoa.
	 * @param idPessoa o novo valor de idPessoa.
	 */
	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
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

	/** Getter para dsObservacaoItemMensalidade.
	 * @return o valor de dsObservacaoItemMensalidade.
	 */
	public String getDsObservacaoItemMensalidade() {
		return dsObservacaoItemMensalidade;
	}

	/** Setter para dsObservacaoItemMensalidade.
	 * @param dsObservacaoItemMensalidade o novo valor de dsObservacaoItemMensalidade.
	 */
	public void setDsObservacaoItemMensalidade(String dsObservacaoItemMensalidade) {
		this.dsObservacaoItemMensalidade = dsObservacaoItemMensalidade;
	}

	/** Getter para vlLancamento.
	 * @return o valor de vlLancamento.
	 */
	public Double getVlLancamento() {
		return vlLancamento;
	}

	/** Setter para vlLancamento.
	 * @param vlLancamento o novo valor de vlLancamento.
	 */
	public void setVlLancamento(Double vlLancamento) {
		this.vlLancamento = NumberUtil.roundToScaleTwo(vlLancamento);
	}

	/** Getter para fProvento.
	 * @return o valor de fProvento.
	 */
	public Boolean getfProvento() {
		return fProvento;
	}

	/** Setter para fProvento.
	 * @param fProvento o novo valor de fProvento.
	 */
	public void setfProvento(Boolean fProvento) {
		this.fProvento = fProvento;
	}

	/** Getter para tipoRubricaCac.
	 * @return o valor de tipoRubricaCac.
	 */
	public TipoRubricaCac getTipoRubricaCac() {
		return tipoRubricaCac;
	}

	/** Setter para tipoRubricaCac.
	 * @param tipoRubricaCac o novo valor de tipoRubricaCac.
	 */
	public void setTipoRubricaCac(TipoRubricaCac tipoRubricaCac) {
		this.tipoRubricaCac = tipoRubricaCac;
	}	
	
}
