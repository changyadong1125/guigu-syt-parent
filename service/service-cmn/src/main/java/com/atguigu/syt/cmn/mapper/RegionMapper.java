package com.atguigu.syt.cmn.mapper;


import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
public interface RegionMapper extends BaseMapper<Region> {

    void batchInsert(@Param(value = "cachedDataList") List<RegionExcelVo> cachedDataList);
}
