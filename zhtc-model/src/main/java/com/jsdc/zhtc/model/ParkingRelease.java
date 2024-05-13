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
 * ClassName: AppealHandleRecord
 * Description:上线收费配置表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("parking_release")
@Table(name = "parking_release")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkingRelease extends Model<ParkingRelease> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //开始收费时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String start_time;
    //停车区类型
    private String parking_type;
    //禁用/启用(1)
    private Integer status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    //创建人
    private Integer create_user;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    // 更新人
    private Integer update_user;
    // 是否删除
    private Integer is_del;

    //停车区域id
    @Transient
    @TableField(exist = false)
    private String parkingplace_id;
    // 上线收费配置ID
    @Transient
    @TableField(exist = false)
    private String parking_release_id;
}
