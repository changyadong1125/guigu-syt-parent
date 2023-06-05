package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.repository.DepartmentRepository;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.model.hosp.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service.impl
 * class:DepartmentServiceImp
 *
 * @author: smile
 * @create: 2023/6/3-23:37
 * @Version: v1.0
 * @Description:
 */
@Service
public class DepartmentServiceImp implements DepartmentService {
    @Resource
    private DepartmentRepository departmentRepository;


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    @Override
    public void save(Map<String, Object> departmentMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(departmentMap), Department.class);
        Department exitDepartment = departmentRepository.findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (exitDepartment == null) {
            departmentRepository.save(department);
        } else {
            department.setId(exitDepartment.getId());
            departmentRepository.save(department);
        }

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    @Override
    public Page<Department> getDepartmentPageList(Integer page, Integer limit, String hoscode) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Department department = new Department();
        department.setHoscode(hoscode);
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        Example<Department> example = Example.of(department);
        return departmentRepository.findAll(example, pageRequest);

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:科室删除
     */
    @Override
    public void removeByHoscodeAndDepcode(String hoscode, String depcode) {
        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        departmentRepository.delete(department);
    }
}
