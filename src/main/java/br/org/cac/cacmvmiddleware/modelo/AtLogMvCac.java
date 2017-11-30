package br.org.cac.cacmvmiddleware.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ATLOGMVCAC")
@Table(name = "AT_LOG_MV_CAC")
@Entity
public class AtLogMvCac {
	
	@Id
	private Integer id_log_mv_cac;
	private Integer id_pessoa;
	private String d_tipo_beneficiario;
	private String d_tipo_operacao;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	private String resposta;
	public Integer getId_log_mv_cac() {
		return id_log_mv_cac;
	}
	public void setId_log_mv_cac(Integer id_log_mv_cac) {
		this.id_log_mv_cac = id_log_mv_cac;
	}
	public Integer getId_pessoa() {
		return id_pessoa;
	}
	public void setId_pessoa(Integer id_pessoa) {
		this.id_pessoa = id_pessoa;
	}
	public String getD_tipo_beneficiario() {
		return d_tipo_beneficiario;
	}
	public void setD_tipo_beneficiario(String d_tipo_beneficiario) {
		this.d_tipo_beneficiario = d_tipo_beneficiario;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getResposta() {
		return resposta;
	}
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	public String getD_tipo_operacao() {
		return d_tipo_operacao;
	}
	public void setD_tipo_operacao(String d_tipo_operacao) {
		this.d_tipo_operacao = d_tipo_operacao;
	}
	
	

}
