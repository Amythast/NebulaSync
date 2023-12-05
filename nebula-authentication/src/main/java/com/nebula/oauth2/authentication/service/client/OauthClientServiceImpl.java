package com.nebula.oauth2.authentication.service.client;

import cn.hutool.core.util.StrUtil;
import com.nebula.oauth2.authentication.config.redis.NebulaRedisRepository;
import com.nebula.oauth2.authentication.consts.CacheConstants;
import com.nebula.oauth2.authentication.entity.OauthClient;
import com.nebula.oauth2.authentication.exception.BusiException;
import com.nebula.oauth2.authentication.mapper.OauthClientMapper;
import com.nebula.oauth2.authentication.vo.OauthClientVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 客户端 Service
 *
 * @author feifeixia
 * 2019/7/5 14:56
 */
@SuppressWarnings("unchecked")
@Service
@Slf4j
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, OauthClient> implements OauthClientService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private NebulaRedisRepository redisRepository;

    @Override
    public OauthClientVo getVo(final String clientId) {
        final OauthClient client = this.getById(clientId);
        return new OauthClientVo(client);
    }

    @Override
    public Boolean update(final OauthClientVo vo) {
        if (StrUtil.isEmpty(vo.getClientId())) {
            throw new BusiException("不存在客户端ID");
        }
        final String key = CacheConstants.REDIS_CLIENTS_PREFIX + vo.getClientId();
        if (redisRepository.exists(key)) {
            if (log.isDebugEnabled()) {
                log.debug("Remove client:{} from redis.", key);
            }
            redisRepository.del(key);
        }
        return this.updateById(new OauthClient(vo));
    }

    @Override
    public Boolean add(final OauthClientVo vo) {
        vo.setClientSecret(encoder.encode(vo.getClientSecret()));
        if (StrUtil.isNotEmpty(vo.getClientId())) {
            throw new BusiException("存在客户端ID,无法新增");
        }
        return this.save(new OauthClient(vo));
    }

    @Override
    public Boolean delete(final String clientId) {
        if (this.getById(clientId) == null) {
            throw new BusiException("不存在的客户端ID: " + clientId);
        }
        final String key = CacheConstants.REDIS_CLIENTS_PREFIX + clientId;
        if (redisRepository.exists(key)) {
            if (log.isDebugEnabled()) {
                log.debug("Remove client:{} from redis.", key);
            }
            redisRepository.del(key);
        }
        return this.removeById(clientId);
    }

    @Override
    public IPage<OauthClientVo> selectPageVo(final Page page) {
        final IPage<OauthClient> iPage = this.page(page);
        return iPage.convert(OauthClientVo::new);
    }
}
