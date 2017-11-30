package br.org.cac.integrador.modelo;

import java.util.List;

import br.org.cac.integrador.util.NumberUtil;

public class ItemMensalidadeUsuario {

	private Integer cdLctoMensalidade;
	
	private Integer cdMatricula;
	
	private Integer cdMensContratoInterno;
	
	private Integer cdMensUsuario;
	
	private String dsObservacao;
	
	private Double vlLancamento;
	
	private List<ImpCoparticipacao> impCoparticipacoes;
	
	public ItemMensalidadeUsuario(){
		
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

	/** Getter para cdMensUsuario.
	 * @return o valor de cdMensUsuario.
	 */
	public Integer getCdMensUsuario() {
		return cdMensUsuario;
	}

	/** Setter para cdMensUsuario.
	 * @param cdMensUsuario o novo valor de cdMensUsuario.
	 */
	public void setCdMensUsuario(Integer cdMensUsuario) {
		this.cdMensUsuario = cdMensUsuario;
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

	/** Getter para vlLancamento.
	 * @return o valor de vlLancamento.
	 */
	public Double getVlLancamento() {
		return vlLancamento;
	}

	/** Setter para vlLancamento.
	 * @param vlLancamento o novo valor de vlLancamento.
	 */
	public void setVlLancamento(Double vlLancamento) {
		this.vlLancamento = NumberUtil.roundToScaleTwo(vlLancamento);
	}

	/** Getter para impCoparticipacoes.
	 * @return o valor de impCoparticipacoes.
	 */
	public List<ImpCoparticipacao> getImpCoparticipacoes() {
		return impCoparticipacoes;
	}

	/** Setter para impCoparticipacoes.
	 * @param impCoparticipacoes o novo valor de impCoparticipacoes.
	 */
	public void setImpCoparticipacoes(List<ImpCoparticipacao> impCoparticipacoes) {
		this.impCoparticipacoes = impCoparticipacoes;
	}
	
}
