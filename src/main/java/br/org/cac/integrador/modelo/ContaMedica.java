package br.org.cac.integrador.modelo;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cac.integrador.util.NumberUtil;

@Entity
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "listaContasMedicas", procedureName = "SPR_ICM_LISTA_CONTAS_MEDICAS", resultClasses = {
				ContaMedica.class }, parameters = {
						@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "cd_lote", type = Integer.class, mode = ParameterMode.IN) }),
		@NamedStoredProcedureQuery(name="calculaCoparticipacaoProcedimento", procedureName = "SP_CALCULA_COPARTICIPACAO_PROCEDIMENTO",
				parameters = {
						@StoredProcedureParameter(name = "ano_apresentacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_representacao", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_processo", type = Integer.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_sub_processo", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "d_natureza", type = String.class, mode = ParameterMode.IN),
						@StoredProcedureParameter(name = "id_sequencial_natureza", type = Integer.class, mode = ParameterMode.IN) }) 
		})
public class ContaMedica implements Comparable<ContaMedica> {

	@EmbeddedId
	@JsonIgnore
	private AtendimentoId atendimento;

	@Column(name = "seq_conta_medica")
	@JsonIgnore
	private Integer seqContaMedica;

	@Column
	private String cdCid;

	@Column
	private String cdCid2;

	@Column
	private String cdCid3;

	@Column
	private String cdCid4;

	@Column
	private String cdCidObito;

	@Column(length = 7)
	private String cdCnes;

	@Column
	private String cdCnesLocalAtendimento;

	@Column
	private Integer cdContaMedica;

	@Column
	private Integer cdContaMedicaTem;

	@Column(columnDefinition = "tinyint")
	private Integer cdEspecialidade;

	@Column(length = 1)
	private String cdIndicadorAcidente;

	@Column
	private Integer cdLote;

	@Column
	private Integer cdMatricula;

	@Column
	private Integer cdMotivo;

	@Column
	private Integer cdMotivoAlta;

	@Column
	private Integer cdMotivoObitoMulher;

	@Column(length = 1)
	private String cdPlano;

	@Column
	private Integer cdPrestador;

	@Column
	private Integer cdPrestadorEndereco;

	@Column
	private Integer cdPrestadorLocalAtendimento;

	@Column
	private Integer cdPrestadorSolicitante;

	@Column(length = 2)
	private String cdRegimeInternacao;

	@Column
	private Integer cdTipAcomodacao;

	@Column(length = 2)
	private String cdTipoAtendimentoTiss;

	@Column(length = 1)
	private String cdTipoConsulta;

	@Column(length = 1)
	private String cdTipoFaturamento;

	@Column
	private Integer cdTipoSaidaGuiaConsulta;

	@Column
	private Integer cdTipoSaidaGuiaSadt;

	@Column
	private String dsObservacao;

	@Column
	private Date dtAlta;

	@Column
	private Date dtApresentacao;

	@Column
	private Date dtAtendimento;

	@Column
	private Date dtAuditoria;

	@Column
	private Date dtInternacao;

	@Column
	private String nmPrestador;

	@Column
	private BigInteger nrCarteiraBeneficiario;

	@Column
	private String nrDeclaracaoNascidoVivo;

	@Column
	private String nrDeclaracaoObito;

	@Column
	private Integer nrGuia;

	@Column
	private String nrGuiaPrestadorPrincipal;

	@Column
	private Integer qtNascidoMorto;

	@Column
	private Integer qtNascidoVivoPrematuro;

	@Column
	private Integer qtNascidoVivoTermo;

	@Column
	private Integer qtObitoNeonatalPrecoce;

	@Column
	private Integer qtObitoNeonatalTardio;

	@Column
	private SimNao snAborto;

	@Column
	private SimNao snAtendimentoRnSalaParto;

	@Column
	private SimNao snBaixoPeso;

	@Column
	private SimNao snComplicacaoNeonatal;

	@Column
	private SimNao snComplicacaoPuerperio;

	@Column
	private SimNao snGestacao;

	@Column
	private SimNao snIndicadorAtendimentoRn;

	@Column
	private SimNao snObitoCausaMaterna;

	@Column
	private SimNao snPartoCesario;

	@Column
	private SimNao snPartoNormal;

	@Column
	private SimNao snReapresentacao;

	@Column
	private SimNao snTranstornoMaternoGravidez;

	@Column(length = 1)
	private String tpCaraterInternacao;

	@Column
	private String tpCaraterSolicitacao;

	@Column
	private String tpCausaInternacao;

	@Column
	private TipoContaHospitalar tpContaHospitalar;

	@Column
	private TipoGuia tpGuia;

	@Column(length = 2)
	private String tpSituacao;

	@Column
	@Access(AccessType.PROPERTY)
	private Double vlFranquia;

	@Transient
	@JsonProperty("itensContas")
	private List<ItemContaMedica> itensConta;

	/**
	 * Getter para atendimento.
	 * 
	 * @return o valor de atendimento.
	 */
	public AtendimentoId getAtendimento() {
		return atendimento;
	}

	/**
	 * Setter para atendimento.
	 * 
	 * @param atendimento
	 *            o novo valor de atendimento.
	 */
	public void setAtendimento(AtendimentoId atendimento) {
		this.atendimento = atendimento;
	}

	/**
	 * Getter para seqContaMedica.
	 * 
	 * @return o valor de seqContaMedica.
	 */
	public Integer getSeqContaMedica() {
		return seqContaMedica;
	}

	/**
	 * Setter para seqContaMedica.
	 * 
	 * @param seqContaMedica
	 *            o novo valor de seqContaMedica.
	 */
	public void setSeqContaMedica(Integer seqContaMedica) {
		this.seqContaMedica = seqContaMedica;
	}

	/**
	 * Getter para cdCid.
	 * 
	 * @return o valor de cdCid.
	 */
	public String getCdCid() {
		return cdCid;
	}

