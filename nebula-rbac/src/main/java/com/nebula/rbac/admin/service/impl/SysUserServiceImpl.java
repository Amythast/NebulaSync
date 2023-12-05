package com.nebula.rbac.admin.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.nebula.common.constants.CacheConstants;
import com.nebula.common.constants.CommonConstant;
import com.nebula.common.constants.SecurityConstants;
import com.nebula.common.exception.InvalidParamException;
import com.nebula.common.redis.template.TarocoRedisRepository;
import com.nebula.common.utils.PageQuery;
import com.nebula.common.vo.SysRole;
import com.nebula.common.vo.UserVO;
import com.nebula.common.web.Response;
import com.nebula.rbac.admin.mapper.SysUserMapper;
import com.nebula.rbac.admin.model.dto.UserDTO;
import com.nebula.rbac.admin.model.entity.SysUser;
import com.nebula.rbac.admin.service.SysRolePermissionService;
import com.nebula.rbac.admin.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author feifeixia
 * @date 2017/10/31
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private TarocoRedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Override
    public UserVO findUserByUsername(String username) {
        final UserVO userVO = sysUserMapper.selectUserVoByUsername(username);
        if (userVO == null) {
            return null;
        }
        final List<SysRole> roleList = userVO.getRoleList();
        if (!CollectionUtils.isEmpty(roleList)) {
            userVO.setPermissions(sysRolePermissionService
                    .getRolePermissions(roleList.stream().map(SysRole::getRoleId).collect(Collectors.toSet())));
        }
        return userVO;
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    public UserVO findUserByMobile(String mobile) {
        return sysUserMapper.selectUserVoByMobile(mobile);
    }

    /**
     * 通过openId查询用户
     *
     * @param openId openId
     * @return 用户信息
     */
    @Override
    public UserVO findUserByOpenId(String openId) {
        return sysUserMapper.selectUserVoByOpenId(openId);
    }

    @Override
    public IPage<UserVO> selectPage(PageQuery pageQuery, String username) {
        return sysUserMapper.selectPageVo(pageQuery, username);
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return sysUserMapper.selectUserVoById(id);
    }

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param randomStr 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String randomStr, String imageCode) {
        redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE);
    }

    @Override
    public Boolean addUser(final UserDTO userDto) {
        final String username = userDto.getUsername();
        final String phone = userDto.getPhone();
        if (findUserByUsername(username) != null) {
            throw new InvalidParamException("用户名:" + username + ", 已经存在");
        }
        if (findUserByMobile(phone) != null) {
            throw new InvalidParamException("手机号:" + phone + ", 已经存在");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        return this.save(sysUser);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生6位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public Response sendSmsCode(String mobile) {
        Object tempCode = redisRepository.get(CacheConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return Response.failure("验证码: " + tempCode + "未失效，请失效后再次申请");
        }

        SysUser params = new SysUser();
        params.setPhone(mobile);
        List<SysUser> userList = this.list(new QueryWrapper<>(params));

        if (CollectionUtils.isEmpty(userList)) {
            log.error("根据用户手机号:{}, 查询用户为空", mobile);
            return Response.failure("手机号不存在");
        }

        String code = RandomUtil.randomNumbers(6);
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE);
        return Response.success(code);
    }

    @Override
    public Boolean deleteUserById(Integer id) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null) {
            throw new InvalidParamException("无效的用户ID");
        }
        if (CommonConstant.ADMIN_USER_NAME.equals(sysUser.getUsername())) {
            throw new InvalidParamException("不允许删除超级管理员");
        }
        sysUser.setDelFlag(CommonConstant.STATUS_DEL);
        return this.updateById(sysUser);
    }

    @Override
    public Response updateUserInfo(UserDTO userDto, String username) {
        UserVO userVo = this.findUserByUsername(username);
        SysUser sysUser = new SysUser();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getPassword())
                && org.apache.commons.lang3.StringUtils.isNotBlank(userDto.getNewpassword1())) {
            if (ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
                sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
            } else {
                log.warn("原密码错误，修改密码失败:{}", username);
                return Response.failure("原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVo.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return Response.success(this.updateById(sysUser));
    }

    @Override
    public Boolean updateUser(SysUser sysUser, String username) {
        sysUser.setUpdateTime(new Date());
        this.updateById(sysUser);
        return Boolean.TRUE;
    }

}
