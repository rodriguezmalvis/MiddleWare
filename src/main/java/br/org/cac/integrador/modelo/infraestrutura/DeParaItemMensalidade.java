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
@Table(name="MV_CAC_DE_PARA_ITEM_MENSALIDADE")
public class DeParaItemMensalidade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_item_mensalidade")
	private Integer idDeParaItemMensalidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_grupo_item_mensalidade")
	private DeParaGrupoItemMensalidade deParaGrupoItemMensalidade;
	
	@Column(name="id_comando")
	private Integer idComando;
	
	public DeParaItemMensalidade(){
		
	}

	/** Getter para idDeParaItemMensalidade.
	 * @return o valor de idDeParaItemMensalidade.
	 */
	public Integer getIdDeParaItemMensalidade() {
		return idDeParaItemMensalidade;
	}

	/** Setter para idDeParaItemMensalidade.
	 * @param idDeParaItemMensalidade o novo valor de idDeParaItemMensalidade.
	 */
	public void setIdDeParaItemMensalidade(Integer idDeParaItemMensalidade) {
		this.idDeParaItemMensalidade = idDeParaItemMensalidade;
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
