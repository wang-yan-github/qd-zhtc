package com.jsdc.zhtc.service;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysMenuDao;
import com.jsdc.zhtc.model.SysMenu;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SysMenuService
 * Description:
 * date: 2021/12/30 9:41
 *
 * @author wp
 */
@Service
@Transactional
public class SysMenuService extends BaseService<SysMenuDao, SysMenu> {

    @Autowired
    private SysRoleMenuService roleMenuService;

    /**
     * create by wp at 2021/12/30 17:24
     * description: 条件查询
     *
     * @param sysMenu
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo list(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        getWrapper(sysMenu, wrapper);
        return ResultInfo.success(selectList(wrapper));
    }

    /**
     * create by wp at 2021/12/30 17:24
     * description: 分页查询
     *
     * @param sysMenu
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getPage(SysMenu sysMenu, int pageIndex, int pageSize) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        getWrapper(sysMenu, wrapper);
        return ResultInfo.success(selectPage(new Page<>(pageIndex, pageSize), wrapper));
    }

    /**
     * create by wp at 2021/12/30 17:25
     * description: 统一处理查询条件方法
     *
     * @param sysMenu
     * @param wrapper
     * @return void
     */
    public void getWrapper(SysMenu sysMenu, LambdaQueryWrapper<SysMenu> wrapper) {
        if (null != sysMenu) {
            if (StringUtils.isNotEmpty(sysMenu.getMenu_name())) {
                wrapper.like(SysMenu::getMenu_name, sysMenu.getMenu_name());
            }

            if (StringUtils.isNotEmpty(sysMenu.getIs_show())) {
                wrapper.eq(SysMenu::getIs_show, sysMenu.getIs_show());
            }
            if (null != sysMenu.getParent_id()) {
                wrapper.eq(SysMenu::getParent_id, sysMenu.getParent_id());
            }
        }
        wrapper.eq(SysMenu::getIs_del, GlobalData.ISDEL_NO);
    }

    /**
     * create by wp at 2021/12/30 17:29
     * description: 新增菜单
     *
     * @param sysMenu
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(SysMenu sysMenu, int userId) {
        if (StringUtils.isEmpty(sysMenu.getMenu_name())) {
            return ResultInfo.error("菜单名称不能为空！");
        }
        if (StringUtils.isEmpty(sysMenu.getHref())) {
            return ResultInfo.error("菜单链接不能为空！");
        }
        sysMenu.setCreate_time(new Date());
        sysMenu.setCreate_user(userId);
        sysMenu.setUpdate_time(new Date());
        sysMenu.setUpdate_user(userId);
        sysMenu.setIs_del(GlobalData.ISDEL_NO);
        if (insert(sysMenu) > 0) {
            return ResultInfo.success("新增菜单成功！");
        } else {
            return ResultInfo.error("新增菜单失败！");
        }
    }

    /**
     * create by wp at 2021/12/30 17:34
     * description: 修改菜单
     *
     * @param sysMenu
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(SysMenu sysMenu, int userId) {
        if (null == sysMenu.getId()) {
            return ResultInfo.error("菜单id为空！");
        }
        SysMenu originalMenu = selectById(sysMenu.getId());
        BeanUtils.copyProperties(sysMenu, originalMenu);
        originalMenu.setUpdate_user(userId);
        originalMenu.setUpdate_time(new Date());
        if (updateById(originalMenu) > 0) {
            return ResultInfo.success("编辑菜单成功！");
        } else {
            return ResultInfo.error("编辑菜单失败！");
        }
    }

    /**
     * create by wp at 2021/12/31 13:42
     * description: 删除菜单
     *
     * @param menuId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo del(int menuId) {
        if (roleMenuService.isExist(menuId)) {
            return ResultInfo.error("该菜单已绑定角色，请先取消角色绑定！");
        }
        UpdateWrapper<SysMenu> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysMenu::getIs_del, GlobalData.ISDEL_YES)
                .eq(SysMenu::getId, menuId);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除菜单成功！");
        } else {
            return ResultInfo.error("删除菜单失败！");
        }
    }

    /**
     * create by wp at 2021/12/31 9:33
     * description: 根据菜单id list查询所有菜单信息
     *
     * @param menuIds
     * @return java.util.List<com.jsdc.zhtc.model.SysMenu>
     */
    public List<SysMenu> getMenus(List<Integer> menuIds) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysMenu::getId, menuIds);
        wrapper.eq(SysMenu::getIs_del, GlobalData.ISDEL_NO);
        return selectList(wrapper);
    }

    public ResultInfo getMenuTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getIs_del, GlobalData.ISDEL_NO);
        List<SysMenu> menus = selectList(wrapper);
        List<SysMenu> roadMenus = menus.stream().filter(x -> x.getMenu_type().equals("0")).collect(Collectors.toList());
        List<SysMenu> parkMenus = menus.stream().filter(x -> x.getMenu_type().equals("1")).collect(Collectors.toList());
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setNameKey("label");
        treeNodeConfig.setDeep(3);
        List<Tree<String>> roadTreeNodes = TreeUtil.build(roadMenus, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(String.valueOf(treeNode.getId()));
                    tree.setParentId(String.valueOf(treeNode.getParent_id()));
                    tree.setWeight("");
                    tree.setName(treeNode.getMenu_name());
                });

        List<Tree<String>> parkTreeNodes = TreeUtil.build(parkMenus, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(String.valueOf(treeNode.getId()));
                    tree.setParentId(String.valueOf(treeNode.getParent_id()));
                    tree.setWeight("");
                    tree.setName(treeNode.getMenu_name());
                });

        JSONObject result = new JSONObject();
        result.put("roadTree", roadTreeNodes);
        result.put("parkTree", parkTreeNodes);
        return ResultInfo.success(result);
    }


}
