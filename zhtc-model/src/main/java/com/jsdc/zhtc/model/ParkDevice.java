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
 * ClassName: ParkDevice 停车场设备<br/>
 * Description: <br/>
 * date: 2022/1/13 10:21<br/>
 *
 * @author bn<br       />
 */
@Entity
@TableName("park_device")
@Table(name = "park_device")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkDevice extends Model<ParkDevice> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //设备编号
    private String device_code;

    //通道号
    private String channel_id;

    //通道名称
    private String channel_name;

    //泊位编号 同设备编号
    private String berth_code;

    //区域 area 表主键
    private Integer area_id;

    //街道 street表主键
    private Integer street_id;

    //停车场 park 表主键
    private Integer park_id;

    //设备类型 1.闸机
    private String device_type;

    // 通道口 0：出口 1.入口
    private String passageway;

    //是否为双通道 1；是 0：否
    private String is_double_way;

    //上传模式
    private String upload_mode;

    //经度
    private String longitude;

    //纬度
    private String latitude;

    //软件版本号
    private String soft_version;

    //硬件版本号
    private String hard_version;

    //最近升级
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recent_upgrade;

    //SN号
    private String sn_no;

    //MAC地址
    private String mac;

    //对接协议版本号
    private String agrver;

    //网络状态
    private String net_status;

    //上一次心跳时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date last_heartbeat_time;

    //状态 1.空闲 2.使用中 3.异常
    private String status;

    // 0 停用 1 启用
    private String is_use;

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


    //设备Ip
    private String device_ip;

    //设备序列号
    private String serialNo;


}
