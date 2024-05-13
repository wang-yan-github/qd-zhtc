package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.zhtc.model.RefundManagement;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@Data
public class RefundManagementVo extends RefundManagement {
    private String phone;
    private String orderNo;
    private String carNo;

    private String tkTypeName;
    private String payment_type;

    //驶入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;
    //驶出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;
    //车牌类型
    private String car_type;
    /**
     * 支付金额
     */
    private String amount;
    /**
     * 0-路测,1-停车
     */
    private Integer type;

    /**
     * 位置名称
     */
    private String placeName;
    /**
     * 支付完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;

    /**
     * 时间
     */
    private String times;

    /**
     * 路侧/停车场id
     */
    private Integer road_park_id;

    public void setTkTypeName() {

        if (StringUtils.isNotBlank(getRefund_channel())) {
            if (getRefund_channel().equals("2")) {
                tkTypeName = "微信";
            } else if (getRefund_channel().equals("3")) {
                tkTypeName = "支付宝";
            } else if (getRefund_channel().equals("4")) {
                tkTypeName = "钱包";
            } else if (getRefund_channel().equals("5")) {
                tkTypeName = "现金";
            }
        } else
            tkTypeName = "未知";

    }
}
