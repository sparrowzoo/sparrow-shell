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

package com.sparrow.utility;

import com.sparrow.constant.DecimalFormatConstant;
import com.sparrow.protocol.constant.magic.CAPITAL_RMB;
import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 0 一个数字 # 一个数字，不包括 0 . 小数的分隔符的占位符 , 分组分隔符的占位符 ; 分隔格式。 - 缺省负数前缀。 % 乘以 100 和作为百分比显示 ? 乘以 1000
 * 和作为千进制货币符显示；用货币符号代替；如果双写，用国际货币符号代替。如果出现在一个模式中，用货币十进制分隔符代替十进制分隔符。 X
 * 前缀或后缀中使用的任何其它字符，用来引用前缀或后缀中的特殊字符。
 *
 * @author harry
 */

public class DecimalUtility {

  public static String format(BigDecimal decimal) {
    DecimalFormat df = new DecimalFormat(DecimalFormatConstant.WITH_2_SCALES_AND_COMMA_FILL_0);
    return df.format(decimal);
  }

  public static String format(BigDecimal decimal, String format) {
    DecimalFormat df = new DecimalFormat(format);
    return df.format(decimal);
  }

  /**
   * big decimal convert chinese.
   *
   * @return string
   */
  public static String toChineseString(BigDecimal num) {
    if (null == num) {
      return SYMBOL.EMPTY;
    }
    //0-9所对应的汉字
    String strNumberChinese = "零壹贰叁肆伍陆柒捌玖";

    //从原num值中取出的值
    String strBit = "";
    //数字的字符串形式
    String strNumbers = "";
    //人民币大写金额形式
    String strChinese = "";
    //num的值乘以100的字符串长度
    int j;
    //数字的汉语读法
    String ch1 = "";
    //数字位的汉字读法
    String ch2 = "";
    //用来计算连续的零值是几个
    int nzero = 0;
    //从原num值中取出的值
    int temp;
    //将num取绝对值并四舍五入取2位小数
    num = num.abs().setScale(2, BigDecimal.ROUND_HALF_UP);
    strNumbers = new DecimalFormat("#0")
        .format(num.multiply(new BigDecimal("100"))); //将num乘100并转换成字符串形式
    j = strNumbers.length(); //找出最高位
    if (j > 15) {
      return "";
    }
    //数字位所对应的汉字
    String strWeightChinese = "万仟佰拾亿仟佰拾万仟佰拾元角分";
    strWeightChinese = strWeightChinese.substring(15 - j); //取出对应位数的str2的值。如：200.55,j为5所以str2=佰拾元角分

    //循环取出每一位需要转换的值
    for (int i = 0; i < j; i++) {
      strBit = strNumbers.substring(i, i + 1); //取出需转换的某一位的值
      temp = Integer.parseInt(strBit); //转换为数字
      if (i != (j - 3) && i != (j - 7) && i != (j - 11) && i != (j - 15)) {
        //当所取位数不为元、万、亿、万亿上的数字时
        if (String.valueOf(DIGIT.ZERO).equals(strBit)) {
          ch1 = "";
          ch2 = "";
          nzero = nzero + 1;
        } else {
          if (!String.valueOf(DIGIT.ZERO).equals(strBit) && nzero != 0) {
            ch1 = CAPITAL_RMB.ZERO + strNumberChinese.substring(temp, temp + 1);
            ch2 = strWeightChinese.substring(i, i + 1);
            nzero = 0;
          } else {
            ch1 = strNumberChinese.substring(temp, temp + 1);
            ch2 = strWeightChinese.substring(i, i + 1);
            nzero = 0;
          }
        }
      } else {
        //该位是万亿，亿，万，元位等关键位
        if (!SYMBOL.ZERO.equals(strBit) && nzero != 0) {
          ch1 = CAPITAL_RMB.ZERO + strNumberChinese.substring(temp * 1, temp * 1 + 1);
          ch2 = strWeightChinese.substring(i, i + 1);
          nzero = 0;
        } else {
          if (!SYMBOL.ZERO.equals(strBit) && nzero == 0) {
            ch1 = strNumberChinese.substring(temp * 1, temp * 1 + 1);
            ch2 = strWeightChinese.substring(i, i + 1);
            nzero = 0;
          } else {
            if (!SYMBOL.ZERO.equals(strBit) && nzero >= 3) {
              ch1 = "";
              ch2 = "";
              nzero = nzero + 1;
            } else {
              if (j >= 11) {
                ch1 = "";
                nzero = nzero + 1;
              } else {
                ch1 = "";
                ch2 = strWeightChinese.substring(i, i + 1);
                nzero = nzero + 1;
              }
            }
          }
        }
      }
      if (i == (j - 11) || i == (j - 3)) {
        //如果该位是亿位或元位，则必须写上
        ch2 = strWeightChinese.substring(i, i + 1);
      }
      strChinese = strChinese + ch1 + ch2;

      if (i == j - 1 && SYMBOL.ZERO.equals(strBit)) {
        //最后一位（分）为0时，加上"整"
        strChinese = strChinese + CAPITAL_RMB.ZHENG;
      }
    }
    if (num.compareTo(BigDecimal.ZERO) == 0) {
      strChinese = "零元整";
    }
    return strChinese;
  }
}
