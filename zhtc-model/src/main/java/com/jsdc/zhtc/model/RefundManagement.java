package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 类 名: 退款管理
 * 描 述: RefundManagement
 * 作 者: lw
 * 创 建：2021/12/30 14:34
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Entity
@TableName("refund_management")
@Table(name = "refund_management")
@DynamicInsert
@DynamicUpdate
@Data
public class RefundManagement extends Model<RefundManagement> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    // 主键
    private Integer id;

    // 会员id
    private Integer member_id;

    //车牌id
    private Integer carno_id;

    // 停车订单号
    private Integer parking_order_id;

    // 支付订单号
    private Integer payment_id;

    // 退款订单号
    private String payment_no;

    // 退款流水号
    private String payment_serialno;

    // 退款金额
    private String refund_amount;

    // 退款渠道(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付)
    private String refund_channel;
    //0.路段 1、停车场
    private String park_type;
    // 退款时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refund_time;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    //创建人
    private Integer create_user;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date update_time;

    // 更新人
    private Integer update_user;

    // 是否删除
    private String is_del;

    public void setRefund_amount(String refund_amount) {
        if (StringUtils.isBlank(refund_amount) || !compareTo2(refund_amount, "0")) {
            this.refund_amount = "0.00";
        } else {
            this.refund_amount = new BigDecimal(refund_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
