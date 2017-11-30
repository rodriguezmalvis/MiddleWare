package br.org.cac.integrador.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoContaHospitalar {
	CLINICO("N"),
	CIRURGICO("C"),
	AMBULATORIAL("A"),
	OBSTETRICA("O"),
	PEDIATRICA("P"),
	PSIQUIATRICA("S"),
	NAO_INFORMADO("I");
	
	
	private String valor;
	
	private TipoContaHospitalar(String valor){
		this.valor = valor;
	}

	/** Getter para valor.
	 * @return o valor de valor.
	 */
	@JsonValue
	public String getValor() {
		return valor;
	}
	
	@JsonCreator
	public static TipoContaHospitalar fromValue(String value){
		switch(value){
		case "N": return CLINICO;
		case "C": return CIRURGICO;
		case "A": return AMBULATORIAL;
		case "O": return OBSTETRICA;
		case "P": return PEDIATRICA;
		case "S": return PSIQUIATRICA;
		case "I": return NAO_INFORMADO;
		default:
			throw new IllegalArgumentException(value + " não é um valor válido para o Enum TipoContaHospitalar!");
		}
	}
	
	

}
