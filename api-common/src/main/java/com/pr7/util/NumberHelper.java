/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

/**
 *
 * @author Admin
 */
public class NumberHelper {

    public static int MIN_RADIX = 2;
    public static int MAX_RADIX = 62;
    
    private NumberHelper() {
    }
    
    private static final String num62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    
    public static String baseConver(String number, int fromRadix, int toRadix) {
        long dec = any2Dec(number, fromRadix);
        return dec2Any(dec, toRadix);
    }

    
    public static String dec2Any(long dec, int toRadix) {
        if (toRadix < MIN_RADIX || toRadix > MAX_RADIX) {
            toRadix = 2;
        }
        if (toRadix == 10) {
            return String.valueOf(dec);
        }
        // -Long.MIN_VALUE è½¬æ¢ä¸º 2 è¿›åˆ¶æ—¶é•¿åº¦ä¸º65
        char[] buf = new char[65]; // 
        int charPos = 64;
        boolean isNegative = (dec < 0);
        if (!isNegative) {
            dec = -dec;
        }
        while (dec <= -toRadix) {
            buf[charPos--] = num62.charAt((int) (-(dec % toRadix)));
            dec = dec / toRadix;
        }
        buf[charPos] = num62.charAt((int) (-dec));
        if (isNegative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }

    
    public static long any2Dec(String number, int fromRadix) {
        long dec = 0;
        long digitValue = 0;
        int len = number.length() - 1;
        for (int t = 0; t <= len; t++) {
            digitValue = num62.indexOf(number.charAt(t));
            dec = dec * fromRadix + digitValue;
        }
        return dec;
    }
}