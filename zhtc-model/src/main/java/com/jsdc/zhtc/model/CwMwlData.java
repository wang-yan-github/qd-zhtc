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
 * 车位满位率
 */
@Entity
@TableName("cwmwl_data")
@Table(name = "cwmwl_data")
@DynamicInsert
@DynamicUpdate
@Data
public class CwMwlData extends Model<CwMwlData> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //满位率
    private String mwl;
    // 在停车辆
    private Integer ztCount;
    // 总泊位数
    private Integer berthCount;
    // 生成日期 yyyy-MM-dd
    private String time;
    // 小时 HH
    private String hour;
    // 小时 HH
    private Integer hours;
    // 类型 0路测 1停车场 2（路测+停车场）总和
    private String type;

    // 路段/停车场id
    private Integer road_park_id;

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

}
