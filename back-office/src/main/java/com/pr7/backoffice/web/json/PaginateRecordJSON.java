/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.json;

import com.pr7.modelsb.dto.BaseFilter;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Admin
 */
public class PaginateRecordJSON {

    private int page;
    private int total;
    private int recordsPerPage;
    private double lastPage;

    public double getLastPage() {
        return lastPage;
    }
    Object[] records;
    private Map<Object,Object> userdata; //for jqGrid 'userdata' object

    public Map<Object,Object> getUserdata() {
        return userdata;
    }

    public void setUserdata(Map<Object,Object> userdata) {
        this.userdata = userdata;
    }
    private double calculateLastPage() {
       double result = Math.ceil( (double) total / (double) recordsPerPage);
       return result;
    }

    public PaginateRecordJSON() {
    }

    public PaginateRecordJSON(BaseFilter filter) {
        this.page = filter.getPageNo();
        this.recordsPerPage = filter.getRecordsPerPage();
        this.lastPage = this.calculateLastPage();
    }

    public PaginateRecordJSON(BaseFilter filter, Object[] records) {
        this(filter.getPageNo(), filter.getRecordsPerPage(), records);
    }
    
     public PaginateRecordJSON(BaseFilter filter, List<Object> records) {
        this(filter.getPageNo(), filter.getRecordsPerPage(), records.toArray());
    }

    public PaginateRecordJSON(int page, int recordsPerPage) {
        this.page = page;
        this.recordsPerPage = recordsPerPage;
        this.lastPage = this.calculateLastPage();
    }

    public PaginateRecordJSON(int page, int recordsPerPage, Object[] records) {
        if (recordsPerPage <= 0) {
            recordsPerPage = records.length;
        }
        this.page = page;
        this.recordsPerPage = recordsPerPage;
        this.total = records.length;
        this.lastPage = this.calculateLastPage();
        int startIndex = (page - 1) * recordsPerPage;
        if (startIndex >= records.length) {
            startIndex = 0;
        }
        this.records = ArrayUtils.subarray(records, startIndex, startIndex + recordsPerPage);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Object[] getRecords() {
        return records;
    }

    public void setRecords(Object[] records) {
        int startIndex = (page - 1) * recordsPerPage;
        if (recordsPerPage <= 0) {
            recordsPerPage = records.length;
        }
        this.total = records.length;
        this.records = ArrayUtils.subarray(records, startIndex, startIndex + recordsPerPage);
        this.lastPage = this.calculateLastPage();
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
