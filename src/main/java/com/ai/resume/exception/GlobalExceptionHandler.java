package com.ai.resume.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
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

    @ExceptionHandler(NotLoginException.class)
    public Result handleNotLogin(NotLoginException e) {
        return Result.error(401, "未登录或token已过期");
    }

    @ExceptionHandler(NotPermissionException.class)
    public Result handleNotPermission(NotPermissionException e) {
        return Result.error(403, "无权限访问：" + e.getPermission());
    }

    @ExceptionHandler(NotRoleException.class)
    public Result handleNotRole(NotRoleException e) {
        return Result.error(403, "无角色访问：" + e.getRole());
    }
}
