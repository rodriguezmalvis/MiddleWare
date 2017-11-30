package br.org.cac.integrador.modelo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import br.org.cac.integrador.services.IntegradorContasMedicas;

/**
 * Classe que contém o identificador da tabela {@code SUB_PROCESSO}, a ser usado como base para o JSON
 * de integração de Contas Médicas.
 * 
 * @author JCJ
 * @version 1.0
 * @since 2017-03-09
 * @see IntegradorContasMedicas
 */
@Embeddable
public class SubProcessoId implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3053575150850740369L;

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
	
	public SubProcessoId(){
		
	}
	
	public SubProcessoId(Integer anoApresentacao, Integer idRepresentacao, Integer idProcesso,
			String dSubProcesso, String dNatureza, Integer idSequencialNatureza){
		this.anoApresentacao = anoApresentacao;
		this.idRepresentacao = idRepresentacao;
		this.idProcesso = idProcesso;
		this.dSubProcesso = dSubProcesso;
		this.dNatureza = dNatureza;
		this.idSequencialNatureza = idSequencialNatureza;
	}

	public Integer getAnoApresentacao() {
		return anoApresentacao;
	}

	public void setAnoApresentacao(Integer anoApresentacao) {
		this.anoApresentacao = anoApresentacao;
	}

	public Integer getIdRepresentacao() {
		return idRepresentacao;
	}

	public void setIdRepresentacao(Integer idRepresentacao) {
		this.idRepresentacao = idRepresentacao;
	}

	public Integer getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(Integer idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getdSubProcesso() {
		return dSubProcesso;
	}

	public void setdSubProcesso(String dSubProcesso) {
		this.dSubProcesso = dSubProcesso;
	}

	public String getdNatureza() {
		return dNatureza;
	}

	public void setdNatureza(String dNatureza) {
		this.dNatureza = dNatureza;
	}

	public Integer getIdSequencialNatureza() {
		return idSequencialNatureza;
	}

	public void setIdSequencialNatureza(Integer idSequencialNatureza) {
		this.idSequencialNatureza = idSequencialNatureza;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubProcessoId other = (SubProcessoId) obj;
		if (anoApresentacao == null) {
			if (other.anoApresentacao != null)
				return false;
		} else if (!anoApresentacao.equals(other.anoApresentacao))
			return false;
		if (dNatureza == null) {
			if (other.dNatureza != null)
				return false;
		} else if (!dNatureza.equals(other.dNatureza))
			return false;
		if (dSubProcesso == null) {
			if (other.dSubProcesso != null)
				return false;
		} else if (!dSubProcesso.equals(other.dSubProcesso))
			return false;
		if (idProcesso == null) {
			if (other.idProcesso != null)
				return false;
		} else if (!idProcesso.equals(other.idProcesso))
			return false;
		if (idRepresentacao == null) {
			if (other.idRepresentacao != null)
				return false;
		} else if (!idRepresentacao.equals(other.idRepresentacao))
			return false;
		if (idSequencialNatureza == null) {
			if (other.idSequencialNatureza != null)
				return false;
		} else if (!idSequencialNatureza.equals(other.idSequencialNatureza))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + anoApresentacao + "-" + idRepresentacao
				+ "-" + idProcesso + "-" + dSubProcesso + "-" + dNatureza
				+ "-" + idSequencialNatureza;
	}
	
	/**
	 * Gera um novo SubProcessoId baseado nos dados de um {@link AtendimentoId}
	 * (particularmente, nos campos que compõem um SubProcessoId e são comuns ao 
	 * AtendimentoId).
	 * @param atendimentoId O AtendimentoId de base para a criação do
	 * SubProcessoId.
	 * @return Um novo SubProcessoId preenchido com os campos comuns com o 
	 * AtendimentoId
	 * @throws IllegalArgumentException Se {@code atendimentoId} for {@code null}.
	 */
	public static SubProcessoId fromAtendimentoId(AtendimentoId atendimentoId){
		if (atendimentoId == null){
			throw new IllegalArgumentException("atendimentoId não pode ser null");
		}
		
		return new SubProcessoId(
				atendimentoId.getAnoApresentacao(),
				atendimentoId.getIdRepresentacao(),
				atendimentoId.getIdProcesso(),
				atendimentoId.getdSubProcesso(),
				atendimentoId.getdNatureza(),
				atendimentoId.getIdSequencialNatureza()
				);
	}
	
	
	
	
}
