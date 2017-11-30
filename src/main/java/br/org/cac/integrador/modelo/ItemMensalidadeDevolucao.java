package br.org.cac.integrador.modelo;

public class ItemMensalidadeDevolucao {

	private Integer cdReembolso;
	
	private Integer cdLctoMensalidade;
	
	private Integer cdSetor;
	
	private Integer cdItemRes;
	
	private Double qtCobrado;
	
	private Double vlCobrado;
	
	public ItemMensalidadeDevolucao() {
	
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

	/** Getter para cdLctoMensalidade.
	 * @return o valor de cdLctoMensalidade.
	 */
	public Integer getCdLctoMensalidade() {
		return cdLctoMensalidade;
	}

	/** Setter para cdLctoMensalidade.
	 * @param cdLctoMensalidade o novo valor de cdLctoMensalidade.
	 */
	public void setCdLctoMensalidade(Integer cdLctoMensalidade) {
		this.cdLctoMensalidade = cdLctoMensalidade;
	}

	/** Getter para cdSetor.
	 * @return o valor de cdSetor.
	 */
	public Integer getCdSetor() {
		return cdSetor;
	}

	/** Setter para cdSetor.
	 * @param cdSetor o novo valor de cdSetor.
	 */
	public void setCdSetor(Integer cdSetor) {
		this.cdSetor = cdSetor;
	}

	/** Getter para cdItemRes.
	 * @return o valor de cdItemRes.
	 */
	public Integer getCdItemRes() {
		return cdItemRes;
	}

	/** Setter para cdItemRes.
	 * @param cdItemRes o novo valor de cdItemRes.
	 */
	public void setCdItemRes(Integer cdItemRes) {
		this.cdItemRes = cdItemRes;
	}

	/** Getter para qtCobrado.
	 * @return o valor de qtCobrado.
	 */
	public Double getQtCobrado() {
		return qtCobrado;
	}

	/** Setter para qtCobrado.
	 * @param qtCobrado o novo valor de qtCobrado.
	 */
	public void setQtCobrado(Double qtCobrado) {
		this.qtCobrado = qtCobrado;
	}

	/** Getter para vlCobrado.
	 * @return o valor de vlCobrado.
	 */
	public Double getVlCobrado() {
		return vlCobrado;
	}

	/** Setter para vlCobrado.
	 * @param vlCobrado o novo valor de vlCobrado.
	 */
	public void setVlCobrado(Double vlCobrado) {
		this.vlCobrado = vlCobrado;
	}	
}
