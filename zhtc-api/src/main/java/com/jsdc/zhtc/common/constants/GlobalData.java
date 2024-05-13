package com.jsdc.zhtc.common.constants;

import io.netty.channel.Channel;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class GlobalData {

    //pos消息已读
    public final String IS_READ_YES = "1";
    //pos消息未读
    public final String IS_READ_NO = "0";

    public String AES_KEY = "0CoJUm6Qyw8W8jud";
    public String AES_VALUE = "0102030405060708";
    //运营端日志
    public String SYS_LOG_TYPE_PC = "1";
    //POS日志
    public String SYS_LOG_TYPE_POS = "2";
    //审核通过
    public String WX_MSG_MANAGER_YES = "2";
    //驳回
    public String WX_MSG_MANAGER_NO = "3";
    //车牌申诉成功
    public String APPEAL_STATUS_YES = "1";
    //车牌申诉失败
    public String APPEAL_STATUS_NO = "0";
    //车牌申诉处理提醒
    public String WX_MSG_MANAGER_APPEAL = "5";
    //逻辑删除 是
    public String ISDEL_YES = "1";
    //逻辑删除 否
    public String ISDEL_NO = "0";
    //禁用
    public String DISABLE = "0";
    //启用
    public String ENABLE = "1";
    //逗号分隔符
    public String SPLIT_COMMA = ",";
    //黄牌
    public String CARTYPE = "3";
    //路段||运营
    public String PARKING_TYPE_ROAD = "0";
    //停车场
    public String PARKING_TYPE_PLAT = "1";
    //角色路段
    public String ROLE_ROAD_TYPE = "road";
    //角色停车场
    public String ROLE_PARK_TYPE = "park";
    //停车超时时间（暂定10分钟，后期改成灵活的）
    public Integer OVERTIME = 20;
    //申诉订单(1、待处理)
    public String OPERATE_APPEAL_DCL = "1";
    //申诉订单通过
    public String HANDLE_STATUS_ADOPT = "2";
    //申诉订单驳回
    public String HANDLE_STATUS_REJECT = "3";
    //申诉订单完成
    public String HANDLE_STATUS_COMPLETE = "4";
    /*停车订单状态start*/
    //在停
    public String PARKING_ORDER_STOP = "1";
    //待缴费
    public String PARKING_ORDER_STAYPAY = "2";
    //已缴费
    public String PARKING_ORDER_ALREADYPAY = "3";
    //已完成
    public String PARKING_ORDER_COMPLETE = "4";
    //是否上传交管||是否领取发票||订单类型
    public String ISUPLOAD = "0";
    /*停车订单状态end*/
    //支付方式 包月
    public String PARKING_ORDER_TYPE_BY = "1";
    //支付方式 微信
    public String PARKING_ORDER_TYPE_WX = "2";
    //支付方式 支付宝
    public String PARKING_ORDER_TYPE_ZFB = "3";
    //支付方式 钱包
    public String PARKING_ORDER_TYPE_QB = "4";
    //支付方式 现金
    public String PARKING_ORDER_TYPE_XJ = "5";
    //支付方式 聚合支付
    public String PARKING_ORDER_TYPE_UNION = "8";
    //未知车牌
    public String PARKING_DIRECTION_UNKNOW = "0";
    //驶入
    public String PARKING_DIRECTION_IN = "1";
    //驶离
    public String PARKING_DIRECTION_OUT = "2";
    //订单来源：人工
    public String PARKING_SOURCE_ARTIFICIAL = "2";
    //订单来源：摄像机
    public String PARKING_SOURCE_CAMERA = "1";
    /*停车场订单生成规则*/
    //路段
    public String ROLE_R = "R";
    //停车场
    public String ROLE_P = "P";
    //发票类型 电子发票
    public String INVOCE_TYPE_ELECTRONIC = "0";
    //发票类型 手撕发票
    public String INVOCE_TYPE_HANDTEAR = "1";

    public String MONTHLY_CONFIG_CATEGORY = "monthlyType";
    //车牌名单类型 普通名单
    public String ROSTER_TYPE_ORDINARY = "1";
    //车牌名单类型 黑名单
    public String ROSTER_TYPE_BLACK = "2";
    //车牌名单类型 白名单
    public String ROSTER_TYPE_WHITE = "3";
    //车牌名单类型 残疾人车辆
    public String ROSTER_TYPE_DISABLED = "4";
    //车牌白名单类型 内部车
    public String WHITE_TYPE_INNER = "0";
    //车牌白名单类型 企业免税车
    public String WHITE_TYPE_FREEDUTY = "1";

    /*停车位状态*/
    // 空闲
    public String PARKING_STATUS_FREE = "1";
    // 被占用/正在使用
    public String PARKING_STATUS_OCCUPY = "2";
    // 异常
    public String PARKING_STATUS_ABNORMAL = "3";
    // 异常在用
    public String PARKING_STATUS_ABNORMAL_OCCUPY = "4";

    //绑定车牌
    public String CARNO_BIND = "0";
    //解绑车牌
    public String CARNO_UNBIND = "1";

    //是领取纸质发票
    public String RECEIVE_PAPER_INVOICE_NO = "0";
    //否领取纸质发票
    public String RECEIVE_PAPER_INVOICE_YES = "1";
    //车牌类型：蓝牌
    public String CAR_TYPE_BLUE = "1";
    //车牌类型：绿牌
    public String CAR_TYPE_GREEN = "2";
    //车牌类型：黄牌
    public String CAR_TYPE_YELLOW = "3";
    //车牌类型：白牌
    public String CAR_TYPE_WHITE = "4";

    //未反馈
    public String UN_FEEDBACK = "1";
    //已反馈
    public String FEEDBACK = "2";
    // 免单类型 0:个人包月 1:白名单 2:未达到免费时长 3:特殊开闸 4:企业包月 5:临时通行
    public String FREETYPEBY = "0";
    public String FREETYPEBMD = "1";
    public String FREETYPETIME = "2";
    public String FREETYPESPECIAL = "3";
    public String FREETYPEQYBY = "4";
    public String FREETYPELSTX = "5";
    //车辆白名单
    public String ROSTERTYPEBMD = "3";

    //已领取发票
    public String IS_INVOICE = "1";
    //未领取发票
    public String NOT_INVOICE = "0";

    //已删除
    public String IS_DEL_YES = "1";
    //未删除
    public String IS_DEL_NO = "0";

    //停车场设备是否是双通道设备 否
    public String IS_DOUBLE_WAY_NO = "0";
    //停车场设备是否是双通道设备 是
    public String IS_DOUBLE_WAY_YES = "1";

    //领取发票时间
    public String SYS_CONFIG_INVOICEAPPLYTIME = "invoiceApplyTime";
    //缴费后限时出场时间
    public String SYS_CONFIG_LEAVETIME = "leaveTime";
    //合并订单时间
    public String SYS_CONFIG_MERGETIME = "mergeTime";
    //注册会员赠送金额
    public String SYS_CONFIG_GIFTMONEY = "giftMoney";
    //残疾人标识
    public String DISCOUNTZK = "Discount";
    //路段正常订单
    public String ROAD_ORDER_NORMAL = "0";
    //路段异常订单
    public String ROAD_ORDER_ABNORMAL = "1";

    //启动
    public String IS_USE_YES = "1";
    //禁用
    public String IS_USE_NO = "0";
    //正常驶出
    public String OPERATION_TYPE_REGULAR = "0";
    //遥控驶出
    public String OPERATION_TYPE_REMOTE = "1";
    //事件类型
    public String EVENTTYPE = "6";

    //订单待支付
    public String ORDER_STAPAY = "1";
    //订单已支付
    public String ORDER_HASPAY = "2";
    //订单支付失败
    public String ORDER_PAYFAIL = "3";

    public Map<String, Integer> HEART_BEAT_OBJ = new HashMap<>();

    public String HEART_BEAT_REDIS_KEY = "heartbeat_key";

    //redis存储netty通道key值
    public String NETTY_CHANNEL_REDIS_KEY = "guid_channel_key";

    //全局netty通道
    public Map<String, Channel> CHANNEL_MAP = new HashMap<>();


}
