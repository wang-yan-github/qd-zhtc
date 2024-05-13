package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.PaymentOrder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类 名: 统计
 * 描 述: ShouldBillRankingVo
 * 作 者: lw
 * 创 建：2022/2/8 14:45
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class ShouldBillRankingVo {

    private Integer id;
    private Boolean status = false;
    private Integer paixu;
    // 名称
    private String title;
    // 车位数、泊位数
    private String stopcarnum;
    // 笔数 账单数
    private String renum;
    // 应收金额 总金额
    private String money;
    // 车辆数
    private Integer carCount;

    private String roadName;
    private String parkName;
    private String counts;
    private String counts2;
    private String billNum;
    private String sumAmount = "0"; // 应收金额
    private String paidAmount = "0"; // 已付金额
    private String unpaidAmount = "0"; // 待付金额
    private String unpaidAmount2 = "0"; // 待付金额
    private String byAmount = "0"; // 包月收入
    private String czAmount = "0"; // 充值收入
    private Integer jfs; //缴费数
    private Integer zs; // 总数
    private String jfl = "0"; // 缴费率
    private String zzl = "0"; // 周转率=总订单/泊位数/查询天数

    private Integer bjcs; // 补缴次数
    private String bjje = "0"; // 补缴金额

    //包月集合：包月单价、月数、总金额
    private List<PaymentOrder> paymentOrderList;

    public void setMoney(String money) {
        if (StringUtils.isBlank(money) || !compareTo2(money, "0")) {
            this.money = "0.00";
        } else {
            this.money = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setSumAmount(String sumAmount) {
        if (StringUtils.isBlank(sumAmount) || !compareTo2(sumAmount, "0")) {
            this.sumAmount = "0.00";
        } else {
            this.sumAmount = new BigDecimal(sumAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setPaidAmount(String paidAmount) {
        if (StringUtils.isBlank(paidAmount) || !compareTo2(paidAmount, "0")) {
            this.paidAmount = "0.00";
        } else {
            this.paidAmount = new BigDecimal(paidAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setUnpaidAmount(String unpaidAmount) {
        if (StringUtils.isBlank(unpaidAmount) || !compareTo2(unpaidAmount, "0")) {
            this.unpaidAmount = "0.00";
        } else {
            this.unpaidAmount = new BigDecimal(unpaidAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setUnpaidAmount2(String unpaidAmount2) {
        if (StringUtils.isBlank(unpaidAmount2) || !compareTo2(unpaidAmount2, "0")) {
            this.unpaidAmount2 = "0.00";
        } else {
            this.unpaidAmount2 = new BigDecimal(unpaidAmount2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setByAmount(String byAmount) {
        if (StringUtils.isBlank(byAmount) || !compareTo2(byAmount, "0")) {
            this.byAmount = "0.00";
        } else {
            this.byAmount = new BigDecimal(byAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setCzAmount(String czAmount) {
        if (StringUtils.isBlank(czAmount) || !compareTo2(czAmount, "0")) {
            this.czAmount = "0.00";
        } else {
            this.czAmount = new BigDecimal(czAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setJfl(String jfl) {
        if (StringUtils.isBlank(jfl) || !compareTo2(jfl, "0")) {
            this.jfl = "0%";
        } else {
            this.jfl = (new BigDecimal(jfl).multiply(BigDecimal.valueOf(100))).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%";
        }
    }

    public void setZzl(String zzl) {
        if (!"-".equals(zzl)) {
            if (StringUtils.isBlank(zzl) || !compareTo2(zzl, "0")) {
                this.zzl = "0%";
            } else {
                this.zzl = (new BigDecimal(zzl).multiply(BigDecimal.valueOf(100))).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%";
            }
        } else {
            this.zzl = "-";
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


    public void setBjje(String bjje) {
        if (StringUtils.isBlank(bjje) || !compareTo2(bjje, "0")) {
            this.bjje = "0.00";
        } else {
            this.bjje = new BigDecimal(bjje).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

}
