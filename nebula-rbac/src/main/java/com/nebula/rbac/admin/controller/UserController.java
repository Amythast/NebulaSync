package com.nebula.rbac.admin.controller;

import com.nebula.common.constants.RoleConst;
import com.nebula.common.utils.PageQuery;
import com.nebula.common.vo.LoginUser;
import com.nebula.common.vo.UserVO;
import com.nebula.common.web.BaseController;
import com.nebula.common.web.Response;
import com.nebula.common.web.annotation.RequireRole;
import com.nebula.rbac.admin.model.dto.UserDTO;
import com.nebula.rbac.admin.model.entity.SysUser;
import com.nebula.rbac.admin.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author feifeixia
 * @date 2017/10/28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private SysUserService userService;

    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public UserVO user(@PathVariable Integer id) {
        return userService.selectUserVoById(id);
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @DeleteMapping("/{id}")
    @RequireRole(RoleConst.ADMIN)
    public Response userDel(@PathVariable Integer id) {
        return Response.success(userService.deleteUserById(id));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    @RequireRole(RoleConst.ADMIN)
    public Response user(@Valid @RequestBody UserDTO userDto) {
        return Response.success(userService.addUser(userDto));
    }

    /**
     * 更新用户信息
     *
     * @param sysUser 用户信息
     * @return R
     */
    @PutMapping
    @RequireRole(RoleConst.ADMIN)
    public Response userUpdate(@Valid @RequestBody SysUser sysUser) {
        SysUser user = userService.getById(sysUser.getUserId());
        return Response.success(userService.updateUser(sysUser, user.getUsername()));
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    @GetMapping("/findUserByUsername/{username}")
    public UserVO findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * 通过手机号查询用户及其角色信息
     *
     * @param mobile 手机号
     * @return UseVo 对象
     */
    @GetMapping("/findUserByMobile/{mobile}")
    public UserVO findUserByMobile(@PathVariable String mobile) {
        return userService.findUserByMobile(mobile);
    }

    /**
     * 通过OpenId查询
     *
     * @param openId openid
     * @return 对象
     */
    @GetMapping("/findUserByOpenId/{openId}")
    public UserVO findUserByOpenId(@PathVariable String openId) {
        return userService.findUserByOpenId(openId);
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @GetMapping("/smsCode/{mobile}")
    public Response sendSmsCode(@PathVariable String mobile) {
        return userService.sendSmsCode(mobile);
    }

    /**
     * 分页查询用户
     *
     * @param params 参数集
     * @return 用户集合
     */
    @GetMapping("/userPage")
    @RequireRole(RoleConst.ADMIN)
    public IPage<UserVO> userPage(@RequestParam Map<String, Object> params) {
        return userService.selectPage(new PageQuery(params), (String) params.get("username"));
    }

    /**
     * 修改个人信息
     *
     * @param userDto   userDto
     * @param loginUser 登录用户信息
     * @return success/false
     */
    @PutMapping("/editInfo")
    @RequireRole(RoleConst.ADMIN)
    public Response editInfo(@Valid @RequestBody UserDTO userDto, LoginUser loginUser) {
        return userService.updateUserInfo(userDto, loginUser.getUsername());
    }
}
