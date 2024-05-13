package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.service.SysConfigService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SysConfigController
 * Description:
 * date: 2022/1/11 8:42
 *
 * @author wp
 */
@RestController
@RequestMapping("sysconfig")
public class SysConfigController {
    @Autowired
    private SysConfigService configService;

    /**
     * create by wp at 2022/1/11 8:43
     * description: 查询系统配置MAP
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getMap.do")
    public ResultInfo getMap() {
        return configService.getMap();
    }

    /**
     * create by wp at 2022/1/11 8:54
     * description: 更新系统参数
     *
     * @param invoiceApplyTime 设置申请发票时间
     * @param leaveTime        缴费后驶离时间
     * @param mergeTime        合并重复订单间隔时间
     * @param giftMoney        关注后赠送金额
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("edit.do")
    @LogInfo(LogEnums.LOG_UPDATECONFIG)
    public ResultInfo edit(String invoiceApplyTime, String leaveTime, String mergeTime, String giftMoney, String Discount) {
        return configService.edit(invoiceApplyTime, leaveTime, mergeTime, giftMoney, Discount);
    }
}
