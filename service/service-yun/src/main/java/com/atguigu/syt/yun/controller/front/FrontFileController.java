package com.atguigu.syt.yun.controller.front;

import com.atguigu.common.service.utils.AuthContextHolder;
import com.atguigu.common.util.result.Result;
import com.atguigu.syt.yun.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.yun.controller.front
 * class:FrontFileController
 *
 * @author: smile
 * @create: 2023/6/9-20:21
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/front/yun/file")
@Api("用户文件上传接口")
public class FrontFileController {
    @Resource
    private FileService fileService;
    @Resource
    private AuthContextHolder authContextHolder;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:文件上传
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        authContextHolder.checkAuth(request,response);
        Map<String, String> map = fileService.upload(file);
        return Result.ok(map);
    }
}
