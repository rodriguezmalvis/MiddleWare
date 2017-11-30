package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ICM_TIPO_PROCESSAMENTO")
public class TipoProcessamento {
	
	// TODO: Mover essas constantes para um Enum
	public static final String CONTA_INDIVIDUAL = "CI";
	public static final String LOTE_CONTAS = "LC";
	public static final String LOTE_TITULOS = "LT";
	public static final String LOTE_REEMBOLSOS = "LR";
	public static final String REEMBOLSO_INDIVIDUAL = "RI";
	public static final String LOTE_TITULOS_FOLHA = "LF";
	public static final String LOTE_TITULOS_BOLETO = "LB";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_processamento", updatable=false, insertable=false)
	private Integer idTipoProcessamento;
	
	@Column(name="cod_processamento", length=2, updatable=false, insertable=false)
	private String codProcessamento;
	
	@Column(name="descricao", length=100, updatable=false, insertable=false)
	private String descricao;
	
	public TipoProcessamento(){
		
	}

	/** Getter para idTipoProcessamento.
	 * @return o valor de idTipoProcessamento.
	 */
	public Integer getIdTipoProcessamento() {
		return idTipoProcessamento;
	}

	/** Setter para idTipoProcessamento.
	 * @param idTipoProcessamento o novo valor de idTipoProcessamento.
	 */
	public void setIdTipoProcessamento(Integer idTipoProcessamento) {
		this.idTipoProcessamento = idTipoProcessamento;
	}

	/** Getter para codProcessamento.
	 * @return o valor de codProcessamento.
	 */
	public String getCodProcessamento() {
		return codProcessamento;
	}

	/** Setter para codProcessamento.
	 * @param codProcessamento o novo valor de codProcessamento.
	 */
	public void setCodProcessamento(String codProcessamento) {
		this.codProcessamento = codProcessamento;
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
	
	
}
