package com.sparrow.cg.impl;

import com.sparrow.cg.Generator4MethodAccessor;
import com.sparrow.cg.MethodAccessor;
import com.sparrow.constant.Pager;
import com.sparrow.constant.User;
import com.sparrow.enums.UserType;
import com.sparrow.protocol.dto.AuthorDTO;
import com.sparrow.protocol.pager.PagerResultWithDictionary;

import java.io.PipedReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ReflectGenerator4MechodAccessorImpl implements Generator4MethodAccessor {

    @Override
    public MethodAccessor newMethodAccessor(Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> cache = new HashMap<>(methods.length*4/3);
        for (Method method : methods) {
            method.setAccessible(true);
            final String methodNameLowCase = method.getName();
            cache.put(methodNameLowCase,method);
        }
        return new ReflectMethodAccessor(cache);
    }

    private static class ReflectMethodAccessor implements MethodAccessor {

        private final Map<String, Method> METHOD_CACHE;

        public ReflectMethodAccessor(Map<String, Method> METHOD_CACHE) {
            this.METHOD_CACHE = METHOD_CACHE;
        }

        @Override
        public Object get(Object o, String methodName) {
            return invoke(o,methodName);
        }

        @Override
        public void set(Object o, String methodName, Object arg) {
            invoke(o, methodName, arg);
        }

        /**
         * execute method
         * @param o
         * @param methodName
         * @param args
         * @return
         */
        private Object invoke(Object o, String methodName, Object... args) {
            final Map<String, Method> cache = METHOD_CACHE;
            final String methodNameLowerCase = methodName;
            final Method method = cache.get(methodNameLowerCase);
            final Class<?> returnType = method.getReturnType();
            try {
                if (returnType == void.class){
                    method.invoke(o, args);
                    return null;
                }
                return method.invoke(o, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
