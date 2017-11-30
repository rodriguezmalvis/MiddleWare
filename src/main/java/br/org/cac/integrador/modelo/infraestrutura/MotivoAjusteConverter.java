package br.org.cac.integrador.modelo.infraestrutura;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter a ser usado para persistência de campos {@link MotivoAjuste}.
 * @author JCJ
 * @since 1.6, 2017-07-09
 */
@Converter(autoApply = true)
public class MotivoAjusteConverter implements AttributeConverter<MotivoAjuste, String>{

	@Override
	public String convertToDatabaseColumn(MotivoAjuste attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public MotivoAjuste convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		return MotivoAjuste.fromValue(dbData);
	}

}
