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
@Table(name="MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE")
public class DeParaGrupoItemMensalidade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_grupo_item_mensalidade")
	private Integer idDeParaGrupoItemMensalidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_mensalidade")
	private DeParaMensalidade deParaMensalidade;
	
	@Column(name="f_devolucao", columnDefinition="bit")
	private Boolean fDevolucao;
	
	@Column(name="cd_inclusao_interno")
	private Integer cdInclusaoInterno;
	
	@Column(name="cd_inclusao_efetivo")
	private Integer cdInclusaoEfetivo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_status_envio_grupo_item_mens")
	private StatusEnvioGrupoItemMensalidade statusEnvioGrupoItemMensalidade;
	
	@OneToMany(mappedBy="deParaGrupoItemMensalidade", fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	private List<DeParaItemMensalidade> deParaItensMensalidade;
	
	public DeParaGrupoItemMensalidade() {
	
	}

	/** Getter para idDeParaGrupoItemMensalidade.
	 * @return o valor de idDeParaGrupoItemMensalidade.
	 */
	public Integer getIdDeParaGrupoItemMensalidade() {
		return idDeParaGrupoItemMensalidade;
	}

	/** Setter para idDeParaGrupoItemMensalidade.
	 * @param idDeParaGrupoItemMensalidade o novo valor de idDeParaGrupoItemMensalidade.
	 */
	public void setIdDeParaGrupoItemMensalidade(Integer idDeParaGrupoItemMensalidade) {
		this.idDeParaGrupoItemMensalidade = idDeParaGrupoItemMensalidade;
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

	/** Getter para cdInclusaoInterno.
	 * @return o valor de cdInclusaoInterno.
	 */
	public Integer getCdInclusaoInterno() {
		return cdInclusaoInterno;
	}

	/** Setter para cdInclusaoInterno.
	 * @param cdInclusaoInterno o novo valor de cdInclusaoInterno.
	 */
	public void setCdInclusaoInterno(Integer cdInclusaoInterno) {
		this.cdInclusaoInterno = cdInclusaoInterno;
	}

	/** Getter para cdInclusaoEfetivo.
	 * @return o valor de cdInclusaoEfetivo.
	 */
	public Integer getCdInclusaoEfetivo() {
		return cdInclusaoEfetivo;
	}

	/** Setter para cdInclusaoEfetivo.
	 * @param cdInclusaoEfetivo o novo valor de cdInclusaoEfetivo.
	 */
	public void setCdInclusaoEfetivo(Integer cdInclusaoEfetivo) {
		this.cdInclusaoEfetivo = cdInclusaoEfetivo;
	}

	/** Getter para statusEnvioGrupoItemMensalidade.
	 * @return o valor de statusEnvioGrupoItemMensalidade.
	 */
	public StatusEnvioGrupoItemMensalidade getStatusEnvioGrupoItemMensalidade() {
		return statusEnvioGrupoItemMensalidade;
	}

	/** Setter para statusEnvioGrupoItemMensalidade.
	 * @param statusEnvioGrupoItemMensalidade o novo valor de statusEnvioGrupoItemMensalidade.
	 */
	public void setStatusEnvioGrupoItemMensalidade(StatusEnvioGrupoItemMensalidade statusEnvioGrupoItemMensalidade) {
		this.statusEnvioGrupoItemMensalidade = statusEnvioGrupoItemMensalidade;
	}

	/** Getter para deParaItensMensalidade.
	 * @return o valor de deParaItensMensalidade.
	 */
	public List<DeParaItemMensalidade> getDeParaItensMensalidade() {
		return deParaItensMensalidade;
	}

	/** Setter para deParaItensMensalidade.
	 * @param deParaItensMensalidade o novo valor de deParaItensMensalidade.
	 */
	public void setDeParaItensMensalidade(List<DeParaItemMensalidade> deParaItensMensalidade) {
		this.deParaItensMensalidade = deParaItensMensalidade;
	}
	
	public String detalhaStatus() {
		return String.format("DeParaGrupoItemMensalidade Id: %s, Processamento Id: %s, Tipo: %s, "
				+ "Código de inclusão interno Id: %d, Código de inclusão efetivo (MV) Id: %d, "
				+ "Status: %s", 
				this.idDeParaGrupoItemMensalidade,
				( (this.deParaMensalidade != null) && (this.deParaMensalidade.getProcessamento() != null) ? 
						this.deParaMensalidade.getProcessamento().getIdProcessamento() : "[não identificado]" ),
				(this.fDevolucao ? "Devolução" : "Cobrança"),
				this.cdInclusaoInterno,
				this.cdInclusaoEfetivo,
				(this.statusEnvioGrupoItemMensalidade != null ? this.statusEnvioGrupoItemMensalidade.getCodStatus() : "[não identificado]")
				);
	}
		
}