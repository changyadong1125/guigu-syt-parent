package com.atguigu.syt.cmn.service.impl;


import com.atguigu.syt.cmn.mapper.RegionMapper;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
@Slf4j
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据父级code获取所有子级元素
     */
    @Override
    @Cacheable(value = "regionList", key = "#parentCode", unless = "#result.size()==0")
    public List<Region> getRegionListByParentCode(String parentCode) {
        LambdaQueryWrapper<Region> regionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        regionLambdaQueryWrapper.eq(Region::getParentCode, parentCode);
        List<Region> regionList = baseMapper.selectList(regionLambdaQueryWrapper);
        regionList.forEach(region -> region.setHasChildren(region.getLevel() < 3));
        return regionList;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 获取数据库的所有数据信息
     */
    @Override
    public List<RegionExcelVo> getRegionList() {
        List<Region> regionList = baseMapper.selectList(new QueryWrapper<>(null));
        return regionList.stream().map(region -> {
            RegionExcelVo regionExcelVo = new RegionExcelVo();
            BeanUtils.copyProperties(region, regionExcelVo);
            return regionExcelVo;
        }).collect(Collectors.toList());
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 进行文件的批量导入数据库
     */
    @Override
    public void batchInsert(List<RegionExcelVo> cachedDataList) {
        long start = System.currentTimeMillis();
        baseMapper.batchInsert(cachedDataList);
        long end = System.currentTimeMillis();
        log.info("插入完成一共花费"+(end-start)+"ms");
    }
}
