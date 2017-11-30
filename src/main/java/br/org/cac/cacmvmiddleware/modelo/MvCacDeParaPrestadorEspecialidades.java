package br.org.cac.cacmvmiddleware.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "MvCacDeParaPrestadorEspecialidades")
@Table(name = "MV_CAC_DE_PARA_PRESTADOR_ESPECIALIDADES")
@Entity
public class MvCacDeParaPrestadorEspecialidades {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cac_de_para_prest_esp")
	private int id;

	@Column
	private int id_prestador;
	
	@Column
	private String id_especialidade;	
	
	@Column
	private String cd_especialidade;
	
	@Column
	private int cod_prestador;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getId_prestador() {
		return id_prestador;
	}


	public void setId_prestador(int id_prestador) {
		this.id_prestador = id_prestador;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public String getId_especialidade() {
		return cd_especialidade;
	}


	public void setId_especialidade(String id_especialidade) {
		this.cd_especialidade = id_especialidade;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public int getCod_prestador() {
		return cod_prestador;
	}


	public void setCod_prestador(int cod_prestador) {
		this.cod_prestador = cod_prestador;
	}


	public String getCd_especialidade() {
		return cd_especialidade;
	}


	public void setCd_especialidade(String cd_especialidade) {
		this.cd_especialidade = cd_especialidade;
	}

	public MvCacDeParaPrestadorEspecialidades() {
		super();
	}

	@Override
	public String toString() {
		return "MV CAC DExPARA PRESTADOR ESPECIALIDADES [id=" + id + ", id_prestador=" + id_prestador + ", id_especialidade=" + id_especialidade + ", cd_especialidade ="+cd_especialidade+ ", cod_prestador = "+cod_prestador+", timestamp=" + timestamp+ "]";
	}
}
