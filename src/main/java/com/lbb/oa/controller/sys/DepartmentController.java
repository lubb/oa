package com.lbb.oa.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.model.sys.SysDepartment;
import com.lbb.oa.service.sys.DepartmentService;
import com.lbb.oa.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理
 *
 * @Author lubingbing
 * @Date 2020/3/15 14:11
 * @Version 1.0
 **/
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 部门列表
     *
     * @return
     */
    @PostMapping("/findDepartmentList")
    public ResponseBean findDepartmentList(@RequestBody JSONObject jsonObject) {
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String name = jsonObject.getString("name");
        PageInfo<SysDepartment> departmentsList = departmentService.findDepartmentList(pageNo, pageSize, name);
        return ResponseBean.success(departmentsList);
    }

    /**
     * 添加部门
     *
     * @return
     */
    @PreAuthorize("hasAuthority('department:add')")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody SysDepartment sysDepartment) {
        departmentService.add(sysDepartment);
        return ResponseBean.success(sysDepartment);
    }

    /**
     * 编辑部门
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('department:edit')")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id) {
        SysDepartment sysDepartment = departmentService.edit(id);
        return ResponseBean.success(sysDepartment);
    }

    /**
     * 更新部门
     *
     * @return
     */
    @PreAuthorize("hasAuthority('department:update')")
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody SysDepartment sysDepartment) {
        departmentService.update(id, sysDepartment);
        return ResponseBean.success(sysDepartment);
    }

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('department:delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseBean.success(id);
    }

    /**
     * 所有部门
     *
     * @return
     */
    @GetMapping("/findAll")
    public ResponseBean findAll() {
        List<SysDepartment> sysDepartmentList = departmentService.findAll();
        return ResponseBean.success(sysDepartmentList);
    }
}
