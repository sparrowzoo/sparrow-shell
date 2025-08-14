package com.ideaworks.club.exception;

/**
 * 自定义异常
 * 
 * @author 王庆港
 * @version 1.0.0
 */
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
