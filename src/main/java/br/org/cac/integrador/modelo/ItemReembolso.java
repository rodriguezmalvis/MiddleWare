package br.org.cac.integrador.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representa um item de reembolso, de acordo como definido
 * pela MV (corresponde à entidade ImpItreembolso
 * @author JCJ
 *
 */
@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaItensReembolso", 
							   procedureName = "SPR_ICM_LISTA_ITENS_REEMBOLSO", 
							   resultClasses = {ItemReembolso.class}, 
							   parameters = {
								@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_atendimento", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "cd_reembolso", type = Integer.class, mode = ParameterMode.IN)
							}) 
})
public class ItemReembolso {
	// Classe baseada nos campos de SPR_ICM_LISTA_ITENS_REEMBOLSO
	
	@EmbeddedId
	@JsonIgnore
	private ProcedimentoId procedimentoId;
	
	private Integer cdReembolso;
	
	private Integer cdProcedimento;
	
	private String dsAdicional;
	
	private Double qtCobrado;
	
	private Double vlCobrado;
	
	private Double vlProcedimento;
	
	private Double vlCoparticipacao;
	
	public ItemReembolso(){
		
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

	/** Getter para dsAdicional.
	 * @return o valor de dsAdicional.
	 */
	public String getDsAdicional() {
		return dsAdicional;
	}

	/** Setter para dsAdicional.
	 * @param dsAdicional o novo valor de dsAdicional.
	 */
	public void setDsAdicional(String dsAdicional) {
		this.dsAdicional = dsAdicional;
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
		this.qtCobrado = qtCobrado;
	}

	/** Getter para vlCobrado.
	 * @return o valor de vlCobrado.
	 */
	public Double getVlCobrado() {
		return vlCobrado;
	}

	/** Setter para vlCobrado.
	 * @param vlCobrado o novo valor de vlCobrado.
	 */
	public void setVlCobrado(Double vlCobrado) {
		this.vlCobrado = vlCobrado;
	}

	/** Getter para vlProcedimento.
	 * @return o valor de vlProcedimento.
	 */
	public Double getVlProcedimento() {
		return vlProcedimento;
	}

	/** Setter para vlProcedimento.
	 * @param vlProcedimento o novo valor de vlProcedimento.
	 */
	public void setVlProcedimento(Double vlProcedimento) {
		this.vlProcedimento = vlProcedimento;
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
		this.vlCoparticipacao = vlCoparticipacao;
	}
	
}
