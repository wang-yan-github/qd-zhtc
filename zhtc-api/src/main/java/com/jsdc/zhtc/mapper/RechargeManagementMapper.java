package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.RechargeManagementDao;
import com.jsdc.zhtc.model.RechargeManagement;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.RechargeManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 类 名: 充值管理
 * 描 述: RechargeManagementMapper
 * 作 者: lw
 * 创 建：2022/1/4 9:38
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Mapper
public interface RechargeManagementMapper extends BaseMapper<RechargeManagement> {

    @SelectProvider(method = "getRechargeMInfo", type = RechargeManagementDao.class)
    List<RechargeManagementVO> getRechargeMInfo(CommonVo data);

    @SelectProvider(method = "getrechargeRecords", type = RechargeManagementDao.class)
    List<RechargeManagementVO> getrechargeRecords(Integer memberId);

    /**
     * 充值记录
     */
    @SelectProvider(method = "getRechargeDataByPaymentId", type = RechargeManagementDao.class)
    List<RechargeManagementVO> getRechargeDataByPaymentId(Integer paymentId);

    @SelectProvider(method = "sumRecharge", type = RechargeManagementDao.class)
    String sumRecharge(Integer memberId);

    /**
     * 充值收入
     */
    @SelectProvider(method = "getSumCzMoney", type = RechargeManagementDao.class)
    String getSumCzMoney(CommonVo data);
}
