package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.PaymentOrderDao;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.vo.DataChartVo;
import com.jsdc.zhtc.vo.PaymentOrderVo;
import com.jsdc.zhtc.vo.ReportVo;
import com.jsdc.zhtc.vo.RoadParkListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {
    @SelectProvider(type = PaymentOrderDao.class, method = "listPaymentOrder")
    List<Map<String, String>> listPaymentOrder(PaymentOrderVo vo);

    @SelectProvider(type = PaymentOrderDao.class, method = "listRoadParkingOrder")
    List<Map<String, String>> listRoadParkingOrder(PaymentOrderVo vo);

    /**
     * 柱状图 支付方式/金额
     */
    @SelectProvider(type = PaymentOrderDao.class, method = "getMoneysByType")
    List<PaymentOrder> getMoneysByType(RoadParkListVo vo);

    /**
     * 营收金额
     * 0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
     */
    @SelectProvider(type = PaymentOrderDao.class, method = "getSumMoneys")
    ReportVo getSumMoneys(PaymentOrderVo vo);

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    @SelectProvider(type = PaymentOrderDao.class, method = "getMoneyDataByHours")
    List<DataChartVo> getMoneyDataByHours(PaymentOrderVo vo);

    /**
     * 支付流水明细分页查询
     */
    @SelectProvider(type = PaymentOrderDao.class, method = "toList")
    List<PaymentOrder> toList(PaymentOrderVo vo);
}
