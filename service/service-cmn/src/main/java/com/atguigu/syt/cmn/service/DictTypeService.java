package com.atguigu.syt.cmn.service;


import com.atguigu.syt.model.cmn.DictType;
import com.atguigu.syt.vo.cmn.DictTypeVo;
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
public interface DictTypeService extends IService<DictType> {
    List<DictTypeVo> getDictTypeLists();
    List<DictTypeVo> findAll_xml();
}
