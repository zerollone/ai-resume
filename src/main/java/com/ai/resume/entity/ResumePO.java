package com.ai.resume.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 简历表
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("resume")
public class ResumePO implements Serializable {

    private static final long serialVersionUID=1L;

      // 简历ID
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      // 所属用户ID
      private Long userId;

      // 文件名称
      private String fileName;

      // 文件大小
      private Long fileSize;

      // 文件类型
      private String fileType;

      // 文件地址
      private String fileUrl;

      // 个人描述
      private String description;

      // 解析状态：0待解析 1解析中 2成功 3失败
      private Integer parseStatus;

      // 解析失败原因
      private String parseErrorMsg;

      // 姓名
      private String fullName;

      // 性别：0未知 1男 2女
      private Integer gender;

      // 出生日期
      private LocalDate birthday;

      // 手机号
      private String phone;

      // 邮箱
      private String email;

      // 国籍
      private String nationality;

      // 所在城市
      private String location;

      // 简历内容
      private String summary;

      // 总工作年限（年）
      private BigDecimal totalWorkYears;

      // 期望薪资（如：20-30k·15薪）
      private String expectedSalary;

      // 求职状态：0在职考虑 1离职 2应届等
      private Integer jobStatus;

      // 创建人
      private Long createBy;

      // 创建时间
      private LocalDateTime createTime;

      // 更新人
      private Long updateBy;

      // 更新时间
      private LocalDateTime updateTime;

      // 是否删除：0未删 1已删
      private Integer deleted;

}
