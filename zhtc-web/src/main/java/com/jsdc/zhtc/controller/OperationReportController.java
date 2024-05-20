package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.jpush.api.excelderive.ExcelDeriveModel;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.vo.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类 名: 运营报表
 * 描 述: OperationReportController
 * 作 者: lw
 * 创 建：2022/1/29 15:31
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Controller
@RequestMapping("/operationreport")
public class OperationReportController {

    @Autowired
    private ParkingOrderService parkingOrderService;


    /**
     * 描 述： TODO(查询营收总览数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return ResultInfo
     */
    @RequestMapping(value = "/getSummaryOfRevenue", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getSummaryOfRevenue(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(new Date());
        data.setStr(formatDate);
        PageInfo<ParkRoadRsVo> page = parkingOrderService.getSummaryOfRevenuePage(data);
        return ResultInfo.success(page);
    }

    /**
     * 描 述： TODO(导出营收总览excel数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/summaryOfRevenueExcel", method = RequestMethod.POST)
    @ResponseBody
    public void summaryOfRevenueExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVo data) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = sdf.format(new Date());
        data.setStr(formatDate);
        //获取数据
        List<ParkRsVo> list = parkingOrderService.getSummaryOfRevenue(data);
        String path = ExcelDeriveModel.class.getResource("/masterplate/tccystj.xlsx").getPath();
        FileInputStream tps = new FileInputStream(new File(path));
        XSSFWorkbook tpWorkbook = new XSSFWorkbook(tps);
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook = tpWorkbook;

        //填写数据
        ExcelDeriveModel.addSummaryOfRevenue(workbook, response, list, formatDate, data);
    }

    /**
     * 描 述： TODO(月租车详情)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/getMonthCarRentalDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getMonthCarRentalDetail(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        return ResultInfo.success();
    }

    /**
     * 描 述： TODO(月租车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/getMonthCarRentalStatistics", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getMonthCarRentalStatistics(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        //获取数据
        return ResultInfo.success();
    }

    /**
     * 描 述： TODO(月租车excel)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/MonthCarRentalExcel", method = RequestMethod.POST)
    @ResponseBody
    public void MonthCarRentalExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVo data) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        data.setStr(sdf.format(new Date()));
////        data.setStr("2022-02-10");
//        List<MonthCarRentalDetailVo> list = monthlyManagementService.getMonthCarRentalDetail(data);
//        ShouldBillRankingVo sbrvo = monthlyManagementService.getMonthCarRentalStatistics(data);
//
//        String path = ExcelDeriveModel.class.getResource("/masterplate/yzcDetail.xlsx").getPath();
//        FileInputStream tps = new FileInputStream(new File(path));
//        XSSFWorkbook tpWorkbook = new XSSFWorkbook(tps);
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        workbook = tpWorkbook;
//        //填写数据
//        ExcelDeriveModel.MonthCarRentalExcel(workbook, response, list, sbrvo, sdf);
    }


    /**
     * 描 述： TODO(内部车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/toInnerList.do", method = RequestMethod.POST)
    @ResponseBody
    // public ResultInfo toInnerList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, ParkingOrderIDetailsVo podVo) {
    public ResultInfo toInnerList(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));

        PageInfo<ParkingOrderIDetailsVo> page = parkingOrderService.toInnerList(data);
        return ResultInfo.success(page);
    }

    /**
     * 描 述： TODO(企业（税免）车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/toFreeEnterpriseList.do", method = RequestMethod.POST)
    @ResponseBody
    //public ResultInfo toFreeEnterpriseList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, ParkingOrderIDetailsVo podVo) {
    public ResultInfo toFreeEnterpriseList(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));

        PageInfo<ParkingOrderIDetailsVo> page = parkingOrderService.toFreeEnterpriseList(data);
        return ResultInfo.success(page);
    }

    /**
     * 描 述： TODO(内部车 免费车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @RequestMapping(value = "/getIFLQStatistics", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toInnerFreeListQyStatistics(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        Map<String, ShouldBillRankingVo> retMap = parkingOrderService.toInnerFreeListQyStatistics(data);
        return ResultInfo.success(retMap);
    }


    /**
     * 描 述： TODO(内部车导出excel)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportExcelNm.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportExcelNm(HttpServletResponse response, @RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        parkingOrderService.exportExcelNm(data, response);
    }

    /**
     * 人工抬杆/车辆总数
     *
     * @return
     * @author wh
     */
    @RequestMapping(value = "/getOpenGateCarNums.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getOpenGateCarNums(Integer park_id, String startTime, String endTime) {

        return parkingOrderService.getOpenGateCarNums(park_id, startTime, endTime);
    }

    /**
     * 人工抬杆/车辆订单欠费总额
     *
     * @return
     * @author wh
     */
    @RequestMapping(value = "/getOpenGateOrderArrears.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getOpenGateOrderArrears(Integer park_id, String startTime, String endTime) {

        return parkingOrderService.getOpenGateOrderArrears(park_id, startTime, endTime);
    }

    /**
     * 包月用户管理 导出
     *
     * @param isTheCompany
     * @param parkingType
     * @return
     */
    @RequestMapping(value = "/byExport", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo byExport(String isTheCompany, String parkingType) {
//        String name = monthlyManagementService.byExport(isTheCompany, parkingType);
        return ResultInfo.success();
    }


}
