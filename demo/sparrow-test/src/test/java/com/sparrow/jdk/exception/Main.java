package com.sparrow.jdk.exception;

import com.sparrow.utility.StringUtility;
import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            try {
                test();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                if (exceptionAsString.trim().equals("java.lang.NullPointerException")) {
                    System.out.println(i);
                    System.out.println("exceptionAsString=" + exceptionAsString);
                } else {
                    System.err.println(exceptionAsString);
                }
            }
        }
    }


    private static void test() {
        try {
            Integer i = null;
            Integer b = i / 10;
        } catch (Exception e) {
            throw e;
        }
    }
}
