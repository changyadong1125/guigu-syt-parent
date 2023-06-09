package com.atguigu.syt.cmn.service.impl;


import com.atguigu.syt.cmn.mapper.DictMapper;
import com.atguigu.syt.cmn.service.DictService;
import com.atguigu.syt.model.cmn.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据hosType获取dictName
     */
    @Override
    public String getDicTypeByHosType(String hosType, Long dicTypeId) {
        LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictLambdaQueryWrapper.eq(Dict::getDictTypeId, dicTypeId);
        dictLambdaQueryWrapper.eq(Dict::getValue, hosType);
        return baseMapper.selectOne(dictLambdaQueryWrapper).getName();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据DictTypeId获取指定dict
     */
    @Override
    public List<Dict> getDictList(String dictTypeId) {
        LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictLambdaQueryWrapper.eq(Dict::getDictTypeId,dictTypeId);
        return baseMapper.selectList(dictLambdaQueryWrapper);
    }
}
