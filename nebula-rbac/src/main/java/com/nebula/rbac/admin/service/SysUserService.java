package com.nebula.rbac.admin.service;

import com.nebula.common.utils.PageQuery;
import com.nebula.common.vo.UserVO;
import com.nebula.common.web.Response;
import com.nebula.rbac.admin.model.dto.UserDTO;
import com.nebula.rbac.admin.model.entity.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author feifeixia
 * @date 2017/10/31
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户角色信息
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO findUserByUsername(String username);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param pageQuery 查询条件
     * @param username  用户名称
     * @return
     */
    IPage<UserVO> selectPage(PageQuery pageQuery, String username);

    /**
     * 保存验证码
     *
     * @param randomStr 随机串
     * @param imageCode 验证码
     */
    void saveImageCode(String randomStr, String imageCode);

    /**
     * 添加用户
     *
     * @param userDto
     * @return
     */
    Boolean addUser(UserDTO userDto);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return boolean
     */
    Boolean deleteUserById(Integer id);

    /**
     * 更新当前用户基本信息
     *
     * @param userDto  用户信息
     * @param username 用户名
     * @return Boolean
     */
    Response updateUserInfo(UserDTO userDto, String username);

    /**
     * 更新指定用户信息
     *
     * @param sysUser  用户信息
     * @param username 用户信息
     * @return
     */
    Boolean updateUser(SysUser sysUser, String username);

    /**
     * 通过手机号查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    UserVO findUserByMobile(String mobile);

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return R
     */
    Response sendSmsCode(String mobile);

    /**
     * 通过openId查询用户
     *
     * @param openId openId
     * @return 用户信息
     */
    UserVO findUserByOpenId(String openId);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO selectUserVoById(Integer id);
}
