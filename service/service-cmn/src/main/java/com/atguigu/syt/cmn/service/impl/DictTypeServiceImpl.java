package com.atguigu.syt.cmn.service.impl;


import com.atguigu.syt.cmn.mapper.DictMapper;
import com.atguigu.syt.cmn.mapper.DictTypeMapper;
import com.atguigu.syt.cmn.service.DictTypeService;
import com.atguigu.syt.model.cmn.Dict;
import com.atguigu.syt.model.cmn.DictType;
import com.atguigu.syt.vo.cmn.DictTypeVo;
import com.atguigu.syt.vo.cmn.DictVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {
    @Resource
    private DictMapper dictMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有的数据字典
     */
    @Override
    public List<DictTypeVo> getDictTypeLists() {
        List<DictType> dictTypeList = baseMapper.selectList(null);

        return dictTypeList.stream().map(dictType -> {
            DictTypeVo dictTypeVo = new DictTypeVo();
            dictTypeVo.setId("parent-" + dictType.getId());
            dictTypeVo.setName(dictType.getName());
            List<Dict> dictList = dictMapper.selectList(null);
            List<DictVo> dictVoList = dictList.stream().filter(dict -> dict.getDictTypeId().longValue()==dictType.getId().longValue()).map(dict -> {
                DictVo dictVo = new DictVo();
                dictVo.setId("children-" + dict.getId());
                dictVo.setName(dict.getName());
                dictVo.setValue(dict.getValue());
                return dictVo;
            }).collect(Collectors.toList());
            dictTypeVo.setChildren(dictVoList);
            return dictTypeVo;
        }).collect(Collectors.toList());
    }
    public List<DictTypeVo> findAll_xml() {
        int a = 3;
        List<DictTypeVo> all_xml = baseMapper.findAll_xml();
        int a1= 3;
        return all_xml;
    }

}
