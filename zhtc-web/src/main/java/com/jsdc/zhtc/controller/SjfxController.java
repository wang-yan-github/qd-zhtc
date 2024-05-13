package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.service.SjfxService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据分析
 */
@Controller
@RequestMapping("/sjfx")
public class SjfxController extends BaseController {

    @Autowired
    private SjfxService sjfxService;


    @RequestMapping(value = "index.do")
    public String index(Model model, String type) {
        return "index";
    }


    /**
     * 图标数据接口
     */
    @RequestMapping(value = "getDataList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getDataList(String type) {
        JSONObject jsonObject = new JSONObject();
        //各区域在停车辆总数排名TOP5
        jsonObject.put("carTop5Data", sjfxService.getCarTop5Data(type, "year"));
        //昨日停车时长占比分析
        jsonObject.put("parkTimeData", sjfxService.getParkTimeData(type));
        //近五日车位占用量及趋势
        jsonObject.put("participatingData", sjfxService.getParticipatingData(type));

        //各区域收费总额排名TOP5
        JSONObject totalChargesTop5Data = sjfxService.getTotalChargesTop5Data(type, "year");
        jsonObject.put("totalChargesTop5Data", totalChargesTop5Data);

        //今日收费总额趋势分析
        JSONObject totalChargesData = sjfxService.getTotalChargesData(type);
        jsonObject.put("totalChargesData", totalChargesData);

        //各区域报警数量排名TOP5
//        JSONObject errorParkData = sjfxService.getErrorParkData(type);
//        jsonObject.put("errorParkData", errorParkData);

        return ResultInfo.success(jsonObject);
    }


    /**
     * @param type: 0:路测 1:停车场
     * @return ResultInfo
     * @author wp
     * @description 昨日停车时长占比分析
     * @date 2022/7/12 14:06
     */
    @RequestMapping(value = "restimeProportion.do")
    @ResponseBody
    public ResultInfo restimeProportion(String type) {
        List<String> data = sjfxService.getParkTimeData(type);
        return ResultInfo.success(data);
    }

    /**
     * 停车场
     * 支付方式占比
     *
     * @return
     */
    @RequestMapping(value = "parkPaymentTypeStatistics.do")
    @ResponseBody
    public ResultInfo parkPaymentTypeStatistics() {
        List<Map<String, Object>> list = sjfxService.parkPaymentTypeStatistics();
        List<String> paymentTypeName = new ArrayList<>();
        List<String> sumamount = new ArrayList<>();
        for (Map<String, Object> map : list) {
            paymentTypeName.add(String.valueOf(map.get("name")));
            sumamount.add(String.valueOf(map.get("value")));
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("types", paymentTypeName);
        result.put("amount", sumamount);
        return ResultInfo.success(result);
    }

    /**
     * 停车场收费排行
     *
     * @return
     */
    @RequestMapping(value = "parkChargeRank.do")
    @ResponseBody
    public ResultInfo parkChargeRank() {
        return ResultInfo.success(sjfxService.parkChargeRank());
    }

    /**
     * 停车场在停车辆排行
     *
     * @return
     */
    @RequestMapping(value = "parkParkingCount.do")
    @ResponseBody
    public ResultInfo parkParkingCount() {
        Map<String, List<String>> result = sjfxService.parkParkingCount();
        return ResultInfo.success(result);
    }


}
