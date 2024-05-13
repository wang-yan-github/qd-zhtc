package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 类 名: 未支付订单vo
 * 描 述: UnpaidOrderVo
 * 作 者: lw
 * 创 建：2022/1/19 19:05
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class UnpaidOrderVo {

    private Integer id;
    //车牌id
    private Integer carnoId;
    //车牌号
    private String carNo;
    //订单号
    private String orderNo;
    //道路/车场 id
    private Integer placeId;
    //道路/车场 名称
    private String placeName;
    //入场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;
    //出场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;
    //应付金额
    private Double sumAmount;
    //证明图片
    private List<String> proveImage;
    //订单类型0路测  1停车场
    private Integer type;
    //订单状态
    private String status;


    public void setSumAmount(Double sumAmount) {
        if (!compareTo2(sumAmount, "0")) {
            this.sumAmount = 0.00;
        } else {
            this.sumAmount = new BigDecimal(sumAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
