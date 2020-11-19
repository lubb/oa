package com.lbb.oa.util;

import com.lbb.oa.mapper.sys.DepartmentMapper;
import com.lbb.oa.model.sys.SysDepartment;
import com.lbb.oa.model.sys.SysUser;
import com.lbb.oa.pojo.sys.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lubingbing
 * @Date 2020/3/7 19:56
 * @Version 1.0
 **/
@Component
public class UserConverterUtil {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 转voList
     * @param users
     * @return
     */
    public  List<UserVO> converterToUserVOList(List<SysUser> users){
        List<UserVO> userVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(users)){
            for (SysUser user : users) {
                UserVO userVO = converterToUserVO(user);
                userVOS.add(userVO);
            }
        }
        return userVOS;
    }

    /**
     * 转vo
     * @return
     */
    public  UserVO converterToUserVO(SysUser user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setStatus(user.getStatus() == 0);
        SysDepartment department = departmentMapper.selectByPrimaryKey(user.getDepartmentId());
        if(department!=null&&department.getName()!=null){
            userVO.setDepartmentName(department.getName());
            userVO.setDepartmentId(department.getId());
        }
        return userVO;
    }
}
