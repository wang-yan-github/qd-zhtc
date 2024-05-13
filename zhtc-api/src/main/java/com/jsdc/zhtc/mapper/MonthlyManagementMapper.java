package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.MonthlyManagementDao;
import com.jsdc.zhtc.model.MonthlyManagement;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 类 名: 包月管理
 * 描 述: MonthlyManagementMapper
 * 作 者: lw
 * 创 建：2022/1/4 10:18
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Mapper
public interface MonthlyManagementMapper extends BaseMapper<MonthlyManagement> {


    /**
     * 描 述： TODO(包月充值统计 按日期)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<CensusVo>}
     */
    @SelectProvider(method = "getTimeCensus", type = MonthlyManagementDao.class)
    List<CensusVo> getTimeCensus(CommonVo data);

    /**
     * 描 述： TODO(包月充值统计 按车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<CensusVo>}
     */
    @SelectProvider(method = "getLicenceCensus", type = MonthlyManagementDao.class)
    List<CensusVo> getLicenceCensus(CommonVo data);

    /**
     * 描 述： TODO(车牌总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<CensusVo>}
     */
    @SelectProvider(method = "getLicenceCount", type = MonthlyManagementDao.class)
    int getLicenceCount(CommonVo data);

    /**
     * 描 述： TODO(充值笔数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<CensusVo>}
     */
    @SelectProvider(method = "getRechargeCount", type = MonthlyManagementDao.class)
    int getRechargeCount(CommonVo data);

    /**
     * 描 述： TODO(充值总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<CensusVo>}
     */
    @SelectProvider(method = "getRechargeAmount", type = MonthlyManagementDao.class)
    String getRechargeAmount(CommonVo data);

    @SelectProvider(method = "byExport", type = MonthlyManagementDao.class)
    List<MonthlyManagementVo> byExport(String isTheCompany, String parkingType);


    @SelectProvider(method = "info", type = MonthlyManagementDao.class)
    List<MonthlyManagementVo> info(String monthly_code);


    @SelectProvider(method = "num", type = MonthlyManagementDao.class)
    List<MonthlyManagementVo> num(String monthly_code);

    /**
     * 描 述： TODO( 用户包月管理信息 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "findManageInfo", type = MonthlyManagementDao.class)
    MonthlyManagementVo findManageInfo(CommonVo data);


    /**
     * 描 述： TODO(查询在包月车辆总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link Integer}
     */
    @SelectProvider(method = "getNormalMPCount", type = MonthlyManagementDao.class)
    Integer getNormalMPCount(CommonVo data);

    /**
     * 查询包月信息
     *
     * @param car_id
     * @param parkingplace_id
     * @param parkType
     * @param dateString
     * @return
     */
    @SelectProvider(method = "selectInfoByCarId", type = MonthlyManagementDao.class)
    List<MonthlyManagement> selectInfoByCarId(Integer car_id, Integer parkingplace_id, String parkType, String dateString);

    /**
     * 微信小程序
     * 包月/月卡
     * 分页查询
     *
     * @author thr
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "selectPageList")
    List<MonthlyManagementVo> selectPageList(MonthlyManagementVo bean);

    /**
     * 描 述： TODO(查询包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType 统计类型
     * @param dateStr    创建时间 年月日
     * @return {@link String}
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyPaymentCount")
    Integer getMonthlyPaymentCount(String parkinType, String dateStr, Integer park_id);

    /**
     * 描 述： TODO(合计包月总费用)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType 统计类型
     * @param dateStr    创建时间 年月日
     * @return {@link String}
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyPaymentSumCost")
    String getMonthlyPaymentSumCost(String parkinType, String dateStr, Integer park_id);

    /**
     * 包月收入
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyPaymentSum")
    String getMonthlyPaymentSum(CommonVo vo);

    /**
     * 停车场
     * 包月收入明细
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyPaymentData")
    List<MonthlyManagementVo> getMonthlyPaymentData(CommonVo vo);

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyRentCount")
    List<ParkRsVo> getMonthlyRentCount(CommonVo data);

    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyRentCountParkAndRoad")
    List<ParkRsVo> getMonthlyRentCountParkAndRoad(CommonVo data);

    /**
     * 描 述： TODO(月租车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthCarRentalDetail")
    List<MonthCarRentalDetailVo> getMonthCarRentalDetail(CommonVo data);

    /**
     * 描 述： TODO(月租车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthCarRentalStatistics")
    ShouldBillRankingVo getMonthCarRentalStatistics(CommonVo data);

    /**
     * 今日停车订单数排名
     * 包月收入
     * 微信小程序包月金额统计
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyAmountGroup")
    List<PaymentOrder> getMonthlyAmountGroup(CommonVo data);

    /**
     * 支付流水明细关联包月订单
     */
    @SelectProvider(type = MonthlyManagementDao.class, method = "getMonthlyDataByPaymentId")
    List<MonthlyManagementVo> getMonthlyDataByPaymentId(Integer paymentId);

}
