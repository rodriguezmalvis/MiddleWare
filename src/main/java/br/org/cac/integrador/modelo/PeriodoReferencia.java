package br.org.cac.integrador.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>Representa um período de datas qualquer, que está associado a uma data de referência.</p>
 * 
 * <p>O JSON correspondente a esse objeto possui a seguinte estrutura:
 * <pre>{
 * "dtReferencia" : &lt;representação String de uma data&gt;,
 * "dtInicial" : &lt;representação String de uma data&gt;,
 * "dtFinal" : &lt;representação String de uma data&gt;
 *}
 * </pre>
 * </p>
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-03-28
 */
public class PeriodoReferencia {
	private Date dtReferencia;
	
	/* Como há os métodos delegados para Período, a serialização/desserialização para JSON
	 * utiliza estes métodos. Portanto, pode-se ignorar o objeto período na ser/desser para
	 * que a estrutura do JSON gerado seja plana.
	 */
	@JsonIgnore
	private Periodo periodo;
	
	/**
	 * Construtor padrão da classe. Esse construtor inicializa o {@code Periodo} associado a
	 * este {@code PeriodoReferencia}, mas não inicializa nenhuma das datas associadas a ele.
	 * 
	 * @see Periodo
	 */
	public PeriodoReferencia(){
		this.periodo = new Periodo();
	}

	/** Getter para dtReferencia.
	 * @return o valor de dtReferencia.
	 */
	public Date getDtReferencia() {
		return dtReferencia;
	}

	/** Setter para dtReferencia.
	 * @param dtReferencia o novo valor de dtReferencia.
	 */
	public void setDtReferencia(Date dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	/** Getter para periodo.
	 * @return o valor de periodo.
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/** Setter para periodo.
	 * @param periodo o novo valor de periodo.
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/** Método de conveniência para {@code Periodo#getDtInicial()}.
	 * @return o valor de dtInicial do {@code Periodo} deste {@code PeriodoReferencia}.
	 * @see br.org.cac.integrador.modelo.Periodo#getDtInicial()
	 */
	public Date getDtInicial() {
		return periodo.getDtInicial();
	}

	/** Método de conveniência para {@code Periodo#setDtInicial(Date)}.
	 * @param dtInicial o novo valor de dtInicial para o {@code Periodo} deste {@code PeriodoReferencia}.
	 * @see br.org.cac.integrador.modelo.Periodo#setDtInicial(java.util.Date)
	 */
	public void setDtInicial(Date dtInicial) {
		periodo.setDtInicial(dtInicial);
	}

	/** Método de conveniência para {@code Periodo#getDtFinal()}.
	 * @return o valor de dtFinal do {@code Periodo} deste {@code PeriodoReferencia}.
	 * @see br.org.cac.integrador.modelo.Periodo#getDtFinal()
	 */
	public Date getDtFinal() {
		return periodo.getDtFinal();
	}

	/** Método de conveniência para {@code Periodo#setDtFinal(Date)}.
	 * @param dtFinal o novo valor de dtFinal para o {@code Periodo} deste {@code PeriodoReferencia}.
	 * @see br.org.cac.integrador.modelo.Periodo#setDtFinal(java.util.Date)
	 */
	public void setDtFinal(Date dtFinal) {
		periodo.setDtFinal(dtFinal);
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		return String.format("Período: Data de Referência: %s; %s",
					this.dtReferencia == null ? "não preenchida" : sdf.format(this.dtReferencia),
					this.periodo.toString()		
				); 
	}
	
}
