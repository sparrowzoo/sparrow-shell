package com.sparrow.jdk.io;

import java.io.File;
import java.io.FileOutputStream;

public class MainApp {
    public static void main(String[] args) throws Exception{
        String arg0 = args[0];
        String arg1 = args[1];
        int count = Integer.parseInt(arg1);
        long t1 = System.currentTimeMillis();
        File file = new File("1.txt");
        FileOutputStream fos = new FileOutputStream(file);
        for(int i=0;i<count;i++){
            fos.write("1111111111\n".getBytes("UTF-8"));
            //断电后不会丢失
            if(arg0.equals("1")){
                fos.getChannel().force(true);
            }
            //断电后不会丢失
            if(arg0.equals("2")){
                fos.getFD().sync();
            }
        }
        fos.flush();
        fos.close();
        long t2 = System.currentTimeMillis();
        System.out.println((t2-t1)/1000.0+"s");
    }
}

