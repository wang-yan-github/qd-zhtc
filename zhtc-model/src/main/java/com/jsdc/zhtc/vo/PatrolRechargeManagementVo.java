package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Data
public class PatrolRechargeManagementVo {
    /**
     * 手机号
     */
    private String nameOrPhone;
    /**
     * 巡检充值统计 按时间 按收费员
     */
    private String group;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 区域id
     */
    private Integer area_id;
    /**
     * 是否领取纸质发票
     */
    private String receivePaperInvoice;
    /**
     * 巡检员Id
     */
    private String inspectId;
    /**
     * 充值金额
     */
    private String rechargeAmount;
    /**
     * 停车区类型 0：路段 1：停车场
     */
    private String parking_type;
}
