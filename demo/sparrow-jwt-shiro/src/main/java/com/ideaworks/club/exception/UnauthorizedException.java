package com.ideaworks.club.exception;

public class UnauthorizedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8334593360514615324L;

    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }

}
