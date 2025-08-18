package com.sparrow.jdk.os;

//https://docs.oracle.com/javase/specs/jvms/se7/html/

/**
 * java -Dtest=1234 com.sparrow.jdk.os.SystemProperty  1 2 3 4
 */
public class SystemProperty {
    static {
        System.out.println("static");
    }
    //Other methods named <clinit> in a class file are of no consequence. They are not class or interface initialization methods. They cannot be invoked by any Java Virtual Machine instruction and are never invoked by the Java Virtual Machine itself.
//    static int clinit(int i){
//        System.out.println("clinit int method");
//        return 1;
//    }
//    public  void  clinit(){
//        System.out.println("clinit static");
//    }
    public static void main(String[] args) {
        for(int i=0;i<args.length;i++){
            System.out.println(args[i]);
        }

        System.out.println(System.getProperty("test"));
        System.out.println(System.getProperty("user.home"));
    }
}
