package br.org.cac.integrador.modelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TipoRubricaCacConverter implements AttributeConverter<TipoRubricaCac, String> {

	@Override
	public String convertToDatabaseColumn(TipoRubricaCac attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public TipoRubricaCac convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		return TipoRubricaCac.fromValue(dbData);
	}
}
