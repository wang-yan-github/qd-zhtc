package com.jsdc.zhtc.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class MainController extends BaseController {

    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    public String login() {
        return "login";
    }


    @RequestMapping(value = "sss.do")
    @ResponseBody
    public String sss(MultipartFile file) throws IOException {
        JSONObject jsonObject = new JSONObject();
        Page<SysUser> list = userService.selectPage(new Page<>(1, 10), new QueryWrapper<>());
        jsonObject.put("msg", list);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public String loginPost(String consumer, String cypher) {
        JSONObject jsonObject = new JSONObject();
        if (empty(consumer)) {
            jsonObject.put("success", false);
            jsonObject.put("msg", "用户名不能为空");
        }
        if (empty(cypher)) {
            jsonObject.put("success", false);
            jsonObject.put("msg", "密码不能为空");
        }
        if (notEmpty(consumer) && notEmpty(cypher)) {
            SaTokenInfo token = userService.login(consumer, cypher);

            if (null != token) {
                SysUser user = userService.getUser();
                jsonObject.put("success", true);
                jsonObject.put("msg", "登录成功");
                jsonObject.put("token", token);
                jsonObject.put("user", user);
            } else {
                jsonObject.put("success", false);
                jsonObject.put("msg", "用户名或密码错误");
            }
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:main.do";
    }

    @RequestMapping(value = "main.do")
    public String main(Model model) {
        return "index";
    }


    @RequestMapping(value = "top.do")
    public String top() {
        return "top";
    }

    @RequestMapping(value = "left.do")
    public String left() {
        return "left";
    }

    @RequestMapping(value = "open.do", method = RequestMethod.GET)
    public String open() {
        return "open";
    }

    @RequestMapping("logout.do")
    @ResponseBody
    public ResultInfo logout() {
        return userService.logout();
    }
}
