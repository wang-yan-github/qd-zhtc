package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.service.MonthlyManagementService;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.service.RechargeManagementService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类 名: 平台概览
 * 描 述: PlatformSurveyController
 * 作 者: lw
 * 创 建：2022/1/8 15:52
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("platformsurvey")
public class PlatformSurveyController {

    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private MonthlyManagementService monthlyManagementService;
    @Autowired
    private RechargeManagementService rechargeManagementService;

    /**
     * 描 述： TODO(查询当日注册人数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link ResultInfo}
     */
    @PostMapping("/getTodayAddCount")
    public ResultInfo getTodayAddCount() {
        return ResultInfo.success("", "查询成功");
    }

    /**
     * 描 述： TODO(查询会员总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link ResultInfo}
     */
    @PostMapping("/getMemberTotality")
    public ResultInfo getMemberTotality() {
        return ResultInfo.success("", "查询成功");
    }


}
