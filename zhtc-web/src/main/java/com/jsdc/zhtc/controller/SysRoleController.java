package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.SysRole;
import com.jsdc.zhtc.service.SysRoleService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SysRoleController
 * Description: 系统角色管理
 * date: 2022/1/8 10:09
 *
 * @author wp
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserService userService;

    /**
     * create by wp at 2022/1/8 10:16
     * description: 分页查询角色列表
     *
     * @param sysRole
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getPage.do")
    public ResultInfo getPage(
            SysRole sysRole,
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return roleService.getPage(sysRole, pageIndex, pageSize);
    }

    @RequestMapping("getList.do")
    public ResultInfo getList(SysRole sysRole) {
        return roleService.list(sysRole);
    }

    /**
     * create by wp at 2022/1/8 19:01
     * description: 保存角色
     *
     * @param sysRole
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("save.do")
    public ResultInfo save(SysRole sysRole) {
        //SysUser user = userService.getUser();
        int userId = 1;
        return roleService.add(sysRole, userId);
    }

    /**
     * create by wp at 2022/1/8 19:01
     * description: 编辑角色
     *
     * @param sysRole
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("edit.do")
    public ResultInfo update(SysRole sysRole) {
        //SysUser user = userService.getUser();
        int userId = 1;
        return roleService.edit(sysRole, userId);
    }

    /**
     * create by wp at 2022/1/8 19:02
     * description: 获取角色信息
     *
     * @param roleId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getRole.do")
    public ResultInfo getRole(String roleId) {
        return roleService.getRole(Integer.parseInt(roleId));
    }

    /**
     * create by wp at 2022/1/8 19:01
     * description: 单个删除角色
     *
     * @param roleId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("del.do")
    public ResultInfo del(Integer roleId) {
        return roleService.del(roleId);
    }

    /**
     * create by wp at 2022/1/8 19:02
     * description: 批量删除角色
     *
     * @param roleIds
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("delAll.do")
    public ResultInfo delAll(String roleIds) {
        return roleService.delAll(roleIds);
    }

}
