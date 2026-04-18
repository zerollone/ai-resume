package com.ai.resume.exception;

import com.ai.resume.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ws
 * @date 2026/4/18 16:05
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleRuntimeException(RuntimeException ex) {
        return Result.error(500, ex.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public Result<String> handleCommonException(RuntimeException ex) {
        return Result.error(500, ex.getMessage());
    }
}
