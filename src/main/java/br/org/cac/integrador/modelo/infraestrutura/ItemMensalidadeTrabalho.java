package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ICM_ITEM_MENSALIDADE_TRABALHO")
public class ItemMensalidadeTrabalho {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_item_mensalidade_trabalho")
	private Integer idItemMensalidadeTrabalho;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_mensalidade_trabalho")
	private MensalidadeTrabalho mensalidadeTrabalho;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_grupo_item_mensalidade_trabalho")
	private GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho;
	
	@Column(name="id_comando")
	private Integer idComando;
	
	public ItemMensalidadeTrabalho(){
		
	}

	/** Getter para idItemMensalidadeTrabalho.
	 * @return o valor de idItemMensalidadeTrabalho.
	 */
	public Integer getIdItemMensalidadeTrabalho() {
		return idItemMensalidadeTrabalho;
	}

	/** Setter para idItemMensalidadeTrabalho.
	 * @param idItemMensalidadeTrabalho o novo valor de idItemMensalidadeTrabalho.
	 */
	public void setIdItemMensalidadeTrabalho(Integer idItemMensalidadeTrabalho) {
		this.idItemMensalidadeTrabalho = idItemMensalidadeTrabalho;
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

	/** Getter para grupoItemMensalidadeTrabalho.
	 * @return o valor de grupoItemMensalidadeTrabalho.
	 */
	public GrupoItemMensalidadeTrabalho getGrupoItemMensalidadeTrabalho() {
		return grupoItemMensalidadeTrabalho;
	}

	/** Setter para grupoItemMensalidadeTrabalho.
	 * @param grupoItemMensalidadeTrabalho o novo valor de grupoItemMensalidadeTrabalho.
	 */
	public void setGrupoItemMensalidadeTrabalho(GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) {
		this.grupoItemMensalidadeTrabalho = grupoItemMensalidadeTrabalho;
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
