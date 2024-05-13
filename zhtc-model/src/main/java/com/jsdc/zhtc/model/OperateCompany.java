package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.*;
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
 * 类 名: 单位管理
 * 描 述: OperateCompany
 * 作 者: lw
 * 创 建：2022/1/4 10:58
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Entity
@TableName("operate_company")
@Table(name = "operate_company")
@DynamicInsert
@DynamicUpdate
@Data
public class OperateCompany extends Model<OperateCompany> implements Serializable {

    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //企业名称
    private String company_name;

    //企业地址
    private String address;

    //联系人
    private String liaison;

    //联系方式
    private String phone;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    //创建人
    private Integer create_user;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date update_time;

    // 更新人
    private Integer update_user;

    // 是否删除
    private String is_del;
}
