package com.jsdc.zhtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.Base;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.vo.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@SuppressWarnings("ALL")
public class Sjfx2Service {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private ParkDeviceService parkDeviceService;

    @Autowired
    private RefundManagementService refundManagementService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private OperateFeedbackService operateFeedbackService;
    @Autowired
    private OperateAppealService operateAppealService;
    @Autowired
    private OperateCarnoService operateCarnoService;
    @Autowired
    private AppealHandleRecordService appealHandleRecordService;
    @Autowired
    private CwMwlDataService cwMwlDataService;
    @Autowired
    private CwZzlDataService cwZzlDataService;
    @Autowired
    private SjfxService sjfxService;


    //************************数据总览 start  定时刷新：每小时************************

    //测试
    public static void main(String[] args) {
//        System.out.println(new DateTime().plusDays(-7).toString("yyyy-MM-dd"));
//        System.out.println(new DateTime().toString("yyyy-MM-dd"));

        int hour = new DateTime().getHourOfDay();
        for (int i = 0; i <= hour; i++) {
            System.out.println(String.format("%02d", i));
        }
    }


    /**
     * 左图1
     * 今日营收
     * 金额单位：元
     * 今日总营收
     * 停车收费 今日/昨日
     * 包期 今日/昨日
     * 退款 今日/昨日
     * 今日欠费、今日优惠、今日充值
     */
    public JSONObject getMainLeft1Data() {
        JSONObject jsonObject = new JSONObject();
        CommonVo data = new CommonVo();
        //type 1今日 2昨日
        setMainLeft1Data(data, jsonObject, "1");
        //昨日
        setMainLeft1Data(data, jsonObject, "2");
        return jsonObject;
    }

    /**
     * 左图1 今日营收 setdata方法
     *
     * @param data
     * @param jsonObject
     * @param type       1今日 2昨日
     */
    public void setMainLeft1Data(CommonVo data, JSONObject jsonObject, String type) {
        //退款管理
//        RefundManagementVo refundManagementVo = new RefundManagementVo();
        //停车订单 总优惠金额
        ParkingOrderVo parkingOrderVo = new ParkingOrderVo();

        RoadParkListVo vo = new RoadParkListVo();//查询条件对象
        RoadParkListVo vo2 = new RoadParkListVo();//结果data

        PaymentOrderVo paymentOrderVo = new PaymentOrderVo();
        ReportVo roadPaymentOrderList = null;
//        ReportVo parkPaymentOrderList = null;

        //1今日 2昨日
        if (type.equals("1")) {
//            data.setStartTime(new DateTime().toString("yyyy-MM-dd"));
//            data.setEndTime(new DateTime().toString("yyyy-MM-dd"));

//            refundManagementVo.setTimes(new DateTime().toString("yyyy-MM-dd"));

            parkingOrderVo.setStartDate(new DateTime().toString("yyyy-MM-dd"));

            //应收金额、已付金额/营收金额、待付金额/欠费金额
            vo.setPayTime(new DateTime().toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo = parkingOrderService.getSumAmounts(vo);
            vo2.setPaid_amount(roadParkListVo.getPaid_amount());//已付金额/营收金额

            vo.setPayTime("");
            vo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(vo);
            vo2.setSum_amount(roadParkListVo2.getSum_amount());//应收金额
            vo2.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//待付金额/欠费金额

            paymentOrderVo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
//            paymentOrderVo.setParkingType("0");
            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
            roadPaymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);
//            paymentOrderVo.setParkingType("1");
//            parkPaymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);

        } else {
            //昨日
//            data.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
//            data.setEndTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));

//            refundManagementVo.setTimes(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));

            parkingOrderVo.setStartDate(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));

            //应收金额、已付金额/营收金额、待付金额/欠费金额
            vo.setPayTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo = parkingOrderService.getSumAmounts(vo);
            vo2.setPaid_amount(roadParkListVo.getPaid_amount());//已付金额/营收金额

            vo.setPayTime("");
            vo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(vo);
            vo2.setSum_amount(roadParkListVo2.getSum_amount());//应收金额
            vo2.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//待付金额/欠费金额

            paymentOrderVo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
//            paymentOrderVo.setParkingType("0");
            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
            roadPaymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);
//            paymentOrderVo.setParkingType("1");
//            parkPaymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);
        }

        //包月收入
//        String sumCost = monthlyManagementService.getMonthlyPaymentSum(data);
//        vo2.setByAmount(sumCost);
//        //总收入 = 已付金额 + 待付金额 +包月收入
//        double today_money = Double.parseDouble(vo2.getPaid_amount()) + Double.parseDouble(vo2.getUnpaid_amount()) + Double.parseDouble(vo2.getByAmount());

        //退款金额
