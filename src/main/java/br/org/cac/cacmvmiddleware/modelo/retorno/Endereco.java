package br.org.cac.cacmvmiddleware.modelo.retorno;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWPRESTADORESENDERECOS")
public class Endereco {
	
	private String id;
	private String prestador;
	private String cdTipoEndereco;
	private String nrCep;
	private String dsEndereco;
	private String nrEndereco;
	private String dsComplemento;
	private String cdMunicipio;
	private String dsMunicipio;
	private String cdUf;
	private String cdPrestadorTem;
	private String snPrincipal;
	private String nrTelefone;
	private String cdCnes;
	private String tpLogradouro;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrestador() {
		return prestador;
	}
	public void setPrestador(String prestador) {
		this.prestador = prestador;
	}
	public String getCdTipoEndereco() {
		return cdTipoEndereco;
	}
	public void setCdTipoEndereco(String cdTipoEndereco) {
		this.cdTipoEndereco = cdTipoEndereco;
	}
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}
	public String getDsEndereco() {
		return dsEndereco;
	}
	public void setDsEndereco(String dsEndereco) {
		this.dsEndereco = dsEndereco;
	}
	public String getNrEndereco() {
		return nrEndereco;
	}
	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}
	public String getDsComplemento() {
		return dsComplemento;
	}
	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}
	public String getCdMunicipio() {
		return cdMunicipio;
	}
	public void setCdMunicipio(String cdMunicipio) {
		this.cdMunicipio = cdMunicipio;
	}
	public String getDsMunicipio() {
		return dsMunicipio;
	}
	public void setDsMunicipio(String dsMunicipio) {
		this.dsMunicipio = dsMunicipio;
	}
	public String getCdUf() {
		return cdUf;
	}
	public void setCdUf(String cdUf) {
		this.cdUf = cdUf;
	}
	public String getCdPrestadorTem() {
		return cdPrestadorTem;
	}
	public void setCdPrestadorTem(String cdPrestadorTem) {
		this.cdPrestadorTem = cdPrestadorTem;
	}
	public String getSnPrincipal() {
		return snPrincipal;
	}
	public void setSnPrincipal(String snPrincipal) {
		this.snPrincipal = snPrincipal;
	}
	public String getNrTelefone() {
		return nrTelefone;
	}
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}
	public String getCdCnes() {
		return cdCnes;
	}
	public void setCdCnes(String cdCnes) {
		this.cdCnes = cdCnes;
	}
	public String getTpLogradouro() {
		return tpLogradouro;
	}
	public void setTpLogradouro(String tpLogradouro) {
		this.tpLogradouro = tpLogradouro;
	}
	public Endereco() {
		super();
	}
	
	
	

}
