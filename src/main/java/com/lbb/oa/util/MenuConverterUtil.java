package com.lbb.oa.util;

import com.lbb.oa.model.sys.SysMenu;
import com.lbb.oa.pojo.sys.MenuNodeVO;
import com.lbb.oa.pojo.sys.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuConverterUtil {

    /**
     * 转成menuVO(只包含菜单)List
     * @param menus
     * @return
     */
    public static List<MenuNodeVO> converterToMenuNodeVO(List<SysMenu> menus){
        //先过滤出用户的菜单
        List<MenuNodeVO> menuNodeVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(menus)){
            for (SysMenu menu : menus) {
                if(menu.getType()==0){
                    MenuNodeVO menuNodeVO = new MenuNodeVO();
                    BeanUtils.copyProperties(menu,menuNodeVO);
                    menuNodeVO.setDisabled(menu.getAvailable()==0);
                    menuNodeVOS.add(menuNodeVO);
                }
            }
        }
        return menuNodeVOS;
    }

    /**
     * 转成menuVO(菜单和按钮）
     * @param menus
     * @return
     */
    public static List<MenuNodeVO> converterToALLMenuNodeVO(List<SysMenu> menus){
        //先过滤出用户的菜单
        List<MenuNodeVO> menuNodeVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(menus)){
            for (SysMenu menu : menus) {
                MenuNodeVO menuNodeVO = new MenuNodeVO();
                BeanUtils.copyProperties(menu,menuNodeVO);
                menuNodeVO.setDisabled(menu.getAvailable()==0);
                menuNodeVOS.add(menuNodeVO);
            }
        }
        return menuNodeVOS;
    }

    /**
     * 转成menuVO(菜单和按钮）
     * @param menu
     * @return
     */
    public static MenuVO converterToMenuVO(SysMenu menu){
        MenuVO menuVO = new MenuVO();
        if(menu!=null){
            BeanUtils.copyProperties(menu,menuVO);
            menuVO.setDisabled(menu.getAvailable()==0);
        }
        return menuVO;
    }
}
