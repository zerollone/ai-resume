package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ws
 * @date 2026/4/18 18:21
 */
@Data
public class UserQueryVO {

    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
}
