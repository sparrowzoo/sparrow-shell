package com.ideaworks.club.bean;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {
    private static final long serialVersionUID = 2231225172429467901L;
    // TOKEN
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

}
