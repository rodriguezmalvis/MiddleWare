package br.org.cac.integrador.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "listaLotes", 
								   procedureName = "SPR_ICM_LISTA_LOTE", 
								   resultClasses = {LoteContaMedica.class }, 
								   parameters = {
									@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN),
								}) 
})
public class LoteContaMedica {
	
	@Transient
	private Integer cdLote;

	@EmbeddedId
	@JsonIgnore
	private SubProcessoId subProcesso;

	@Column(name = "cd_prestador")
	private Integer cdPrestador;

	@Column(name = "ds_lote", length = 40)
	private String dsLote;

	@Column(name = "dt_inicial")
	private Date dtInicial;

	@Column(name = "dt_final")
	private Date dtFinal;

	@Column(name = "dt_lote")
	private Date dtLote;

	@Column(name = "dt_vencimento")
	private Date dtVencimento;

	@Column(name = "nr_ano", columnDefinition = "smallint")
	private Integer nrAno;

	@Column(name = "nr_ano_pagto", columnDefinition = "smallint")
	private Integer nrAnoPagto;

	@Column(name = "nr_mes", columnDefinition = "smallint")
	// TODO: Não funciona @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="0#")
	private Integer nrMes;

	@Column(name = "nr_mes_pagto", columnDefinition = "smallint")
	// TODO: Não funciona @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="0#")
	private Integer nrMesPagto;

	@Column(name = "sn_fechado", length = 1)
	private SimNao snFechado;

	@Column(name = "tp_guia", length = 1)	
	private TipoGuia tpGuia;

	@Column(name = "tp_lote", length = 1) // valor padrão = "P"
	private String tpLote;

	@Column(name = "tp_origem", length = 1) // valor padrão = "L"
	private String tpOrigem;
	
	@Transient
	private List<ContaMedica> contasMedicas;

	public LoteContaMedica() {
		this.tpLote = "P";
		this.tpOrigem = "L";
	}
	
	
	/** Getter para cdLote.
	 * @return o valor de cdLote.
	 */
	public Integer getCdLote() {
		return cdLote;
	}

	/** Setter para cdLote.
	 * @param cdLote o novo valor de cdLote.
	 */
	public void setCdLote(Integer cdLote) {
		this.cdLote = cdLote;
	}

	/**
	 * Getter para subProcesso.
	 * 
	 * @return o valor de subProcesso.
	 */
	public SubProcessoId getSubProcesso() {
		return subProcesso;
	}

	/**
	 * Setter para subProcesso.
	 * 
	 * @param subProcesso
	 *            o novo valor de subProcesso.
	 */
	public void setSubProcesso(SubProcessoId subProcesso) {
		this.subProcesso = subProcesso;
	}

	/**
	 * Getter para cdPrestador.
	 * 
	 * @return o valor de cdPrestador.
	 */
	public Integer getCdPrestador() {
		return cdPrestador;
	}

	/**
	 * Setter para cdPrestador.
	 * 
	 * @param cdPrestador
	 *            o novo valor de cdPrestador.
	 */
	public void setCdPrestador(Integer cdPrestador) {
		this.cdPrestador = cdPrestador;
	}

	/**
	 * Getter para dsLote.
	 * 
	 * @return o valor de dsLote.
	 */
	public String getDsLote() {
		return dsLote;
	}

	/**
	 * Setter para dsLote.
	 * 
	 * @param dsLote
	 *            o novo valor de dsLote.
	 */
	public void setDsLote(String dsLote) {
		this.dsLote = dsLote;
	}

	/**
	 * Getter para dtInicial.
	 * 
	 * @return o valor de dtInicial.
	 */
	public Date getDtInicial() {
		return dtInicial;
	}

	/**
	 * Setter para dtInicial.
	 * 
	 * @param dtInicial
	 *            o novo valor de dtInicial.
	 */
	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	/**
	 * Getter para dtFinal.
	 * 
	 * @return o valor de dtFinal.
	 */
	public Date getDtFinal() {
		return dtFinal;
	}

	/**
	 * Setter para dtFinal.
	 * 
	 * @param dtFinal
	 *            o novo valor de dtFinal.
	 */
	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	/**
	 * Getter para dtLote.
	 * 
	 * @return o valor de dtLote.
	 */
	public Date getDtLote() {
		return dtLote;
	}

	/**
	 * Setter para dtLote.
	 * 
	 * @param dtLote
	 *            o novo valor de dtLote.
	 */
	public void setDtLote(Date dtLote) {
		this.dtLote = dtLote;
	}

	/**
	 * Getter para dtVencimento.
	 * 
	 * @return o valor de dtVencimento.
	 */
	public Date getDtVencimento() {
		return dtVencimento;
	}

	/**
	 * Setter para dtVencimento.
	 * 
	 * @param dtVencimento
	 *            o novo valor de dtVencimento.
	 */
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	/**
	 * Getter para nrAno.
	 * 
	 * @return o valor de nrAno.
	 */
	public Integer getNrAno() {
		return nrAno;
	}

