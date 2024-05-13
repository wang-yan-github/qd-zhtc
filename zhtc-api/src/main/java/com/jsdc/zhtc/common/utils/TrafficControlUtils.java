package com.jsdc.zhtc.common.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 市交控接口
 * 上行接口
 *
 * @author ZDQ
 */
@Component
public class TrafficControlUtils {

    private static String trafficUrl;

    private static String key;

    private static String secret;


    @Value("${jsdc.trafficUrl}")
    public void setGateUrl(String trafficUrl) {
        TrafficControlUtils.trafficUrl = trafficUrl;
    }

    @Value("${jsdc.trafficKey}")
    public void setKey(String trafficKey) {
        TrafficControlUtils.key = trafficKey;
    }

    @Value("${jsdc.trafficSecret}")
    public void setSecret(String trafficSecret) {
        TrafficControlUtils.secret = trafficSecret;
    }

    @Autowired
    private ParkService parkService;

    /**
     * 发送请求
     */
    public static ResultInfo sendRequests(String url, Map<String, Object> map) {
        map.put("appkey", key);
        map.put("time", System.currentTimeMillis());
        JSONObject params = mapToJsonObject(map);

        SortedMap<String, Object> temps = new TreeMap<>();//升序
        temps.putAll(map);
        String token = getToken(url, JSON.toJSONString(temps), secret);
        url = trafficUrl + url;
        String body = HttpRequest.post(url + "?token=" + token).setConnectionTimeout(3000)
                .header("Content-Type", "application/json")
                .body(params.toJSONString())
                //.auth(getToken(url, params.toJSONString(), secret))
                .execute().body();
        return ResultInfo.success(body);
    }

