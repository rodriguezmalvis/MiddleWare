package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CEPLOCALIDADES")
@Table(name = "cep_localidades")
@Entity
public class CepLocalidade {

	@Id
	private Integer cd_localidade;
	private String cd_uf;
	private String nm_localidade;
	private Integer nr_cep;
	private String tp_situacao;
	private String tp_localidade;
	private String tp_atualizacao;
	private Integer cd_localidade_origem;
	private Integer cd_municipio;
	
	public Integer getCd_localidade() {
		return cd_localidade;
	}
	public void setCd_localidade(Integer cd_localidade) {
		this.cd_localidade = cd_localidade;
	}
	public String getCd_uf() {
		return cd_uf;
	}
	public void setCd_uf(String cd_uf) {
		this.cd_uf = cd_uf;
	}
	public String getNm_localidade() {
		return nm_localidade;
	}
	public void setNm_localidade(String nm_localidade) {
		this.nm_localidade = nm_localidade;
	}
	public Integer getNr_cep() {
		return nr_cep;
	}
	public void setNr_cep(Integer nr_cep) {
		this.nr_cep = nr_cep;
	}
	public String getTp_situacao() {
		return tp_situacao;
	}
	public void setTp_situacao(String tp_situacao) {
		this.tp_situacao = tp_situacao;
	}
	public String getTp_localidade() {
		return tp_localidade;
	}
	public void setTp_localidade(String tp_localidade) {
		this.tp_localidade = tp_localidade;
	}
	public String getTp_atualizacao() {
		return tp_atualizacao;
	}
	public void setTp_atualizacao(String tp_atualizacao) {
		this.tp_atualizacao = tp_atualizacao;
	}
	public Integer getCd_localidade_origem() {
		return cd_localidade_origem;
	}
	public void setCd_localidade_origem(Integer cd_localidade_origem) {
		this.cd_localidade_origem = cd_localidade_origem;
	}
	public Integer getCd_municipio() {
		return cd_municipio;
	}
	public void setCd_municipio(Integer cd_municipio) {
		this.cd_municipio = cd_municipio;
	}
	
	
	
		
}
