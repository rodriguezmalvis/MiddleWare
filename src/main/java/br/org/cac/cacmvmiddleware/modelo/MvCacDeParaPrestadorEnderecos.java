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


@XmlRootElement(name = "MvCacDeParaPrestadorEnderecos")
@Table(name = "MV_CAC_DE_PARA_PRESTADOR_ENDERECOS")
@Entity
public class MvCacDeParaPrestadorEnderecos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cac_de_para_prest_end")
	private int id;

	@Column
	private int id_prestador;
	
	@Column
	private String id_endereco_prestador;

	@Column
	private String cod_prestador_endereco;
	
	
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


	public String getId_endereco_prestador() {
		return id_endereco_prestador;
	}


	public void setId_endereco_prestador(String id_endereco_prestador) {
		this.id_endereco_prestador = id_endereco_prestador;
	}


	public String getCod_prestador_endereco() {
		return cod_prestador_endereco;
	}


	public void setCod_prestador_endereco(String cod_prestador_endereco) {
		this.cod_prestador_endereco = cod_prestador_endereco;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public MvCacDeParaPrestadorEnderecos() {
		super();
	}

	@Override
	public String toString() {
		return "MV CAC DExPARA PRESTADOR ENDERECOS [id=" + id + ", id_prestadpr=" + id_prestador + ", id_prestador_endereco ="+id_endereco_prestador+", cod_prestador_endereco=" + cod_prestador_endereco + ", timestamp=" + timestamp+ "]";
	}
}
