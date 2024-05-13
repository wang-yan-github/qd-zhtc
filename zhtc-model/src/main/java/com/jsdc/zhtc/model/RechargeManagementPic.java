package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 充值管理关联图片
 */
@Entity
@TableName("recharge_management_pic")
@Table(name = "recharge_management_pic")
@DynamicInsert
@DynamicUpdate
@Data
public class RechargeManagementPic extends Model<RechargeManagementPic> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //充值管理id
    private Integer recharge_management_id;
    //凭证图片id
    private Integer picture_id;
    //是否删除
    private Integer is_del;
}