    /**
     * Map<String, Object> 根据 key 值排序 返回排序好的Map
     */
    public static JSONObject mapToJsonObject(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        if (!CollectionUtils.isEmpty(map)) {
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                jsonObject.put(entry.getKey(), entry.getValue());
            });
        }
        return jsonObject;
    }


    /**
     * MD5 生成 token
     * token 生成规则
     * token = MD5(请求 URI + 参数串 + secret)；
     * 参数串包括：接口业务参数、必传参数 appkey 和 time。
     * 注：加密的字符串统一采用 UTF-8 编码。
     */
    public static String getToken(String url, String str, String secret) {
        String token = MD5Utils.getMD5(url + str + secret, "UTF-8");
        return token;
    }

    /**
     * 数据处理
     * 3.2.2 路外车辆预出场上报
     * 1.封闭式停车场
     * 2.车辆预出场上报，根据计费规则在哪一方传参数
     * 3.调用周期：实时
     * 4.接口返回成功不代表扣款成功，扣款成功只通过支付回调通知接口
     * http://server:port/api/outer/postOuterCarPrePassOutData
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回说明信息
     * data 	Object	null
     */
    public static ResultInfo postOuterCarPrePassOutData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        Map<String, Object> params = new HashMap();
        // 订单编号 选填（停车场计费 必填）
        if (isNull(map.get("orderNo"))) {
            return ResultInfo.error("订单编号不能为空");
        }
        params.put("orderNo", map.get("orderNo"));

        // 账单编号 选填（停车场计费 必填）
        if (isNull(map.get("billId"))) {
            return ResultInfo.error("账单编号不能为空");
        }
        params.put("billId", map.get("billId"));

        // 平台停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        params.put("parkCode", map.get("parkCode"));

        // 停车场名称 选填
        params.put("parkName", map.get("parkName"));

        // 车牌号码 必填
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号码不能为空");
        }
        params.put("plateNo", map.get("plateNo"));

        // 车辆进场时间 选填（停车场计费 必填）
        if (isNull(map.get("enterTime"))) {
            return ResultInfo.error("车辆进场时间不能为空");
        }
        params.put("enterTime", map.get("enterTime"));
        // 结算时间 选填（停车场计费 必填）
        if (isNull(map.get("costTime"))) {
            return ResultInfo.error("结算时间不能为空");
        }
        params.put("costTime", map.get("costTime"));

        // 停车时长（分钟）选填（停车场计费 必填）int
        if (isNull(map.get("parkPeriodTime"))) {
            return ResultInfo.error("停车时长不能为空");
        }
        params.put("parkPeriodTime", map.get("parkPeriodTime"));

        // 支付状态 0 待支付 1 已支付 选填（停车场计费 必填）int
        if (isNull(map.get("payStatus"))) {
            return ResultInfo.error("支付状态不能为空");
        }
        params.put("payStatus", map.get("payStatus"));

        // 应收金额 分 选填（停车场计费 必填）
        if (isNull(map.get("totalCost"))) {
            return ResultInfo.error("应收金额不能为空");
        }
        params.put("totalCost", map.get("totalCost"));

        // 优惠金额 分 选填（停车场计费 必填）
        if (isNull(map.get("freeMoney"))) {
            return ResultInfo.error("优惠金额不能为空");
        }
        params.put("freeMoney", map.get("freeMoney"));

        // 已付金额 分 选填（停车场计费 必填）
        if (isNull(map.get("preMoney"))) {
            return ResultInfo.error("已付金额不能为空");
        }
        params.put("preMoney", map.get("preMoney"));
        // 待付金额 分 选填（停车场计费 必填）
        if (isNull(map.get("payMoney"))) {
            return ResultInfo.error("待付金额不能为空");
        }
        params.put("payMoney", map.get("payMoney"));

        return sendRequests("/api/outer/postOuterCarPrePassOutData", params);
    }

    /**
     * 3.3.1 支付账单同步
     * http://serverIP/api/upload/payBillInfo
     * 封闭停车场将通过非云平台支付渠道支付的账单信息同步给云平台
     * 返回参数
     * 名称	数据类型	说明
     * code	int	0 成功  其他失败
     * msg	string	success 其他失败原因
     * data	object
     */
    public static ResultInfo payBillInfo(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        Map<String, Object> params = new HashMap();
        // 停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("停车场编号不能为空");
        }
        params.put("parkCode", map.get("parkCode"));

        // 车牌号 必填）
        if (isNull(map.get("carNo"))) {
            return ResultInfo.error("车牌号不能为空");
        }
        params.put("carNo", map.get("carNo"));

        // 入车时间 (yyyy-MM-dd HH:mm:ss) 必填
        if (isNull(map.get("entryTime"))) {
            return ResultInfo.error("入车时间必填不能为空");
        }
        params.put("entryTime", map.get("entryTime"));

        // 订单总金额，单位分 必填
        if (isNull(map.get("amount"))) {
            return ResultInfo.error("订单总金额(单位分)，不能为空");
        }
        params.put("amount", map.get("amount"));

        // 支付金额，单位分 必填
        if (isNull(map.get("payAmount"))) {
            return ResultInfo.error("支付金额(单位分)，必填不能为空");
        }
        params.put("payAmount", map.get("payAmount"));

        // 优惠金额，单位分 必填
        if (isNull(map.get("discount"))) {
            return ResultInfo.error("优惠金额(单位分)，必填不能为空");
        }
        params.put("discount", map.get("discount"));

        // 支付类型 （1-现金；2-支付宝；3-微信；4-银联） 必填
        if (isNull(map.get("payType"))) {
            return ResultInfo.error("支付类型必填不能为空");
        }
        params.put("payType", map.get("payType"));

        // 支付时间 (yyyy-MM-dd HH:mm:ss) 必填
        if (isNull(map.get("payTime"))) {
            return ResultInfo.error("支付时间必填不能为空");
        }
        params.put("payTime", map.get("payTime"));

        return sendRequests("/api/upload/payBillInfo", params);
    }

    /**
     * 3.3.2 岗亭修改入车车牌号
     * 封闭停车场将修改后的入车车牌号同步给云平台
     * http://server:port/api/outer/postModifyCarNo
     * 返回参数
     * 名称	数据类型	说明
     * code	int	0 成功  其他失败
     * msg	string	success 其他失败原因
     * data	object
     */
    public static ResultInfo postModifyCarNo(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        Map<String, Object> params = new HashMap();
        // 停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("停车场编号不能为空");
        }
        params.put("parkCode", map.get("parkCode"));

        // 原始车牌号 必填）
        if (isNull(map.get("oldPlateNo"))) {
            return ResultInfo.error("原始车牌号不能为空");
        }
        params.put("oldPlateNo", map.get("oldPlateNo"));

        // 车牌号 必填
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号必填不能为空");
        }
        params.put("plateNo", map.get("plateNo"));


        return sendRequests("/api/outer/postModifyCarNo", params);
    }

    /**
     * 路外停车账单查询接口 http://server:port/api/bill/parkPaymentInfo
     * 1．封闭式停车场
     * 2．停车用户查询停车进行中账单信息
     *
     * @param map
     * @return
     */
    public static ResultInfo parkPaymentInfo(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        Map<String, Object> params = new HashMap();
        // 停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("停车场编号不能为空");
        }
        params.put("parkCode", map.get("parkCode"));

        // 车牌号 必填
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号必填不能为空");
        }
        params.put("plateNo", map.get("plateNo"));


        return sendRequests("/api/bill/parkPaymentInfo", params);
    }

    /**
     * 岗亭二维码查询账单接口 http://server:port/api/bill/getPayQrCode
     * 1.路外停车场
     * 2.用于停车用户在岗亭扫码直接获取停车账单
     * 3.仅支持单边计费方式
     *
     * @param map
     * @return
     */
    public static ResultInfo getPayQrCode(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        Map<String, Object> params = new HashMap();
        // 停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("停车场编号不能为空");
        }
        params.put("parkCode", map.get("parkCode"));

        // 车场出口数字编号 （1-1 号出口；2-2 号出口…） 必填
        if (isNull(map.get("exitNo"))) {
            return ResultInfo.error("车场出口数字编号必填不能为空");
        }
        params.put("exitNo", map.get("exitNo"));

        // 1- 车场计费，返回账单信息；2-平台计费，返回车牌号 必填
        if (isNull(map.get("type"))) {
            return ResultInfo.error("计费类型不能为空");
        }
        params.put("type", map.get("type"));


        return sendRequests("/api/bill/getPayQrCode", params);
    }

    /**
     * 对象转map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        return JSONObject.parseObject(JSONObject.toJSONString(obj), Map.class);
    }

    /**
     * 判断object是否为空,类型是否为Integer或者是String,如果是String判断是否为空
     */
    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Integer) {
            return false;
        }
        if (object instanceof String) {
            return StringUtils.isBlank(object.toString());
        }
        return false;
    }

    /**
     * 3.2.3 路外车辆出场记录
     * 1．封闭式停车场
     * 2．车辆出场记录
     * 3．调用周期：实时
     * http://server:port/api/outer/postOuterCarPassOutData
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回说明信息
     * data 	Object	null
     */
    public static ResultInfo postOuterCarPassOutData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //actTime 发生时间（yyyy-MM-dd HH:mm:ss）24 小时制 必 填 string
        if (isNull(map.get("actTime"))) {
            return ResultInfo.error("发生时间不能为空");
        }
        //actType 进/出场标识（1：进场，2：出场）
        if (isNull(map.get("actType"))) {
            return ResultInfo.error("进/出场标识不能为空");
        }
        // parkCode 平台停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        //deviceCode 设备（入场/出场）编号 选填
        //district 行政区号 选填 见附录徐州片区数据
        //parkName 停车场名称 选填
        //plateNo 车牌号 必填
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号不能为空");
        }
        // plateColor 车牌颜色 必填 详见附件
        if (isNull(map.get("plateColor"))) {
            return ResultInfo.error("车牌颜色不能为空");
        }
        //picUrl 抓拍图片的 Url 走图片上报接口时选填，imageId 为空是 必填
        if (isNull(map.get("imageId")) && isNull(map.get("picUrl"))) {
            return ResultInfo.error("抓拍图片不能为空");
        }
        //imageId 第三方平台出车图片 id 走图片上报接口时必填

        return sendRequests("/api/outer/postOuterCarPassOutData", map);
    }

    /**
     * 3.2.4 停车场剩余泊位上报
     * 1．封闭式停车场
     * 2．上传停车场泊位数信息
     * 3．调用周期：实时
     * http://server:port/api/outer/postPlaceData
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回说明信息
     */
    public static ResultInfo postPlaceData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //parkCode 平台停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        //createTime 上传时间(格式：yyyy-MM-dd HH:mm:ss) 必填
        if (isNull(map.get("createTime"))) {
            return ResultInfo.error("上传时间不能为空");
        }
        // totalPlaceCount 停车场总泊位数 必填
        if (isNull(map.get("totalPlaceCount"))) {
            return ResultInfo.error("停车场总泊位数不能为空");
        }
        //reducePlaceCount 停车场剩余泊位数
