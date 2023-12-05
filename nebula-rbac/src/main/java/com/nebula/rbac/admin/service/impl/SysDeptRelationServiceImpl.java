package com.nebula.rbac.admin.service.impl;

import com.nebula.rbac.admin.model.entity.SysDeptRelation;
import com.nebula.rbac.admin.service.SysDeptRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nebula.rbac.admin.mapper.SysDeptRelationMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author feifeixia
 * @since 2018-02-12
 */
@Service
public class SysDeptRelationServiceImpl
        extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation>
        implements SysDeptRelationService {
}
