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

@XmlRootElement(name = "MvCacDeParaPrestador")
@Table(name = "MV_CAC_DE_PARA_PRESTADOR")
@Entity
public class MvCacDeParaPrestador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cac_de_para_prestador")
	private int id;

	@Column
	private int id_prestador;
	
	@Column
	private int cod_prestador;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	
	public MvCacDeParaPrestador() {

	}

	public MvCacDeParaPrestador(int id, int id_prestador, int cod_prestador, Date timestamp) {
		super();
		this.id = id;
		this.id_prestador = id_prestador;
		this.cod_prestador = cod_prestador;
		this.timestamp = timestamp;

	}

	@Override
	public String toString() {
		return "MV CAC DExPARA PRESTADOR [id=" + id + ", id_prestadpr=" + id_prestador + ", cod_prestador=" + cod_prestador + ", timestamp=" + timestamp+ "]";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getId_prestador() {
		return id_prestador;
	}

	public void setId_prestador(int id_prestador) {
		this.id_prestador = id_prestador;
	}

	public int getCod_prestador() {
		return cod_prestador;
	}

	public void setCod_prestador(int cod_prestador) {
		this.cod_prestador = cod_prestador;
	}
	
	


}
