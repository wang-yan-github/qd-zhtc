package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: PaymonthlyConfig
 * Description: 包月配置
 * date: 2021/12/30 10:15
 *
 * @author wp
 */
@Entity
@TableName("paymonthly_config")
@Table(name = "paymonthly_config")
@DynamicInsert
@DynamicUpdate
@Data
public class PaymonthlyConfig extends Model<PaymonthlyConfig> implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 车辆类型 1：小车 0：大车
     */
    private String category;
    /**
     * 包月类型 1：个人 2： 公司
     */
    private String monthType;

    /**
     * 停车区类型 0：路段 1：停车场
     */
    private String parking_type;

    /**
     * 价格
     */
    private String price;
    // 价格类型 1永久 2期限
    private String price_type;
    // 明年一月一日零点零时零秒启的新价格
    private String new_price;

    /**
     * 包月时间段(全天00:00-24:00)开始时间
     */
    private String start_time;

    /**
     * 包月时间段(全天00:00-24:00)结束时间
     */
    private String end_time;

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

    @Transient
    @TableField(exist = false)
    private String parkingIds;

    @Transient
    @TableField(exist = false)
    private List<Park> parkList = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private List<Integer> roadIds;

    @Transient
    @TableField(exist = false)
    private String user_name;

    public void setPrice(String price) {
        if (StringUtils.isBlank(price) || !compareTo2(price, "0")) {
            this.price = "0.00";
        } else {
            this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setNew_price(String new_price) {
        if (StringUtils.isBlank(new_price) || !compareTo2(new_price, "0")) {
            this.new_price = "0.00";
        } else {
            this.new_price = new BigDecimal(new_price).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
