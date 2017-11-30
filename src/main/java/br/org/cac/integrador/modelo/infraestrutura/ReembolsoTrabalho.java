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

import br.org.cac.integrador.modelo.AtendimentoId;

@Entity
@Table(name="ICM_REEMBOLSO_TRABALHO")
public class ReembolsoTrabalho {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reembolso_trabalho")
	private Integer idReembolsoTrabalho;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@Embedded
	private AtendimentoId atendimentoId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_status_processamento")
	private StatusProcessamento statusProcessamento;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_reembolso_reprocessando")
	private ReembolsoTrabalho reembolsoReprocessando;
	
	@OneToMany(mappedBy="reembolsoTrabalho", fetch=FetchType.LAZY)
	private List<ErroProcessamentoReembolso> errosProcessamentoReembolso;
	
	public ReembolsoTrabalho(){
		
	}

	/** Getter para idReembolsoTrabalho.
	 * @return o valor de idReembolsoTrabalho.
	 */
	public Integer getIdReembolsoTrabalho() {
		return idReembolsoTrabalho;
	}

	/** Setter para idReembolsoTrabalho.
	 * @param idReembolsoTrabalho o novo valor de idReembolsoTrabalho.
	 */
	public void setIdReembolsoTrabalho(Integer idReembolsoTrabalho) {
		this.idReembolsoTrabalho = idReembolsoTrabalho;
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

	/** Getter para atendimentoId.
	 * @return o valor de atendimentoId.
	 */
	public AtendimentoId getAtendimentoId() {
		return atendimentoId;
	}

	/** Setter para atendimentoId.
	 * @param atendimentoId o novo valor de atendimentoId.
	 */
	public void setAtendimentoId(AtendimentoId atendimentoId) {
		this.atendimentoId = atendimentoId;
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

	/** Getter para reembolsoReprocessando.
	 * @return o valor de reembolsoReprocessando.
	 */
	public ReembolsoTrabalho getReembolsoReprocessando() {
		return reembolsoReprocessando;
	}

	/** Setter para reembolsoReprocessando.
	 * @param reembolsoReprocessando o novo valor de reembolsoReprocessando.
	 */
	public void setReembolsoReprocessando(ReembolsoTrabalho reembolsoReprocessando) {
		this.reembolsoReprocessando = reembolsoReprocessando;
	}

	/** Getter para errosProcessamentoReembolso.
	 * @return o valor de errosProcessamentoReembolso.
	 */
	public List<ErroProcessamentoReembolso> getErrosProcessamentoReembolso() {
		return errosProcessamentoReembolso;
	}

	/** Setter para errosProcessamentoReembolso.
	 * @param errosProcessamentoReembolso o novo valor de errosProcessamentoReembolso.
	 */
	public void setErrosProcessamentoReembolso(List<ErroProcessamentoReembolso> errosProcessamentoReembolso) {
		this.errosProcessamentoReembolso = errosProcessamentoReembolso;
	}

}
