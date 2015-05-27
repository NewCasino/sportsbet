package com.pr7.common.web.view;

/**
 *
 * @author mark.ma
 */
public class XmlView extends TextView {
    
    public XmlView(String text) {
        super(text);
    }

    @Override
    public String getContentType() {
        return "text/xml;charset=UTF-8";
    }
}
