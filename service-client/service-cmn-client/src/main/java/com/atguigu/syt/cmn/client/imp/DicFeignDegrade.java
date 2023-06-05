package com.atguigu.syt.cmn.client.imp;

import com.atguigu.syt.cmn.client.DictFeignClient;
import org.springframework.stereotype.Component;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.cmn.client.imp
 * class:DicFeignDegrade
 *
 * @author: smile
 * @create: 2023/6/5-20:34
 * @Version: v1.0
 * @Description:
 */
@Component
public class DicFeignDegrade implements DictFeignClient {

    @Override
    public String getDicTypeByHosType(String hosType, Long dicTypeId) {
        return "数据异常";
    }
}
