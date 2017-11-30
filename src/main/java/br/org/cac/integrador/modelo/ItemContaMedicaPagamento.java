package br.org.cac.integrador.modelo;

import javax.persistence.Access;
import javax.persistence.AccessType;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cac.integrador.util.NumberUtil;

public class ItemContaMedicaPagamento implements Cloneable{

	private Integer cdAtiMed;
	
	@JsonProperty("cdItcontaMedica")
	private Long cdItContaMedica;
	
	private Integer cdMotivo;
	
	private Integer cdPrestador;
	
	private String tpSituacao;
	
	@Access(AccessType.PROPERTY)
	private Double vlPago;
	
	@Access(AccessType.PROPERTY)
	private Double vlTotalCobrado;
	
	public ItemContaMedicaPagamento(){
		
	}

	/** Getter para cdAtiMed.
	 * @return o valor de cdAtiMed.
	 */
	public Integer getCdAtiMed() {
		return cdAtiMed;
	}

	/** Setter para cdAtiMed.
	 * @param cdAtiMed o novo valor de cdAtiMed.
	 */
	public void setCdAtiMed(Integer cdAtiMed) {
		this.cdAtiMed = cdAtiMed;
	}

	/** Getter para cdItContaMedica.
	 * @return o valor de cdItContaMedica.
	 */
	public Long getCdItContaMedica() {
		return cdItContaMedica;
	}

	/** Setter para cdItContaMedica.
	 * @param cdItContaMedica o novo valor de cdItContaMedica.
	 */
	public void setCdItContaMedica(Long cdItContaMedica) {
		this.cdItContaMedica = cdItContaMedica;
	}

	/** Getter para cdMotivo.
	 * @return o valor de cdMotivo.
	 */
	public Integer getCdMotivo() {
		return cdMotivo;
	}

	/** Setter para cdMotivo.
	 * @param cdMotivo o novo valor de cdMotivo.
	 */
	public void setCdMotivo(Integer cdMotivo) {
		this.cdMotivo = cdMotivo;
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

	/** Getter para tpSituacao.
	 * @return o valor de tpSituacao.
	 */
	public String getTpSituacao() {
		return tpSituacao;
	}

	/** Setter para tpSituacao.
	 * @param tpSituacao o novo valor de tpSituacao.
	 */
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
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
		this.vlPago = NumberUtil.roundToScaleTwo(vlPago);
	}

	/** Getter para vlTotalCobrado.
	 * @return o valor de vlTotalCobrado.
	 */
	public Double getVlTotalCobrado() {
		return vlTotalCobrado;
	}

	/** Setter para vlTotalCobrado.
	 * @param vlTotalCobrado o novo valor de vlTotalCobrado.
	 */
	public void setVlTotalCobrado(Double vlTotalCobrado) {
		this.vlTotalCobrado = NumberUtil.roundToScaleTwo(vlTotalCobrado);
	}
	
	@Override
	public ItemContaMedicaPagamento clone() {
		ItemContaMedicaPagamento novo = new ItemContaMedicaPagamento();
		novo.setCdAtiMed(this.getCdAtiMed());
		novo.setCdItContaMedica(this.getCdItContaMedica());
		novo.setCdMotivo(this.getCdMotivo());
		novo.setCdPrestador(this.getCdPrestador());
		novo.setTpSituacao(this.getTpSituacao());
		novo.setVlPago(this.getVlPago());
		novo.setVlTotalCobrado(this.getVlTotalCobrado());
		
		
		return novo;
	}
	
}
