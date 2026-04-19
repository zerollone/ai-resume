package com.ai.resume.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ai.resume.controller.vo.UserQueryVO;
import com.ai.resume.controller.vo.UserVO;
import com.ai.resume.entity.SysUserPO;
import com.ai.resume.entity.SysUserRolePO;
import com.ai.resume.exception.CommonException;
import com.ai.resume.mapper.SysUserMapper;
import com.ai.resume.mapper.SysUserRoleMapper;
import com.ai.resume.service.SysUserService;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    private final static Long REGULAR_USER = 1L;

    @Override
    public Boolean register(UserQueryVO vo) {
        String username = vo.getUsername();
        String password = vo.getPassword();
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            throw new CommonException("请传入用户名或密码！");
        }

        List<SysUserPO> userPOList = this.list(Wrappers.<SysUserPO>lambdaQuery()
                .eq(SysUserPO::getUsername, username));
        if (ObjectUtils.isNotEmpty(userPOList)) {
            throw new CommonException("用户名已存在！");
        }

        // 加密
        String passwordWithCrypt = BCrypt.hashpw(password, BCrypt.gensalt());

        SysUserPO po = new SysUserPO();
        po.setUsername(username);
        po.setPassword(passwordWithCrypt);
        this.save(po);

        // 用户角色表更新
        SysUserRolePO rolePO = new SysUserRolePO();
        rolePO.setUserId(po.getId());
        rolePO.setRoleId(REGULAR_USER);
        userRoleMapper.insert(rolePO);
        return Boolean.TRUE;
    }

    @Override
    public String login(UserQueryVO vo) {

        SysUserPO userPO = this.getOne(Wrappers.<SysUserPO>lambdaQuery()
                .eq(SysUserPO::getUsername, vo.getUsername()));
        if (ObjectUtils.isEmpty(userPO)) {
            throw new CommonException("用户名或密码错误");
        }

        if (userPO.getStatus() == 0) {
            throw new CommonException("账号已被禁用，请联系管理员");
        }

        boolean matches = BCrypt.checkpw(vo.getPassword(), userPO.getPassword());
        if (!matches) {
            throw new CommonException("用户名或密码错误");
        }

        StpUtil.login(userPO.getId());

        return StpUtil.getTokenValue();
    }

    @Override
    public UserVO getUserInfo() {
        long id = StpUtil.getLoginIdAsLong();
        SysUserPO userPO = this.getById(id);
        if (ObjectUtils.isEmpty(userPO)) {
            throw new CommonException("数据异常");
        }
        return BeanUtil.copyProperties(userPO, UserVO.class);
    }
}
