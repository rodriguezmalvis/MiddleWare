package br.org.cac.integrador.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Representa um par de datas, que correspondem a um período qualquer.</p>
 * <p><strong>NOTA:</strong> No momento, não há validação das datas, ou seja, pode
 * acontecer de a data inicial ser maior que a data final.</p>
 *   
 * @author JCJ
 * @version 1.0
 * @since 1.0, 2017-03-27
 *
 */
public class Periodo {
	private Date dtInicial;
	
	private Date dtFinal;
	
	public Periodo(){
		
	}
	
	public Periodo(Date dtInicial, Date dtFinal) {
		this.dtInicial = dtInicial;
		this.dtFinal = dtFinal;
	}

	/** Getter para dtInicial.
	 * @return o valor de dtInicial.
	 */
	public Date getDtInicial() {
		return dtInicial;
	}

	/** Setter para dtInicial.
	 * @param dtInicial o novo valor de dtInicial.
	 */
	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	/** Getter para dtFinal.
	 * @return o valor de dtFinal.
	 */
	public Date getDtFinal() {
		return dtFinal;
	}

	/** Setter para dtFinal.
	 * @param dtFinal o novo valor de dtFinal.
	 */
	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		return "Data inicial: " + (this.dtInicial == null ? "não preenchida" : sdf.format(this.dtInicial)) 
				+ "; Data final: " + (this.dtFinal == null ? "não preenchida" : sdf.format(this.dtFinal));
	}
	
}
