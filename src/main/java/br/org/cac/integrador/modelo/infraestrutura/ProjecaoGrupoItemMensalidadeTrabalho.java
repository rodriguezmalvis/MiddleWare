package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@Entity
@NamedStoredProcedureQuery( name = "listaGruposItensMensalidade", 
							procedureName = "SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE", 
							resultClasses = { ProjecaoGrupoItemMensalidadeTrabalho.class }, 
							parameters = {
									@StoredProcedureParameter(name = "id_mensalidade_trabalho", type = Integer.class, mode = ParameterMode.IN) 
						  })
public class ProjecaoGrupoItemMensalidadeTrabalho {
	
	@Column(name="seq_mensalidade")
	private Integer seqMensalidade;
	
	@Id
	@Column(name="id_comando")
	private Integer idComando;
	
	@Column(name="f_devolucao", columnDefinition="bit")
	private Boolean fDevolucao;
	
	public ProjecaoGrupoItemMensalidadeTrabalho() {
		
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

	/** Getter para fDevolucao.
	 * @return o valor de fDevolucao.
	 */
	public Boolean getfDevolucao() {
		return fDevolucao;
	}

	/** Setter para fDevolucao.
	 * @param fDevolucao o novo valor de fDevolucao.
	 */
	public void setfDevolucao(Boolean fDevolucao) {
		this.fDevolucao = fDevolucao;
	}
	
}
