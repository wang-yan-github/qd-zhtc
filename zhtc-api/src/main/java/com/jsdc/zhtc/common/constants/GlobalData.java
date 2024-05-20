package com.jsdc.zhtc.common.constants;

import io.netty.channel.Channel;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举类
 * 常用公共变量
 */
@UtilityClass
public class GlobalData {

    //逻辑删除 是
    public String ISDEL_YES = "1";
    //逻辑删除 否
    public String ISDEL_NO = "0";

    //已删除
    public String IS_DEL_YES = "1";
    //未删除
    public String IS_DEL_NO = "0";

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

    //支付方式 微信
    public String PARKING_ORDER_TYPE_WX = "2";
    //支付方式 支付宝
    public String PARKING_ORDER_TYPE_ZFB = "3";
    //支付方式 现金
    public String PARKING_ORDER_TYPE_XJ = "5";
    //支付方式 聚合支付
    public String PARKING_ORDER_TYPE_UNION = "8";

    //驶入
    public String PARKING_DIRECTION_IN = "1";
    //驶离
    public String PARKING_DIRECTION_OUT = "2";

    //订单来源：人工
    public String PARKING_SOURCE_ARTIFICIAL = "2";
    //订单来源：摄像机
    public String PARKING_SOURCE_CAMERA = "1";

    /*停车场订单生成规则*/
    //停车场
    public String ROLE_P = "P";

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

    /**
     * 系统配置
     */
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

    //车主管理-车主属性
    public String CAR_OWNER_TYPE = "car_owner_type";
    //减免规则管理-类型
    public String RULE_CONFIGURATION_TYPE = "rule_configuration_type";
}
