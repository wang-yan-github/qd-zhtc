package com.jsdc.zhtc.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.service.OperateAppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 * 申诉订单
 */
@Component
public class Appealupd {

    @Autowired
    private OperateAppealService operateAppealService;

    /**
     * 申诉订单,锁定数据
     * 每30分钟刷新一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void appealUpd() {
//        System.out.println("=======申诉订单,锁定数据每30分钟刷新一次 appealUpd================");
        List<OperateAppeal> list = operateAppealService.selectList(new QueryWrapper<OperateAppeal>()
                .eq("is_del", 0)
                .eq("is_verify", "1") //是否核实 1 是 0 否
                .eq("appeal_status", "1"));//申诉状态 1 待处理 2通过 3驳回 4完成
        if (StringUtils.isNotEmpty(list) && list.size() > 0) {
            for (OperateAppeal p : list) {
                new Thread(() -> {
                    p.setIs_verify("0");
                    operateAppealService.updateById(p);
                }).start();
            }
        }
    }
}
