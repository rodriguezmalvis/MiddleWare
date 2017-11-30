package br.org.cac.integrador.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa o modelo de reembolso esperado pela mv (correspondente à
 * entidade ImpReembolso).
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1., 2017-04-01
 *
 */
@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaReembolsos", 
							   procedureName = "SPR_ICM_LISTA_REEMBOLSOS", 
							   resultClasses = {Reembolso.class }, 
							   parameters = {
								@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "id_atendimento", type = Integer.class, mode = ParameterMode.IN)
							}) 
})
public class Reembolso {
	//Campos implementados de acordo com SPR_ICM_LISTA_REEMBOLSOS
	
	@JsonIgnore
	@EmbeddedId
	private AtendimentoId atendimentoId;
	
	private Integer cdAgencia;
	
	private Integer cdBanco;
	
	private Integer cdConPag;
	
	private Integer cdControleInterno;
	
	private Integer cdEspecialidade;
	
	private Integer cdExpContabilidade;
	
	private String cdFornecedor;
	
	@JsonIgnore
	private Integer cdMatriculaTitular;
	
	private Integer cdMatricula;
	
	private Integer cdMultiEmpresa;
	
	private String cdMunicipio;
	
	private Integer cdPrestador;
	
	private Integer cdReembolso;
	
	private Integer cdReembolsoPai;
	
	@JsonIgnore
	private TipoGuia tpGuia;
	
	private Integer cdTipoEndereco;
	
	private String cdUf;
	
	private String cdUsuarioInclusao;
	
	private String dsBairro;
	
	private String dsCodigoConselho;
	
	private String dsComplemento;
	
	private String dsConselho;
	
	private String dsConsideracoesInternas;
	
	private String dsEmail;
	
	private String dsEndereco;
	
	private String dsMunicipio;
	
	private String dsObservacao;
	
	private String dsPrestador;
	
	private String dsReciboNotaFiscal;
	
	private Date dtAtendimento;
	
	private Date dtFechamento;
	
	private Date dtInclusao;
	
	private Date dtVencimento;
	
	private String dvAgencia;
	
	private String dvContaCorrente;
	
	private String nmBanco;
	
	private String nrCep;
	
	private String nrConta;
	
	private String nrCpfCnpj;
	
	private String nrEndereco;
	
	private Integer nrProtocoloAns;
	
	private String nrTelefone;
	
	private SimNao snRecusado;
	
	private String tpLogradouro;
	
	private String tpPessoa;
	
	private String tpReembolso;
	
	private String tpStatus;
	
	private String tpStatusReembolso;
	
	private Double vlTotalOriginal;
	
	@Transient
	@JsonProperty("itreembolsos")	
	private List<ItemReembolso> itensReembolso;
	
	public Reembolso(){
		
	}

	/** Getter para atendimentoId.
	 * @return o valor de atendimentoId.
	 */
	public AtendimentoId getAtendimentoId() {
		return atendimentoId;
	}

	/** Setter para atendimentoId.
	 * @param atendimentoId o novo valor de atendimentoId.
	 */
	public void setAtendimentoId(AtendimentoId atendimentoId) {
		this.atendimentoId = atendimentoId;
	}

	/** Getter para cdAgencia.
	 * @return o valor de cdAgencia.
	 */
	public Integer getCdAgencia() {
		return cdAgencia;
	}

	/** Setter para cdAgencia.
	 * @param cdAgencia o novo valor de cdAgencia.
	 */
	public void setCdAgencia(Integer cdAgencia) {
		this.cdAgencia = cdAgencia;
	}

	/** Getter para cdBanco.
	 * @return o valor de cdBanco.
	 */
	public Integer getCdBanco() {
		return cdBanco;
	}

	/** Setter para cdBanco.
	 * @param cdBanco o novo valor de cdBanco.
	 */
	public void setCdBanco(Integer cdBanco) {
		this.cdBanco = cdBanco;
	}

	/** Getter para cdConPag.
	 * @return o valor de cdConPag.
	 */
	public Integer getCdConPag() {
		return cdConPag;
	}

	/** Setter para cdConPag.
	 * @param cdConPag o novo valor de cdConPag.
	 */
	public void setCdConPag(Integer cdConPag) {
		this.cdConPag = cdConPag;
	}

	/** Getter para cdControleInterno.
	 * @return o valor de cdControleInterno.
	 */
	public Integer getCdControleInterno() {
		return cdControleInterno;
	}

