package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @description: 路段巡检员关联
 */
@Entity
@TableName("section_inspector")
@Table(name = "section_inspector")
@DynamicInsert
@DynamicUpdate
@Data
public class SectionInspector extends Model<SectionInspector> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 停车区类型 0：路段 1：停车场
     */
    private String parking_type;
    /**
     * 开始时间
     */
    private String begin_time;
    /**
     * 结束时间
     */
    private String end_time;
    /**
     * 巡检员id
     */
    private Integer inspect_id;
    /**
     * 路段id
     */
    private Integer allocated_section_id;
    /**
     * 创建时间
     */
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private String is_del;

    //路段名称
    @Transient
    @TableField(exist = false)
    private String road_name;
}
