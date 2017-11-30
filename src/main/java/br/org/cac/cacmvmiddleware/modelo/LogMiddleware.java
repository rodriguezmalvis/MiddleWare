package br.org.cac.cacmvmiddleware.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "LOGMIDDLEWARE")
@Table(name = "LOGMIDDLEWARE")
@Entity
public class LogMiddleware {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int id;

	@Column(columnDefinition = "TEXT")
	private String mensagem = "";

	@Column(columnDefinition = "TEXT")
	private String resposta = "";

	@Column
	private int statusCode;

	@Column(columnDefinition = "TEXT")
	private String statusCodeDescricao = "";

	@Column(length = 500)
	private String path;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_log_cac_mv_webservices", nullable = false)
	private LogWebservice logWebservice;

	public LogMiddleware() {

	}

	public LogMiddleware(int id, String mensagem, String resposta, int statusCode, String statusCodeDescricao,
			String path, Date date, LogWebservice logWebservice) {
		super();
		this.id = id;
		this.mensagem = mensagem;
		this.resposta = resposta;
		this.statusCode = statusCode;
		this.statusCodeDescricao = statusCodeDescricao;
		this.path = path;
		this.date = date;
		this.logWebservice = logWebservice;
	}

	@Override
	public String toString() {
		return "LogMiddleware [id=" + id + ", mensagem=" + mensagem + ", resposta=" + resposta + ", statusCode="
				+ statusCode + ", statusCodeDescricao=" + statusCodeDescricao + ", path=" + path + ", date=" + date
				+ ", logWebservice=" + logWebservice + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCodeDescricao() {
		return statusCodeDescricao;
	}

	public void setStatusCodeDescricao(String statusCodeDescricao) {
		this.statusCodeDescricao = statusCodeDescricao;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LogWebservice getLogWebservice() {
		return logWebservice;
	}

	public void setLogWebservice(LogWebservice logWebservice) {
		this.logWebservice = logWebservice;
	}

}