//        RefundManagementVo refundManagementVo2 = refundManagementService.sumRefundAmount(refundManagementVo);

        //总优惠金额
        ParkingOrderVo parkingOrderVo2 = parkingOrderService.sumDiscountMoney(parkingOrderVo);
        //1今日 2昨日
        if (type.equals("1")) {
//            //充值收入
//            String czMoney = rechargeManagementService.getSumCzMoney(data);
//            jsonObject.put("today_czMoney", czMoney);
            //欠费 停车订单待付金额
            jsonObject.put("today_unpaidAmount", vo2.getUnpaid_amount());
            //优惠 停车订单优惠金额+优惠金额
            jsonObject.put("today_discount_money", parkingOrderVo2.getDiscount_amount() + parkingOrderVo2.getDiscount_money());

//            //今日 总营收 = 停车场/路侧总收入 + 充值收入
//            jsonObject.put("today_moneys", today_money + czMoney);
//
//            //今日 停车收费收入 = 停车场/已付金额
//            jsonObject.put("today_paidAmount", vo2.getPaid_amount());
//            //今日 包月收入
//            jsonObject.put("today_moneys", sumCost);
//            //今日 退款金额
//            jsonObject.put("today_refund_amount", refundManagementVo2.getRefund_amount());

            //今日 总营收 = 停车场/路侧总收入 + 充值收入
            jsonObject.put("today_moneys", Double.parseDouble(roadPaymentOrderList.getAmount()) - Double.parseDouble(roadPaymentOrderList.getAmount5()));

            //今日 停车收费收入 = 停车场/已付金额
            jsonObject.put("today_paidAmount", Double.parseDouble(roadPaymentOrderList.getAmount3()));
            //今日 包月收入
            jsonObject.put("today_month_moneys", Double.parseDouble(roadPaymentOrderList.getAmount1()));
            //今日 退款金额
            jsonObject.put("today_refund_amount", Double.parseDouble(roadPaymentOrderList.getAmount5()));

            //充值收入
            jsonObject.put("today_czMoney", Double.parseDouble(roadPaymentOrderList.getAmount2()) + Double.parseDouble(roadPaymentOrderList.getAmount4()));
        } else {
            //昨日
//            //昨日 停车收费收入 = 停车场/路侧已付金额
//            jsonObject.put("yesterday_paidAmount", vo2.getPaid_amount());
//            //昨日 包月收入
//            jsonObject.put("yesterday_moneys", sumCost);
//            //昨日 退款金额
//            jsonObject.put("yesterday_refund_amount", refundManagementVo2.getRefund_amount());

            //昨日 停车收费收入 = 停车场/路侧已付金额
            jsonObject.put("yesterday_paidAmount", Double.parseDouble(roadPaymentOrderList.getAmount3()));
            //昨日 包月收入
            jsonObject.put("yesterday_month_moneys", Double.parseDouble(roadPaymentOrderList.getAmount1()));
            //昨日 退款金额
            jsonObject.put("yesterday_refund_amount", Double.parseDouble(roadPaymentOrderList.getAmount5()));
        }
    }

    /**
     * 左图1
     * 今日营收 折线图
     * 24小时、金额
     */
    public JSONObject getMainLeft1LineChartData() {
        JSONObject jsonObject = new JSONObject();

        PaymentOrderVo vo = new PaymentOrderVo();
        vo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
//        vo.setParkingType("0");
        //今日0时至当前时间各个小时整点营收金额统计
        List<DataChartVo> roadList = paymentOrderService.getMoneyDataByHours(vo);
//        vo.setParkingType("1");
//        List<DataChartVo> parkList = paymentOrderService.getMoneyDataByHours(vo);

        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (int i = 0; i < roadList.size(); i++) {
            nameList.add(String.valueOf(i));
            //金额 - 退款金额
            Double value = Double.parseDouble(roadList.get(i).getAmount()) - Double.parseDouble(roadList.get(i).getAmount2());
            valueList.add(value.toString());
        }

        jsonObject.put("nameList", nameList);
        jsonObject.put("valueList", valueList);

        return jsonObject;
    }

    /**
     * 左图2
     * 车场盘点 折线图、柱状图
     * 3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间
     * 占用率、在停车辆
     * 柱状图：停车场/路侧名称 在停数量top5
     */
    public JSONObject getMainLeft2Data() {
        JSONObject jsonObject = new JSONObject();

        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        List<String> valueList2 = new ArrayList<>();
        List<String> valueList3 = new ArrayList<>();

        // 车位总数（停车场车位总数+路侧泊位总数）、
        String roadNum = parkService.berthSumData("0");//路段
        String parkNum = parkService.berthSumData("1");//停车场
        Integer placeTotal = Integer.parseInt(roadNum) + Integer.parseInt(parkNum);

        //在停车辆
        ParkingOrderVo vo = new ParkingOrderVo();
        ReportVo reportVo = parkingOrderService.getZtCount(vo);
        jsonObject.put("ztCount", reportVo.getZtCount().toString());
        //车位满位率 = （在停数量/泊位总数）
        if (placeTotal.toString().equals("0")) {
            jsonObject.put("mwl", "0");
        } else {
            String mwl = new BigDecimal(reportVo.getZtCount().toString()).divide(new BigDecimal(placeTotal.toString()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).toString();
            if (mwl.indexOf(".") >= 0) {
                jsonObject.put("mwl", mwl.substring(0, mwl.indexOf(".")));
            } else {
                jsonObject.put("mwl", mwl);
            }
        }

        //柱状图：停车场/路侧名称 在停数量top3
        List<ReportVo> list2 = parkingOrderService.getZtCountTop5(vo);
        jsonObject.put("top5Data", list2);

        //3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间 【定时任务】
        CwMwlData cwMwlData = new CwMwlData();
        // 类型 0路测 1停车场 2（路测+停车场）总和
        cwMwlData.setType("2");
        cwMwlData.setTime(new DateTime().toString("yyyy-MM-dd"));
        List<CwMwlData> cwMwlDataList = cwMwlDataService.getHourMwlList(cwMwlData);

        cwMwlData.setTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        List<CwMwlData> cwMwlDataList2 = cwMwlDataService.getHourMwlList(cwMwlData);

        cwMwlData.setTime(new DateTime().plusDays(-7).toString("yyyy-MM-dd"));
        List<CwMwlData> cwMwlDataList3 = cwMwlDataService.getHourMwlList(cwMwlData);

        jsonObject.put("today_data", cwMwlDataList);
        jsonObject.put("yesterday_data", cwMwlDataList2);
        jsonObject.put("last_week_data", cwMwlDataList3);
        return jsonObject;
    }


    /**
     * 左图3
     * 车主分析
     * 用户总数、今日活跃度、7日活跃度（活跃度=车主表绑定openid数量）
     * 本地车占比、绑定车占比（车主表绑定openid的数量）
     * 柱状图 支付方式/金额
     */
    public JSONObject getMainLeft3Data() {
        JSONObject jsonObject = new JSONObject();

        //所有车牌数量
        QueryWrapper<OperateCarno> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("is_del", 0);
        Long carno_count = operateCarnoService.selectCount(wrapper4);

        //本地车数量
        QueryWrapper<OperateCarno> wrapper5 = new QueryWrapper<>();
        wrapper5.eq("is_del", 0);
        wrapper5.like("car_no", "苏C");
        Long carno_c_count = operateCarnoService.selectCount(wrapper5);

        //本地车占比、
        if (carno_count.toString().equals("0")) {
            jsonObject.put("car_count", "0");
        } else {
            jsonObject.put("car_count", new BigDecimal(carno_c_count).divide(new BigDecimal(carno_count), 2, BigDecimal.ROUND_HALF_UP));
        }

        // 绑定车占比（车主表绑定openid的数量）
        QueryWrapper<OperateCarno> wrapper6 = new QueryWrapper<>();
        wrapper6.eq("is_del", 0);
        wrapper6.isNotNull("member_id");
        Long bind_count = operateCarnoService.selectCount(wrapper6);
        if (carno_count.toString().equals("0")) {
            jsonObject.put("bind_count", "0");
        } else {
            jsonObject.put("bind_count", new BigDecimal(bind_count).divide(new BigDecimal(carno_count), 2, BigDecimal.ROUND_HALF_UP));
        }

        //柱状图 支付方式/金额
        RoadParkListVo roadParkListVo = new RoadParkListVo();
        List<PaymentOrder> paymentOrders = paymentOrderService.getMoneysByType(roadParkListVo);
        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (PaymentOrder paymentOrder : paymentOrders) {
            nameList.add(paymentOrder.getPayment_type());
            valueList.add(paymentOrder.getAmount());
        }
        jsonObject.put("nameList", nameList);
        jsonObject.put("valueList", valueList);
        return jsonObject;
    }

    /**
     * 右图1
     * 车位周转率 柱状图、饼状图、双折线图
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     * 饼状图： 停车时长：停车时长状态暂定
     * 折线图： 出入趋势双折线：7天内出入次数
     * type 类型 0路测 1停车场 2（路测+停车场）总和
     * id 路段/停车场id
     */
    public JSONObject getMainRight1Data(String type, Integer id) {
        JSONObject jsonObject = new JSONObject();

        //柱状图：日均车位周转率：最近7天/日均车位周转率【定时任务生成数据】
        CwZzlData cwZzlData = new CwZzlData();
        // 类型 0路测 1停车场 2（路测+停车场）总和
        cwZzlData.setType(type);
        cwZzlData.setRoad_park_id(id);//路段/停车场id
        jsonObject.put("cwZzlData", cwZzlDataService.getDaysZzlList(cwZzlData));

        String type2 = type;
        if (Base.notEmpty(type2) && type2.equals("2")) {
            type2 = "";
        }

        //饼状图： 停车时长：停车时长状态暂定
        //总次数
        String data = parkingOrderService.getTodayParkTimeCount(type2, id);
        //停车时长占比
        List<ReportVo> list = parkingOrderService.getTodayParkTimeData(type2, id);

        List<Map<String, String>> retList = new ArrayList<>();
        String[] nameList = {"0-2小时", "2-4小时", "4-6小时", "6-8小时", "8小时以上"};
        int i = 0;
        if (null != list && list.size() > 0) {
            for (ReportVo reportVo : list) {
                Map<String, String> map = new HashMap<>();
                if (!data.equals("0") && !reportVo.getAmount().equals("0")) {
                    BigDecimal divide = new BigDecimal(reportVo.getAmount()).divide(new BigDecimal(data), 2, RoundingMode.HALF_UP);
                    map.put("value", divide.multiply(BigDecimal.valueOf(100)).toString());
                } else {
                    map.put("value", "0");
                }
                map.put("name", nameList[i]);
                retList.add(map);
                i++;
            }
        }
        jsonObject.put("today_parkTimeData", retList);

        //折线图： 出入趋势双折线：7天内出入次数
        List<DataChartVo> inList = parkingOrderService.getInOutCount(type2, "in");
        List<DataChartVo> outList = parkingOrderService.getInOutCount(type2, "out");
        jsonObject.put("inList", inList);
        jsonObject.put("outList", outList);
        return jsonObject;
    }

    /**
     * 右图2
     * 客户服务 折线图
     * 车主反馈：总咨询量 今日/昨日
     * 订单申诉：总咨询量 今日/昨日
     * 车牌申诉：总咨询量 今日/昨日
     * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 4.修正车牌处理
     * 折线图：近七日服务类型趋势 车主反馈/订单申诉/车牌申诉 最近7日/数量
     */
    public JSONObject getMainRight2Data() {
        JSONObject jsonObject = new JSONObject();

        //车主反馈：总咨询量 今日/昨日
        QueryWrapper<OperateFeedback> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0);
//        wrapper.eq("CONVERT(varchar, create_time, 23)", new DateTime().toString("yyyy-MM-dd"));
        Long today_count = operateFeedbackService.selectCount(wrapper);
        jsonObject.put("today_feedbackcount", today_count);

        QueryWrapper<OperateFeedback> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_del", 0);
        wrapper2.eq("CONVERT(varchar, create_time, 23)", new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        Long yesterday_count = operateFeedbackService.selectCount(wrapper2);
        jsonObject.put("yesterday_feedbackcount", yesterday_count);

        //订单申诉：总咨询量 今日/昨日
        QueryWrapper<OperateAppeal> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("is_del", 0);
//        wrapper3.eq("CONVERT(varchar, create_time, 23)", new DateTime().toString("yyyy-MM-dd"));
        Long today_appealcount = operateAppealService.selectCount(wrapper3);
        jsonObject.put("today_appealcount", today_appealcount);

        QueryWrapper<OperateAppeal> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("is_del", 0);
        wrapper4.eq("CONVERT(varchar, create_time, 23)", new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        Long yesterday_appealcount = operateAppealService.selectCount(wrapper4);
        jsonObject.put("yesterday_appealcount", yesterday_appealcount);
//
//        //车牌申诉：总咨询量 今日/昨日
//        QueryWrapper<CarnoAppeal> wrapper5 = new QueryWrapper<>();
//        wrapper5.eq("is_del", 0);
////        wrapper5.eq("CONVERT(varchar, create_time, 23)", new DateTime().toString("yyyy-MM-dd"));
//        Long today_carnocount = carnoAppealService.selectCount(wrapper5);
//        jsonObject.put("today_carnocount", today_carnocount);
//
//        QueryWrapper<CarnoAppeal> wrapper6 = new QueryWrapper<>();
//        wrapper6.eq("is_del", 0);
//        wrapper6.eq("CONVERT(varchar, create_time, 23)", new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
//        Long yesterday_carnocount = carnoAppealService.selectCount(wrapper6);
//        jsonObject.put("yesterday_carnocount", yesterday_carnocount);

        //折线图：近七日服务类型趋势 车主反馈 最近7日/数量
        jsonObject.put("operateFeedbackData", operateFeedbackService.getDaysCount());
        //折线图：近七日服务类型趋势 订单申诉 最近7日/数量
        jsonObject.put("operateAppealData", operateAppealService.getDaysCount());
        //折线图：近七日服务类型趋势 车牌申诉 最近7日/数量
//        jsonObject.put("carnoAppealData", carnoAppealService.getDaysCount());

        //订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 5.修正车牌处理
        List<ReportVo> list = appealHandleRecordService.getCountByType();
        jsonObject.put("appealHandleRecordData", list);

//        //订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 5.修正车牌处理
//        ReportVo reportVo = appealHandleRecordService.getTypesCount();
//
//        List<Map<String, String>> retList = new ArrayList<>();
//
//        Map<String, String> map = new HashMap<>();
//        map.put("value", reportVo.getAmount1());
//        map.put("name", "结束时间处理");
//        retList.add(map);
//
//        Map<String, String> map2 = new HashMap<>();
//        map2.put("value", reportVo.getAmount2());
//        map2.put("name", "订单费用处理");
//        retList.add(map2);
//
//        Map<String, String> map3 = new HashMap<>();
//        map3.put("value", reportVo.getAmount3());
//        map3.put("name", "退款处理");
//        retList.add(map3);
//
//        Map<String, String> map4 = new HashMap<>();
//        map4.put("value", reportVo.getAmount5());
//        map4.put("name", "修正车牌处理");
//        retList.add(map4);
//
//        jsonObject.put("appealHandleRecordData", retList);
        return jsonObject;
    }


    //************************数据总览 end************************

    //************************车场停车 start************************


    /**
     * 欠费车辆排名TOP10
     */
    public JSONObject getSumAmountTop10ByCarNo(String type) {
        JSONObject jsonObject = new JSONObject();
        List<ParkingOrderVo> list = parkingOrderService.getSumAmountTop10ByCarNo(type);
        jsonObject.put("list", list);
        return jsonObject;
    }

    /**
     * 停车场/路侧
     * 车位满位率
     * 3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间
     *
     * @param type 类型 0路测 1停车场 2（路测+停车场）总和
     * @param id   路段/停车场id
     * @return
     */
    public JSONObject getCwMwlDatas(String type, Integer id) {
        JSONObject jsonObject = new JSONObject();

        //3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间 【定时任务】
        CwMwlData cwMwlData = new CwMwlData();
        // 类型 0路测 1停车场 2（路测+停车场）总和
        cwMwlData.setType(type);
        cwMwlData.setRoad_park_id(id);// 路段/停车场id
        cwMwlData.setTime(new DateTime().toString("yyyy-MM-dd"));
        List<CwMwlData> today_data = cwMwlDataService.getHourMwlList(cwMwlData);

        cwMwlData.setTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        List<CwMwlData> yesterday_data = cwMwlDataService.getHourMwlList(cwMwlData);

        cwMwlData.setTime(new DateTime().plusDays(-7).toString("yyyy-MM-dd"));
        List<CwMwlData> last_week_data = cwMwlDataService.getHourMwlList(cwMwlData);

        jsonObject.put("today_data", today_data);
        jsonObject.put("yesterday_data", yesterday_data);
        jsonObject.put("last_week_data", last_week_data);
        return jsonObject;
    }

    /**
     * 停车场
     * 路边停车
     * 发票发放率、问题订单率、手动开闸率、免费订单率
     */
    public JSONObject getParkRight3Data(Integer id) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<ParkingOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        if (Base.notEmpty(id)) {
            wrapper.eq("park_id", id);
        }
        Long total_order_count = parkingOrderService.selectCount(wrapper);
        //发票发放率
        RoadParkListVo roadParkListVo = new RoadParkListVo();
        roadParkListVo.setType("1");
        roadParkListVo.setPId(id);//停车场id
        if (total_order_count.toString().equals("0")) {
            jsonObject.put("fpffl", 0);
        } else {
            jsonObject.put("fpffl", new BigDecimal(parkingOrderService.getFpCount(roadParkListVo)).divide(new BigDecimal(total_order_count.toString()), 2, RoundingMode.HALF_UP));
        }


//        //问题订单数量
//        QueryWrapper<ParkingOrder> wrapper2 = new QueryWrapper<>();
//        wrapper2.eq("is_del", GlobalData.ISDEL_NO);
//        wrapper2.isNotNull("artificial_open");
//        Long question_order_count = parkingOrderService.selectCount(wrapper2);

        //手动开闸数量
        QueryWrapper<ParkingOrder> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("is_del", GlobalData.ISDEL_NO);
        wrapper3.isNotNull("artificial_open"); //人工开闸类型 1:公务开闸 2：故障开闸 3：临时通行
        if (Base.notEmpty(id)) {
            wrapper3.eq("park_id", id);
        }
        Long open_order_count = parkingOrderService.selectCount(wrapper3);
        //手动开闸率
        if (total_order_count.toString().equals("0")) {
            jsonObject.put("sdkzl", 0);
        } else {
            jsonObject.put("sdkzl", new BigDecimal(open_order_count.toString()).divide(new BigDecimal(total_order_count.toString()), 2, RoundingMode.HALF_UP));
        }

        //免费订单数量
        QueryWrapper<ParkingOrder> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("is_del", GlobalData.ISDEL_NO);
        wrapper4.isNotNull("free_type");    //免费类型 0：包月 1：白名单 2：免费时段 3：特殊开闸
        if (Base.notEmpty(id)) {
            wrapper3.eq("park_id", id);
        }
        Long free_order_count = parkingOrderService.selectCount(wrapper4);
        //免费订单率
        if (total_order_count.toString().equals("0")) {
            jsonObject.put("mfddl", 0);
        } else {
            jsonObject.put("mfddl", new BigDecimal(free_order_count.toString()).divide(new BigDecimal(total_order_count.toString()), 2, RoundingMode.HALF_UP));
        }

        return jsonObject;
    }

    //************************车场停车 end************************

    //************************停车场停车详情 start************************


    /**
     * 停车场详情
     * 首页
     * setdata方法
     *
     * @param data
     * @param jsonObject
     * @param type       1今日 2昨日
     * @param orderType  0路侧 1停车场
     * @param id         路侧/停车场id
     */
    public void setParkViewIndexData(JSONObject jsonObject, String type, String orderType, Integer id) {
        RoadParkListVo roadParkListVo = new RoadParkListVo();
        //停车区类型 0：路段 1：停车场
        roadParkListVo.setType(orderType);
        //路侧/停车场id
        roadParkListVo.setPId(id);

        //应收金额、已付金额/营收金额、待付金额/欠费金额
        RoadParkListVo vo = new RoadParkListVo();

        //支付订单管理
        PaymentOrderVo paymentOrderVo = new PaymentOrderVo();
        paymentOrderVo.setParkingType(orderType);
        if (Base.notEmpty(orderType) && orderType.equals("1")) {
            paymentOrderVo.setPark_id(id);
        } else {
            paymentOrderVo.setRoadId(id);
        }

        ReportVo paymentOrderList = null;

        //1今日 2昨日
        if (type.equals("1")) {
            paymentOrderVo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
            paymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);

            // 应收金额、待付金额/欠费金额
            roadParkListVo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(roadParkListVo);
            vo.setSum_amount(roadParkListVo2.getSum_amount());//sum_amount 应收金额
            vo.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//unpaid_amount 待付金额/欠费金额
        } else {
            //昨日
            paymentOrderVo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
            paymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);

            // 应收金额、待付金额/欠费金额
            roadParkListVo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(roadParkListVo);
            vo.setSum_amount(roadParkListVo2.getSum_amount());//sum_amount 应收金额
            vo.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//unpaid_amount 待付金额/欠费金额
        }

        //1今日 2昨日
        if (type.equals("1")) {
            //今日
            //今日 总营收 = 金额 - 退款金额
            jsonObject.put("today_moneys", Double.parseDouble(paymentOrderList.getAmount()) - Double.parseDouble(paymentOrderList.getAmount5()));
            //今日 停车收费收入 = 路侧已付金额
            jsonObject.put("today_paidAmount", paymentOrderList.getAmount3());
            //今日 退款金额
            jsonObject.put("today_refund_amount", paymentOrderList.getAmount5());
            //今日 包月金额
            jsonObject.put("today_month_amount", paymentOrderList.getAmount1());

            //今日 欠费金额
            jsonObject.put("today_unpaid_amount", vo.getUnpaid_amount());
        } else {
            //昨日
            //昨日 总营收 = 金额 - 退款金额
            jsonObject.put("yesterday_moneys", Double.parseDouble(paymentOrderList.getAmount()) - Double.parseDouble(paymentOrderList.getAmount5()));
            //昨日 停车收费收入 = 路侧已付金额
            jsonObject.put("yesterday_paidAmount", paymentOrderList.getAmount3());
            //昨日 退款金额
            jsonObject.put("yesterday_refund_amount", paymentOrderList.getAmount5());
            //昨日 包月金额
            jsonObject.put("yesterday_month_amount", paymentOrderList.getAmount1());

            //昨日 欠费金额
            jsonObject.put("yesterday_unpaid_amount", vo.getUnpaid_amount());
        }
    }

