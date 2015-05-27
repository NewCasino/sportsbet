package com.pr7.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.pr7.constant.Constants;

public class DateUtilTest {
	@Test
	public void testConvertToTimeZone() throws ParseException {
		String str = "2011,08,05,22,00,00";
		String format = "yyyy,MM,dd,HH,mm,ss";
		
		Date date = DateUtil.convertToTimeZone(str, format, TimeZone.getTimeZone("GMT+2"), TimeZone.getTimeZone("GMT"));
		System.out.println(date.toString());
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		
		Assert.assertEquals(20, cal.get(Calendar.HOUR_OF_DAY));
		
		str = "19.08.2010 04:00";
		format = "dd.MM.yyyy HH:mm";
		
		date = DateUtil.convertToTimeZone(str, format, TimeZone.getTimeZone("GMT+2"), TimeZone.getTimeZone("GMT"));
		System.out.println(date.toString());
		cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
	}
	
	@Test
	public void testMinuteDiff() {
		Date source = DateUtil.parse("2011-08-17 15:00:00", "yyyy-MM-dd HH:mm:ss");
		Date target = DateUtil.parse("2011-08-17 14:45:00", "yyyy-MM-dd HH:mm:ss");
		
		Assert.assertEquals(new Double(15), DateUtil.minuteDiff(source, target));
		Assert.assertEquals(-new Double(15), DateUtil.minuteDiff(target, source));
	}
	
	@Test
	public void testDayDiff() {
		Date source = DateUtil.parse("2011-08-17 15:00:00", "yyyy-MM-dd HH:mm:ss");
		Date target = DateUtil.parse("2011-08-17 14:45:00", "yyyy-MM-dd HH:mm:ss");
		
		Assert.assertEquals(0, DateUtil.dayDiff(DateUtil.parse("2011-08-17 15:00:00", "yyyy-MM-dd HH:mm:ss"), DateUtil.parse("2011-08-17 14:45:00", "yyyy-MM-dd HH:mm:ss")));
		Assert.assertEquals(-2, DateUtil.dayDiff(DateUtil.parse("2011-08-15 15:00:00", "yyyy-MM-dd HH:mm:ss"), DateUtil.parse("2011-08-17 14:45:00", "yyyy-MM-dd HH:mm:ss")));
		Assert.assertEquals(32, DateUtil.dayDiff(DateUtil.parse("2012-03-01 15:00:00", "yyyy-MM-dd HH:mm:ss"), DateUtil.parse("2012-01-29 14:45:00", "yyyy-MM-dd HH:mm:ss")));
	}
	
	@Test
	public void testPercent() {
		Assert.assertEquals(15, 15 % 20);
		Assert.assertEquals(0, 20 % 20);
		Assert.assertEquals(0, 40 % 20);
		Assert.assertEquals(5, 45 % 20);
	}
	
	@Test
	public void testFormat() {
		Date source = DateUtil.parse("2011-08-17 15:00:00", "yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals("2011-08-17", DateUtil.format(source, "yyyy-MM-dd"));
	}
	
	@Test
	public void testCalTimeZone() {
		Calendar calCurrent = new GregorianCalendar();
		Calendar calFs = new GregorianCalendar(TimeZone.getTimeZone(Constants.TIMEZONE_FLASHSCORE));
		
		System.out.println(DateUtil.format(calFs.getTime(), "yyyy.dd.MM. ", TimeZone.getTimeZone(Constants.TIMEZONE_FLASHSCORE)));
		System.out.println(calFs.get(Calendar.YEAR) + "." + calFs.get(Calendar.MONTH) + "." + calFs.get(Calendar.DATE));//this is not correct because Jan is 0 while Sep is 8 
		
		Assert.assertEquals(calCurrent.get(Calendar.HOUR_OF_DAY) - 6, calFs.get(Calendar.HOUR_OF_DAY));
	}
	
	@Test
	public void testParse() {
		Date source = DateUtil.parse(DateUtil.format(new Date(), "dd/MM/yyyy ") + "23:55:00", "dd/MM/yyyy HH:mm:ss");
		Assert.assertEquals("21/05/2012 23:55:00", DateUtil.format(source, "dd/MM/yyyy HH:mm:ss"));
		Date test = null;
		System.out.println("test = " + test);
		test = new Date();
		System.out.println("test2 = " + test);
	}
	
	@Test
	public void testCronExpression() throws Exception {
		Date d1 = DateUtil.parse("10/10/2011 11:55:00", "dd/MM/yyyy HH:mm:ss");
		Date d2 = DateUtil.parse("11/10/2011 11:55:00", "dd/MM/yyyy HH:mm:ss");
		
		Assert.assertEquals("11:55:00", DateUtil.format(d2, "HH:mm:ss"));
		
//		CronExpression cronExpression = new CronExpression("* 55 11 * * ?");
//		Assert.assertEquals(true, cronExpression.isSatisfiedBy(d1));
//		Assert.assertEquals(false, cronExpression.isSatisfiedBy(new Date()));
	}
	
	@Test
	public void testNewFolder() throws IOException {
		String folderName = "archive";
		File folder = new File(folderName);
		if (!folder.exists()) {
			if (folder.mkdir()) {
				System.out.println("Create " + folderName + " success.");
			} else {
				System.out.println("Create " + folderName + " failed.");
			}
		} else {
			System.out.println("Folder " + folderName + " already exists.");
			File testFile = new File(folder, "test.txt");
			testFile.createNewFile();
		}
	}
	
	@Test
	public void testAddDays() throws IOException {
		Date today = new Date();
		System.out.println(DateUtils.addDays(today, -1));
	}
	
	@Test
	public void testAddMonths() throws IOException {
		Date month = DateUtil.parse("30/09/2012", "dd/MM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		cal.add(Calendar.MONTH, 1);
		int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Date toDate = DateUtils.addSeconds(DateUtils.addDays(month, maxDays), -1);
		System.out.println("toDate = " + toDate + ", DateUtils.addMonths(month, 1) = " + DateUtils.addMonths(month, 1) + ", cal = " + cal.getTime() + ", maxDays = " + maxDays);
	}
	
}
