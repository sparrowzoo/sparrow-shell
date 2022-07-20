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

import com.sparrow.excel.CellValue;
import com.sparrow.excel.ExcelUtility;
import com.sparrow.excel.Validator;
import com.sparrow.excel.exception.TemplateFieldNotMatchException;
import com.sparrow.excel.exception.TemplateFileException;
import com.sparrow.excel.exception.TemplateValidateException;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 2015/7/7.
 */
public class ExcelValidator {
    public static void main(String[] args) {
        Integer innerSheet = 0;
        Integer outterSheet = 1;
        //Workbook workbook = ExcelUtility.getWorkbook(com.sparrow.facade.excel.ExcelValidator.class.getResource("template.xls"));
        Workbook workbook = null;
        try {
            workbook = ExcelUtility.getWorkbook("/Users/harry/download_template.xlsx");
        } catch (TemplateFileException e) {
            System.out.println(e.toString());
        }
        //文件的title
        List<String> excelTitle = null;
        if (workbook != null) {
            excelTitle = ExcelUtility.readLines(workbook.getSheetAt(innerSheet), 0);
        }
        //初始化字段 名称映射文件
        Map<String, Validator> validatorMap = Validator.init(ExcelValidator.class.getResourceAsStream("passenger_field_title_mapping.txt"), innerSheet);
        for (String key : validatorMap.keySet()) {
            Validator validator = validatorMap.get(key);
            if (validator.getDbField().equals("nationality")) {
                Map<String, String> options = new HashMap<String, String>();
                options.put("澳大利亚", "001");
                validator.setOption(options);
            }
        }
        //验证
        List<List<CellValue>> r = null;
        try {
            r = ExcelUtility.validate(workbook, validatorMap, excelTitle, innerSheet, 1);
            for (List<CellValue> rowResult : r) {
                for (CellValue cell : rowResult) {
                    Validator validator = validatorMap.get(cell.getExcelTitle());
                    System.out.println(cell.getValue() + "|" + validator.getTable() + "|" + validator.getDbField() + "|" + validator.getRecordIndex());
                }
            }
        } catch (TemplateFileException e) {
            e.printStackTrace();
        } catch (TemplateFieldNotMatchException e) {
            e.printStackTrace();
        } catch (TemplateValidateException e) {
            e.printStackTrace();
        }
    }
}
