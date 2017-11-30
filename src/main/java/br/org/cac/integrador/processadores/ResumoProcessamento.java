/**
 * 
 */
package br.org.cac.integrador.processadores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cac.integrador.modelo.infraestrutura.Processamento;

/** Classe que representa o resumo do que ocorreu durante um processamento.
 * 
 * @author JCJ
 * @since 2017-08-28
 * 
 */
public class ResumoProcessamento {

	@JsonIgnore
	private Processamento processamento;
	
	private int totalItens;
	
	private int itensEnviados;
	
	private int itensComErro;
	
	private List<String> mensagens;
	
	public ResumoProcessamento(Processamento processamento) {
		this.processamento = Objects.requireNonNull(processamento);
		
		this.totalItens = 0;
		this.itensEnviados = 0;
		this.itensComErro = 0;		
	}
	
	public void incTotalItens() {
		this.totalItens++;
	}
	
	public void incItensEnviados() {
		this.itensEnviados++;
	}
	
	public void incItensComErro() {
		this.itensComErro++;
	}
	
	public boolean addMensagem(String mensagem) {
		if (this.mensagens == null) {
			this.mensagens = new ArrayList<>();
		}

		return this.mensagens.add(mensagem);
	}

	/** Getter para idProcessamento.
	 * @return o valor de idProcessamento.
	 */
	@JsonProperty("idProcessamento")
	public Integer getIdProcessamento() {
		return processamento.getIdProcessamento();
	}

	/** Getter para tipoProcessamento.
	 * @return o valor de tipoProcessamento.
	 */
	@JsonProperty("tipoProcessamento")
	public String getTipoProcessamento() {
		return processamento.getTipoProcessamento().getDescricao();
	}

	/** Getter para totalItens.
	 * @return o valor de totalItens.
	 */
	public int getTotalItens() {
		return totalItens;
	}

	/** Setter para totalItens.
	 * @param totalItens o novo valor de totalItens.
	 */
	public void setTotalItens(int totalItens) {
		this.totalItens = totalItens;
	}

	/** Getter para itensEnviados.
	 * @return o valor de itensEnviados.
	 */
	public int getItensEnviados() {
		return itensEnviados;
	}

	/** Setter para itensEnviados.
	 * @param itensEnviados o novo valor de itensEnviados.
	 */
	public void setItensEnviados(int itensEnviados) {
		this.itensEnviados = itensEnviados;
	}

	/** Getter para itensComErro.
	 * @return o valor de itensComErro.
	 */
	public int getItensComErro() {
		return itensComErro;
	}

	/** Setter para itensComErro.
	 * @param itensComErro o novo valor de itensComErro.
	 */
	public void setItensComErro(int itensComErro) {
		this.itensComErro = itensComErro;
	}

	/** Getter para dtInicio.
	 * @return o valor de dtInicio.
	 */
	@JsonProperty("dtInicio")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss", timezone="America/Sao_Paulo")
	public Date getDtInicio() {
		return processamento.getDthrInicioProcessamento();
	}

	/** Getter para dtFim.
	 * @return o valor de dtFim.
	 */
	@JsonProperty("dtFim")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss", timezone="America/Sao_Paulo")
	public Date getDtFim() {
		return Optional.ofNullable(processamento.getDthrFimProcessamento()).orElse(Calendar.getInstance().getTime());
	}

	/** Getter para mensagens.
	 * @return o valor de mensagens.
	 */
	public List<String> getMensagens() {
		return mensagens;
	}

	/** Setter para mensagens.
	 * @param mensagens o novo valor de mensagens.
	 */
	public void setMensagens(List<String> mensagens) {		
		this.mensagens = Objects.requireNonNull(mensagens, "mensagens não pode ser null");
	}	
	
}
