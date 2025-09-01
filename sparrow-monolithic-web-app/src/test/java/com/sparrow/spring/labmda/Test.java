package com.sparrow.spring.labmda;

import com.sparrow.cg.PropertyNamer;
import com.sparrow.protocol.SFunction;
import com.sparrow.utility.ClassUtility;
import com.sparrowzoo.coder.po.ProjectConfig;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
//        LambdaQueryWrapper<ProjectConfig> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ProjectConfig::getStatus, 1);
//        wrapper.eq(ProjectConfig::getChineseName, "中文名称");
        //project_config.status = 1 and project_config.chinese_name = '中文名称'
        System.out.println(getPropertyNameAndClassName(ProjectConfig::getStatus).entityDotProperty());
        //System.out.println(getPropertyNameAndClassName(ProjectConfig::getChineseName).entityDotProperty());
    }

    public static <T> ClassUtility.PropertyWithBeanName getPropertyNameAndClassName(SFunction<T, ?> function) {
        try {
            // 反射获取 writeReplace 方法
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            // 调用该方法获取 SerializedLambda 对象
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            // 解析方法名
            String methodName = serializedLambda.getImplMethodName();
            String clazz = serializedLambda.getImplClass();//PO
            return new ClassUtility.PropertyWithBeanName(PropertyNamer.methodToProperty(methodName), clazz);

            //ShadowLambdaMeta lambdaMeta = new ShadowLambdaMeta(serializedLambda);
            //return new ClassUtility.PropertyWithEntityName(PropertyNamer.methodToProperty(methodName), lambdaMeta.getInstantiatedClass());
        } catch (Exception e) {
            throw new RuntimeException("无法解析方法名", e);
        }
    }


}
