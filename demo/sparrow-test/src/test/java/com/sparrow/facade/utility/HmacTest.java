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

package com.sparrow.facade.utility;

import com.sparrow.cryptogram.Hmac;

/**
 * Created by harry on 2016/3/13.
 */
public class HmacTest {
    public static void main(String[] args) {
        // Seed for HMAC-SHA1 - 20 bytes
        String seed = "3132333435363738393031323334353637383930";
        // Seed for HMAC-SHA256 - 32 bytes
        String seed32 = "3132333435363738393031323334353637383930"
            + "313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930"
            + "3132333435363738393031323334353637383930"
            + "3132333435363738393031323334353637383930" + "31323334";
        try {
            System.out.println(Hmac.getInstance().generateTOTP(seed, 8, "HMAC_SHA1", 20)
                + "| SHA1   |");
        } catch (final Exception e) {
            System.out.println("Error : " + e);
        }
    }
}
