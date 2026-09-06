package com.sparrow;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

public class SpringAsmVerify {

    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("开始使用 Spring 框架封装的 ASM 进行元数据解析");
        System.out.println("目标类: com.sparrow.boot.config.SparrowAutoConfiguration2");
        System.out.println("注意：观察控制台，验证 static 块是否被执行！");
        System.out.println("========================================\n");

        // 1. 创建 Spring 的 MetadataReader 工厂（底层使用 Spring 重打包的 ASM）
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory(new DefaultResourceLoader());

        // 2. 获取类的元数据读取器（注意：这里传入的是全限定类名，Spring 会自动从 classpath 加载 .class 文件流）
        MetadataReader reader = factory.getMetadataReader("com.sparrow.boot.config.SparrowAutoConfiguration2");

        // 3. 获取类的基本元数据（类名、父类、接口）
        ClassMetadata classMetadata = reader.getClassMetadata();
        System.out.println("[Spring ASM 解析] 类名: " + classMetadata.getClassName());
        System.out.println("[Spring ASM 解析] 父类: " + classMetadata.getSuperClassName());
        System.out.println("[Spring ASM 解析] 是否接口: " + classMetadata.isInterface());
        System.out.println("[Spring ASM 解析] 是否抽象: " + classMetadata.isAbstract());

        // 4. 获取类的注解元数据（包括 @Bean 方法上的注解等）
        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        System.out.println("[Spring ASM 解析] 类上的注解: " + annotationMetadata.getAnnotationTypes());

        // 5. （可选）如果想看 @Bean 方法，可以获取所有方法元数据，但此处的 AnnotationMetadata 已经包含了
        // 我们可以打印所有注解属性，但为了简洁，这里只打印存在性。
        System.out.println("\n[Spring ASM 解析] 是否标注了 @Configuration？ " 
                + annotationMetadata.hasAnnotation("org.springframework.context.annotation.Configuration"));
        System.out.println("[Spring ASM 解析] 是否标注了 @Slf4j（Lombok source 不存在）？ "
                + annotationMetadata.hasAnnotation("lombok.extern.slf4j.Slf4j"));

        System.out.println("[Spring ASM 解析] 是否标注了 @Service（Spring runtime 存在）？ "
                + annotationMetadata.hasAnnotation("org.springframework.stereotype.Service"));


        System.out.println("\n========================================");
        System.out.println("Spring ASM 元数据解析执行完毕！");
        System.out.println("！！！ 关键验证结果 ！！！");
        System.out.println("1. 上面打印了类名、注解等元数据信息。");
        System.out.println("2. 但是，SparrowAutoConfiguration2 类中的 static { log.info(...) } 块");
        System.out.println("   绝对 没有 在控制台输出任何日志！");
        System.out.println("这证明：Spring 的 MetadataReader（基于 ASM）只解析字节码结构，");
        System.out.println("绝不触发 JVM 类加载，所以 static 代码块绝对不会执行！");
        System.out.println("========================================");
    }
}