	/**
	 * Setter para cdCid.
	 * 
	 * @param cdCid
	 *            o novo valor de cdCid.
	 */
	public void setCdCid(String cdCid) {
		this.cdCid = cdCid;
	}

	/**
	 * Getter para cdCid2.
	 * 
	 * @return o valor de cdCid2.
	 */
	public String getCdCid2() {
		return cdCid2;
	}

	/**
	 * Setter para cdCid2.
	 * 
	 * @param cdCid2
	 *            o novo valor de cdCid2.
	 */
	public void setCdCid2(String cdCid2) {
		this.cdCid2 = cdCid2;
	}

	/**
	 * Getter para cdCid3.
	 * 
	 * @return o valor de cdCid3.
	 */
	public String getCdCid3() {
		return cdCid3;
	}

	/**
	 * Setter para cdCid3.
	 * 
	 * @param cdCid3
	 *            o novo valor de cdCid3.
	 */
	public void setCdCid3(String cdCid3) {
		this.cdCid3 = cdCid3;
	}

	/**
	 * Getter para cdCid4.
	 * 
	 * @return o valor de cdCid4.
	 */
	public String getCdCid4() {
		return cdCid4;
	}

	/**
	 * Setter para cdCid4.
	 * 
	 * @param cdCid4
	 *            o novo valor de cdCid4.
	 */
	public void setCdCid4(String cdCid4) {
		this.cdCid4 = cdCid4;
	}

	/**
	 * Getter para cdCidObito.
	 * 
	 * @return o valor de cdCidObito.
	 */
	public String getCdCidObito() {
		return cdCidObito;
	}

	/**
	 * Setter para cdCidObito.
	 * 
	 * @param cdCidObito
	 *            o novo valor de cdCidObito.
	 */
	public void setCdCidObito(String cdCidObito) {
		this.cdCidObito = cdCidObito;
	}

	/**
	 * Getter para cdCnes.
	 * 
	 * @return o valor de cdCnes.
	 */
	public String getCdCnes() {
		return cdCnes;
	}

	/**
	 * Setter para cdCnes.
	 * 
	 * @param cdCnes
	 *            o novo valor de cdCnes.
	 */
	public void setCdCnes(String cdCnes) {
		this.cdCnes = cdCnes;
	}

	/**
	 * Getter para cdCnesLocalAtendimento.
	 * 
	 * @return o valor de cdCnesLocalAtendimento.
	 */
	public String getCdCnesLocalAtendimento() {
		return cdCnesLocalAtendimento;
	}

	/**
	 * Setter para cdCnesLocalAtendimento.
	 * 
	 * @param cdCnesLocalAtendimento
	 *            o novo valor de cdCnesLocalAtendimento.
	 */
	public void setCdCnesLocalAtendimento(String cdCnesLocalAtendimento) {
		this.cdCnesLocalAtendimento = cdCnesLocalAtendimento;
	}

	/**
	 * Getter para cdContaMedica.
	 * 
	 * @return o valor de cdContaMedica.
	 */
	public Integer getCdContaMedica() {
		return cdContaMedica;
	}

	/**
	 * Setter para cdContaMedica.
	 * 
	 * @param cdContaMedica
	 *            o novo valor de cdContaMedica.
	 */
	public void setCdContaMedica(Integer cdContaMedica) {
		this.cdContaMedica = cdContaMedica;
	}

	/**
	 * Getter para cdContaMedicaTem.
	 * 
	 * @return o valor de cdContaMedicaTem.
	 */
	public Integer getCdContaMedicaTem() {
		return cdContaMedicaTem;
	}

	/**
	 * Setter para cdContaMedicaTem.
	 * 
	 * @param cdContaMedicaTem
	 *            o novo valor de cdContaMedicaTem.
	 */
	public void setCdContaMedicaTem(Integer cdContaMedicaTem) {
		this.cdContaMedicaTem = cdContaMedicaTem;
	}

	/**
	 * Getter para cdEspecialidade.
	 * 
	 * @return o valor de cdEspecialidade.
	 */
	public Integer getCdEspecialidade() {
		return cdEspecialidade;
	}

	/**
	 * Setter para cdEspecialidade.
	 * 
	 * @param cdEspecialidade
	 *            o novo valor de cdEspecialidade.
	 */
	public void setCdEspecialidade(Integer cdEspecialidade) {
		this.cdEspecialidade = cdEspecialidade;
	}

	/**
	 * Getter para cdIndicadorAcidente.
	 * 
	 * @return o valor de cdIndicadorAcidente.
	 */
	public String getCdIndicadorAcidente() {
		return cdIndicadorAcidente;
	}

	/**
	 * Setter para cdIndicadorAcidente.
	 * 
	 * @param cdIndicadorAcidente
	 *            o novo valor de cdIndicadorAcidente.
	 */
	public void setCdIndicadorAcidente(String cdIndicadorAcidente) {
		this.cdIndicadorAcidente = cdIndicadorAcidente;
	}

	/**
	 * Getter para cdLote.
	 * 
	 * @return o valor de cdLote.
	 */
	public Integer getCdLote() {
		return cdLote;
	}

	/**
	 * Setter para cdLote.
	 * 
	 * @param cdLote
	 *            o novo valor de cdLote.
	 */
	public void setCdLote(Integer cdLote) {
		this.cdLote = cdLote;
	}

	/**
	 * Getter para cdMatricula.
	 * 
	 * @return o valor de cdMatricula.
	 */
	public Integer getCdMatricula() {
		return cdMatricula;
	}

	/**
	 * Setter para cdMatricula.
	 * 
	 * @param cdMatricula
	 *            o novo valor de cdMatricula.
	 */
	public void setCdMatricula(Integer cdMatricula) {
		this.cdMatricula = cdMatricula;
	}

