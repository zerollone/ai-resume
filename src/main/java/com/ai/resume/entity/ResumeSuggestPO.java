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
 * 简历优化建议表
 * </p>
 *
 * @author ws
 * @since 2026-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resume_suggest")
public class ResumeSuggestPO implements Serializable {

      private static final long serialVersionUID=1L;

      // 主键ID
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      // 关联的简历ID
      private Long resumeId;

      // AI生成的优化建议(Markdown格式)
      private String suggestContent;

      // 创建时间
      private LocalDateTime createTime;

      // 更新时间
      private LocalDateTime updateTime;

}
