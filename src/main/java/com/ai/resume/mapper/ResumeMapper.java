package com.ai.resume.mapper;

import com.ai.resume.entity.ResumePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 简历表 Mapper 接口
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Mapper
public interface ResumeMapper extends BaseMapper<ResumePO> {

}
