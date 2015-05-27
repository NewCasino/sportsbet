package com.pr7.common.web.taglib;

import com.pr7.common.web.AppSetting;
import com.pr7.common.web.spring.support.SpringApplicationContext;
import com.pr7.common.web.wro4j.support.FilesLocator;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import ro.isdc.wro.model.resource.ResourceType;

/**
 * @author mark.ma
 */
public class ResourceTagHandler extends BodyTagSupport {

    /**
     * can only be js or css
     * for js need more processing to convert the html template into js string
     */
    private String group = "";
    private static final Logger logger = Logger.getLogger(ResourceTagHandler.class.getName());
    public String extName = ".css";
    public String outputPrefix = "\t\t<link type='text/css' href='";
    public String outputSuffix = "' rel='stylesheet' media='all' />\r\n";
    public ResourceType type = ResourceType.CSS;

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        AppSetting appSetting = (AppSetting)SpringApplicationContext.getCurrent().getBean(AppSetting.class);

        boolean debug = appSetting.isDebugMode();

        if (pageContext.getRequest().getParameter("debug") != null) {
            debug = "on".equalsIgnoreCase(pageContext.getRequest().getParameter("debug"));
        }

        JspWriter out = pageContext.getOut();
        final ServletContext servletContext = pageContext.getServletContext();

        try {
            if (debug) {
                List<String> files = new FilesLocator(servletContext, type, group).locate();
                for (String uri : files) {
                    write(out, servletContext.getContextPath() + uri);
                }
            } else {
                write(out, servletContext.getContextPath() + "/bundles/" + this.group + this.extName);
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return super.doEndTag();
    }

    private void write(Writer writer, String path) throws IOException {
        AppSetting appSetting = (AppSetting)SpringApplicationContext.getCurrent().getBean(AppSetting.class);
        
        writer.append(this.outputPrefix).append(appSetting.getCdnPrefix() + path).append("?v=" + appSetting.getVersion()).append(this.outputSuffix);
    }
}
