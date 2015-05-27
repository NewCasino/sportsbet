/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.constant;

/**
 *
 * @author Admin
 */
public enum Language {

    ENGLISH("english", "en-gb"),
    CHINESE_TRADITIONAL("traditional", "zh-tw"),
    CHINESE_SIMPLIFIED("simplified", "zh-cn"),
    UNSUPPORT("", "");
    private String value;
    private String namedValue;

    private Language(String value, String namedValue) {
        this.value = value;
        this.namedValue = namedValue;
    }

    public String getNamedValue() {
        return namedValue;
    }

    public String getValue() {
        return value;
    }
    
    public static Language parse(String lang){
        try {
            return Language.valueOf(lang);
        } catch (Exception e) {
        }
        
        String prefix = lang.split("-|_")[0].toLowerCase();
        Language l = getLanguageByValue(lang);
        if(l == UNSUPPORT){
            for (Language lanauge : values()) {
                if (lanauge.getNamedValue().startsWith(prefix)) {
                    return lanauge;
                }
            }
        }
        if(l == UNSUPPORT) l = ENGLISH;
        return l;
    }

    public static Language getLanguageByValue(String value) {
        for (Language lanauge : values()) {
            if (lanauge.getValue().equalsIgnoreCase(value)) {
                return lanauge;
            }
        }
        return Language.UNSUPPORT;
    }

    public static Language getLanguageByNamedValue(String namedValue) {
        for (Language lanauge : values()) {
            if (lanauge.getNamedValue().equalsIgnoreCase(namedValue)) {
                return lanauge;
            }
        }
        return Language.UNSUPPORT;
    }
}
