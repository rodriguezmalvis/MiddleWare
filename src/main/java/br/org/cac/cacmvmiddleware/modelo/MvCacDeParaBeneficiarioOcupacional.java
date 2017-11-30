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

@XmlRootElement(name = "MvCacDeParaBeneficiarioOcupacional")
@Table(name = "MV_CAC_DE_PARA_BENEFICIARIO_OCUPACIONAL")
@Entity
public class MvCacDeParaBeneficiarioOcupacional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_cac_de_para_beneficiario_ocupacional")
	private int id;

	@Column
	private int id_pessoa;
	
	@Column
	private int cod_pessoa;
	
	@Column
	private String cd_contrato;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	
	public MvCacDeParaBeneficiarioOcupacional() {

	}

	public MvCacDeParaBeneficiarioOcupacional(int id, int id_pessoa, int cod_pessoa, Date timestamp) {
		super();
		this.id = id;
		this.id_pessoa = id_pessoa;
		this.cod_pessoa = cod_pessoa;
		this.timestamp = timestamp;

	}

	@Override
	public String toString() {
		return "MV CAC DExPARA BENEFICIARIO [id=" + id + ", id_pessoa=" + id_pessoa + ", cod_pessoa=" + cod_pessoa + ", timestamp=" + timestamp+ "]";
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

	public int getId_pessoa() {
		return id_pessoa;
	}

	public void setId_pessoa(int id_pessoa) {
		this.id_pessoa = id_pessoa;
	}

	public int getCod_pessoa() {
		return cod_pessoa;
	}

	public void setCod_pessoa(int cod_pessoa) {
		this.cod_pessoa = cod_pessoa;
	}

	public String getCd_contrato() {
		return cd_contrato;
	}

	public void setCd_contrato(String cd_contrato) {
		this.cd_contrato = cd_contrato;
	}
	
	


}
