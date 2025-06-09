package com.sparrow.test;


import com.sparrow.cryptogram.RSAUtils;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.FileUtility;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAUtilsTest {
    public static void main(String[] args) throws Exception {
        //读取私钥
        String privateKey = FileUtility.getInstance().readFileContent("/rsa/test");
        System.out.printf("private key: %s", privateKey);

        //读取公钥
        String publicKey = FileUtility.getInstance().readFileContent("/rsa/test.pub");
        System.out.printf("public key: %s", publicKey);
        privateKey = privateKey.replaceAll("\\s", "");
        PrivateKey privateKey1 = RSAUtils.getRSAPrivateKey(privateKey);

        PublicKey publicKey1 = RSAUtils.getRSAPublicKey(publicKey);

        //RSAUtils.encryptByPublicKey("CONTENT",)

        System.out.println(privateKey1.toString());
        System.out.println(publicKey1.toString());

        //公钥加密
        String encrypt = RSAUtils.encryptByPublicKey("test", publicKey1);
        System.out.printf("encrypt: %s\n", encrypt);
        //私钥解密
        String content = RSAUtils.decryptByPrivateKey(encrypt, privateKey1);
        System.out.printf("content: %s\n", content);

        //私钥签名
        String sign = RSAUtils.sign("test", privateKey1);
        System.out.printf("sign: %s\n", sign);

        //公钥验签
        boolean verify = RSAUtils.verify("test", sign, publicKey1);
        System.out.printf("verify: %s\n", verify);


        JwtRSAGenerator jwtRSAGenerator = new JwtRSAGenerator(privateKey,publicKey);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setNickName("harry");
        loginUser.setUserName("harry");
        loginUser.setAvatar("http://avatar.com");
        loginUser.setDeviceId("");
        loginUser.setDays(2D);
        loginUser.setExpireAt(2L);
//        loginUser.setExtensions();


        String token = jwtRSAGenerator.generateToken(loginUser);
        jwtRSAGenerator.parseToken(token);

    }
}