	/**
	 * Setter para nrAno.
	 * 
	 * @param nrAno
	 *            o novo valor de nrAno.
	 */
	public void setNrAno(Integer nrAno) {
		this.nrAno = nrAno;
	}

	/**
	 * Getter para nrAnoPagto.
	 * 
	 * @return o valor de nrAnoPagto.
	 */
	public Integer getNrAnoPagto() {
		return nrAnoPagto;
	}

	/**
	 * Setter para nrAnoPagto.
	 * 
	 * @param nrAnoPagto
	 *            o novo valor de nrAnoPagto.
	 */
	public void setNrAnoPagto(Integer nrAnoPagto) {
		this.nrAnoPagto = nrAnoPagto;
	}

	/**
	 * Getter para nrMes.
	 * 
	 * @return o valor de nrMes.
	 */
	public Integer getNrMes() {
		return nrMes;
	}

	/**
	 * Setter para nrMes.
	 * 
	 * @param nrMes
	 *            o novo valor de nrMes.
	 */
	public void setNrMes(Integer nrMes) {
		this.nrMes = nrMes;
	}

	/**
	 * Getter para nrMesPagto.
	 * 
	 * @return o valor de nrMesPagto.
	 */
	public Integer getNrMesPagto() {
		return nrMesPagto;
	}

	/**
	 * Setter para nrMesPagto.
	 * 
	 * @param nrMesPagto
	 *            o novo valor de nrMesPagto.
	 */
	public void setNrMesPagto(Integer nrMesPagto) {
		this.nrMesPagto = nrMesPagto;
	}

	/**
	 * Getter para snFechado.
	 * 
	 * @return o valor de snFechado.
	 */
	public SimNao getSnFechado() {
		return snFechado;
	}

	/**
	 * Setter para snFechado.
	 * 
	 * @param snFechado
	 *            o novo valor de snFechado.
	 */
	public void setSnFechado(SimNao snFechado) {
		this.snFechado = snFechado;
	}

	/**
	 * Getter para tpGuia.
	 * 
	 * @return o valor de tpGuia.
	 */
	public TipoGuia getTpGuia() {
		return tpGuia;
	}

	/**
	 * Setter para tpGuia.
	 * 
	 * @param tpGuia
	 *            o novo valor de tpGuia.
	 */
	public void setTpGuia(TipoGuia tipoGuia) {
		this.tpGuia = tipoGuia;
	}

	/**
	 * Getter para tpLote.
	 * 
	 * @return o valor de tpLote.
	 */
	public String getTpLote() {
		return tpLote;
	}

	/**
	 * Setter para tpLote.
	 * 
	 * @param tpLote
	 *            o novo valor de tpLote.
	 */
	public void setTpLote(String tpLote) {
		this.tpLote = tpLote;
	}

	/**
	 * Getter para tpOrigem.
	 * 
	 * @return o valor de tpOrigem.
	 */
	public String getTpOrigem() {
		return tpOrigem;
	}

	/**
	 * Setter para tpOrigem.
	 * 
	 * @param tpOrigem
	 *            o novo valor de tpOrigem.
	 */
	public void setTpOrigem(String tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	
	/** Getter para contasMedicas.
	 * @return o valor de contasMedicas.
	 */
	public List<ContaMedica> getContasMedicas() {
		return contasMedicas;
	}

	/** Setter para contasMedicas.
	 * @param contasMedicas o novo valor de contasMedicas.
	 */
	public void setContasMedicas(List<ContaMedica> contasMedicas) {
		this.contasMedicas = contasMedicas;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoteContaMedica [" + (cdLote != null ? "cdLote=" + cdLote + ", " : "")
				+ (subProcesso != null ? "subProcesso=" + subProcesso + ", " : "")
				+ (cdPrestador != null ? "cdPrestador=" + cdPrestador + ", " : "")
				+ (dsLote != null ? "dsLote=" + dsLote + ", " : "")
				+ (dtInicial != null ? "dtInicial=" + dtInicial + ", " : "")
				+ (dtFinal != null ? "dtFinal=" + dtFinal + ", " : "")
				+ (dtLote != null ? "dtLote=" + dtLote + ", " : "")
				+ (dtVencimento != null ? "dtVencimento=" + dtVencimento + ", " : "")
				+ (nrAno != null ? "nrAno=" + nrAno + ", " : "")
				+ (nrAnoPagto != null ? "nrAnoPagto=" + nrAnoPagto + ", " : "")
				+ (nrMes != null ? "nrMes=" + nrMes + ", " : "")
				+ (nrMesPagto != null ? "nrMesPagto=" + nrMesPagto + ", " : "")
				+ (snFechado != null ? "snFechado=" + snFechado + ", " : "")
				+ (tpGuia != null ? "tpGuia=" + tpGuia + ", " : "")
				+ (tpLote != null ? "tpLote=" + tpLote + ", " : "") + (tpOrigem != null ? "tpOrigem=" + tpOrigem : "")
				+ "]";
	}


	
}
