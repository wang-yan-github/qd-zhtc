package com.jsdc.zhtc.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * ClassName: InvoicingVo
 * Description:开票统计
 * date: 2022/1/7 11:49
 *
 * @author wh
 **/
@Data
public class InvoicingVo {
    //用户
    private String userName;
    //开票金额余额(元)
    private String balance;
    //类型
    private String type;
    //开票时间
    private String time;

    private String phone;
    //来源
    private String source;

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
