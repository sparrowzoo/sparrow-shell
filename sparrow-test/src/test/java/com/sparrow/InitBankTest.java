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

package com.sparrow;

import com.sparrow.utility.FileUtility;
import java.util.List;

public class InitBankTest {
    public static void main(String[] args) {
        List<String> banks = FileUtility.getInstance().readLines("/bank.txt");
        for (String bank : banks) {
            String[] bankArray = bank.split(" ");
            String bankCodeType = bankArray[0];
            String bankCodeTypeArray[]=bankCodeType.split("_");
            String bankCode=bankCodeTypeArray[0];
            String bankType=bankCodeTypeArray[1];


            String bankName = bankArray[bankArray.length - 1];
            bankName=bankName.replace("(借记卡)","");
            bankName=bankName.replace("(信用卡)","");
//            System.out.println(bankCode);
//            System.out.println(bankType);
//            System.out.print(bankName);

            System.out.printf("insert into bank(`code`,`name`,`bank_type`) values('%1$s','%2$s','%3$s');%n",bankCode,bankName,bankType);
        }
    }
}