	/** Setter para cdControleInterno.
	 * @param cdControleInterno o novo valor de cdControleInterno.
	 */
	public void setCdControleInterno(Integer cdControleInterno) {
		this.cdControleInterno = cdControleInterno;
	}

	/** Getter para cdEspecialidade.
	 * @return o valor de cdEspecialidade.
	 */
	public Integer getCdEspecialidade() {
		return cdEspecialidade;
	}

	/** Setter para cdEspecialidade.
	 * @param cdEspecialidade o novo valor de cdEspecialidade.
	 */
	public void setCdEspecialidade(Integer cdEspecialidade) {
		this.cdEspecialidade = cdEspecialidade;
	}

	/** Getter para cdExpContabilidade.
	 * @return o valor de cdExpContabilidade.
	 */
	public Integer getCdExpContabilidade() {
		return cdExpContabilidade;
	}

	/** Setter para cdExpContabilidade.
	 * @param cdExpContabilidade o novo valor de cdExpContabilidade.
	 */
	public void setCdExpContabilidade(Integer cdExpContabilidade) {
		this.cdExpContabilidade = cdExpContabilidade;
	}

	/** Getter para cdFornecedor.
	 * @return o valor de cdFornecedor.
	 */
	public String getCdFornecedor() {
		return cdFornecedor;
	}

	/** Setter para cdFornecedor.
	 * @param cdFornecedor o novo valor de cdFornecedor.
	 */
	public void setCdFornecedor(String cdFornecedor) {
		this.cdFornecedor = cdFornecedor;
	}

	/** Getter para cdMatriculaTitular.
	 * @return o valor de cdMatriculaTitular.
	 */
	public Integer getCdMatriculaTitular() {
		return cdMatriculaTitular;
	}

	/** Setter para cdMatriculaTitular.
	 * @param cdMatriculaTitular o novo valor de cdMatriculaTitular.
	 */
	public void setCdMatriculaTitular(Integer cdMatriculaTitular) {
		this.cdMatriculaTitular = cdMatriculaTitular;
	}

	/** Getter para cdMatricula.
	 * @return o valor de cdMatricula.
	 */
	public Integer getCdMatricula() {
		return cdMatricula;
	}

	/** Setter para cdMatricula.
	 * @param cdMatricula o novo valor de cdMatricula.
	 */
	public void setCdMatricula(Integer cdMatricula) {
		this.cdMatricula = cdMatricula;
	}

	/** Getter para cdMultiEmpresa.
	 * @return o valor de cdMultiEmpresa.
	 */
	public Integer getCdMultiEmpresa() {
		return cdMultiEmpresa;
	}

	/** Setter para cdMultiEmpresa.
	 * @param cdMultiEmpresa o novo valor de cdMultiEmpresa.
	 */
	public void setCdMultiEmpresa(Integer cdMultiEmpresa) {
		this.cdMultiEmpresa = cdMultiEmpresa;
	}

	/** Getter para cdMunicipio.
	 * @return o valor de cdMunicipio.
	 */
	public String getCdMunicipio() {
		return cdMunicipio;
	}

	/** Setter para cdMunicipio.
	 * @param cdMunicipio o novo valor de cdMunicipio.
	 */
	public void setCdMunicipio(String cdMunicipio) {
		this.cdMunicipio = cdMunicipio;
	}

	/** Getter para cdPrestador.
	 * @return o valor de cdPrestador.
	 */
	public Integer getCdPrestador() {
		return cdPrestador;
	}

	/** Setter para cdPrestador.
	 * @param cdPrestador o novo valor de cdPrestador.
	 */
	public void setCdPrestador(Integer cdPrestador) {
		this.cdPrestador = cdPrestador;
	}

	/** Getter para cdReembolso.
	 * @return o valor de cdReembolso.
	 */
	public Integer getCdReembolso() {
		return cdReembolso;
	}

	/** Setter para cdReembolso.
	 * @param cdReembolso o novo valor de cdReembolso.
	 */
	public void setCdReembolso(Integer cdReembolso) {
		this.cdReembolso = cdReembolso;
	}

	/** Getter para cdReembolsoPai.
	 * @return o valor de cdReembolsoPai.
	 */
	public Integer getCdReembolsoPai() {
		return cdReembolsoPai;
	}

	/** Setter para cdReembolsoPai.
	 * @param cdReembolsoPai o novo valor de cdReembolsoPai.
	 */
	public void setCdReembolsoPai(Integer cdReembolsoPai) {
		this.cdReembolsoPai = cdReembolsoPai;
	}

