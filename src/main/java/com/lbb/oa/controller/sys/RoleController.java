package com.lbb.oa.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.RoleVO;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.service.sys.RoleService;
import com.lbb.oa.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lubingbing
 * @Date 2020/3/9 16:21
 * @Version 1.0
 **/
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    /**
     * 角色列表
     * @return
     */
    @PostMapping("/findRoleList")
    public ResponseBean findRoleList(@RequestBody JSONObject jsonObject) {
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String roleName = jsonObject.getString("roleName");
        PageInfo<RoleVO> roleList = roleService.findRoleList(pageNo, pageSize, roleName);
        return ResponseBean.success(roleList);
    }

    /**
     * 角色拥有的菜单权限id和菜单树
     * @param id
     * @return
     */
    @GetMapping("/findRoleMenu/{id}")
    public ResponseBean findRoleMenu(@PathVariable Long id) {
        List<MenuNodeVO> tree = menuService.findMenuTree();
        //角色拥有的菜单id
        List<Long> mids = roleService.findMenuIdsByRoleId(id);
        List<Long> ids = menuService.findOpenIds();
        Map<String, Object> map = new HashMap<>();
        map.put("tree", tree);
        map.put("mids", mids);
        map.put("open", ids);
        return ResponseBean.success(map);
    }

    /**
     * 添加角色信息
     *
     * @param roleVO
     * @return
     */
    @PreAuthorize("hasAuthority('role:add')")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody RoleVO roleVO) {
        roleService.add(roleVO);
        return ResponseBean.success(roleVO);
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return
     */
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseBean.success(id);
    }

    /**
     * 编辑角色信息
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('role:edit')")
    public ResponseBean edit(@PathVariable Long id) {
        RoleVO roleVO = roleService.edit(id);
        return ResponseBean.success(roleVO);
    }

    /**
     * 更新角色
     * @param id
     * @param roleVo
     * @return
     */
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody RoleVO roleVo) {
        roleService.update(id, roleVo);
        return ResponseBean.success(roleVo);
    }

    /**
     * 角色授权
     * @param mids
     * @return
     */
    @PostMapping("/authority/{id}")
    @PreAuthorize("hasAuthority('role:authority')")
    public ResponseBean authority(@PathVariable Long id, @RequestBody Long[] mids) {
        roleService.authority(id, mids);
        return ResponseBean.success(id);
    }

    /**
     * 更新角色状态
     * @param id
     * @param status
     * @return
     */
    @PreAuthorize("hasAuthority('role:status')")
    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseBean updateStatus(@PathVariable Long id, @PathVariable Boolean status) {
        roleService.updateStatus(id, status);
        return ResponseBean.success(id);
    }
}
