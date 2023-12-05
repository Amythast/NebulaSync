# Spring Security 用户权限管理

## Scope & Authority

Scope 是客户端（应用）的权限范围，一般是 Read、Write、All、Server 之类的常见字符，也可根据实际情况自定义。
Authority 是用户的权限范围。在 Spring Security OAuth2 当中，解析 token 会附带的将用户的权限一并解析。

## 用户权限方案

用户信息由 UserDetailsService 加载，用户权限信息也可以由自定义的 UserDetailsService 一起加载到 UserDetails。也可以在用户认证成功过后，在通过用户ID、用户名再次请求用户权限。
在 Spring Security 当中，由于只有一个 Authority 的概念存在，关于用户的角色以及用户的资源权限，我们只能放在 Authority 当中，好在 Spring Security 对角色和权限已经做了区分。
在 SecurityExpressionRoot 当中，对角色做了单独的处理，加了默认的前缀 “ROLE_”，因此我们在创建角色的 Authority 的时候，只需要加入这个前缀即可，“hasRole("ROLE_ADMIN")” 和 “hasRole("ADMIN")” 是一样的效果：

```java
@Data
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = -1956975342008354518L;
    private static final String PREFIX = "ROLE_";
    private String role;
    private List<Operation> operations;
    @Override
    public String getAuthority() {
        return PREFIX + role.toUpperCase();
    }
}
```

一个角色对应多个可执行的操作权限（资源），资源同样是作为 Authority 存在，我给资源加入一个自定义的前缀标识 “OP_”，以便在 Authority 当中进行区分：

```java
@Data
@AllArgsConstructor
public class Operation implements GrantedAuthority {
    private static final long serialVersionUID = 6260083887682221456L;
    private static final String PREFIX = "OP_";
    private String op;
    @Override
    public String getAuthority() {
        return PREFIX + op.toUpperCase();
    }
}
```

这样我们在自定义的 UserDetails 实现当中，加入角色并且重写 getAuthorities() 方法即可：

```java
@Data
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 8741046663436494432L;
    /**
     * 角色列表
     */
    private List<Role> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }
        final List<GrantedAuthority> authorities = new ArrayList<>(AuthorityUtils.createAuthorityList(
                roles.stream().map(Role::getAuthority).collect(Collectors.joining())));
        roles.forEach(role -> {
            if (CollUtil.isNotEmpty(role.getOperations())) {
                authorities.addAll(AuthorityUtils.createAuthorityList(
                        role.getOperations().stream().map(Operation::getAuthority).collect(Collectors.joining())));
            }
        });
        return authorities;
    }
}
```

## 用户扩展

用户以及用户权限方面留给使用者自己去扩展，这里只是使用了一个 `MockUserService` 模拟了获取用户的过程，以及其他相关的角色、组织、接口权限都属于扩展的部分，统一认证只做认证做的事情。

```java
/**
 * 模拟 UserService 实现查询用户
 * 在实际使用上需要替换
 *
 * @author feifeixia
 * 2019/7/3 9:52
 */
@Service
public class MockUserService {
    @Autowired
    private PasswordEncoder encoder;
    /**
     * 根据用户名称返回用户
     *
     * @param username 用户名称,必须唯一
     * @return
     */
    public User findUserByUsername(String username) {
        final User user = new User();
        user.setUsername(username);
        // 密码和用户名保持一致
        user.setPassword(encoder.encode(username));
        user.setEnabled(true);
        user.setUserId(RandomUtil.randomInt());
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);
        user.setPasswordExpired(false);
        user.setRoles(Collections.singletonList(defaultRole()));
        return user;
    }
    /**
     * 根据手机号返回用户
     *
     * @param mobile 手机号,必须唯一
     * @return
     */
    public User findUserByMobile(String mobile) {
        final User user = new User();
        user.setUsername(mobile);
        // 密码和用户名保持一致
        user.setPassword(encoder.encode(mobile));
        user.setEnabled(true);
        user.setUserId(RandomUtil.randomInt());
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);
        user.setPasswordExpired(false);
        user.setRoles(Collections.singletonList(defaultRole()));
        return user;
    }
    private Role defaultRole() {
        return new Role(CommonConstants.ROLE_DEFAULT,
                Collections.singletonList(new Operation(CommonConstants.OP_DEFAULT)));
    }
}
```
