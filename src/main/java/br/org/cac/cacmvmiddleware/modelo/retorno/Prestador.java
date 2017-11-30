package br.org.cac.cacmvmiddleware.modelo.retorno;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWCARGAINICIALMVPRESTADORES")
public class Prestador {
	
	private String id;
	private String cdMultiEmpresa;
	private String nmPrestador;
	private String tpSituacao;
	private String cdInterno;
	private String nmFantasia;
	private String cdGrupoPrestador;
	private String cdTipPrestador;
	private String ufConselho;
	private String dsCodConselho;
	private String cdConselhoProfissional;
	private String tpPrestador;
	private String nrCpfCgc;
	private String cdCnes;
	private String tpCredenciamento;
	private String snExecutor;
	private String dtNascimento;
	private String dsAgencia;
	private String nrConta;
	private String cdBanco;
	private String cdAgencia;
	private String dvAgencia;
	private String dvContaCorrente;
	private String cdFornecedor;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCdMultiEmpresa() {
		return cdMultiEmpresa;
	}
	public void setCdMultiEmpresa(String cdMultiEmpresa) {
		this.cdMultiEmpresa = cdMultiEmpresa;
	}
	public String getNmPrestador() {
		return nmPrestador;
	}
	public void setNmPrestador(String nmPrestador) {
		this.nmPrestador = nmPrestador;
	}
	public String getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public String getCdInterno() {
		return cdInterno;
	}
	public void setCdInterno(String cdInterno) {
		this.cdInterno = cdInterno;
	}
	public String getNmFantasia() {
		return nmFantasia;
	}
	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}
	public String getCdGrupoPrestador() {
		return cdGrupoPrestador;
	}
	public void setCdGrupoPrestador(String cdGrupoPrestador) {
		this.cdGrupoPrestador = cdGrupoPrestador;
	}
	public String getCdTipPrestador() {
		return cdTipPrestador;
	}
	public void setCdTipPrestador(String cdTipPrestador) {
		this.cdTipPrestador = cdTipPrestador;
	}
	public String getUfConselho() {
		return ufConselho;
	}
	public void setUfConselho(String ufConselho) {
		this.ufConselho = ufConselho;
	}
	public String getDsCodConselho() {
		return dsCodConselho;
	}
	public void setDsCodConselho(String dsCodConselho) {
		this.dsCodConselho = dsCodConselho;
	}
	public String getCdConselhoProfissional() {
		return cdConselhoProfissional;
	}
	public void setCdConselhoProfissional(String cdConselhoProfissional) {
		this.cdConselhoProfissional = cdConselhoProfissional;
	}
	public String getTpPrestador() {
		return tpPrestador;
	}
	public void setTpPrestador(String tpPrestador) {
		this.tpPrestador = tpPrestador;
	}
	public String getNrCpfCgc() {
		return nrCpfCgc;
	}
	public void setNrCpfCgc(String nrCpfCgc) {
		this.nrCpfCgc = nrCpfCgc;
	}
	public String getCdCnes() {
		return cdCnes;
	}
	public void setCdCnes(String cdCnes) {
		this.cdCnes = cdCnes;
	}
	public String getTpCredenciamento() {
		return tpCredenciamento;
	}
	public void setTpCredenciamento(String tpCredenciamento) {
		this.tpCredenciamento = tpCredenciamento;
	}
	public String getSnExecutor() {
		return snExecutor;
	}
	public void setSnExecutor(String snExecutor) {
		this.snExecutor = snExecutor;
	}
	public String getDtNascimento() {
		return dtNascimento;
	}
	public void setDtNascimento(String dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	public String getDsAgencia() {
		return dsAgencia;
	}
	public void setDsAgencia(String dsAgencia) {
		this.dsAgencia = dsAgencia;
	}
	public String getNrConta() {
		return nrConta;
	}
	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}
	public String getCdBanco() {
		return cdBanco;
	}
	public void setCdBanco(String cdBanco) {
		this.cdBanco = cdBanco;
	}
	public String getCdAgencia() {
		return cdAgencia;
	}
	public void setCdAgencia(String cdAgencia) {
		this.cdAgencia = cdAgencia;
	}
	public String getDvAgencia() {
		return dvAgencia;
	}
	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}
	public String getDvContaCorrente() {
		return dvContaCorrente;
	}
	public void setDvContaCorrente(String dvContaCorrente) {
		this.dvContaCorrente = dvContaCorrente;
	}
	public String getCdFornecedor() {
		return cdFornecedor;
	}
	public void setCdFornecedor(String cdFornecedor) {
		this.cdFornecedor = cdFornecedor;
	}
	


	public Prestador() {
		super();
	}
}
