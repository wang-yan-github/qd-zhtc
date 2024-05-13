package com.jsdc.zhtc.common.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.service.OperateAppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ScheduledTask
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/28 16:48
 * @Version 1.0
 */
@Component
public class ScheduledTask {

    @Autowired
    private OperateAppealService operateAppealService;

    /**
     * 申诉订单,锁定数据每15分钟刷新一次
     */
    //@Scheduled(cron = "0 */30 * * * ?")
    public void appealupd() {
        System.out.println("=======================正在执行中");
        List<OperateAppeal> list = operateAppealService.selectList(new QueryWrapper<OperateAppeal>().eq("is_del", 0).eq("is_verify", "1").eq("appeal_status", "1"));
        if (null != list && list.size() > 0) {
            for (OperateAppeal p : list) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        p.setIs_verify("0");
                        operateAppealService.updateById(p);
                    }
                }).start();
            }
        }
    }

}
