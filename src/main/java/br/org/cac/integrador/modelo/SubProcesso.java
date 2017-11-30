package br.org.cac.integrador.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaSubProcessoPorHomologacao", 
							   procedureName = "SPR_ICM_LISTA_SUB_PROCESSO_POR_HOMOLOGACAO", 
							   resultClasses = {SubProcesso.class }, 
							   parameters = {
								@StoredProcedureParameter(name = "dt_homologacao_inicial", type = Date.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "dt_homologacao_final", type = Date.class, mode = ParameterMode.IN)
							}),
	@NamedStoredProcedureQuery(name = "listaSubProcessoPorDataCredito", 
	   procedureName = "SPR_ICM_LISTA_SUB_PROCESSO_POR_DATA_CREDITO", 
	   resultClasses = {SubProcesso.class }, 
	   parameters = {
		@StoredProcedureParameter(name = "dt_credito_inicial", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "dt_credito_final", type = Date.class, mode = ParameterMode.IN)
	}),
	@NamedStoredProcedureQuery(name = "listaSubProcessoDiretoPorHomologacao", 
	   procedureName = "SPR_ICM_LISTA_SUB_PROCESSO_DIRETO_POR_HOMOLOGACAO", 
	   resultClasses = {SubProcesso.class }, 
	   parameters = {
		@StoredProcedureParameter(name = "dt_homologacao_inicial", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "dt_homologacao_final", type = Date.class, mode = ParameterMode.IN)
	})
})
public class SubProcesso {

	@EmbeddedId
	private SubProcessoId subProcessoId;
	
	@Column(name="dthr_homologacao")
	private Date dthrHomologacao;
	
	public SubProcesso(){
		
	}

	/** Getter para subProcessoId.
	 * @return o valor de subProcessoId.
	 */
	public SubProcessoId getSubProcessoId() {
		return subProcessoId;
	}

	/** Setter para subProcessoId.
	 * @param subProcessoId o novo valor de subProcessoId.
	 */
	public void setSubProcesso(SubProcessoId subProcessoId) {
		this.subProcessoId = subProcessoId;
	}

	/** Getter para dthrHomologacao.
	 * @return o valor de dthrHomologacao.
	 */
	public Date getDthrHomologacao() {
		return dthrHomologacao;
	}

	/** Setter para dthrHomologacao.
	 * @param dthrHomologacao o novo valor de dthrHomologacao.
	 */
	public void setDthrHomologacao(Date dthrHomologacao) {
		this.dthrHomologacao = dthrHomologacao;
	}
	
}
