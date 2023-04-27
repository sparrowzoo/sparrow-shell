package com.sparrow.test;

import com.sparrow.cryptogram.Base64;
import com.sparrow.cryptogram.RSAUtils;

import java.security.KeyPair;

public class RSAPairGenerateTest {
    public static void main(String[] args) {
        KeyPair keyPair = RSAUtils.generateKeyPair();

        String publicKey = Base64.encodeBytes(keyPair.getPublic().getEncoded());

        System.out.println(publicKey);
        String privateKey = Base64.encodeBytes(keyPair.getPrivate().getEncoded());

        System.out.println(privateKey);
    }
}
