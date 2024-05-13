package com.jsdc.zhtc.model;


import com.baomidou.mybatisplus.annotation.*;
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

@Entity
@TableName("carno_bind_record")
@Table(name = "carno_bind_record")
@DynamicInsert
@DynamicUpdate
@Data
public class CarnoBindRecord extends Model<CarnoBindRecord> implements Serializable {


    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //车牌id
    private Integer car_id;
    //绑定状态
    private String bind_type;
    //绑定用户
    private Integer member_id;
    //绑定单位
    private Integer company_id;
    //绑定时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bind_date;
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
    private Integer is_del;


}