	/**
	 * Getter para cdMotivo.
	 * 
	 * @return o valor de cdMotivo.
	 */
	public Integer getCdMotivo() {
		return cdMotivo;
	}

	/**
	 * Setter para cdMotivo.
	 * 
	 * @param cdMotivo
	 *            o novo valor de cdMotivo.
	 */
	public void setCdMotivo(Integer cdMotivo) {
		this.cdMotivo = cdMotivo;
	}

	/**
	 * Getter para cdMotivoAlta.
	 * 
	 * @return o valor de cdMotivoAlta.
	 */
	public Integer getCdMotivoAlta() {
		return cdMotivoAlta;
	}

	/**
	 * Setter para cdMotivoAlta.
	 * 
	 * @param cdMotivoAlta
	 *            o novo valor de cdMotivoAlta.
	 */
	public void setCdMotivoAlta(Integer cdMotivoAlta) {
		this.cdMotivoAlta = cdMotivoAlta;
	}

	/**
	 * Getter para cdMotivoObitoMulher.
	 * 
	 * @return o valor de cdMotivoObitoMulher.
	 */
	public Integer getCdMotivoObitoMulher() {
		return cdMotivoObitoMulher;
	}

	/**
	 * Setter para cdMotivoObitoMulher.
	 * 
	 * @param cdMotivoObitoMulher
	 *            o novo valor de cdMotivoObitoMulher.
	 */
	public void setCdMotivoObitoMulher(Integer cdMotivoObitoMulher) {
		this.cdMotivoObitoMulher = cdMotivoObitoMulher;
	}

	/**
	 * Getter para cdPlano.
	 * 
	 * @return o valor de cdPlano.
	 */
	public String getCdPlano() {
		return cdPlano;
	}

