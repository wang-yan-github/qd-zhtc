package com.jsdc.zhtc.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class ChargeProgrammeInfoVo {

    private Integer id;
    //收费方案名称
    private String programmeName;
    //24小时限价设定
    private String isLimitPrice;
    //24小时限价设定说明
    private String isLimitPriceStr;
    //24小时限价金额
    private String limitPriceAmount;

    //白天开始时间
    private String btStaetTime;
    //白天结束时间
    private String btEndTime;
    //白天免费时长
    private String btFreeTime;
    //白天分段限价
    private String btIntervalLimitPrice;
    //白天收付费方式
    private String btChargeType;
    //白天收付费方式说明
    private String btChargeTypeStr;

    //夜间开始时间
    private String yjStaetTime;
    //夜间结束时间
    private String yjEndTime;
    //夜间免费时长
    private String yjFreeTime;
    //夜间分段限价
    private String yjIntervalLimitPrice;
    //夜间收付费方式
    private String yjChargeType;
    //夜间收付费方式说明
    private String yjChargeTypeStr;


    public void setIsLimitPrice(String isLimitPrice) {
        this.isLimitPrice = isLimitPrice;
        if (StringUtils.isNotBlank(isLimitPrice)) {
            if (isLimitPrice.equals("1"))
                this.isLimitPriceStr = "24小时限价";
            else
                this.isLimitPriceStr = "无24小时限价";
        }
    }

    public void setBtChargeType(String btChargeType) {
        this.btChargeType = btChargeType;
        if (StringUtils.isNotBlank(btChargeType)) {
            if (btChargeType.equals("1"))
                this.btChargeTypeStr = "分时";
            else
                this.btChargeTypeStr = "其他";
        }
    }

    public void setYjChargeType(String yjChargeType) {
        this.yjChargeType = yjChargeType;
        if (StringUtils.isNotBlank(yjChargeType)) {
            if (yjChargeType.equals("1"))
                this.yjChargeTypeStr = "分时";
            else
                this.yjChargeTypeStr = "其他";
        }
    }


}
