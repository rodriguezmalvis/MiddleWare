package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.cac.integrador.modelo.AtendimentoId;
import br.org.cac.integrador.modelo.ProcedimentoId;

/**
 * Classe responsável por registrar os ajustes feitos em tempo de envio
 * para o SOUL nos dados presentes em uma dada conta médica.
 * @author JCJ
 * @since 1.6, 2017-07-09
 *
 */
@Entity
@Table(name="MV_CAC_DE_PARA_AJUSTE_SUB_PROCESSO")
public class DeParaAjusteSubProcesso {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_de_para_ajuste_sub_processo")	
	private Integer idDeParaAjusteSubProcesso;
	
	@ManyToOne
	@JoinColumn(name="id_de_para_sub_processo")
	private DeParaSubProcesso deParaSubProcesso;
	
	@Column(name="id_atendimento", columnDefinition="smallint", nullable=true)
	private Integer idAtendimento;
	
	@Column(name="id_procedimento", columnDefinition="tinyInt", nullable=true)
	private Integer idProcedimento;
	
	@Column(name="campo", length=100)
	private String campo;
	
	@Column(name="valor_original", length=100)
	private String valorOriginal;
	
	@Column(name="valor_novo", length=100)
	private String valorNovo;
	
	@Column(name="motivo", length=2)
	private MotivoAjuste motivoAjuste;
	
	/**
	 * Construtor padrão.
	 */
	public DeParaAjusteSubProcesso() {
		// Construtor padrão da classe
	}
	
	/**
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 */
	public DeParaAjusteSubProcesso(String campo, String valorOriginal, String valorNovo, 
			MotivoAjuste motivoAjuste) {
		this.campo = campo;
		this.valorOriginal = valorOriginal;
		this.valorNovo = valorNovo;
		this.motivoAjuste = motivoAjuste;
	}	
	

	/**
	 * @param idAtendimento
	 * @param idProcedimento
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 */
	public DeParaAjusteSubProcesso(Integer idAtendimento, Integer idProcedimento, String campo, String valorOriginal,
			String valorNovo, MotivoAjuste motivoAjuste) {
		this(campo, valorOriginal, valorNovo, motivoAjuste);
		this.idAtendimento = idAtendimento;
		this.idProcedimento = idProcedimento;
	}
	
	/**
	 * 
	 * @param atendimentoId
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 */
	public DeParaAjusteSubProcesso(AtendimentoId atendimentoId, String campo, String valorOriginal,
			String valorNovo, MotivoAjuste motivoAjuste) {
		this(campo, valorOriginal, valorNovo, motivoAjuste);
		
		if (atendimentoId == null) {
			throw new IllegalArgumentException("atendimentoId não pode ser null!");
		}
		
		this.idAtendimento = atendimentoId.getIdAtendimento();
		this.idProcedimento = null;				
	}
	
	/**
	 * 
	 * @param procedimentoId
	 * @param campo
	 * @param valorOriginal
	 * @param valorNovo
	 * @param motivoAjuste
	 */
	public DeParaAjusteSubProcesso(ProcedimentoId procedimentoId, String campo, String valorOriginal,
			String valorNovo, MotivoAjuste motivoAjuste) {
		this(campo, valorOriginal, valorNovo, motivoAjuste);
		
		if (procedimentoId == null) {
			throw new IllegalArgumentException("procedimentoId não pode ser null!");
		}
		
		this.idAtendimento = procedimentoId.getIdAtendimento();
		this.idProcedimento = procedimentoId.getIdProcedimento();	
	}



	/** Getter para idDeParaAjusteSubProcesso.
	 * @return o valor de idDeParaAjusteSubProcesso.
	 */
	public Integer getIdDeParaAjusteSubProcesso() {
		return idDeParaAjusteSubProcesso;
	}

	/** Setter para idDeParaAjusteSubProcesso.
	 * @param idDeParaAjusteSubProcesso o novo valor de idDeParaAjusteSubProcesso.
	 */
	public void setIdDeParaAjusteSubProcesso(Integer idDeParaAjusteSubProcesso) {
		this.idDeParaAjusteSubProcesso = idDeParaAjusteSubProcesso;
	}

