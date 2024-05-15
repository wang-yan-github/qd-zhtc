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
 * 车主管理
 */
@Entity
@TableName("car_owner")
@Table(name = "car_owner")
@DynamicInsert
@DynamicUpdate
@Data
public class CarOwner extends Model<CarOwner> implements Serializable {
    @Id
    @TableId
    private String id;
    //姓名
    private String name;
    //车主属性（必填、单选框） 内部人员/内部人员/其他
    private String type;
    //手机号（必填，手机号格式验证）
    private String phone;
    //工作单位
    private String work_unit;
    //职务
    private String position;


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
