package com.atguigu.syt.hosp.controller.inner;

import com.atguigu.syt.hosp.service.HospitalSetService;
import com.atguigu.syt.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.inner
 * class:InnerHospSetController
 *
 * @author: smile
 * @create: 2023/6/14-20:14
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/inner/hosp/hospset")
@RequiredArgsConstructor
@Api("医院接口-供其他微服务远程调用")
public class InnerHospSetController {
    private final HospitalSetService hospitalSetService;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据医院编号获取医院信息
     */
    @GetMapping("/info/{hoscode}")
    public HospitalSet getHospitalSet(@PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getHospitalSet(hoscode);
    }
}
