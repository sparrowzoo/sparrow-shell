package com.sparrow.test;

import com.sparrow.cryptogram.RSAUtils;

import java.security.KeyPair;
import java.util.Base64;

public class RSAPairGenerateTest {
    public static void main(String[] args) {
        KeyPair keyPair = RSAUtils.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        System.out.println(publicKey);
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        System.out.println(privateKey);
    }
}
