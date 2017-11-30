package br.org.cac.integrador.modelo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MensalidadeContrato {
	
	private Integer cdContrato;
	
	private Integer cdMensContratoInterno;
	
	private Integer cdMotivoCancelamento;
	
	private Integer cdMultiEmpresa;
	
	private String cdNossoNumero;
	
	private String dsObservacao;
	
	private Date dtCancelamento;
	
	private Date dtEmissao;
	
	private Date dtVencimento;
	
	private Date dtVencimentoOriginal;
	
	private String nrAgencia;
	
	private Integer nrAno;
	
	private Integer nrBanco;
	
	private String nrConta;
	
	private String nrDocumento;
	
	private Integer nrMes;
	
	private Integer nrParcela;
	
	private String tpQuitacao;
	
	private String tpReceita;
	
	private Double vlJurosMora;
	
	private Double vlMensalidade;
	
	private Double vlPago;
	
	private Double vlPercentualMulta;
	
	@JsonProperty("itensMensalidades")
	private List<ItemMensalidadeUsuario> itensMensalidadeUsuario;
	
	public MensalidadeContrato(){
		
	}

	/** Getter para cdContrato.
	 * @return o valor de cdContrato.
	 */
	public Integer getCdContrato() {
		return cdContrato;
	}

	/** Setter para cdContrato.
	 * @param cdContrato o novo valor de cdContrato.
	 */
	public void setCdContrato(Integer cdContrato) {
		this.cdContrato = cdContrato;
	}

	/** Getter para cdMensContratoInterno.
	 * @return o valor de cdMensContratoInterno.
	 */
	public Integer getCdMensContratoInterno() {
		return cdMensContratoInterno;
	}

	/** Setter para cdMensContratoInterno.
	 * @param cdMensContratoInterno o novo valor de cdMensContratoInterno.
	 */
	public void setCdMensContratoInterno(Integer cdMensContratoInterno) {
		this.cdMensContratoInterno = cdMensContratoInterno;
	}

	/** Getter para cdMotivoCancelamento.
	 * @return o valor de cdMotivoCancelamento.
	 */
	public Integer getCdMotivoCancelamento() {
		return cdMotivoCancelamento;
	}

	/** Setter para cdMotivoCancelamento.
	 * @param cdMotivoCancelamento o novo valor de cdMotivoCancelamento.
	 */
	public void setCdMotivoCancelamento(Integer cdMotivoCancelamento) {
		this.cdMotivoCancelamento = cdMotivoCancelamento;
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

	/** Getter para cdNossoNumero.
	 * @return o valor de cdNossoNumero.
	 */
	public String getCdNossoNumero() {
		return cdNossoNumero;
	}

	/** Setter para cdNossoNumero.
	 * @param cdNossoNumero o novo valor de cdNossoNumero.
	 */
	public void setCdNossoNumero(String cdNossoNumero) {
		this.cdNossoNumero = cdNossoNumero;
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

	/** Getter para dtCancelamento.
	 * @return o valor de dtCancelamento.
	 */
	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	/** Setter para dtCancelamento.
	 * @param dtCancelamento o novo valor de dtCancelamento.
	 */
	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}

	/** Getter para dtEmissao.
	 * @return o valor de dtEmissao.
	 */
	public Date getDtEmissao() {
		return dtEmissao;
	}

	/** Setter para dtEmissao.
	 * @param dtEmissao o novo valor de dtEmissao.
	 */
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
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

	/** Getter para dtVencimentoOriginal.
	 * @return o valor de dtVencimentoOriginal.
	 */
	public Date getDtVencimentoOriginal() {
		return dtVencimentoOriginal;
	}

	/** Setter para dtVencimentoOriginal.
	 * @param dtVencimentoOriginal o novo valor de dtVencimentoOriginal.
	 */
	public void setDtVencimentoOriginal(Date dtVencimentoOriginal) {
		this.dtVencimentoOriginal = dtVencimentoOriginal;
	}

	/** Getter para nrAgencia.
	 * @return o valor de nrAgencia.
	 */
	public String getNrAgencia() {
		return nrAgencia;
	}

	/** Setter para nrAgencia.
	 * @param nrAgencia o novo valor de nrAgencia.
	 */
	public void setNrAgencia(String nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	/** Getter para nrAno.
	 * @return o valor de nrAno.
	 */
	public Integer getNrAno() {
		return nrAno;
	}

	/** Setter para nrAno.
	 * @param nrAno o novo valor de nrAno.
	 */
	public void setNrAno(Integer nrAno) {
		this.nrAno = nrAno;
	}

	/** Getter para nrBanco.
	 * @return o valor de nrBanco.
	 */
	public Integer getNrBanco() {
		return nrBanco;
	}

	/** Setter para nrBanco.
	 * @param nrBanco o novo valor de nrBanco.
	 */
	public void setNrBanco(Integer nrBanco) {
		this.nrBanco = nrBanco;
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

	/** Getter para nrDocumento.
	 * @return o valor de nrDocumento.
	 */
	public String getNrDocumento() {
		return nrDocumento;
	}

	/** Setter para nrDocumento.
	 * @param nrDocumento o novo valor de nrDocumento.
	 */
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	/** Getter para nrMes.
	 * @return o valor de nrMes.
	 */
	public Integer getNrMes() {
		return nrMes;
	}

	/** Setter para nrMes.
	 * @param nrMes o novo valor de nrMes.
	 */
	public void setNrMes(Integer nrMes) {
		this.nrMes = nrMes;
	}

	/** Getter para nrParcela.
	 * @return o valor de nrParcela.
	 */
	public Integer getNrParcela() {
		return nrParcela;
	}

	/** Setter para nrParcela.
	 * @param nrParcela o novo valor de nrParcela.
	 */
	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	/** Getter para tpQuitacao.
	 * @return o valor de tpQuitacao.
	 */
	public String getTpQuitacao() {
		return tpQuitacao;
	}

	/** Setter para tpQuitacao.
	 * @param tpQuitacao o novo valor de tpQuitacao.
	 */
	public void setTpQuitacao(String tpQuitacao) {
		this.tpQuitacao = tpQuitacao;
	}

	/** Getter para tpReceita.
	 * @return o valor de tpReceita.
	 */
	public String getTpReceita() {
		return tpReceita;
	}

	/** Setter para tpReceita.
	 * @param tpReceita o novo valor de tpReceita.
	 */
	public void setTpReceita(String tpReceita) {
		this.tpReceita = tpReceita;
	}

	/** Getter para vlJurosMora.
	 * @return o valor de vlJurosMora.
	 */
	public Double getVlJurosMora() {
		return vlJurosMora;
	}

	/** Setter para vlJurosMora.
	 * @param vlJurosMora o novo valor de vlJurosMora.
	 */
	public void setVlJurosMora(Double vlJurosMora) {
		this.vlJurosMora = vlJurosMora;
	}

	/** Getter para vlMensalidade.
	 * @return o valor de vlMensalidade.
	 */
	public Double getVlMensalidade() {
		return vlMensalidade;
	}

	/** Setter para vlMensalidade.
	 * @param vlMensalidade o novo valor de vlMensalidade.
	 */
	public void setVlMensalidade(Double vlMensalidade) {
		this.vlMensalidade = vlMensalidade;
	}

	/** Getter para vlPago.
	 * @return o valor de vlPago.
	 */
	public Double getVlPago() {
		return vlPago;
	}

	/** Setter para vlPago.
	 * @param vlPago o novo valor de vlPago.
	 */
	public void setVlPago(Double vlPago) {
		this.vlPago = vlPago;
	}

	/** Getter para vlPercentualMulta.
	 * @return o valor de vlPercentualMulta.
	 */
	public Double getVlPercentualMulta() {
		return vlPercentualMulta;
	}

	/** Setter para vlPercentualMulta.
	 * @param vlPercentualMulta o novo valor de vlPercentualMulta.
	 */
	public void setVlPercentualMulta(Double vlPercentualMulta) {
		this.vlPercentualMulta = vlPercentualMulta;
	}

	/** Getter para itensMensalidadeUsuario.
	 * @return o valor de itensMensalidadeUsuario.
	 */
	public List<ItemMensalidadeUsuario> getItensMensalidadeUsuario() {
		return itensMensalidadeUsuario;
	}

	/** Setter para itensMensalidadeUsuario.
	 * @param itensMensalidadeUsuario o novo valor de itensMensalidadeUsuario.
	 */
	public void setItensMensalidadeUsuario(List<ItemMensalidadeUsuario> itensMensalidadeUsuario) {
		this.itensMensalidadeUsuario = itensMensalidadeUsuario;
	}
	
}
