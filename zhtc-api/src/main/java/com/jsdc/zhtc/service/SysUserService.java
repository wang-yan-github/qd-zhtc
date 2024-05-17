package com.jsdc.zhtc.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.MD5Utils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysUserDao;
import com.jsdc.zhtc.mapper.SysUserMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.utils.RedisUtils;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;
import static java.util.Comparator.comparing;

@Service
@Transactional
@SuppressWarnings("ALL")
public class SysUserService extends BaseService<SysUserDao, SysUser> {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysMenuService menuService;

    /**
     * 登录
     */
    public SaTokenInfo login(String userName, String passWord) {
        QueryWrapper<SysUser> queryWrapper = dao.queryByName(userName, MD5Utils.getMD5(passWord));
        SysUser user = selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        StpUtil.login(user.getId());
        SaTokenInfo token = StpUtil.getTokenInfo();
        StpUtil.getSession().set(user.getId().toString(), user);
        return token;
    }

    /**
     * create by wp at 2021/12/30 14:06
     * description: 获取当前登录用户信息
     *
     * @return com.jsdc.zhtc.model.SysUser
     */
    public SysUser getUser() {
        return (SysUser) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
    }

    /**
     * create by wp at 2021/12/31 11:39
     * description: 查询当前登录用户信息，带出角色信息
     *
     * @return com.jsdc.zhtc.model.SysUser
     */
    public SysUser getUserAndRole() {
        SysUser sysUser = getUser();
        SysUserRole userRole = userRoleService.getMap(sysUser.getId());
        SysRole role = roleService.selectById(userRole.getRole_id());
        sysUser.setSysRole(role);
        return sysUser;
    }

    //判断缓存查询是否包含路段、停车场角色
//    public String redisRoadOrPark() {
//        return (String) RedisUtils.getBeanValue(getUser().getLogin_name() + "_sys");
//    }

    /**
     * create by wp at 2021/12/30 14:07
     * description: 登出
     *
     * @return void
     */
    public void loginOut() {
        Subject subject = SecurityUtils.getSubject();

        subject.logout();
    }

    /**
     * create by wp at 2021/12/30 13:32
     * description: 条件查询
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo list(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //处理查询条件
        getWrapper(sysUser, wrapper);
        List<SysUser> users = selectList(wrapper);
        return ResultInfo.success(users);
    }

    /**
     * create by wp at 2021/12/30 14:04
     * description: 分页查询
     *
     * @param sysUser
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getPage(SysUser sysUser, int pageIndex, int pageSize) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //处理查询条件
        getWrapper(sysUser, wrapper);
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<SysUser> pageInfo = new PageInfo<SysUser>(selectList(wrapper));
        pageInfo.getList().forEach(x -> {
            int userId = x.getId();
            List<SysRole> roles = getRoles(userId);
            x.setSysRoles(roles);
        });
        return ResultInfo.success(pageInfo);
    }

    /**
     * create by wp at 2021/12/30 14:05
     * description: 统一处理查询条件方法
     *
     * @param sysUser
     * @param wrapper
     * @return void
     */
    public void getWrapper(SysUser sysUser, LambdaQueryWrapper<SysUser> wrapper) {
        if (null != sysUser) {
            if (StringUtils.isNotEmpty(sysUser.getId_card())) {
                wrapper.like(SysUser::getId_card, sysUser.getId_card());
            }
            if (StringUtils.isNotEmpty(sysUser.getLogin_name())) {
                wrapper.like(SysUser::getLogin_name, sysUser.getLogin_name());
            }
            if (StringUtils.isNotEmpty(sysUser.getUser_name())) {
                wrapper.like(SysUser::getUser_name, sysUser.getUser_name());
            }
            if (StringUtils.isNotEmpty(sysUser.getPhone())) {
                wrapper.like(SysUser::getPhone, sysUser.getPhone());
            }
            if (StringUtils.isNotEmpty(sysUser.getSex())) {
                wrapper.eq(SysUser::getSex, sysUser.getSex());
            }
            if (StringUtils.isNotEmpty(sysUser.getStatus())) {
                wrapper.eq(SysUser::getStatus, sysUser.getStatus());
            }
        }
        wrapper.eq(SysUser::getIs_del, GlobalData.ISDEL_NO);
    }

