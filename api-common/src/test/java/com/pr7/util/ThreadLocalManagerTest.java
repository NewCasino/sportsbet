package com.pr7.util;

import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ThreadLocalManagerTest {
	private static String KEY = "KEY";
	
	@Test
	public void testMultiThread() throws Exception {
		
		int count = 10;
		
		MyThread[] threadArray = new MyThread[count];
		
		for(int i = 0; i < count; i++) {
			threadArray[i] = new MyThread("" + i);
		}
		
		for(int i = 0; i < count; i++) {
			threadArray[i].start();
		}
		
		for(int i = 0; i < count; i++) {
			threadArray[i].join();
		}
		
		for(int i = 0; i < count; i++) {
			System.out.println("i = " + i + ", getThreadValue = " + threadArray[i].getThreadValue());
		}
	}
	
	class MyThread extends Thread {
		
		private String name;
		private String threadValue;
		
		public MyThread(String name) {
			this.name = name;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(RandomUtils.nextInt(5)*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String value = "Thread " + this.name + ", date = " + new Date();
			appendThreadValue(value);
			
			try {
				Thread.sleep(RandomUtils.nextInt(5)*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			value = "Thread " + this.name + ", date = " + new Date();
			appendThreadValue(value);
			
			this.threadValue = (String) ThreadLocalManager.getThreadContext().getDataMap().get(KEY);			
		}
		
		public String getThreadValue() {
			return this.threadValue;
		}
	}
	
	private void appendThreadValue(String value) {
		String currentValue = (String) ThreadLocalManager.getThreadContext().getDataMap().get(KEY);
		ThreadLocalManager.getThreadContext().getDataMap().put(KEY, currentValue + "<br>" + value);	
	}
}
