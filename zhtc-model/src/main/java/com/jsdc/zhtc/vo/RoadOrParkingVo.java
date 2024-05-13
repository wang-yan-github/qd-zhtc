package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.zhtc.model.*;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * ClassName: RoadOrParkingVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 * 路段停车场公用字段
 *
 * @author zln
 */
@Data
public class RoadOrParkingVo {

    private Integer id;
    //会员id
    private Integer member_id;
    //当前类型
    private String type;
    //订单id
    private String order_no;
    //车牌号
    private String car_no;
    private String car_no2;
    //车牌id
    private Integer carno_id;
    private Integer carno_id2;
    //手机号
    private String phone;
    //路段停车场名称
    private String road_name;
    //停车泊位号
    private String berth;
    //停留时间
    private String resitime;
    //订单来源
    private String source;
    //支付订单id
    private Integer payment_id;
    //创建订单时间
    private String create_time;
    //状态名称
    private String statusName;
    //状态id
    private String status;
    //发票id
    private Integer invoice_id;
    //车牌类型
    private String carType;
    //应付金额
    private String sum_amount;
    //优惠金额
    private String discount_amount;
    //已付金额
    private String paid_amount;
    //车牌类型id
    private String car_type;
    //待付金额
    private String unpaid_amount;
    //入场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drivein_time;
    //出场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveout_time;
    //驶入
    private List<FileManage> scList;
    //离开
    private List<FileManage> slList;
    //驳回凭证
    private List<FileManage> bhpzList;
    //申诉订单
    OperateAppeal operateAppeal;
    //申诉订单
    List<OperateAppeal> appeals;
    //支付记录
    List<PaymentOrder> payments;
    //退款记录
    List<RefundManagement> refunds;
    //申诉处理记录
    AppealHandleRecord records;
    //申诉反馈记录
    AppealNoticeFeedback appealNoticeFeedback;
    List<AppealNoticeFeedback> appealNoticeFeedbacks;
    //发票记录
    InvoicesManagement management;
    //是否有申诉订单
    private Integer isAppeals;
    //运营人员审批状态
    private Integer approve_status;
    //登陆用户类型
    private Integer userType;
    //免费类型
    private String free_type;
    //免费类型转义
    private String free_name;

    /**
     * 商家id
     */
    private Integer business_id;

    /**
     * 商家电话
     */
    private String business_phone;

    /**
     * 商家扣款金额
     */
    private String business_amount;

    //是否享受商家优惠(0是1否)
    private String is_discount;

    //商家优惠金额
    private String discount_money;

    //登录人id
    private Integer inspect_id;
    //收费员工号
    private String inspect_code;

    //支付方式名称
    private String payName;
    //收费时间段
    private String charge_time;
    //区域id
    private Integer area_id;
    private String area_name;
    //arrears_count：欠费总笔数
    private Integer arrears_count;
    //arrears_money：欠费总金额
    private String arrears_money;

    //人工放行理由
    private String fxReason;

    public void setDiscount_money(String discount_money) {
        if (StringUtils.isBlank(discount_money) || !compareTo2(discount_money, "0")) {
            this.discount_money = "0.00";
        } else {
            this.discount_money = new BigDecimal(discount_money).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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

    public void setUnpaid_amount(String unpaid_amount) {
        if (StringUtils.isBlank(unpaid_amount) || !compareTo2(unpaid_amount, "0")) {
            this.unpaid_amount = "0.00";
        } else {
            this.unpaid_amount = new BigDecimal(unpaid_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
