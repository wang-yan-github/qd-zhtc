package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wh
 * @description 充值活动规则
 */
@TableName("recharge_activity_config")
@Entity
@Table(name = "recharge_activity_config")
@Data
public class RechargeActivityConfig {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //充值活动ID
    private Integer recharge_activity_id;
    //充值金额
    private String recharge_amount;
    //赠送金额
    private String additional_amount;
    //排序
    private Integer sort;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
}
