package com.jsdc.zhtc.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * 类 名: 路侧停车营收报表vo
 * 描 述: ParkRoadRsVo
 * 作 者: lw
 * 创 建：2022/2/9 13:43
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class ParkRoadRsVo {

    // 路侧道路id
    private Integer roadId;
    // 道路名称
    private String roadName;

    // 月租 车数量
    private Integer yzCarNum = 0;
    // 月租 订单数
    private Integer yzCounts = 0;
    // 月租 总收益
    private String yzTotalCost;

    // 流动车 车次
    //private Integer ldCarNum = 0;
    // 流动车 订单数
    private Integer ldOrderNum = 0;
    // 流动车 营收
    private String ldSumPaidAmount;
    // 流动车 微信
    private String ldWxAmount;
    // 流动车 支付宝
    private String ldZfbAmount;
    // 流动车 第三方
    private String ldDsfAmount = "0.00";
    // 流动车 现金支付
    private String ldXjAmount;
    // 流动车 钱包支付
    private String ldQbAmount;

    // 逃单 数量
    private Integer tdCounts = 0;
    // 逃单 总额
    private String tdSumAmount;

    // 免费车/辆 内部车
    private Integer mfNbCount = 0;
    // 免费车/辆 企业
    private Integer mfQyCount = 0;

    // 总计 车次
    private Integer zjCarNum = 0;
    // 总计 营收
    private String zjAmount;

    // 月租 总收益
    public void setYzTotalCost(String yzTotalCost) {
        this.yzTotalCost = setScale1(yzTotalCost);
    }

    // 流动车 总收益
    public void setLdSumPaidAmount(String ldSumPaidAmount) {
        this.ldSumPaidAmount = setScale1(ldSumPaidAmount);
    }

    // 流动车 微信收费
    public void setLdWxAmount(String ldWxAmount) {
        this.ldWxAmount = setScale1(ldWxAmount);
    }

    // 流动车 支付宝支付
    public void setLdZfbAmount(String ldZfbAmount) {
        this.ldZfbAmount = setScale1(ldZfbAmount);
    }

    // 流动车 现金收费
    public void setLdXjAmount(String ldXjAmount) {
        this.ldXjAmount = setScale1(ldXjAmount);
    }

    // 流动车 钱包支付
    public void setLdQbAmount(String ldQbAmount) {
        this.ldQbAmount = setScale1(ldQbAmount);
    }

    // 逃单 总额
    public void setLdDsfAmount(String ldDsfAmount) {
        this.ldDsfAmount = setScale1(ldDsfAmount);
    }

    public void setTdSumAmount(String tdSumAmount) {
        this.tdSumAmount = setScale1(tdSumAmount);
    }

    public void setZjAmount(String zjAmount) {
        this.zjAmount = setScale1(zjAmount);
    }


    /**
     * 描 述： TODO( 保留小数标准 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param num1
     * @return {@link BigDecimal}
     */
    public String setScale1(String num1) {
        if (StringUtils.isBlank(num1))
            return "0.00";
        else
            return new BigDecimal(num1).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

}
