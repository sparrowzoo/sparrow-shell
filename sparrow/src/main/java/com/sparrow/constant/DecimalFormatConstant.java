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

package com.sparrow.constant;

public class DecimalFormatConstant {

    /**
     * Decimal to fill 0 with 2 scales: 123,234.20, 123,234.00, 123,234.12
     */

    public static final String WITH_2_SCALES_AND_COMMA_FILL_0 = ",###,##0.00";

    /**
     * Decimal to without 0 in end: 123,234.2, 123,234, 123,234.12
     */
    public static final String WITH_2_SCALES_AND_COMMA_WITHOUT_0 = ",###,##0.##";

    /**
     * Decimal to fill 0 and no comma: 123434.00, 12345.50, 12343442.23
     */
    public static final String WITH_2_SCALES_FILL_0_WITHOUT_COMMA = "###0.00";

    /**
     * Decimal to without 0 in end and no comma: 123434, 12345.5, 12343442.23
     */
    public static final String WITH_2_SCALES_WITHOUT_0_AND_COMMA = "###0.##";

    public static final String PERCENT = "###0.##%";
}
