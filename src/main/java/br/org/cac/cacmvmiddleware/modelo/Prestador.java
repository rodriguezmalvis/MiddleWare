package br.org.cac.cacmvmiddleware.modelo;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWCARGAINICIALMVPRESTADORES")
@Table(name = "VW_CARGA_INICIAL_MV_PRESTADORES")
@Entity
public class Prestador {
	
	@Id
	@Column(name="id_prestador")
	private int id_prestador;

	private String nome;
	
	private String situacao;
	
	private String d_prestador;
	
	private String nome_fantasia;
	
	private String tipo_prestador;
	
	private String cpf_cgc;
	
	private String cnes;
	
	private String d_tipo_pessoa;
	
	private String d_tipo_prestador;
	
	private String id_banco;
	
	private String id_agencia;
	
	private String numero;
	
	private String registro_conselho;
	
	private String dt_nascimento;
	
	private String dt_cadastramento;
	
	private String ordem_banco;
	
	private String ordem_agencia;
	
	private String ordem_numero;
	
	private String dtStatus;
	
	private String cdMotivoDesligamento;
	
	private String cdPrestador;
	
	private String inss;
	
	public String getCdPrestador() {
		return cdPrestador;
	}

	public void setCdPrestador(String cdPrestador) {
		this.cdPrestador = cdPrestador;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="prestador") 
	private Set<Endereco> endereco;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="prestador")
	private List<Especialidade> especialidade;

	public int getId_prestador() {
		return id_prestador;
	}

	public void setId_prestador(int id_prestador) {
		this.id_prestador = id_prestador;
	}

	public String getD_tipo_prestador() {
		return d_tipo_prestador;
	}

	public void setD_tipo_prestador(String d_tipo_prestador) {
		this.d_tipo_prestador = d_tipo_prestador;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public String getTipo_prestador() {
		return tipo_prestador;
	}

	public void setTipo_prestador(String tipo_prestador) {
		this.tipo_prestador = tipo_prestador;
	}

	public String getCpf_cgc() {
		return cpf_cgc;
	}

	public void setCpf_cgc(String cpf_cgc) {
		this.cpf_cgc = cpf_cgc;
	}

	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	public String getD_tipo_pessoa() {
		return d_tipo_pessoa;
	}

	public void setD_tipo_pessoa(String d_tipo_pessoa) {
		this.d_tipo_pessoa = d_tipo_pessoa;
	}

	public String getId_banco() {
		return id_banco;
	}

	public void setId_banco(String id_banco) {
		this.id_banco = id_banco;
	}

	public String getId_agencia() {
		return id_agencia;
	}

	public void setId_agencia(String id_agencia) {
		this.id_agencia = id_agencia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDt_cadastramento() {
		return dt_cadastramento;
	}

	public void setDt_cadastramento(String dt_cadastramento) {
		this.dt_cadastramento = dt_cadastramento;
	}

	public Set<Endereco> getEndereco() {
		return endereco;
	}

	public void setEndereco(Set<Endereco> endereco) {
		this.endereco = endereco;
	}

	public List<Especialidade> getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(List<Especialidade> especialidade) {
		this.especialidade = especialidade;
	}

	public String getOrdem_banco() {
		return ordem_banco;
	}

	public void setOrdem_banco(String ordem_banco) {
		this.ordem_banco = ordem_banco;
	}

	public String getOrdem_agencia() {
		return ordem_agencia;
	}

	public void setOrdem_agencia(String ordem_agencia) {
		this.ordem_agencia = ordem_agencia;
	}

	public String getOrdem_numero() {
		return ordem_numero;
	}

	public void setOrdem_numero(String ordem_numero) {
		this.ordem_numero = ordem_numero;
	}

	public String getRegistro_conselho() {
		return registro_conselho;
	}

	public void setRegistro_conselho(String registro_conselho) {
		this.registro_conselho = registro_conselho;
	}

	public String getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(String dtStatus) {
		this.dtStatus = dtStatus;
	}

	public String getCdMotivoDesligamento() {
		return cdMotivoDesligamento;
	}

	public void setCdMotivoDesligamento(String cdMotivoDesligamento) {
		this.cdMotivoDesligamento = cdMotivoDesligamento;
	}

	public String getD_prestador() {
		return d_prestador;
	}

	public void setD_prestador(String d_prestador) {
		this.d_prestador = d_prestador;
	}

	public String getDt_nascimento() {
		return dt_nascimento;
	}

	public void setDt_nascimento(String dt_nascimento) {
		this.dt_nascimento = dt_nascimento;
	}

	public String getInss() {
		return inss;
	}

	public void setInss(String inss) {
		this.inss = inss;
	}

	public Prestador() {
		super();
	}
	
	

}
