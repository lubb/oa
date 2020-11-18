package com.lbb.oa.service.sys;

import com.github.pagehelper.PageInfo;
import com.lbb.oa.model.sys.SysDepartment;

import java.util.List;

/**
 * @Author zhangyukang
 * @Date 2020/3/15 14:12
 * @Version 1.0
 **/
public interface DepartmentService {

    /**
     * 删除院部门
     * @param id
     */
    void delete(Long id);


    /**
     * 全部部门
     * @return
     */
    List<SysDepartment> findAll();

    /**
     * 分页查询部门信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    PageInfo<SysDepartment> findDepartmentList(Integer pageNum, Integer pageSize, String name);

    /**
     * 添加部门
     * @param sysDepartment
     */
    void add(SysDepartment sysDepartment);

    /**
     * 部门详情
     * @param id
     * @return
     */
    SysDepartment edit(Long id);

    /**
     * 更新部门
     * @param id
     * @param sysDepartment
     */
    void update(Long id, SysDepartment sysDepartment);
}
