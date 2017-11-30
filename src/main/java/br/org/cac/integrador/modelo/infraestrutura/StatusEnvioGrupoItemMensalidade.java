package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author JCJ
 * @since 2017-07-15
 */
@Entity
@Table(name="MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS")
public class StatusEnvioGrupoItemMensalidade {
	
	// TODO: Mover esses campos para um Enum
	public static final String ENVIADO_TOTALMENTE = "ET";
	public static final String ENVIADO_PARCIALMENTE = "EP";
	public static final String A_CANCELAR = "AC";
	public static final String CANCELADO = "CA";
	
	@Id
	@Column(name="id_status_envio_grupo_item_mens")
	private Integer idStatusEnvioGrupoItemMens;
	
	@Column(name="cod_status", length=2)
	private String codStatus;
	
	@Column(name="f_final", columnDefinition="bit")
	private Boolean fFinal;
	
	@Column(name="f_gera_inconsistencia", columnDefinition="bit")
	private Boolean fGeraInconsistencia;
	
	public StatusEnvioGrupoItemMensalidade() {
		// TODO Construtor padrão
	}

	/** Getter para idStatusEnvioGrupoItemMens.
	 * @return o valor de idStatusEnvioGrupoItemMens.
	 */
	public Integer getIdStatusEnvioGrupoItemMens() {
		return idStatusEnvioGrupoItemMens;
	}

	/** Setter para idStatusEnvioGrupoItemMens.
	 * @param idStatusEnvioGrupoItemMens o novo valor de idStatusEnvioGrupoItemMens.
	 */
	public void setIdStatusEnvioGrupoItemMens(Integer idStatusEnvioGrupoItemMens) {
		this.idStatusEnvioGrupoItemMens = idStatusEnvioGrupoItemMens;
	}

	/** Getter para codStatus.
	 * @return o valor de codStatus.
	 */
	public String getCodStatus() {
		return codStatus;
	}

	/** Setter para codStatus.
	 * @param codStatus o novo valor de codStatus.
	 */
	public void setCodStatus(String codStatus) {
		this.codStatus = codStatus;
	}

	/** Getter para fFinal.
	 * @return o valor de fFinal.
	 */
	public Boolean getfFinal() {
		return fFinal;
	}

	/** Setter para fFinal.
	 * @param fFinal o novo valor de fFinal.
	 */
	public void setfFinal(Boolean fFinal) {
		this.fFinal = fFinal;
	}

	/** Getter para fGeraInconsistencia.
	 * @return o valor de fGeraInconsistencia.
	 */
	public Boolean getfGeraInconsistencia() {
		return fGeraInconsistencia;
	}

	/** Setter para fGeraInconsistencia.
	 * @param fGeraInconsistencia o novo valor de fGeraInconsistencia.
	 */
	public void setfGeraInconsistencia(Boolean fGeraInconsistencia) {
		this.fGeraInconsistencia = fGeraInconsistencia;
	}
	
}
