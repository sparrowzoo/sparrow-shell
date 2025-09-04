package com.sparrow.spring;

import com.sparrow.cryptogram.MessageSignature;

public class PassportRest {
    public static void main(String[] args) {
        System.out.println(MessageSignature.getInstance().md5("abc123ABC!"));
    }
}
