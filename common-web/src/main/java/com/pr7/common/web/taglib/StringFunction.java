/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pr7.common.web.taglib;

import com.pr7.common.web.localization.I18n;

/**
 *
 * @author mark.ma
 */
public class StringFunction {

    public static String i18n(String text) {
        return I18n._n(text);
    }

    public static String i18n(String text, String domain) {
        return I18n._n(text, domain);
    }

    public static String echo(String text) {
        return String.format("<script>utils.echo(%s)</script>", text);
    }

}
