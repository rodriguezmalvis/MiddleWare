package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ICM_STATUS_PROCESSAMENTO")
public class StatusProcessamento {
	
	// TODO: Mover esses campos para um Enum 
	public static final String ADICIONADO = "AD";
	public static final String EM_PROCESSAMENTO = "EP";
	public static final String PROCESSADO_COM_ERROS = "PE";
	public static final String PROCESSADO_TOTALMENTE = "PT";
	public static final String ENVIADO_SPAG = "ES";
	public static final String REPROCESSADO = "RE";
	public static final String CANCELADO = "CA";
	public static final String PROCESSADO_PARCIALMENTE = "PP";
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_status_processamento")
	private Integer idStatusProcessamento;
	
	@Column(name="cod_status", length=2)
	private String codStatus;
	
	@Column(name="descricao", length=100)
	private String descricao;
	
	@Column(name="f_final", columnDefinition="bit")
	private Boolean statusFinal;
	
	public StatusProcessamento(){
		
	}

	/** Getter para idStatusProcessamento.
	 * @return o valor de idStatusProcessamento.
	 */
	public Integer getIdStatusProcessamento() {
		return idStatusProcessamento;
	}

	/** Setter para idStatusProcessamento.
	 * @param idStatusProcessamento o novo valor de idStatusProcessamento.
	 */
	public void setIdStatusProcessamento(Integer idStatusProcessamento) {
		this.idStatusProcessamento = idStatusProcessamento;
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

	/** Getter para descricao.
	 * @return o valor de descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	/** Setter para descricao.
	 * @param descricao o novo valor de descricao.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/** Getter para statusFinal.
	 * @return o valor de statusFinal.
	 */
	public Boolean getStatusFinal() {
		return statusFinal;
	}

	/** Setter para statusFinal.
	 * @param statusFinal o novo valor de statusFinal.
	 */
	public void setStatusFinal(Boolean statusFinal) {
		this.statusFinal = statusFinal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codStatus == null) ? 0 : codStatus.hashCode());
		result = prime * result + ((idStatusProcessamento == null) ? 0 : idStatusProcessamento.hashCode());
		result = prime * result + ((statusFinal == null) ? 0 : statusFinal.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StatusProcessamento other = (StatusProcessamento) obj;
		if (codStatus == null) {
			if (other.codStatus != null) {
				return false;
			}
		} else if (!codStatus.equals(other.codStatus)) {
			return false;
		}
		if (idStatusProcessamento == null) {
			if (other.idStatusProcessamento != null) {
				return false;
			}
		} else if (!idStatusProcessamento.equals(other.idStatusProcessamento)) {
			return false;
		}
		if (statusFinal == null) {
			if (other.statusFinal != null) {
				return false;
			}
		} else if (!statusFinal.equals(other.statusFinal)) {
			return false;
		}
		return true;
	}
	
	
}
