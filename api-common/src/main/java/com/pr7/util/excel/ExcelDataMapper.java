/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author Admin
 */
public interface ExcelDataMapper {
    void cellMapper(Cell cell, Object obj, ColumnMap colMap);
}
