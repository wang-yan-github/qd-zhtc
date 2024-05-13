package com.jsdc.zhtc.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

@Data
public class CensusVo {

    // 分组条件
    private String gpby;
    // 车牌总数
    private Integer carnoNum;
    // 订单总数
    private Integer quantum;
    // 总额
    private String amount;
    // 车牌类型
    private Integer carType;
    // 车牌类型名称
    private String carTypeName = "蓝牌";


    public void setAmount(String amount) {
        if (StringUtils.isBlank(amount)) {
            this.amount = "0.0";
        } else {
            this.amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
        switch (carType) {
            case 2:
                this.carTypeName = "绿牌";
                break;
            case 3:
                this.carTypeName = "黄牌";
                break;
            default:
                this.carTypeName = "蓝牌";
                break;

        }
    }

}
