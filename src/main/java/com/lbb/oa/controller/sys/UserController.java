package com.lbb.oa.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.RoleTransferItemVO;
import com.lbb.oa.pojo.sys.UserEditVO;
import com.lbb.oa.pojo.sys.UserVO;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.service.sys.UserService;
import com.lbb.oa.util.ResponseBean;
import com.lbb.oa.util.RoleConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/findMenu")
    public ResponseBean findMenu(){
        List<MenuNodeVO> menuTreeVOS = userService.findMenu();
        return ResponseBean.success(menuTreeVOS);
    }

    /**
     * 用户列表
     * @return
     */
    @PostMapping("/findUserList")
    public ResponseBean findUserList(@RequestBody JSONObject jsonObject) {
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String username = jsonObject.getString("username");
        String nickname = jsonObject.getString("nickname");
        String email = jsonObject.getString("email");
        Integer sex = jsonObject.getInteger("sex");
        Long departmentId = jsonObject.getLong("departmentId");
        PageInfo<UserVO> userList = userService.findUserList(pageNo, pageSize, username,nickname,email,sex,departmentId);
        return ResponseBean.success(userList);
    }

    /**
     * 分配角色
     * @param id
     * @param rids
     * @return
     */
    @PreAuthorize("hasAuthority('user:assign')")
    @PostMapping("/{id}/assignRoles")
    public ResponseBean assignRoles(@PathVariable Long id, @RequestBody Long[] rids) {
        userService.assignRoles(id, rids);
        return ResponseBean.success(id);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return
     */
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseBean.success(id);
    }

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    @PreAuthorize("hasAuthority('user:status')")
    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseBean updateStatus(@PathVariable Long id, @PathVariable Boolean status) {
        userService.updateStatus(id, status);
        return ResponseBean.success(id);
    }

    /**
     * 更新用户
     * @param id
     * @param userEditVO
     * @return
     */
    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody UserEditVO userEditVO) {
        SysUser sysUser = userService.update(id, userEditVO);
        return ResponseBean.success(sysUser);
    }

    /**
     * 编辑用户
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('user:edit')")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id) {
        UserEditVO userVO = userService.edit(id);
        return ResponseBean.success(userVO);
    }

    /**
     * 添加用户信息
     * @param userVO
     * @return
     */
    @PreAuthorize("hasAuthority('user:add')")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody UserVO userVO) {
        SysUser sysUser = userService.add(userVO);
        return ResponseBean.success(sysUser);
    }

    /**
     * 用户角色信息
     * @param id
     * @return
     */
    @GetMapping("/{id}/roles")
    public ResponseBean roles(@PathVariable Long id) {
        List<Long> values = userService.roles(id);
        List<SysRole> list = roleService.findAll();
        //转成前端需要的角色Item
        List<RoleTransferItemVO> items = RoleConverterUtil.converterToRoleTransferItem(list);
        Map<String, Object> map = new HashMap<>();
        map.put("roles", items);
        map.put("values", values);
        return ResponseBean.success(map);
    }
}
