package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 减免规则管理
 */
@Entity
@TableName("rule_configuration")
@Table(name = "rule_configuration")
@DynamicInsert
@DynamicUpdate
@Data
public class RuleConfiguration extends Model<RuleConfiguration> implements Serializable {
    @Id
    @TableId
    private String id;
    //停车区类型 1：车辆类型减免规则配置 2：手动减免规则配置 3: 自动减免规则配置-如果同时出现多种减免情况，只减免2小时停车时长，同时查询到可减免的his订单信息验旧不可再次使用
    private String type;
    @Transient
    @TableField(exist = false)
    private String type_name;
    //调整前的累计时长
    private String before_time;
    //调整后的累计时长
    private String after_time;
    //键
    private String value;
    //值
    private String label;
    //排序
    private String sort;
    //备注
    private String remark;


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
     * 创建人姓名
     */
    private String create_user_name;

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
     * 更新人姓名
     */
    private String update_user_name;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
}
