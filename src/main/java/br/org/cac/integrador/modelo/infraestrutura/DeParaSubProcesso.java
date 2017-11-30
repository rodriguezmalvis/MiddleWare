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

import br.org.cac.integrador.modelo.SubProcessoId;
import br.org.cac.integrador.modelo.TipoGuia;

@Entity
@Table(name="MV_CAC_DE_PARA_SUB_PROCESSO")
public class DeParaSubProcesso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_sub_processo")
	private Integer idDeParaSubProcesso;
	
	@ManyToOne
	@JoinColumn(name="id_processamento")
	private Processamento processamento;
	
	@Embedded
	private SubProcessoId subProcesso;
	
	@Column(name="cd_lote_inclusao")
	private Integer cdLoteInclusao;
	
	@Column(name="cd_lote_efetivo")
	private Integer cdLoteEfetivo;
	
	@Column(name="tipo_guia_mv")
	private TipoGuia tipoGuia;
	
	@OneToMany(mappedBy="deParaSubProcesso")
	private List<DeParaAtendimento> deParaAtendimentos;
	
	public DeParaSubProcesso(){
		
	}

	/** Getter para idDeParaSubProcesso.
	 * @return o valor de idDeParaSubProcesso.
	 */
	public Integer getIdDeParaSubProcesso() {
		return idDeParaSubProcesso;
	}

	/** Setter para idDeParaSubProcesso.
	 * @param idDeParaSubProcesso o novo valor de idDeParaSubProcesso.
	 */
	public void setIdDeParaSubProcesso(Integer idDeParaSubProcesso) {
		this.idDeParaSubProcesso = idDeParaSubProcesso;
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

	/** Getter para cdLoteInclusao.
	 * @return o valor de cdLoteInclusao.
	 */
	public Integer getCdLoteInclusao() {
		return cdLoteInclusao;
	}

	/** Setter para cdLoteInclusao.
	 * @param cdLoteInclusao o novo valor de cdLoteInclusao.
	 */
	public void setCdLoteInclusao(Integer cdLoteInclusao) {
		this.cdLoteInclusao = cdLoteInclusao;
	}

	/** Getter para cdLoteEfetivo.
	 * @return o valor de cdLoteEfetivo.
	 */
	public Integer getCdLoteEfetivo() {
		return cdLoteEfetivo;
	}

	/** Setter para cdLoteEfetivo.
	 * @param cdLoteEfetivo o novo valor de cdLoteEfetivo.
	 */
	public void setCdLoteEfetivo(Integer cdLoteEfetivo) {
		this.cdLoteEfetivo = cdLoteEfetivo;
	}

	/** Getter para tipoGuia.
	 * @return o valor de tipoGuia.
	 */
	public TipoGuia getTipoGuia() {
		return tipoGuia;
	}

	/** Setter para tipoGuia.
	 * @param tipoGuia o novo valor de tipoGuia.
	 */
	public void setTipoGuia(TipoGuia tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	/** Getter para deParaAtendimentos.
	 * @return o valor de deParaAtendimentos.
	 */
	public List<DeParaAtendimento> getDeParaAtendimentos() {
		return deParaAtendimentos;
	}

	/** Setter para deParaAtendimentos.
	 * @param deParaAtendimentos o novo valor de deParaAtendimentos.
	 */
	public void setDeParaAtendimentos(List<DeParaAtendimento> deParaAtendimentos) {
		this.deParaAtendimentos = deParaAtendimentos;
	}
	
}
