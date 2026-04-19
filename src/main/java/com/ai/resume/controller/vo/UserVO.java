package com.ai.resume.controller.vo;

import lombok.Data;

/**
 * @author ws
 * @date 2026/4/18 21:16
 */
@Data
public class UserVO {

    private Long id;

    // 用户名
    private String username;

    // 昵称
    private String nickname;

    // 个人描述
    private String description;

    // 邮箱
    private String email;

    // 手机号
    private String mobile;

    // 头像URL
    private String avatar;
}
