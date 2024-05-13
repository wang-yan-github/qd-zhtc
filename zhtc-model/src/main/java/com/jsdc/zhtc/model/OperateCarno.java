package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ClassName: OperateCarno
 * Description:车牌管理表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("operate_carno")
@Table(name = "operate_carno")
@Data
public class OperateCarno extends Model<OperateCarno> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //车牌号
    private String car_no;
    //车牌类型(1蓝牌、2绿牌、3黄牌)
    private String car_type;
    //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
    private String roster_type;
    //    //车辆性质(车牌性质、1军用车、2救护车、3内部车、4其他)
//    private String car_natures;
    //绑定类型(0：个人 1：企业)
    private String bind_type;
    //姓名
    private String name;
    //手机号
    private String phone;
    //公司名称
    private String company_name;
    //企业白名单 免费截止时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cut_off_date;

    //内部车白名单 免费开始时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_start;
    //内部车白名单 免费截止时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_end;

    //残疾证编码
    private String deformity_cert;
    //残疾证照片
    private String deformity_picture_id;
    //所属用户
    @TableField(fill = FieldFill.UPDATE)
    private Integer member_id;
    //所属单位
    private Integer company_id;
    //绑定时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE, jdbcType = JdbcType.TIMESTAMP)
    private Date bind_date;
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
    private String is_del;
    // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
    private String whitelist_type;
    // 理由说明
    private String reason;
    // 白名单免费类型 1永久 2期限
    private String white_time_type;

    @TableField(exist = false)
    @Transient
    private List<Integer> parkIds;
    @TableField(exist = false)
    @Transient
    private List<Integer> roadIds;
    @TableField(exist = false)
    @Transient
    private String picUrl;
}
