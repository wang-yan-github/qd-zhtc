package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 车牌管理表
 *
 * @author thr
 */
@Entity
@TableName("operate_carno")
@Table(name = "operate_carno")
@Data
public class OperateCarno extends Model<OperateCarno> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //车牌号码
    private String car_no;
    //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌 5黑牌)
    private String car_type;
    //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
    private String roster_type;

    // 车型，1：小型车；2：中型车；3：大型车
    private String vehicle_type;

    // 月租时间类型 1永久 2期限
    private String white_time_type;

    //月租车 免费开始时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_start;
    //月租车 免费截止时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_end;

    //车主id
    private String car_owner_id;

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
    private String is_del;

    // 理由说明
    private String reason;

}
