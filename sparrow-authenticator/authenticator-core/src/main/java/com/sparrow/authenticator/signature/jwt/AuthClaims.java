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

package com.sparrow.authenticator.signature.jwt;

import io.jsonwebtoken.Claims;

import java.util.*;

public class AuthClaims extends HashMap<String, Object> implements Claims {

    /**
     * 表示JWT的签发者，即标识谁创建了该JWT
     * 用户ID即可
     */
    @Override
    public String getIssuer() {
        Object issuer = super.get(Claims.ISSUER);
        return issuer == null ? null : issuer.toString();
    }

    @Override
    public Claims setIssuer(String iss) {
        super.put(Claims.ISSUER, iss);
        return this;
    }

    /**
     * 表示JWT的实体，即标识该JWT所代表的用户或实体。
     * 与issuer 相同即可
     */
    @Override
    public String getSubject() {
        Object subject = this.get(Claims.SUBJECT);
        return subject == null ? null : subject.toString();
    }

    @Override
    public Claims setSubject(String sub) {
        super.put(Claims.SUBJECT, sub);
        return this;
    }

    /**
     * 表示JWT的受众，即标识预期的接收者
     * 所有人,可不添
     */
    @Override
    public String getAudience() {
        Object audience = super.get(Claims.AUDIENCE);
        return audience == null ? null : audience.toString();
    }

    @Override
    public Claims setAudience(String aud) {
        super.put(Claims.AUDIENCE, aud);
        return this;
    }

    /**
     * 表示JWT的过期时间，即标识JWT的有效期限。在该时间之后，该JWT将不再被接受
     * 为了实现续期，可设置逻辑过期时间，最终以数据库为准
     */
    @Override
    public Date getExpiration() {
        Object exp = super.get(Claims.EXPIRATION);
        return exp == null ? null : (Date) exp;
    }

    @Override
    public Claims setExpiration(Date exp) {
        super.put(Claims.EXPIRATION, exp);
        return this;
    }

    /**
     * 表示JWT的生效时间，即在该时间之前，该JWT将不会被接受
     * 一般即时生效，默认0
     */

    @Override
    public Date getNotBefore() {
        Object nf = super.get(Claims.NOT_BEFORE);
        return nf == null ? null : (Date) nf;
    }

    @Override
    public Claims setNotBefore(Date nbf) {
        super.put(Claims.NOT_BEFORE, nbf);
        return this;
    }

    /**
     * 表示JWT的签发时间，即标识JWT的创建时间
     */
    @Override
    public Date getIssuedAt() {
        Object issuedAt = super.get(Claims.ISSUED_AT);
        return issuedAt == null ? null : (Date) issuedAt;
    }

    @Override
    public Claims setIssuedAt(Date iat) {
        super.put(Claims.ISSUED_AT, iat);
        return this;
    }


    /**
     * 表示JWT的唯一标识符，用于防止JWT重放攻击
     */
    @Override
    public String getId() {
        Object id = super.get(Claims.ID);
        return id == null ? null : id.toString();
    }

    @Override
    public Claims setId(String jti) {
        super.put(Claims.ID, jti);
        return this;
    }

    @Override
    public <T> T get(String claimName, Class<T> requiredType) {
        Object o = super.get(claimName);
        if (o == null) {
            return null;
        }
        if (o.getClass().isAssignableFrom(requiredType)) {
            return (T) o;
        }
        throw new ClassCastException("class not cast required type " + requiredType + " get class is " + o.getClass());
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<Object> values() {
        return super.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return super.entrySet();
    }
}
