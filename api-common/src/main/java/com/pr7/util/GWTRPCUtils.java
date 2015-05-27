/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

/**
 *
 * @author Admin
 */
public class GWTRPCUtils {

    public static boolean isAnObjectType(String str) {
        return str.matches(".*/\\d+");
    }

    public static String getTypeName(String str) {
        return str.replaceAll("/\\d+", "");
    }
}
