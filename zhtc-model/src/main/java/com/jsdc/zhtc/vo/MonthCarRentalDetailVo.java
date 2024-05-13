package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MonthCarRentalDetailVo {
    // 申请人名称
    private String name;
    // 申请人电话
    private String phone;
    // 启用日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    // 截止日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    // 车牌号
    private String carNo;
    // 申请数量（月数）
    private String months;
    // 单价
    private String price;
    // 总价
    private String totalCost;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    // 企业名称
    private String companyName;
    // 停车场、路内名称
    private String parkNames;

    //办理端类型
    private String transact_style;
    //支付方式
    private String payment_type;
    //总额
    private String amount;
    //车次
    private String frequency;
    //车辆总数
    private String carNum;

    public void setPrice(String price) {
        if (StringUtils.isBlank(price) || !compareTo2(price, "0")) {
            this.price = "0.00";
        } else {
            this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setTotalCost(String totalCost) {
        if (StringUtils.isBlank(totalCost) || !compareTo2(totalCost, "0")) {
            this.totalCost = "0.00";
        } else {
            this.totalCost = new BigDecimal(totalCost).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public static boolean compareTo2(Object num1, Object num2) {
        BigDecimal a = new BigDecimal(num1.toString());
        BigDecimal b = new BigDecimal(num2.toString());
        if (a.compareTo(b) > 0) {
            return true;
        }
        return false;
    }
}
