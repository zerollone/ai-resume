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
 * 权限资源表
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission")
public class SysPermissionPO implements Serializable {

    private static final long serialVersionUID=1L;

      // 权限ID
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      // 权限名称
      private String name;

      // 权限编码（如 user:add）
      private String code;

      // 类型：1目录 2菜单 3按钮
      private Integer type;

      // 路由路径（前端）
      private String path;

      // 组件路径（前端）
      private String component;

      // 图标
      private String icon;

      // 父权限ID（0为顶级）
      private Long parentId;

      // 权限层级
      private Integer level;

      // 排序值
      private Integer sortOrder;

      // 状态：0禁用 1启用
      private Integer status;

      // 创建人
      private Long createBy;

      // 创建时间
      private LocalDateTime createTime;

      // 更新人
      private Long updateBy;

      // 更新时间
      private LocalDateTime updateTime;

}
