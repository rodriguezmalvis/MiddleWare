package br.org.cac.integrador.modelo;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.org.cac.integrador.util.NumberUtil;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(	name = "listaImpCoparticipacao",
								procedureName = "SPR_ICM_LISTA_IMP_COPARTICIPACAO",
								resultClasses = {ImpCoparticipacao.class},
								parameters = {
										@StoredProcedureParameter(name = "id_comando", type = Integer.class, mode = ParameterMode.IN )
								})
})
public class ImpCoparticipacao {
	
	@EmbeddedId
	@JsonIgnore
	private ProcedimentoId procedimentoId;
	
	@Column(name="id_comando")
	@JsonIgnore
	private Integer idComando;
	
	@Column(name="cd_procedimento")
	private Integer cdProcedimento;
	
	@Column(name="tp_guia")
	private Character tpGuia;
	
	@Column(name="dt_realizacao")
	private Date dtRealizacao;
	
	@Column(name="vl_coparticipacao")
	@Access(AccessType.PROPERTY)
	private Double vlCoparticipacao;
	
	@Column(name="cd_prestador")
	private Integer cdPrestador;
	
	public ImpCoparticipacao(){
		
	}

	/** Getter para procedimentoId.
	 * @return o valor de procedimentoId.
	 */
	public ProcedimentoId getProcedimentoId() {
		return procedimentoId;
	}

	/** Setter para procedimentoId.
	 * @param procedimentoId o novo valor de procedimentoId.
	 */
	public void setProcedimentoId(ProcedimentoId procedimentoId) {
		this.procedimentoId = procedimentoId;
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

	/** Getter para tpGuia.
	 * @return o valor de tpGuia.
	 */
	public Character getTpGuia() {
		return tpGuia;
	}

	/** Setter para tpGuia.
	 * @param tpGuia o novo valor de tpGuia.
	 */
	public void setTpGuia(Character tpGuia) {
		this.tpGuia = tpGuia;
	}

	/** Getter para dtRealizacao.
	 * @return o valor de dtRealizacao.
	 */
	public Date getDtRealizacao() {
		return dtRealizacao;
	}

	/** Setter para dtRealizacao.
	 * @param dtRealizacao o novo valor de dtRealizacao.
	 */
	public void setDtRealizacao(Date dtRealizacao) {
		this.dtRealizacao = dtRealizacao;
	}

	/** Getter para vlCoparticipacao.
	 * @return o valor de vlCoparticipacao.
	 */
	public Double getVlCoparticipacao() {
		return vlCoparticipacao;
	}

	/** Setter para vlCoparticipacao.
	 * @param vlCoparticipacao o novo valor de vlCoparticipacao.
	 */
	public void setVlCoparticipacao(Double vlCoparticipacao) {
		this.vlCoparticipacao = NumberUtil.roundToScaleTwo(vlCoparticipacao);
	}

	/** Getter para cdPrestador.
	 * @return o valor de cdPrestador.
	 */
	public Integer getCdPrestador() {
		return cdPrestador;
	}

	/** Setter para cdPrestador.
	 * @param cdPrestador o novo valor de cdPrestador.
	 */
	public void setCdPrestador(Integer cdPrestador) {
		this.cdPrestador = cdPrestador;
	}
	
	
	
}
