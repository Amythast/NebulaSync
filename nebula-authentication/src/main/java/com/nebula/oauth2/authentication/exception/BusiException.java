package com.nebula.oauth2.authentication.exception;

/**
 * 自定义业务异常
 *
 * @author feifeixia
 * 2019/7/5 16:01
 */
public class BusiException extends RuntimeException{

    private static final long serialVersionUID = 5064805178344553207L;

    public BusiException(String message) {
        super(message);
    }
}
