package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.SysRoleMenuDao;
import com.jsdc.zhtc.model.SysRoleMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: SysRoleMenuService
 * Description:
 * date: 2021/12/30 10:05
 *
 * @author wp
 */
@Service
@Transactional
public class SysRoleMenuService extends BaseService<SysRoleMenuDao, SysRoleMenu> {

    /**
     * create by wp at 2021/12/31 8:51
     * description: 新增角色、菜单关联关系
     *
     * @param menuId
     * @param roleId
     * @return void
     */
    public void addMap(String menuId, int roleId) {
        SysRoleMenu roleMenu = new SysRoleMenu();
        roleMenu.setMenu_id(Integer.parseInt(menuId));
        roleMenu.setRole_id(roleId);
        roleMenu.setIs_del(GlobalData.ISDEL_NO);
        insert(roleMenu);
    }

    /**
     * create by wp at 2021/12/31 9:22
     * description: 更新角色菜单绑定信息：1.先逻辑删除所有roleid记录 2.新增绑定记录
     *
     * @param menuIds
     * @param roleId
     * @return void
     */
    public void editMap(String menuIds, int roleId) {
        UpdateWrapper<SysRoleMenu> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysRoleMenu::getIs_del, GlobalData.ISDEL_YES)
                .eq(SysRoleMenu::getRole_id, roleId);
        update(null, wrapper);
        List<String> menuIdList = Arrays.asList(menuIds.split(GlobalData.SPLIT_COMMA));
        for (String menuid : menuIdList) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenu_id(Integer.parseInt(menuid));
            roleMenu.setRole_id(roleId);
            roleMenu.setIs_del(GlobalData.ISDEL_NO);
            insert(roleMenu);
        }
    }

    /**
     * create by wp at 2021/12/31 13:34
     * description: 根据菜单id判断是否存在已绑定角色的菜单
     *
     * @param menuId
     * @return boolean
     */
    public boolean isExist(int menuId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getMenu_id, menuId);
        wrapper.eq(SysRoleMenu::getIs_del, GlobalData.ISDEL_NO);
        Long count = selectCount(wrapper);
        return count > 0;
    }

    /**
     * create by wp at 2021/12/31 9:30
     * description: 根据角色id获取所有绑定的菜单
     *
     * @param roleId
     * @return java.util.List<com.jsdc.zhtc.model.SysRoleMenu>
     */
    public List<SysRoleMenu> getRoleMenus(int roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRole_id, roleId);
        wrapper.eq(SysRoleMenu::getIs_del, GlobalData.ISDEL_NO);
        return selectList(wrapper);
    }


}
