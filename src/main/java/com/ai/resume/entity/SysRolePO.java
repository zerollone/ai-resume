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
 * 角色表
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRolePO implements Serializable {

    private static final long serialVersionUID=1L;

      // 角色ID
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      // 角色名称
      private String roleName;

      // 角色编码
      private String roleCode;

      // 角色描述
      private String description;

      // 状态：0禁用 1启用
      private Integer status;

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
