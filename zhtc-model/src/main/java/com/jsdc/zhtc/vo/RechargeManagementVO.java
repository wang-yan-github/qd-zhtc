package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.model.RechargeManagement;
import lombok.Data;

import java.util.List;

@Data
public class RechargeManagementVO extends RechargeManagement {
    private String paymentNo;
    private String phone;
    private String paymentType;
    private String nickName;
    private String balance;
    private String typeName = "充值";

    // 充值金额、
    private String czMoney;
    // 付款金额、
    private String amount;
    // 付款方式
    private String payment_type;
    //图片路径
    private List<FileManage> fileList;
}
