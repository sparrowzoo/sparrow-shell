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

package com.sparrow.protocol;


import java.util.Iterator;
import java.util.ServiceLoader;

public class ResultI18nMessageAssemblerProvider {
    private static final String DEFAULT_PROVIDER = "com.sparrow.support.web.DefaultResultI18nMessageAssembler";
    private volatile static ResultI18nMessageAssembler resultI18nMessageAssembler;

    public static ResultI18nMessageAssembler getProvider() {
        if (resultI18nMessageAssembler != null) {
            return resultI18nMessageAssembler;
        }
        synchronized (ResultI18nMessageAssembler.class) {
            if (resultI18nMessageAssembler != null) {
                return resultI18nMessageAssembler;
            }

            ServiceLoader<ResultI18nMessageAssembler> loader = ServiceLoader.load(ResultI18nMessageAssembler.class);
            Iterator<ResultI18nMessageAssembler> it = loader.iterator();
            if (it.hasNext()) {
                resultI18nMessageAssembler = it.next();
                return resultI18nMessageAssembler;
            }

            try {
                Class<?> jsonClazz = Class.forName(DEFAULT_PROVIDER);
                resultI18nMessageAssembler = (ResultI18nMessageAssembler) jsonClazz.newInstance();
                return resultI18nMessageAssembler;
            } catch (Exception x) {
                throw new RuntimeException(
                        "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
                        x);
            }
        }
    }
}
