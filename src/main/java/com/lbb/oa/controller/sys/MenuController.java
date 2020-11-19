package com.lbb.oa.controller.sys;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.MenuVO;
import com.lbb.oa.service.sys.MenuService;
import com.lbb.oa.util.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lubingbing
 * @Date 2020/3/10 11:51
 * @Version 1.0
 **/
@RequestMapping("/menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 加载菜单树
     * @return
     */
    @GetMapping("/tree")
    public ResponseBean tree() {
        List<MenuNodeVO> menuTree = menuService.findMenuTree();
        List<Long> ids = menuService.findOpenIds();
        Map<String, Object> map = new HashMap<>();
        map.put("tree", menuTree);
        map.put("open", ids);
        return ResponseBean.success(map);
    }

    /**
     * 新增菜单/按钮
     *
     * @return
     */
    @PreAuthorize("hasAuthority('menu:add')")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody MenuVO menuVO) {
        SysMenu node = menuService.add(menuVO);
        Map<String, Object> map = new HashMap<>();
        map.put("id", node.getId());
        map.put("menuName", node.getMenuName());
        map.put("children", new ArrayList<>());
        map.put("icon", node.getIcon());
        return ResponseBean.success(map);
    }

    /**
     * 删除菜单/按钮
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('menu:delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseBean.success(id);
    }

    /**
     * 菜单详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('menu:edit')")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id) {
        MenuVO menuVO = menuService.edit(id);
        return ResponseBean.success(menuVO);
    }

    /**
     * 更新菜单
     *
     * @param id
     * @param menuVO
     * @return
     */
    @PreAuthorize("hasAuthority('menu:update')")
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody MenuVO menuVO) {
        menuService.update(id, menuVO);
        return ResponseBean.success(menuVO);
    }
}
