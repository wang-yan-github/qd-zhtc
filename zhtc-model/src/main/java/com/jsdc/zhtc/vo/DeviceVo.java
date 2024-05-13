package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: DeviceVo <br/>
 * Description: <br/>
 * date: 2021/12/31 13:40<br/>
 *
 * @author bn<br   />
 */
@Data
public class DeviceVo {

    private Integer id;

    //设备编号
    private String device_code;

    //泊位编号 同设备编号
    private String berth_code;

    //区域 area 表主键
    private Integer area_id;

    //街道 street表主键
    private Integer street_id;

    //路段 road 表主键
    private Integer road_id;

    //设备类型 1.视频杆 2.电池视频杆 3.地磁 4.人工泊位 5.高位视频 6.蘑菇墩
    private String device_type;

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

    // 1 启用 0禁用
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

    // 街道名
    private String street_name;
    // 路段名
    private String road_name;
    // 是否绑定 0：未绑定，1：绑定
    private Integer isBind;
    // 地址
    private String address;

    // 设备类型名
    private String device_type_name;


    /**
     * 设备型号
     */
    private String device_model;


}
