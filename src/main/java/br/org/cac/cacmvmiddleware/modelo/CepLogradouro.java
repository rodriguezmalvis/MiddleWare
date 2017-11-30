package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CEPLOGRADOURO")
@Table(name = "cep_logradouros")
@Entity
public class CepLogradouro {

	@Id
	private Integer cd_logradouro;
	private Integer cd_localidade;
	private String ds_logradouro;
	private Integer nr_cep;
	private String ds_complemento;
	private Integer cd_bairro_inicial;
	private Integer cd_bairro_final;
	private String cd_uf;
	private String tp_logradouro;
	private String ds_adicional;
	
	
	public Integer getCd_logradouro() {
		return cd_logradouro;
	}
	public void setCd_logradouro(Integer cd_logradouro) {
		this.cd_logradouro = cd_logradouro;
	}
	public Integer getCd_localidade() {
		return cd_localidade;
	}
	public void setCd_localidade(Integer cd_localidade) {
		this.cd_localidade = cd_localidade;
	}
	public String getDs_logradouro() {
		return ds_logradouro;
	}
	public void setDs_logradouro(String ds_logradouro) {
		this.ds_logradouro = ds_logradouro;
	}
	public Integer getNr_cep() {
		return nr_cep;
	}
	public void setNr_cep(Integer nr_cep) {
		this.nr_cep = nr_cep;
	}
	public String getDs_complemento() {
		return ds_complemento;
	}
	public void setDs_complemento(String ds_complemento) {
		this.ds_complemento = ds_complemento;
	}
	public Integer getCd_bairro_inicial() {
		return cd_bairro_inicial;
	}
	public void setCd_bairro_inicial(Integer cd_bairro_inicial) {
		this.cd_bairro_inicial = cd_bairro_inicial;
	}
	public Integer getCd_bairro_final() {
		return cd_bairro_final;
	}
	public void setCd_bairro_final(Integer cd_bairro_final) {
		this.cd_bairro_final = cd_bairro_final;
	}
	public String getCd_uf() {
		return cd_uf;
	}
	public void setCd_uf(String cd_uf) {
		this.cd_uf = cd_uf;
	}
	public String getTp_logradouro() {
		return tp_logradouro;
	}
	public void setTp_logradouro(String tp_logradouro) {
		this.tp_logradouro = tp_logradouro;
	}
	public String getDs_adicional() {
		return ds_adicional;
	}
	public void setDs_adicional(String ds_adicional) {
		this.ds_adicional = ds_adicional;
	}
	
	
	
	
	
		
}
