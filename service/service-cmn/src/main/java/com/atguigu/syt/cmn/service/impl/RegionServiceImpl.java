package com.atguigu.syt.cmn.service.impl;


import com.atguigu.syt.cmn.mapper.RegionMapper;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
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
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据父级code获取所有子级元素
     */
    @Override
    @Cacheable(value = "regionList",key = "#parentCode",unless = "#result.size()==0")
    public List<Region> getRegionListByParentCode(String parentCode) {
        LambdaQueryWrapper<Region> regionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        regionLambdaQueryWrapper.eq(Region::getParentCode, parentCode);
        List<Region> regionList = baseMapper.selectList(regionLambdaQueryWrapper);
        regionList.forEach(region ->  region.setHasChildren(region.getLevel() < 3));
        return regionList;
    }
}
