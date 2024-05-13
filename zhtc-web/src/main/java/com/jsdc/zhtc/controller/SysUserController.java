package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SysUserController
 * Description:
 * date: 2022/1/8 19:03
 *
 * @author wp
 */
@RestController
@RequestMapping("sysuser")
public class SysUserController {
    @Autowired
    private SysUserService userService;

    /**
     * create by wp at 2022/1/8 19:10
     * description: 分页查询
     *
     * @param sysUser
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getPage.do")
    public ResultInfo getPage(SysUser sysUser,
                              @RequestParam(defaultValue = "1") Integer pageIndex,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getPage(sysUser, pageIndex, pageSize);
    }

    /**
     * create by wp at 2022/1/10 9:44
     * description: 查询用户列表
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getList.do")
    public ResultInfo getList(SysUser sysUser) {
        return userService.list(sysUser);
    }

    /**
     * create by wp at 2022/1/8 19:12
     * description: 新增用户
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("save.do")
    @LogInfo(LogEnums.LOG_ADDUSER)
    public ResultInfo save(SysUser sysUser) {

        return userService.add(sysUser);
    }

    /**
     * create by wp at 2022/1/8 19:14
     * description: 编辑用户
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("edit.do")
    @LogInfo(LogEnums.LOG_UPDATEUSER)
    public ResultInfo edit(SysUser sysUser) {
        return userService.edit(sysUser);
    }

    /**
     * 修改密码
     * thr
     */
    @RequestMapping("updPass.do")
    public ResultInfo updPass(String newsure, String old) {
        return userService.updPass(newsure, old);
    }

    /**
     * create by wp at 2022/2/7 14:13
     * description: 启用/停用用户
     *
     * @param id
     * @param status
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("handleStatus.do")
    public ResultInfo handleStatus(Integer id, String status) {
        return userService.handleStatus(id, status);
    }

    /**
     * create by wp at 2022/1/8 19:15
     * description: 删除用户
     *
     * @param userId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("del.do")
    @LogInfo(LogEnums.LOG_DELETEUSER)
    public ResultInfo del(Integer userId) {
        return userService.del(userId);
    }

    /**
     * create by wp at 2022/1/8 19:16
     * description: 查询用户信息
     *
     * @param userId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getUser.do")
    public ResultInfo getUser(Integer userId) {
        return ResultInfo.success(userService.getUser(userId));
    }

    /**
     * 当前登录用户
     *
     * @return
     */
    @RequestMapping("loginUser.do")
    public ResultInfo loginUser() {
        return ResultInfo.success(userService.getUser());
    }

    /**
     * create by wp at 2022/1/8 20:46
     * description: 批量删除
     *
     * @param userIds
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("delAll.do")
    public ResultInfo delAll(String userIds) {
        return userService.delAll(userIds);
    }

    /**
     * create by wp at 2022/1/14 16:52
     * description: 切换模式（停车场、路段）
     *
     * @param type
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("switchModel.do")
    public ResultInfo switchModel(String type) {
        return userService.switchModel(type);
    }

    /**
     * 当前所属模式（停车场，路段）
     *
     * @return
     */
    @RequestMapping("isModel.do")
    public ResultInfo isModel() {
        return userService.isModel();
    }

    @RequestMapping("validateLoginName.do")
    public ResultInfo validateLoginName(String loginName, Integer userId) {
        return userService.validateLoginName(loginName, userId);
    }
}
