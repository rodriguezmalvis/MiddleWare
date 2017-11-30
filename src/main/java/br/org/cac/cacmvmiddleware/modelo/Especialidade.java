package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWPRESTADORESESPECIALIDADES")
@Table(name = "VW_PRESTADORES_ESPECIALIDADES")
@Entity
public class Especialidade {
	
	@Id
	private int id_prestador_especialidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_prestador")
	private Prestador prestador;
	
	private String id_especialidade;
	
	private String principal;


	public int getId_prestador_especialidade() {
		return id_prestador_especialidade;
	}

	public void setId_prestador_especialidade(int id_prestador_especialidade) {
		this.id_prestador_especialidade = id_prestador_especialidade;
	}
	

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}

	public String getId_especialidade() {
		return id_especialidade;
	}

	public void setId_especialidade(String id_especialidade) {
		this.id_especialidade = id_especialidade;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Especialidade() {
		super();
	}


}
