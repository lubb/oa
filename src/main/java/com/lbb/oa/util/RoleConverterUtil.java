package com.lbb.oa.util;

import com.lbb.oa.model.sys.SysRole;
import com.lbb.oa.pojo.sys.RoleTransferItemVO;
import com.lbb.oa.pojo.sys.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lubingbing
 * @Date 2020/3/9 16:26
 * @Version 1.0
 **/
public class RoleConverterUtil {

    /**
     * 转vo
     * @param roles
     * @return
     */
    public static List<RoleVO> converterToRoleVOList(List<SysRole> roles) {
        List<RoleVO> roleVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(roles)){
            for (SysRole role : roles) {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(role,roleVO);
                roleVO.setStatus(role.getStatus() == 0);
                roleVOS.add(roleVO);
            }
        }
        return roleVOS;
    }

    /**
     * 转成前端需要的角色Item
     * @param list
     * @return
     */
    public static List<RoleTransferItemVO> converterToRoleTransferItem(List<SysRole> list) {
        List<RoleTransferItemVO> itemVOList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for (SysRole role : list) {
                RoleTransferItemVO item = new RoleTransferItemVO();
                item.setLabel(role.getRoleName());
                item.setDisabled(role.getStatus()==0);
                item.setKey(role.getId());
                itemVOList.add(item);
            }
        }
        return itemVOList;
    }
}
