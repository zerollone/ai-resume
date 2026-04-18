package com.ai.resume.exception;

/**
 * @author ws
 * @date 2026/4/18 16:24
 */
public class CommonException extends RuntimeException {

    private final int code;

    public CommonException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }
}
