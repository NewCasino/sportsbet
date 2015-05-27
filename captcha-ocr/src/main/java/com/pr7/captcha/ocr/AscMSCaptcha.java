/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.captcha.ocr;

import com.pr7.captcha.utils.ImageUtil;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.OCREnginMode;
import net.sourceforge.tess4j.TesseractException;

public class AscMSCaptcha extends CaptchaOCR{
    public AscMSCaptcha() {
    }
    
    public AscMSCaptcha(String tessdataPath){
        this.setTessDataPath(tessdataPath);
    }

    @Override
    public OCREnginMode getOcrEngine() {
        return OCREnginMode.OEM_DEFAULT;
    }
    @Override
    public String readCaptcha(byte[] imageData) throws IOException, TesseractException, InterruptedException {
               
        AffineTransform tx = new AffineTransform();
        tx.shear(0.35, 0);        
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        
        InputStream in = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(in);
        
        //Shear image
        bufferedImage = op.filter(bufferedImage, null);
//        bufferedImage = cropImage(bufferedImage, new Rectangle(48, 16));
        //Remove background noise
        bufferedImage = ImageUtil.imageToBufferedImage(ImageUtil.transformColorToTransparency(bufferedImage, new Color(36, 101, 121), new Color(223, 255, 255)), bufferedImage.getWidth(), bufferedImage.getHeight());

        //New image with white background
        BufferedImage newImage = new BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage( bufferedImage, 0, 0, Color.WHITE, null);
        
//        ImageIO.write(newImage, "jpg", new File("C:\\Users\\Admin\\Desktop\\test\\code " + System.currentTimeMillis()+ " .jpg"));
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(newImage, "jpg", stream);
        
        return super.readCaptcha(stream.toByteArray());
    } 
}
