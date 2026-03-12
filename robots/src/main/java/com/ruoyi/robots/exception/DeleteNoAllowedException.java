package com.ruoyi.robots.exception;


public class DeleteNoAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public DeleteNoAllowedException(String message) {
        super(message);
    }
}
