package br.org.cac.integrador.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FormaPagamento {
	TESOURARIA("T"),
	FOLHA("F"),
	CARNE_INDIVIDUAL("I"),
	CARNE_UNIFICADO("C");
	
	private String valor;
	
	private FormaPagamento(String valor){
		this.valor = valor;
	}
	
	@JsonValue
	public String getValor() {
		return valor;
	}	
	
	@JsonCreator
	public static FormaPagamento fromValue(String value) {
		switch (value) {
			case "T":
				return TESOURARIA;
			case "F":
				return FOLHA;
			case "I":
				return CARNE_INDIVIDUAL;
			case "C":
				return CARNE_UNIFICADO;
			default:
				throw new IllegalArgumentException(value + " não é um valor válido para o Enum FormaPagamento!");
		}
	}
}
