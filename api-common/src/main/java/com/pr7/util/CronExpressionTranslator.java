package com.pr7.util;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

public class CronExpressionTranslator {
	/**
	 * Only support format every second, minute and hour
	 * @param cron
	 * @return
	 */
	public static Entry<TimeUnit, Integer> translate(String cron) {
		if (cron == null)
			return null;
		
		Entry<TimeUnit, Integer> result = null;
		String[] array = cron.split(" ");
		for(int i=0; i < 3; i++) {
			if (array[i].contains("/")) {
				Integer quantity = Integer.parseInt(StringUtils.substringAfter(array[i], "/").trim());
				switch(i) {
					case 0:
						result = new AbstractMap.SimpleEntry<TimeUnit, Integer>(TimeUnit.SECONDS, quantity);
						break;
					case 1:
						result = new AbstractMap.SimpleEntry<TimeUnit, Integer>(TimeUnit.MINUTES, quantity);
						break;
					case 2:
						result = new AbstractMap.SimpleEntry<TimeUnit, Integer>(TimeUnit.HOURS, quantity);
						break;
				}
			}			
		}
		return result;
	}
}
