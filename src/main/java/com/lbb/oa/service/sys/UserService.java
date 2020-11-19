package com.lbb.oa.service.sys;

import com.github.pagehelper.PageInfo;
import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.UserEditVO;
import com.lbb.oa.pojo.sys.UserVO;

import java.util.List;

public interface UserService {

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    public SysUser getSysUserById(Long id);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    public SysUser selectByUsername(String username);

    /**
     * 加载菜单
     * @return
     */
    List<MenuNodeVO> findMenu();

    /**
     * 已拥有的角色ids
     * @param id 用户id
     * @return
     */
    List<Long> roles(Long id);

    /**
     * 分配角色
     *
     * @param id
     * @param rids
     */
    void assignRoles(Long id, Long[] rids);

    /**
     * 所有用户
     *
     * @return
     */
    List<SysUser> findAll();

    /**
     * 分页获取用户
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    PageInfo<UserVO> findUserList(Integer pageNum, Integer pageSize, String username,
                                  String nickname,
                                  String email,
                                  Integer sex,
                                  Long departmentId);

    /**
     * 删除用户
     * @param id
     */
    void deleteById(Long id);

    /**
     * 更新状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Boolean status);

    /**
     * 更新用户
     * @param id
     * @param userEditVO
     */
    SysUser update(Long id, UserEditVO userEditVO);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UserEditVO edit(Long id);

    /**
     * 添加用户
     * @param userVO
     */
    SysUser add(UserVO userVO);
}
