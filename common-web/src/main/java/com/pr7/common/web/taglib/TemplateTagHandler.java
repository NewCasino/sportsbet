/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.taglib;

import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * @author Mark
 */
public class TemplateTagHandler extends BodyTagSupport {

    private String src = "";
    private static transient Logger logger = Logger.getLogger(TemplateTagHandler.class.getName());

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        try {

            JspWriter out = pageContext.getOut();

            final ServletContext servletContext = pageContext.getServletContext();

            final String fullPath = FilenameUtils.getFullPath(servletContext.getContextPath()) + src;
            final String realPath = servletContext.getRealPath(fullPath);
            out.write(IOUtils.toString(new FileInputStream(realPath), "UTF-8"));
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.toString());
        }

        return super.doEndTag();
    }
}
