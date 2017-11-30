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
		@NamedStoredProcedureQuery(name = "listaAtendimentosReembolsoPorHomologacao", procedureName = "SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO", resultClasses = {
				Atendimento.class }, parameters = {
						@StoredProcedureParameter(name = "dt_homologacao_inicial", type = Date.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "dt_homologacao_final", type = Date.class, mode = ParameterMode.IN) }),
		@NamedStoredProcedureQuery(name = "listaAtendimentosReembolso", procedureName = "SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO", resultClasses = {
				Atendimento.class }, parameters = {
						@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN) }),
		@NamedStoredProcedureQuery(name = "listaAtendimentosReembolsoPorDataCredito", procedureName = "SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_DATA_CREDITO", resultClasses = {
				Atendimento.class }, parameters = {
						@StoredProcedureParameter(name = "dt_credito_inicial", type = Date.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "dt_credito_final", type = Date.class, mode = ParameterMode.IN) })

})
public class Atendimento {

	@EmbeddedId
	private AtendimentoId atendimentoId;

	@Column(name = "dthr_homologacao")
	private Date dthrHomologacao;

	public Atendimento() {

	}

	/**
	 * Getter para atendimentoId.
	 * 
	 * @return o valor de atendimentoId.
	 */
	public AtendimentoId getAtendimentoId() {
		return atendimentoId;
	}

	/**
	 * Setter para atendimentoId.
	 * 
	 * @param atendimentoId
	 *            o novo valor de atendimentoId.
	 */
	public void setAtendimentoId(AtendimentoId atendimentoId) {
		this.atendimentoId = atendimentoId;
	}

	/**
	 * Getter para dthrHomologacao.
	 * 
	 * @return o valor de dthrHomologacao.
	 */
	public Date getDthrHomologacao() {
		return dthrHomologacao;
	}

	/**
	 * Setter para dthrHomologacao.
	 * 
	 * @param dthrHomologacao
	 *            o novo valor de dthrHomologacao.
	 */
	public void setDthrHomologacao(Date dthrHomologacao) {
		this.dthrHomologacao = dthrHomologacao;
	}

}
