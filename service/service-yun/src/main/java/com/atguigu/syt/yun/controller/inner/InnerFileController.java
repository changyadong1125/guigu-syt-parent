package com.atguigu.syt.yun.controller.inner;

import com.atguigu.syt.yun.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.controller.inner
 * class:InnerFileController
 *
 * @author: smile
 * @create: 2023/6/10-21:40
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/inner/yun/file")
public class InnerFileController {
    @Resource
    private FileService fileService;

    @ApiOperation(value = "获取图片预览Url")
    @ApiImplicitParam(name = "objectName",value = "文件名", required = true)
    @GetMapping("/getPreviewUrl")
    public String getPreviewUrl(@RequestParam String objectName) {
        return fileService.getPreviewUrl(objectName);
    }
}
