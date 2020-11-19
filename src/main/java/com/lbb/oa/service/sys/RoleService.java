package com.lbb.oa.service.sys;

import com.github.pagehelper.PageInfo;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.pojo.sys.RoleVO;

import java.util.List;

public interface RoleService {

    /**
     * 通过userId获取用户的角色
     * @param userId
     * @return
     */
    public List<SysRole> getRoleByUserId(Long userId);

    /**
     * 删除角色
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据角色状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Boolean status);

    /**
     * 查询所有的角色
     * @return
     */
    List<SysRole> findAll();

    /**
     * 查询角色拥有的菜单权限id
     * @param id
     * @return
     */
    List<Long> findMenuIdsByRoleId(Long id);

    /**
     * 角色授权
     * @param mids
     */
    void authority(Long id, Long[] mids);

    /**
     * 分页获取角色列表
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @return
     */
    PageInfo<RoleVO> findRoleList(Integer pageNum, Integer pageSize, String roleName);

    /**
     * 新增角色
     * @param roleVO
     */
    void add(RoleVO roleVO);

    /**
     * 更新角色
     * @param id
     * @param roleVo
     */
    void update(Long id, RoleVO roleVo);

    /**
     * 获取详情
     * @param id
     * @return
     */
    RoleVO edit(Long id);
}
