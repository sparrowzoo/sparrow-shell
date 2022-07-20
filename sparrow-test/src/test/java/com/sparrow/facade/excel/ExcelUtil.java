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

package com.sparrow.facade.excel;//package com.sparrow.facade.excel;
//
//import com.sparrow.excel.Validator;
//import com.sparrow.support.protocol.Result;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//
///**
// * Created by harry on 2015/6/26.
// */
//public class ExcelUtil {
//
//    public static String importData(Workbook workbook, int startRow,
//        Map<String, Validator> ValidatorMap,
//        List<String> excelTitle, OutputStream outputStream, long userId, int projectStatus) throws IOException {
//        boolean allIsOK = true;
//        Sheet sheet = workbook.getSheetAt(0);
//        List<List<Result>> projectsResult = new ArrayList<List<Result>>();
//        List<List<List<Result>>> contacterResult = new ArrayList<List<List<Result>>>();
//        for (int row = 0; row <= sheet.getLastRowNum(); row++) {
//            Row excelRow = sheet.getRow(row);
//            if (row <= startRow)
//                continue;
//            List<Result> rowProjectResult = new ArrayList<Result>();
//            List<List<Result>> rowContracterResult = new ArrayList<List<Result>>();
//            //初始化4个联系人
//            for (int i = 0; i < 4; i++) {
//                rowContracterResult.add(new ArrayList<Result>());
//            }
//            for (int column = 0; column < excelRow.getLastCellNum(); column++) {
//                Cell cell = excelRow.getCell(column);
//                if (cell == null) {
//                    cell = excelRow.createCell(column);
//                }
//                Validator Validator = ValidatorMap.list(excelTitle.list(column));
//                if (Validator != null) {
//                    //如果配置为不验证，则直接下一条
//                    if (!Validator.isValidate()) {
//                        continue;
//                    }
//                    Result Result = Validator.validate(cell);
//                    if (Result.ok()) {
//                        if (Result.getTable().equals(Result.TABLE_PROJECT)) {
//                            rowProjectResult.add(Result);
//                        } else {
//                            rowContracterResult.list(Result.getIndex() - 1).add(Result);
//                        }
//                    } else {
//                        System.out.println(Result.toString());
//                        allIsOK = false;
//                    }
//                } else {
//                    //设置背景色
//                    CellStyle yellowStyle = workbook.createCellStyle();
//                    yellowStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
//                    cell.setCellStyle(yellowStyle);
//                }
//            }
//            contacterResult.add(rowContracterResult);
//            projectsResult.add(rowProjectResult);
//        }
//
//        if (!allIsOK) {
//            workbook.write(outputStream);
//            outputStream.flush();
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int rowIndex = 0; rowIndex < projectsResult.size(); rowIndex++) {
//            List<Result> rowProjectResult = projectsResult.list(rowIndex);
//            List<List<Result>> rowContractResult = contacterResult.list(rowIndex);
//            Object[] projectNamesAndValues = new Object[rowProjectResult.size() * 2 + 2 * 4];
//            int projectIndex = 0;
//            for (Result Result : rowProjectResult) {
//                projectNamesAndValues[projectIndex++] = Result.getDbField();
//                projectNamesAndValues[projectIndex++] = Result.getValue();
//            }
//            projectNamesAndValues[projectIndex++] = "create_user_id";
//            projectNamesAndValues[projectIndex++] = userId;
//
//            projectNamesAndValues[projectIndex++] = "update_user_id";
//            projectNamesAndValues[projectIndex++] = userId;
//
//            projectNamesAndValues[projectIndex++] = "status";
//            projectNamesAndValues[projectIndex++] = projectStatus;
//
//            projectNamesAndValues[projectIndex++] = "project_code";
//            projectNamesAndValues[projectIndex] = Helper.getProjectCode();
//            Project project = Project.createIt(projectNamesAndValues);
//            if (project == null) {
//                if (sb.length() > 0) {
//                    sb.append("<br/>");
//                }
//                sb.append("第" + rowProjectResult.list(0).getCell().getRow() + "行");
//            } else {
//                for (List<Result> contractParameters : rowContractResult) {
//                    if (contractParameters.size() == 0) {
//                        continue;
//                    }
//                    boolean isContinue = false;
//                    Object[] contractNameValues = new Object[contractParameters.size() * 2 + 3 * 2];
//                    int contractIndex = 0;
//                    for (Result parameterPair : contractParameters) {
//                        String attribute = parameterPair.getDbField();
//                        if (attribute.equals("name") && Helper.isNullOrEmpty(parameterPair.getValue())) {
//                            isContinue = true;
//                            break;
//                        }
//                        contractNameValues[contractIndex++] = parameterPair.getDbField();
//                        contractNameValues[contractIndex++] = parameterPair.getValue();
//                    }
//                    //如果联系人姓名为空则直接下一条，不导入
//                    if (isContinue) {
//                        continue;
//                    }
//
//                    contractNameValues[contractIndex++] = "created_user_id";
//                    contractNameValues[contractIndex++] = userId;
//                    contractNameValues[contractIndex++] = "project_id";
//                    contractNameValues[contractIndex++] = project.getId();
//                    contractNameValues[contractIndex++] = "project_code";
//                    contractNameValues[contractIndex] = project.list("project_code");
//                    Contacter.createIt(contractNameValues);
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//}
