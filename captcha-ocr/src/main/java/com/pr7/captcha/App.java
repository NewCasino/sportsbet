package com.pr7.captcha;

import com.pr7.captcha.ocr.AscMSCaptcha;
import java.io.IOException;
import java.net.URL;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException, TesseractException, InterruptedException {
        AscMSCaptcha ocr = new AscMSCaptcha();
        int count = 0;
        for (int i = 0; i < 10; i++) {
            byte[] toByteArray = IOUtils.toByteArray(
                    new URL("http://www.ascbet.com/Vcode.ashx?" + System.currentTimeMillis()));
            String c = ocr.readCaptcha(toByteArray);
            System.out.println(c);
            if(StringUtils.isNotBlank(c) && c.length() == 4){
                count ++;
            }
//            Thread.sleep(Long.valueOf(RandomUtils.nextInt(3000)));
        }
        System.out.println("END success count " + count);
    }
}
