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
import javax.persistence.Table;

@Entity
@Table(name="MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO")
@Deprecated
public class DeParaMensalidadeDevolucao {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_mensalidade_devolucao")
	private Integer idDeParaMensalidadeDevolucao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_mensalidade")
	private DeParaMensalidade deParaMensalidade;
	
	@Column(name="cd_devolucao_interno")
	private Integer cdDevolucaoInterno;
	
	@Column(name="cd_devolucao_efetivo")
	private Integer cdDevolucaoEfetivo;
	
	@OneToMany(mappedBy="deParaMensalidadeDevolucao", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<DeParaItemMensalidadeDevolucao> deParaItensMensalidadeDevolucao;
	
	public DeParaMensalidadeDevolucao(){
		
	}

	/** Getter para deParaMensalidade.
	 * @return o valor de deParaMensalidade.
	 */
	public DeParaMensalidade getDeParaMensalidade() {
		return deParaMensalidade;
	}

	/** Setter para deParaMensalidade.
	 * @param deParaMensalidade o novo valor de deParaMensalidade.
	 */
	public void setDeParaMensalidade(DeParaMensalidade deParaMensalidade) {
		this.deParaMensalidade = deParaMensalidade;
	}

	/** Getter para cdDevolucaoInterno.
	 * @return o valor de cdDevolucaoInterno.
	 */
	public Integer getCdDevolucaoInterno() {
		return cdDevolucaoInterno;
	}

	/** Setter para cdDevolucaoInterno.
	 * @param cdDevolucaoInterno o novo valor de cdDevolucaoInterno.
	 */
	public void setCdDevolucaoInterno(Integer cdDevolucaoInterno) {
		this.cdDevolucaoInterno = cdDevolucaoInterno;
	}

	/** Getter para cdDevolucaoEfetivo.
	 * @return o valor de cdDevolucaoEfetivo.
	 */
	public Integer getCdDevolucaoEfetivo() {
		return cdDevolucaoEfetivo;
	}

	/** Setter para cdDevolucaoEfetivo.
	 * @param cdDevolucaoEfetivo o novo valor de cdDevolucaoEfetivo.
	 */
	public void setCdDevolucaoEfetivo(Integer cdDevolucaoEfetivo) {
		this.cdDevolucaoEfetivo = cdDevolucaoEfetivo;
	}

	/** Getter para deParaItensMensalidadeDevolucao.
	 * @return o valor de deParaItensMensalidadeDevolucao.
	 */
	public List<DeParaItemMensalidadeDevolucao> getDeParaItensMensalidadeDevolucao() {
		return deParaItensMensalidadeDevolucao;
	}

	/** Setter para deParaItensMensalidadeDevolucao.
	 * @param deParaItensMensalidadeDevolucao o novo valor de deParaItensMensalidadeDevolucao.
	 */
	public void setDeParaItensMensalidadeDevolucao(List<DeParaItemMensalidadeDevolucao> deParaItensMensalidadeDevolucao) {
		this.deParaItensMensalidadeDevolucao = deParaItensMensalidadeDevolucao;
	}
}
