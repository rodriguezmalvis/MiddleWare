package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cac.integrador.modelo.ProcedimentoId;

@Entity
@Table(name="MV_CAC_DE_PARA_PROCEDIMENTO")
public class DeParaProcedimento {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_procedimento")
	private Integer idDeParaProcedimento;
	
	@ManyToOne
	@JoinColumn(name="id_de_para_atendimento")
	private DeParaAtendimento deParaAtendimento;
	
	@Embedded
	private ProcedimentoId procedimento;
	
	@Column(name="cd_itconta_medica_inclusao")
	private Integer cdItContaMedicaInclusao;
	
	@Column(name="cd_itconta_medica_efetivo")
	private Integer cdItContaMedicaEfetivo;
	
	public DeParaProcedimento(){
		
	}

	/** Getter para idDeParaProcedimento.
	 * @return o valor de idDeParaProcedimento.
	 */
	public Integer getIdDeParaProcedimento() {
		return idDeParaProcedimento;
	}

	/** Setter para idDeParaProcedimento.
	 * @param idDeParaProcedimento o novo valor de idDeParaProcedimento.
	 */
	public void setIdDeParaProcedimento(Integer idDeParaProcedimento) {
		this.idDeParaProcedimento = idDeParaProcedimento;
	}

	/** Getter para deParaAtendimento.
	 * @return o valor de deParaAtendimento.
	 */
	public DeParaAtendimento getDeParaAtendimento() {
		return deParaAtendimento;
	}

	/** Setter para deParaAtendimento.
	 * @param deParaAtendimento o novo valor de deParaAtendimento.
	 */
	public void setDeParaAtendimento(DeParaAtendimento deParaAtendimento) {
		this.deParaAtendimento = deParaAtendimento;
	}

	/** Getter para procedimento.
	 * @return o valor de procedimento.
	 */
	public ProcedimentoId getProcedimento() {
		return procedimento;
	}

	/** Setter para procedimento.
	 * @param procedimento o novo valor de procedimento.
	 */
	public void setProcedimento(ProcedimentoId procedimento) {
		this.procedimento = procedimento;
	}

	/** Getter para cdItContaMedicaInclusao.
	 * @return o valor de cdItContaMedicaInclusao.
	 */
	public Integer getCdItContaMedicaInclusao() {
		return cdItContaMedicaInclusao;
	}

	/** Setter para cdItContaMedicaInclusao.
	 * @param cdItContaMedicaInclusao o novo valor de cdItContaMedicaInclusao.
	 */
	public void setCdItContaMedicaInclusao(Integer cdItContaMedicaInclusao) {
		this.cdItContaMedicaInclusao = cdItContaMedicaInclusao;
	}

	/** Getter para cdItContaMedicaEfetivo.
	 * @return o valor de cdItContaMedicaEfetivo.
	 */
	public Integer getCdItContaMedicaEfetivo() {
		return cdItContaMedicaEfetivo;
	}

	/** Setter para cdItContaMedicaEfetivo.
	 * @param cdItContaMedicaEfetivo o novo valor de cdItContaMedicaEfetivo.
	 */
	public void setCdItContaMedicaEfetivo(Integer cdItContaMedicaEfetivo) {
		this.cdItContaMedicaEfetivo = cdItContaMedicaEfetivo;
	}
	
}
