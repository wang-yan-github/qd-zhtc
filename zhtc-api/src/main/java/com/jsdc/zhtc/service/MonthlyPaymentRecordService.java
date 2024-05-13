package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.MonthlyPaymentRecordDao;
import com.jsdc.zhtc.model.MonthlyPaymentRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类 名: 包月管理和支付订单关联表
 * 描 述: MonthlyPaymentRecord
 * 作 者: thr
 * 创 建：2022/2/23 10:11
 */
@Service
@Transactional
public class MonthlyPaymentRecordService extends BaseService<MonthlyPaymentRecordDao, MonthlyPaymentRecord> {


}