//        if (isNull(map.get("reducePlaceCount"))) {
//            return ResultInfo.error("停车场剩余泊位数不能为空");
//        }

        return sendRequests("/api/outer/postPlaceData", map);
    }

    /**
     * 3.2.2 图片上报接口
     * 1．封闭式停车场&路内停车场
     * 2．停车场上传车辆到达（进场）、离开（出场）照片
     * 3．调用周期：实时
     * http://server:port/api/outer/postPic
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回说明信息
     * data	Object	null
     */
    public static ResultInfo postPic(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //imageId 第三方平台图片 id（与出入车记录保持一致）必填
        if (isNull(map.get("imageId"))) {
            return ResultInfo.error("第三方平台图片不能为空");
        }
        //parkType 停车场类型：1-路内；2-路外 必填 【传2路外】
        if (isNull(map.get("parkType"))) {
            return ResultInfo.error("停车场类型不能为空");
        }
        //picTime 照片生成时间(格式：yyyy-MM-dd HH:mm:ss)
        //type 到达或离开的照片（0-入车；1-出车）
        //name 照片文件名
        // file Base64 编码字符串（注：大小不超过 3M）或可访问的图片 url 必填
        if (isNull(map.get("file"))) {
            return ResultInfo.error("图片 url不能为空");
        }

        return sendRequests("/api/outer/postPic", map);
    }

    /**
     * 3.1.1 停车场基本信息上报接口
     * 1.路外停车场；
     * 2.将停车场基础数据（包括名称、区域、车位数等信息）上报给平台；
     * 3.调用周期：初始上报，发生变化上报
     * http://server:port/api/outer/postParkInfo
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回信息说明
     * data	Object	字段如下
     * parkCode	string	平台停车场编号
     * 对应业务方法
     * /park/toAdd.do
     * /park/toEdit.do
     */
    public ResultInfo postParkInfo(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        // postType 上报类型：1-新增；2-修改 必填
//        if (isNull(map.get("postType")) || 2 != (int) map.get("postType")) {
//            return ResultInfo.error("上报类型不能为空,且只能修改");
//        }
        //停车场编号,必传
        if (isNull(map.get("thirdParkCode"))) {
            return ResultInfo.error("停车场编号,必传");
        }
        //停车场名称 必填
        if (isNull(map.get("thirdParkName"))) {
            return ResultInfo.error("停车场名称 必填");
        }
        if (isNull(map.get("longitude"))) {
            return ResultInfo.error("经度,必填");
        }
        if (isNull(map.get("latitude"))) {
            return ResultInfo.error("纬度,必填");
        }
        if (isNull(map.get("address"))) {
            return ResultInfo.error("停车场地址,必填");
        }
        if (isNull(map.get("isPayMonth"))) {
            return ResultInfo.error("是否支持包月,必填");
        }
        if (isNull(map.get("totalPlaceCount"))) {
            return ResultInfo.error("停车场总停车位,必填");
        }

        String url = "/api/outer/postParkInfo";
        map.put("appkey", key);
        map.put("time", System.currentTimeMillis());
        JSONObject params = mapToJsonObject(map);

        SortedMap<String, Object> temps = new TreeMap<>();//升序
        temps.putAll(map);
        String token = getToken(url, JSON.toJSONString(temps), secret);
        url = trafficUrl + url;
        String body = "";
        try {
            body = HttpRequest.post(url + "?token=" + token).setConnectionTimeout(3000)
                    .header("Content-Type", "application/json")
                    .body(params.toJSONString())
                    //.auth(getToken(url, params.toJSONString(), secret))
                    .execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        TrfficPostParkVo trfficPostParkVo = JSON.parseObject(body, TrfficPostParkVo.class);
//        String parkCode = trfficPostParkVo.getData().getParkCode();

//        String code = (String) map.get("thirdParkCode");
//        QueryWrapper<Park> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("park_code", code);
//        queryWrapper.eq("is_del", "0");
//        Park park = parkService.selectOne(queryWrapper);
//        if (null != park) {
//            park.setTraffic_park_code(parkCode);
//            parkService.updateById(park);
//        }

        return ResultInfo.success(body);
    }


    /**
     * 3.1.2 车场终端设备上报接口
     * 1．路内&路外停车场
     * 2．上报车场的终端设备信息
     * 3．调用周期：每天
     * http://server:port/api/outer/postParkDevice
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回信息说明
     * data	object	包含以下信息
     */
    public static ResultInfo postParkDevice(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //上报类型 必填 1-新增 2-修改
        if (isNull(map.get("postType"))) {
            return ResultInfo.error("上报类型,必填");
        }
        //停车场名称 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场关联编号 必填");
        }
        //设备IP  与deviceSN选填一个
//        if (isNull(map.get("deviceIp"))) {
//            return ResultInfo.error("平台停车场关联编号 必填");
//        }
        return sendRequests("/api/outer/postParkDevice", map);
    }

    /**
     * 3.2.1 路外车辆入场记录
     * 1．封闭式停车场
     * 2．车辆入场记录
     * 3．调用周期：实时
     * http://server:port/api/outer/postOuterCarPassInData
     * 返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     * msg	string	返回说明信息
     * data 	Object	null
     */
    public static ResultInfo postOuterCarPassInData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        if (isNull(map.get("actTime"))) {
            return ResultInfo.error("车辆入场时间,必填");
        }
        if (isNull(map.get("actType"))) {
            return ResultInfo.error("车辆入场类型,必填");
        }
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号,必填");
        }
        if (isNull(map.get("orderNo"))) {
            return ResultInfo.error("订单号,必填");
        }
        if (isNull(map.get("deviceCode"))) {
            return ResultInfo.error("设备编号,必填");
        }
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号,必填");
        }
        if (isNull(map.get("plateColor"))) {
            return ResultInfo.error("车牌颜色,必填");
        }
        if (isNull(map.get("picUrl"))) {
            return ResultInfo.error("车辆入场图片,必填");
        }
