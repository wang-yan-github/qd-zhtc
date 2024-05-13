package com.jsdc.jpush.api.excelderive;

import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.vo.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ExcelDeriveModel {

    /**
     * 描 述： TODO(停车场 营收报表添加数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @param data
     * @return
     */
    public static void addSummaryOfRevenue(XSSFWorkbook workbook, HttpServletResponse response, List<ParkRsVo> data, String formatDate, CommonVo commonVo) {

        try (OutputStream out = response.getOutputStream();) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 5;
            //其实编号
            int num = 1;
            row = sheet.getRow(1);
            row.createCell(17).setCellValue(formatDate);

            row.createCell(13).setCellValue(commonVo.getStartTime());
            row.createCell(14).setCellValue(commonVo.getEndTime());
            XSSFCellStyle cellStyle = getCellStyle(workbook);
            if (data != null & data.size() > 0) {
                for (ParkRsVo bean : data) {
                    row = sheet.createRow(trnum);
                    row.setHeight((short) (28 * 20));
                    // (XSSFRow row ,String type ,Integer index, XSSFCellStyle cellStyle , Object data ){
                    // 序号
                    setVal(row, "int", 0, cellStyle, num);
                    // 停车场
                    setVal(row, "String", 1, cellStyle, bean.getParkName());
                    // 月租卡 车辆
                    setVal(row, "int", 2, cellStyle, bean.getYzCarNum() == null ? 0 : bean.getYzCarNum());
                    // 月租卡 营收
                    setVal(row, "String", 3, cellStyle, bean.getYzTotalCost() == null ? "0.0" : bean.getYzTotalCost());
                    // 流动车 车次
//                    setVal(row, "int", 4, cellStyle, bean.getLdCarNum( ) == null ? 0 : bean.getLdCarNum( ));
                    // 流动车 微信
                    setVal(row, "String", 4, cellStyle, bean.getLdWxAmount() == null ? "0.0" : bean.getLdWxAmount());
                    // 流动车 支付宝
                    setVal(row, "String", 5, cellStyle, bean.getLdZfbAmount() == null ? "0.0" : bean.getLdZfbAmount());
                    // 流动车 聚合支付
                    setVal(row, "String", 6, cellStyle, bean.getLdDsfAmount() == null ? "0.0" : bean.getLdDsfAmount());
                    // 流动车 现金
                    setVal(row, "String", 7, cellStyle, bean.getLdXjAmount() == null ? "0.0" : bean.getLdXjAmount());
                    // 流动车 钱包支付
                    setVal(row, "String", 8, cellStyle, bean.getLdQbAmount() == null ? "0.0" : bean.getLdQbAmount());
                    // 流动车营收
                    setVal(row, "String", 9, cellStyle, bean.getLdSumPaidAmount() == null ? "0.0" : bean.getLdSumPaidAmount());
                    // 公务 单数
                    setVal(row, "int", 10, cellStyle, bean.getLdGwOrderCount() == null ? 0 : bean.getLdGwOrderCount());
                    // 公务 费用
                    setVal(row, "String", 11, cellStyle, bean.getLdGwAmount() == null ? "0.0" : bean.getLdGwAmount());
                    // 故障 单数
                    setVal(row, "int", 12, cellStyle, bean.getLdGzOrderCount() == null ? 0 : bean.getLdGzOrderCount());
                    // 故障 费用
                    setVal(row, "String", 13, cellStyle, bean.getLdGzAmount() == null ? "0.0" : bean.getLdGzAmount());
                    // 逃费 单数
                    setVal(row, "int", 14, cellStyle, bean.getTdCounts() == null ? 0 : bean.getTdCounts());
                    // 逃费 费用
                    setVal(row, "String", 15, cellStyle, bean.getTdSumAmount() == null ? "0.0" : bean.getTdSumAmount());
                    //免费 内部车
                    setVal(row, "int", 16, cellStyle, bean.getMfNbCount() == null ? 0 : bean.getMfNbCount());
                    //免费 企业
                    setVal(row, "int", 17, cellStyle, bean.getMfQyCount() == null ? 0 : bean.getMfQyCount());

                    //总计 车次
//                    setVal(row, "int", 18, cellStyle, bean.getZjCarNum( ));
                    //总计 营收
                    setVal(row, "String", 18, cellStyle, bean.getZjAmount());

                    trnum++;
                    num++;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            /*response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode( "营收总览.xlsx", "UTF-8"));*/
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("营收总览.xlsx".getBytes("UTF-8"), "UTF-8"));
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 描 述： TODO( 路测 营收报表添加数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @param data
     * @return
     */
    public static void addSummaryORExcel(XSSFWorkbook workbook, HttpServletResponse response, List<ParkRoadRsVo> data, String formatDate, CommonVo commonVo) {

        try (OutputStream out = response.getOutputStream();) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 5;
            //其实编号
            int num = 1;
            row = sheet.getRow(1);
            if (StringUtils.isEmpty(commonVo.getStartTime()) && StringUtils.isEmpty(commonVo.getEndTime())) {
                row.createCell(7).setCellValue(formatDate);
            }
            row.createCell(9).setCellValue(commonVo.getStartTime());
            row.createCell(10).setCellValue(commonVo.getEndTime());

            XSSFCellStyle cellStyle = getCellStyle(workbook);
            if (data != null & data.size() > 0) {
                for (ParkRoadRsVo bean : data) {
                    row = sheet.createRow(trnum);
                    row.setHeight((short) (28 * 20));
                    // (XSSFRow row ,String type ,Integer index, XSSFCellStyle cellStyle , Object data ){
                    // 序号
                    setVal(row, "int", 0, cellStyle, num);
                    // 路段
                    setVal(row, "String", 1, cellStyle, bean.getRoadName());

                    // 月租车卡 车辆
                    setVal(row, "int", 2, cellStyle, bean.getYzCarNum() == null ? 0 : bean.getYzCarNum());
                    // 月租车卡 营收
                    setVal(row, "String", 3, cellStyle, bean.getYzTotalCost() == null ? "0.0" : bean.getYzTotalCost());

                    // 流动车 车次
//                    setVal(row, "int", 2, cellStyle, bean.getLdOrderNum( ) == null ? 0 : bean.getLdOrderNum( ));

                    // 流动车 微信
                    setVal(row, "String", 4, cellStyle, bean.getLdWxAmount() == null ? "0.0" : bean.getLdWxAmount());
                    // 流动车 支付宝
                    setVal(row, "String", 5, cellStyle, bean.getLdZfbAmount() == null ? "0.0" : bean.getLdZfbAmount());
                    // 流动车 第三方
                    setVal(row, "String", 6, cellStyle, bean.getLdDsfAmount() == null ? "0.0" : bean.getLdDsfAmount());
                    // 流动车 现金
                    setVal(row, "String", 7, cellStyle, bean.getLdXjAmount() == null ? "0.0" : bean.getLdXjAmount());
                    // 流动车 钱包
                    setVal(row, "String", 8, cellStyle, bean.getLdQbAmount() == null ? "0.0" : bean.getLdQbAmount());

                    // 流动车营收
                    setVal(row, "String", 9, cellStyle, bean.getLdSumPaidAmount() == null ? "0.0" : bean.getLdSumPaidAmount());

                    // 逃费 单数
                    setVal(row, "int", 10, cellStyle, bean.getTdCounts() == null ? 0 : bean.getTdCounts());
                    // 逃费 费用
                    setVal(row, "String", 11, cellStyle, bean.getTdSumAmount() == null ? "0.0" : bean.getTdSumAmount());
                    //免费 内部车
                    setVal(row, "int", 12, cellStyle, bean.getMfNbCount() == null ? 0 : bean.getMfNbCount());
                    //免费 企业
                    setVal(row, "int", 13, cellStyle, bean.getMfQyCount() == null ? 0 : bean.getMfQyCount());

                    //总计 车次
//                    setVal(row, "int", 12, cellStyle, bean.getZjCarNum( ));
                    //总计 营收
                    setVal(row, "String", 14, cellStyle, bean.getZjAmount());

                    trnum++;
                    num++;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            /*response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode( "营收总览.xlsx", "UTF-8"));*/
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("路内营收总览.xlsx".getBytes("UTF-8"), "UTF-8"));
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 描 述： TODO( 停车场 营收报表添加数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @param data
     * @return
     */
    public static void MonthCarRentalExcel(XSSFWorkbook workbook, HttpServletResponse response, List<MonthCarRentalDetailVo> data, ShouldBillRankingVo sbrvo, SimpleDateFormat sdf) {

        try (OutputStream out = response.getOutputStream();) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 3;
            //其实编号
            int num = 1;

            XSSFCellStyle cellStyle = getCellStyle(workbook);
            XSSFCellStyle cellStyle2 = getCellStyle2(workbook);
            if (data != null & data.size() > 0) {
                for (MonthCarRentalDetailVo bean : data) {
                    row = sheet.createRow(trnum);
                    row.setHeight((short) (28 * 20));

                    String startTime = "";
                    String endTime = "";
                    String createTime = "";

                    if (StringUtils.isNotNull(bean.getStartTime())) {
                        startTime = sdf.format(bean.getStartTime());
                    }
                    if (StringUtils.isNotNull(bean.getEndTime())) {
                        endTime = sdf.format(bean.getEndTime());
                    }
                    if (StringUtils.isNotNull(bean.getCreateTime())) {
                        createTime = sdf.format(bean.getCreateTime());
                    }

                    // 申请人名称
                    setVal(row, "String", 0, cellStyle, bean.getName());
                    // 申请人电话
                    setVal(row, "String", 1, cellStyle, bean.getPhone());
                    // 车牌号码
                    setVal(row, "String", 2, cellStyle, bean.getCarNo());

                    setVal(row, "String", 3, cellStyle, bean.getParkNames());
                    // 启用日期
                    setVal(row, "String", 4, cellStyle, startTime);
                    // 结束日期
                    setVal(row, "String", 5, cellStyle, endTime);
                    // 申请数量
                    setVal(row, "String", 6, cellStyle, bean.getMonths());
                    // 产品金额
                    setVal(row, "String", 7, cellStyle, bean.getPrice());
                    // 订单金额
                    setVal(row, "String", 8, cellStyle, bean.getTotalCost());
                    // 支付时间
                    setVal(row, "String", 9, cellStyle, createTime);
                    // 企业名称
                    setVal(row, "String", 10, cellStyle, bean.getCompanyName());
                    trnum++;
                    num++;
                }
            } else {
                trnum++;
            }

            row = sheet.createRow(trnum);
            row.setHeight((short) (28 * 15));
            setVal(row, "String", 0, cellStyle, "总计：");
            setVal(row, "String", 1, cellStyle2, "车辆：");
            setVal(row, "String", 2, cellStyle2, sbrvo.getCarCount() + "辆");
            setVal(row, "String", 3, cellStyle2, "金额：");
            setVal(row, "String", 4, cellStyle2, sbrvo.getSumAmount() + "元");

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            /*response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode( "营收总览.xlsx", "UTF-8"));*/
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("月租车明细.xlsx".getBytes("UTF-8"), "ISO8859-1"));

            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 描 述： TODO( 路测 营收报表添加数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @param data
     * @return
     */
    public static void mcrcStatisticsExcel(XSSFWorkbook workbook, HttpServletResponse response, List<Map<String, String>> data, String timesMS) {

        try (OutputStream out = response.getOutputStream();) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 5;
            //其实编号
            int num = 1;

            // 设置日期
            row = sheet.getRow(1);
            row.getCell(1).setCellValue(timesMS);
            Map<String, String> map0 = data.get(0);
            Map<String, String> map1 = data.get(1);


            // 运营中心
            row = sheet.getRow(4);
            // 车辆
            row.getCell(1).setCellValue(map0.get("carNum") == null ? "0" : map0.get("carNum"));
            // 钱包
            row.getCell(2).setCellValue("0.0");
            // 微信
            row.getCell(3).setCellValue(map0.get("2") == null ? "0" : map0.get("2"));
            // 支付宝
            row.getCell(4).setCellValue(map0.get("3") == null ? "0" : map0.get("3"));
            // 第三方
            row.getCell(5).setCellValue("0.0");
            // 现金支付
            row.getCell(6).setCellValue(map0.get("5") == null ? "0" : map0.get("5"));
            // 营收
            row.getCell(7).setCellValue(map0.get("sum") == null ? "0" : map0.get("sum"));

            // 移动端
            row = sheet.getRow(5);
            // 车辆
            row.getCell(1).setCellValue(map1.get("carNum") == null ? "0" : map1.get("carNum"));
            // 钱包
            row.getCell(2).setCellValue("0.0");
            // 微信
            row.getCell(3).setCellValue(map1.get("2") == null ? "0" : map1.get("2"));
            // 支付宝
            row.getCell(4).setCellValue(map1.get("3") == null ? "0" : map1.get("3"));
            // 第三方
            row.getCell(5).setCellValue("0.0");
            // 现金支付
            row.getCell(6).setCellValue(map1.get("5") == null ? "0" : map1.get("5"));
            // 营收
            row.getCell(7).setCellValue(map1.get("sum") == null ? "0" : map1.get("sum"));

            // 总计
            row = sheet.getRow(6);
            // 车次
//            String frequency = StringUtils.isBlank(map0.get("frequency")) ? "0": map0.get("frequency");
//            row.getCell(4).setCellValue(frequency+"车次");
            // 车辆
            String carNumAll = StringUtils.isBlank(map0.get("carNumAll")) ? "0" : map0.get("carNumAll");
            row.getCell(1).setCellValue(carNumAll + "辆");
            // 金额
            String amount = StringUtils.isBlank(map0.get("amount")) ? "0" : map0.get("amount");
            row.getCell(7).setCellValue(amount + "元");


            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            /*response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode( "营收总览.xlsx", "UTF-8"));*/
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("路内月租车卡统计.xlsx".getBytes("UTF-8"), "UTF-8"));
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setVal(XSSFRow row, String type, Integer index, XSSFCellStyle cellStyle, Object data) {
        XSSFCell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        if (data != null) {
            switch (type) {
                case "int":
                    cell.setCellValue((int) data);
                    return;
                case "String":
                    cell.setCellValue((String) data);
                    return;
                case "double":
                    cell.setCellValue((double) data);
                    return;
                case "boolean":
                    cell.setCellValue((boolean) data);
                    return;
                case "date":
                    cell.setCellValue((Date) data);
                    return;
                default:
                    cell.setCellValue((String) data);
                    return;
            }
        }

    }

    public static XSSFCellStyle getCellStyle(XSSFWorkbook workbook) {
        //设置单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        return cellStyle;
    }

    public static XSSFCellStyle getCellStyle2(XSSFWorkbook workbook) {
        //设置单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.index);

        cellStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居右
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        return cellStyle;
    }


    /**
     * 描 述： TODO(停车场概览统计分析 添加数据)
     * 作 者： thr
     */
    public static void addParkingOrderRanking(XSSFWorkbook workbook, HttpServletResponse response, List<ShouldBillRankingVo> data, String formatDate, CommonVo commonVo, ShouldBillRankingVo vo) {
        try (OutputStream out = response.getOutputStream()) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 4;
            //其实编号
            int num = 1;
            row = sheet.getRow(1);
            // 营收合计
            row.createCell(1).setCellValue(vo.getMoney());
            // 月租营收
            row.createCell(5).setCellValue(vo.getByAmount());
            // 临停营收
            row.createCell(7).setCellValue(vo.getPaidAmount());
            // 补缴费用
            row.createCell(9).setCellValue(vo.getBjje());
            // 钱包充值
            row.createCell(11).setCellValue(vo.getCzAmount());
            // 统计日期
            if (StringUtils.isNotEmpty(commonVo.getStartTime()) && StringUtils.isNotEmpty(commonVo.getEndTime())) {
                row.createCell(13).setCellValue(commonVo.getStartTime() + " 至 " + commonVo.getEndTime());
            } else {
                row.createCell(13).setCellValue(formatDate);
            }

            XSSFCellStyle cellStyle = getCellStyle(workbook);
            if (data != null & data.size() > 0) {
                for (ShouldBillRankingVo bean : data) {

                    if (bean.getPaymentOrderList().size() > 0) {
                        int yzcount = 0;
                        int yznum = trnum;
                        for (PaymentOrder paymentOrder : bean.getPaymentOrderList()) {
                            row = sheet.createRow(trnum);
                            row.setHeight((short) (28 * 20));
                            if (yzcount == 0) {
                                // 停车场名称
                                setVal(row, "String", 0, cellStyle, bean.getTitle());
                                // 泊位数
                                setVal(row, "String", 1, cellStyle, bean.getStopcarnum());
                                // 单价
                                setVal(row, "String", 2, cellStyle, paymentOrder.getMonths_unit_price() == null ? "0" : paymentOrder.getMonths_unit_price());
                                // 数量
                                setVal(row, "int", 3, cellStyle, paymentOrder.getMonths() == null ? 0 : paymentOrder.getMonths());
                                // 金额
                                setVal(row, "String", 4, cellStyle, paymentOrder.getAmount() == null ? "0" : paymentOrder.getAmount());
                                // 订单数
                                setVal(row, "String", 5, cellStyle, bean.getCounts());
                                // 非零订单数
                                setVal(row, "String", 6, cellStyle, bean.getCounts2());
                                // 应收金额
                                setVal(row, "String", 7, cellStyle, bean.getSumAmount());
                                // 实收金额
                                setVal(row, "String", 8, cellStyle, bean.getPaidAmount());
                                // 在停金额
                                setVal(row, "String", 9, cellStyle, bean.getUnpaidAmount());
                                // 待缴费
                                setVal(row, "String", 10, cellStyle, bean.getUnpaidAmount2());
                                // 数量
                                setVal(row, "int", 11, cellStyle, bean.getBjcs());
                                // 金额
                                setVal(row, "String", 12, cellStyle, bean.getBjje());
                                // 缴费数
                                setVal(row, "int", 13, cellStyle, bean.getJfs());
                                // 缴费率
                                setVal(row, "String", 14, cellStyle, bean.getJfl());
                                // 周转率
                                setVal(row, "String", 15, cellStyle, bean.getZzl());
                            } else {
                                // 停车场名称
                                setVal(row, "String", 0, cellStyle, "");
                                // 泊位数
                                setVal(row, "String", 1, cellStyle, "");
                                // 单价
                                setVal(row, "String", 2, cellStyle, paymentOrder.getMonths_unit_price() == null ? "0" : paymentOrder.getMonths_unit_price());
                                // 数量
                                setVal(row, "int", 3, cellStyle, paymentOrder.getMonths() == null ? 0 : paymentOrder.getMonths());
                                // 金额
                                setVal(row, "String", 4, cellStyle, paymentOrder.getAmount() == null ? "0" : paymentOrder.getAmount());
                                // 订单数
                                setVal(row, "String", 5, cellStyle, "");
                                // 非零订单数
                                setVal(row, "String", 6, cellStyle, "");
                                // 应收金额
                                setVal(row, "String", 7, cellStyle, "");
                                // 实收金额
                                setVal(row, "String", 8, cellStyle, "");
                                // 在停金额
                                setVal(row, "String", 9, cellStyle, "");
                                // 待缴费
                                setVal(row, "String", 10, cellStyle, "");
                                // 数量
                                setVal(row, "int", 11, cellStyle, null);
                                // 金额
                                setVal(row, "String", 12, cellStyle, "");
                                // 缴费数
                                setVal(row, "int", 13, cellStyle, null);
                                // 缴费率
                                setVal(row, "String", 14, cellStyle, "");
                                // 周转率
                                setVal(row, "String", 15, cellStyle, "");
                            }
                            trnum++;
                            yzcount++;
                        }

                        if (yzcount > 0) {
                            yzcount = yzcount - 1;
                        }
                        // 合并单元格，参数为：起始行号，结束行号，起始列号，结束列号
//                        System.out.println("起始行号" + yznum + "，结束行号" + (yznum + yzcount));
                        for (int i = 0; i < 16; i++) {
                            if (i < 2 || i > 4) {
                                if (yznum < (yznum + yzcount)) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(yznum, yznum + yzcount, i, i);
                                    sheet.addMergedRegion(cellRangeAddress);
                                }
                            }
                        }
                    } else {
                        row = sheet.createRow(trnum);
                        row.setHeight((short) (28 * 20));

                        // 停车场名称
                        setVal(row, "String", 0, cellStyle, bean.getTitle());
                        // 泊位数
                        setVal(row, "String", 1, cellStyle, bean.getStopcarnum());
                        // 单价
                        setVal(row, "String", 2, cellStyle, "0");
                        // 数量
                        setVal(row, "int", 3, cellStyle, 0);
                        // 金额
                        setVal(row, "String", 4, cellStyle, "0");
                        // 订单数
                        setVal(row, "String", 5, cellStyle, bean.getCounts());
                        // 非零订单数
                        setVal(row, "String", 6, cellStyle, bean.getCounts2());
                        // 应收金额
                        setVal(row, "String", 7, cellStyle, bean.getSumAmount());
                        // 实收金额
                        setVal(row, "String", 8, cellStyle, bean.getPaidAmount());
                        // 在停金额
                        setVal(row, "String", 9, cellStyle, bean.getUnpaidAmount());
                        // 待缴费
                        setVal(row, "String", 10, cellStyle, bean.getUnpaidAmount2());
                        // 数量
                        setVal(row, "int", 11, cellStyle, bean.getBjcs());
                        // 金额
                        setVal(row, "String", 12, cellStyle, bean.getBjje());
                        // 缴费数
                        setVal(row, "int", 13, cellStyle, bean.getJfs());
                        // 缴费率
                        setVal(row, "String", 14, cellStyle, bean.getJfl());
                        // 周转率
                        setVal(row, "String", 15, cellStyle, bean.getZzl());
                        trnum++;
                    }
                    num++;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("停车场概览.xlsx".getBytes("UTF-8"), "UTF-8"));
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 描 述： TODO(路内概览统计分析 添加数据)
     * 作 者： thr
     */
    public static void addRoadParkingOrderRanking(XSSFWorkbook workbook, HttpServletResponse response, List<ShouldBillRankingVo> data, String formatDate, CommonVo commonVo, ShouldBillRankingVo vo) {
        try (OutputStream out = response.getOutputStream()) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            //起始行数
            int trnum = 4;
            //其实编号
            int num = 1;
            row = sheet.getRow(1);
            // 营收合计
            row.createCell(1).setCellValue(vo.getMoney());
            // 月租营收
            row.createCell(5).setCellValue(vo.getByAmount());
            // 临停营收
            row.createCell(7).setCellValue(vo.getPaidAmount());
            // 补缴费用
            row.createCell(9).setCellValue(vo.getBjje());
            // 钱包充值
            row.createCell(11).setCellValue("");
            // 统计日期
            if (StringUtils.isNotEmpty(commonVo.getStartTime()) && StringUtils.isNotEmpty(commonVo.getEndTime())) {
                row.createCell(13).setCellValue(commonVo.getStartTime() + " 至 " + commonVo.getEndTime());
            } else {
                row.createCell(13).setCellValue(formatDate);
            }

            XSSFCellStyle cellStyle = getCellStyle(workbook);
            if (data != null & data.size() > 0) {
                for (ShouldBillRankingVo bean : data) {

                    if (bean.getPaymentOrderList().size() > 0) {
                        int yzcount = 0;
                        int yznum = trnum;
                        for (PaymentOrder paymentOrder : bean.getPaymentOrderList()) {
                            row = sheet.createRow(trnum);
                            row.setHeight((short) (28 * 20));
                            if (yzcount == 0) {
                                // 路内名称
                                setVal(row, "String", 0, cellStyle, bean.getTitle());
                                // 泊位数
                                setVal(row, "String", 1, cellStyle, bean.getStopcarnum());
                                // 单价
                                setVal(row, "String", 2, cellStyle, paymentOrder.getMonths_unit_price() == null ? "0" : paymentOrder.getMonths_unit_price());
                                // 数量
                                setVal(row, "int", 3, cellStyle, paymentOrder.getMonths() == null ? 0 : paymentOrder.getMonths());
                                // 金额
                                setVal(row, "String", 4, cellStyle, paymentOrder.getAmount() == null ? "0" : paymentOrder.getAmount());
                                // 订单数
                                setVal(row, "String", 5, cellStyle, bean.getCounts());
                                // 非零订单数
                                setVal(row, "String", 6, cellStyle, bean.getCounts2());
                                // 应收金额
                                setVal(row, "String", 7, cellStyle, bean.getSumAmount());
                                // 实收金额
                                setVal(row, "String", 8, cellStyle, bean.getPaidAmount());
                                // 在停金额
                                setVal(row, "String", 9, cellStyle, bean.getUnpaidAmount());
                                // 待缴费
                                setVal(row, "String", 10, cellStyle, bean.getUnpaidAmount2());
                                // 数量
                                setVal(row, "int", 11, cellStyle, bean.getBjcs());
                                // 金额
                                setVal(row, "String", 12, cellStyle, bean.getBjje());
                                // 缴费数
                                setVal(row, "int", 13, cellStyle, bean.getJfs());
                                // 缴费率
                                setVal(row, "String", 14, cellStyle, bean.getJfl());
                                // 周转率
                                setVal(row, "String", 15, cellStyle, bean.getZzl());
                            } else {
                                // 路内名称
                                setVal(row, "String", 0, cellStyle, "");
                                // 泊位数
                                setVal(row, "String", 1, cellStyle, "");
                                // 单价
                                setVal(row, "String", 2, cellStyle, paymentOrder.getMonths_unit_price() == null ? "0" : paymentOrder.getMonths_unit_price());
                                // 数量
                                setVal(row, "int", 3, cellStyle, paymentOrder.getMonths() == null ? 0 : paymentOrder.getMonths());
                                // 金额
                                setVal(row, "String", 4, cellStyle, paymentOrder.getAmount() == null ? "0" : paymentOrder.getAmount());
                                // 订单数
                                setVal(row, "String", 5, cellStyle, "");
                                // 非零订单数
                                setVal(row, "String", 6, cellStyle, "");
                                // 应收金额
                                setVal(row, "String", 7, cellStyle, "");
                                // 实收金额
                                setVal(row, "String", 8, cellStyle, "");
                                // 在停金额
                                setVal(row, "String", 9, cellStyle, "");
                                // 待缴费
                                setVal(row, "String", 10, cellStyle, "");
                                // 数量
                                setVal(row, "int", 11, cellStyle, null);
                                // 金额
                                setVal(row, "String", 12, cellStyle, "");
                                // 缴费数
                                setVal(row, "int", 13, cellStyle, null);
                                // 缴费率
                                setVal(row, "String", 14, cellStyle, "");
                                // 周转率
                                setVal(row, "String", 15, cellStyle, "");
                            }
                            trnum++;
                            yzcount++;
                        }

                        if (yzcount > 0) {
                            yzcount = yzcount - 1;
                        }
                        // 合并单元格，参数为：起始行号，结束行号，起始列号，结束列号
//                        System.out.println("起始行号" + yznum + "，结束行号" + (yznum + yzcount));
                        for (int i = 0; i < 16; i++) {
                            if (i < 2 || i > 4) {
                                if (yznum < (yznum + yzcount)) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(yznum, yznum + yzcount, i, i);
                                    sheet.addMergedRegion(cellRangeAddress);
                                }
                            }
                        }
                    } else {
                        row = sheet.createRow(trnum);
                        row.setHeight((short) (28 * 20));

                        // 路内名称
                        setVal(row, "String", 0, cellStyle, bean.getTitle());
                        // 泊位数
                        setVal(row, "String", 1, cellStyle, bean.getStopcarnum());
                        // 单价
                        setVal(row, "String", 2, cellStyle, "0");
                        // 数量
                        setVal(row, "int", 3, cellStyle, 0);
                        // 金额
                        setVal(row, "String", 4, cellStyle, "0");
                        // 订单数
                        setVal(row, "String", 5, cellStyle, bean.getCounts());
                        // 非零订单数
                        setVal(row, "String", 6, cellStyle, bean.getCounts2());
                        // 应收金额
                        setVal(row, "String", 7, cellStyle, bean.getSumAmount());
                        // 实收金额
                        setVal(row, "String", 8, cellStyle, bean.getPaidAmount());
                        // 在停金额
                        setVal(row, "String", 9, cellStyle, bean.getUnpaidAmount());
                        // 待缴费
                        setVal(row, "String", 10, cellStyle, bean.getUnpaidAmount2());
                        // 数量
                        setVal(row, "int", 11, cellStyle, bean.getBjcs());
                        // 金额
                        setVal(row, "String", 12, cellStyle, bean.getBjje());
                        // 缴费数
                        setVal(row, "int", 13, cellStyle, bean.getJfs());
                        // 缴费率
                        setVal(row, "String", 14, cellStyle, bean.getJfl());
                        // 周转率
                        setVal(row, "String", 15, cellStyle, bean.getZzl());
                        trnum++;
                    }
                    num++;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" +
                    new String("路内概览.xlsx".getBytes("UTF-8"), "UTF-8"));
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
