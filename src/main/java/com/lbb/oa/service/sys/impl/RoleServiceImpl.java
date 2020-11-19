package com.lbb.oa.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.exception.ServiceException;
import com.lbb.oa.mapper.sys.MenuMapper;
import com.lbb.oa.mapper.sys.RoleMapper;
import com.lbb.oa.mapper.sys.RoleMenuMapper;
import com.lbb.oa.mapper.sys.UserRoleMapper;
import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysRoleMenu;
import com.lbb.oa.model.sys.SysUserRole;
import com.lbb.oa.pojo.sys.RoleVO;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.util.GlobalConfig;
import com.lbb.oa.util.RoleConverterUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 通过用户id获取用户的角色
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        List<SysRole> roles=new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        List<SysUserRole> userRoleList = userRoleMapper.select(sysUserRole);
        List<Long> rids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(userRoleList)){
            for (SysUserRole userRole : userRoleList) {
                rids.add(userRole.getRoleId());
            }
            if(!CollectionUtils.isEmpty(rids)){
                for (Long rid : rids) {
                    SysRole role = roleMapper.selectByPrimaryKey(rid);
                    if(role!=null){
                        roles.add(role);
                    }
                }
            }
        }
        return roles;
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if(role==null){
            throw new ServiceException("要删除的角色不存在");
        }
        roleMapper.deleteByPrimaryKey(id);
        //删除对应的[角色-菜单]记录
        Example o = new Example(SysRoleMenu.class);
        o.createCriteria().andEqualTo("roleId",id);
        roleMenuMapper.deleteByExample(o);
    }

    /**
     * 更新状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if(role==null){
            throw new ServiceException("角色不存在");
        }
        SysRole t = new SysRole();
        t.setId(id);
        t.setStatus(status? GlobalConfig.RoleStatusEnum.DISABLE.getStatusCode():
                GlobalConfig. RoleStatusEnum.AVAILABLE.getStatusCode());
        roleMapper.updateByPrimaryKeySelective(t);
    }

    /**
     * 查询所以
     * @return
     */
    @Override
    public List<SysRole> findAll() {
        return roleMapper.selectAll();
    }

    /**
     * 通过角色id得到所有的菜单ids
     * @param id
     * @return
     */
    @Override
    public List<Long> findMenuIdsByRoleId(Long id) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if(role==null){
            throw new ServiceException("该角色已不存在");
        }
        List<Long> ids=new ArrayList<>();
        Example o = new Example(SysRoleMenu.class);
        o.createCriteria().andEqualTo("roleId",id);
        List<SysRoleMenu> roleMenus = roleMenuMapper.selectByExample(o);
        if(!CollectionUtils.isEmpty(roleMenus)){
            for (SysRoleMenu roleMenu : roleMenus) {
                ids.add(roleMenu.getMenuId());
            }
        }
        return ids;
    }

    /**
     * 角色授权
     * @param id
     * @param mids
     */
    @Transactional
    @Override
    public void authority(Long id,Long[] mids) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if(role==null){
            throw new ServiceException("该角色不存在");
        }
        //先删除原来的权限
        Example o = new Example(SysRoleMenu.class);
        o.createCriteria().andEqualTo("roleId",id);
        roleMenuMapper.deleteByExample(o);
        //增加现在分配的角色
        if(mids.length>0){
            for (Long mid : mids) {
                SysMenu menu = menuMapper.selectByPrimaryKey(mid);
                if(menu==null){
                    throw new ServiceException("menuId="+mid+",菜单权限不存在");
                }else {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(id);
                    roleMenu.setMenuId(mid);
                    roleMenuMapper.insertSelective(roleMenu);
                }
            }
        }
    }

    /**
     * 分页获取角色数据
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @return
     */
    @Override
    public PageInfo<RoleVO> findRoleList(Integer pageNum, Integer pageSize, String roleName) {
        PageHelper.startPage(pageNum,pageSize);
        Example o = new Example(SysRole.class);
        if (roleName!=null && !"".equals(roleName)){
            o.createCriteria().andLike("roleName","%"+roleName+"%");
        }
        List<SysRole> roles = roleMapper.selectByExample(o);
        List<RoleVO> roleVOS= RoleConverterUtil.converterToRoleVOList(roles);
        PageInfo<SysRole> pageInfo = new PageInfo<>(roles);
        PageInfo<RoleVO> pageInfos = new PageInfo<>(roleVOS);
        pageInfos.setTotal(pageInfo.getTotal());
        return pageInfos;
    }

    /**
     * 添加角色
     * @param roleVO
     */
    @Override
    public void add(RoleVO roleVO) {
        String roleName = roleVO.getRoleName();
        Example o = new Example(SysRole.class);
        o.createCriteria().andEqualTo("roleName",roleName);
        int i = roleMapper.selectCountByExample(o);
        if(i!=0){
            throw new ServiceException("该角色名已被占用");
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleVO,role);
        role.setCreateTime(new Date());
        role.setModifiedTime(new Date());
        role.setStatus(GlobalConfig.RoleStatusEnum.AVAILABLE.getStatusCode());//默认启用添加的角色
        roleMapper.insert(role);
    }

    /**
     * 编辑角色信息
     * @param id
     * @return
     */
    @Override
    public RoleVO edit(Long id) {
        SysRole role = roleMapper.selectByPrimaryKey(id);
        if(role==null){
            throw new ServiceException("编辑的角色不存在");
        }
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role,roleVO);
        return roleVO;
    }

    /**
     * 更新
     * @param id
     * @param roleVo
     */
    @Override
    public void update(Long id, RoleVO roleVo) {
        String roleName = roleVo.getRoleName();
        SysRole dbRole = roleMapper.selectByPrimaryKey(id);
        if(dbRole==null){
            throw new ServiceException("要更新的角色不存在");
        }
        Example o = new Example(SysRole.class);
        o.createCriteria().andEqualTo("roleName",roleName);
        List<SysRole> roles = roleMapper.selectByExample(o);
        if(!CollectionUtils.isEmpty(roles)){
            SysRole r = roles.get(0);
            if(!r.getId().equals(id)){
                throw new ServiceException("该角色名已被占用");
            }
        }
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleVo,role);
        role.setModifiedTime(new Date());
        roleMapper.updateByPrimaryKeySelective(role);
    }
}
