package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.SysUserRoleDao;
import com.jsdc.zhtc.model.SysUserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: SysUserRoleService
 * Description: 用户-角色管理
 * date: 2021/12/30 9:27
 *
 * @author wp
 */
@Service
@Transactional
public class SysUserRoleService extends BaseService<SysUserRoleDao, SysUserRole> {
    /**
     * create by wp at 2021/12/31 9:46
     * description: 新增用户-角色关联关系 1对1
     *
     * @param userId
     * @param roleId
     * @return void
     */
    public void addMap(int userId, int roleId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setRole_id(roleId);
        userRole.setUser_id(userId);
        userRole.setIs_del(GlobalData.ISDEL_NO);
        insert(userRole);
    }

    /**
     * create by wp at 2021/12/31 9:50
     * description: 更新用户-角色关联关系 1对1
     *
     * @param userId
     * @param roleId
     * @return void
     */
    public void editMap(int userId, int roleId) {
        UpdateWrapper<SysUserRole> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysUserRole::getIs_del, GlobalData.ISDEL_YES)
                .eq(SysUserRole::getUser_id, userId);
        update(null, wrapper);
        addMap(userId, roleId);
    }

    public void delMap(int userId) {
        UpdateWrapper<SysUserRole> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysUserRole::getIs_del, GlobalData.ISDEL_YES)
                .eq(SysUserRole::getUser_id, userId);
        update(null, wrapper);
    }

    /**
     * create by wp at 2021/12/31 9:56
     * description: 根据用户Id获取用户-角色关联信息
     *
     * @param userId
     * @return com.jsdc.zhtc.model.SysUserRole
     */
    public SysUserRole getMap(int userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUser_id, userId);
        wrapper.eq(SysUserRole::getIs_del, GlobalData.ISDEL_NO);
        return selectOne(wrapper);
    }

    /**
     * create by wp at 2021/12/31 13:48
     * description: 判断是否存在已绑定用户的角色，删除角色前应先解除绑定关系
     *
     * @param roleId
     * @return boolean
     */
    public boolean isExist(int roleId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRole_id, roleId).eq(SysUserRole::getIs_del, GlobalData.ISDEL_NO);
        Long count = selectCount(wrapper);
        return count > 0;
    }
}
