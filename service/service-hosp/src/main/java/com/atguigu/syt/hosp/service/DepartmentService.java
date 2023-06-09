package com.atguigu.syt.hosp.service;

import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.hosp.service
 * class:DepartmentService
 *
 * @author: smile
 * @create: 2023/6/3-23:36
 * @Version: v1.0
 * @Description:
 */
public interface DepartmentService {


    void save(Map<String, Object> departmentMap);

    Page<Department> getDepartmentPageList(Integer page, Integer limit, String hoscode);
    void removeByHoscodeAndDepcode(String hoscode, String depcode);

    List<DepartmentVo> getDepartmentByHoscode(String hoscode);
}
