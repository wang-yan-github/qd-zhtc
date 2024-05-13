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
import java.util.List;

/**
 * ClassName: ChargeIntervalConfig <br/>
 * Description: 收费时段配置表<br/>
 * date: 2021/12/30 9:52<br/>
 *
 * @author bn<br       />
 */
@Entity
@TableName("charge_interval_config")
@Table(name = "charge_interval_config")
@DynamicInsert
@DynamicUpdate
@Data
public class ChargeIntervalConfig extends Model<ChargeIntervalConfig> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //类型 0：白天 1：夜间
    private String inteval_type;

    //收费时段开始时间
    private String start_time;

    //收费时段结束时间
    private String end_time;

    //免费停车时长
    private Integer free_time;

    //分段限价
    private String interval_limit_price;

    //收费方式 1.分时
    private Integer charge_type;

    //收费单位
    private Integer charge_unit;

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

    @TableField(exist = false)
    @Transient
    private List<ChargeTimeConfig> chargeTimeConfigs;

    @Transient
    @TableField(exist = false)
    private String excelCode;

}
