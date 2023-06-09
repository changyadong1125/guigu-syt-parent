package com.atguigu.syt.hosp.controller.front;

import com.atguigu.common.util.result.Result;
import com.atguigu.syt.hosp.service.HospitalService;
import com.atguigu.syt.model.hosp.Hospital;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.controller.front
 * class:FrontHospitalController
 *
 * @author: smile
 * @create: 2023/6/7-18:17
 * @Version: v1.0
 * @Description:
 */
@Api("用户端页面接口")
@RestController
@RequestMapping("/front/hosp/hospital")
public class FrontHospitalController {
    @Resource
    private HospitalService hospitalService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件查询医院
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据医院名称、级别和区域查询医院列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hosname", value = "医院名称"),
            @ApiImplicitParam(name = "hostype", value = "医院类型"),
            @ApiImplicitParam(name = "districtCode", value = "医院地区")})
    public Result<?> getHospitalList(String hosname, String hostype, String districtCode) {
        List<Hospital> list = hospitalService.getHospitalList(hosname, hostype, districtCode);
        return Result.ok(list);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取医院详细信息
     */
    @GetMapping("/detail/{hoscode}")
    @ApiOperation("获取医院详细信息")
    @ApiImplicitParam(name = "hoscode",value = "医院编号")
    public Result<?> getHospitalDetail(@PathVariable String hoscode){
        Hospital hospital = hospitalService.getHosDetail(hoscode);
        return Result.ok(hospital);
    }
}