//        if (isNull(map.get("imageId"))) {
//            return ResultInfo.error("车辆入场图片编号,必填");
//        }
        return sendRequests("/api/outer/postOuterCarPassInData", map);
    }
    //停车场交控接口  end =======================================================

    //路内交控接口  start =======================================================

    /**
     * 路内
     * 3.1.1 路段基本信息上报接口
     * http://server:port/api/inner/postParkInfo
     * 1. 路段；
     * 2. 将路段基础数据（包括名称、片区、泊位、车位数等信息）上报给平台；
     * 3. 调用周期：初始上报，发生变化上报
     * 3.1.1.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data Object 字段如下
     * parkCode string 平台路段编号
     */
    public ResultInfo innerPostParkInfo(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        // postType 上报类型：1-新增；2-修改 必填
        if (isNull(map.get("postType"))) {
            return ResultInfo.error("上报类型不能为空");
        }
        //路段编号,必传
        if (isNull(map.get("thirdParkCode"))) {
            return ResultInfo.error("路段编号,必填");
        }
        //路段名称 必填
        if (isNull(map.get("thirdParkName"))) {
            return ResultInfo.error("路段名称,必填");
        }
        if (isNull(map.get("totalPlaceCount"))) {
            return ResultInfo.error("总泊位数,必填");
        }
        if (isNull(map.get("longitude"))) {
            return ResultInfo.error("经度,必填");
        }
        if (isNull(map.get("latitude"))) {
            return ResultInfo.error("纬度,必填");
        }
        if (isNull(map.get("address"))) {
            return ResultInfo.error("停车场地址,必填");
        }
        if (isNull(map.get("berthInfo"))) {
            return ResultInfo.error("路段泊位数据,必填");
        }
        return sendRequests("/api/inner/postParkInfo", map);
    }

    /**
     * 路内
     * 3.1.3 车场终端设备与泊位关系上报接口
     * http://server:port/api/inner/postParkBerthNos
     * 3.1.3.1 接口描述
     * 1.路内停车场
     * 2.上报车场的终端设备与泊位关联关系
     * 3.调用周期：初始上报，发生变化上报
     * 3.1.3.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data object 包含以下信息
     */
    public static ResultInfo postParkBerthNos(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        // 上报类型 必填 1-新增 2-修改 必填
        if (isNull(map.get("postType"))) {
            return ResultInfo.error("上报类型不能为空");
        }
        // 车牌号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场关联编号，必填");
        }
        if (isNull(map.get("berthInfo"))) {
            return ResultInfo.error("路段泊位数据,必填");
        }
        //berthInfo array 路段泊位数据，包含以下参数 必填 ；berthNo string 泊位编号 必填
