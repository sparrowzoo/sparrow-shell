package com.sparrow.jdk.asm;

import java.io.FileOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Helloworld extends ClassLoader implements Opcodes {

    /**
     * public class Example {
     * <p>
     * static {
     * System.out.println("hello world");
     * }
     * public static void main (String[] args) {
     * System.out.println("Hello world!");
     * }
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String args[]) throws Exception {


        //定义一个叫做Example的类
        ClassWriter cw = new ClassWriter(0);
        //50 即为JDK 1.6
        //通过个性版本号来验证是否需要ACC_STATIC FLAG
        cw.visit(V1_6, ACC_PUBLIC, "Example", null, "java/lang/Object", null);

        //生成默认的构造方法
        MethodVisitor initVisitor = cw.visitMethod(ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null);

        //生成构造方法的字节码指令
        initVisitor.visitVarInsn(ALOAD, 0);
        initVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        initVisitor.visitInsn(RETURN);
        initVisitor.visitMaxs(1, 1);
        initVisitor.visitEnd();

        //生成main方法
        initVisitor = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null);

        //生成main方法中的字节码指令
        initVisitor.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");

        initVisitor.visitLdcInsn("Hello world!");
        initVisitor.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V", false);
        initVisitor.visitInsn(RETURN);
        initVisitor.visitMaxs(2, 2);
        //字节码生成完成
        initVisitor.visitEnd();

        MethodVisitor classInitVisitor = cw.visitMethod(ACC_PUBLIC, "<clinit>", "()V", null, null);
        classInitVisitor.visitCode();
        classInitVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        classInitVisitor.visitLdcInsn("static hello world");
        classInitVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",false);
        classInitVisitor.visitInsn(RETURN);
        classInitVisitor.visitMaxs(2, 0);
        classInitVisitor.visitEnd();

//        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//        mv.visitCode();
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("static hello world");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",false);
//        mv.visitInsn(RETURN);
//        mv.visitMaxs(2, 0);
//        mv.visitEnd();


        // 获取生成的class文件对应的二进制流
        byte[] code = cw.toByteArray();
        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Example.class");
        fos.write(code);
        fos.close();

        //直接将二进制流加载到内存中
        Helloworld loader = new Helloworld();
        Class<?> exampleClass = loader.defineClass("Example", code, 0, code.length);

        //通过反射调用main方法
        exampleClass.getMethods()[0].invoke(null, new Object[]{null});
    }
}
