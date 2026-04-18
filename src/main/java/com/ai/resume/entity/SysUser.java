package com.ai.resume.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID=1L;

      // 用户ID
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      // 用户名
      private String username;

      // 密码
      private String password;

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

      // 状态：0禁用 1启用
      private Integer status;

      // 最后登录时间
      private LocalDateTime lastLoginTime;

      // 最后登录IP
      private String lastLoginIp;

      // 创建人
      private String createBy;

      // 创建时间
      private LocalDateTime createTime;

      // 更新人
      private String updateBy;

      // 更新时间
      private LocalDateTime updateTime;

      // 是否删除：0未删 1已删
      private Integer deleted;

}
