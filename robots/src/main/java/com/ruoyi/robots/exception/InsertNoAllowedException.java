package com.ruoyi.robots.exception;


public class InsertNoAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InsertNoAllowedException(String message) {
        super(message);
    }
}
