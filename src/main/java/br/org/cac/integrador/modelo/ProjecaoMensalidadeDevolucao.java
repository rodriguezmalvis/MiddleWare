package br.org.cac.integrador.modelo;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import br.org.cac.integrador.util.NumberUtil;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "listaMensalidadesDevolucao",
							   procedureName = "SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO",
							   resultClasses = {ProjecaoMensalidadeDevolucao.class },
							   parameters = {
									   @StoredProcedureParameter(name = "id_grupo_item_mensalidade_trabalho", 
											   					 type = Integer.class, 
											   					 mode = ParameterMode.IN)
							})			
})
public class ProjecaoMensalidadeDevolucao {
	
	@Column(name="seq_item_devolucao")
	private Integer seqItemDevolucao;
	
	@Id
	@Column(name="id_comando")
	private Integer idComando;
	
	@Column(name="cd_matricula_titular")
	private Integer cdMatriculaTitular;
	
	@Column(name="cd_matricula_fornecedor")
	private Integer cdMatriculaFornecedor;
	
	@Column(name="tp_reembolso", length=1)
	private String tpReembolso;
	
	@Column(name="cd_reembolso")
	private Integer cdReembolso;
	
	@Column(name="cd_controle_interno")
	private String cdControleInterno;
	
	@Column(name="cd_matricula")
	private Integer cdMatricula;
	
	@Column(name="cd_mens_contrato")
	private Integer cdMensContrato;
	
	@Column(name="vl_total_original")
	@Access(AccessType.PROPERTY)
	private Double vlTotalOriginal;
	
	@Column(name="cd_fornecedor")
	private Integer cdFornecedor;
	
	@Column(name="dt_vencimento")
	private Date dtVencimento;
	
	@Column(name="dt_inclusao")
	private Date dtInclusao;
	
	@Column(name="cd_usuario_inclusao")
	private String cdUsuarioInclusao;
	
	@Column(name="cd_tip_doc")
	private Integer cdTipDoc;
	
	@Column(name="cd_multi_empresa")
	private Integer cdMultiEmpresa;
	
	@Column(name="sn_notificado")
	private SimNao snNotificado;
	
	@Column(name="sn_recusado")
	private SimNao snRecusado;
	
	@Column(name="nr_ano")
	private Integer nrAno;
	
	@Column(name="nr_mes")
	private Integer nrMes;
	
	@Column(name="cd_lcto_mensalidade")
	private Integer cdLctoMensalidade;
	
	@Column(name="cd_setor")
	private Integer cdSetor;
	
	@Column(name="cd_item_res")
	private Integer cdItemRes;
	
	@Column(name="qt_cobrado")
	@Access(AccessType.PROPERTY)
	private Double qtCobrado;
	
	@Column(name="vl_cobrado")
	@Access(AccessType.PROPERTY)
	private Double vlCobrado;
	
	public ProjecaoMensalidadeDevolucao(){
		
	}

	/** Getter para seqItemDevolucao.
	 * @return o valor de seqItemDevolucao.
	 */
	public Integer getSeqItemDevolucao() {
		return seqItemDevolucao;
	}

	/** Setter para seqItemDevolucao.
	 * @param seqItemDevolucao o novo valor de seqItemDevolucao.
	 */
	public void setSeqItemDevolucao(Integer seqItemDevolucao) {
		this.seqItemDevolucao = seqItemDevolucao;
	}

	/** Getter para idComando.
	 * @return o valor de idComando.
	 */
	public Integer getIdComando() {
		return idComando;
	}

	/** Setter para idComando.
	 * @param idComando o novo valor de idComando.
	 */
	public void setIdComando(Integer idComando) {
		this.idComando = idComando;
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

	/** Getter para cdMatriculaFornecedor.
	 * @return o valor de cdMatriculaFornecedor.
	 */
	public Integer getCdMatriculaFornecedor() {
		return cdMatriculaFornecedor;
	}

	/** Setter para cdMatriculaFornecedor.
	 * @param cdMatriculaFornecedor o novo valor de cdMatriculaFornecedor.
	 */
	public void setCdMatriculaFornecedor(Integer cdMatriculaFornecedor) {
		this.cdMatriculaFornecedor = cdMatriculaFornecedor;
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

	/** Getter para cdControleInterno.
	 * @return o valor de cdControleInterno.
	 */
	public String getCdControleInterno() {
		return cdControleInterno;
	}

	/** Setter para cdControleInterno.
	 * @param cdControleInterno o novo valor de cdControleInterno.
	 */
	public void setCdControleInterno(String cdControleInterno) {
		this.cdControleInterno = cdControleInterno;
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

	/** Getter para cdMensContrato.
	 * @return o valor de cdMensContrato.
	 */
	public Integer getCdMensContrato() {
		return cdMensContrato;
	}

	/** Setter para cdMensContrato.
	 * @param cdMensContrato o novo valor de cdMensContrato.
	 */
	public void setCdMensContrato(Integer cdMensContrato) {
		this.cdMensContrato = cdMensContrato;
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
		this.vlTotalOriginal = NumberUtil.roundToScaleTwo(vlTotalOriginal);
	}

	/** Getter para cdFornecedor.
	 * @return o valor de cdFornecedor.
	 */
	public Integer getCdFornecedor() {
		return cdFornecedor;
	}

	/** Setter para cdFornecedor.
	 * @param cdFornecedor o novo valor de cdFornecedor.
	 */
	public void setCdFornecedor(Integer cdFornecedor) {
		this.cdFornecedor = cdFornecedor;
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

	/** Getter para cdTipDoc.
	 * @return o valor de cdTipDoc.
	 */
	public Integer getCdTipDoc() {
		return cdTipDoc;
	}

	/** Setter para cdTipDoc.
	 * @param cdTipDoc o novo valor de cdTipDoc.
	 */
	public void setCdTipDoc(Integer cdTipDoc) {
		this.cdTipDoc = cdTipDoc;
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

	/** Getter para snNotificado.
	 * @return o valor de snNotificado.
	 */
	public SimNao getSnNotificado() {
		return snNotificado;
	}

	/** Setter para snNotificado.
	 * @param snNotificado o novo valor de snNotificado.
	 */
	public void setSnNotificado(SimNao snNotificado) {
		this.snNotificado = snNotificado;
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
		this.qtCobrado = NumberUtil.roundToScaleTwo(qtCobrado);
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
