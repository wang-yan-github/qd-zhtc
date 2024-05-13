package com.jsdc.zhtc.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.mapper.MonthlyManagementMapper;
import com.jsdc.zhtc.mapper.MonthlyPaymentRecordMapper;
import com.jsdc.zhtc.mapper.PaymentOrderMapper;
import com.jsdc.zhtc.mapper.WxPayConfigMapper;
import com.jsdc.zhtc.model.PaymonthlyConfig;
import com.jsdc.zhtc.pay.WxPay;
import com.jsdc.zhtc.service.PaymonthlyConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务
 * 包月
 */
@Component
public class MonthlyManageSchedule {

    @Autowired
    MonthlyPaymentRecordMapper mapper;
    @Autowired
    PaymentOrderMapper paymentOrderMapper;
    @Autowired
    WxPayConfigMapper wxPayConfigMapper;
    @Autowired
    MonthlyManagementMapper monthlyManagementMapper;
    @Autowired
    PaymonthlyConfigService paymonthlyConfigService;
    @Autowired
    WxPay wxPay;

    Logger logger = LoggerFactory.getLogger(MonthlyManageSchedule.class);

    /**
     * 包月配置管理价格变更
     * 每年12月31日 23:59:59执行，变更包月价格
     */
    @Scheduled(cron = "59 59 23 31 12 ?")
    public void updConfigPrice() {
        logger.info(">>>>>>>>>>>>>>>>>>包月配置管理价格变更");

        LambdaQueryWrapper<PaymonthlyConfig> wrapper = new LambdaQueryWrapper();
        // 价格类型 1永久 2期限
        wrapper.eq(PaymonthlyConfig::getPrice_type, "2")
                .eq(PaymonthlyConfig::getIs_del, GlobalData.ISDEL_NO)
                .isNotNull(PaymonthlyConfig::getNew_price);
        List<PaymonthlyConfig> list = paymonthlyConfigService.selectList(wrapper);
        for (PaymonthlyConfig paymonthlyConfig : list) {
            if (StringUtils.isNotEmpty(paymonthlyConfig.getNew_price())) {
                paymonthlyConfig.setPrice(paymonthlyConfig.getNew_price());
                paymonthlyConfig.setUpdate_time(new Date());
                paymonthlyConfigService.updateById(paymonthlyConfig);
            }
        }
    }
}
