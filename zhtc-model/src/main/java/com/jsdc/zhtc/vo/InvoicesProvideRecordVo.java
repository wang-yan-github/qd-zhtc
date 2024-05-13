package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: InvoicesProvideRecordVo
 * Description:
 * date: 2022/1/17 11:50
 *
 * @author bn
 */
@Data
public class InvoicesProvideRecordVo {


    private Integer id;

    // 订单号
    private String order_no;
    //订单ID
    private Integer order_id;
    //订单类型 0：路段 1：停车场
    private String parking_type;
    //订单金额
    private String order_amount;
    //发票金额
    private String invoice_amount;
    //巡检员id
    private Integer inspect_id;
    //发放时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date provide_time;

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


    // 车牌号
    private String car_no;

    //
    private String car_type;

    // 停车场名
    private String park_name;

    // 路段名
    private String road_name;

    // 巡检员名
    private String name;


    //进场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drivein_time;
    //出场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveout_time;
    //停留时长 分钟
    private Integer resitime;
    // 应收金额
    private String sum_amount;
    //订单状态 在停、待缴费、已缴费、已完成
    private String status;
    //已付金额
    private String paid_amount;
    //订单支付方式  字典 1包月、2微信、3支付宝、4钱包、5现金
    private String pay_type;

    //区域
    private Integer area_id;

    //街道
    private Integer street_id;

    // 路段
    private Integer road_id;

    // 停车场
    private Integer park_id;

    // 开始时间

    private String start_time;

    // 结束时间

    private String end_time;


    /**
     * 泊位号
     */
    private String berth;

    public void setOrder_amount(String order_amount) {
        if (StringUtils.isBlank(order_amount) || !compareTo2(order_amount, "0")) {
            this.order_amount = "0.00";
        } else {
            this.order_amount = new BigDecimal(order_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setInvoice_amount(String invoice_amount) {
        if (StringUtils.isBlank(invoice_amount) || !compareTo2(invoice_amount, "0")) {
            this.invoice_amount = "0.00";
        } else {
            this.invoice_amount = new BigDecimal(invoice_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setSum_amount(String sum_amount) {
        if (StringUtils.isBlank(sum_amount) || !compareTo2(sum_amount, "0")) {
            this.sum_amount = "0.00";
        } else {
            this.sum_amount = new BigDecimal(sum_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setPaid_amount(String paid_amount) {
        if (StringUtils.isBlank(paid_amount) || !compareTo2(paid_amount, "0")) {
            this.paid_amount = "0.00";
        } else {
            this.paid_amount = new BigDecimal(paid_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
