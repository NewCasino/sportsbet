package com.pr7.common.web.util;

import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;

public class XSSValidator {
	protected static Logger logger = LogManager.getLogger(XSSValidator.class);

	private static Pattern[] patterns = new Pattern[] {
//            //Any tag will be rejected
//			Pattern.compile("[<>]+", Pattern.CASE_INSENSITIVE),
                        
//			// Script fragments
//			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			
			// src='...'
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			// lonely script tags
//			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
//			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
//					| Pattern.MULTILINE | Pattern.DOTALL),
			// eval(...)
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// expression(...)
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// javascript:...
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			// vbscript:...
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			//alert
			Pattern.compile("alert\\(", Pattern.CASE_INSENSITIVE),
			// onload(...)=...
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL) };

	public static boolean isValid(String value) {
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and
			// uncomment the following line to
			// avoid encoded attacks.
			value = ESAPI.encoder().canonicalize(value);
			
			// Avoid null characters
			value = value.replaceAll("\0", "");

			if (value.contains("<") || value.contains(">")) {
				logger.error("INVALID(0) request " + value);
				return false;
			}
			
			// Remove all sections that match a pattern
			for (Pattern scriptPattern : patterns) {
				if (scriptPattern.matcher(value).find()) {
					logger.error("INVALID(1) request " + value);
					return false;
				}
			}
		}

		return true;
	}
}
