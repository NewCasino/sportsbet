package com.pr7.common.web.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
/**
 * this class is meant to be used as a return value from @Controller directly
 *
 * and this implementation doesn't follow the spring view's contract, which states that the view should be stateless and
 * be registered as a spring bean 
 *
 * it is not thread safe
 * @author dapeng
 */
public class TextView implements View {
    private String strContent;

    public TextView() {
    }

    public TextView(String strContent) {
        this.strContent = strContent;
    }
    @Override
    public String getContentType() {
        return "text/html;charset=UTF-8";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());       
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
        response.getWriter().write(strContent);
    }
}
