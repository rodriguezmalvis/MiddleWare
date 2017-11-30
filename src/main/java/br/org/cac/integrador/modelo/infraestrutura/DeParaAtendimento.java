package br.org.cac.integrador.modelo.infraestrutura;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.org.cac.integrador.modelo.AtendimentoId;

@Entity
@Table(name="MV_CAC_DE_PARA_ATENDIMENTO")
public class DeParaAtendimento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_atendimento")
	private Integer idDeParaAtendimento;
	
	@ManyToOne
	@JoinColumn(name="id_de_para_sub_processo")
	private DeParaSubProcesso deParaSubProcesso;
	
	@Embedded
	private AtendimentoId atendimento;
	
	@Column(name="cd_conta_medica_inclusao")
	private Integer cdContaMedicaInclusao;
	
	@Column(name="cd_conta_medica_efetivo")
	private Integer cdContaMedicaEfetivo;
	
	@OneToMany(mappedBy="deParaAtendimento")
	private List<DeParaProcedimento> deParaProcedimentos;
	
	public DeParaAtendimento(){
		
	}

	/** Getter para idDeParaAtendimento.
	 * @return o valor de idDeParaAtendimento.
	 */
	public Integer getIdDeParaAtendimento() {
		return idDeParaAtendimento;
	}

	/** Setter para idDeParaAtendimento.
	 * @param idDeParaAtendimento o novo valor de idDeParaAtendimento.
	 */
	public void setIdDeParaAtendimento(Integer idDeParaAtendimento) {
		this.idDeParaAtendimento = idDeParaAtendimento;
	}

	/** Getter para deParaSubProcesso.
	 * @return o valor de deParaSubProcesso.
	 */
	public DeParaSubProcesso getDeParaSubProcesso() {
		return deParaSubProcesso;
	}

	/** Setter para deParaSubProcesso.
	 * @param deParaSubProcesso o novo valor de deParaSubProcesso.
	 */
	public void setDeParaSubProcesso(DeParaSubProcesso deParaSubProcesso) {
		this.deParaSubProcesso = deParaSubProcesso;
	}

	/** Getter para atendimento.
	 * @return o valor de atendimento.
	 */
	public AtendimentoId getAtendimento() {
		return atendimento;
	}

	/** Setter para atendimento.
	 * @param atendimento o novo valor de atendimento.
	 */
	public void setAtendimento(AtendimentoId atendimento) {
		this.atendimento = atendimento;
	}

	/** Getter para cdContaMedicaInclusao.
	 * @return o valor de cdContaMedicaInclusao.
	 */
	public Integer getCdContaMedicaInclusao() {
		return cdContaMedicaInclusao;
	}

	/** Setter para cdContaMedicaInclusao.
	 * @param cdContaMedicaInclusao o novo valor de cdContaMedicaInclusao.
	 */
	public void setCdContaMedicaInclusao(Integer cdContaMedicaInclusao) {
		this.cdContaMedicaInclusao = cdContaMedicaInclusao;
	}

	/** Getter para cdContaMedicaEfetivo.
	 * @return o valor de cdContaMedicaEfetivo.
	 */
	public Integer getCdContaMedicaEfetivo() {
		return cdContaMedicaEfetivo;
	}

	/** Setter para cdContaMedicaEfetivo.
	 * @param cdContaMedicaEfetivo o novo valor de cdContaMedicaEfetivo.
	 */
	public void setCdContaMedicaEfetivo(Integer cdContaMedicaEfetivo) {
		this.cdContaMedicaEfetivo = cdContaMedicaEfetivo;
	}

	/** Getter para deParaProcedimentos.
	 * @return o valor de deParaProcedimentos.
	 */
	public List<DeParaProcedimento> getDeParaProcedimentos() {
		return deParaProcedimentos;
	}

	/** Setter para deParaProcedimentos.
	 * @param deParaProcedimentos o novo valor de deParaProcedimentos.
	 */
	public void setDeParaProcedimentos(List<DeParaProcedimento> deParaProcedimentos) {
		this.deParaProcedimentos = deParaProcedimentos;
	}

}
