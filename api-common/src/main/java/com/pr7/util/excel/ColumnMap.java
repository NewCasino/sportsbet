/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Admin
 */
public class ColumnMap {
    private String fieldName;
    private String columnTitle;
    private String format;
    private String defaultStr;
    private Validator validator;
    private short textHAlign = CellStyle.ALIGN_LEFT;
    private short textVAlign = CellStyle.VERTICAL_CENTER;    
    private boolean ignoreThisColumn = false;

    public boolean getIgnoreThisColumn() {
        return ignoreThisColumn;
    }

    public void setIgnoreThisColumn(boolean renderInExcel) {
        this.ignoreThisColumn = renderInExcel;
    }

    public ColumnMap(String fieldName) {
        this.fieldName = fieldName;
        this.columnTitle = fieldName;
    }

    public ColumnMap(String fieldName, String columnTitle, short textHAlign) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.textHAlign = textHAlign;
        this.validator = new Validator();
    }
    
    public ColumnMap(String fieldName, String columnTitle) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
    }

    public ColumnMap(String fieldName, String columnTitle, String defaultStr) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.defaultStr = defaultStr;
    }

    public ColumnMap(String fieldName, String columnTitle, String format, String defaultStr) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.format = format;
        this.defaultStr = defaultStr;
    }

    public ColumnMap(String fieldName, String columnTitle, Validator validator) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.validator = validator;
    }

    public ColumnMap(String fieldName, String columnTitle, String format, Validator validator) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.format = format;
        this.validator = validator;
    }

    public ColumnMap(String fieldName, String columnTitle, String format, String defaultStr, Validator validator) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.format = format;
        this.defaultStr = defaultStr;
        this.validator = validator;
    }

    public ColumnMap(String fieldName, String columnTitle, String format, String defaultStr, short textHAlign) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.format = format;
        this.defaultStr = defaultStr;
        this.textHAlign = textHAlign;
        this.validator = new Validator();
    }
    
    public ColumnMap(String fieldName, String columnTitle, String format, String defaultStr, short textHAlign, short textVAlign) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.format = format;
        this.defaultStr = defaultStr;        
        this.textHAlign = textHAlign;
        this.textVAlign = textVAlign;
        this.validator = new Validator();
    }

    public ColumnMap(String fieldName, String columnTitle, String defaultStr, short textHAlign, short textVAlign) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.defaultStr = defaultStr;
        this.textHAlign = textHAlign;
        this.textVAlign = textVAlign;
        this.validator = new Validator();
    }
    
    public ColumnMap(String fieldName, String columnTitle, String defaultStr, short textHAlign) {
        this.fieldName = fieldName;
        this.columnTitle = columnTitle;
        this.defaultStr = defaultStr;
        this.textHAlign = textHAlign;
        this.validator = new Validator();
    }
            
    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDefaultStr() {
        return defaultStr;
    }

    public void setDefaultStr(String defaultStr) {
        this.defaultStr = defaultStr;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator dataValidator) {
        this.validator = dataValidator;
    }

    public short getTextHAlign() {
        return textHAlign;
    }

    public short getTextVAlign() {
        return textVAlign;
    }

    public void setTextHAlign(short textHAlign) {
        this.textHAlign = textHAlign;
    }

    public void setTextVAlign(short textVAlign) {
        this.textVAlign = textVAlign;
    }
            
    public static class Validator{
        public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {return value;}
        public CellStyle getCellStyle(Object data, ColumnMap col,CellStyle columnCellStyle, Workbook workbook, Row row){            
            return columnCellStyle;
        }
    }
}
