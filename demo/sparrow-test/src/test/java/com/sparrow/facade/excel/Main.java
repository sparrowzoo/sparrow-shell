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

package com.sparrow.facade.excel;

import com.sparrow.utility.FileUtility;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by harry on 2015/6/5.
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.debug("debug");
        InputStream inputStream = new FileInputStream("d:\\template_date.xls");
        Workbook workbook = getWorkbook(inputStream, "template.xls");

        for (int row = 0; row <= workbook.getSheetAt(0).getLastRowNum(); row++) {
            Row excelRow = workbook.getSheetAt(0).getRow(row);
            for (short column = 1; column < excelRow.getLastCellNum(); column++) {
                Cell cell = excelRow.getCell(column);
                if (cell != null) {
                    //设置背景色
                    if (column > 0) {
                        Font okFont = workbook.createFont();
                        // 将错误的cell清空
                        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
                        style.setDataFormat(cell.getCellStyle().getDataFormat());
                        Font font = okFont;
                        font.setColor(IndexedColors.BLACK.getIndex());
                        font.setFontHeightInPoints((short) 12);
                        style.setFont(font);
                        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        //style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        cell.setCellStyle(style);

/*
                        CellStyle redStyle = cell.getSheet().getWorkbook().createCellStyle();
                        redStyle.setDataFormat(cell.getCellStyle().getDataFormat());
                        Font redFont = workbook.createFont();
                        redFont.setFontHeightInPoints((short) 12);
                        redFont.setColor(IndexedColors.WHITE.index);
                        redStyle.setFont(redFont);
                        redStyle.setFillForegroundColor(column);
                        redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                        cell.setCellStyle(redStyle);
*/

                    }
                }
            }
        }
        File file = new File("d:\\template2.xls");
        workbook.write(new FileOutputStream(file));
    }

    public static Workbook getWorkbook(InputStream fin, String fileName) throws IOException {
        Workbook workbook = null;
        String extension = FileUtility.getInstance().getFileNameProperty(fileName).getExtension();
        if (".xls".equals(extension)) {
            workbook = new HSSFWorkbook(fin);
        } else if (".xlsx".equals(extension)) {
            workbook = new XSSFWorkbook(fin);
        }
        return workbook;
    }
}
