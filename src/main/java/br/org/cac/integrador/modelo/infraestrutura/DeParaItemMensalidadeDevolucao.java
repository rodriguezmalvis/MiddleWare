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
@Table(name="MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO")
@Deprecated
public class DeParaItemMensalidadeDevolucao {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_item_mensalidade_devolucao")
	private Integer idDeParaItemMensalidadeDevolucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_mensalidade_devolucao")
	private DeParaMensalidadeDevolucao deParaMensalidadeDevolucao;
	
	@Column(name="id_comando")
	private Integer idComando;
	
	public DeParaItemMensalidadeDevolucao(){
		
	}

	/** Getter para idDeParaItemMensalidadeDevolucao.
	 * @return o valor de idDeParaItemMensalidadeDevolucao.
	 */
	public Integer getIdDeParaItemMensalidadeDevolucao() {
		return idDeParaItemMensalidadeDevolucao;
	}

	/** Setter para idDeParaItemMensalidadeDevolucao.
	 * @param idDeParaItemMensalidadeDevolucao o novo valor de idDeParaItemMensalidadeDevolucao.
	 */
	public void setIdDeParaItemMensalidadeDevolucao(Integer idDeParaItemMensalidadeDevolucao) {
		this.idDeParaItemMensalidadeDevolucao = idDeParaItemMensalidadeDevolucao;
	}

	/** Getter para deParaMensalidadeDevolucao.
	 * @return o valor de deParaMensalidadeDevolucao.
	 */
	public DeParaMensalidadeDevolucao getDeParaMensalidadeDevolucao() {
		return deParaMensalidadeDevolucao;
	}

	/** Setter para deParaMensalidadeDevolucao.
	 * @param deParaMensalidadeDevolucao o novo valor de deParaMensalidadeDevolucao.
	 */
	public void setDeParaMensalidadeDevolucao(DeParaMensalidadeDevolucao deParaMensalidadeDevolucao) {
		this.deParaMensalidadeDevolucao = deParaMensalidadeDevolucao;
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