//        map.put("berthInfo", berthNo);
        return sendRequests("/api/inner/postParkBerthNos", map);
    }


    /**
     * 3.2 停车数据上报
     * 3.2.1 路内车辆入场记录
     * 3.2.1.1 接口描述
     * 1. 路内停车场
     * 2. 停车场库上传车辆入场
     * 3. 调用周期：实时
     * 3.2.1.2 接口路径
     * http://server:port/api/inner/postInnerCarPassInData
     * 3.2.1.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data object 参数如下
     * IsWhiteCar int 入场上报反馈，是否为内部车辆.
     * 0:临时车,1:白名单车辆
     * 如果为内部车，,出场时不需要同步账单，直接出场
     */
    public static ResultInfo postInnerCarPassInData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //发生时间（yyyy-MM-dd HH:mm:ss）24 小时制 必填
        if (isNull(map.get("actTime"))) {
            return ResultInfo.error("车辆入场时间,必填");
        }
        //进场标识（1：进场）必填
        if (isNull(map.get("actType"))) {
            return ResultInfo.error("车辆入场类型,必填");
        }
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号,必填");
        }
        if (isNull(map.get("orderNo"))) {
            return ResultInfo.error("订单号,必填");
        }
//        if (isNull(map.get("deviceCode"))) {
//            return ResultInfo.error("设备编号,必填");
//        }
        if (isNull(map.get("berthNo"))) {
            return ResultInfo.error("泊位号,必填");
        }
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号,必填");
        }
