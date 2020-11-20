package com.lbb.oa.service.sys.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.enums.ErrorCodeEnum;
import com.lbb.oa.enums.GlobalConfigEnum;
import com.lbb.oa.exception.ServiceException;
import com.lbb.oa.mapper.sys.*;
import com.lbb.oa.model.sys.*;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.SecuritySysUser;
import com.lbb.oa.pojo.sys.UserEditVO;
import com.lbb.oa.pojo.sys.UserVO;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.service.sys.UserService;
import com.lbb.oa.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserConverterUtil userConverter;

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    public SysUser getSysUserById(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser selectByUsername(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        return userMapper.selectOne(sysUser);
    }

    /**
     * 获取用户的菜单
     * @return
     */
    @Override
    public List<MenuNodeVO> findMenu() {
        List<SysMenu> menus=null;
        List<MenuNodeVO> menuNodeVOS=new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuritySysUser securitySysUser = (SecuritySysUser) authentication.getPrincipal();
        if(securitySysUser.getType()== GlobalConfigEnum.UserTypeEnum.SYSTEM_ADMIN.getTypeCode()){
            //超级管理员
            menus=menuMapper.selectAll();
        }else if(securitySysUser.getType()== GlobalConfigEnum.UserTypeEnum.SYSTEM_USER.getTypeCode()){
            //普通系统用户
            List<SysRole> roles = roleService.getRoleByUserId(securitySysUser.getId());
            //获取用户的菜单和按钮权限
            menus = menuService.findMenuByRoles(roles);
        }
        if(!CollectionUtils.isEmpty(menus)){
            menuNodeVOS= MenuConverterUtil.converterToMenuNodeVO(menus);
        }
        //构建树形菜单
        return MenuTreeBuilder.build(menuNodeVOS);
    }

    /**
     * 用户拥有的角色ID
     * @param id 用户id
     * @return
     */
    @Override
    public List<Long> roles(Long id) {
        SysUser user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            throw new ServiceException("该用户不存在");
        }
        Example o = new Example(SysUserRole.class);
        o.createCriteria().andEqualTo("userId",user.getId());
        List<SysUserRole> userRoleList = userRoleMapper.selectByExample(o);
        List<Long> roleIds=new ArrayList<>();
        if(!CollectionUtils.isEmpty(userRoleList)){
            for (SysUserRole userRole : userRoleList) {
                SysRole role = roleMapper.selectByPrimaryKey(userRole.getRoleId());
                if(role!=null){
                    roleIds.add(role.getId());
                }
            }
        }
        return roleIds;
    }

    /**
     * 分配角色
     * @param id 用户id
     * @param rids 角色数组
     */
    @Override
    public void assignRoles(Long id, Long[] rids) {
        //删除之前用户的所有角色
        SysUser user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            throw new ServiceException("用户不存在");
        }
        //删除之前分配的
        Example o = new Example(SysUserRole.class);
        o.createCriteria().andEqualTo("userId",user.getId());
        userRoleMapper.deleteByExample(o);
        //增加现在分配的
        if(rids!=null && rids.length>0){
            for (Long rid : rids) {
                SysRole role = roleMapper.selectByPrimaryKey(rid);
                if(role==null){
                    throw new ServiceException("roleId="+rid+",该角色不存在");
                }
                //判断角色状态
                if(role.getStatus()==0){
                    throw new ServiceException("roleName="+role.getRoleName()+",该角色已禁用");
                }
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(id);
                userRole.setRoleId(rid);
                userRoleMapper.insert(userRole);
            }
        }
    }

    /**
     * 查询所有的用户
     * @return
     */
    @Override
    public List<SysUser> findAll() {
        return userMapper.selectAll();
    }

    /**
     * 分页获取用户数据
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<UserVO> findUserList(Integer pageNum,
                                         Integer pageSize,
                                         String username,
                                         String nickname,
                                         String email,
                                         Integer sex,
                                         Long departmentId) {
        //已经拥有的
        PageHelper.startPage(pageNum,pageSize);
        Example o = new Example(SysUser.class);
        Example.Criteria criteria = o.createCriteria();
        if(username!=null&&!"".equals(username)){
            criteria.andLike("username","%"+username+"%");
        }
        if(nickname!=null&&!"".equals(nickname)){
            criteria.andLike("nickname","%"+nickname+"%");
        }
        if(email!=null&&!"".equals(email)){
            criteria.andLike("email","%"+email+"%");
        }
        if(sex!=null){
            criteria.andEqualTo("sex",sex);
        }
        if(departmentId!=null){
            criteria.andEqualTo("departmentId",departmentId);
        }
        criteria.andNotEqualTo("type",0);
        List<SysUser> userList = userMapper.selectByExample(o);
        List<UserVO> userVOS = userConverter.converterToUserVOList(userList);
        PageInfo<SysUser> pageInfo = new PageInfo<>(userList);
        PageInfo<UserVO> pageInfos = new PageInfo<>(userVOS);
        pageInfos.setTotal(pageInfo.getTotal());
        return pageInfos;
    }

    /**
     * 删除用户
     * @param id 用户ID
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        SysUser user = userMapper.selectByPrimaryKey(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuritySysUser securitySysUser = (SecuritySysUser) authentication.getPrincipal();
        if(user==null){
            throw new ServiceException("要删除的用户不存在");
        }
        if(user.getId().equals(securitySysUser.getId())){
            throw new ServiceException("不能删除当前登入用户");
        }
        userMapper.deleteByPrimaryKey(id);
        //删除对应[用户-角色]记录
        Example o = new Example(SysUserRole.class);
        o.createCriteria().andEqualTo("userId",id);
        userRoleMapper.deleteByExample(o);
    }

    /**
     * 更新用户禁用状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) {
        SysUser dbUser = userMapper.selectByPrimaryKey(id);
        if(dbUser==null){
            throw new ServiceException("要更新状态的用户不存在");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuritySysUser securitySysUser = (SecuritySysUser) authentication.getPrincipal();
        if(dbUser.getId().equals(securitySysUser.getId())){
            throw new ServiceException(ErrorCodeEnum.DoNotAllowToDisableTheCurrentUser);
        }else {
            SysUser t = new SysUser();
            t.setId(id);
            t.setStatus(status? GlobalConfigEnum.UserStatusEnum.DISABLE.getStatusCode() :
                    GlobalConfigEnum.UserStatusEnum.AVAILABLE.getStatusCode());
            userMapper.updateByPrimaryKeySelective(t);
        }
    }

    /**
     * 更新
     * @param id
     * @param userVO
     */
    @Transactional
    @Override
    public SysUser update(Long id,UserEditVO userVO) {
        SysUser dbUser = userMapper.selectByPrimaryKey(id);
        String username = userVO.getUsername();
        Long departmentId = userVO.getDepartmentId();
        if(dbUser==null){
            throw new ServiceException("要删除的用户不存在");
        }
        SysDepartment department = departmentMapper.selectByPrimaryKey(departmentId);
        if(department==null){
            throw new ServiceException("该部门不存在");
        }
        Example o = new Example(SysUser.class);
        o.createCriteria().andEqualTo("username",username);
        List<SysUser> users = userMapper.selectByExample(o);
        if(!CollectionUtils.isEmpty(users)){
            if(!users.get(0).getId().equals(id)){
                throw new ServiceException("该用户名已被占用");
            }
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userVO,user);
        user.setModifiedTime(new Date());
        user.setId(dbUser.getId());
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    /**
     * 编辑
     * @param id
     * @return
     */
    @Transactional
    @Override
    public UserEditVO edit(Long id) {
        SysUser user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            throw new ServiceException("要编辑的用户不存在");
        }
        UserEditVO userEditVO = new UserEditVO();
        BeanUtils.copyProperties(user,userEditVO);
        SysDepartment department = departmentMapper.selectByPrimaryKey(user.getDepartmentId());
        if(department!=null){
            userEditVO.setDepartmentId(department.getId());
        }
        return userEditVO;
    }

    /**
     * 添加用户
     * @param userVO
     */
    @Transactional
    @Override
    public SysUser add(UserVO userVO) {
        String username = userVO.getUsername();
        Long departmentId = userVO.getDepartmentId();
        Example o = new Example(SysUser.class);
        o.createCriteria().andEqualTo("username",username);
        int i = userMapper.selectCountByExample(o);
        if(i!=0){
            throw new ServiceException("该用户名已被占用");
        }
        SysDepartment department = departmentMapper.selectByPrimaryKey(departmentId);
        if(department==null){
            throw new ServiceException("该部门不存在");
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userVO,user);
        user.setPassword(new BCryptPasswordEncoder().encode(userVO.getPassword()));
        user.setModifiedTime(new Date());
        user.setCreateTime(new Date());
        user.setType(GlobalConfigEnum.UserTypeEnum.SYSTEM_USER.getTypeCode());//添加的用户默认是普通用户
        user.setStatus(GlobalConfigEnum.UserStatusEnum.AVAILABLE.getStatusCode());//添加的用户默认启用
        user.setAvatar("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1140572439,1755979681&fm=26&gp=0.jpg");
        userMapper.insert(user);
        return user;
    }
}
