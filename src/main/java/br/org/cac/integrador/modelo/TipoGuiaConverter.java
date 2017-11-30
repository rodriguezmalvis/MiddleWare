package br.org.cac.integrador.modelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TipoGuiaConverter implements AttributeConverter<TipoGuia, String> {

	@Override
	public String convertToDatabaseColumn(TipoGuia attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public TipoGuia convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		return TipoGuia.fromValue(dbData);
	}

}