	/** Getter para deParaSubProcesso.
	 * @return o valor de deParaSubProcesso.
	 */
	public DeParaSubProcesso getDeParaSubProcesso() {
		return deParaSubProcesso;
	}

	/** Setter para deParaSubProcesso.
	 * @param deParaSubProcesso o novo valor de deParaSubProcesso.
	 */
	public void setDeParaSubProcesso(DeParaSubProcesso deParaSubProcesso) {
		this.deParaSubProcesso = deParaSubProcesso;
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

	/** Getter para campo.
	 * @return o valor de campo.
	 */
	public String getCampo() {
		return campo;
	}

	/** Setter para campo.
	 * @param campo o novo valor de campo.
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/** Getter para valorOriginal.
	 * @return o valor de valorOriginal.
	 */
	public String getValorOriginal() {
		return valorOriginal;
	}

	/** Setter para valorOriginal.
	 * @param valorOriginal o novo valor de valorOriginal.
	 */
	public void setValorOriginal(String valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	/** Getter para valorNovo.
	 * @return o valor de valorNovo.
	 */
	public String getValorNovo() {
		return valorNovo;
	}

	/** Setter para valorNovo.
	 * @param valorNovo o novo valor de valorNovo.
	 */
	public void setValorNovo(String valorNovo) {
		this.valorNovo = valorNovo;
	}

	/** Getter para motivoAjuste.
	 * @return o valor de motivoAjuste.
	 */
	public MotivoAjuste getMotivoAjuste() {
		return motivoAjuste;
	}

	/** Setter para motivoAjuste.
	 * @param motivoAjuste o novo valor de motivoAjuste.
	 */
	public void setMotivoAjuste(MotivoAjuste motivoAjuste) {
		this.motivoAjuste = motivoAjuste;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campo == null) ? 0 : campo.hashCode());
		result = prime * result + ((idAtendimento == null) ? 0 : idAtendimento.hashCode());
		result = prime * result + ((idDeParaAjusteSubProcesso == null) ? 0 : idDeParaAjusteSubProcesso.hashCode());
		result = prime * result + ((idProcedimento == null) ? 0 : idProcedimento.hashCode());
		result = prime * result + ((motivoAjuste == null) ? 0 : motivoAjuste.hashCode());
		result = prime * result + ((valorNovo == null) ? 0 : valorNovo.hashCode());
		result = prime * result + ((valorOriginal == null) ? 0 : valorOriginal.hashCode());
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
		DeParaAjusteSubProcesso other = (DeParaAjusteSubProcesso) obj;
		if (campo == null) {
			if (other.campo != null) {
				return false;
			}
		} else if (!campo.equals(other.campo)) {
			return false;
		}
		if (idAtendimento == null) {
			if (other.idAtendimento != null) {
				return false;
			}
		} else if (!idAtendimento.equals(other.idAtendimento)) {
			return false;
		}
		if (idDeParaAjusteSubProcesso == null) {
			if (other.idDeParaAjusteSubProcesso != null) {
				return false;
			}
		} else if (!idDeParaAjusteSubProcesso.equals(other.idDeParaAjusteSubProcesso)) {
			return false;
		}
		if (idProcedimento == null) {
			if (other.idProcedimento != null) {
				return false;
			}
		} else if (!idProcedimento.equals(other.idProcedimento)) {
			return false;
		}
		if (motivoAjuste != other.motivoAjuste) {
			return false;
		}
		if (valorNovo == null) {
			if (other.valorNovo != null) {
				return false;
			}
		} else if (!valorNovo.equals(other.valorNovo)) {
			return false;
		}
		if (valorOriginal == null) {
			if (other.valorOriginal != null) {
				return false;
			}
		} else if (!valorOriginal.equals(other.valorOriginal)) {
			return false;
		}
		return true;
	}
	
	
	
}
