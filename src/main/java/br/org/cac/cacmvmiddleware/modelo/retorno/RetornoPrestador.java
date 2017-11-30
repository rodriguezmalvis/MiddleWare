package br.org.cac.cacmvmiddleware.modelo.retorno;

import java.util.List;

public class RetornoPrestador {
	
	private String status;
	
	private Prestador prestador;
	
	private List<Especialidade> prestadorEspecialidade;
    private List<Endereco> prestadorEndereco;
	
	private String mensagem;
	
	private String entidadeId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(String entidadeId) {
		this.entidadeId = entidadeId;
	}

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public List<Especialidade> getPrestadorEspecialidade() {
		return prestadorEspecialidade;
	}
	public void setPrestadorEspecialidade(List<Especialidade> prestadorEspecialidade) {
		this.prestadorEspecialidade = prestadorEspecialidade;
	}
	public List<Endereco> getPrestadorEndereco() {
		return prestadorEndereco;
	}
	public void setPrestadorEndereco(List<Endereco> prestadorEndereco) {
		this.prestadorEndereco = prestadorEndereco;
	}	

	public RetornoPrestador() {
		super();
	}
	
	

}
