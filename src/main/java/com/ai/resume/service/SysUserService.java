package com.ai.resume.service;

import com.ai.resume.controller.vo.UserQueryVO;
import com.ai.resume.controller.vo.UserVO;
import com.ai.resume.entity.SysUserPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
public interface SysUserService extends IService<SysUserPO> {

    Boolean register(UserQueryVO vo);

    String login(UserQueryVO vo);

    UserVO getUserInfo();
}
