package com.lbb.oa.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.exception.ServiceException;
import com.lbb.oa.mapper.sys.DepartmentMapper;
import com.lbb.oa.model.sys.SysDepartment;
import com.lbb.oa.service.sys.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangyukang
 * @Date 2020/3/15 14:15
 * @Version 1.0
 **/
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 系别列表
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageInfo<SysDepartment> findDepartmentList(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        Example o = new Example(SysDepartment.class);
        if (name != null && !"".equals(name)) {
            o.createCriteria().andLike("name", "%" + name + "%");
        }
        o.setOrderByClause("create_time desc");
        List<SysDepartment> departments = departmentMapper.selectByExample(o);
        PageInfo<SysDepartment> pageInfo = new PageInfo<>(departments);
        return pageInfo;
    }

    /**
     * 添加部门
     * @param sysDepartment
     */
    @Override
    public void add(SysDepartment sysDepartment) {
        sysDepartment.setCreateTime(new Date());
        sysDepartment.setModifiedTime(new Date());
        departmentMapper.insert(sysDepartment);
    }

    /**
     * 删除部门
     * @param id
     */
    @Override
    public void delete(Long id) {
        SysDepartment department = departmentMapper.selectByPrimaryKey(id);
        if(department==null){
            throw new ServiceException("要删除的部门不存在");
        }
        departmentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询所有的部门
     * @return
     */
    @Override
    public List<SysDepartment> findAll() {
        return departmentMapper.selectAll();
    }

    /**
     * 编辑部门
     * @param id
     * @return
     */
    @Override
    public SysDepartment edit(Long id) {
        SysDepartment department = departmentMapper.selectByPrimaryKey(id);
        if(department==null){
            //throw new ServiceException("编辑的部门不存在");
        }
        return department;
    }

    /**
     * 更新部门
     * @param id
     * @param sysDepartment
     */
    @Override
    public void update(Long id, SysDepartment sysDepartment) {
        SysDepartment dbDepartment = departmentMapper.selectByPrimaryKey(id);
        if(dbDepartment==null){
            //throw new ServiceException("要更新的部门不存在");
        }
        sysDepartment.setId(id);
        sysDepartment.setModifiedTime(new Date());
        departmentMapper.updateByPrimaryKeySelective(sysDepartment);
    }
}