	/**
	 * Setter para cdPlano.
	 * 
	 * @param cdPlano
	 *            o novo valor de cdPlano.
	 */
	public void setCdPlano(String cdPlano) {
		this.cdPlano = cdPlano;
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
	 * Getter para cdPrestadorEndereco.
	 * 
	 * @return o valor de cdPrestadorEndereco.
	 */
	public Integer getCdPrestadorEndereco() {
		return cdPrestadorEndereco;
	}

	/**
	 * Setter para cdPrestadorEndereco.
	 * 
	 * @param cdPrestadorEndereco
	 *            o novo valor de cdPrestadorEndereco.
	 */
	public void setCdPrestadorEndereco(Integer cdPrestadorEndereco) {
		this.cdPrestadorEndereco = cdPrestadorEndereco;
	}

	/**
	 * Getter para cdPrestadorLocalAtendimento.
	 * 
	 * @return o valor de cdPrestadorLocalAtendimento.
	 */
	public Integer getCdPrestadorLocalAtendimento() {
		return cdPrestadorLocalAtendimento;
	}

	/**
	 * Setter para cdPrestadorLocalAtendimento.
	 * 
	 * @param cdPrestadorLocalAtendimento
	 *            o novo valor de cdPrestadorLocalAtendimento.
	 */
	public void setCdPrestadorLocalAtendimento(Integer cdPrestadorLocalAtendimento) {
		this.cdPrestadorLocalAtendimento = cdPrestadorLocalAtendimento;
	}

	/**
	 * Getter para cdPrestadorSolicitante.
	 * 
	 * @return o valor de cdPrestadorSolicitante.
	 */
	public Integer getCdPrestadorSolicitante() {
		return cdPrestadorSolicitante;
	}

	/**
	 * Setter para cdPrestadorSolicitante.
	 * 
	 * @param cdPrestadorSolicitante
	 *            o novo valor de cdPrestadorSolicitante.
	 */
	public void setCdPrestadorSolicitante(Integer cdPrestadorSolicitante) {
		this.cdPrestadorSolicitante = cdPrestadorSolicitante;
	}

	/**
	 * Getter para cdRegimeInternacao.
	 * 
	 * @return o valor de cdRegimeInternacao.
	 */
	public String getCdRegimeInternacao() {
		return cdRegimeInternacao;
	}

	/**
	 * Setter para cdRegimeInternacao.
	 * 
	 * @param cdRegimeInternacao
	 *            o novo valor de cdRegimeInternacao.
	 */
	public void setCdRegimeInternacao(String cdRegimeInternacao) {
		this.cdRegimeInternacao = cdRegimeInternacao;
	}

	/**
	 * Getter para cdTipAcomodacao.
	 * 
	 * @return o valor de cdTipAcomodacao.
	 */
	public Integer getCdTipAcomodacao() {
		return cdTipAcomodacao;
	}

	/**
	 * Setter para cdTipAcomodacao.
	 * 
	 * @param cdTipAcomodacao
	 *            o novo valor de cdTipAcomodacao.
	 */
	public void setCdTipAcomodacao(Integer cdTipAcomodacao) {
		this.cdTipAcomodacao = cdTipAcomodacao;
	}

	/**
	 * Getter para cdTipoAtendimentoTiss.
	 * 
	 * @return o valor de cdTipoAtendimentoTiss.
	 */
	public String getCdTipoAtendimentoTiss() {
		return cdTipoAtendimentoTiss;
	}

	/**
	 * Setter para cdTipoAtendimentoTiss.
	 * 
	 * @param cdTipoAtendimentoTiss
	 *            o novo valor de cdTipoAtendimentoTiss.
	 */
	public void setCdTipoAtendimentoTiss(String cdTipoAtendimentoTiss) {
		this.cdTipoAtendimentoTiss = cdTipoAtendimentoTiss;
	}

	/**
	 * Getter para cdTipoConsulta.
	 * 
	 * @return o valor de cdTipoConsulta.
	 */
	public String getCdTipoConsulta() {
		return cdTipoConsulta;
	}

	/**
	 * Setter para cdTipoConsulta.
	 * 
	 * @param cdTipoConsulta
	 *            o novo valor de cdTipoConsulta.
	 */
	public void setCdTipoConsulta(String cdTipoConsulta) {
		this.cdTipoConsulta = cdTipoConsulta;
	}

	/**
	 * Getter para cdTipoFaturamento.
	 * 
	 * @return o valor de cdTipoFaturamento.
	 */
	public String getCdTipoFaturamento() {
		return cdTipoFaturamento;
	}

	/**
	 * Setter para cdTipoFaturamento.
	 * 
	 * @param cdTipoFaturamento
	 *            o novo valor de cdTipoFaturamento.
	 */
	public void setCdTipoFaturamento(String cdTipoFaturamento) {
		this.cdTipoFaturamento = cdTipoFaturamento;
	}

	/**
	 * Getter para cdTipoSaidaGuiaConsulta.
	 * 
	 * @return o valor de cdTipoSaidaGuiaConsulta.
	 */
	public Integer getCdTipoSaidaGuiaConsulta() {
		return cdTipoSaidaGuiaConsulta;
	}

	/**
	 * Setter para cdTipoSaidaGuiaConsulta.
	 * 
	 * @param cdTipoSaidaGuiaConsulta
	 *            o novo valor de cdTipoSaidaGuiaConsulta.
	 */
	public void setCdTipoSaidaGuiaConsulta(Integer cdTipoSaidaGuiaConsulta) {
		this.cdTipoSaidaGuiaConsulta = cdTipoSaidaGuiaConsulta;
	}

	/**
	 * Getter para cdTipoSaidaGuiaSadt.
	 * 
	 * @return o valor de cdTipoSaidaGuiaSadt.
	 */
	public Integer getCdTipoSaidaGuiaSadt() {
		return cdTipoSaidaGuiaSadt;
	}

	/**
	 * Setter para cdTipoSaidaGuiaSadt.
	 * 
	 * @param cdTipoSaidaGuiaSadt
	 *            o novo valor de cdTipoSaidaGuiaSadt.
	 */
	public void setCdTipoSaidaGuiaSadt(Integer cdTipoSaidaGuiaSadt) {
		this.cdTipoSaidaGuiaSadt = cdTipoSaidaGuiaSadt;
	}

	/**
	 * Getter para dsObservacao.
	 * 
	 * @return o valor de dsObservacao.
	 */
	public String getDsObservacao() {
		return dsObservacao;
	}

	/**
	 * Setter para dsObservacao.
	 * 
	 * @param dsObservacao
	 *            o novo valor de dsObservacao.
	 */
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	/**
	 * Getter para dtAlta.
	 * 
	 * @return o valor de dtAlta.
	 */
	public Date getDtAlta() {
		return dtAlta;
	}

	/**
	 * Setter para dtAlta.
	 * 
	 * @param dtAlta
	 *            o novo valor de dtAlta.
	 */
	public void setDtAlta(Date dtAlta) {
		this.dtAlta = dtAlta;
	}

	/**
	 * Getter para dtAtendimento.
	 * 
	 * @return o valor de dtAtendimento.
	 */
	public Date getDtAtendimento() {
		return dtAtendimento;
	}

	/**
	 * Setter para dtAtendimento.
	 * 
	 * @param dtAtendimento
	 *            o novo valor de dtAtendimento.
	 */
	public void setDtAtendimento(Date dtAtendimento) {
		this.dtAtendimento = dtAtendimento;
	}

	/**
	 * Getter para dtApresentacao.
	 * 
	 * @return o valor de dtApresentacao.
	 */
	public Date getDtApresentacao() {
		return dtApresentacao;
	}

	/**
	 * Setter para dtApresentacao.
	 * 
	 * @param dtApresentacao
	 *            o novo valor de dtApresentacao.
	 */
	public void setDtApresentacao(Date dtApresentacao) {
		this.dtApresentacao = dtApresentacao;
	}

	/**
	 * Getter para dtAuditoria.
	 * 
	 * @return o valor de dtAuditoria.
	 */
	public Date getDtAuditoria() {
		return dtAuditoria;
	}

	/**
	 * Setter para dtAuditoria.
	 * 
	 * @param dtAuditoria
	 *            o novo valor de dtAuditoria.
	 */
	public void setDtAuditoria(Date dtAuditoria) {
		this.dtAuditoria = dtAuditoria;
	}

	/**
	 * Getter para dtInternacao.
	 * 
	 * @return o valor de dtInternacao.
	 */
	public Date getDtInternacao() {
		return dtInternacao;
	}

	/**
	 * Setter para dtInternacao.
	 * 
	 * @param dtInternacao
	 *            o novo valor de dtInternacao.
	 */
	public void setDtInternacao(Date dtInternacao) {
		this.dtInternacao = dtInternacao;
	}

	/**
	 * Getter para nmPrestador.
	 * 
	 * @return o valor de nmPrestador.
	 */
	public String getNmPrestador() {
		return nmPrestador;
	}

	/**
	 * Setter para nmPrestador.
	 * 
	 * @param nmPrestador
	 *            o novo valor de nmPrestador.
	 */
	public void setNmPrestador(String nmPrestador) {
		this.nmPrestador = nmPrestador;
	}

	/**
	 * Getter para nrCarteiraBeneficiario.
	 * 
	 * @return o valor de nrCarteiraBeneficiario.
	 */
	public BigInteger getNrCarteiraBeneficiario() {
		return nrCarteiraBeneficiario;
	}

	/**
	 * Setter para nrCarteiraBeneficiario.
	 * 
	 * @param nrCarteiraBeneficiario
	 *            o novo valor de nrCarteiraBeneficiario.
	 */
	public void setNrCarteiraBeneficiario(BigInteger nrCarteiraBeneficiario) {
		this.nrCarteiraBeneficiario = nrCarteiraBeneficiario;
	}

	/**
	 * Getter para nrDeclaracaoNascidoVivo.
	 * 
	 * @return o valor de nrDeclaracaoNascidoVivo.
	 */
	public String getNrDeclaracaoNascidoVivo() {
		return nrDeclaracaoNascidoVivo;
	}

	/**
	 * Setter para nrDeclaracaoNascidoVivo.
	 * 
	 * @param nrDeclaracaoNascidoVivo
	 *            o novo valor de nrDeclaracaoNascidoVivo.
	 */
	public void setNrDeclaracaoNascidoVivo(String nrDeclaracaoNascidoVivo) {
		this.nrDeclaracaoNascidoVivo = nrDeclaracaoNascidoVivo;
	}

	/**
	 * Getter para nrDeclaracaoObito.
	 * 
	 * @return o valor de nrDeclaracaoObito.
	 */
	public String getNrDeclaracaoObito() {
		return nrDeclaracaoObito;
	}

	/**
	 * Setter para nrDeclaracaoObito.
	 * 
	 * @param nrDeclaracaoObito
	 *            o novo valor de nrDeclaracaoObito.
	 */
	public void setNrDeclaracaoObito(String nrDeclaracaoObito) {
		this.nrDeclaracaoObito = nrDeclaracaoObito;
	}

	/**
	 * Getter para nrGuia.
	 * 
	 * @return o valor de nrGuia.
	 */
	public Integer getNrGuia() {
		return nrGuia;
	}

	/**
	 * Setter para nrGuia.
	 * 
	 * @param nrGuia
	 *            o novo valor de nrGuia.
	 */
	public void setNrGuia(Integer nrGuia) {
		this.nrGuia = nrGuia;
	}

	/**
	 * Getter para nrGuiaPrestadorPrincipal.
	 * 
	 * @return o valor de nrGuiaPrestadorPrincipal.
	 */
	public String getNrGuiaPrestadorPrincipal() {
		return nrGuiaPrestadorPrincipal;
	}

	/**
	 * Setter para nrGuiaPrestadorPrincipal.
	 * 
	 * @param nrGuiaPrestadorPrincipal
	 *            o novo valor de nrGuiaPrestadorPrincipal.
	 */
	public void setNrGuiaPrestadorPrincipal(String nrGuiaPrestadorPrincipal) {
		this.nrGuiaPrestadorPrincipal = nrGuiaPrestadorPrincipal;
	}

	/**
	 * Getter para qtNascidoMorto.
	 * 
	 * @return o valor de qtNascidoMorto.
	 */
	public Integer getQtNascidoMorto() {
		return qtNascidoMorto;
	}

	/**
	 * Setter para qtNascidoMorto.
	 * 
	 * @param qtNascidoMorto
	 *            o novo valor de qtNascidoMorto.
	 */
	public void setQtNascidoMorto(Integer qtNascidoMorto) {
		this.qtNascidoMorto = qtNascidoMorto;
	}

	/**
	 * Getter para qtNascidoVivoPrematuro.
	 * 
	 * @return o valor de qtNascidoVivoPrematuro.
	 */
	public Integer getQtNascidoVivoPrematuro() {
		return qtNascidoVivoPrematuro;
	}

	/**
	 * Setter para qtNascidoVivoPrematuro.
	 * 
	 * @param qtNascidoVivoPrematuro
	 *            o novo valor de qtNascidoVivoPrematuro.
	 */
	public void setQtNascidoVivoPrematuro(Integer qtNascidoVivoPrematuro) {
		this.qtNascidoVivoPrematuro = qtNascidoVivoPrematuro;
	}

	/**
	 * Getter para qtNascidoVivoTermo.
	 * 
	 * @return o valor de qtNascidoVivoTermo.
	 */
	public Integer getQtNascidoVivoTermo() {
		return qtNascidoVivoTermo;
	}

	/**
	 * Setter para qtNascidoVivoTermo.
	 * 
	 * @param qtNascidoVivoTermo
	 *            o novo valor de qtNascidoVivoTermo.
	 */
	public void setQtNascidoVivoTermo(Integer qtNascidoVivoTermo) {
		this.qtNascidoVivoTermo = qtNascidoVivoTermo;
	}

	/**
	 * Getter para qtObitoNeonatalPrecoce.
	 * 
	 * @return o valor de qtObitoNeonatalPrecoce.
	 */
	public Integer getQtObitoNeonatalPrecoce() {
		return qtObitoNeonatalPrecoce;
	}

	/**
	 * Setter para qtObitoNeonatalPrecoce.
	 * 
	 * @param qtObitoNeonatalPrecoce
	 *            o novo valor de qtObitoNeonatalPrecoce.
	 */
	public void setQtObitoNeonatalPrecoce(Integer qtObitoNeonatalPrecoce) {
		this.qtObitoNeonatalPrecoce = qtObitoNeonatalPrecoce;
	}

	/**
	 * Getter para qtObitoNeonatalTardio.
	 * 
	 * @return o valor de qtObitoNeonatalTardio.
	 */
	public Integer getQtObitoNeonatalTardio() {
		return qtObitoNeonatalTardio;
	}

	/**
	 * Setter para qtObitoNeonatalTardio.
	 * 
	 * @param qtObitoNeonatalTardio
	 *            o novo valor de qtObitoNeonatalTardio.
	 */
	public void setQtObitoNeonatalTardio(Integer qtObitoNeonatalTardio) {
		this.qtObitoNeonatalTardio = qtObitoNeonatalTardio;
	}

	/**
	 * Getter para snAborto.
	 * 
	 * @return o valor de snAborto.
	 */
	public SimNao getSnAborto() {
		return snAborto;
	}

	/**
	 * Setter para snAborto.
	 * 
	 * @param snAborto
	 *            o novo valor de snAborto.
	 */
	public void setSnAborto(SimNao snAborto) {
		this.snAborto = snAborto;
	}

	/**
	 * Getter para snAtendimentoRnSalaParto.
	 * 
	 * @return o valor de snAtendimentoRnSalaParto.
	 */
	public SimNao getSnAtendimentoRnSalaParto() {
		return snAtendimentoRnSalaParto;
	}

	/**
	 * Setter para snAtendimentoRnSalaParto.
	 * 
	 * @param snAtendimentoRnSalaParto
	 *            o novo valor de snAtendimentoRnSalaParto.
	 */
	public void setSnAtendimentoRnSalaParto(SimNao snAtendimentoRnSalaParto) {
		this.snAtendimentoRnSalaParto = snAtendimentoRnSalaParto;
	}

	/**
	 * Getter para snBaixoPeso.
	 * 
	 * @return o valor de snBaixoPeso.
	 */
	public SimNao getSnBaixoPeso() {
		return snBaixoPeso;
	}

	/**
	 * Setter para snBaixoPeso.
	 * 
	 * @param snBaixoPeso
	 *            o novo valor de snBaixoPeso.
	 */
	public void setSnBaixoPeso(SimNao snBaixoPeso) {
		this.snBaixoPeso = snBaixoPeso;
	}

	/**
	 * Getter para snComplicacaoNeonatal.
	 * 
	 * @return o valor de snComplicacaoNeonatal.
	 */
	public SimNao getSnComplicacaoNeonatal() {
		return snComplicacaoNeonatal;
	}

	/**
	 * Setter para snComplicacaoNeonatal.
	 * 
	 * @param snComplicacaoNeonatal
	 *            o novo valor de snComplicacaoNeonatal.
	 */
	public void setSnComplicacaoNeonatal(SimNao snComplicacaoNeonatal) {
		this.snComplicacaoNeonatal = snComplicacaoNeonatal;
	}

	/**
	 * Getter para snComplicacaoPuerperio.
	 * 
	 * @return o valor de snComplicacaoPuerperio.
	 */
	public SimNao getSnComplicacaoPuerperio() {
		return snComplicacaoPuerperio;
	}

	/**
	 * Setter para snComplicacaoPuerperio.
	 * 
	 * @param snComplicacaoPuerperio
	 *            o novo valor de snComplicacaoPuerperio.
	 */
	public void setSnComplicacaoPuerperio(SimNao snComplicacaoPuerperio) {
		this.snComplicacaoPuerperio = snComplicacaoPuerperio;
	}

	/**
	 * Getter para snGestacao.
	 * 
	 * @return o valor de snGestacao.
	 */
	public SimNao getSnGestacao() {
		return snGestacao;
	}

	/**
	 * Setter para snGestacao.
	 * 
	 * @param snGestacao
	 *            o novo valor de snGestacao.
	 */
	public void setSnGestacao(SimNao snGestacao) {
		this.snGestacao = snGestacao;
	}

	/**
	 * Getter para snIndicadorAtendimentoRn.
	 * 
	 * @return o valor de snIndicadorAtendimentoRn.
	 */
	public SimNao getSnIndicadorAtendimentoRn() {
		return snIndicadorAtendimentoRn;
	}

	/**
	 * Setter para snIndicadorAtendimentoRn.
	 * 
	 * @param snIndicadorAtendimentoRn
	 *            o novo valor de snIndicadorAtendimentoRn.
	 */
	public void setSnIndicadorAtendimentoRn(SimNao snIndicadorAtendimentoRn) {
		this.snIndicadorAtendimentoRn = snIndicadorAtendimentoRn;
	}

	/**
	 * Getter para snObitoCausaMaterna.
	 * 
	 * @return o valor de snObitoCausaMaterna.
	 */
	public SimNao getSnObitoCausaMaterna() {
		return snObitoCausaMaterna;
	}

	/**
	 * Setter para snObitoCausaMaterna.
	 * 
	 * @param snObitoCausaMaterna
	 *            o novo valor de snObitoCausaMaterna.
	 */
	public void setSnObitoCausaMaterna(SimNao snObitoCausaMaterna) {
		this.snObitoCausaMaterna = snObitoCausaMaterna;
	}

	/**
	 * Getter para snPartoCesario.
	 * 
	 * @return o valor de snPartoCesario.
	 */
	public SimNao getSnPartoCesario() {
		return snPartoCesario;
	}

	/**
	 * Setter para snPartoCesario.
	 * 
	 * @param snPartoCesario
	 *            o novo valor de snPartoCesario.
	 */
	public void setSnPartoCesario(SimNao snPartoCesario) {
		this.snPartoCesario = snPartoCesario;
	}

	/**
	 * Getter para snPartoNormal.
	 * 
	 * @return o valor de snPartoNormal.
	 */
	public SimNao getSnPartoNormal() {
		return snPartoNormal;
	}

	/**
	 * Setter para snPartoNormal.
	 * 
	 * @param snPartoNormal
	 *            o novo valor de snPartoNormal.
	 */
	public void setSnPartoNormal(SimNao snPartoNormal) {
		this.snPartoNormal = snPartoNormal;
	}

	/**
	 * Getter para snReapresentacao.
	 * 
	 * @return o valor de snReapresentacao.
	 */
	public SimNao getSnReapresentacao() {
		return snReapresentacao;
	}

	/**
	 * Setter para snReapresentacao.
	 * 
	 * @param snReapresentacao
	 *            o novo valor de snReapresentacao.
	 */
	public void setSnReapresentacao(SimNao snReapresentacao) {
		this.snReapresentacao = snReapresentacao;
	}

	/**
	 * Getter para snTranstornoMaternoGravidez.
	 * 
	 * @return o valor de snTranstornoMaternoGravidez.
	 */
	public SimNao getSnTranstornoMaternoGravidez() {
		return snTranstornoMaternoGravidez;
	}

	/**
	 * Setter para snTranstornoMaternoGravidez.
	 * 
	 * @param snTranstornoMaternoGravidez
	 *            o novo valor de snTranstornoMaternoGravidez.
	 */
	public void setSnTranstornoMaternoGravidez(SimNao snTranstornoMaternoGravidez) {
		this.snTranstornoMaternoGravidez = snTranstornoMaternoGravidez;
	}

	/**
	 * Getter para tpCaraterInternacao.
	 * 
	 * @return o valor de tpCaraterInternacao.
	 */
	public String getTpCaraterInternacao() {
		return tpCaraterInternacao;
	}

	/**
	 * Setter para tpCaraterInternacao.
	 * 
	 * @param tpCaraterInternacao
	 *            o novo valor de tpCaraterInternacao.
	 */
	public void setTpCaraterInternacao(String tpCaraterInternacao) {
		this.tpCaraterInternacao = tpCaraterInternacao;
	}

	/**
	 * Getter para tpCaraterSolicitacao.
	 * 
	 * @return o valor de tpCaraterSolicitacao.
	 */
	public String getTpCaraterSolicitacao() {
		return tpCaraterSolicitacao;
	}

	/**
	 * Setter para tpCaraterSolicitacao.
	 * 
	 * @param tpCaraterSolicitacao
	 *            o novo valor de tpCaraterSolicitacao.
	 */
	public void setTpCaraterSolicitacao(String tpCaraterSolicitacao) {
		this.tpCaraterSolicitacao = tpCaraterSolicitacao;
	}

	/**
	 * Getter para tpCausaInternacao.
	 * 
	 * @return o valor de tpCausaInternacao.
	 */
	public String getTpCausaInternacao() {
		return tpCausaInternacao;
	}

	/**
	 * Setter para tpCausaInternacao.
	 * 
	 * @param tpCausaInternacao
	 *            o novo valor de tpCausaInternacao.
	 */
	public void setTpCausaInternacao(String tpCausaInternacao) {
		this.tpCausaInternacao = tpCausaInternacao;
	}

	/**
	 * Getter para tpContaHospitalar.
	 * 
	 * @return o valor de tpContaHospitalar.
	 */
	public TipoContaHospitalar getTpContaHospitalar() {
		return tpContaHospitalar;
	}

	/**
	 * Setter para tpContaHospitalar.
	 * 
	 * @param tpContaHospitalar
	 *            o novo valor de tpContaHospitalar.
	 */
	public void setTpContaHospitalar(TipoContaHospitalar tpContaHospitalar) {
		this.tpContaHospitalar = tpContaHospitalar;
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
	public void setTpGuia(TipoGuia tpGuia) {
		this.tpGuia = tpGuia;
	}

	/**
	 * Getter para tpSituacao.
	 * 
	 * @return o valor de tpSituacao.
	 */
	public String getTpSituacao() {
		return tpSituacao;
	}

	/**
	 * Setter para tpSituacao.
	 * 
	 * @param tpSituacao
	 *            o novo valor de tpSituacao.
	 */
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	/**
	 * Getter para vlFranquia.
	 * 
	 * @return o valor de vlFranquia.
	 */
	public Double getVlFranquia() {
		return vlFranquia;
	}

	/**
	 * Setter para vlFranquia.
	 * 
	 * @param vlFranquia
	 *            o novo valor de vlFranquia.
	 */
	public void setVlFranquia(Double vlFranquia) {
		this.vlFranquia = NumberUtil.roundToScaleTwo(vlFranquia);
	}

	/**
	 * Getter para itensConta.
	 * 
	 * @return o valor de itensConta.
	 */
	public List<ItemContaMedica> getItensConta() {
		return itensConta;
	}

	/**
	 * Setter para itensConta.
	 * 
	 * @param itensConta
	 *            o novo valor de itensConta.
	 */
	public void setItensConta(List<ItemContaMedica> itensConta) {
		this.itensConta = itensConta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContaMedica [" + (atendimento != null ? "atendimento=" + atendimento + ", " : "")
				+ (seqContaMedica != null ? "seqContaMedica=" + seqContaMedica + ", " : "")
				+ (cdCid != null ? "cdCid=" + cdCid + ", " : "") + (cdCid2 != null ? "cdCid2=" + cdCid2 + ", " : "")
				+ (cdCid3 != null ? "cdCid3=" + cdCid3 + ", " : "") + (cdCid4 != null ? "cdCid4=" + cdCid4 + ", " : "")
				+ (cdCidObito != null ? "cdCidObito=" + cdCidObito + ", " : "")
				+ (cdCnes != null ? "cdCnes=" + cdCnes + ", " : "")
				+ (cdCnesLocalAtendimento != null ? "cdCnesLocalAtendimento=" + cdCnesLocalAtendimento + ", " : "")
				+ (cdContaMedica != null ? "cdContaMedica=" + cdContaMedica + ", " : "")
				+ (cdContaMedicaTem != null ? "cdContaMedicaTem=" + cdContaMedicaTem + ", " : "")
				+ (cdEspecialidade != null ? "cdEspecialidade=" + cdEspecialidade + ", " : "")
				+ (cdIndicadorAcidente != null ? "cdIndicadorAcidente=" + cdIndicadorAcidente + ", " : "")
				+ (cdLote != null ? "cdLote=" + cdLote + ", " : "")
				+ (cdMatricula != null ? "cdMatricula=" + cdMatricula + ", " : "")
				+ (cdMotivo != null ? "cdMotivo=" + cdMotivo + ", " : "")
				+ (cdMotivoAlta != null ? "cdMotivoAlta=" + cdMotivoAlta + ", " : "")
				+ (cdMotivoObitoMulher != null ? "cdMotivoObitoMulher=" + cdMotivoObitoMulher + ", " : "")
				+ (cdPlano != null ? "cdPlano=" + cdPlano + ", " : "")
				+ (cdPrestador != null ? "cdPrestador=" + cdPrestador + ", " : "")
				+ (cdPrestadorEndereco != null ? "cdPrestadorEndereco=" + cdPrestadorEndereco + ", " : "")
				+ (cdPrestadorLocalAtendimento != null
						? "cdPrestadorLocalAtendimento=" + cdPrestadorLocalAtendimento + ", " : "")
				+ (cdPrestadorSolicitante != null ? "cdPrestadorSolicitante=" + cdPrestadorSolicitante + ", " : "")
				+ (cdRegimeInternacao != null ? "cdRegimeInternacao=" + cdRegimeInternacao + ", " : "")
				+ (cdTipAcomodacao != null ? "cdTipAcomodacao=" + cdTipAcomodacao + ", " : "")
				+ (cdTipoAtendimentoTiss != null ? "cdTipoAtendimentoTiss=" + cdTipoAtendimentoTiss + ", " : "")
				+ (cdTipoConsulta != null ? "cdTipoConsulta=" + cdTipoConsulta + ", " : "")
				+ (cdTipoFaturamento != null ? "cdTipoFaturamento=" + cdTipoFaturamento + ", " : "")
				+ (cdTipoSaidaGuiaConsulta != null ? "cdTipoSaidaGuiaConsulta=" + cdTipoSaidaGuiaConsulta + ", " : "")
				+ (cdTipoSaidaGuiaSadt != null ? "cdTipoSaidaGuiaSadt=" + cdTipoSaidaGuiaSadt + ", " : "")
				+ (dsObservacao != null ? "dsObservacao=" + dsObservacao + ", " : "")
				+ (dtAlta != null ? "dtAlta=" + dtAlta + ", " : "")
				+ (dtAtendimento != null ? "dtAtendimento=" + dtAtendimento + ", " : "")
				+ (dtAuditoria != null ? "dtAuditoria=" + dtAuditoria + ", " : "")
				+ (dtInternacao != null ? "dtInternacao=" + dtInternacao + ", " : "")
				+ (nmPrestador != null ? "nmPrestador=" + nmPrestador + ", " : "")
				+ (nrCarteiraBeneficiario != null ? "nrCarteiraBeneficiario=" + nrCarteiraBeneficiario + ", " : "")
				+ (nrDeclaracaoNascidoVivo != null ? "nrDeclaracaoNascidoVivo=" + nrDeclaracaoNascidoVivo + ", " : "")
				+ (nrDeclaracaoObito != null ? "nrDeclaracaoObito=" + nrDeclaracaoObito + ", " : "")
				+ (nrGuia != null ? "nrGuia=" + nrGuia + ", " : "")
				+ (nrGuiaPrestadorPrincipal != null ? "nrGuiaPrestadorPrincipal=" + nrGuiaPrestadorPrincipal + ", "
						: "")
				+ (qtNascidoMorto != null ? "qtNascidoMorto=" + qtNascidoMorto + ", " : "")
				+ (qtNascidoVivoPrematuro != null ? "qtNascidoVivoPrematuro=" + qtNascidoVivoPrematuro + ", " : "")
				+ (qtNascidoVivoTermo != null ? "qtNascidoVivoTermo=" + qtNascidoVivoTermo + ", " : "")
				+ (qtObitoNeonatalPrecoce != null ? "qtObitoNeonatalPrecoce=" + qtObitoNeonatalPrecoce + ", " : "")
				+ (qtObitoNeonatalTardio != null ? "qtObitoNeonatalTardio=" + qtObitoNeonatalTardio + ", " : "")
				+ (snAborto != null ? "snAborto=" + snAborto + ", " : "")
				+ (snAtendimentoRnSalaParto != null ? "snAtendimentoRnSalaParto=" + snAtendimentoRnSalaParto + ", "
						: "")
				+ (snBaixoPeso != null ? "snBaixoPeso=" + snBaixoPeso + ", " : "")
				+ (snComplicacaoNeonatal != null ? "snComplicacaoNeonatal=" + snComplicacaoNeonatal + ", " : "")
				+ (snComplicacaoPuerperio != null ? "snComplicacaoPuerperio=" + snComplicacaoPuerperio + ", " : "")
				+ (snGestacao != null ? "snGestacao=" + snGestacao + ", " : "")
				+ (snIndicadorAtendimentoRn != null ? "snIndicadorAtendimentoRn=" + snIndicadorAtendimentoRn + ", "
						: "")
				+ (snObitoCausaMaterna != null ? "snObitoCausaMaterna=" + snObitoCausaMaterna + ", " : "")
				+ (snPartoCesario != null ? "snPartoCesario=" + snPartoCesario + ", " : "")
				+ (snPartoNormal != null ? "snPartoNormal=" + snPartoNormal + ", " : "")
				+ (snReapresentacao != null ? "snReapresentacao=" + snReapresentacao + ", " : "")
				+ (snTranstornoMaternoGravidez != null
						? "snTranstornoMaternoGravidez=" + snTranstornoMaternoGravidez + ", " : "")
				+ (tpCaraterInternacao != null ? "tpCaraterInternacao=" + tpCaraterInternacao + ", " : "")
				+ (tpCaraterSolicitacao != null ? "tpCaraterSolicitacao=" + tpCaraterSolicitacao + ", " : "")
				+ (tpCausaInternacao != null ? "tpCausaInternacao=" + tpCausaInternacao + ", " : "")
				+ (tpContaHospitalar != null ? "tpContaHospitalar=" + tpContaHospitalar + ", " : "")
				+ (tpGuia != null ? "tpGuia=" + tpGuia + ", " : "")
				+ (tpSituacao != null ? "tpSituacao=" + tpSituacao + ", " : "")
				+ (vlFranquia != null ? "vlFranquia=" + vlFranquia : "") + "]";
	}

	@Override
	public int compareTo(ContaMedica other) {
		return Optional.ofNullable(this.getCdContaMedica()).orElse(0)
			   .compareTo(Optional.ofNullable(other.getCdContaMedica()).orElse(0));		
	}

}
