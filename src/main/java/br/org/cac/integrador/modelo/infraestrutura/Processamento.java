package br.org.cac.integrador.modelo.infraestrutura;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="ICM_PROCESSAMENTO")
public class Processamento {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_processamento")
	private Integer idProcessamento;
	
	@OneToOne
	@JoinColumn(name="id_tipo_processamento")
	private TipoProcessamento tipoProcessamento;
	
	@Column(name="dthr_inicio_processamento")
	private Date dthrInicioProcessamento;
	
	@Column(name="dthr_fim_processamento")
	private Date dthrFimProcessamento;
	
	@Column(name="dthr_referencia_inicial")
	private Date dthrReferenciaInicial;
	
	@Column(name="dthr_referencia_final")
	private Date dthrReferenciaFinal;
	
	@OneToMany(mappedBy="processamento", fetch=FetchType.LAZY)
	private List<DeParaSubProcesso> deParaSubProcessos;
	
	public Processamento(){
		
	}

	/** Getter para idProcessamento.
	 * @return o valor de idProcessamento.
	 */
	public Integer getIdProcessamento() {
		return idProcessamento;
	}

	/** Setter para idProcessamento.
	 * @param idProcessamento o novo valor de idProcessamento.
	 */
	public void setIdProcessamento(Integer idProcessamento) {
		this.idProcessamento = idProcessamento;
	}

	/** Getter para tipoProcessamento.
	 * @return o valor de tipoProcessamento.
	 */
	public TipoProcessamento getTipoProcessamento() {
		return tipoProcessamento;
	}

	/** Setter para tipoProcessamento.
	 * @param tipoProcessamento o novo valor de tipoProcessamento.
	 */
	public void setTipoProcessamento(TipoProcessamento tipoProcessamento) {
		this.tipoProcessamento = tipoProcessamento;
	}

	/** Getter para dthrInicioProcessamento.
	 * @return o valor de dthrInicioProcessamento.
	 */
	public Date getDthrInicioProcessamento() {
		return dthrInicioProcessamento;
	}

	/** Setter para dthrInicioProcessamento.
	 * @param dthrInicioProcessamento o novo valor de dthrInicioProcessamento.
	 */
	public void setDthrInicioProcessamento(Date dthrInicioProcessamento) {
		this.dthrInicioProcessamento = dthrInicioProcessamento;
	}

	/** Getter para dthrFimProcessamento.
	 * @return o valor de dthrFimProcessamento.
	 */
	public Date getDthrFimProcessamento() {
		return dthrFimProcessamento;
	}

	/** Setter para dthrFimProcessamento.
	 * @param dthrFimProcessamento o novo valor de dthrFimProcessamento.
	 */
	public void setDthrFimProcessamento(Date dthrFimProcessamento) {
		this.dthrFimProcessamento = dthrFimProcessamento;
	}

	/** Getter para dthrReferenciaInicial.
	 * @return o valor de dthrReferenciaInicial.
	 */
	public Date getDthrReferenciaInicial() {
		return dthrReferenciaInicial;
	}

	/** Setter para dthrReferenciaInicial.
	 * @param dthrReferenciaInicial o novo valor de dthrReferenciaInicial.
	 */
	public void setDthrReferenciaInicial(Date dthrReferenciaInicial) {
		this.dthrReferenciaInicial = dthrReferenciaInicial;
	}

	/** Getter para dthrReferenciaFinal.
	 * @return o valor de dthrReferenciaFinal.
	 */
	public Date getDthrReferenciaFinal() {
		return dthrReferenciaFinal;
	}

	/** Setter para dthrReferenciaFinal.
	 * @param dthrReferenciaFinal o novo valor de dthrReferenciaFinal.
	 */
	public void setDthrReferenciaFinal(Date dthrHomologacaoFinal) {
		this.dthrReferenciaFinal = dthrHomologacaoFinal;
	}

	/** Getter para deParaSubProcessos.
	 * @return o valor de deParaSubProcessos.
	 */
	public List<DeParaSubProcesso> getDeParaSubProcessos() {
		return deParaSubProcessos;
	}

	/** Setter para deParaSubProcessos.
	 * @param deParaSubProcessos o novo valor de deParaSubProcessos.
	 */
	public void setDeParaSubProcessos(List<DeParaSubProcesso> deParaSubProcessos) {
		this.deParaSubProcessos = deParaSubProcessos;
	}	
	
}
