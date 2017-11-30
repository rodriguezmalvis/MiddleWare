package br.org.cac.integrador.modelo.infraestrutura;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

import br.org.cac.integrador.modelo.SubProcessoId;

@Entity
@Table(name="ICM_LOTE_TRABALHO")
public class LoteTrabalho {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_lote_trabalho")
	private Integer idLoteTrabalho;
	
	@ManyToOne
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@Embedded
	private SubProcessoId subProcesso;
	
	@OneToOne
	@JoinColumn(name="id_status_processamento")
	private StatusProcessamento statusProcessamento;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_lote_reprocessando")
	private LoteTrabalho loteReprocessando;
	
	@OneToMany(mappedBy="loteTrabalho")	
	private List<ErroProcessamento> errosProcessamento;
	
	
	public LoteTrabalho(){
		
	}


	/** Getter para idLoteTrabalho.
	 * @return o valor de idLoteTrabalho.
	 */
	public Integer getIdLoteTrabalho() {
		return idLoteTrabalho;
	}

	/** Setter para idLoteTrabalho.
	 * @param idLoteTrabalho o novo valor de idLoteTrabalho.
	 */
	public void setIdLoteTrabalho(Integer idLoteTrabalho) {
		this.idLoteTrabalho = idLoteTrabalho;
	}

	/** Getter para processamento.
	 * @return o valor de processamento.
	 */
	public Processamento getProcessamento() {
		return processamento;
	}

	/** Setter para processamento.
	 * @param processamento o novo valor de processamento.
	 */
	public void setProcessamento(Processamento processamento) {
		this.processamento = processamento;
	}

	/** Getter para subProcesso.
	 * @return o valor de subProcesso.
	 */
	public SubProcessoId getSubProcesso() {
		return subProcesso;
	}

	/** Setter para subProcesso.
	 * @param subProcesso o novo valor de subProcesso.
	 */
	public void setSubProcesso(SubProcessoId subProcesso) {
		this.subProcesso = subProcesso;
	}

	/** Getter para statusProcessamento.
	 * @return o valor de statusProcessamento.
	 */
	public StatusProcessamento getStatusProcessamento() {
		return statusProcessamento;
	}

	/** Setter para statusProcessamento.
	 * @param statusProcessamento o novo valor de statusProcessamento.
	 */
	public void setStatusProcessamento(StatusProcessamento statusProcessamento) {
		this.statusProcessamento = statusProcessamento;
	}

	/** Getter para loteReprocessando.
	 * @return o valor de loteReprocessando.
	 */
	public LoteTrabalho getLoteReprocessando() {
		return loteReprocessando;
	}

	/** Setter para loteReprocessando.
	 * @param loteReprocessando o novo valor de loteReprocessando.
	 */
	public void setLoteReprocessando(LoteTrabalho loteReprocessando) {
		this.loteReprocessando = loteReprocessando;
	}

	/** Getter para errosProcessamento.
	 * @return o valor de errosProcessamento.
	 */
	public List<ErroProcessamento> getErrosProcessamento() {
		return errosProcessamento;
	}

	/** Setter para errosProcessamento.
	 * @param errosProcessamento o novo valor de errosProcessamento.
	 */
	public void setErrosProcessamento(List<ErroProcessamento> errosProcessamento) {
		this.errosProcessamento = errosProcessamento;
	}
	
}
