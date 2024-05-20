package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.jpush.api.excelderive.ExcelDeriveModel;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.ShouldBillRankingVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类 名: 停车场平台概览
 * 描 述: ParkingPlatformSurveyController
 * 作 者: lw
 * 创 建：2022/1/24 17:32
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("parkingplatformsurvey")
public class ParkingPlatformSurveyController {

    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 描 述： TODO(查询包月统计、包月费用合计、流动车订单统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Integer}
     */
    @PostMapping("getParkingStatistics")
    public ResultInfo getParkingStatistics(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = sdf.format(date);
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();


//        //车辆包月数
//        Integer mpCount = mmService.getMonthlyPaymentCount(GlobalData.PARKING_TYPE_PLAT, dateStr, park_id);
//        //车辆包月总收入
//        String sumCost = mmService.getMonthlyPaymentSumCost(GlobalData.PARKING_TYPE_PLAT, dateStr, park_id);
        //流动订单数
        Integer soCount = parkingOrderService.getStreamingOrderCount(dateStr, park_id);

        //获取当天收入总额
        String toDaySumAmount = parkingOrderService.getStreamingOrderSumAmount(dateStr, park_id);
        dateStr = sdf.format(ArithmeticUtils.subtractDays(date, 1));
        //获取前一天收入总额
        String yesterdaySumAmount = parkingOrderService.getStreamingOrderSumAmount(dateStr, park_id);
        // 解决java.lang.ArithmeticException: / by zero 被除数为整型时不可为零
        yesterdaySumAmount = yesterdaySumAmount.equals("0.00") ? "1" : yesterdaySumAmount;
        //盘算计算收入升降比例
        String bl = ArithmeticUtils.getBl(toDaySumAmount, yesterdaySumAmount);

        HashMap<String, Object> map = new HashMap<>();
//        map.put("mpCount", mpCount == null ? 0 : mpCount);
//        map.put("sumCost", sumCost);
        map.put("soCount", soCount);
        map.put("sumAmount", ArithmeticUtils.setScale1(toDaySumAmount, 2, BigDecimal.ROUND_HALF_UP).toString());
        map.put("bl", bl);

        return ResultInfo.success(map);
    }

    /**
     * 描 述： 停车场概览统计分析
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Integer}
     */
    @PostMapping("getParkingOrderRanking")
    public ResultInfo getParkingOrderRanking(@RequestBody CommonVo data) {
        JSONObject jsonObject = new JSONObject();
        ShouldBillRankingVo vo = new ShouldBillRankingVo();
        //停车订单数排名
        PageHelper.startPage(data.getPageNum(), data.getPageSize());
        List<ShouldBillRankingVo> parkingOrderRanking = parkingOrderService.getParkingOrderRanking(data);

        CommonVo data2 = new CommonVo();
        BeanUtils.copyProperties(data, data2);
        for (ShouldBillRankingVo rankingVo : parkingOrderRanking) {
            data2.setParkId(rankingVo.getId());
//            rankingVo.setPaymentOrderList(mmService.getMonthlyAmountGroup(data2));
        }

        //停车订单数排名
        PageInfo<ShouldBillRankingVo> page = new PageInfo<>(parkingOrderRanking);

        //临停收入、临停欠费金额
        ShouldBillRankingVo shouldBillRankingVo = parkingOrderService.getSumLtsr(data);
        vo.setSumAmount(shouldBillRankingVo.getSumAmount());
        vo.setPaidAmount(shouldBillRankingVo.getPaidAmount());
        vo.setUnpaidAmount(shouldBillRankingVo.getUnpaidAmount());

        //停车区类型 0：路段 1：停车场 2:场站
        data.setParking_type("1");
//        //包月收入
//        String sumCost = mmService.getMonthlyPaymentSum(data);
//        vo.setByAmount(sumCost);
//        //充值收入
//        String czMoney = rechargeManagementService.getSumCzMoney(data);
//        vo.setCzAmount(czMoney);

        //营收合计
//        double money = Double.parseDouble(vo.getPaidAmount()) + Double.parseDouble(vo.getByAmount()) + Double.parseDouble(vo.getCzAmount());
        double money = Double.parseDouble(vo.getPaidAmount()) + Double.parseDouble(vo.getByAmount());
        vo.setMoney(Double.toString(money));

        //补缴金额
        vo.setBjje(parkingOrderService.getParkingOrderBjje(data));

        jsonObject.put("data", page);
        jsonObject.put("vo", vo);
        return ResultInfo.success(jsonObject);
    }

