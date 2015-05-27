/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice;

import com.pr7.modelsb.dto.BaseFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class BaseFilterService {
    private static final Logger logger = Logger.getLogger(BaseFilter.class);

    @Autowired
    protected HttpServletRequest request;

    public BaseFilter getBaseFilter() {
        return getPaginationInfo(null);
    }

    public BaseFilter getPaginationInfo(BaseFilter filter) {
        if (filter == null) {
            filter = new BaseFilter();
        }
        int page = 1;
        int rp =  0;
        
        try {
            page = Integer.valueOf(request.getParameter("page"));
            if(page < 0 ) page = 1;
        } catch (NumberFormatException e) {
            logger.debug("Cant convert page number from " + request.getParameter("page") + " to int.");
        }

        try {
            rp = Integer.valueOf(request.getParameter("rp"));
            if(rp < 0 ) rp = 0;
        } catch (NumberFormatException e) {
            logger.debug("Cant convert record per page from " + request.getParameter("rp") + " to int.");
        }

        String sortname = request.getParameter("sortname");
        String sortorder = StringUtils.defaultIfBlank(request.getParameter("sortorder"), "asc");
        filter.setOrderBy(sortorder.equalsIgnoreCase("asc") ? BaseFilter.SortOrder.ASC : BaseFilter.SortOrder.DESC);
        filter.setSortByCols(sortname);
        filter.setPageNo(page);
        filter.setRecordsPerPage(rp);
        return filter;
    }
}
