package com.ai.resume.controller;


import com.ai.resume.controller.vo.UserQueryVO;
import com.ai.resume.controller.vo.UserVO;
import com.ai.resume.result.Result;
import com.ai.resume.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/register")
    @Operation(summary = "注册")
    public Result<Boolean> register(@RequestBody UserQueryVO vo){
        return Result.success(userService.register(vo));
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody UserQueryVO vo) {
        return Result.success(userService.login(vo));
    }

    @GetMapping("/user/info")
    @Operation(summary = "获取用户信息")
    public Result<UserVO> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }
}

