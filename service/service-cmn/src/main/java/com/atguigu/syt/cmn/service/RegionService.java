package com.atguigu.syt.cmn.service;


import com.atguigu.syt.model.cmn.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-05-31
 */
public interface RegionService extends IService<Region> {

    List<Region> getRegionListByParentCode(String parentCode);
}
