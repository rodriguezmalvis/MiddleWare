package br.org.cac.integrador.modelo.infraestrutura;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO")
public class GrupoItemMensalidadeTrabalho {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_grupo_item_mensalidade_trabalho")
	private Integer idGrupoItemMensalidadeTrabalho;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_mensalidade_trabalho")
	private MensalidadeTrabalho mensalidadeTrabalho;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_grupo_item_mensalidade")
	private DeParaGrupoItemMensalidade deParaGrupoItemMensalidade;
	
	@OneToMany(mappedBy="grupoItemMensalidadeTrabalho", fetch=FetchType.LAZY, cascade=CascadeType.MERGE)
	private List<ItemMensalidadeTrabalho> itensMensalidadeTrabalho;
	
	@Column(name="f_devolucao",columnDefinition="bit")
	private Boolean fDevolucao;
	
	public GrupoItemMensalidadeTrabalho() {
	
	}

	/** Getter para idGrupoItemMensalidadeTrabalho.
	 * @return o valor de idGrupoItemMensalidadeTrabalho.
	 */
	public Integer getIdGrupoItemMensalidadeTrabalho() {
		return idGrupoItemMensalidadeTrabalho;
	}

	/** Setter para idGrupoItemMensalidadeTrabalho.
	 * @param idGrupoItemMensalidadeTrabalho o novo valor de idGrupoItemMensalidadeTrabalho.
	 */
	public void setIdGrupoItemMensalidadeTrabalho(Integer idGrupoItemMensalidadeTrabalho) {
		this.idGrupoItemMensalidadeTrabalho = idGrupoItemMensalidadeTrabalho;
	}

	/** Getter para mensalidadeTrabalho.
	 * @return o valor de mensalidadeTrabalho.
	 */
	public MensalidadeTrabalho getMensalidadeTrabalho() {
		return mensalidadeTrabalho;
	}

	/** Setter para mensalidadeTrabalho.
	 * @param mensalidadeTrabalho o novo valor de mensalidadeTrabalho.
	 */
	public void setMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho) {
		this.mensalidadeTrabalho = mensalidadeTrabalho;
	}

	/** Getter para deParaGrupoItemMensalidade.
	 * @return o valor de deParaGrupoItemMensalidade.
	 */
	public DeParaGrupoItemMensalidade getDeParaGrupoItemMensalidade() {
		return deParaGrupoItemMensalidade;
	}

	/** Setter para deParaGrupoItemMensalidade.
	 * @param deParaGrupoItemMensalidade o novo valor de deParaGrupoItemMensalidade.
	 */
	public void setDeParaGrupoItemMensalidade(DeParaGrupoItemMensalidade deParaGrupoItemMensalidade) {
		this.deParaGrupoItemMensalidade = deParaGrupoItemMensalidade;
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
		if (this.itensMensalidadeTrabalho != null) {
			this.itensMensalidadeTrabalho
				.stream()
				.forEach(imt -> imt.setGrupoItemMensalidadeTrabalho(this));				
		}
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fDevolucao == null) ? 0 : fDevolucao.hashCode());
		result = prime * result
				+ ((idGrupoItemMensalidadeTrabalho == null) ? 0 : idGrupoItemMensalidadeTrabalho.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GrupoItemMensalidadeTrabalho other = (GrupoItemMensalidadeTrabalho) obj;
		if (fDevolucao == null) {
			if (other.fDevolucao != null) {
				return false;
			}
		} else if (!fDevolucao.equals(other.fDevolucao)) {
			return false;
		}
		if (idGrupoItemMensalidadeTrabalho == null) {
			if (other.idGrupoItemMensalidadeTrabalho != null) {
				return false;
			}
		} else if (!idGrupoItemMensalidadeTrabalho.equals(other.idGrupoItemMensalidadeTrabalho)) {
			return false;
		}
		return true;
	}
	
	
}
