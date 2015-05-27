/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.filter;

import com.pr7.common.web.AppSetting;
import com.pr7.common.web.spring.support.SpringApplicationContext;
import com.pr7.common.web.wro4j.support.FilesLocator;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import ro.isdc.wro.model.resource.ResourceType;

/**
 *
 * @author mark.ma
 */
public class WroFilter implements Filter {

    FilterConfig filterConfig;
    static final String scriptLoader = new StringBuilder().
                                append("function loadScript (src) {\n").
//                                append("    if (navigator.appVersion.indexOf('MSIE') != -1 || document.readyState != 'loading') {\n").
//                                append("        var container = document.head || document.getElementsByTagName('head')[0];\n").
//                                append("        var script = document.createElement('script');\n").
//                                append("        script.src = src;\n").
//                                append("        container.appendChild(script);\n").
//                                append("    } else {\n").
                                append("        document.write('<script src=\"' + src + '\" type=\"text/javascript\"></script>');\n").
//                                append("    }\n").
                                append("};").toString();
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
        filterConfig = fc;
    }

    @Override
    public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        AppSetting appSetting = (AppSetting)SpringApplicationContext.getCurrent().getBean(AppSetting.class);

        if (!appSetting.isDebugMode()) {
            Filter wroFilter = new ro.isdc.wro.http.WroFilter();
            wroFilter.init(filterConfig);
            wroFilter.doFilter(req, res, chain);
            return;
        }
        
        String fileName = new URL(request.getRequestURL().toString()).getFile();
        ResourceType type = fileName.endsWith(".js") ? ResourceType.JS : ResourceType.CSS;
        String group = FilenameUtils.getBaseName(fileName);

        List<String> files = new FilesLocator(
                filterConfig.getServletContext(), 
                type, 
                group).locate();
        
        Writer writer = response.getWriter();
        if (type == ResourceType.JS) {
            response.setContentType("text/javascript;charset=UTF-8");
            writer.write(scriptLoader);
            for (String file : files) {
                writer.write("loadScript('http://" + req.getRemoteAddr() + file + "');\n");
            }
        } else if (type == ResourceType.CSS) {
            response.setContentType("text/css;charset=UTF-8");
            for (String file : files) {
                writer.write("@import 'http://" + req.getRemoteAddr() + file + "';\n");
            }
        }
        writer.close();
    }
    
    @Override
    public void destroy() {
    }
}
