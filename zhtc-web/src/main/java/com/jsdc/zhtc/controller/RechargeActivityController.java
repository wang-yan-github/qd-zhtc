package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.RechargeActivity;
import com.jsdc.zhtc.model.RechargeActivityConfig;
import com.jsdc.zhtc.service.RechargeActivityService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RechargeActivityController
 * Description: 充值活动
 * date: 2021/12/30 14:00
 *
 * @author wh
 */
@Controller
@RequestMapping("rechargeActivity")
public class RechargeActivityController extends BaseController {

    @Autowired
    private RechargeActivityService rechargeActivityService;

    /**
     * 添加
     *
     * @param rechargeActivity
     * @author wh
     * @returndd
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(@RequestBody String rechargeActivity) throws ParseException {
        JSONObject jsonObject = JSONObject.parseObject(rechargeActivity);
        JSONObject jsonObject2 = JSONObject.parseObject(jsonObject.get("rechargeActivity").toString());
        RechargeActivity recharge = new RechargeActivity();
        recharge.setActivity_name(jsonObject2.get("activity_name").toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        recharge.setType(jsonObject2.get("type").toString());
        if (recharge.getType().equals("2")) {
            recharge.setStart_time(simpleDateFormat.parse(jsonObject2.get("start_time").toString()));
            recharge.setEnd_time(simpleDateFormat.parse(jsonObject2.get("end_time").toString()));
        }
        recharge.setSort(Integer.valueOf(jsonObject2.get("sort").toString()));
        JSONArray jsonArray = jsonObject2.getJSONArray("rechargeActivityConfigs");
        List<RechargeActivityConfig> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
            RechargeActivityConfig rechargeActivityConfig = new RechargeActivityConfig();
            rechargeActivityConfig.setRecharge_amount(jsonObject3.get("recharge_amount").toString());
            rechargeActivityConfig.setAdditional_amount(jsonObject3.get("additional_amount").toString());
            list.add(rechargeActivityConfig);
        }
        recharge.setRechargeActivityConfigs(list);
        return rechargeActivityService.add(recharge);
    }

    /**
     * 编辑
     *
     * @param rechargeActivity
     * @author wh
     * @returndd
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(@RequestBody String rechargeActivity) throws ParseException {
        JSONObject jsonObject = JSONObject.parseObject(rechargeActivity);
        JSONObject jsonObject2 = JSONObject.parseObject(jsonObject.get("rechargeActivity").toString());
        RechargeActivity recharge = new RechargeActivity();
        recharge.setId(Integer.valueOf(jsonObject2.get("id").toString()));
        recharge.setActivity_name(jsonObject2.get("activity_name").toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        recharge.setType(jsonObject2.get("type").toString());
        if (recharge.getType().equals("2")) {
            recharge.setStart_time(simpleDateFormat.parse(jsonObject2.get("start_time").toString()));
            recharge.setEnd_time(simpleDateFormat.parse(jsonObject2.get("end_time").toString()));
        }
        recharge.setSort(Integer.valueOf(jsonObject2.get("sort").toString()));
        JSONArray jsonArray = jsonObject2.getJSONArray("rechargeActivityConfigs");
        List<RechargeActivityConfig> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
            RechargeActivityConfig rechargeActivityConfig = new RechargeActivityConfig();
            rechargeActivityConfig.setRecharge_amount(jsonObject3.get("recharge_amount").toString());
            rechargeActivityConfig.setAdditional_amount(jsonObject3.get("additional_amount").toString());
            list.add(rechargeActivityConfig);
        }
        recharge.setRechargeActivityConfigs(list);
        return rechargeActivityService.edit(recharge);
    }

    /**
     * 删除启用禁用
     *
     * @param rechargeActivity
     * @author wh
     * @returndd
     */
    @RequestMapping(value = "toDel.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toDel(RechargeActivity rechargeActivity) {
        return rechargeActivityService.edit(rechargeActivity);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @author wh
     */
    @RequestMapping(value = "toDelAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toDelAll(String ids) {
        return rechargeActivityService.toDelAll(ids);
    }

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param rechargeActivity
     * @return
     * @author wh
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, RechargeActivity rechargeActivity) {
        PageInfo<RechargeActivity> rechargeActivityPageInfo = rechargeActivityService.toList(pageIndex, pageSize, rechargeActivity);
        return ResultInfo.success(rechargeActivityPageInfo);
    }

    /**
     * 列表查询
     *
     * @param id
     * @return
     * @author wh
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id) {
        RechargeActivity rechargeActivity = rechargeActivityService.getById(id);
        return ResultInfo.success(rechargeActivity);
    }
}
