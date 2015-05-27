/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import java.io.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class TesseractOcrClient {

    String binaryFileName;
    String workingDir;

    public TesseractOcrClient(String binaryFileName, String workingDir) {
        this.binaryFileName = binaryFileName;
        this.workingDir = workingDir;
    }
    
    public String convertToText(byte[] image, boolean digitsOnly, String modes) throws IOException, InterruptedException {
        Long time = (Long)System.currentTimeMillis();
        String imageFileName = "ocr_image_" + time.toString();
        String outputFileName = "ocr_result_" + time.toString() + ".txt";
        File imageFile = new File(workingDir + "/" + imageFileName);
        File outputFile = new File(workingDir + "/" + outputFileName);
        String options = digitsOnly ? "nobatch digits" : "";
        
        try {
            FileOutputStream imageStream = new FileOutputStream(imageFile);
            imageStream.write(image);
            imageStream.close();
            
            Process process = new ProcessBuilder(
                binaryFileName,
                imageFileName,
                StringUtils.stripEnd(outputFileName, ".txt"),
                "-psm " + (modes == null ? "8" : modes), // default = 8, Treat the image as a single word.
                options
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

            String result = null;
            if (outputFile.exists()) {
                BufferedReader buffer = new BufferedReader(new FileReader(outputFile));
                result = buffer.readLine();
                buffer.close();
            } else {
                throw new RuntimeException(IOUtils.toString(process.getInputStream()) + ", " + IOUtils.toString(process.getErrorStream()));
            }
            return result;
        } finally {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }
    } 
    
}
