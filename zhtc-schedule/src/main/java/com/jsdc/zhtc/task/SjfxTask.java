package com.jsdc.zhtc.task;

import com.jsdc.zhtc.service.Sjfx2Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 统计分析
 * 定时任务
 */
@Component
public class SjfxTask {

    @Autowired
    private Sjfx2Service sjfx2Service;

    /**
     * 车位满位率 = （在停数量/订单总数量）
     * 每小时整点执行
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void onCwMyl() {
        // 类型 0路测 1停车场 2（路测+停车场）总和
        sjfx2Service.createCwMwl("1");
        System.out.println(new DateTime().toString("yyyy-MM-dd") + "------------------------车位满位率/每小时整点执行----------------------------");
    }

    /**
     * 车位周转率（周转率=路侧+停车场日均总停车数（今日路侧+停车场总订单数）/路侧+停车场总泊位数X100%）
     * 每天0时1分执行
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void onCwZzl() {
        // 类型 0路测 1停车场 2（路测+停车场）总和
        sjfx2Service.createCwZzl("1");
        System.out.println(new DateTime().toString("yyyy-MM-dd") + "------------------------车位周转率/每天0时1分执行----------------------------");
    }

}
