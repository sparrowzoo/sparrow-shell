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

import com.sparrow.constant.Regex;
import com.sparrow.protocol.constant.magic.Digit;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.enums.DataType;
import com.sparrow.excel.exception.TemplateCellValidateException;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

public class Validator {
    public static final String VALIDATOR = "validator";
    public static final String CELL = "cell";

    /**
     * excel 表格的索引
     */
    private Integer sheetIndex;
    /**
     * 所属表名
     */
    private String table;
    /**
     * 所属字段
     */
    private String dbField;
    /**
     * 对应excel title
     */
    private String excelTitle;
    /**
     * 是否需要验证
     */
    private boolean validate;

    /**
     * 类型
     */
    private String type;
    /**
     * 是否允许为空
     */
    private boolean nullable;
    /**
     * 验证正则
     */
    private String regex;
    /**
     * 第n条记录
     */
    private int recordIndex;

    /**
     * 选项
     */
    private Map<String, String> optionMap;

    /**
     * 验证excel上传数据
     *
     * @param cell 当前数据的cell
     * @return
     */
    public CellValue validate(Cell cell) throws TemplateCellValidateException {
        //一个cell不允许有多个 font 对象
        DataType dataType = DataType.valueOf(this.getType());
        String content = ExcelUtility.readCell(cell, dataType).trim();

        //如果不允许空但内容为空则报必填异常
        if (!this.isNullable() && (StringUtility.isNullOrEmpty(content) || "必填".equals(content))) {
            cell.setCellValue("必填");
            throw new TemplateCellValidateException("必填");
        }

        //如果允许空但内容为空则直接返回null
        if (this.isNullable() && StringUtility.isNullOrEmpty(content)) {
            return new CellValue(dataType, null, this.excelTitle);
        }
        try {

            //以下非空
            switch (dataType) {
                case STRING:
                case OPTION:
                    if (dataType == DataType.OPTION) {
                        Map<String, String> map = this.getOption();
                        if (map != null) {
                            String option = null;
                            if (!StringUtility.isNullOrEmpty(content)) {
                                option = map.get(content);
                                if (option != null) {
                                    return new CellValue(dataType, option, this.excelTitle);
                                }
                                throw new TemplateCellValidateException(content + "输入项不存在");
                            }
                        }
                    } else if (!StringUtility.isNullOrEmpty(this.regex)) {
                        if (!Pattern.compile(regex, Regex.OPTION).matcher(content).find()) {
                            throw new TemplateCellValidateException(content + "输入格式不正确," + this.regex);
                        }
                    }
                    return new CellValue(dataType, content, this.excelTitle);
                case DATE:
                    Date date = cell.getDateCellValue();
                    return new CellValue(dataType, date, this.excelTitle);
                case DECIMAL:
                    Double v;
                    if (content.endsWith(Symbol.PERCENT)) {
                        v = Double.valueOf(content.substring(0, content.length() - 1)) / 100d;
                    } else {
                        v = Double.valueOf(content);
                    }
                    return new CellValue(dataType, v, this.excelTitle);
                case INT:
                    int iValue = Integer.valueOf(content);
                    return new CellValue(dataType, iValue, this.excelTitle);
                case BOOLEAN:
                    boolean bValue = false;
                    if (!StringUtility.isNullOrEmpty(content)) {
                        bValue = Boolean.valueOf(content);
                    }
                    return new CellValue(dataType, bValue, this.excelTitle);
                default:
            }
        } catch (Exception e) {
            throw new TemplateCellValidateException(e);
        }
        CellStyle redStyle = ExcelUtility.style(cell, IndexedColors.WHITE.getIndex(), IndexedColors.RED.getIndex());
        cell.setCellStyle(redStyle);
        throw new TemplateCellValidateException(content);
    }

    /**
     * 字段 excel field 映射格式 dbfield,description,validate,INT,nullable,regex
     *
     * @param line
     */
    public Validator(String line) {

        String[] metadataArray = line.split(Symbol.COMMA);
        if (metadataArray.length < Digit.SIX) {
            throw new RuntimeException("length of fields must be great equal 6");
        }

        if (metadataArray[Digit.ONE].contains(Symbol.DOLLAR)) {
            String[] array = metadataArray[Digit.ONE].split("\\$");
            this.table = array[Digit.ZERO];
            this.dbField = array[Digit.ONE];
            if (array.length > Digit.TOW) {
                //contacter$name$2
                this.recordIndex = Integer.valueOf(array[Digit.TOW]);
            }
        } else {
            this.dbField = metadataArray[Digit.ONE].trim();
        }

        this.excelTitle = metadataArray[Digit.TOW].trim();
        this.validate = Boolean.valueOf(metadataArray[Digit.THREE]);
        this.type = metadataArray[Digit.FOUR].trim();
        this.nullable = Boolean.valueOf(metadataArray[Digit.FIVE].trim());

        if (metadataArray.length >= Digit.SEVEN) {
            int index = line.indexOf("^");
            if ("OPTION".equalsIgnoreCase(this.type)) {
                index += Digit.ONE;
            }
            this.regex = line.substring(index);
        }
    }

    public String getType() {
        return type;
    }

    public String getDbField() {
        return dbField;
    }

    public String getExcelTitle() {
        return excelTitle;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isValidate() {
        return validate;
    }

    public String getTable() {
        return table;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    /**
     * 获取选项列表 例：男:1|女:0
     *
     * @return
     */
    public Map<String, String> getOption() {
        if (this.optionMap != null) {
            return this.optionMap;
        }
        if (DataType.valueOf(this.getType()) == DataType.OPTION) {
            String[] options = this.regex.split("\\|");
            Map<String, String> map = new HashMap<String, String>();
            for (String option : options) {
                String[] optionArray = option.split(Symbol.COLON);
                map.put(optionArray[Digit.ZERO].trim(), optionArray[Digit.ONE]);
            }
            this.optionMap = map;
        }
        return this.optionMap;
    }

    public void setOption(Map<String, String> optionMap) {
        this.optionMap = optionMap;
    }

    public static Map<String, Validator> init(InputStream inputStream) {
        return init(inputStream, 1);
    }

    public static Map<String, Validator> init(InputStream inputStream, Integer sheetIndex) {
        Map<String, Validator> validatorMap = new HashMap<String, Validator>();
        List<String> lines = FileUtility.getInstance().readLines(inputStream);
        for (String line : lines) {
            if (line.trim().length() > Digit.ZERO) {
                String[] metadataArray = line.split(Symbol.COMMA);
                Integer configSheetIndex = Integer.valueOf(metadataArray[Digit.ZERO]);
                if (!sheetIndex.equals(configSheetIndex)) {
                    continue;
                }
                Validator validator = new Validator(line);
                validatorMap.put(validator.getExcelTitle(), validator);
            }
        }
        return validatorMap;
    }
}
