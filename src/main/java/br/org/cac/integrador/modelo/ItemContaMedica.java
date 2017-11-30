package br.org.cac.integrador.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cac.integrador.modelo.infraestrutura.DeParaAjusteSubProcesso;
import br.org.cac.integrador.modelo.infraestrutura.MotivoAjuste;
import br.org.cac.integrador.util.NumberUtil;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(	name = "listaItensContaMedica",
								procedureName = "SPR_ICM_LISTA_ITENS_CONTA_MEDICA",
								resultClasses = {ItemContaMedica.class},
								parameters = {
										@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "id_atendimento", type = Integer.class, mode = ParameterMode.IN),
										@StoredProcedureParameter(name = "cd_conta_medica", type = Integer.class, mode = ParameterMode.IN)
								}
							)
})
public class ItemContaMedica {
	@EmbeddedId
	@JsonIgnore
	private ProcedimentoId procedimento;
	
	@Column
	private Integer cdContaMedica;
	
	@Column
	@JsonProperty("cdItcontaMedica")
	private Long cdItContaMedica;
	
	@Column
	private Integer cdLctoMensalidade;
	
	@Column
	private Integer cdMensContrato;
	
	@Column
	private Integer cdMotivo;
	
	@Column
	private Integer cdProcDigita;
	
	@Column
	private Integer cdProcedimento;
	
	@Column
	private Integer cdTabela;
	
	@Column
	private Integer cdTecnica;
	
	@Column
	private Integer cdTermo;
	
	@Column
	private Integer cdViaAcesso;
	
	@Column
	private String dsObservacao;
	
	@Column
	private String dsObservacaoGlosa;
	
	@Column
	private Date dtLancamento;
	
	@Column
	private Integer nrAnoCob;
	
	@Column
	private Integer nrMesCob;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double qtCobrado;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double qtFranquia;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double qtPaga;
	
	@Column
	private SimNao snFaturar;
	
	@Column
	private SimNao snHorarioNormal;
	
	@Column
	private SimNao snPago;
	
	@Column(length=2)
	private String tpSituacao;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlCalculado;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlFranquia;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlFranquiaCalculado;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlPago;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlPercentual;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlTotalCobrado;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlUnitarioCobrado;
	
	@Column
	@Access(AccessType.PROPERTY)
	private Double vlUnitPago;
	
	@Transient
	@JsonProperty("pagamentosEquipe")
	private List<ItemContaMedicaPagamento> itensContaMedicaPagamento;
	
	@Transient
	@JsonIgnore
	private List<DeParaAjusteSubProcesso> deParaAjustesSubProcesso = new ArrayList<>();

	/** Getter para procedimento.
	 * @return o valor de procedimento.
	 */
	public ProcedimentoId getProcedimento() {
		return procedimento;
	}

	/** Setter para procedimento.
	 * @param procedimento o novo valor de procedimento.
	 */
	public void setProcedimento(ProcedimentoId procedimento) {
		this.procedimento = procedimento;
	}

	/** Getter para cdContaMedica.
	 * @return o valor de cdContaMedica.
	 */
	public Integer getCdContaMedica() {
		return cdContaMedica;
	}

	/** Setter para cdContaMedica.
	 * @param cdContaMedica o novo valor de cdContaMedica.
	 */
	public void setCdContaMedica(Integer cdContaMedica) {
		this.cdContaMedica = cdContaMedica;
	}

	/** Getter para cdItContaMedica.
	 * @return o valor de cdItContaMedica.
	 */
	public Long getCdItContaMedica() {
		return cdItContaMedica;
	}

