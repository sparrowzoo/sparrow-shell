package com.sparrow.jdk.volatilekey;

/**
 * Created by harry on 2018/6/27.
 * 虚拟机参数：
 * -XX:+PrintAssembly：输出反汇编内容；
 * -Xcomp：是让虚拟机以编译模式执行代码；
 * -XX:CompileCommand=dontinline,*ClassName.methodName：让编译器不要内联sum()；
 * -XX:CompileCommand=compileonly,*Bar.sum：只编译sum()；
 * <p>
 * java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=print com.sparrow.jdk.volatilekey.Test.test ClassFullPath
 * <p>
 * java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*VolitaleTest.main com.sparrow.jdk.volatilekey.VolitaleTest
 */
public class VolitaleTest {
    private static volatile int i = 0;
    public static void main(String[] args) {
        i++;
    }
}
