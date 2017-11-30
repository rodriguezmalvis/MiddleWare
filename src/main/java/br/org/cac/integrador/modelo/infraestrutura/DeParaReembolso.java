package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cac.integrador.modelo.AtendimentoId;

@Entity
@Table(name="MV_CAC_DE_PARA_REEMBOLSO")
public class DeParaReembolso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_reembolso")
	private Integer idDeParaReembolso;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@Embedded
	private AtendimentoId atendimentoId;
	
	@Column(name="cd_reembolso_inclusao")
	private Integer cdReembolsoInclusao;
	
	@Column(name="cd_reembolso_efetivo")
	private Integer cdReembolsoEfetivo;
	
	public DeParaReembolso(){
		
	}

	/** Getter para idDeParaReembolso.
	 * @return o valor de idDeParaReembolso.
	 */
	public Integer getIdDeParaReembolso() {
		return idDeParaReembolso;
	}

	/** Setter para idDeParaReembolso.
	 * @param idDeParaReembolso o novo valor de idDeParaReembolso.
	 */
	public void setIdDeParaReembolso(Integer idDeParaReembolso) {
		this.idDeParaReembolso = idDeParaReembolso;
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

	/** Getter para cdReembolsoInclusao.
	 * @return o valor de cdReembolsoInclusao.
	 */
	public Integer getCdReembolsoInclusao() {
		return cdReembolsoInclusao;
	}

	/** Setter para cdReembolsoInclusao.
	 * @param cdReembolsoInclusao o novo valor de cdReembolsoInclusao.
	 */
	public void setCdReembolsoInclusao(Integer cdReembolsoInclusao) {
		this.cdReembolsoInclusao = cdReembolsoInclusao;
	}

	/** Getter para cdReembolsoEfetivo.
	 * @return o valor de cdReembolsoEfetivo.
	 */
	public Integer getCdReembolsoEfetivo() {
		return cdReembolsoEfetivo;
	}

	/** Setter para cdReembolsoEfetivo.
	 * @param cdReembolsoEfetivo o novo valor de cdReembolsoEfetivo.
	 */
	public void setCdReembolsoEfetivo(Integer cdReembolsoEfetivo) {
		this.cdReembolsoEfetivo = cdReembolsoEfetivo;
	}
	
}
