package com.nebula.oauth2.authentication.mapper;

import com.nebula.oauth2.authentication.entity.OauthClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户端dao
 *
 * @author feifeixia
 * 2019/7/5 14:49
 */
@Mapper
public interface OauthClientMapper extends BaseMapper<OauthClient> {

}