	/** Setter para cdItContaMedica.
	 * @param cdItContaMedica o novo valor de cdItContaMedica.
	 */
	public void setCdItContaMedica(Long cdItContaMedica) {
		this.cdItContaMedica = cdItContaMedica;
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

	/** Getter para cdMotivo.
	 * @return o valor de cdMotivo.
	 */
	public Integer getCdMotivo() {
		return cdMotivo;
	}

	/** Setter para cdMotivo.
	 * @param cdMotivo o novo valor de cdMotivo.
	 */
	public void setCdMotivo(Integer cdMotivo) {
		this.cdMotivo = cdMotivo;
	}

	/** Getter para cdProcDigita.
	 * @return o valor de cdProcDigita.
	 */
	public Integer getCdProcDigita() {
		return cdProcDigita;
	}

	/** Setter para cdProcDigita.
	 * @param cdProcDigita o novo valor de cdProcDigita.
	 */
	public void setCdProcDigita(Integer cdProcDigita) {
		this.cdProcDigita = cdProcDigita;
	}

	/** Getter para cdProcedimento.
	 * @return o valor de cdProcedimento.
	 */
	public Integer getCdProcedimento() {
		return cdProcedimento;
	}

	/** Setter para cdProcedimento.
	 * @param cdProcedimento o novo valor de cdProcedimento.
	 */
	public void setCdProcedimento(Integer cdProcedimento) {
		this.cdProcedimento = cdProcedimento;
	}

	/** Getter para cdTabela.
	 * @return o valor de cdTabela.
	 */
	public Integer getCdTabela() {
		return cdTabela;
	}

	/** Setter para cdTabela.
	 * @param cdTabela o novo valor de cdTabela.
	 */
	public void setCdTabela(Integer cdTabela) {
		this.cdTabela = cdTabela;
	}

	/** Getter para cdTecnica.
	 * @return o valor de cdTecnica.
	 */
	public Integer getCdTecnica() {
		return cdTecnica;
	}

	/** Setter para cdTecnica.
	 * @param cdTecnica o novo valor de cdTecnica.
	 */
	public void setCdTecnica(Integer cdTecnica) {
		this.cdTecnica = cdTecnica;
	}

	/** Getter para cdTermo.
	 * @return o valor de cdTermo.
	 */
	public Integer getCdTermo() {
		return cdTermo;
	}

	/** Setter para cdTermo.
	 * @param cdTermo o novo valor de cdTermo.
	 */
	public void setCdTermo(Integer cdTermo) {
		this.cdTermo = cdTermo;
	}

	/** Getter para cdViaAcesso.
	 * @return o valor de cdViaAcesso.
	 */
	public Integer getCdViaAcesso() {
		return cdViaAcesso;
	}

	/** Setter para cdViaAcesso.
	 * @param cdViaAcesso o novo valor de cdViaAcesso.
	 */
	public void setCdViaAcesso(Integer cdViaAcesso) {
		this.cdViaAcesso = cdViaAcesso;
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

	/** Getter para dsObservacaoGlosa.
	 * @return o valor de dsObservacaoGlosa.
	 */
	public String getDsObservacaoGlosa() {
		return dsObservacaoGlosa;
	}

	/** Setter para dsObservacaoGlosa.
	 * @param dsObservacaoGlosa o novo valor de dsObservacaoGlosa.
	 */
	public void setDsObservacaoGlosa(String dsObservacaoGlosa) {
		this.dsObservacaoGlosa = dsObservacaoGlosa;
	}

	/** Getter para dtLancamento.
	 * @return o valor de dtLancamento.
	 */
	public Date getDtLancamento() {
		return dtLancamento;
	}

	/** Setter para dtLancamento.
	 * @param dtLancamento o novo valor de dtLancamento.
	 */
	public void setDtLancamento(Date dtLancamento) {
		this.dtLancamento = dtLancamento;
	}

	/** Getter para nrAnoCob.
	 * @return o valor de nrAnoCob.
	 */
	public Integer getNrAnoCob() {
		return nrAnoCob;
	}

	/** Setter para nrAnoCob.
	 * @param nrAnoCob o novo valor de nrAnoCob.
	 */
	public void setNrAnoCob(Integer nrAnoCob) {
		this.nrAnoCob = nrAnoCob;
	}

	/** Getter para nrMesCob.
	 * @return o valor de nrMesCob.
	 */
	public Integer getNrMesCob() {
		return nrMesCob;
	}

	/** Setter para nrMesCob.
	 * @param nrMesCob o novo valor de nrMesCob.
	 */
	public void setNrMesCob(Integer nrMesCob) {
		this.nrMesCob = nrMesCob;
	}

	/** Getter para qtCobrado.
	 * @return o valor de qtCobrado.
	 */
	public Double getQtCobrado() {
		return qtCobrado;
	}

	/** Setter para qtCobrado.
	 * @param qtCobrado o novo valor de qtCobrado.
	 */
	public void setQtCobrado(Double qtCobrado) {
		this.qtCobrado = NumberUtil.roundToScaleTwo(qtCobrado);
	}

	/** Getter para qtFranquia.
	 * @return o valor de qtFranquia.
	 */
	public Double getQtFranquia() {
		return qtFranquia;
	}

	/** Setter para qtFranquia.
	 * @param qtFranquia o novo valor de qtFranquia.
	 */
	public void setQtFranquia(Double qtFranquia) {
		this.qtFranquia = NumberUtil.roundToScaleTwo(qtFranquia);
	}

	/** Getter para qtPaga.
	 * @return o valor de qtPaga.
	 */
	public Double getQtPaga() {
		return qtPaga;
	}

	/** Setter para qtPaga.
	 * @param qtPaga o novo valor de qtPaga.
	 */
	public void setQtPaga(Double qtPaga) {
		this.qtPaga = NumberUtil.roundToScaleTwo(qtPaga);
	}

	/** Getter para snFaturar.
	 * @return o valor de snFaturar.
	 */
	public SimNao getSnFaturar() {
		return snFaturar;
	}

	/** Setter para snFaturar.
	 * @param snFaturar o novo valor de snFaturar.
	 */
	public void setSnFaturar(SimNao snFaturar) {
		this.snFaturar = snFaturar;
	}

	/** Getter para snHorarioNormal.
	 * @return o valor de snHorarioNormal.
	 */
	public SimNao getSnHorarioNormal() {
		return snHorarioNormal;
	}

	/** Setter para snHorarioNormal.
	 * @param snHorarioNormal o novo valor de snHorarioNormal.
	 */
	public void setSnHorarioNormal(SimNao snHorarioNormal) {
		this.snHorarioNormal = snHorarioNormal;
	}

	/** Getter para snPago.
	 * @return o valor de snPago.
	 */
	public SimNao getSnPago() {
		return snPago;
	}

	/** Setter para snPago.
	 * @param snPago o novo valor de snPago.
	 */
	public void setSnPago(SimNao snPago) {
		this.snPago = snPago;
	}

	/** Getter para tpSituacao.
	 * @return o valor de tpSituacao.
	 */
	public String getTpSituacao() {
		return tpSituacao;
	}

	/** Setter para tpSituacao.
	 * @param tpSituacao o novo valor de tpSituacao.
	 */
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	/** Getter para vlCalculado.
	 * @return o valor de vlCalculado.
	 */
	public Double getVlCalculado() {
		return vlCalculado;
	}

	/** Setter para vlCalculado.
	 * @param vlCalculado o novo valor de vlCalculado.
	 */
	public void setVlCalculado(Double vlCalculado) {
		this.vlCalculado = NumberUtil.roundToScaleTwo(vlCalculado);
	}

	/** Getter para vlFranquia.
	 * @return o valor de vlFranquia.
	 */
	public Double getVlFranquia() {
		return vlFranquia;
	}

	/** Setter para vlFranquia.
	 * @param vlFranquia o novo valor de vlFranquia.
	 */
	public void setVlFranquia(Double vlFranquia) {
		this.vlFranquia = NumberUtil.roundToScaleTwo(vlFranquia);
	}

	/** Getter para vlFranquiaCalculado.
	 * @return o valor de vlFranquiaCalculado.
	 */
	public Double getVlFranquiaCalculado() {
		return vlFranquiaCalculado;
	}

	/** Setter para vlFranquiaCalculado.
	 * @param vlFranquiaCalculado o novo valor de vlFranquiaCalculado.
	 */
	public void setVlFranquiaCalculado(Double vlFranquiaCalculado) {
		this.vlFranquiaCalculado = NumberUtil.roundToScaleTwo(vlFranquiaCalculado);
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

	/** Getter para vlPercentual.
	 * @return o valor de vlPercentual.
	 */
	public Double getVlPercentual() {
		return vlPercentual;
	}

	/** Setter para vlPercentual.
	 * @param vlPercentual o novo valor de vlPercentual.
	 */
	public void setVlPercentual(Double vlPercentual) {
		this.vlPercentual = NumberUtil.roundToScaleTwo(vlPercentual);
	}

	/** Getter para vlTotalCobrado.
	 * @return o valor de vlTotalCobrado.
	 */
	public Double getVlTotalCobrado() {
		return vlTotalCobrado;
	}

	/** Setter para vlTotalCobrado.
	 * @param vlTotalCobrado o novo valor de vlTotalCobrado.
	 */
	public void setVlTotalCobrado(Double vlTotalCobrado) {
		this.vlTotalCobrado = NumberUtil.roundToScaleTwo(vlTotalCobrado);
	}

	/** Getter para vlUnitarioCobrado.
	 * @return o valor de vlUnitarioCobrado.
	 */
	public Double getVlUnitarioCobrado() {
		return vlUnitarioCobrado;
	}

	/** Setter para vlUnitarioCobrado.
	 * @param vlUnitarioCobrado o novo valor de vlUnitarioCobrado.
	 */
	public void setVlUnitarioCobrado(Double vlUnitarioCobrado) {
		this.vlUnitarioCobrado = NumberUtil.roundToScaleTwo(vlUnitarioCobrado);
	}

	/** Getter para vlUnitPago.
	 * @return o valor de vlUnitPago.
	 */
	public Double getVlUnitPago() {
		return vlUnitPago;
	}

	/** Setter para vlUnitPago.
	 * @param vlUnitPago o novo valor de vlUnitPago.
	 */
	public void setVlUnitPago(Double vlUnitPago) {
		this.vlUnitPago = NumberUtil.roundToScaleTwo(vlUnitPago);
	}

	/** Getter para itensContaMedicaPagamento.
	 * @return o valor de itensContaMedicaPagamento.
	 */
	public List<ItemContaMedicaPagamento> getItensContaMedicaPagamento() {
		return itensContaMedicaPagamento;
	}

	/** Setter para itensContaMedicaPagamento.
	 * @param itensContaMedicaPagamento o novo valor de itensContaMedicaPagamento.
	 */
	public void setItensContaMedicaPagamento(List<ItemContaMedicaPagamento> itensContaMedicaPagamento) {
		this.itensContaMedicaPagamento = itensContaMedicaPagamento;
	}
	
	/**
	 * Getter para DeParaAjustesSubProcesso
	 * @return o valor de deParaAjustesSubProcesso
	 */
	public List<DeParaAjusteSubProcesso> getDeParaAjustesSubProcesso() {
		return deParaAjustesSubProcesso;
	}
	
	/**
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 * @return
	 */
	public boolean addAjusteSubProcesso(String campo, String valorOriginal, String valorNovo, MotivoAjuste motivoAjuste) {
		DeParaAjusteSubProcesso ajuste = new DeParaAjusteSubProcesso(this.procedimento, campo, valorOriginal, valorNovo, motivoAjuste);		
		return this.deParaAjustesSubProcesso.add(ajuste);
	}
	
	/**
	 * 
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 * @return
	 */
	public boolean removeAjusteSubProcesso(String campo, String valorOriginal, String valorNovo, MotivoAjuste motivoAjuste) {
		DeParaAjusteSubProcesso ajuste = new DeParaAjusteSubProcesso(this.procedimento, campo, valorOriginal, valorNovo, motivoAjuste);		
		return this.deParaAjustesSubProcesso.remove(ajuste);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemContaMedica [procedimento=" + procedimento + ", cdContaMedica=" + cdContaMedica
				+ ", cdItContaMedica=" + cdItContaMedica + ", cdLctoMensalidade=" + cdLctoMensalidade
				+ ", cdMensContrato=" + cdMensContrato + ", cdMotivo=" + cdMotivo + ", cdProcDigita=" + cdProcDigita
				+ ", cdProcedimento=" + cdProcedimento + ", cdTabela=" + cdTabela + ", cdTecnica=" + cdTecnica
				+ ", cdTermo=" + cdTermo + ", cdViaAcesso=" + cdViaAcesso + ", dsObservacao=" + dsObservacao
				+ ", dsObservacaoGlosa=" + dsObservacaoGlosa + ", dtLancamento=" + dtLancamento + ", nrAnoCob="
				+ nrAnoCob + ", nrMesCob=" + nrMesCob + ", qtCobrado=" + qtCobrado + ", qtFranquia=" + qtFranquia
				+ ", qtPaga=" + qtPaga + ", snFaturar=" + snFaturar + ", snHorarioNormal=" + snHorarioNormal
				+ ", snPago=" + snPago + ", tpSituacao=" + tpSituacao + ", vlCalculado=" + vlCalculado + ", vlFranquia="
				+ vlFranquia + ", vlFranquiaCalculado=" + vlFranquiaCalculado + ", vlPago=" + vlPago + ", vlPercentual="
				+ vlPercentual + ", vlTotalCobrado=" + vlTotalCobrado + ", vlUnitarioCobrado=" + vlUnitarioCobrado
				+ ", vlUnitPago=" + vlUnitPago + "]";
	}
	
}