    /**
     * 停车场概览统计分析 导出
     */
    @RequestMapping(value = "exportParkingOrderRanking", method = RequestMethod.POST)
    public void exportParkingOrderRanking(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVo data) throws Exception {
//        parkingOrderService.export(data, response);

        ShouldBillRankingVo vo = new ShouldBillRankingVo();
        //停车订单数排名
        List<ShouldBillRankingVo> parkingOrderRanking = parkingOrderService.getParkingOrderRanking(data);

        CommonVo data2 = new CommonVo();
        BeanUtils.copyProperties(data, data2);
        for (ShouldBillRankingVo rankingVo : parkingOrderRanking) {
            data2.setParkId(rankingVo.getId());
//            rankingVo.setPaymentOrderList(mmService.getMonthlyAmountGroup(data2));
        }

        //临停收入、临停欠费金额
        ShouldBillRankingVo shouldBillRankingVo = parkingOrderService.getSumLtsr(data);
        vo.setSumAmount(shouldBillRankingVo.getSumAmount());
        vo.setPaidAmount(shouldBillRankingVo.getPaidAmount());
        vo.setUnpaidAmount(shouldBillRankingVo.getUnpaidAmount());

        //停车区类型 0：路段 1：停车场 2:场站
        data.setParking_type("1");
        //包月收入
//        String sumCost = mmService.getMonthlyPaymentSum(data);
//        vo.setByAmount(sumCost);
        //充值收入
//        String czMoney = rechargeManagementService.getSumCzMoney(data);
//        vo.setCzAmount(czMoney);

        //营收合计
//        double money = Double.parseDouble(vo.getPaidAmount()) + Double.parseDouble(vo.getByAmount()) + Double.parseDouble(vo.getCzAmount());
        double money = Double.parseDouble(vo.getPaidAmount()) + Double.parseDouble(vo.getByAmount());
        vo.setMoney(Double.toString(money));

        //补缴金额
        vo.setBjje(parkingOrderService.getParkingOrderBjje(data));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(new Date());
        data.setStr(formatDate);
        String path = ExcelDeriveModel.class.getResource("/masterplate/tccgltj.xlsx").getPath();
        FileInputStream tps = new FileInputStream(new File(path));
        XSSFWorkbook tpWorkbook = new XSSFWorkbook(tps);
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook = tpWorkbook;

        //填写数据
        ExcelDeriveModel.addParkingOrderRanking(workbook, response, parkingOrderRanking, formatDate, data, vo);
    }

    /**
     * 描 述： TODO(近十天包月趋势)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Map< String, Object>}
     */
    @PostMapping("tenDaysMonthlyTrend")
    public ResultInfo tenDaysMonthlyTrend(@RequestBody CommonVo data) {

        Map<String, Object> retMap = new HashMap<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> monthlyCount = new ArrayList<>();
        ArrayList<String> monthlyIncome = new ArrayList<>();
        retMap.put("dates", dates);
        retMap.put("monthlyCount", monthlyCount);
        retMap.put("monthlyIncome", monthlyIncome);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (Base.empty(park_id)) {
            park_id = data.getParkId();
        }

        for (int i = 9; i >= 0; i--) {
            Date date = ArithmeticUtils.subtractDays(new Date(), i);
            //日期
            dates.add(sdf2.format(date));
            String dateStr = sdf.format(date);
//            //车辆包月数
//            Integer mpCount = mmService.getMonthlyPaymentCount(GlobalData.PARKING_TYPE_PLAT, dateStr, park_id);
//            monthlyCount.add(mpCount.toString());
//            //车辆包月总收入
//            String sumCost = mmService.getMonthlyPaymentSumCost(GlobalData.PARKING_TYPE_PLAT, dateStr, park_id);
//            monthlyIncome.add(sumCost);
        }
        return ResultInfo.success(retMap);

    }

    /**
     * 描 述： TODO(流动车近十天趋势)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Map< String, Object>}
     */
    @PostMapping("tenDaysCurrentOrderTrends")
    public ResultInfo tenDaysCurrentOrderTrends(@RequestBody CommonVo data) {

        Map<String, Object> retMap = new HashMap<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> orderCount = new ArrayList<>();
        ArrayList<String> receivable = new ArrayList<>();
        ArrayList<String> actual = new ArrayList<>();
        retMap.put("dates", dates);
        retMap.put("orderCount", orderCount);
        retMap.put("receivable", receivable);
        retMap.put("actual", actual);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");

        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (Base.empty(park_id)) {
            park_id = data.getParkId();
        }

        for (int i = 9; i >= 0; i--) {
            Date date = ArithmeticUtils.subtractDays(new Date(), i);
            //日期
            dates.add(sdf2.format(date));
            String dateStr = sdf.format(date);
            //订单总数
            Integer orders = parkingOrderService.getStreamingOrderCount(dateStr, park_id);
            orderCount.add(orders.toString());
            //应收金额
            String sumReceivable = parkingOrderService.getStreamingOrderReceivable(dateStr, park_id);
            receivable.add(sumReceivable);
            //获取收入总额
            String sumAmount = parkingOrderService.getStreamingOrderSumAmount(dateStr, park_id);
            actual.add(sumAmount);

        }
        return ResultInfo.success(retMap);
    }

    /**
     * 停车订单列表
     */
    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getOrderList(@RequestBody CommonVo data) {
        return ResultInfo.success(parkingOrderService.getParkingOrderData(data));
    }

    /**
     * 包月收入明细
     */
    @RequestMapping(value = "getMonthlyPaymentData", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getMonthlyPaymentData(@RequestBody CommonVo data) {
        //停车场
//        return mmService.getMonthlyPaymentData(data);
        return ResultInfo.success();
    }

    /**
     * 停车场概览统计分析
     * 包月收入明细导出
     */
    @RequestMapping(value = "exportBy", method = RequestMethod.POST)
    public void exportBy(HttpServletResponse response, @RequestBody CommonVo data) throws Exception {
//        mmService.exportExcel(data, response);
    }
}
