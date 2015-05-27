package com.pr7.util;

import java.math.BigDecimal;
import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

public class NumberHelperTest {
	
	@Test
	public void testBaseConver() throws ParseException {
		Assert.assertEquals("z", Integer.toString(35, 36));
		Assert.assertEquals("0", Integer.toString(0, 36));
		Assert.assertEquals("zzz", Integer.toString(46655, 36));
	}
	
	@Test
	public void testIsIntegerValue() throws ParseException {
		Assert.assertEquals(true, GenericValidator.isIntegerValue(new BigDecimal("9.00")));
		Assert.assertEquals(true, GenericValidator.isIntegerValue(new BigDecimal("9")));
		Assert.assertEquals(false, GenericValidator.isIntegerValue(new BigDecimal("9.12")));
		System.out.println(new BigDecimal("-9.52").remainder(BigDecimal.ONE));
		System.out.println(new BigDecimal("-0.52").remainder(BigDecimal.ONE));
	}
}
