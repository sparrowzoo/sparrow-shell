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

package com.sparrow.cache;

import com.sparrow.protocol.ModuleSupport;
import com.sparrow.protocol.constant.magic.Symbol;
import java.util.Arrays;
import java.util.Collections;

public class Key {
    private String business;
    private Object businessId;
    private String module;

    private Key() {
    }

    private Key(Builder builder) {
        this.business = builder.business.getKey();
        this.module = builder.business.getModule();
        if (builder.businessId != null) {
            this.businessId = this.join(Arrays.asList(builder.businessId), Symbol.COLON);
        }
    }

    /**
     * module:business.b2:businessId.id2.id3 user:register.times.validate:{userId}:{k2}
     *
     * @param key
     * @return
     */
    public static Key parse(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        Key k = new Key();
        String[] keyArray = key.split(Symbol.COLON);
        k.module = keyArray[0];
        k.business = keyArray[1];
        if (keyArray.length > 2) {
            k.businessId = keyArray[2];
        }
        return k;
    }

    public String key() {
        if (this.businessId == null) {
            return (this.module + Symbol.COLON + this.business).toLowerCase();
        }
        return (this.module + Symbol.COLON + this.business + Symbol.COLON + this.businessId).toLowerCase();
    }

    private static String join(Iterable<?> collection, String joinChar) {
        StringBuilder sb = new StringBuilder();
        for (Object object : collection) {
            if (object == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(joinChar);
            }
            sb.append(object.toString().trim());
        }
        return sb.toString();
    }

    public String getBusiness() {
        return business;
    }

    public String getModule() {
        return module;
    }

    public static class Business {
        private String module;
        private StringBuilder key = new StringBuilder();

        public Business(ModuleSupport module, String... business) {
            this.module = module.name();
            if (business != null && business.length > 0) {
                if (this.key.length() > 0) {
                    this.key.append(Symbol.COLON);
                }
                this.key.append(join(Collections.singletonList(business), Symbol.DOT));
            }
        }

        public Business append(String... business) {
            if (business == null || business.length == 0) {
                return this;
            }
            this.key.append(Symbol.DOT);
            this.key.append(join(Arrays.asList(business), Symbol.DOT));
            return this;
        }

        public String getKey() {
            return key.toString();
        }

        public String getModule() {
            return module;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Business business = (Business) o;

            if (module != null ? !module.equals(business.module) : business.module != null)
                return false;
            return key != null ? key.equals(business.key) : business.key == null;

        }

        @Override
        public int hashCode() {
            int result = module != null ? module.hashCode() : 0;
            result = 31 * result + (key != null ? key.hashCode() : 0);
            return result;
        }
    }

    public static class Builder {
        private Business business;
        private Object[] businessId;

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder businessId(Object... businessId) {
            this.businessId = businessId;
            return this;
        }

        public Key build() {
            return new Key(this);
        }
    }
}
