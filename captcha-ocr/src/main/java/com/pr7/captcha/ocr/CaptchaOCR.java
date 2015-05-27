/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.captcha.ocr;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.OCREnginMode;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public abstract class CaptchaOCR {
    private String tessDataPath;
    private String workingDir;
    private String binaryFileName;
    private boolean useCmd = false;
    private CmdOptions option = CmdOptions.DEFAULT;
    
    public OCREnginMode getOcrEngine (){
        return OCREnginMode.OEM_TESSERACT_CUBE_COMBINED;
    }
    
    public String readCaptcha(byte[] imageData) throws IOException, TesseractException, InterruptedException {
        String captcha;
        if (useCmd) {
            captcha =  StringUtils.defaultIfBlank(convertToText(imageData, true, null, option.getConfigFile()), "");
        }else{
            InputStream in = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(in);
            Tesseract ocr = getTesseractOCR();
            ImageIO.scanForPlugins();
            captcha = ocr.doOCR(image);
        }
        return captcha.replaceAll("[^0-9]", "").trim();
    }
    
    protected Tesseract getTesseractOCR() {
        Tesseract ocr = Tesseract.getInstance();
        ocr.setOcrEngineMode(getOcrEngine());
        ocr.setPageSegMode(8);
        ocr.setTessVariable("tessedit_char_whitelist", "0123456789");
        if (StringUtils.isNotBlank(tessDataPath)) {
            ocr.setDatapath(tessDataPath);
        }
        return ocr;
    }

    public String convertToText(byte[] image, boolean digitsOnly, String modes,String options) throws IOException, InterruptedException {
        long time = System.currentTimeMillis();
        String imageFileName = "ocr_image_" + time;
        String outputFileName = "ocr_result_" + time + ".txt";

        File imageFile = new File(workingDir + "/" + imageFileName);
        File outputFile = new File(workingDir + "/" + outputFileName);
        
        options = StringUtils.defaultIfBlank(options,"") + (digitsOnly ? "nobatch digits" : "");

        try {
            FileOutputStream imageStream = new FileOutputStream(imageFile);
            imageStream.write(image);
            imageStream.close();

            Process process = new ProcessBuilder(
                    binaryFileName,
                    imageFileName,
                    StringUtils.stripEnd(outputFileName, ".txt"),
                    "-psm " + (modes == null ? "8" : modes), // default = 8, Treat the image as a single word.
                    options).directory(new File(workingDir)).start();

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
    
    
    public String getTessDataPath() {
        return tessDataPath;
    }

    public void setTessDataPath(String tessDataPath) {
        this.tessDataPath = tessDataPath;
    }

    public String getBinaryFileName() {
        return binaryFileName;
    }

    public void setBinaryFileName(String binaryFileName) {
        this.binaryFileName = binaryFileName;
    }

    public boolean isUseCmd() {
        return useCmd;
    }

    public void setUseCmd(boolean useCmd) {
        this.useCmd = useCmd;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }    
    
    public static enum CmdOptions {
        DEFAULT(""),
        /**
         * Make sure you have <code>cubemode</code> configuration file in <code>tessdata/tessconfig</code>
         * with <b><code>tessedit_ocr_engine_mode 2</code></b> in content
         */
        ENGINE_MODE_CUBE_COMBINED("cubemode");
        String configFile;
        private CmdOptions(String configFile) {
            this.configFile = configFile;
        }

        public String getConfigFile() {
            return configFile;
        }        
    }

    public CmdOptions getOption() {
        return option;
    }

    public void setOption(CmdOptions option) {
        this.option = option;
    }
    
}
