package com.ai.resume.result;

public enum ResultCode {

    SUCCESS(200, "success"),
    ERROR(500, "System exception"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    PARAM_ERROR(400, "Invalid parameter");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
