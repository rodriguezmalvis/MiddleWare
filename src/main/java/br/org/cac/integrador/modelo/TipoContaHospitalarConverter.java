package br.org.cac.integrador.modelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TipoContaHospitalarConverter implements AttributeConverter<TipoContaHospitalar, String>{

	@Override
	public String convertToDatabaseColumn(TipoContaHospitalar attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public TipoContaHospitalar convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		return TipoContaHospitalar.fromValue(dbData);
	}
	

}
