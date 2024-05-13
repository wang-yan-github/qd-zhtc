package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author wh
 * @description 充值活动表
 */
@TableName("recharge_activity")
@Entity
@Table(name = "recharge_activity")
@Data
public class RechargeActivity {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //活动名称
    private String activity_name;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date start_time;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date end_time;
    //充值活动描述
    private String activity_desc;
    //排序号
    private Integer sort;
    //类型
    private String type;
    // 状态
    private String status;
    @Transient
    @TableField(exist = false)
    private List<RechargeActivityConfig> rechargeActivityConfigs;
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
