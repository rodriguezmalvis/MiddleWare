package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ICM_ERRO_PROCESSAMENTO_REEMBOLSO")
public class ErroProcessamentoReembolso {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_erro_processamento_reembolso")
	private Integer idErroProcessamentoReembolso;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_reembolso_trabalho")
	private ReembolsoTrabalho reembolsoTrabalho;
	
	@Column(name="mensagem", length=800)
	private String mensagem;
	
	public ErroProcessamentoReembolso(){
		
	}

	/** Getter para idErroProcessamentoReembolso.
	 * @return o valor de idErroProcessamentoReembolso.
	 */
	public Integer getIdErroProcessamentoReembolso() {
		return idErroProcessamentoReembolso;
	}

	/** Setter para idErroProcessamentoReembolso.
	 * @param idErroProcessamentoReembolso o novo valor de idErroProcessamentoReembolso.
	 */
	public void setIdErroProcessamentoReembolso(Integer idErroProcessamentoReembolso) {
		this.idErroProcessamentoReembolso = idErroProcessamentoReembolso;
	}

	/** Getter para reembolsoTrabalho.
	 * @return o valor de reembolsoTrabalho.
	 */
	public ReembolsoTrabalho getReembolsoTrabalho() {
		return reembolsoTrabalho;
	}

	/** Setter para reembolsoTrabalho.
	 * @param reembolsoTrabalho o novo valor de reembolsoTrabalho.
	 */
	public void setReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho) {
		this.reembolsoTrabalho = reembolsoTrabalho;
	}

	/** Getter para mensagem.
	 * @return o valor de mensagem.
	 */
	public String getMensagem() {
		return mensagem;
	}

	/** Setter para mensagem.
	 * @param mensagem o novo valor de mensagem.
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
