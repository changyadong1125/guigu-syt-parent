package com.atguigu.syt.cmn.service;


import com.atguigu.syt.model.cmn.Dict;
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
public interface DictService extends IService<Dict> {

    String getDicTypeByHosType(String hosType, Long dicTypeId);

    List<Dict> getDictList(String dictTypeId);
}
