package com.nebula.oauth2.authentication.service.client;

import com.nebula.oauth2.authentication.entity.OauthClient;
import com.nebula.oauth2.authentication.vo.OauthClientVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 客户端Service
 *
 * @author feifeixia
 * 2019/7/5 14:55
 */
public interface OauthClientService extends IService<OauthClient> {

    /**
     * 查询客户端VO
     *
     * @param clientId
     * @return
     */
    OauthClientVo getVo(String clientId);

    /**
     * 更新客户端
     *
     * @param vo 客户端
     * @return
     */
    Boolean update(OauthClientVo vo);

    /**
     * 新增客户端
     *
     * @param vo 客户端
     * @return
     */
    Boolean add(OauthClientVo vo);

    /**
     * 删除客户端VO
     *
     * @param clientId
     * @return
     */
    Boolean delete(String clientId);

    /**
     * 分页查询
     *
     * @param page
     * @return
     */
    IPage<OauthClientVo> selectPageVo(Page page);
}
