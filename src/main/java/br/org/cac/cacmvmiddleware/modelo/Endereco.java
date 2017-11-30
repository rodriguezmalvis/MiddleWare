package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWPRESTADORESENDERECOS")
@Table(name = "VW_PRESTADORES_ENDERECOS")
@Entity
public class Endereco {
	
	@Id
	private int id_prestador_endereco;
	
	private String id_endereco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_prestador")
	private Prestador prestador;
	
	private String cnes;
	
	private String tipo_endereco;
	
	private String id_cidade;
	
	@Column(name="id_sigla_estado")
	private String id_sigla_uf;
	
	private String cidade;
	
	private String cep;
	
	@Column(name="logradouro")
	private String endereco;
	
	private String porta;

	private String bairro;
	
	private String principal;
	
	private String cod_prestador_endereco;
	
	public int getId_prestador_endereco() {
		return id_prestador_endereco;
	}

	public void setId_prestador_endereco(int id_prestador_endereco) {
		this.id_prestador_endereco = id_prestador_endereco;
	}

	public String getTipo_endereco() {
		return tipo_endereco;
	}

	public void setTipo_endereco(String tipo_endereco) {
		this.tipo_endereco = tipo_endereco;
	}

	public String getId_cidade() {
		return id_cidade;
	}

	public void setId_cidade(String id_cidade) {
		this.id_cidade = id_cidade;
	}

	public String getId_sigla_uf() {
		return id_sigla_uf;
	}

	public void setId_sigla_uf(String id_sigla_uf) {
		this.id_sigla_uf = id_sigla_uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}
	
	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	public String getCod_prestador_endereco() {
		return cod_prestador_endereco;
	}

	public void setCod_prestador_endereco(String cod_prestador_endereco) {
		this.cod_prestador_endereco = cod_prestador_endereco;
	}

	public String getId_endereco() {
		return id_endereco;
	}

	public void setId_endereco(String id_endereco) {
		this.id_endereco = id_endereco;
	}

	public Endereco() {
		super();
	}
	
	
		
	

}
