package br.org.cac.integrador.modelo.infraestrutura;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="MV_CAC_DE_PARA_MENSALIDADE_COBRANCA")
@Deprecated
public class DeParaMensalidadeCobranca {
	
	@Id
	private Integer idDeParaMensalidadeCobranca;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_de_para_mensalidade_cobranca", referencedColumnName="id_de_para_mensalidade")
	@MapsId
	private DeParaMensalidade deParaMensalidade;
	
	@Column(name="cd_mens_contrato_interno")
	private Integer cdMensContratoInterno;
	
	@Column(name="cd_mens_contrato_efetivo")
	private Integer cdMensContratoEfetivo;
	
	@OneToMany(mappedBy="deParaMensalidadeCobranca", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private List<DeParaItemMensalidadeCobranca> deParaItensMensalidadeCobranca;	
	
	public DeParaMensalidadeCobranca(){
		
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
		if (this.deParaMensalidade != null){
			this.idDeParaMensalidadeCobranca = this.deParaMensalidade.getIdDeParaMensalidade();
		}
	}

	/** Getter para cdMensContratoInterno.
	 * @return o valor de cdMensContratoInterno.
	 */
	public Integer getCdMensContratoInterno() {
		return cdMensContratoInterno;
	}

	/** Setter para cdMensContratoInterno.
	 * @param cdMensContratoInterno o novo valor de cdMensContratoInterno.
	 */
	public void setCdMensContratoInterno(Integer cdMensContratoInterno) {
		this.cdMensContratoInterno = cdMensContratoInterno;
	}

	/** Getter para cdMensContratoEfetivo.
	 * @return o valor de cdMensContratoEfetivo.
	 */
	public Integer getCdMensContratoEfetivo() {
		return cdMensContratoEfetivo;
	}

	/** Setter para cdMensContratoEfetivo.
	 * @param cdMensContratoEfetivo o novo valor de cdMensContratoEfetivo.
	 */
	public void setCdMensContratoEfetivo(Integer cdMensContratoEfetivo) {
		this.cdMensContratoEfetivo = cdMensContratoEfetivo;
	}

	/** Getter para deParaItensMensalidadeCobranca.
	 * @return o valor de deParaItensMensalidadeCobranca.
	 */
	public List<DeParaItemMensalidadeCobranca> getDeParaItensMensalidadeCobranca() {
		return deParaItensMensalidadeCobranca;
	}

	/** Setter para deParaItensMensalidadeCobranca.
	 * @param deParaItensMensalidadeCobranca o novo valor de deParaItensMensalidadeCobranca.
	 */
	public void setDeParaItensMensalidadeCobranca(List<DeParaItemMensalidadeCobranca> deParaItensMensalidadeCobranca) {
		this.deParaItensMensalidadeCobranca = deParaItensMensalidadeCobranca;
	}
		
}
