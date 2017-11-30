package br.org.cac.integrador.modelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SimNaoConverter implements AttributeConverter<SimNao, String> {

	@Override
	public String convertToDatabaseColumn(SimNao attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public SimNao convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		return SimNao.fromValue(dbData);
	}

}
