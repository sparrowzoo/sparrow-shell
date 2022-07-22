package com.sparrow.authorizing;

public class AuthorizerTest {
    public static void main(String[] args) {
        AuthenticatorDemo authenticatorDemo = new AuthenticatorDemo();
        System.out.println(authenticatorDemo.getSecret(1L));
    }
}
