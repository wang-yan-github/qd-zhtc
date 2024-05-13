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
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Entity
@TableName("carno_appeal")
@Table(name = "carno_appeal")
@DynamicInsert
@DynamicUpdate
@Data
public class CarnoAppeal extends Model<CarnoAppeal> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 车牌id
     */
    private Integer carno_id;
    /**
     * 申诉人id
     */
    private Integer member_id;
    /**
     * 备注
     */
    private String remark;
    /**
     * 申诉状态(1通过、0拒绝、为空 申诉中 默认)
     */
    private String status;
    /**
     * 审核人id
     */
    private Integer verifier;
    /**
     * 行驶证图片id
     */
    private Integer driveringlic_id;
    /**
     * 驾驶证图片id
     */
    private Integer driverlic_id;
    /**
     * 拒绝原因
     */
    private String reject_reason;
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


    /**
     * 车牌号
     */
    @Transient
    @TableField(exist = false)
    public String car_no;
    /**
     * 行驶证图片路径
     */
    @Transient
    @TableField(exist = false)
    public String xszPicUrl;
    /**
     * 驾驶证图片路径
     */
    @Transient
    @TableField(exist = false)
    public String jszPicUrl;

    //车牌类型(1蓝牌、2绿牌、3黄牌)
    @Transient
    @TableField(exist = false)
    private String car_type;
}
