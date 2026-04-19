package com.ai.resume.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 简历标签表
 * </p>
 *
 * @author ws
 * @since 2026-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resume_tag")
public class ResumeTagPO implements Serializable {

      private static final long serialVersionUID=1L;

      // 主键ID")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      // 标签名称(如:大厂牛马型)
      private String tagName;

      // 2D Q版头像 MinIO 地址
      private String imageUrl;

      // 3D 模型 MinIO 地址
      private String modelUrl;

      // 创建时间
      private LocalDateTime createTime;

      // 更新时间
      private LocalDateTime updateTime;


}
