package com.pr7.util;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class GenericValidator {
	private static final Logger _logger = LogManager.getLogger(GenericValidator.class);
	
	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static boolean isDouble(String val) {
		if (StringUtils.isEmpty(val))
			return false;
		
		if (tryParseDouble(val) == null) {			
			return false;
		}
			
		return true;
	}
	
	public static Double tryParseDouble(String val) {
		if (StringUtils.isEmpty(val))
			return null;
		
		String newval = val.replace(",", "").trim();
		
		try {
			return Double.parseDouble(newval);
		} catch (NumberFormatException nfe) {
			_logger.error("'" + newval + "' is NOT double. Msg: " + nfe.getMessage());
			return null;
		}
	}
	
	public static BigDecimal tryParseBigDecimal(String val) {
		if (StringUtils.isEmpty(val))
			return null;
		
		String newval = val.replace(",", "").trim();
		
		try {
			return new BigDecimal(newval);
		} catch (NumberFormatException nfe) {
			_logger.error("'" + newval + "' is NOT BigDecimal. Msg: " + nfe.getMessage());
			return null;
		}
	}
	
	public static BigDecimal tryParseBigDecimal(String val, BigDecimal defaultVal) {
		if (StringUtils.isEmpty(val))
			return defaultVal;
		
		String newval = val.replace(",", "").trim();
		
		try {
			return new BigDecimal(newval);
		} catch (NumberFormatException nfe) {
			_logger.error("'" + newval + "' is NOT BigDecimal. Msg: " + nfe.getMessage());
			return defaultVal;
		}
	}
	
	public static Double defaultIfNull(Double val, Double defaultVal) {
		if (val == null) 
			return defaultVal;
		
		return val;
	}
	
	public static boolean isIntegerValue(BigDecimal bd) {
	  return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}
}
