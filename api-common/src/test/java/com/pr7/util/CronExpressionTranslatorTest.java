package com.pr7.util;

import java.text.ParseException;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class CronExpressionTranslatorTest {
	@Test
	public void testTranslate() throws ParseException {		
		Entry<TimeUnit, Integer> result = CronExpressionTranslator.translate(null);
		Assert.assertNull(result);
		
		result = CronExpressionTranslator.translate("0 0 7 * * ?");
		Assert.assertNull(result);
		
		result = CronExpressionTranslator.translate("*/4 0 7 * * ?");
		Assert.assertEquals(TimeUnit.SECONDS, result.getKey());
		Assert.assertEquals(new Integer(4), result.getValue());
		
		result = CronExpressionTranslator.translate("4 */6 7 * * ?");
		Assert.assertEquals(TimeUnit.MINUTES, result.getKey());
		Assert.assertEquals(new Integer(6), result.getValue());
		
		result = CronExpressionTranslator.translate("4 6 */7 * * ?");
		Assert.assertEquals(TimeUnit.HOURS, result.getKey());
		Assert.assertEquals(new Integer(7), result.getValue());
	}	
}
