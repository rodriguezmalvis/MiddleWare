package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ICM_ERRO_PROCESSAMENTO_MENSALIDADE")
public class ErroProcessamentoMensalidade {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_erro_processamento_mensalidade")
	private Integer idErroProcessamentoMensalidade;
	
	@ManyToOne
	@JoinColumn(name="id_mensalidade_trabalho")
	private MensalidadeTrabalho mensalidadeTrabalho;
	
	@Column(name="mensagem",length=800)
	private String mensagem;
	
	public ErroProcessamentoMensalidade(){
		
	}

	/** Getter para idErroProcessamentoMensalidade.
	 * @return o valor de idErroProcessamentoMensalidade.
	 */
	public Integer getIdErroProcessamentoMensalidade() {
		return idErroProcessamentoMensalidade;
	}

	/** Setter para idErroProcessamentoMensalidade.
	 * @param idErroProcessamentoMensalidade o novo valor de idErroProcessamentoMensalidade.
	 */
	public void setIdErroProcessamentoMensalidade(Integer idErroProcessamentoMensalidade) {
		this.idErroProcessamentoMensalidade = idErroProcessamentoMensalidade;
	}

	/** Getter para mensalidadeTrabalho.
	 * @return o valor de mensalidadeTrabalho.
	 */
	public MensalidadeTrabalho getMensalidadeTrabalho() {
		return mensalidadeTrabalho;
	}

	/** Setter para mensalidadeTrabalho.
	 * @param mensalidadeTrabalho o novo valor de mensalidadeTrabalho.
	 */
	public void setMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho) {
		this.mensalidadeTrabalho = mensalidadeTrabalho;
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
