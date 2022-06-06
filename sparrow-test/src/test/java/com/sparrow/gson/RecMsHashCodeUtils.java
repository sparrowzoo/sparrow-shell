package com.sparrow.gson;

import com.google.common.base.Objects;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.missfresh.algo.common.api.entity.RecoRequest;

/**
 * Pack:       cn.missfresh.rec.common.util
 * File:       RecoHashCodeUtils
 * Desc:
 * User:       jianglz@missfresh.cn
 * CreateTime: 2019-09-20 12:28
 */
public class RecMsHashCodeUtils {

    private static ExclusionStrategy recoExclusionStrategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return "requestId".equals(fieldAttributes.getName()) //
                    || "offset".equals(fieldAttributes.getName())//
                    || "flagMap".equals(fieldAttributes.getName()); //
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    };

    public static int reflectionHashCode(RecoRequest2 request, String excludeField) throws RuntimeException {
        GsonBuilder gson = new GsonBuilder().setExclusionStrategies(recoExclusionStrategy);

        String code = gson.create().toJson(request);

        return Objects.hashCode(code);

    }

    public static int reflectionHashCode(RecoRequest request, String excludeField) throws RuntimeException {
        GsonBuilder gson = new GsonBuilder().setExclusionStrategies(recoExclusionStrategy);

        String code = gson.create().toJson(request);

        return Objects.hashCode(code);

    }

    public static void main(String[] args) {
        System.out.println(reflectionHashCode(new RecoRequest(),""));
        System.out.println(reflectionHashCode(new RecoRequest2(),""));

    }
}
