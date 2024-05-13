package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysRoleDao;
import com.jsdc.zhtc.mapper.SysRoleMapper;
import com.jsdc.zhtc.model.SysMenu;
import com.jsdc.zhtc.model.SysRole;
import com.jsdc.zhtc.model.SysRoleMenu;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SysRoleService
 * Description:
 * date: 2021/12/30 10:00
 *
 * @author wp
 */
@Service
@Transactional
public class SysRoleService extends BaseService<SysRoleDao, SysRole> {

    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * create by wp at 2021/12/30 17:07
     * description: 条件查询
     *
     * @param sysRole
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo list(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        getWrapper(sysRole, wrapper);
        return ResultInfo.success(selectList(wrapper));
    }

    /**
     * create by wp at 2021/12/30 17:07
     * description: 分页查询
     *
     * @param sysRole
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getPage(SysRole sysRole, int pageIndex, int pageSize) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        getWrapper(sysRole, wrapper);
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<SysRole> pageInfo = new PageInfo<>(selectList(wrapper));
        pageInfo.getList().forEach(x -> {
            Integer roleId = x.getId();
            List<SysMenu> menulist = getMenusByRoleId(roleId);
            menulist = menulist.stream().filter(y -> null != y).collect(Collectors.toList());
            x.setMenus(menulist);
        });
        return ResultInfo.success(pageInfo);
    }

    /**
     * create by wp at 2021/12/31 9:36
     * description: 根据角色id获取角色以及绑定的菜单信息
     *
     * @param roleId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getRole(int roleId) {
        SysRole sysRole = selectById(roleId);
        List<SysRoleMenu> roleMenus = roleMenuService.getRoleMenus(roleId);
        List<Integer> menuIds = roleMenus.stream().map(x -> x.getMenu_id()).collect(Collectors.toList());

        List<SysMenu> menus = menuService.getMenus(menuIds);
        List<SysMenu> roadMenuList = menus.stream().filter(x -> x.getMenu_type().equals("0")).collect(Collectors.toList());
        List<SysMenu> parkMenuList = menus.stream().filter(x -> x.getMenu_type().equals("1")).collect(Collectors.toList());
        sysRole.setMenus(menus);
        sysRole.setMenuIdList(menuIds);
        sysRole.setRoadMenuList(roadMenuList.stream().map(x -> x.getId()).collect(Collectors.toList()));
        sysRole.setParkMenuList(parkMenuList.stream().map(x -> x.getId()).collect(Collectors.toList()));
        return ResultInfo.success(sysRole);
    }

    /**
     * create by wp at 2021/12/30 17:07
     * description: 统一处理查询条件方法
     *
     * @param sysRole
     * @param wrapper
     * @return void
     */
    public void getWrapper(SysRole sysRole, LambdaQueryWrapper<SysRole> wrapper) {
        if (null != sysRole) {
            if (StringUtils.isNotEmpty(sysRole.getRole_name())) {
                wrapper.like(SysRole::getRole_name, sysRole.getRole_name());
            }
            if (StringUtils.isNotEmpty(sysRole.getRole_type())) {
                wrapper.eq(SysRole::getRole_type, sysRole.getRole_type());
            }
        }
        wrapper.eq(SysRole::getIs_del, GlobalData.ISDEL_NO);
    }

    /**
     * create by wp at 2022/2/7 15:18
     * description: 查询停车场管理员角色（系统默认、唯一）
     *
     * @return com.jsdc.zhtc.model.SysRole
     */
    public SysRole getParkRole() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRole_type, GlobalData.ROLE_PARK_TYPE);
        wrapper.eq(SysRole::getIs_del, GlobalData.ISDEL_NO);
        return selectOne(wrapper);
    }

    /**
     * create by wp at 2021/12/30 17:12
     * description: 新增角色 角色绑定菜单以菜单id逗号分隔，角色新增成功后绑定菜单
     *
     * @param sysRole
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(SysRole sysRole, int userId) {
        if (StringUtils.isEmpty(sysRole.getRole_name())) {
            return ResultInfo.error("角色名称不能为空！");
        }
        if (StringUtils.isEmpty(sysRole.getMenuIds())) {
            return ResultInfo.error("角色必须绑定菜单！");
        }
        sysRole.setCreate_time(new Date());
        sysRole.setCreate_user(userId);
        sysRole.setUpdate_time(new Date());
        sysRole.setUpdate_user(userId);
        sysRole.setIs_del(GlobalData.ISDEL_NO);
        sysRole.setRole_type(GlobalData.ROLE_ROAD_TYPE);
        if (insert(sysRole) > 0) {
            int roleId = sysRole.getId();
            List<String> menuIds = Arrays.asList(sysRole.getMenuIds().split(GlobalData.SPLIT_COMMA));
            for (String menuid : menuIds) {
                roleMenuService.addMap(menuid, roleId);
            }

            return ResultInfo.success("新增角色成功！");
        } else {
            return ResultInfo.error("新增角色失败！");
        }
    }

    /**
     * create by wp at 2021/12/30 17:15
     * description: 更新角色信息
     *
     * @param sysRole
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(SysRole sysRole, int userId) {
        if (null == sysRole.getId()) {
            return ResultInfo.error("更新角色id为空！");
        }
        SysRole originalRole = selectById(sysRole.getId());
        if (null == originalRole) {
            return ResultInfo.error("更新的角色不存在！");
        }
        BeanUtils.copyProperties(sysRole, originalRole);
        originalRole.setUpdate_user(userId);
        originalRole.setUpdate_time(new Date());
        if (updateById(originalRole) > 0) {
            if (StringUtils.isNotEmpty(sysRole.getMenuIds())) {
                roleMenuService.editMap(sysRole.getMenuIds(), sysRole.getId());
            }
            return ResultInfo.success("更新角色成功！");
        } else {
            return ResultInfo.error("更新角色失败！");
        }
    }

    /**
     * create by wp at 2021/12/31 13:51
     * description: 删除角色
     *
     * @param roleId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo del(int roleId) {
        if (userRoleService.isExist(roleId)) {
            return ResultInfo.error("该角色已绑定用户，请先解除绑定！");
        }
        UpdateWrapper<SysRole> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysRole::getIs_del, GlobalData.ISDEL_YES)
                .eq(SysRole::getId, roleId);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除角色成功！");
        } else {
            return ResultInfo.error("删除角色失败！");
        }
    }

    public ResultInfo delAll(String roleIds) {
        List<String> roleIdList = Arrays.asList(roleIds.split(","));
        UpdateWrapper<SysRole> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysRole::getIs_del, GlobalData.ISDEL_YES)
                .in(SysRole::getId, roleIdList);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除角色成功！");
        } else {
            return ResultInfo.error("删除角色失败！");
        }
    }

    /**
     * create by wp at 2022/1/8 10:48
     * description: 根据角色id查询关联的菜单
     *
     * @param roleId
     * @return java.util.List<com.jsdc.zhtc.model.SysMenu>
     */
    public List<SysMenu> getMenusByRoleId(Integer roleId) {
        return roleMapper.getMenusByRoleId(roleId);
    }

}