//    public void setParkViewIndexData(JSONObject jsonObject, String type, String orderType, Integer id) {
////        RoadParkListVo roadParkListVo = new RoadParkListVo();
////        //停车区类型 0：路段 1：停车场
////        roadParkListVo.setType(orderType);
////        //路侧/停车场id
////        roadParkListVo.setPId(id);
//
//        //支付订单管理
//        PaymentOrderVo paymentOrderVo = new PaymentOrderVo();
//        paymentOrderVo.setParkingType(orderType);
//        if (Base.notEmpty(orderType) && orderType.equals("1")) {
//            paymentOrderVo.setPark_id(id);
//        } else {
//            paymentOrderVo.setRoadId(id);
//        }
//
//        List<String> paymentOrderList = null;
//
////        CommonVo data = new CommonVo();
////        //停车区类型 0：路段 1：停车场
////        data.setParking_type(orderType);
////        //路侧/停车场id
////        data.setParkId(id);
//
////        //退款管理
////        RefundManagementVo refundManagementVo = new RefundManagementVo();
////        refundManagementVo.setPark_type(orderType);
////        refundManagementVo.setRoad_park_id(id);
//
////        //应收金额、已付金额/营收金额、待付金额/欠费金额
////        RoadParkListVo vo = new RoadParkListVo();
//
//        //1今日 2昨日
//        if (type.equals("1")) {
////            data.setStartTime(new DateTime().toString("yyyy-MM-dd"));
////            data.setEndTime(new DateTime().toString("yyyy-MM-dd"));
//
////            refundManagementVo.setTimes(new DateTime().toString("yyyy-MM-dd"));
//
//            //paid_amount 已付金额/营收金额
////            roadParkListVo.setPayTime(new DateTime().toString("yyyy-MM-dd"));
////            RoadParkListVo roadParkListVo1 = parkingOrderService.getSumAmounts(roadParkListVo);
////            vo.setPaid_amount(roadParkListVo1.getPaid_amount());
//
////            // 应收金额、待付金额/欠费金额
////            roadParkListVo.setPayTime("");
////            roadParkListVo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
////            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(roadParkListVo);
////            vo.setSum_amount(roadParkListVo2.getSum_amount());//sum_amount 应收金额
////            vo.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//unpaid_amount 待付金额/欠费金额
//
//            paymentOrderVo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
//            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
//            paymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);
//        } else {
//            //昨日
////            data.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
////            data.setEndTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
//
////            refundManagementVo.setTimes(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
////
////            //paid_amount 已付金额/营收金额
////            roadParkListVo.setPayTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
////            RoadParkListVo roadParkListVo1 = parkingOrderService.getSumAmounts(roadParkListVo);
////            vo.setPaid_amount(roadParkListVo1.getPaid_amount());
////
////            // 应收金额、待付金额/欠费金额
////            roadParkListVo.setPayTime("");
////            roadParkListVo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
////            RoadParkListVo roadParkListVo2 = parkingOrderService.getSumAmounts(roadParkListVo);
////            vo.setSum_amount(roadParkListVo2.getSum_amount());//sum_amount 应收金额
////            vo.setUnpaid_amount(roadParkListVo2.getUnpaid_amount());//unpaid_amount 待付金额/欠费金额
//
//            paymentOrderVo.setStartTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
//            //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
//            paymentOrderList = paymentOrderService.getSumMoneys(paymentOrderVo);
//        }
//
////        //退款金额
////        RefundManagementVo refundManagementVo2 = refundManagementService.sumRefundAmount(refundManagementVo);
//
//        //路测
//        if (orderType.equals("0")) {
//            //路测
//
//            //包月收入
////            String sumCost = monthlyManagementService.getMonthlyPaymentSum(data);
////            vo.setByAmount(sumCost);
////            //总收入 = 已付金额 + 待付金额 +包月收入
////            double today_money = Double.parseDouble(vo.getPaid_amount()) + Double.parseDouble(vo.getUnpaid_amount()) + Double.parseDouble(vo.getByAmount());
//
//            //充值收入
////            String czMoney = rechargeManagementService.getSumCzMoney(data);
//            //1今日 2昨日
//            if (type.equals("1")) {
////                //今日 总营收 = 路侧总收入 + 充值收入
////                jsonObject.put("today_moneys", today_money);
////                //今日 停车收费收入 = 路侧已付金额
////                jsonObject.put("today_paidAmount", vo.getPaid_amount());
////                //今日 退款金额
////                jsonObject.put("today_refund_amount", refundManagementVo2.getRefund_amount());
//
//                //今日 总营收 = 金额 - 退款金额
//                jsonObject.put("today_moneys", Double.parseDouble(paymentOrderList.get(0)) - Double.parseDouble(paymentOrderList.get(5)));
//                //今日 停车收费收入 = 路侧已付金额
//                jsonObject.put("today_paidAmount", paymentOrderList.get(3));
//                //今日 退款金额
//                jsonObject.put("today_refund_amount", paymentOrderList.get(5));
//                //今日 包月金额
//                jsonObject.put("today_month_amount", paymentOrderList.get(1));
//            } else {
//                //昨日
////                //昨日 总营收 = 路侧总收入 + 充值收入
////                jsonObject.put("yesterday_moneys", today_money);
////                //昨日 停车收费收入 = 路侧已付金额
////                jsonObject.put("yesterday_paidAmount", vo.getPaid_amount());
////                //昨日 退款金额
////                jsonObject.put("yesterday_refund_amount", refundManagementVo2.getRefund_amount());
//
//                //昨日 总营收 = 金额 - 退款金额
//                jsonObject.put("yesterday_moneys", Double.parseDouble(paymentOrderList.get(0)) - Double.parseDouble(paymentOrderList.get(5)));
//                //昨日 停车收费收入 = 路侧已付金额
//                jsonObject.put("yesterday_paidAmount", paymentOrderList.get(3));
//                //昨日 退款金额
//                jsonObject.put("yesterday_refund_amount", paymentOrderList.get(5));
//                //今日 包月金额
//                jsonObject.put("yesterday_month_amount", paymentOrderList.get(1));
//            }
//        } else {
//            //停车场
//            //包月收入
////            String sumCost2 = monthlyManagementService.getMonthlyPaymentSum(data);
////            vo.setByAmount(sumCost2);
////            //总收入 = 已付金额 + 待付金额 +包月收入
////            double today_money2 = Double.parseDouble(vo.getPaid_amount()) + Double.parseDouble(vo.getUnpaid_amount()) + Double.parseDouble(vo.getByAmount());
//
//            //充值收入
////            String czMoney = rechargeManagementService.getSumCzMoney(data);
//            //1今日 2昨日
//            if (type.equals("1")) {
////                //今日 总营收 = 停车场总收入 + 充值收入
////                jsonObject.put("today_moneys", today_money2);
////
////                //今日 停车收费收入 = 停车场已付金额
////                jsonObject.put("today_paidAmount", vo.getPaid_amount());
////                //今日 退款金额
////                jsonObject.put("today_refund_amount", refundManagementVo2.getRefund_amount());
////                //今日 包月金额
////                jsonObject.put("today_month_amount", sumCost2);
//
//                //今日 总营收 = 金额 - 退款金额
//                jsonObject.put("today_moneys", Double.parseDouble(paymentOrderList.get(0)) - Double.parseDouble(paymentOrderList.get(5)));
//                //今日 停车收费收入 = 路侧已付金额
//                jsonObject.put("today_paidAmount", paymentOrderList.get(3));
//                //今日 退款金额
//                jsonObject.put("today_refund_amount", paymentOrderList.get(5));
//                //今日 包月金额
//                jsonObject.put("today_month_amount", paymentOrderList.get(1));
//            } else {
//                //昨日
////                //昨日 总营收 = 停车场总收入 + 充值收入
////                jsonObject.put("yesterday_moneys", today_money2);
////
////                //昨日 停车收费收入 = 停车场已付金额
////                jsonObject.put("yesterday_paidAmount", vo.getPaid_amount());
////                //昨日 退款金额
////                jsonObject.put("yesterday_refund_amount", refundManagementVo2.getRefund_amount());
////                //昨日 包月金额
////                jsonObject.put("yesterday_month_amount", sumCost2);
//
//                //昨日 总营收 = 金额 - 退款金额
//                jsonObject.put("yesterday_moneys", Double.parseDouble(paymentOrderList.get(0)) - Double.parseDouble(paymentOrderList.get(5)));
//                //昨日 停车收费收入 = 路侧已付金额
//                jsonObject.put("yesterday_paidAmount", paymentOrderList.get(3));
//                //昨日 退款金额
//                jsonObject.put("yesterday_refund_amount", paymentOrderList.get(5));
//                //今日 包月金额
//                jsonObject.put("yesterday_month_amount", paymentOrderList.get(1));
//            }
//        }
//
//    }

    /**
     * 路侧/停车场详情
     * 今日进出 折线图
     */
    public JSONObject getInOutData(Integer id, String type) {
        JSONObject jsonObject = new JSONObject();
        RoadParkListVo vo = new RoadParkListVo();
        vo.setType(type);
        vo.setPId(id);
        vo.setInOutType("in");
        List<DataChartVo> list = parkingOrderService.getInOutDataByHours(vo);
        vo.setInOutType("out");
        List<DataChartVo> list2 = parkingOrderService.getInOutDataByHours(vo);

        List<String> nameList = new ArrayList<>();
        List<String> inValueList = new ArrayList<>();
        List<String> outValueList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            nameList.add(String.valueOf(i));
            inValueList.add(list.get(i).getAmount());
            outValueList.add(list2.get(i).getAmount());
        }

        jsonObject.put("nameList", nameList);
        jsonObject.put("inValueList", inValueList);
        jsonObject.put("outValueList", outValueList);

        return jsonObject;
    }

    /**
     * 路侧/停车场详情
     * 今日进出记录
     */
    public JSONObject getInOutList(Integer id, String type) {
        JSONObject jsonObject = new JSONObject();
        RoadParkListVo vo = new RoadParkListVo();
        vo.setType(type);
        vo.setPId(id);
        vo.setDriveinTimeStr(new DateTime().toString("yyyy-MM-dd"));
//        vo.setDriveinTimeStr("2023-02-06");
        jsonObject.put("list", parkingOrderService.selectRoadParkList(vo));
        return jsonObject;
    }

    /**
     * 路侧/停车场详情
     * 近30日进出 折线图
     */
    public JSONObject getInOutDaysData(Integer id, String type) {
        JSONObject jsonObject = new JSONObject();
        RoadParkListVo vo = new RoadParkListVo();
        vo.setType(type);
        vo.setPId(id);
        vo.setInOutType("in");
        jsonObject.put("inData", parkingOrderService.getInOutDaysData(vo));
        vo.setInOutType("out");
        jsonObject.put("outData", parkingOrderService.getInOutDaysData(vo));
        return jsonObject;
    }

    /**
     * 路侧/停车场
     * 今日营收 折线图
     * 24小时、金额
     */
    public JSONObject getAmountByHoursData(Integer id, String type) {
        JSONObject jsonObject = new JSONObject();

        PaymentOrderVo vo = new PaymentOrderVo();
        vo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
        vo.setParkingType(type);
        if (Base.notEmpty(type) && type.equals("1")) {
            vo.setPark_id(id);
        } else {
            vo.setRoadId(id);
        }

        //今日0时至当前时间各个小时整点营收金额统计
        List<DataChartVo> list = paymentOrderService.getMoneyDataByHours(vo);

        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        int i = 0;
        for (DataChartVo vo1 : list) {
            nameList.add(String.valueOf(i));
            //金额 - 退款金额
            Double value = Double.parseDouble(vo1.getAmount()) - Double.parseDouble(vo1.getAmount2());
            valueList.add(value.toString());

            i++;
        }
        jsonObject.put("nameList", nameList);
        jsonObject.put("valueList", valueList);
        return jsonObject;
    }

    /**
     * 停车场详情
     * 路侧/停车场欠费分析
     */
    public JSONObject getArrearsData(Integer id, String type) {
        JSONObject jsonObject = new JSONObject();

        //今日欠费金额
        RoadParkListVo vo = new RoadParkListVo();
        vo.setType(type);
        vo.setPId(id);
        vo.setStartTime(new DateTime().toString("yyyy-MM-dd"));
        //应收金额、待付金额/欠费金额
        RoadParkListVo roadParkListVo = parkingOrderService.getSumAmounts(vo);
        jsonObject.put("today_unpaid_amount", roadParkListVo.getUnpaid_amount());

        ParkingOrderVo parkingOrderVo = new ParkingOrderVo();
        parkingOrderVo.setType_name(type);
        parkingOrderVo.setPark_id(id);
        //今日0时至当前时间各个小时整点欠费金额统计
        List<DataChartVo> list = parkingOrderService.getArrearsMoneyDataByHours(parkingOrderVo);

        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        int i = 0;
        for (DataChartVo vo1 : list) {
            nameList.add(String.valueOf(i));
            valueList.add(vo1.getAmount());

            i++;
        }
        jsonObject.put("nameList", nameList);
        jsonObject.put("valueList", valueList);
        return jsonObject;
    }

    //************************停车场停车详情 end************************

    //************************路侧停车详情 start************************

    //************************路侧停车详情 end************************

    /**
     * 车位满位率 = （在停数量/泊位总数）
     * 【整点定时任务】
     */
    public void createCwMwl(String type) {
        // 车位总数（停车场车位总数+路侧泊位总数）、
        String parkNum = parkService.berthSumData("1");//停车场
        Integer placeTotal = Integer.parseInt(parkNum);
        // 类型 0路测 1停车场 2（路测+停车场）总和
        if (type.equals("1")) {
            // 类型 1停车场
            //在停车辆
            ParkingOrderVo vo = new ParkingOrderVo();
            vo.setType_name("1");
            ReportVo reportVo = parkingOrderService.getZtCount(vo);

            BigDecimal decimal = new BigDecimal(0);
            if (!parkNum.toString().equals("0")) {
                //车位满位率 = （在停数量/泊位总数）
                decimal = new BigDecimal(reportVo.getZtCount().toString()).divide(new BigDecimal(parkNum.toString()), 2, BigDecimal.ROUND_HALF_UP);
            }

            //车位满位率
            CwMwlData cwMwlData = new CwMwlData();
            cwMwlData.setTime(new DateTime().toString("yyyy-MM-dd"));
            cwMwlData.setHour(new DateTime().toString("HH"));
            cwMwlData.setHours(new DateTime().getHourOfDay());
            cwMwlData.setZtCount(reportVo.getZtCount());
            cwMwlData.setBerthCount(Integer.parseInt(parkNum));
            cwMwlData.setMwl(decimal.multiply(BigDecimal.valueOf(100)).toString());
            cwMwlData.setType(type);
            cwMwlDataService.addCwMwlData(cwMwlData);

            //在停车辆 各个路侧/停车场
            ParkingOrderVo vo2 = new ParkingOrderVo();
            vo2.setType_name("1");
            List<ReportVo> list2 = parkingOrderService.getZtCountByIds(vo2);

            //各个停车场车位数
            List<Park> parkList = parkService.selectList(new QueryWrapper<Park>().eq("status", "0").eq("is_del", "0"));
            Map<Integer, Integer> parkMap = parkList.stream().collect(Collectors.toMap(Park::getId, Park::getPark_num));

            for (ReportVo reportVo1 : list2) {
                BigDecimal decimal2 = new BigDecimal(0);
                //车位满位率 = （在停数量/泊位总数）
                if (Base.notEmpty(parkMap.get(reportVo1.getPlaceId()))) {
                    if (!parkMap.get(reportVo1.getPlaceId()).toString().equals("0")) {
                        decimal2 = new BigDecimal(reportVo1.getZtCount().toString()).divide(new BigDecimal(parkMap.get(reportVo1.getPlaceId()).toString()), 2, BigDecimal.ROUND_HALF_UP);
                    }
                }

                //车位满位率
                CwMwlData cwMwlData2 = new CwMwlData();
                cwMwlData2.setTime(new DateTime().toString("yyyy-MM-dd"));
                cwMwlData2.setHour(new DateTime().toString("HH"));
                cwMwlData2.setHours(new DateTime().getHourOfDay());
                cwMwlData2.setZtCount(reportVo1.getZtCount());
                cwMwlData2.setBerthCount(parkMap.get(reportVo1.getPlaceId()));
                cwMwlData2.setMwl(decimal2.multiply(BigDecimal.valueOf(100)).toString());
                cwMwlData2.setType(type);
                cwMwlData2.setRoad_park_id(reportVo1.getPlaceId());
                cwMwlDataService.addCwMwlData(cwMwlData2);
            }
        }
    }

    /**
     * 车位周转率
     * 周转率（周转率=路侧+停车场日均总停车数（今日路侧+停车场总订单数）/路侧+停车场总泊位数X100%）
     * 【每天定时任务】
     */
    public void createCwZzl(String type) {
        // 车位总数（停车场车位总数+路侧泊位总数）、
        String parkNum = parkService.berthSumData("1");//停车场
        Integer placeTotal = Integer.parseInt(parkNum);
        // 类型 0路测 1停车场 2（路测+停车场）总和
        if (type.equals("1")) {
            // 类型 1停车场
            //在停车辆
            ParkingOrderVo vo = new ParkingOrderVo();
            vo.setType_name("1");
            vo.setStartDate(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            ReportVo reportVo = parkingOrderService.getZtCount(vo);

            BigDecimal decimal = new BigDecimal(0);
            if (!parkNum.toString().equals("0")) {
                //周转率 总订单数/总泊位数
                decimal = new BigDecimal(reportVo.getCounts().toString()).divide(new BigDecimal(parkNum.toString()), 2, BigDecimal.ROUND_HALF_UP);
            }

            //周转率
            CwZzlData cwZzlData = new CwZzlData();
            cwZzlData.setTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            cwZzlData.setOrdersCount(reportVo.getCounts());
            cwZzlData.setBerthCount(Integer.parseInt(parkNum));
            cwZzlData.setZzl(decimal.multiply(BigDecimal.valueOf(100)).toString());
            cwZzlData.setType(type);
            cwZzlDataService.addCwZzlData(cwZzlData);

            //在停车辆 各个路侧/停车场
            ParkingOrderVo vo2 = new ParkingOrderVo();
            vo2.setType_name("1");
            vo2.setStartDate(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
            List<ReportVo> list2 = parkingOrderService.getZtCountByIds(vo2);

            //各个停车场车位数
            List<Park> parkList = parkService.selectList(new QueryWrapper<Park>().eq("status", "0").eq("is_del", "0"));
            Map<Integer, Integer> parkMap = parkList.stream().collect(Collectors.toMap(Park::getId, Park::getPark_num));

            for (ReportVo reportVo1 : list2) {
                BigDecimal decimal2 = new BigDecimal(0);
                //周转率 总订单数/总泊位数
                if (Base.notEmpty(parkMap.get(reportVo1.getPlaceId()))) {
                    if (!parkMap.get(reportVo1.getPlaceId()).toString().equals("0")) {
                        decimal2 = new BigDecimal(reportVo1.getCounts().toString()).divide(new BigDecimal(parkMap.get(reportVo1.getPlaceId()).toString()), 2, BigDecimal.ROUND_HALF_UP);
                    }
                }

                //周转率
                CwZzlData cwZzlData2 = new CwZzlData();
                cwZzlData2.setTime(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
                cwZzlData2.setOrdersCount(reportVo1.getCounts());
                cwZzlData2.setBerthCount(parkMap.get(reportVo1.getPlaceId()));
                cwZzlData2.setZzl(decimal2.multiply(BigDecimal.valueOf(100)).toString());
                cwZzlData2.setType(type);
                cwZzlData2.setRoad_park_id(reportVo1.getPlaceId());
                cwZzlDataService.addCwZzlData(cwZzlData2);
            }
        }

    }

}