	/** Getter para tpGuia.
	 * @return o valor de tpGuia.
	 */
	public TipoGuia getTpGuia() {
		return tpGuia;
	}

	/** Setter para tpGuia.
	 * @param tpGuia o novo valor de tpGuia.
	 */
	public void setTpGuia(TipoGuia tpGuia) {
		this.tpGuia = tpGuia;
	}
	
	/**
	 * Retorna o tipo de atendimento associado ao campo {@link #tpGuia}.
	 * @return O tipo de atendimento associado, se existir.
	 */
	@JsonProperty("cdTipoAtendimento")
	public String getTpAtendimentoMv(){
		if (this.tpGuia == null){
			return null;
		}
		return this.tpGuia.getTpAtendimentoMv();
	}

	/** Getter para cdTipoEndereco.
	 * @return o valor de cdTipoEndereco.
	 */
	public Integer getCdTipoEndereco() {
		return cdTipoEndereco;
	}

	/** Setter para cdTipoEndereco.
	 * @param cdTipoEndereco o novo valor de cdTipoEndereco.
	 */
	public void setCdTipoEndereco(Integer cdTipoEndereco) {
		this.cdTipoEndereco = cdTipoEndereco;
	}

	/** Getter para cdUf.
	 * @return o valor de cdUf.
	 */
	public String getCdUf() {
		return cdUf;
	}

	/** Setter para cdUf.
	 * @param cdUf o novo valor de cdUf.
	 */
	public void setCdUf(String cdUf) {
		this.cdUf = cdUf;
	}

	/** Getter para cdUsuarioInclusao.
	 * @return o valor de cdUsuarioInclusao.
	 */
	public String getCdUsuarioInclusao() {
		return cdUsuarioInclusao;
	}

	/** Setter para cdUsuarioInclusao.
	 * @param cdUsuarioInclusao o novo valor de cdUsuarioInclusao.
	 */
	public void setCdUsuarioInclusao(String cdUsuarioInclusao) {
		this.cdUsuarioInclusao = cdUsuarioInclusao;
	}

	/** Getter para dsBairro.
	 * @return o valor de dsBairro.
	 */
	public String getDsBairro() {
		return dsBairro;
	}

