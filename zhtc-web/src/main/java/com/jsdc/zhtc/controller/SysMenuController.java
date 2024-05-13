package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.service.SysMenuService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SysMenuController
 * Description:
 * date: 2022/1/8 13:40
 *
 * @author wp
 */
@RestController
@RequestMapping("sysmenu")
public class SysMenuController {
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysUserService userService;

    /**
     * create by wp at 2022/1/8 13:41
     * description: 获取菜单树形结构数据
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getMenuTree.do")
    public ResultInfo getMenuTree() {
        return menuService.getMenuTree();
    }

    @RequestMapping("getMenus.do")
    public ResultInfo getMenus() {
        return userService.getMenusbar();
    }
}
