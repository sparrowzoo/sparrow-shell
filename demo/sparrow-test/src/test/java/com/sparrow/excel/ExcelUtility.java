/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.excel;

import com.sparrow.enums.DataType;
import com.sparrow.excel.exception.TemplateCellValidateException;
import com.sparrow.excel.exception.TemplateFieldNotMatchException;
import com.sparrow.excel.exception.TemplateFileException;
import com.sparrow.excel.exception.TemplateValidateException;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.utility.FileUtility;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtility {
    public static Workbook getWorkbook(String fullFileName) throws TemplateFileException {
        File file = new File(fullFileName);
        InputStream fin;
        if (!file.exists()) {
            fin = ExcelUtility.class.getResourceAsStream(fullFileName);
        } else {
            try {
                fin = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new TemplateFileException(e);
            }
        }

        Workbook workbook = null;
        String extension = FileUtility.getInstance().getFileNameProperty(fullFileName).getExtension();
        if (Extension.EXCEL2003.equals(extension)) {
            try {
                workbook = new HSSFWorkbook(fin);
            } catch (IOException e) {
                throw new TemplateFileException(e);
            }
        } else if (Extension.EXCEL2007.equals(extension)) {
            try {
                workbook = new XSSFWorkbook(fin);
            } catch (IOException e) {
                throw new TemplateFileException(e);
            }
        }
        return workbook;
    }

    public static List<String> readLines(Sheet sheet, int row) {
        Row excelRow = sheet.getRow(row);
        List<String> line = new ArrayList<String>();
        for (int i = 0; i < excelRow.getLastCellNum(); i++) {
            Cell cell = excelRow.getCell(i);
            if (cell != null) {
                line.add(readCell(cell, DataType.STRING));
            } else {
                line.add("");
            }
        }
        return line;
    }

    public static String readCell(Cell cell, DataType dataType) {
        switch (cell.getCellType()) {
            case  Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DataType.STRING == dataType) {
                    return cell.getStringCellValue();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
        }
        return "";
    }

    public static List<List<CellValue>> validate(Workbook workbook, Map<String, Validator> validatorMap,
        List<String> excelTitle, int sheetIndex,
        int startRow) throws TemplateFileException, TemplateFieldNotMatchException, TemplateValidateException {
        if (workbook == null) {
            throw new TemplateFileException("excel template error");
        }

        StringBuilder notMatchFieldList = new StringBuilder();
        for (String title : validatorMap.keySet()) {
            //不需要验证
            if (!validatorMap.get(title.trim()).isValidate()) {
                continue;
            }
            //包含在excel中则下一个title
            if (excelTitle.contains(title.trim())) {
                continue;
            }
            if (notMatchFieldList.length() > 0) {
                notMatchFieldList.append("、");
            }
            notMatchFieldList.append(title.trim());
        }
        if (notMatchFieldList.length() > 0) {
            throw new TemplateFieldNotMatchException(notMatchFieldList.toString());
        }

        StringBuilder templateError = new StringBuilder();

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        List<List<CellValue>> validateResultTable = new ArrayList<List<CellValue>>();
        for (int row = 0; row <= sheet.getLastRowNum(); row++) {
            Row excelRow = sheet.getRow(row);
            if (row < startRow) {
                continue;
            }
            List<CellValue> validateResultRow = new ArrayList<CellValue>();
            for (int column = 0; column < excelRow.getLastCellNum(); column++) {
                Cell cell = excelRow.getCell(column);
                if (cell == null) {
                    cell = excelRow.createCell(column);
                }
                Validator fieldValidator = validatorMap.get(excelTitle.get(column));
                if (fieldValidator != null) {
                    //如果配置为不验证，则直接下一条
                    if (!fieldValidator.isValidate()) {
                        continue;
                    }
                    try {
                        CellValue validateResult = fieldValidator.validate(cell);
                        validateResultRow.add(validateResult);
                    } catch (TemplateCellValidateException e) {
                        if (templateError.length() > 0) {
                            templateError.append("＼n");
                        }
                        templateError.append(e.getMessage());
                    }
                } else {
                    //设置背景色
                    CellStyle yellowStyle = workbook.createCellStyle();
                    yellowStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                    cell.setCellStyle(yellowStyle);
                }
            }
            validateResultTable.add(validateResultRow);
        }
        if (templateError.length() == 0) {
            return validateResultTable;
        }
        throw new TemplateValidateException(templateError.toString());
    }

    public static CellStyle style(Cell cell, short color, short background) {
        if (cell == null) {
            return null;
        }
        //如果用cell.getCellStyle() 则会把整个sheet  样式改变
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        CellStyle currentStyle = cell.getCellStyle();
        style.setDataFormat(currentStyle.getDataFormat());
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setColor(color);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(background);
        if (background != IndexedColors.WHITE.getIndex()) {
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return style;
    }
}
