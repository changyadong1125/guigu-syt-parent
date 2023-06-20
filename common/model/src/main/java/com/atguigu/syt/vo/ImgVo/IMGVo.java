package com.atguigu.syt.vo.ImgVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.vo.ImgVo
 * class:IMGVo
 *
 * @author: smile
 * @create: 2023/6/19-23:39
 * @Version: v1.0
 * @Description:
 */
@Data
@ApiModel(description = "图片实体")
public class IMGVo {

    @ApiModelProperty(value = "uid")
    private Long uid;

    @ApiModelProperty(value = "短信模板参数")
    private Set<Object> objectsName;
}
