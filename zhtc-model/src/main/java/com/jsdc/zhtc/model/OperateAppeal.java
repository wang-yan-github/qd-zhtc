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
 * ClassName: OperateAppeal
 * Description:申诉管理表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("operate_appeal")
@Table(name = "operate_appeal")
@DynamicInsert
@DynamicUpdate
@Data
public class OperateAppeal extends Model<OperateAppeal> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //停车id
    private Integer parking_order_id;
    //投诉内容
    private String content;
    //联系方式
    private String phone;
    //申诉类型（0:路段，1 ：停车）
    private String appeal_type;
    //申诉时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appeal_time;
    //申诉时订单状态
    private String appeal_order_status;
    //申诉状态 1 待处理 2通过 3驳回 4完成
    private String appeal_status;
    //车主id
    private Integer member_id;
    //是否核实 1 是 0 否
    private String is_verify;
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

    @TableField(exist = false)
    @Transient
    private String appeal_status_name;
}
