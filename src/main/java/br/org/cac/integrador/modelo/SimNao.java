/**
 * 
 */
package br.org.cac.integrador.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author JCJ
 *
 */
public enum SimNao {
	SIM("S"), 
	NAO("N");

	
	private String valor;

	private SimNao(String valor) {
		this.valor = valor;
	}

	@JsonValue
	public String getValor() {
		return valor;
	}

	@JsonCreator
	public static SimNao fromValue(String value) {
		switch (value) {
			case "S":
				return SIM;
			case "N":
				return NAO;
			default:
				throw new IllegalArgumentException(value + " não é um valor válido para o Enum SimNao!");
		}
	}
}