//        if (isNull(map.get("plateColor"))) {
//            return ResultInfo.error("车牌颜色,选填");
//        }
//        if (isNull(map.get("picUrl"))) {
//            return ResultInfo.error("车辆入场图片,必填");
//        }
//        if (isNull(map.get("imageId"))) {
//            return ResultInfo.error("第三方平台入车图片 id 选填");
//        }
        return sendRequests("/api/inner/postInnerCarPassInData", map);
    }

    /**
     * 3.2.2 路内车辆出场记录
     * 3.2.2.1 接口描述
     * 1. 路段
     * 2. 出车记录
     * 3. 调用周期：实时
     * 3.2.2.2 接口路径
     * http://server:port/api/inner/postInnerCarPassOutData
     * 3.2.2.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data Object null
     */
    public static ResultInfo postInnerCarPassOutData(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //发生时间（yyyy-MM-dd HH:mm:ss）24 小时制 必填 string
        if (isNull(map.get("actTime"))) {
            return ResultInfo.error("发生时间不能为空");
        }
        //出场标识（2：出场）必填
        if (isNull(map.get("actType"))) {
            return ResultInfo.error("出场标识不能为空");
        }
        // parkCode 平台停车场编号 必填
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        //deviceCode string 平台设备编号 选填
        //district string 行政区号 选填 见附录徐州片区数据
        //parkName string 道路名称 选填
        if (isNull(map.get("berthNo"))) {
            return ResultInfo.error("泊位号不能为空");
        }
        if (isNull(map.get("plateNo"))) {
            return ResultInfo.error("车牌号不能为空");
        }
        // plateColor 车牌颜色 选填 详见附件
//        if (isNull(map.get("plateColor"))) {
//            return ResultInfo.error("车牌颜色不能为空");
//        }
        //picUrl 抓拍图片的 Url 走图片上报接口时选填，imageId 为空是 必填
//        if (isNull(map.get("picUrl"))) {
//            return ResultInfo.error("抓拍图片Url不能为空");
//        }
        //imageId 第三方平台出车图片 id 走图片上报接口时必填

        return sendRequests("/api/inner/postInnerCarPassOutData", map);
    }

    /**
     * 4.2 数据同步相关
     * 4.2.1 路内进行中订单关闭
     * 4.2.1.1 接口描述
     * 路内停车场，一级平台与二级平台保持停车数据同步
     * 4.2.1.2 接口路径
     * http://server:port/api/pos/orderClose
     * 4.2.1.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data Object null
     */
    public static ResultInfo orderClose(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
        //发生时间（yyyy-MM-dd HH:mm:ss）24 小时制 必填 string
        if (isNull(map.get("actTime"))) {
            return ResultInfo.error("发生时间不能为空");
        }
        //关闭类型（1-免费；2-已支付；3-未支付）必填
        if (isNull(map.get("type"))) {
            return ResultInfo.error("关闭类型不能为空");
        }
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        if (isNull(map.get("orderNo"))) {
            return ResultInfo.error("平台订单编号不能为空");
        }
        if (isNull(map.get("thirdOrderNo"))) {
            return ResultInfo.error("第三方平台订单编号不能为空");
        }
        return sendRequests("/api/pos/orderClose", map);
    }

    /**
     * 4.2.3 路内订单异常处理
     * 4.2.3.1 接口描述
     * 路内停车场，二级平台上报要修改的停车数据；
     * 1、 一级平台未收到出车数据可以修改入车相关数据；
     * 2、 一级平台已收到出车数据只能修改出车相关数据；
     * 3、 订单作废（已支付订单不支持）；
     * 4.2.3.2 接口路径
     * http://server:port/api/exce/orderExceHandle
     * 4.2.3.4 返回参数
     * 名称 数据类型 说明
     * code int 成功标识（0/-1）
     * msg string 返回信息说明
     * data Object null
     */
    public static ResultInfo orderExceHandle(Map<String, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            return ResultInfo.error("参数不能为空");
        }
