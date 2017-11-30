package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "LOG_CAC_MV_WEBSERVICES")
@Table(name = "LOG_CAC_MV_WEBSERVICES")
@Entity
public class LogWebservice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_log_cac_mv_webservices")
	private int id;

	@Column(length = 15)
	private String acao;

	@Column(length = 30)
	private String webservice;

	public LogWebservice() {

	}

	public LogWebservice(int id, String acao, String webservice) {
		this.id = id;
		this.acao = acao;
		this.webservice = webservice;
	}

	@Override
	public String toString() {
		return "LogWebservice [id=" + id + ", acao=" + acao + ", webservice=" + webservice + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getWebservice() {
		return webservice;
	}

	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}

}
