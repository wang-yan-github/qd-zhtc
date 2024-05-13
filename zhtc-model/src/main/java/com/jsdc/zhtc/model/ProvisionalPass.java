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
 * ClassName: ProvisionalPass
 * Description: 临时通行证
 * date: 2022/2/24 9:38
 *
 * @author bn
 */
@Entity
@TableName("provisional_pass")
@Table(name = "provisional_pass")
@DynamicInsert
@DynamicUpdate
@Data
public class ProvisionalPass extends Model<ProvisionalPass> implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //公司名称
    private String companyName;

    //停车场ID
    private Integer park_id;

    //1启用/0禁用
    private String is_use;

    //可核销次数
    private Integer hxCount;

    //失效时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expire_time;
    //核销类型 1:单次核销  2：多次核销
    private String type;
    //重复类型	同一车牌仅可使用一次 0：否 1：是
    private String repeat_type;
    //限制时间	单位：分钟
    private Integer limit_time;

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

    //收款人
    @Transient
    @TableField(exist = false)
    private String park_name;

    //状态 0失效 1有效
    @Transient
    @TableField(exist = false)
    private String status;

}
