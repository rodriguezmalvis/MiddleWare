package br.org.cac.integrador.modelo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProcedimentoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3054368067743945619L;
	
	@Column(name="ano_apresentacao")
	private Integer anoApresentacao;
	
	@Column(name="id_representacao")
	private Integer idRepresentacao;
	
	@Column(name="id_processo")
	private Integer idProcesso;
	
	@Column(name="d_sub_processo", length=1)
	private String dSubProcesso;
	
	@Column(name="d_natureza")
	private String dNatureza;
	
	@Column(name="id_sequencial_natureza")
	private Integer idSequencialNatureza;	
	
	@Column(name="id_atendimento")
	private Integer idAtendimento;
	
	@Column(name="id_procedimento")
	private Integer idProcedimento;
	
	public ProcedimentoId(){
		
	}

	/** Getter para anoApresentacao.
	 * @return o valor de anoApresentacao.
	 */
	public Integer getAnoApresentacao() {
		return anoApresentacao;
	}

	/** Setter para anoApresentacao.
	 * @param anoApresentacao o novo valor de anoApresentacao.
	 */
	public void setAnoApresentacao(Integer anoApresentacao) {
		this.anoApresentacao = anoApresentacao;
	}

	/** Getter para idRepresentacao.
	 * @return o valor de idRepresentacao.
	 */
	public Integer getIdRepresentacao() {
		return idRepresentacao;
	}

	/** Setter para idRepresentacao.
	 * @param idRepresentacao o novo valor de idRepresentacao.
	 */
	public void setIdRepresentacao(Integer idRepresentacao) {
		this.idRepresentacao = idRepresentacao;
	}

	/** Getter para idProcesso.
	 * @return o valor de idProcesso.
	 */
	public Integer getIdProcesso() {
		return idProcesso;
	}

	/** Setter para idProcesso.
	 * @param idProcesso o novo valor de idProcesso.
	 */
	public void setIdProcesso(Integer idProcesso) {
		this.idProcesso = idProcesso;
	}

	/** Getter para dSubProcesso.
	 * @return o valor de dSubProcesso.
	 */
	public String getdSubProcesso() {
		return dSubProcesso;
	}

	/** Setter para dSubProcesso.
	 * @param dSubProcesso o novo valor de dSubProcesso.
	 */
	public void setdSubProcesso(String dSubProcesso) {
		this.dSubProcesso = dSubProcesso;
	}

	/** Getter para dNatureza.
	 * @return o valor de dNatureza.
	 */
	public String getdNatureza() {
		return dNatureza;
	}

	/** Setter para dNatureza.
	 * @param dNatureza o novo valor de dNatureza.
	 */
	public void setdNatureza(String dNatureza) {
		this.dNatureza = dNatureza;
	}

	/** Getter para idSequencialNatureza.
	 * @return o valor de idSequencialNatureza.
	 */
	public Integer getIdSequencialNatureza() {
		return idSequencialNatureza;
	}

	/** Setter para idSequencialNatureza.
	 * @param idSequencialNatureza o novo valor de idSequencialNatureza.
	 */
	public void setIdSequencialNatureza(Integer idSequencialNatureza) {
		this.idSequencialNatureza = idSequencialNatureza;
	}

	/** Getter para idAtendimento.
	 * @return o valor de idAtendimento.
	 */
	public Integer getIdAtendimento() {
		return idAtendimento;
	}

	/** Setter para idAtendimento.
	 * @param idAtendimento o novo valor de idAtendimento.
	 */
	public void setIdAtendimento(Integer idAtendimento) {
		this.idAtendimento = idAtendimento;
	}

	/** Getter para idProcedimento.
	 * @return o valor de idProcedimento.
	 */
	public Integer getIdProcedimento() {
		return idProcedimento;
	}

	/** Setter para idProcedimento.
	 * @param idProcedimento o novo valor de idProcedimento.
	 */
	public void setIdProcedimento(Integer idProcedimento) {
		this.idProcedimento = idProcedimento;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anoApresentacao == null) ? 0 : anoApresentacao.hashCode());
		result = prime * result + ((dNatureza == null) ? 0 : dNatureza.hashCode());
		result = prime * result + ((dSubProcesso == null) ? 0 : dSubProcesso.hashCode());
		result = prime * result + ((idAtendimento == null) ? 0 : idAtendimento.hashCode());
		result = prime * result + ((idProcedimento == null) ? 0 : idProcedimento.hashCode());
		result = prime * result + ((idProcesso == null) ? 0 : idProcesso.hashCode());
		result = prime * result + ((idRepresentacao == null) ? 0 : idRepresentacao.hashCode());
		result = prime * result + ((idSequencialNatureza == null) ? 0 : idSequencialNatureza.hashCode());
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
		ProcedimentoId other = (ProcedimentoId) obj;
		if (anoApresentacao == null) {
			if (other.anoApresentacao != null) {
				return false;
			}
		} else if (!anoApresentacao.equals(other.anoApresentacao)) {
			return false;
		}
		if (dNatureza == null) {
			if (other.dNatureza != null) {
				return false;
			}
		} else if (!dNatureza.equals(other.dNatureza)) {
			return false;
		}
		if (dSubProcesso == null) {
			if (other.dSubProcesso != null) {
				return false;
			}
		} else if (!dSubProcesso.equals(other.dSubProcesso)) {
			return false;
		}
		if (idAtendimento == null) {
			if (other.idAtendimento != null) {
				return false;
			}
		} else if (!idAtendimento.equals(other.idAtendimento)) {
			return false;
		}
		if (idProcedimento == null) {
			if (other.idProcedimento != null) {
				return false;
			}
		} else if (!idProcedimento.equals(other.idProcedimento)) {
			return false;
		}
		if (idProcesso == null) {
			if (other.idProcesso != null) {
				return false;
			}
		} else if (!idProcesso.equals(other.idProcesso)) {
			return false;
		}
		if (idRepresentacao == null) {
			if (other.idRepresentacao != null) {
				return false;
			}
		} else if (!idRepresentacao.equals(other.idRepresentacao)) {
			return false;
		}
		if (idSequencialNatureza == null) {
			if (other.idSequencialNatureza != null) {
				return false;
			}
		} else if (!idSequencialNatureza.equals(other.idSequencialNatureza)) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + anoApresentacao + "-" + idRepresentacao
				+ "-" + idProcesso + "-" + dSubProcesso + "-" + dNatureza
				+ "-" + idSequencialNatureza + "-" + idAtendimento + "-"
				+ idProcedimento;
	}
	

	
}
