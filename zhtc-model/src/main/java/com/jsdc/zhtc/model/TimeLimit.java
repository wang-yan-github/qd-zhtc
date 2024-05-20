package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 停车场-限时停放权限中间表
 */
@Entity
@TableName("time_limit")
@Table(name = "time_limit")
@DynamicInsert
@DynamicUpdate
@Data
public class TimeLimit extends Model<TimeLimit> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //停车场主键
    private Integer park_id  ;

    //车辆类型 字典id rosterType
    private Integer dict_id  ;


}
