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

package com.sparrow.support.mobile;

import com.sparrow.core.Pair;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.MobileShortMessaging;

public interface ShortMessageService {

    /**
     * init send short message base config
     *
     * @param mobile
     * @return
     */
    MobileShortMessaging.Builder init(String mobile);

    /**
     * build validate code
     * <p>
     * e.g
     * <p>
     * validate code
     * <p>
     * send time
     * <p>
     * content
     *
     * @param builder
     * @return
     */
    MobileShortMessaging.Builder validateCode(MobileShortMessaging.Builder builder);

    /**
     * send short message
     *
     * @param shortMessaging
     * @return
     * @throws BusinessException
     */
    Boolean send(MobileShortMessaging shortMessaging) throws BusinessException;

    /**
     * check validateCode is correct?
     *
     * @param validateCode
     * @param shortMessaging
     * @return
     * @throws BusinessException
     */
    Boolean validate(String validateCode, MobileShortMessaging shortMessaging) throws BusinessException;

    /**
     * secret mobile
     *
     * @param mobile mobile no
     * @return 135****1111 ****  secret
     */
    Pair<String, String> secretMobile(String mobile);
}
