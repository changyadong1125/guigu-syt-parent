package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.repository.DepartmentRepository;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据医院编号获取医院科室信息列表
     */
    @Override
    public List<DepartmentVo> getDepartmentByHoscode(String hoscode) {
        List<DepartmentVo> result = new ArrayList<>();
        //先查询出所有的科室信息
        List<Department> departmentList = departmentRepository.findByHoscode(hoscode);
        //根据大科室编号 bigcode 分组 获取每个大科室下面的小科室
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map结合
        for (Map.Entry<String, List<Department>> bigDepartment : departmentMap.entrySet()) {
            String bigCode = bigDepartment.getKey();
            List<Department> bigDepartmentList = bigDepartment.getValue();
            //封装大科室信息
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigCode);
            bigDepartmentVo.setDepname(bigDepartmentList.get(0).getBigname());
            //创建封装小科室信息的集合
            List<DepartmentVo> bigDepartmentVoListWithChildren = new ArrayList<>();
            //封装小科室信息
            for (Department smallDepartment : bigDepartmentList) {
                DepartmentVo smallDepartmentVo = new DepartmentVo();
                smallDepartmentVo.setDepcode(smallDepartment.getDepcode());
                smallDepartmentVo.setDepname(smallDepartment.getDepname());
                bigDepartmentVoListWithChildren.add(smallDepartmentVo);
            }
            bigDepartmentVo.setChildren(bigDepartmentVoListWithChildren);
            result.add(bigDepartmentVo);
        }
        return result;
    }
}
