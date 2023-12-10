package com.nebula.oauth2.authentication.oauth2.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebula.oauth2.authentication.config.redis.NebulaRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 自定义 ClientDetailsService
 *
 * @author feifeixia
 * 2019/7/23 16:39
 */
@Slf4j
public class CustomClientDetailsService extends JdbcClientDetailsService {

    private NebulaRedisRepository redisRepository;

    private ObjectMapper objectMapper;

    private String prefix;

    public CustomClientDetailsService(final String prefix,
                                      final DataSource dataSource,
                                      final NebulaRedisRepository redisRepository,
                                      final ObjectMapper objectMapper) {
        super(dataSource);
        this.prefix = prefix;
        this.redisRepository = redisRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws InvalidClientException {
        final String key = prefix + clientId;
        final String value = redisRepository.get(key);
        if (value != null) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Get clientDetails:{} from redis", value);
                }
                return objectMapper.readValue(value, BaseClientDetails.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final ClientDetails clientDetails = super.loadClientByClientId(clientId);
        try {
            if (clientDetails instanceof BaseClientDetails) {
                if (log.isDebugEnabled()) {
                    log.debug("Put clientDetails:{} into redis", clientDetails.toString());
                }
                redisRepository.set(key, objectMapper.writeValueAsString(clientDetails));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return clientDetails;
    }
}
