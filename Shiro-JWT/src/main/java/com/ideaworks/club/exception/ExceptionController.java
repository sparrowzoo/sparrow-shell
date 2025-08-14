package com.ideaworks.club.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ideaworks.club.bean.ResponseBean;

/**
 * 异常处理
 * 
 * @author 王庆港
 * @version 1.0.0
 */
@RestControllerAdvice
public class ExceptionController {
	// Catch Shiro Exception
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(ShiroException.class)
	public ResponseBean handle401(ShiroException e) {
		return new ResponseBean(401, e.getMessage(), null);
	}

	// Catch UnauthorizedException
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseBean handle401() {
		return new ResponseBean(401, "Unauthorized", null);
	}

	// Catch Other Exception
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
		return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
