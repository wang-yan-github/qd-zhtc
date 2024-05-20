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
 * 停车场-白名单中间表
 */
@Entity
@TableName("white_list")
@Table(name = "white_list")
@DynamicInsert
@DynamicUpdate
@Data
public class WhiteList extends Model<WhiteList> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //停车场主键
    private Integer park_id  ;

    //车辆主键
    private Integer car_id  ;


}
