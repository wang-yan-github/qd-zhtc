package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Entity
@TableName("payment_order")
@Table(name = "payment_order")
@DynamicInsert
@DynamicUpdate
@Data
public class PaymentOrder extends Model<PaymentOrder> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付 9:交通账户)
     */
    private String payment_type;

    //转账流水号
    private String transferSerialNumber;
    /**
     * 支付来源(1包月、2会员充值、3停车订单支付、4商家充值)
     */
    private String payment_resource;
    /**
     * 支付状态 1待支付 2已支付 3支付失败
     */
    private String status;
    /**
     * 支付金额
     */
    private String amount;
    /**
     * 支付订单号
     */
    private String payment_no;
    /**
     * 支付流水号
     */
    private String payment_serialno;
    /**
     * 收款人
     */
    private Integer inspect_id;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 支付完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private String is_del;
    /**
     * 是否联合支付
     */
    private String is_union_pay;

    /**
     * 账单时间 聚合支付退款需要该字段
     */
    private String billDate;

    //包月月数
    private Integer months;
    //包月单价
    private String months_unit_price;

    /**
     * 是否退款 空为否 1是
     */
    private String is_refund;
    /**
     * 退款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refund_time;
    /**
     * 退款金额
     */
    private String refund_amount;

    //收款人
    @Transient
    @TableField(exist = false)
    private String inspect_name;

    @Transient
    @TableField(exist = false)
    private String openid;

    @Transient
    @TableField(exist = false)
    private Integer memberid;

    //是否开闸 0否 1是
    @Transient
    @TableField(exist = false)
    private String isKz;

    public void setAmount(String amount) {
        if (StringUtils.isBlank(amount) || !compareTo2(amount, "0")) {
            this.amount = "0.00";
        } else {
            this.amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
