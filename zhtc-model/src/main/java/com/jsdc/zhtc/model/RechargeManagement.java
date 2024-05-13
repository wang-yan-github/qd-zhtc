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
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 类 名: 充值管理
 * 描 述: RechargeManagement
 * 作 者: lw
 * 创 建：2022/1/4 9:30
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Entity
@TableName("recharge_management")
@Table(name = "recharge_management")
@DynamicInsert
@DynamicUpdate
@Data
public class RechargeManagement extends Model<RechargeManagement> implements Serializable {

    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //微信昵称
    private String nick_name;

    //会员用户
    private Integer member_id;

    //充值金额
    private String recharge_amount;

    //赠送金额
    private String gift_amount;

    //充值时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recharge_time;

    //支付订单id
    private Integer payment_id;

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

    @Transient
    @TableField(exist = false)
    private String fileIds;

    @Transient
    @TableField(exist = false)
    private String openId;

    public void setRecharge_amount(String recharge_amount) {
        if (StringUtils.isBlank(recharge_amount) || !compareTo2(recharge_amount, "0")) {
            this.recharge_amount = "0.00";
        } else {
            this.recharge_amount = new BigDecimal(recharge_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setGift_amount(String gift_amount) {
        if (StringUtils.isBlank(gift_amount) || !compareTo2(gift_amount, "0")) {
            this.gift_amount = "0.00";
        } else {
            this.gift_amount = new BigDecimal(gift_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
