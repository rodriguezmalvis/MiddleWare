package br.org.cac.integrador.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoGuia {
	CONSULTA("C", "1"),
	ODONTOLOGICO("D", null),
	HONORARIO_INDIVIDUAL("H", "4"),
	INTERNACAO("I", "3"),
	OPME("P", null),
	QUIMIOTERAPIA("Q", null),
	RADIOTERAPIA("R", null),
	SP_SADT("S", "2");
	
	private String valor;
	
	private String tpAtendimentoMv;
	
	private TipoGuia(String valor, String tpAtendimentoMv){
		this.valor = valor;
		this.tpAtendimentoMv = tpAtendimentoMv;
	}

	@JsonValue
	public String getValor() {
		return valor;
	}
	
	public String getTpAtendimentoMv(){
		return this.tpAtendimentoMv;
	}
	
	@JsonCreator
	public static TipoGuia fromValue(String value) {
		switch (value) {
			case "C":
				return CONSULTA;
			case "D":
				return ODONTOLOGICO;
			case "H":
				return HONORARIO_INDIVIDUAL;
			case "I":
				return INTERNACAO;
			case "P":
				return OPME;
			case "Q":
				return QUIMIOTERAPIA;
			case "R":
				return RADIOTERAPIA;
			case "S":
				return SP_SADT;
			default:
				throw new IllegalArgumentException(value + " não é um valor válido para o Enum TipoGuia!");
		}
	}	
}