	/** Setter para dsBairro.
	 * @param dsBairro o novo valor de dsBairro.
	 */
	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}

	/** Getter para dsCodigoConselho.
	 * @return o valor de dsCodigoConselho.
	 */
	public String getDsCodigoConselho() {
		return dsCodigoConselho;
	}

	/** Setter para dsCodigoConselho.
	 * @param dsCodigoConselho o novo valor de dsCodigoConselho.
	 */
	public void setDsCodigoConselho(String dsCodigoConselho) {
		this.dsCodigoConselho = dsCodigoConselho;
	}

	/** Getter para dsComplemento.
	 * @return o valor de dsComplemento.
	 */
	public String getDsComplemento() {
		return dsComplemento;
	}

	/** Setter para dsComplemento.
	 * @param dsComplemento o novo valor de dsComplemento.
	 */
	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}

	/** Getter para dsConselho.
	 * @return o valor de dsConselho.
	 */
	public String getDsConselho() {
		return dsConselho;
	}

	/** Setter para dsConselho.
	 * @param dsConselho o novo valor de dsConselho.
	 */
	public void setDsConselho(String dsConselho) {
		this.dsConselho = dsConselho;
	}

	/** Getter para dsConsideracoesInternas.
	 * @return o valor de dsConsideracoesInternas.
	 */
	public String getDsConsideracoesInternas() {
		return dsConsideracoesInternas;
	}

	/** Setter para dsConsideracoesInternas.
	 * @param dsConsideracoesInternas o novo valor de dsConsideracoesInternas.
	 */
	public void setDsConsideracoesInternas(String dsConsideracoesInternas) {
		this.dsConsideracoesInternas = dsConsideracoesInternas;
	}

	/** Getter para dsEmail.
	 * @return o valor de dsEmail.
	 */
	public String getDsEmail() {
		return dsEmail;
	}

	/** Setter para dsEmail.
	 * @param dsEmail o novo valor de dsEmail.
	 */
	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	/** Getter para dsEndereco.
	 * @return o valor de dsEndereco.
	 */
	public String getDsEndereco() {
		return dsEndereco;
	}

	/** Setter para dsEndereco.
	 * @param dsEndereco o novo valor de dsEndereco.
	 */
	public void setDsEndereco(String dsEndereco) {
		this.dsEndereco = dsEndereco;
	}

	/** Getter para dsMunicipio.
	 * @return o valor de dsMunicipio.
	 */
	public String getDsMunicipio() {
		return dsMunicipio;
	}

	/** Setter para dsMunicipio.
	 * @param dsMunicipio o novo valor de dsMunicipio.
	 */
	public void setDsMunicipio(String dsMunicipio) {
		this.dsMunicipio = dsMunicipio;
	}

	/** Getter para dsObservacao.
	 * @return o valor de dsObservacao.
	 */
	public String getDsObservacao() {
		return dsObservacao;
	}

	/** Setter para dsObservacao.
	 * @param dsObservacao o novo valor de dsObservacao.
	 */
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	/** Getter para dsPrestador.
	 * @return o valor de dsPrestador.
	 */
	public String getDsPrestador() {
		return dsPrestador;
	}

	/** Setter para dsPrestador.
	 * @param dsPrestador o novo valor de dsPrestador.
	 */
	public void setDsPrestador(String dsPrestador) {
		this.dsPrestador = dsPrestador;
	}

	/** Getter para dsReciboNotaFiscal.
	 * @return o valor de dsReciboNotaFiscal.
	 */
	public String getDsReciboNotaFiscal() {
		return dsReciboNotaFiscal;
	}

	/** Setter para dsReciboNotaFiscal.
	 * @param dsReciboNotaFiscal o novo valor de dsReciboNotaFiscal.
	 */
	public void setDsReciboNotaFiscal(String dsReciboNotaFiscal) {
		this.dsReciboNotaFiscal = dsReciboNotaFiscal;
	}

	/** Getter para dtAtendimento.
	 * @return o valor de dtAtendimento.
	 */
	public Date getDtAtendimento() {
		return dtAtendimento;
	}

	/** Setter para dtAtendimento.
	 * @param dtAtendimento o novo valor de dtAtendimento.
	 */
	public void setDtAtendimento(Date dtAtendimento) {
		this.dtAtendimento = dtAtendimento;
	}

	/** Getter para dtFechamento.
	 * @return o valor de dtFechamento.
	 */
	public Date getDtFechamento() {
		return dtFechamento;
	}

	/** Setter para dtFechamento.
	 * @param dtFechamento o novo valor de dtFechamento.
	 */
	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	/** Getter para dtInclusao.
	 * @return o valor de dtInclusao.
	 */
	public Date getDtInclusao() {
		return dtInclusao;
	}

	/** Setter para dtInclusao.
	 * @param dtInclusao o novo valor de dtInclusao.
	 */
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	/** Getter para dtVencimento.
	 * @return o valor de dtVencimento.
	 */
	public Date getDtVencimento() {
		return dtVencimento;
	}

	/** Setter para dtVencimento.
	 * @param dtVencimento o novo valor de dtVencimento.
	 */
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	/** Getter para dvAgencia.
	 * @return o valor de dvAgencia.
	 */
	public String getDvAgencia() {
		return dvAgencia;
	}

	/** Setter para dvAgencia.
	 * @param dvAgencia o novo valor de dvAgencia.
	 */
	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}

	/** Getter para dvContaCorrente.
	 * @return o valor de dvContaCorrente.
	 */
	public String getDvContaCorrente() {
		return dvContaCorrente;
	}

	/** Setter para dvContaCorrente.
	 * @param dvContaCorrente o novo valor de dvContaCorrente.
	 */
	public void setDvContaCorrente(String dvContaCorrente) {
		this.dvContaCorrente = dvContaCorrente;
	}

	/** Getter para nmBanco.
	 * @return o valor de nmBanco.
	 */
	public String getNmBanco() {
		return nmBanco;
	}

	/** Setter para nmBanco.
	 * @param nmBanco o novo valor de nmBanco.
	 */
	public void setNmBanco(String nmBanco) {
		this.nmBanco = nmBanco;
	}

	/** Getter para nrCep.
	 * @return o valor de nrCep.
	 */
	public String getNrCep() {
		return nrCep;
	}

	/** Setter para nrCep.
	 * @param nrCep o novo valor de nrCep.
	 */
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	/** Getter para nrConta.
	 * @return o valor de nrConta.
	 */
	public String getNrConta() {
		return nrConta;
	}

	/** Setter para nrConta.
	 * @param nrConta o novo valor de nrConta.
	 */
	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}

	/** Getter para nrCpfCnpj.
	 * @return o valor de nrCpfCnpj.
	 */
	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}

	/** Setter para nrCpfCnpj.
	 * @param nrCpfCnpj o novo valor de nrCpfCnpj.
	 */
	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}

	/** Getter para nrEndereco.
	 * @return o valor de nrEndereco.
	 */
	public String getNrEndereco() {
		return nrEndereco;
	}

	/** Setter para nrEndereco.
	 * @param nrEndereco o novo valor de nrEndereco.
	 */
	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}

	/** Getter para nrProtocoloAns.
	 * @return o valor de nrProtocoloAns.
	 */
	public Integer getNrProtocoloAns() {
		return nrProtocoloAns;
	}

	/** Setter para nrProtocoloAns.
	 * @param nrProtocoloAns o novo valor de nrProtocoloAns.
	 */
	public void setNrProtocoloAns(Integer nrProtocoloAns) {
		this.nrProtocoloAns = nrProtocoloAns;
	}

	/** Getter para nrTelefone.
	 * @return o valor de nrTelefone.
	 */
	public String getNrTelefone() {
		return nrTelefone;
	}

	/** Setter para nrTelefone.
	 * @param nrTelefone o novo valor de nrTelefone.
	 */
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	/** Getter para snRecusado.
	 * @return o valor de snRecusado.
	 */
	public SimNao getSnRecusado() {
		return snRecusado;
	}

	/** Setter para snRecusado.
	 * @param snRecusado o novo valor de snRecusado.
	 */
	public void setSnRecusado(SimNao snRecusado) {
		this.snRecusado = snRecusado;
	}

	/** Getter para tpLogradouro.
	 * @return o valor de tpLogradouro.
	 */
	public String getTpLogradouro() {
		return tpLogradouro;
	}

	/** Setter para tpLogradouro.
	 * @param tpLogradouro o novo valor de tpLogradouro.
	 */
	public void setTpLogradouro(String tpLogradouro) {
		this.tpLogradouro = tpLogradouro;
	}

	/** Getter para tpPessoa.
	 * @return o valor de tpPessoa.
	 */
	public String getTpPessoa() {
		return tpPessoa;
	}

	/** Setter para tpPessoa.
	 * @param tpPessoa o novo valor de tpPessoa.
	 */
	public void setTpPessoa(String tpPessoa) {
		this.tpPessoa = tpPessoa;
	}

	/** Getter para tpReembolso.
	 * @return o valor de tpReembolso.
	 */
	public String getTpReembolso() {
		return tpReembolso;
	}

	/** Setter para tpReembolso.
	 * @param tpReembolso o novo valor de tpReembolso.
	 */
	public void setTpReembolso(String tpReembolso) {
		this.tpReembolso = tpReembolso;
	}

	/** Getter para tpStatus.
	 * @return o valor de tpStatus.
	 */
	public String getTpStatus() {
		return tpStatus;
	}

	/** Setter para tpStatus.
	 * @param tpStatus o novo valor de tpStatus.
	 */
	public void setTpStatus(String tpStatus) {
		this.tpStatus = tpStatus;
	}

	/** Getter para tpStatusReembolso.
	 * @return o valor de tpStatusReembolso.
	 */
	public String getTpStatusReembolso() {
		return tpStatusReembolso;
	}

	/** Setter para tpStatusReembolso.
	 * @param tpStatusReembolso o novo valor de tpStatusReembolso.
	 */
	public void setTpStatusReembolso(String tpStatusReembolso) {
		this.tpStatusReembolso = tpStatusReembolso;
	}

	/** Getter para vlTotalOriginal.
	 * @return o valor de vlTotalOriginal.
	 */
	public Double getVlTotalOriginal() {
		return vlTotalOriginal;
	}

	/** Setter para vlTotalOriginal.
	 * @param vlTotalOriginal o novo valor de vlTotalOriginal.
	 */
	public void setVlTotalOriginal(Double vlTotalOriginal) {
		this.vlTotalOriginal = vlTotalOriginal;
	}

	/** Getter para itensReembolso.
	 * @return o valor de itensReembolso.
	 */
	public List<ItemReembolso> getItensReembolso() {
		return itensReembolso;
	}

	/** Setter para itensReembolso.
	 * @param itensReembolso o novo valor de itensReembolso.
	 */
	public void setItensReembolso(List<ItemReembolso> itensReembolso) {
		this.itensReembolso = itensReembolso;
	}
	
}
