/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.interceptors;

import com.pr7.backoffice.utils.RequestUtils;
import com.pr7.backoffice.web.JsonView;
import com.pr7.server.common.exception.AuthenticationException;
import com.pr7.server.common.exception.AuthorizationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Admin
 */
public class BOExceptionResolver implements  HandlerExceptionResolver  {
    
    final private static Logger logger = LogManager.getLogger(BOExceptionResolver.class.getName());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        logger.warn(o, ex);
        
        if (ex.getCause() != null)
            ex = (Exception) ex.getCause();
        
        if(ex instanceof AuthorizationException){
            AuthorizationException exception = (AuthorizationException)ex;
            if(exception.isWebService()){                
                return new ModelAndView(new JsonView().put("error", exception.getCode()).put("msg", ex.getMessage()));
            }else{
                return new ModelAndView("error/403");
            }
        } else if (ex instanceof AuthenticationException){
            if(RequestUtils.isWebServiceRquest(request)){
                return new ModelAndView(new JsonView().put("error",401).put("msg",  ex.getMessage()));
            }else{
                return new ModelAndView("redirect:/");
            }
        }
        if(RequestUtils.isWebServiceRquest(request)){
            return new ModelAndView(new JsonView().put("error",500).put("msg", ex.getMessage()));
        }
        return new ModelAndView("error/500");
    }

}
