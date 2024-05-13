package com.jsdc.zhtc.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类 名: 停车场营收报表vo
 * 描 述: ParkRsVo
 * 作 者: lw
 * 创 建：2022/1/28 17:16
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class ParkRsVo {

    // 停车场ID
    private Integer parkId;
    // 停车场名称
    private String parkName;

    // 月租 车数量
    private Integer yzCarNum = 0;
    // 月租 订单数
    private Integer yzCounts = 0;
    // 月租 总收益
    private String yzTotalCost;

    // 流动车 车次
    private Integer ldCarNum = 0;
    // 流动车 总收益
    private String ldSumPaidAmount;
    // 流动车 微信收费
    private String ldWxAmount;
    // 流动车 支付宝支付
    private String ldZfbAmount;
    // 流动车 现金收费
    private String ldXjAmount;
    // 流动车 第三方收费
    private String ldDsfAmount;
    // 流动车 钱包支付
    private String ldQbAmount;

    // 人工抬杆 公务抬杆数
    private Integer ldGwOrderCount = 0;
    // 人工抬杆 公务抬杆应收
    private String ldGwAmount;
    // 人工抬杆 故障抬杆数
    private Integer ldGzOrderCount = 0;
    // 人工抬杆 故障抬杆总收益
    private String ldGzAmount;

    // 逃单 数量
    private Integer tdCounts = 0;
    // 逃单 总额
    private String tdSumAmount;

    // 免费 内部免费
    private Integer mfNbCount = 0;
    // 免费 企业免费
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

    //流动车 第三方收费
    public void setLdDsfAmount(String ldDsfAmount) {
        this.ldDsfAmount = setScale1(ldDsfAmount);
    }

    //流动车 钱包支付
    public void setLdQbAmount(String ldQbAmount) {
        this.ldQbAmount = setScale1(ldQbAmount);
    }

    // 人工抬杆 公务抬杆应收
    public void setLdGwAmount(String ldGwAmount) {
        this.ldGwAmount = setScale1(ldGwAmount);
    }

    // 人工抬杆 故障抬杆总收益
    public void setLdGzAmount(String ldGzAmount) {
        this.ldGzAmount = setScale1(ldGzAmount);
    }

    // 逃单 总额
    public void setTdSumAmount(String tdSumAmount) {
        this.tdSumAmount = setScale1(tdSumAmount);
    }


    /**
     * 描 述： TODO( 保留小数标准 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param num1
     * @return {@link BigDecimal}
     */
    public String setScale1(Object num1) {
        if (num1 == null)
            return "0.0";
        else
            return new BigDecimal(num1.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


}
