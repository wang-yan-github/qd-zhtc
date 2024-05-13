package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkingOrderDao;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingOrderMapper extends BaseMapper<ParkingOrder> {
    @SelectProvider(method = "getSumByArea", type = ParkingOrderDao.class)
    List<Map<String, String>> getSumByArea(String areaId, String startTime, String endTime);

    @SelectProvider(method = "getSumTimeByPark", type = ParkingOrderDao.class)
    Integer getSumTimeByPark(Integer parkId, String startTime, String endTime);


    //分页
    @SelectProvider(type = ParkingOrderDao.class, method = "selectPageList")
    List<ParkingOrderVo> selectPageList(RoadOrParkingCommentVo bean);

    /**
     * 现金核销
     * 分页查询
     * thr
     *
     * @param bean
     * @return
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "selectHxPageList")
    List<ParkingOrderVo> selectHxPageList(ParkingOrderVo bean);

    /**
     * 现金核销
     * 总金额
     * thr
     *
     * @param bean
     * @return
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "getSumMoney")
    Integer getSumMoney(ParkingOrderVo bean);

    /**
     * 现金核销
     * 核销
     * thr
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "updHx")
    void updHx(ParkingOrderVo bean);

    //应收款
    @SelectProvider(type = ParkingOrderDao.class, method = "getShouldCharge")
    String getShouldCharge(CommonVo data);

    //实收款
    @SelectProvider(type = ParkingOrderDao.class, method = "getActualCharge")
    String getActualCharge(CommonVo data);

    //订单统计基础数据
    @SelectProvider(method = "getParkingOrderBasicData", type = ParkingOrderDao.class)
    List<OrderBasicDataVo> getParkingOrderBasicData(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime);

    //订单统计订单增长
    @SelectProvider(method = "getParkingOrderGrowth", type = ParkingOrderDao.class)
    List<OrderGrowthVo> getParkingOrderGrowth(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime);

    //近八天订单增长分析
    @SelectProvider(method = "getParkingNearly8DaysOrderGrowth", type = ParkingOrderDao.class)
    List<OrderGrowthVo> getParkingNearly8DaysOrderGrowth(Integer area_id, Integer street_id, Integer road_id, String user_type);

    //订单统计订单状态占比
    @SelectProvider(method = "getParkingOrderStatus", type = ParkingOrderDao.class)
    List<OrderStatusVo> getParkingOrderStatus(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime);

    //资金统计收入报表
    @SelectProvider(method = "getParkingOrderIncome", type = ParkingOrderDao.class)
    List<OrderIncomeVo> getParkingOrderIncome(Integer area_id, Integer street_id, Integer park_id, String startTime, String endTime);

    //资金统计充值增长
    @SelectProvider(method = "getParkingRechargeGrowth", type = ParkingOrderDao.class)
    List<OrderRechargeGrowthVo> getParkingRechargeGrowth(String timeType, String startTime, String endTime);

    //资金统计缴费方式统计
    @SelectProvider(method = "getParkingPaymentMethod", type = ParkingOrderDao.class)
    List<OrderPaymentMethodVo> getParkingPaymentMethod(String startTime, String endTime);

    //资金统计免单统计
    @SelectProvider(method = "getParkingFree", type = ParkingOrderDao.class)
    List<OrderFreeVo> getParkingFree(String startTime, String endTime);

    //开票统计
    @SelectProvider(method = "getParkingInvoicing", type = ParkingOrderDao.class)
    List<InvoicingVo> getParkingInvoicing(String phone, Integer area_id, String invoice_type, String startTime, String endTime);

    //大额欠费
    @SelectProvider(method = "getParkingLargeArrears", type = ParkingOrderDao.class)
    List<LargeArrearsVo> getParkingLargeArrears(String money, Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime);

    //大额欠费欠费订单
    @SelectProvider(method = "getParkingLargeArrearsOrders", type = ParkingOrderDao.class)
    List<ArrearsOrderVo> getParkingLargeArrearsOrders(Integer carno_id, String money, Integer areaId, Integer streetId, Integer roadId, String startTime, String endTime);

    @SelectProvider(method = "getParkingOrder", type = ParkingOrderDao.class)
    List<ParkingOrder> getParkingOrder(CommonVo data);

    @SelectProvider(method = "getUnpaidOrder", type = ParkingOrderDao.class)
    List<UnpaidOrderVo> getUnpaidOrder(CommonVo data);

    //微信小程序路段和停车场订单查询
    @SelectProvider(type = ParkingOrderDao.class, method = "selectWXPage")
    List<RoadParkListVo> selectWXPage(RoadParkListVo bean);

    @SelectProvider(method = "getMergeOrder", type = ParkingOrderDao.class)
    List<MergeOrderVo> getMergeOrder(Integer orderId);

    @SelectProvider(type = ParkingOrderDao.class, method = "getUnGrantInvoice")
    List<ParkingOrder> getUnGrantInvoice(String parkids);

    @SelectProvider(type = ParkingOrderDao.class, method = "getOrderByParkAndCarNo")
    List<ParkingOrderVo> getOrderByParkAndCarNo(Integer parkId, Integer carType, String carNo);

    /**
     * 微信小程序路段和停车场订单查询
     * 待开发票数据
     * 分页查询
     *
     * @author thr
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "selectFpPage")
    List<RoadParkListVo> selectFpPage(RoadParkListVo bean);

    /**
     * 微信小程序路段订单ids
     * 待开发票数据
     * 停车场、路段标识（0.路段1停车场）
     *
     * @author thr
     */
    @SelectProvider(method = "getRoadParkOrderIds", type = ParkingOrderDao.class)
    RoadParkListVo getRoadParkOrderIds(RoadParkListVo bean);

    /**
     * 微信小程序停车场订单ids
     * 待开发票数据
     * 停车场、路段标识（0.路段1停车场）
     *
     * @author thr
     */
    @SelectProvider(method = "getParkOrderIds", type = ParkingOrderDao.class)
    RoadParkListVo getParkOrderIds(RoadParkListVo bean);

    @SelectProvider(method = "getParkingOrderCount", type = ParkingOrderDao.class)
    List<StatisticsVo> getParkingOrderCount(String status);

    //根据id查询订单信息(微信端)
    @SelectProvider(method = "selectByWxId", type = ParkingOrderDao.class)
    RoadOrParkingVo selectByWxId(Integer id);

    /**
     * 描 述： TODO(查询流动订单总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    @SelectProvider(method = "getStreamingOrderCount", type = ParkingOrderDao.class)
    Integer getStreamingOrderCount(String dateStr, Integer park_id);

    /**
     * 描 述： TODO(查询流动订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    @SelectProvider(method = "getStreamingOrderSumAmount", type = ParkingOrderDao.class)
    String getStreamingOrderSumAmount(String dateStr, Integer park_id);


    /**
     * 描 述： TODO(查询流动订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    @SelectProvider(method = "getStreamingOrderReceivable", type = ParkingOrderDao.class)
    String getStreamingOrderReceivable(String dateStr, Integer park_id);

    /**
     * 描 述： TODO(今日停车订单数排名)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     */
    @SelectProvider(method = "getParkingOrderRanking", type = ParkingOrderDao.class)
    List<ShouldBillRankingVo> getParkingOrderRanking(CommonVo data);

    /**
     * 补缴金额
     */
    @SelectProvider(method = "getParkingOrderBjje", type = ParkingOrderDao.class)
    String getParkingOrderBjje(CommonVo data);

    /**
     * 停车订单列表
     */
    @SelectProvider(method = "getParkingOrderData", type = ParkingOrderDao.class)
    List<ParkingOrderVo> getParkingOrderData(CommonVo data);

    /**
     * 临停收入、临停欠费金额
     */
    @SelectProvider(method = "getSumLtsr", type = ParkingOrderDao.class)
    ShouldBillRankingVo getSumLtsr(CommonVo data);

    /**
     * 描 述： TODO(查询订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getOrderReceivable", type = ParkingOrderDao.class)
    String getOrderReceivable(CommonVo data);

    /**
     * 描 述： TODO(查询订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getOrderSumAmount", type = ParkingOrderDao.class)
    String getOrderSumAmount(CommonVo data);


    /**
     * 描 述： TODO( 根据收款类型统计 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getCollectionsStatistics", type = ParkingOrderDao.class)
    List<StatisticsVo> getCollectionsStatistics(CommonVo data);


    /**
     * 描 述： TODO(获取欠费停车数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getArrearageParkCount", type = ParkingOrderDao.class)
    String getArrearageParkCount(CommonVo data);

    /**
     * 描 述： TODO(查询停车场订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link  List<DeviceInfoVo>}
     */
    @SelectProvider(method = "getParkDeviceInfo", type = ParkingOrderDao.class)
    List<DeviceInfoVo> getParkDeviceInfo(CommonVo data);


    /**
     * 出场收费列表
     * Author wzn
     * Date 2022/1/28 10:10
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "appearanceFee")
    List<RoadParkListVo> appearanceFee(RoadParkListVo bean);

    /**
     * 出场收费详情
     * Author wzn
     * Date 2022/1/28 10:10
     */
    @SelectProvider(type = ParkingOrderDao.class, method = "appearanceFeeInfo")
    RoadParkListVo appearanceFeeInfo(RoadParkListVo bean);

    @SelectProvider(method = "toList", type = ParkingOrderDao.class)
    List<ParkingOrderVo> toList(ParkingOrderVo parkOrderVo);


    /**
     * 描 述： TODO(内部车明细/企业（税免）车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @param whitelistTypeName 免费类型名称（内部车 、 企业免税车）
     * @param whitelistType     免费类型
     * @return {@link String}
     */
    @SelectProvider(method = "toInnerFreeListQyNb", type = ParkingOrderDao.class)
    List<ParkingOrderIDetailsVo> toInnerFreeListQyNb(CommonVo data, String whitelistTypeName, String whitelistType);

    /**
     * 描 述： TODO(内部车明细/企业（税免）车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @param whitelistType 免费类型
     * @return {@link String}
     */
    @SelectProvider(method = "toInnerFreeListQyStatistics", type = ParkingOrderDao.class)
    ShouldBillRankingVo toInnerFreeListQyStatistics(CommonVo data, String whitelistType);

    //人工抬杆/车辆总数
    @SelectProvider(method = "getOpenGateCarNums", type = ParkingOrderDao.class)
    String getOpenGateCarNums(Integer park_id, String startTime, String endTime);

    //人工抬杆/车辆订单欠费总额
    @SelectProvider(method = "getOpenGateOrderArrears", type = ParkingOrderDao.class)
    String getOpenGateOrderArrears(Integer park_id, String startTime, String endTime);


    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    @SelectProvider(method = "getMobileCartCount", type = ParkingOrderDao.class)
    List<ParkRsVo> getMobileCartCount(CommonVo data);

    @SelectProvider(method = "getAllCartCount", type = ParkingOrderDao.class)
    List<ParkRsVo> getAllCartCount(CommonVo data);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车各收费方式收费总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    @SelectProvider(method = "getMobileCartPayStatistics", type = ParkingOrderDao.class)
    List<ParkRsVo> getMobileCartPayStatistics(CommonVo data);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 人工抬杆统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    @SelectProvider(method = "getArtificialLiftRodCount", type = ParkingOrderDao.class)
    List<ParkRsVo> getArtificialLiftRodCount(CommonVo data);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 逃费统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getEscapeFeeStatistics", type = ParkingOrderDao.class)
    List<ParkRsVo> getEscapeFeeStatistics(CommonVo data);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    @SelectProvider(method = "getFreeParkingCount", type = ParkingOrderDao.class)
    List<ParkRsVo> getFreeParkingCount(CommonVo data);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    @SelectProvider(method = "getWhiteCarnoCount", type = ParkingOrderDao.class)
    List<ParkRsVo> getWhiteCarnoCount(CommonVo data);


    @SelectProvider(method = "toCount", type = ParkingOrderDao.class)
    ParkingOrderVo toCount(ParkingOrderVo parkingOrderVo);

    @SelectProvider(method = "getParkOrderCount", type = ParkingOrderDao.class)
    List<StatisticsVo> getParkOrderCount();

    //会员非会员count个数
    @SelectProvider(method = "getMemberIdCount", type = ParkingOrderDao.class)
    List<StatisticsVo> getMemberIdCount(CommonVo data);

    /**
     * 各区域收费总额排名TOP5
     * 0 路段 1停车场
     * 作 者： thr
     */
    @SelectProvider(method = "getTotalChargesTop5Data", type = ParkingOrderDao.class)
    List<DataChartVo> getTotalChargesTop5Data(String type, String timeType);

    /**
     * 今日收费总额趋势分析
     * type 0 路段 1停车场
     * dayType 0 今日 -1 昨日
     * 作 者： thr
     */
    @SelectProvider(method = "getTotalChargesData", type = ParkingOrderDao.class)
    List<DataChartVo> getTotalChargesData(String type, int dayType);

    //各区域在停车辆总数排名TOP5
    @SelectProvider(method = "getCarTop5Data", type = ParkingOrderDao.class)
    List<DataChartVo> getCarTop5Data(String type, String timeType);

    //昨日停车占比
    @SelectProvider(method = "getParkTimeData", type = ParkingOrderDao.class)
    List<String> getParkTimeData(String type);

    //昨日停车占比数量
    @SelectProvider(method = "getParkTimeCountData", type = ParkingOrderDao.class)
    String getParkTimeCountData(String type);

    //近五日车位占用量及趋势
    @SelectProvider(method = "getParticipatingData", type = ParkingOrderDao.class)
    List<String> getParticipatingData(String type);

    //今年总收费额
    @SelectProvider(method = "chargesYear", type = ParkingOrderDao.class)
    DataChartVo chargesYear(String type, String year);

    //占用车位
    @SelectProvider(method = "occupationData", type = ParkingOrderDao.class)
    DataChartVo occupationData(String type);

    //停车实际收费
    @SelectProvider(method = "actualParkingChargeData", type = ParkingOrderDao.class)
    DataChartVo actualParkingChargeData(String type);

    //停车应该收费
    @SelectProvider(method = "parkingShouldChargedData", type = ParkingOrderDao.class)
    DataChartVo parkingShouldChargedData(String type);

    /**
     * 收费方式占比
     *
     * @return
     */
    @SelectProvider(method = "paymentTypeStatistics", type = ParkingOrderDao.class)
    List<StatisticsVo> paymentTypeStatistics();

    /**
     * 停车场收费排行
     *
     * @return
     */
    @SelectProvider(method = "parkChargeRank", type = ParkingOrderDao.class)
    List<Map<String, String>> parkChargeRank();

    /**
     * 停车场收费排行
     *
     * @return
     */
    @SelectProvider(method = "parkParkingCount", type = ParkingOrderDao.class)
    List<Map<String, String>> parkParkingCount();

    /**
     * 总优惠金额
     */
    @SelectProvider(method = "sumDiscountMoney", type = ParkingOrderDao.class)
    ParkingOrderVo sumDiscountMoney(ParkingOrderVo vo);

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    @SelectProvider(method = "getMoneyDataByHours", type = ParkingOrderDao.class)
    List<DataChartVo> getMoneyDataByHours(ParkingOrderVo vo);

    /**
     * 欠费分析 折线图
     * 数据分析页面 路侧/停车场详情页面使用
     * 今日0时至当前时间各个小时整点欠费金额统计
     */
    @SelectProvider(method = "getArrearsMoneyDataByHours", type = ParkingOrderDao.class)
    List<DataChartVo> getArrearsMoneyDataByHours(ParkingOrderVo vo);

    /**
     * 在停车辆
     */
    @SelectProvider(method = "getZtCount", type = ParkingOrderDao.class)
    ReportVo getZtCount(ParkingOrderVo vo);

    /**
     * 在停车辆
     * 各个路侧/停车场
     */
    @SelectProvider(method = "getZtCountByIds", type = ParkingOrderDao.class)
    List<ReportVo> getZtCountByIds(ParkingOrderVo vo);

    /**
     * 在停数量排名TOP5
     */
    @SelectProvider(method = "getZtCountTop5", type = ParkingOrderDao.class)
    List<ReportVo> getZtCountTop5(ParkingOrderVo vo);

    /**
     * 今日停车时长占比
     */
    @SelectProvider(method = "getTodayParkTimeData", type = ParkingOrderDao.class)
    List<ReportVo> getTodayParkTimeData(String type, Integer id);

    /**
     * 统计今日停车占比总次数
     */
    @SelectProvider(method = "getTodayParkTimeCount", type = ParkingOrderDao.class)
    String getTodayParkTimeCount(String type, Integer id);

    /**
     * 折线图： 出入趋势双折线：7天内出入次数
     */
    @SelectProvider(method = "getInOutCount", type = ParkingOrderDao.class)
    List<DataChartVo> getInOutCount(String type, String inOutType);

    /**
     * 自助停车率（排除人工订单和人工抬杆的所有状态订单）
     */
    @SelectProvider(method = "getCountBySource", type = ParkingOrderDao.class)
    List<DataChartVo> getCountBySource(RoadParkListVo vo);

    /**
     * 订单收费率（已缴费和已完成的订单数量/总数量）
     */
    @SelectProvider(method = "getCountByStatus", type = ParkingOrderDao.class)
    List<DataChartVo> getCountByStatus(RoadParkListVo vo);

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 缴费时间-出场时间<15分钟的数量
     */
    @SelectProvider(method = "getCountByLt15Min", type = ParkingOrderDao.class)
    String getCountByLt15Min(RoadParkListVo vo);

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 总已缴费、总已完成状态的订单
     */
    @SelectProvider(method = "getYjfYwcCount", type = ParkingOrderDao.class)
    String getYjfYwcCount(RoadParkListVo vo);

    /**
     * 超24H占位率（已缴费+已完成的超24H停车时长/总订单数量）
     */
    @SelectProvider(method = "getCountByGt24h", type = ParkingOrderDao.class)
    String getCountByGt24h(RoadParkListVo vo);

    /**
     * 发票发放率
     */
    @SelectProvider(method = "getFpCount", type = ParkingOrderDao.class)
    String getFpCount(RoadParkListVo vo);

    /**
     * 欠费车辆排名TOP5
     */
    @SelectProvider(method = "getSumAmountTop10ByCarNo", type = ParkingOrderDao.class)
    List<ParkingOrderVo> getSumAmountTop10ByCarNo(String type);

    /**
     * 路段和停车场订单信息
     * 查询视图
     */
    @SelectProvider(method = "selectRoadParkList", type = ParkingOrderDao.class)
    List<RoadParkListVo> selectRoadParkList(RoadParkListVo bean);

    /**
     * 应收金额、已付金额/营收金额、待付金额/欠费金额
     */
    @SelectProvider(method = "getSumAmounts", type = ParkingOrderDao.class)
    RoadParkListVo getSumAmounts(RoadParkListVo bean);

    /**
     * 今日进出 折线图
     * 数据分析页面
     * 今日0时至当前时间各个小时整点进出次数统计
     */
    @SelectProvider(method = "getInOutDataByHours", type = ParkingOrderDao.class)
    List<DataChartVo> getInOutDataByHours(RoadParkListVo bean);

    /**
     * 近30日进出 折线图
     */
    @SelectProvider(method = "getInOutDaysData", type = ParkingOrderDao.class)
    List<DataChartVo> getInOutDaysData(RoadParkListVo bean);

    /**
     * 支付记录关联 路段和停车场订单信息
     */
    @SelectProvider(method = "getOrderDataByPaymentId", type = ParkingOrderDao.class)
    List<RoadParkListVo> getOrderDataByPaymentId(RoadParkListVo bean);

}
