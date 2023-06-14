package com.atguigu.syt.user.controller.inner;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.service.PatientService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.controller.inner
 * class:InnerPatientController
 *
 * @author: smile
 * @create: 2023/6/14-20:18
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/inner/user/patient")
@Api("获取就诊人信息")
@RequiredArgsConstructor
public class InnerPatientController {
    private final PatientService patientService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据就诊人id获取就诊人信息
     */
    @GetMapping("/info/{id}")
    public Patient getPatient(@PathVariable("id") Long id) {
        return patientService.getPatient(id);
    }
}
