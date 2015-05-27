package com.pr7.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TesseractOcrClientTest {
	
	@Test
	public void testPercentageMs() throws Exception {
		//TesseractOcrClient ocr = new TesseractOcrClient("C:/Program Files (x86)/Tesseract-OCR/tesseract.exe", "C:/tmp");
		String workingDir = "C:/tmp/newcore2/ms";
		String captchaUrl = "http://www.ascbet.com/Vcode.ashx";
        
		for(int i=11; i <= 20; i++) {
			String imageFileName = i + "_ms_captcha" + ".jpg";
			ConnectionResponse connectionResponse = ConnectionHelper.getFromUrl(captchaUrl, "");
	        byte[] image = connectionResponse.getData();        
	        FileUtils.writeByteArrayToFile(new File(workingDir + "/" + imageFileName), image);
	        
			Process process = new ProcessBuilder(
					"C:/Program Files (x86)/Tesseract-OCR/tesseract.exe",
	                imageFileName,
	                i + "_result",
	                "-psm 8", // default = 8, Treat the image as a single word.
	                "nobatch digits"
	                )
	                .directory(new File(workingDir))
	                .start();
	        
	            try {
	                process.waitFor();
	            } catch (InterruptedException ex) {
	                if (process.exitValue() != 0) {
	                    throw ex;
	                }
	            }
			
		}       
	}
	
	@Test
	public void testPercentageAgent() throws Exception {
		//TesseractOcrClient ocr = new TesseractOcrClient("C:/Program Files (x86)/Tesseract-OCR/tesseract.exe", "C:/tmp");
		String workingDir = "C:/tmp/newcore2/agent";
		String captchaUrl = "http://wm.ascbet.com/DataPage/Vcode.ashx";
        
		for(int i=21; i <= 40; i++) {
			String imageFileName = i + "_agent_captcha" + ".jpg";
			ConnectionResponse connectionResponse = ConnectionHelper.getFromUrl(captchaUrl, "");
	        byte[] image = connectionResponse.getData();        
	        FileUtils.writeByteArrayToFile(new File(workingDir + "/" + imageFileName), image);
	        
			Process process = new ProcessBuilder(
					"C:/Program Files (x86)/Tesseract-OCR/tesseract.exe",
	                imageFileName,
	                i + "_result",
	                "-psm 8", // default = 8, Treat the image as a single word.
	                "nobatch digits"
	                )
	                .directory(new File(workingDir))
	                .start();
	        
	            try {
	                process.waitFor();
	            } catch (InterruptedException ex) {
	                if (process.exitValue() != 0) {
	                    throw ex;
	                }
	            }
			
		}       
	}
}
