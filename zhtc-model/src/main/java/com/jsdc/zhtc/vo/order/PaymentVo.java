package com.jsdc.zhtc.vo.order;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: Payment
 * Description:
 * date: 2022/1/25 15:02
 *
 * @author zonglina
 */
@Data
public class PaymentVo {

    //缴费金额
    private String pay_money;
    //优惠金额
    private String discount_money;
    //停留时间
    private String resitime;

    //支付状态(1.成功 2.支付一半 3.失败 4、时间过期 5、未查询到车牌 )
    private String status;
    //支付金额
    private String zf_money;
    private String free_type;
    private String inTime;
    private String outTime;
    public PaymentVo() {
    }

    public PaymentVo(String pay_money, String discount_money, String resitime) {
        this.pay_money = pay_money;
        this.discount_money = discount_money;
        this.resitime = resitime;
    }
}
