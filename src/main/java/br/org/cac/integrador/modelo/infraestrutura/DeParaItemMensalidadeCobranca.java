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
@Table(name="MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA")
@Deprecated
public class DeParaItemMensalidadeCobranca {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_item_mensalidade_cobranca")
	private Integer idDeParaItemMensalidadeCobranca;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_mensalidade_cobranca")
	private DeParaMensalidadeCobranca deParaMensalidadeCobranca;
	
	@Column(name="id_comando")
	private Integer idComando;
	
	public DeParaItemMensalidadeCobranca(){
		
	}

	/** Getter para idDeParaItemMensalidadeCobranca.
	 * @return o valor de idDeParaItemMensalidadeCobranca.
	 */
	public Integer getIdDeParaItemMensalidadeCobranca() {
		return idDeParaItemMensalidadeCobranca;
	}

	/** Setter para idDeParaItemMensalidadeCobranca.
	 * @param idDeParaItemMensalidadeCobranca o novo valor de idDeParaItemMensalidadeCobranca.
	 */
	public void setIdDeParaItemMensalidadeCobranca(Integer idDeParaItemMensalidadeCobranca) {
		this.idDeParaItemMensalidadeCobranca = idDeParaItemMensalidadeCobranca;
	}

	/** Getter para deParaMensalidadeCobranca.
	 * @return o valor de deParaMensalidadeCobranca.
	 */
	public DeParaMensalidadeCobranca getDeParaMensalidade() {
		return deParaMensalidadeCobranca;
	}

	/** Setter para deParaMensalidadeCobranca.
	 * @param deParaMensalidadeCobranca o novo valor de deParaMensalidadeCobranca.
	 */
	public void setDeParaMensalidadeCobranca(DeParaMensalidadeCobranca deParaMensalidade) {
		this.deParaMensalidadeCobranca = deParaMensalidade;
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