    /**
     * create by wp at 2021/12/30 11:14
     * description: 新增用户
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(SysUser sysUser) {
        if (!verifyUserInfo(sysUser)) {
            return ResultInfo.error("必填字段不能为空！");
        }
        SysUser user = getUser();
        sysUser.setPassword(MD5Utils.getMD5(sysUser.getPassword()));
        sysUser.setStatus(GlobalData.ENABLE);
        sysUser.setCreate_time(new Date());
        sysUser.setCreate_user(user.getId());
        sysUser.setUpdate_time(new Date());
        sysUser.setUpdate_user(user.getId());
        sysUser.setIs_del(GlobalData.ISDEL_NO);
        if (insert(sysUser) > 0) {
            if (StringUtils.equals(sysUser.getUser_type(), "0")) {
                userRoleService.addMap(sysUser.getId(), sysUser.getRoleId());
            } else {
                SysRole sysRole = roleService.getParkRole();
                userRoleService.addMap(sysUser.getId(), sysRole.getId());
            }

            return ResultInfo.success("新增用户成功！", "新增用户：" + sysUser.getUser_name());
        } else {
            return ResultInfo.error("新增用户失败！");
        }
    }

    /**
     * create by wp at 2021/12/31 9:59
     * description: 验证必填字段是否为空
     *
     * @param sysUser
     * @return boolean
     */
    public boolean verifyUserInfo(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getLogin_name()) || StringUtils.isEmpty(sysUser.getPassword())
                || (null == sysUser.getRoleId() && StringUtils.equals(sysUser.getUser_type(), "0"))) {
            return false;
        }
        return true;
    }

    /**
     * create by wp at 2021/12/30 17:42
     * description: 更新用户信息
     *
     * @param sysUser
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(SysUser sysUser) {
        if (null == sysUser.getId()) {
            return ResultInfo.error("更新用户id为空！");
        }
        if (!verifyUserInfo(sysUser)) {
            return ResultInfo.error("必填字段不能为空！");
        }
        int userId = sysUser.getId();
        SysUser originalUser = selectById(userId);
        if (null == originalUser) {
            return ResultInfo.error("该用户不存在！");
        }
        BeanUtils.copyProperties(sysUser, originalUser);
        SysUser loginUser = getUser();
        originalUser.setUpdate_user(userId);
        originalUser.setUpdate_time(new Date());
        if (updateById(originalUser) > 0) {
            userRoleService.editMap(userId, sysUser.getRoleId());
            return ResultInfo.success("更新用户信息成功！", "修改用户：" + originalUser.getUser_name());
        } else {
            return ResultInfo.error("更新用户信息失败！");
        }
    }

    /**
     * 修改密码
     * thr
     */
    public ResultInfo updPass(String newPass, String oldPass) {
        SysUser sysUser = getUser();
        if (empty(sysUser) || empty(sysUser.getId())) {
            return ResultInfo.error("更新用户id为空！");
        }
        if (sysUser.getPassword().equals(MD5Utils.getMD5(oldPass))) {//原密码是否正确
            if (sysUser.getPassword().equals(MD5Utils.getMD5(newPass))) {//原密码与新密码不能一致
                return ResultInfo.error("修改密码不能与原始密码一致！");
            } else {
                sysUser.setUpdate_user(sysUser.getId());
                sysUser.setUpdate_time(new Date());
                sysUser.setPassword(MD5Utils.getMD5(newPass));
                if (updateById(sysUser) > 0) {
                    return ResultInfo.success("修改成功,跳转到登录页面,请重新登录！");
                } else {
                    return ResultInfo.error("密码修改失败！");
                }
            }
        } else {
            return ResultInfo.error("原密码不正确！");
        }

    }

    public ResultInfo handleStatus(Integer id, String status) {
        SysUser sysUser = selectById(id);
        if (sysUser.getId().equals(getUser().getId())) {
            return ResultInfo.error("本机用户不能对自己进行【启用/禁用】操作");
        } else {
            sysUser.setStatus(status);
            if (updateById(sysUser) > 0) {
                return ResultInfo.success();
            } else {
                if (StringUtils.equals(status, "0")) {
                    return ResultInfo.error("禁用失败");
                } else {
                    return ResultInfo.error("启用失败");
                }
            }
        }
    }

    /**
     * create by wp at 2021/12/31 13:28
     * description: 删除用户
     *
     * @param userId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo del(int userId) {
        SysUser user = getUser();
        SysUser sysUser = selectById(userId);
        if (user.getId().equals(sysUser.getId())) {
            return ResultInfo.error("本机用户不能对自己进行【删除】操作");
        } else {
            sysUser.setIs_del(GlobalData.ISDEL_YES);
            sysUser.setUpdate_time(new Date());
            sysUser.setUpdate_user(1);
            if (updateById(sysUser) > 0) {
                userRoleService.delMap(userId);
                return ResultInfo.success("删除用户成功", "删除用户：" + sysUser.getUser_name());
            } else {
                return ResultInfo.error("删除用户失败！");
            }
        }
    }

    /**
     * create by wp at 2021/12/31 9:59
     * description: 根据用户id获取用户信息以及角色信息
     *
     * @param userId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public SysUser getUser(Integer userId) {
        SysUser user = selectById(userId);
        SysUserRole userRole = userRoleService.getMap(userId);
        if (null != userRole) {
            SysRole role = roleService.selectById(userRole.getRole_id());
            user.setSysRole(role);
            user.setRoleId(role.getId());
        }
        return user;
    }

    public List<SysRole> getRoles(Integer userId) {
        return sysUserMapper.getRoles(userId);
    }

    public ResultInfo delAll(String userIds) {
        List<String> userList = Arrays.asList(userIds.split(","));
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(SysUser::getIs_del, GlobalData.ISDEL_YES)
                .in(SysUser::getId, userList);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除用户成功！");
        } else {
            return ResultInfo.error("删除用户失败！");
        }
    }

    public ResultInfo getMenusbar() {
        SysUser sysUser = getUser();
        SysUserRole userRole = userRoleService.getMap(sysUser.getId());
        int roleId = userRole.getRole_id();
        List<SysRoleMenu> roleMenus = roleMenuService.getRoleMenus(roleId);
        List<Integer> menuIds = roleMenus.stream().map(x -> x.getMenu_id()).collect(Collectors.toList());
        List<SysMenu> menuAll = menuService.getMenus(menuIds);
        List<SysMenu> menus = menuAll.stream().filter(x -> x.getIs_button() == 0).collect(Collectors.toList());
        List<String> authoritys = menuAll.stream().filter(x -> x.getIs_button() == 1 && "1".equals(x.getMenu_type())).map(x -> x.getCode()).collect(Collectors.toList());
        //路段菜单
        List<SysMenu> topMenus = menus.stream().filter(x -> x.getParent_id() == 0 && x.getMenu_type().equals("0")).collect(Collectors.toList());
        Collections.sort(topMenus, comparing(SysMenu::getSort));
        JSONObject item = new JSONObject();
        JSONArray ja = new JSONArray();
        for (SysMenu sysMenu : topMenus) {
            JSONObject top = new JSONObject();
            top.put("id", sysMenu.getId());
            top.put("icon", sysMenu.getIcon());
            top.put("title", sysMenu.getMenu_name());
            ja.add(top);
            int parentId = sysMenu.getId();
            List<SysMenu> children = menus.stream().filter(x -> x.getParent_id() == parentId).collect(Collectors.toList());
            Collections.sort(children, comparing(SysMenu::getSort));
            JSONArray childs = new JSONArray();
            for (SysMenu sysMenu1 : children) {
                JSONObject child = new JSONObject();
                child.put("id", sysMenu1.getId());
                child.put("icon", sysMenu1.getIcon());
                child.put("title", sysMenu1.getMenu_name());
                child.put("index", sysMenu1.getHref());
                childs.add(child);
            }
            item.put(String.valueOf(parentId), childs);
        }
        JSONObject result = new JSONObject();
        Integer firstRoadParentNode = 0;
        Integer firstRoadChildNode = 0;
        if (ja.size() > 0) {
            firstRoadParentNode = ja.getJSONObject(0).getInteger("id");
            firstRoadChildNode = item.getJSONArray(String.valueOf(firstRoadParentNode)).getJSONObject(0).getInteger("id");
        }

        result.put("top", ja);
        result.put("children", item);
        result.put("firstRoadParentNode", firstRoadParentNode);
        result.put("firstRoadChildNode", firstRoadChildNode);
        //停车场菜单
        List<SysMenu> parktopMenus = menus.stream().filter(x -> x.getParent_id() == 0 && x.getMenu_type().equals("1")).collect(Collectors.toList());
        Collections.sort(parktopMenus, comparing(SysMenu::getSort));
        JSONObject parkitem = new JSONObject();
        JSONArray parkja = new JSONArray();
        for (SysMenu sysMenu : parktopMenus) {
            JSONObject top = new JSONObject();
            top.put("id", sysMenu.getId());
            top.put("icon", sysMenu.getIcon());
            top.put("title", sysMenu.getMenu_name());
            parkja.add(top);
            int parentId = sysMenu.getId();
            List<SysMenu> children = menus.stream().filter(x -> x.getParent_id() == parentId).collect(Collectors.toList());
            Collections.sort(children, comparing(SysMenu::getSort));
            JSONArray childs = new JSONArray();
            for (SysMenu sysMenu1 : children) {
                JSONObject child = new JSONObject();
                child.put("id", sysMenu1.getId());
                child.put("icon", sysMenu1.getIcon());
                child.put("title", sysMenu1.getMenu_name());
                child.put("index", sysMenu1.getHref());
                childs.add(child);
            }
            parkitem.put(String.valueOf(parentId), childs);
        }
        JSONObject parkresult = new JSONObject();
        Integer firstParkParentNode = 0;
        Integer firstParkChildNode = 0;
        if (parkja.size() > 0) {
            firstParkParentNode = parkja.getJSONObject(0).getInteger("id");
            firstParkChildNode = parkitem.getJSONArray(String.valueOf(firstParkParentNode)).getJSONObject(0).getInteger("id");
        }

        parkresult.put("top", parkja);
        parkresult.put("children", parkitem);
        parkresult.put("firstParkParentNode", firstParkParentNode);
        parkresult.put("firstParkChildNode", firstParkChildNode);
        String key = sysUser.getLogin_name() + "_sys";
        String isRP = "1";
        JSONObject res = new JSONObject();
        if (GlobalData.PARKING_TYPE_ROAD.equals(sysUser.getUser_type()) && notEmpty(isRP)) {
            RedisUtils.setBeanValue(key, isRP);
            res.put("isRP", isRP);
        } else {
            if (result.size() > 0) {
                RedisUtils.setBeanValue(key, GlobalData.PARKING_TYPE_ROAD);
            } else {
                RedisUtils.setBeanValue(key, GlobalData.PARKING_TYPE_PLAT);
            }
        }
        res.put("roadmenu", result);
        res.put("parkmenu", parkresult);
        res.put("userType", sysUser.getUser_type());
        res.put("authoritys", authoritys);
        return ResultInfo.success(res);
    }

    public ResultInfo switchModel(String type) {
        try {
            SysUser sysUser = getUser();
            String key = sysUser.getLogin_name() + "_sys";
            RedisUtils.setBeanValue(key, type);
            System.out.println("当前模式：" + RedisUtils.getBeanValue(key));
            return ResultInfo.success("切换成功");
        } catch (Exception e) {
            return ResultInfo.error("切换失败");
        }
    }

    public ResultInfo isModel() {
        SysUser sysUser = getUser();
        String key = sysUser.getLogin_name() + "_sys";

        return ResultInfo.success(RedisUtils.getBeanValue(key));
    }

    public ResultInfo validateLoginName(String loginName, Integer userId) {

        if (StringUtils.isEmpty(loginName)) {
            return ResultInfo.success(true);
        }
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLogin_name, loginName);
        wrapper.eq(SysUser::getIs_del, GlobalData.ISDEL_NO);
        if (null != userId) {
            wrapper.ne(SysUser::getId, userId);
        }
        if (selectCount(wrapper) > 0) {
            return ResultInfo.success(false);
        } else {
            return ResultInfo.success(true);
        }
    }

    public ResultInfo logout() {
        SysUser sysUser = getUser();
        //String token = StpUtil.getTokenValue();
        StpUtil.logout(sysUser.getId());
        //StpUtil.getSession().logoutByTokenSignCountToZero();
        return ResultInfo.success();
    }
}
