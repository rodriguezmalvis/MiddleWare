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
@Table(name="ICM_ERRO_PROCESSAMENTO")
public class ErroProcessamento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_erro_processamento")
	private Integer idErroProcessamento;
	
	@ManyToOne
	@JoinColumn(name="id_lote_trabalho")
	private LoteTrabalho loteTrabalho;
	
	@Column(name="mensagem", length=400)
	private String mensagem;
	
	public ErroProcessamento(){
		
	}

	/** Getter para idErroProcessamento.
	 * @return o valor de idErroProcessamento.
	 */
	public Integer getIdErroProcessamento() {
		return idErroProcessamento;
	}

	/** Setter para idErroProcessamento.
	 * @param idErroProcessamento o novo valor de idErroProcessamento.
	 */
	public void setIdErroProcessamento(Integer idErroProcessamento) {
		this.idErroProcessamento = idErroProcessamento;
	}

	/** Getter para loteTrabalho.
	 * @return o valor de loteTrabalho.
	 */
	public LoteTrabalho getLoteTrabalho() {
		return loteTrabalho;
	}

	/** Setter para loteTrabalho.
	 * @param loteTrabalho o novo valor de loteTrabalho.
	 */
	public void setLoteTrabalho(LoteTrabalho loteTrabalho) {
		this.loteTrabalho = loteTrabalho;
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
