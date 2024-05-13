package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ClassName: InspectManage
 * Description:巡检员、收费员管理表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("inspect_manage")
@Table(name = "inspect_manage")
@DynamicInsert
@DynamicUpdate
@Data
public class InspectManage extends Model<InspectManage> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //账号
    private String login_name;
    // 姓名
    private String name;
    // 登录密码
    private String logpwd;
    //性别
    private String gender;
    //年龄
    private String age;
    //联系电话
    private String phone;
    //身份证号
    private String idCard;
    //巡检照片
    private String picture_id;
    // 工号
    private String job_no;
    //发票余额
    private String invoice_balance;
    //0：路测 1：停车场
    private String personType;
    //极光推送注册id
    private String registerId;
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
    //头像图片 URL
    private String head_portrait;

    //图片路径
    @TableField(exist = false)
    @Transient
    private String pic_url;
    //图片路径
    @TableField(exist = false)
    @Transient
    private List<FileManage> fileList;

    public String getIdCard() {
        if (StringUtils.isNotEmpty(this.idCard)) {
            int length = this.idCard.length();
            if (length < 15) {
                return this.idCard;
            }
            int l = length - 4;
            String encryption = this.idCard.replaceAll("^.{0," + l + "}", "**************");
            return encryption;
        }
        return this.idCard;
    }

    public void setInvoice_balance(String invoice_balance) {
        if (org.apache.commons.lang.StringUtils.isBlank(invoice_balance) || !compareTo2(invoice_balance, "0")) {
            this.invoice_balance = "0.00";
        } else {
            this.invoice_balance = new BigDecimal(invoice_balance).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public static boolean compareTo2(Object num1, Object num2) {
        BigDecimal a = new BigDecimal(num1.toString());
        BigDecimal b = new BigDecimal(num2.toString());
        if (a.compareTo(b) > 0) {
            return true;
        }
        return false;
    }

}
