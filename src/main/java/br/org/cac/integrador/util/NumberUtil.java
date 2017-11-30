package br.org.cac.integrador.util;

import java.math.BigDecimal;

public class NumberUtil {
	
	public static Double roundToScaleTwo(Double aDouble){
		return NumberUtil.roundTo(aDouble, 2);
	}

	
	public static Double roundTo(Double aDouble, int scale){
		if (aDouble == null){
			return null;
		}
		
		BigDecimal value = BigDecimal.valueOf(aDouble).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		
		return value.doubleValue();
	}
}
