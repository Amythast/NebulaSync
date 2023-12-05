package com.nebula.oauth2.authentication.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nebula.oauth2.authentication.vo.OauthClientVo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 客户端实体类
 *
 * @author feifeixia
 * 2019/7/5 14:48
 */
@Data
@TableName("oauth_client_details")
public class OauthClient {

    @TableId
    private String clientId;

    private String appName;

    private String resourceIds;

    private String clientSecret;

    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation;

    private String autoapprove;

    public OauthClient(final OauthClientVo vo) {
        this.setClientId(vo.getClientId());
        this.setAppName(vo.getAppName());
        this.setResourceIds(CollUtil.join(vo.getResourceIds(), StrUtil.COMMA));
        this.setClientSecret(vo.getClientSecret());
        this.setScope(CollUtil.join(vo.getScope(), StrUtil.COMMA));
        this.setAuthorizedGrantTypes(CollUtil.join(vo.getAuthorizedGrantTypes(), StrUtil.COMMA));
        this.setWebServerRedirectUri(vo.getWebServerRedirectUri());
        this.setAuthorities(CollUtil.join(vo.getAuthorities(), StrUtil.COMMA));
        this.setAutoapprove(String.valueOf(vo.getAutoapprove()));
    }
}
