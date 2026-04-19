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
 * 用户角色关联表
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_role")
public class SysUserRolePO implements Serializable {

    private static final long serialVersionUID=1L;

      // 关联ID
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      // 用户ID
      private Long userId;

      // 角色ID
      private Long roleId;

      // 创建人
      private Long createBy;

      // 创建时间
      private LocalDateTime createTime;

      // 更新人
      private Long updateBy;

      // 更新时间
      private LocalDateTime updateTime;

}
