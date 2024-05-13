package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.RechargeActivityConfigDao;
import com.jsdc.zhtc.model.RechargeActivityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: RechargeActivityConfigService
 * Description:
 * date: 2021/12/30 10:55
 *
 * @author wh
 */
@Service
@Transactional
public class RechargeActivityConfigService extends BaseService<RechargeActivityConfigDao, RechargeActivityConfig> {

    /**
     * create by wh at 2022/1/4 1:37
     * description: 新增充值活动与活动规则关联关系
     *
     * @param rechargeActivityId
     * @param rechargeActivityConfigs
     * @return void
     */
    public void addMap(int rechargeActivityId, List<RechargeActivityConfig> rechargeActivityConfigs) {
        for (RechargeActivityConfig rechargeActivityConfig : rechargeActivityConfigs) {
            rechargeActivityConfig.setRecharge_activity_id(rechargeActivityId);
            rechargeActivityConfig.setIs_del(GlobalData.ISDEL_NO);
            insert(rechargeActivityConfig);
        }
    }

    /**
     * create by wh at 2022/1/4 1:37
     * description: 编辑充值活动与活动规则关联关系
     *
     * @param rechargeActivityId
     * @param rechargeActivityConfigs
     * @return void
     */
    public void editMap(int rechargeActivityId, List<RechargeActivityConfig> rechargeActivityConfigs) {
        UpdateWrapper<RechargeActivityConfig> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(RechargeActivityConfig::getIs_del, GlobalData.ISDEL_YES)
                .eq(RechargeActivityConfig::getRecharge_activity_id, rechargeActivityId);
        update(null, wrapper);
        addMap(rechargeActivityId, rechargeActivityConfigs);
    }
}
