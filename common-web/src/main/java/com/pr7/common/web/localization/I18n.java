/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.localization;

import com.pr7.common.web.spring.support.SpringApplicationContext;
import flexjson.JSONDeserializer;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mark.ma
 */
final public class I18n {

    private static Logger logger = Logger.getLogger(I18n.class.getName());

    /**
     * English is default language
     */
    public static final Locale defaultLocale = Locale.US;

    /**
     * Dictionaries, lazy load dictionary into memory when it is required
     */
    private static final Map<String, Map> dictionaries = new HashMap<String, Map>();

    private static final String resourcePrefix = "/META-INF/locale.";
    private static final String resourceSuffix = ".json";

    private static final ThreadLocal currentLocale = new ThreadLocal() {
        @Override
        protected synchronized Object initialValue() {
            return defaultLocale;
        }
    };

    /**
     * get locale from current session
     * @param locale
     */
    public static Locale getCurrentLocale() {
        return (Locale)currentLocale.get();
    }

    /**
     * set locale from current session
     * @param locale
     */
    public static void setCurrentLocale(Locale locale) {
        currentLocale.set(locale);
    }

    /**
     * translate texts
     * @param text
     * @param domain
     * @return
     */
    public static String _n(String text) {
        return _n(text, null);
    }

    /**
     * translate texts with domain
     * @param text
     * @param domain
     * @return
     */
    public static String _n(String text, String domain) {

        String locale = currentLocale.get().toString();

        if (!StringUtils.isBlank(locale) && !locale.equals(defaultLocale.toString())) {

            String prefix = StringUtils.isBlank(domain) ? "" : domain + ".";
            Map dictionary = getDictionary(prefix + locale);
            if (dictionary != null && dictionary.containsKey(text)) {
                try {
                    return (String)dictionary.get(text);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "directory " + domain + ", " + locale + " needs to be define", ex);
                }
            }
        }

        return text;
    }

    /**
     * get <text, translation> dictionary by domain and language,
     * the dictionary stored as JSON format
     * @param name
     * @return
     */
    private static Map getDictionary(String name) {
        Map dictionary = null;

        try {
            if (dictionaries.containsKey(name)) {
                dictionary = dictionaries.get(name);
            } else {
                // get JSON format of dictionary from /META-INF
                InputStream stream = null;
                String json = null;

                try {
                    stream = SpringApplicationContext.getCurrent()
                            .getResource(resourcePrefix + name + resourceSuffix).getInputStream();
                    json = IOUtils.toString(stream, "UTF-8");
                } finally {
                    stream.close();
                }
                
                dictionary = (Map)new JSONDeserializer().deserialize(json);
                dictionaries.put(name, dictionary);
            }
        } catch (Exception ex) { 
            logger.log(Level.WARNING, "I18N: failed to load dictionary '" + resourcePrefix + name + resourceSuffix + "'.", ex);
        }

        return dictionary;
    }
}
