package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: park <br/>
 * Description: 停车场<br/>
 * date: 2021/12/30 9:48<br/>
 *
 * @author bn<br       />
 */
@Entity
@TableName("park")
@Table(name = "park")
@DynamicInsert
@DynamicUpdate
@Data
public class Park extends Model<Park> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //停车场编号
    private String park_code;


    //市交停车场编号
    private String traffic_park_code;

    //停车场名称
    private String park_name;

    //百胜ukey
    private String ukey;

    // 泊位总数
    private Integer park_num;

    //开放泊位数
    private Integer openBerthNum;

    //临停车位数
    private Integer tempTotalNum;

    //月租车位数
    private Integer intrinsicTotalNum;

    //访客车位数
    private Integer visitorTotalNum;

    //充电桩车位数
    private Integer chargeTotalNum;

    //停车场类型 1.急诊区域
    private String park_grade;

    // 城市
    private String city;

    //城区编号01 ：市南；02 ：市北；03：黄岛；04 ：崂山；05 ：李沧；06 ：城阳；07 ：
    //即墨；08 ：胶州；09 ：平度；10 ：莱西
    private String area_code;


    //定价类型 1 ：政府定价； 2 ：市场定价 ；3 ：不收费
    private String price_type;

    //开放时间
    private String openTime;

    //停车位置  1 ：路外；2 ：路内
    private String parkingLocate;

    //停车场类型  1 ：配建停车场；2 ：公共停车场；3 ：道路停车位
    private String parkingType;




    // 区域 area 表主键
    private Integer area_id;

    // 街道  street表主键
    private Integer street_id;

    //地址
    private String address;

    // 百度经度
    private String longitude = "0";

    //百度纬度
    private String latitude = "0";

    // 状态 0启用 1.禁用
    private String status;

    // 收费说明
    @Column(length = 2000)
    private String charge_remark;

    //收费方案（蓝牌）
    private Integer blue_charge_id;
    //收费方案（绿牌）
    private Integer green_charge_id;
    //收费方案（黄牌）
    private Integer yellow_charge_id;
    //停车场包月用户数上限
    private Integer limit_monthly;

    //品牌 bs(百胜) dc(鼎驰)
    private String brand;


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

    //限流开关 0开启 1关闭
    private String on_off;

    //空闲车位数量
    private Integer free_count;

    public void setLongitude(String longitude) {
        if (StringUtils.isNotBlank(longitude))
            this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        if (StringUtils.isNotBlank(latitude))
            this.latitude = latitude;
    }

    //急诊区域 时长类型 1日 2月 3年
    private Integer timeType ;

    //急诊区域 累计停车时长
    private Integer cumulativeDuration ;


}