//        type int 类型（1-入车；2-出车；3-订单作废）必填
        if (isNull(map.get("type"))) {
            return ResultInfo.error("类型不能为空");
        }
        if (isNull(map.get("parkCode"))) {
            return ResultInfo.error("平台停车场编号不能为空");
        }
        if (isNull(map.get("thirdOrderNo"))) {
            return ResultInfo.error("第三方平台订单编号 必填");
        }
        if (isNull(map.get("desc"))) {
            return ResultInfo.error("异常处理原因描述（不能超过 100 字符）必填");
        }
//        entryTime string 入车时间（yyyy-MM-dd HH:mm:ss）24 小时制
//        outTime string 出车时间（yyyy-MM-dd HH:mm:ss）24 小时制
//        berthNo string 泊位号
//        plateNo string 车牌号
//        plateColor int 车牌颜色 详见附件
//        carSizeType int 0-小型车；1-中型车；2-大型车；3-其他 详见附件
//        picUrl string 抓拍图片的 Url
        return sendRequests("/api/exce/orderExceHandle", map);
    }

    /**
     * 5.3 车牌颜色
     * 名称 数据值 说明
     * 其他 0
     * 蓝色 1
     * 黄色 2
     * 黑色 3
     * 白色 4
     * 绿色 5
     */

}
