package com.jsdc.zhtc.enums;

import lombok.Getter;

@Getter
public enum LogEnums {
    LOG_LOGIN("1", "登录"),
    LOG_LOGOUT("2", "登出"),
    LOG_RESOURCE("3", "系统资源"),
    LOG_UPDCARNO("4", "车牌修改"),
    LOG_UPDAPPALE("5", "申诉审核"),
    LOG_INVOICECASH("6", "发票现金"),
    LOG_ADDUSER("7", "新增用户"),
    LOG_UPDATEUSER("8", "修改用户"),
    LOG_DELETEUSER("9", "删除用户"),
    LOG_UPDATECONFIG("10", "修改系统参数"),
    LOG_ADDMONTHYCONFIG("11", "新增包月配置"),
    LOG_UPDATEMONTHYCONFIG("12", "修改包月配置"),
    LOG_DELETEMONTHYCONFIG("13", "删除包月配置"),
    LOG_DELETEBATCHMONTHYCONFIG("14", "批量删除包月配置"),
    LOG_ADDAPPEAL("15", "申诉录入"),
    LOG_ADDCHARGECROGRAMME("16", "新增收费方案"),
    LOG_UPDATECHARGECROGRAMME("17", "修改收费方案"),
    LOG_DELETECHARGECROGRAMME("18", "删除收费方案"),
    LOG_DELETEBATCHCHARGECROGRAMME("18", "批量删除收费方案"),
    LOG_DELETEPARKINGORDER("19", "删除停车场订单"),
    LOG_REFUNDMANAGEMENT("20", "退款日志"),
    LOG_ADDMONTHLYMANAGEMENT("21", "批量导入个人包月"),
    LOG_ADDWHITECARNO("22", "批量导入白名单车牌"),
    LOG_GATEOPEN("23", "人工开闸"),
    LOG_WXPAYERROR("24", "微信小程序缴费根据支付id未找到停车订单"),
    LOG_DELETEROADPARKINGORDER("25", "删除路内订单"),
    ;


    LogEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;

    private String desc;
}
