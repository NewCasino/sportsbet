/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class ExcelGenerator {
    private static final Logger _logger = LogManager.getLogger(ExcelGenerator.class);
    private List<Object> _data;
    private String _fileName;
    private Workbook _workbook;
    List<ColumnMap> _columnsMap;
    String[] _totalColumns;
    HashMap<ColumnMap, CellStyle> _columnCellStyle = new HashMap<ColumnMap, CellStyle>();
    

    private ExcelGenerator.ExcelFileFormat _excelFileFormat = ExcelGenerator.ExcelFileFormat.XLS;

    private ExcelDataMapper dataMapper;

    public ExcelGenerator(String fileName, List data, List<ColumnMap> columnsMap, ExcelGenerator.ExcelFileFormat excelFileFormat) {
        this._fileName = fileName;
        this._columnsMap = columnsMap;
        this._data = data;
        this._excelFileFormat = excelFileFormat;
        
    }

    public ExcelGenerator(String fileName, List data, List<ColumnMap> columnsMap, ExcelGenerator.ExcelFileFormat excelFileFormat, String[] totalColumns) {
        this._fileName = fileName;
        this._columnsMap = columnsMap;
        this._data = data;
        this._excelFileFormat = excelFileFormat;
        this._totalColumns = totalColumns;
    }

    public ExcelGenerator(String fileName, List<ColumnMap> columnsMap, ExcelDataMapper dataMapper) {
        this._fileName = fileName;
        this._columnsMap = columnsMap;
        this.dataMapper = dataMapper;
    }
    
    public ExcelGenerator(String fileName, List data, ExcelGenerator.ExcelFileFormat excelFileFormat, ColumnMap... columnMaps){
        this._fileName = fileName;
        this._columnsMap = Arrays.asList(columnMaps);        
        this._excelFileFormat = excelFileFormat;
        this._data = data;
    }
    
    public ExcelGenerator(String fileName, List data, ExcelGenerator.ExcelFileFormat excelFileFormat, ExcelDataMapper dataMapper, ColumnMap... columnMaps){
        this._fileName = fileName;
        this._columnsMap = Arrays.asList(columnMaps);
        this.dataMapper = dataMapper;
        this._excelFileFormat = excelFileFormat;
        this._data = data;
    }
    

    public List<Object> getData() {
        return _data;
    }
    
    public Workbook generateWorkbook(){
        List<Object> data = getData();
        _workbook = createWorkbook();        
        if(data != null){
            Sheet sheet = _workbook.createSheet(WorkbookUtil.createSafeSheetName(getFileName()));
            createHeaderRow(sheet);
            int i;
            for (i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                pupoplateRowData(row, data.get(i));
            }
            
            // generate total row
            if (_totalColumns != null) {
                try {
                    Row row = sheet.createRow(i + 1);
                    Map<String, Double> colValues = new HashMap<String, Double>();
                    for (i = 0; i < data.size(); i++) {
                        for (String colName : _totalColumns) {
                            Class clazz = data.get(i).getClass();         
                            Field field = null;
                            Object value;
                            try {
                                field = clazz.getDeclaredField(colName);
                            } catch (NoSuchFieldException noSuchFieldException) {
                                _logger.debug(noSuchFieldException.getMessage() +". Will try to get by getter method.");
                            } 
                            if(field == null || !field.isAccessible()){
                                value = clazz.getMethod("get" + StringUtils.capitalize(colName)).invoke(data.get(i));
                            }else{
                                value = field.get(data.get(i));
                            }
                            
                            if (value instanceof Double) {
                                if (colValues.containsKey(colName)) {
                                    colValues.put(colName, colValues.get(colName) + (Double)value);
                                } else {
                                    colValues.put(colName, (Double)value);
                                }
                            }
                        }
                    }
                    
                    int col = 0;
                    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                    for (ColumnMap colMap : _columnsMap) {            
                        Cell cell = row.createCell(col++);   
                        cell.setCellStyle(cellStyle);        
                        if (col == 1) {
                            cell.setCellValue("Total:");
                        }
                        else if (colValues.containsKey(colMap.getFieldName())) {
                            Double value = colValues.get(colMap.getFieldName());
                            cell.setCellValue(value);
                        }
                    }        
                } catch (Exception ex) {        
                    _logger.error("Error when gerenating total row", ex);            
                }
            }
            
        }else{
            _logger.debug("No data has been set.");
        }
        return _workbook;
    }
    
    public void write(OutputStream outputStream) throws IOException{
        if(_workbook == null) generateWorkbook();
        _workbook.write(outputStream);
        outputStream.close();
    }
    
    public Workbook getWorkbook() {
        return _workbook;
    }
      
    public ExcelGenerator.ExcelFileFormat getExcelFileFormat() {
        return _excelFileFormat;
    }

    public String getFileName() {
        return _fileName;
    }    
    
    private Workbook createWorkbook(){
        switch(getExcelFileFormat()){
            case XLSX:
                return new XSSFWorkbook();
            case XLS:
                return new HSSFWorkbook();
            default:
        }
        return new HSSFWorkbook();
    }
    
    public String getFileExtension(){
        return "." + getExcelFileFormat().name().toLowerCase();
    }
    
    private void createHeaderRow(Sheet sheet) {
        if(_columnsMap == null) {
            //TODO: auto populate 
            _columnsMap = new ArrayList<ColumnMap>();
        }
        int col = 0;
        Row row = sheet.createRow(0);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);        
        cellStyle.setFont(font); 
        for (ColumnMap colMap : _columnsMap) {
            if (colMap.getIgnoreThisColumn())
            {
                continue;
            }
            Cell cell = row.createCell(col++);            
            cell.setCellValue(colMap.getColumnTitle());
            cell.setCellStyle(cellStyle);
        }        
    }
    private void pupoplateRowData(Row row, Object obj) {
        if(obj == null) {
            _logger.debug("Object is NULL, ignore this row[" + row.getRowNum()+ "]");
        }
        if(_columnsMap == null) {
            //TODO: auto populate 
            _columnsMap = new ArrayList<ColumnMap>();
        }
        Workbook wb = row.getSheet().getWorkbook();        
        int col = 0;
        for (ColumnMap colMap : _columnsMap) {
            if(colMap.getIgnoreThisColumn())
            {
                continue;
            }
            Cell cell = row.createCell(col++);            
            if(dataMapper != null){
                dataMapper.cellMapper(cell, obj, colMap);
            }else{
                setCellData(cell, obj, colMap);
            }
            
            if(StringUtils.isNotBlank(colMap.getFormat()) || colMap.getValidator() !=null){
                CellStyle cellStyle = _columnCellStyle.get(colMap);                
                try {
                    if (cellStyle == null) {
                        cellStyle = wb.createCellStyle();
                        _columnCellStyle.put(colMap, cellStyle);
                        cellStyle.setAlignment(colMap.getTextHAlign());
                        cellStyle.setVerticalAlignment(colMap.getTextVAlign());
                        if(StringUtils.isNotBlank(colMap.getFormat())){
                            cellStyle.setDataFormat(wb.createDataFormat().getFormat(colMap.getFormat()));
                        }
                    }
                } catch (Exception e) {
                    _logger.error("Cant create cellstyle. " + e.getMessage());
                }
                
                if(cellStyle != null && colMap.getValidator() !=null){                    
                    cellStyle = colMap.getValidator().getCellStyle(obj, colMap, cellStyle, wb, row);                    
                }
                
                if(cellStyle != null) cell.setCellStyle(cellStyle);
            }
        }
    }

    private void setCellData(Cell cell, Object obj, ColumnMap colMap) {
        Object value = null;
        try {
            //boolean isAccessible = 
            Class clzz = obj.getClass();            
            Field field = null;
            try {
                field = clzz.getDeclaredField(colMap.getFieldName());
            } catch (NoSuchFieldException noSuchFieldException) {
                _logger.debug(noSuchFieldException.getMessage() +". Will try to get by getter method.");
            } 
            if(field == null || !field.isAccessible()){
                value = clzz.getMethod("get" + StringUtils.capitalize(colMap.getFieldName())).invoke(obj);
            }else{
                value = field.get(obj);
            }
        } catch (Exception ex) {        
            _logger.error("Error when getting value of field " + colMap.getFieldName() + ". " + ex.getMessage() , ex);            
        }
        if(colMap.getValidator() != null){
            value = colMap.getValidator().preProcessData(value, obj, colMap, cell.getRowIndex(), cell.getColumnIndex());
        }
        if(value != null){
            if(value instanceof String){
                cell.setCellValue((String)value);
            } else if(value instanceof Number){
                cell.setCellValue(((Number)value).doubleValue());
            } else if (value instanceof Date){
                cell.setCellValue((Date)value);
            } else if (value instanceof Calendar){
                cell.setCellValue((Calendar)value);
            } else {
                cell.setCellValue(value.toString());
            }
        }else{
            if(StringUtils.isNotBlank(colMap.getDefaultStr())){
                cell.setCellValue(colMap.getDefaultStr());
            }
        }
    }
    
    
    
    public enum ExcelFileFormat{
        /**
         * Excel 2007 format, OOXML 
         */
        XLSX,
        /**
         * Excel 97-2003 format
         */
        XLS
    }
}
