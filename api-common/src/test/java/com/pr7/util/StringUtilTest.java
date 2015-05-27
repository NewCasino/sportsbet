package com.pr7.util;

import java.text.ParseException;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringUtilTest {
	
	@Test
	public void testReplaceNonDigit() throws ParseException {
		String refno = "ABC12345678909999DEF";
		String result = StringUtil.replaceNonDigit(refno);
		System.out.println("result = " + result);
	}
	
	@Test
	public void testContainsIgnoreCase() throws ParseException {
		String s = null;
		Assert.assertEquals(false, StringUtils.containsIgnoreCase(s, "aaa"));
	}
	
	@Test
	public void testSubstringBefore() {
		String s = "aaa,";
		Assert.assertEquals("aaa,", StringUtils.substringBefore(s, ":"));
		
		s = "aaa:";
		Assert.assertEquals("aaa", StringUtils.substringBefore(s, ":"));
	}
}
