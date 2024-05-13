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
 * 诱导屏管理
 * 管理路段和停车场
 */
@Entity
@TableName("screen_park_road")
@Table(name = "screen_park_road")
@DynamicInsert
@DynamicUpdate
@Data
public class ScreenParkRoad extends Model<ScreenParkRoad> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 诱导屏id
     */
    private Integer screen_id;
    /**
     * 停车区类型 0：路段 1：停车场
     */
    private String type;
    /**
     * 停车场、路段id
     */
    private Integer pk_id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private String is_del;

    //停车场、路段名称
    @Transient
    @TableField(exist = false)
    private String name;
}
