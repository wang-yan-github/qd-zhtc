package com.jsdc.zhtc.service;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.mapper.ParkingOrderMapper;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.vo.DataChartVo;
import com.jsdc.zhtc.vo.StatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@SuppressWarnings("ALL")
public class SjfxService {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private ParkingOrderMapper parkingOrderMapper;

    /**
     * 各区域在停车辆总数排名TOP5
     */
    public JSONObject getCarTop5Data(String type, String time) {
        JSONObject object = new JSONObject();
        List<String> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        List<Integer> yData2 = new ArrayList<>();
        List<DataChartVo> list = parkingOrderService.getCarTop5Data(type, time);
        int i = 0;
        for (DataChartVo vo : list) {
            xData.add(vo.getName());
            yData.add(Integer.parseInt(vo.getAmount()));
            yData2.add(Integer.parseInt(vo.getAmount()));
        }
        object.put("xData", xData);
        object.put("yData", yData);
        Collections.reverse(yData2);
        object.put("yData2", yData2);
        return object;
    }

    /**
     * 昨日停车时长占比分析
     */
    public List<String> getParkTimeData(String type) {
        //总次数
        String data = parkingOrderService.getParkTimeCountData(type);
        List<String> list = parkingOrderService.getParkTimeData(type);
        List<String> datalist = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (String val : list) {
                if (!data.equals("0") && !val.equals("0")) {
                    BigDecimal divide = new BigDecimal(val).divide(new BigDecimal(data), 2, RoundingMode.HALF_UP);
                    datalist.add(divide.multiply(BigDecimal.valueOf(100)).toString());
                } else {
                    datalist.add(BigDecimal.valueOf(0).toString());
                }
            }
        }
        return datalist;
    }

    /**
     * 近五日车位占用量及趋势
     */
    public List<String> getParticipatingData(String type) {
        return parkingOrderService.getParticipatingData(type);
    }

    /**
     * 各区域收费总额排名TOP5
     */
    public JSONObject getTotalChargesTop5Data(String type, String timeType) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = parkingOrderService.getTotalChargesTop5Data(type, timeType);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        List<String> yData2 = new ArrayList<>();

        for (DataChartVo vo : list) {
            xData.add(vo.getName());
            yData.add(vo.getAmount());
            yData2.add(vo.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        Collections.reverse(yData2);
        jsonObject.put("yData2", yData2);
        return jsonObject;
    }

    /**
     * 今日收费总额趋势分析
     */
    public JSONObject getTotalChargesData(String type) {
        JSONObject jsonObject = new JSONObject();
        //昨日
        List<DataChartVo> yesterdayList = parkingOrderService.getTotalChargesData(type, -1);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo : yesterdayList) {
            xData.add(vo.getName());
            yData.add(vo.getAmount());
        }
        jsonObject.put("xDataYesterday", xData);
        jsonObject.put("yDataYesterday", yData);

        //今日
        List<DataChartVo> todayList = parkingOrderService.getTotalChargesData(type, 0);
        List<String> xDataToday = new ArrayList<>();
        List<String> yDataToday = new ArrayList<>();
        for (DataChartVo vo : todayList) {
            xDataToday.add(vo.getName());
            yDataToday.add(vo.getAmount());
        }
        jsonObject.put("xDataToday", xDataToday);
        jsonObject.put("yDataToday", yDataToday);
        return jsonObject;
    }

    public List<Map<String, Object>> parkPaymentTypeStatistics() {
        List<String> typeNames = new ArrayList<>();
        List<Map<String, Object>> retList = new ArrayList<>();
        typeNames.add("微信");
        typeNames.add("支付宝");
        typeNames.add("钱包");
        typeNames.add("现金");
        List<StatisticsVo> lists = parkingOrderMapper.paymentTypeStatistics();
        Map<String, String> countMap = new HashMap<String, String>();
        if (lists != null && lists.size() > 0)
            countMap = lists.stream().filter(item -> !item.getPayTypeName().equals("其他")).collect(Collectors.toMap(StatisticsVo::getPayTypeName, StatisticsVo::getSumAmount));
        for (String str : typeNames) {
            Map<String, Object> map = new HashMap<>();
            String sumAmount = countMap.get(str);
            map.put("value", StringUtils.isBlank(sumAmount) ? "0.0" : ArithmeticUtils.setScale1(sumAmount, 2, BigDecimal.ROUND_HALF_UP));
            map.put("name", str);
            retList.add(map);
            //typeCounts.add( count==null?"0":count+"" );
        }
        return retList;
    }

    /**
     * 停车场收费排行
     *
     * @return
     */
    public Map<String, List<String>> parkChargeRank() {
        List<Map<String, String>> data = parkingOrderMapper.parkChargeRank();
        List<String> parks = data.stream().map(x -> String.valueOf(x.get("park_name"))).collect(Collectors.toList());
        List<String> amount = data.stream().map(x -> String.valueOf(x.get("amount"))).collect(Collectors.toList());
        Map<String, List<String>> result = new HashMap<>();
        result.put("parks", parks);
        result.put("amount", amount);
        return result;
    }

    /**
     * 停车场在停车辆排名
     *
     * @return
     */
    public Map<String, List<String>> parkParkingCount() {
        List<Map<String, String>> data = parkingOrderMapper.parkParkingCount();
        List<String> parks = data.stream().map(x -> String.valueOf(x.get("park_name"))).collect(Collectors.toList());
        List<String> count = data.stream().map(x -> String.valueOf(x.get("count"))).collect(Collectors.toList());
        Map<String, List<String>> result = new HashMap<>();
        result.put("parks", parks);
        result.put("count", count);
        return result;
    }


}
