package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: MemberManage <br/>
 * Description: 会员/车主管理表<br/>
 * date: 2022/1/4 11:28<br/>
 *
 * @author bn<br       />
 */
@Entity
@TableName("member_manage")
@Table(name = "member_manage")
@DynamicInsert
@DynamicUpdate
@Data
public class MemberManage extends Model<MemberManage> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //会员名称
    private String member_name;
    //会员昵称
    private String nick_name;
    //会员头像
    private String member_portrait;
    //账户余额
    private String balance;
    //微信openid
    private String openid;
    //手机号
    private String phone;
    //注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date register_time;


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

//    public String getOpenid(){
//        if(StringUtils.isNotEmpty(this.openid)){
//            String encryption = this.openid.replaceAll("^.{0,20}", "**********");
//            return encryption;
//        }
//        return this.openid;
//    }

    public void setBalance(String balance) {
        if (StringUtils.isBlank(balance) || !compareTo2(balance, "0")) {
            this.balance = "0.00";
        } else {
            this.balance = new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
