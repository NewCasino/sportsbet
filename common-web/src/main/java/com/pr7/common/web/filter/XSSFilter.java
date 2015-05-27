package com.pr7.common.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.web.filter.RequestContextFilter;

import com.pr7.common.web.util.XSSValidator;
import com.pr7.util.PropertyHelper;

/**
 * XSSFilter.
 * 
 * Reject cross site scripting
 * 
 * @author Xuan
 * 
 */
public class XSSFilter extends RequestContextFilter {
	
	private static final Logger _logger = LogManager.getLogger(XSSFilter.class);
	private static final String DEFAULT_URI_WHITE_LIST = "/sb2odds/";
	private static final String DEFAULT_METHOD_TO_SCAN = "POST";
	private static ConcurrentSkipListSet<String> URI_WHITE_LIST = null;
	private static String METHOD_TO_SCAN = null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.GenericFilterBean#initBeanWrapper(org.
	 * springframework.beans.BeanWrapper)
	 */
	@Override
	protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
		super.initBeanWrapper(bw);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		
		if (URI_WHITE_LIST == null) {
			String jndiName = this.getFilterConfig().getInitParameter("jndiName");
			String whiteList = null;
			if (jndiName != null) {
				whiteList = PropertyHelper.getProperty(jndiName, "xssfilter.uri.whitelist");
				if (whiteList == null) {
					whiteList = DEFAULT_URI_WHITE_LIST;
				}
				URI_WHITE_LIST = new ConcurrentSkipListSet<String>(Arrays.asList(whiteList.split(",")));
			} else {
				URI_WHITE_LIST = new ConcurrentSkipListSet<String>();
			}
			_logger.debug("initFilterBean:: jndiName = " + jndiName + ", whiteList = " + whiteList + ", size = " + URI_WHITE_LIST.size());
		}
		
		if (METHOD_TO_SCAN == null) {
			String jndiName = this.getFilterConfig().getInitParameter("jndiName");
			if (jndiName != null) {
				METHOD_TO_SCAN = PropertyHelper.getProperty(jndiName, "xssfilter.method.toscan");
				if (METHOD_TO_SCAN == null) {
					METHOD_TO_SCAN = DEFAULT_METHOD_TO_SCAN;
				}				
			} else {
				METHOD_TO_SCAN = "";
			}
			
			_logger.debug("initFilterBean:: jndiName = " + jndiName + ", METHOD_TO_SCAN = " + METHOD_TO_SCAN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.RequestContextFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {		
		if (StringUtils.containsIgnoreCase(METHOD_TO_SCAN, request.getMethod())) {
			boolean isWhiteList = false;
			for(String whiteList: URI_WHITE_LIST) {
				if (request.getRequestURI().contains(whiteList)) {
					isWhiteList = true;
					break;
				}
			}
			if (!isWhiteList) {
				for(Object name: Collections.list(request.getParameterNames())) {
					String value = request.getParameter((String) name);
					if (!XSSValidator.isValid(value)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						logger.error("INVALID request params = " + name + ", value = " + value);
						return;
					}
				}
			}		
		}

		super.doFilterInternal(request, response, filterChain);
	}
}
