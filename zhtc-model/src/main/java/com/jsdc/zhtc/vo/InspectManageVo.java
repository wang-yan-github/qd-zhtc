package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.InspectManage;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * ClassName: InspectManageVo
 * Description:
 * date: 2021/12/30 14:55
 *
 * @author zonglina
 */
@Data
public class InspectManageVo extends InspectManage {

    //巡检员id
    private Integer inspect_id;
    //区域
    private Integer area_id;
    //街道
    private Integer street_id;
    //路段
    private Integer road_id;
    //状态
    private String status;
    //开始时间
    private String start;
    //结束时间
    private String end;
    //余额
    private String after_account_balance;
    //泊车数量
    private Integer berth_num;

    public void setAfter_account_balance(String after_account_balance) {
        if (StringUtils.isBlank(after_account_balance) || !compareTo2(after_account_balance, "0")) {
            this.after_account_balance = "0.00";
        } else {
            this.after_account_balance = new BigDecimal(after_account_balance).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
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
