package com.sparrow.jdk.volatilekey;

/**
 * $ java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*Sington.sort com.sparrow.jdk.volatilekey.Sington
 * CompilerOracle: compileonly *Sington.sort
 * Could not load hsdis-amd64.dll; library not loadable; PrintAssembly is disabled
 * Java HotSpot(TM) 64-Bit Server VM warning: PrintAssembly is enabled; turning on DebugNonSafepoints to gain additional output
 * <p>
 * <p>
 * created by harry
 * on 2018/7/4.
 * <p>
 * java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=dontinline,*Sington.getInstance -XX:CompileCommand=compileonly,*Sington.getInstance com.sparrow.jdk.volatilekey.Sington
 */
public class Sington {
    public static void main(String[] args) {
        Sington sington = Sington.getInstance();
        //因为是运行时反汇编，所以如果运行时没有执行，可能无法打印代码
        Sington.sort(new int[]{1});
    }

    private Sington() {
    }

    public static void sort(int[] array) {
        // synchronize this operation so that some other thread can't
        // manipulate the array while we are sorting it. This assumes that other
        // threads also synchronize their accesses to the array.
        synchronized (array) {
            // now sort elements in array
        }
    }

    private static Sington sington;

    public static Sington getInstance() {
        if (sington != null) {
            return sington;
        }
        synchronized (Sington.class) {
            if (sington != null) {
                return sington;
            }
            sington = new Sington();
            return sington;
        }
    }
}
