package br.org.cac.cacmvmiddleware.modelo;

import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RespostaMiddleware {

	private Integer entidadeId = null;

	@JsonIgnore
	private Status statusDescricao;

	private Integer status;

	private String mensagem;

	public RespostaMiddleware() {
	}

	public RespostaMiddleware(Integer entidadeId, Status statusDescricao, Integer status, String mensagem) {
		this.entidadeId = entidadeId;
		this.statusDescricao = statusDescricao;
		this.status = status;
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return "RespostaMiddleware [entidadeId=" + entidadeId + ", status=" + status + ", mensagem=" + mensagem + "]";
	}

	public Integer getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(Integer entidadeId) {
		this.entidadeId = entidadeId;
	}

	public Status getStatusDescricao() {
		return statusDescricao;
	}

	public void setStatusDescricao(Status statusDescricao) {
		this.statusDescricao = statusDescricao;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
