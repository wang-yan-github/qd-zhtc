package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Entity
@TableName("patrol_recharge_management")
@Table(name = "patrol_recharge_management")
@DynamicInsert
@DynamicUpdate
@Data
public class PatrolRechargeManagement extends Model<PatrolRechargeManagement> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 巡检员id
     */
    private Integer inject_id;
    /**
     * 充值前账户余额
     */
    private Integer before_account_balance;

    /**
     * 充值金额
     */
    private Integer recharge_amount;
    /**
     * 充值后账户余额
     */
    private Integer after_account_balance;
    /**
     * 充值方式
     */
    private String recharge_mode;
    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recharge_time;
    /**
     * 是否领取纸质发票
     */
    private String receive_paper_invoice;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人id
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人id
     */
    private Integer update_user;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
}
