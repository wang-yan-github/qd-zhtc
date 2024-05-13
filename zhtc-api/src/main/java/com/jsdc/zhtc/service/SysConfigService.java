package com.jsdc.zhtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysConfigDao;
import com.jsdc.zhtc.model.SysConfig;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: SysConfigService
 * Description:
 * date: 2021/12/31 10:42
 *
 * @author wp
 */
@Service
@Transactional
public class SysConfigService extends BaseService<SysConfigDao, SysConfig> {
    @Autowired
    private SysUserService userService;

    /**
     * create by wp at 2022/1/11 8:42
     * description: 查询系统配置MAP
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getMap() {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getIs_del, GlobalData.ISDEL_NO);
        List<SysConfig> list = selectList(wrapper);
        JSONObject result = new JSONObject();
        list.forEach(x -> {
            switch (x.getConfig_key()) {
                case "0":
                    result.put("invoiceApplyTime", x.getConfig_value());
                    break;
                case "1":
                    result.put("leaveTime", x.getConfig_value());
                    break;
                case "2":
                    result.put("mergeTime", x.getConfig_value());
                    break;
                case "3":
                    result.put("giftMoney", x.getConfig_value());
                    break;
                case "4":
                    result.put("Discount", x.getConfig_value());
                    break;
            }
        });
        return ResultInfo.success(result);
    }

    /**
     * create by wp at 2021/12/31 10:48
     * description: 新增系统参数
     *
     * @param sysConfig
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(SysConfig sysConfig) {
        SysUser sysUser = userService.getUser();
        sysConfig.setCreate_time(new Date());
        sysConfig.setCreate_user(sysUser.getId());
        sysConfig.setUpdate_time(new Date());
        sysConfig.setUpdate_user(sysUser.getId());
        sysConfig.setIs_del(GlobalData.ISDEL_NO);
        if (insert(sysConfig) > 0) {
            return ResultInfo.success("新增系统参数成功！");
        } else {
            return ResultInfo.error("新增系统参数失败！");
        }
    }

    /**
     * create by wp at 2022/1/11 8:54
     * description: 更新系统参数
     *
     * @param invoiceApplyTime 设置申请发票时间
     * @param leaveTime        缴费后驶离时间
     * @param mergeTime        合并重复订单间隔时间
     * @param giftMoney        关注后赠送金额
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(String invoiceApplyTime, String leaveTime, String mergeTime, String giftMoney, String Discount) {
        Map<String, SysDict> map = DcCacheDataUtil.getMapDicts("sysConfig");
        SysConfig sysConfig1 = getConfig(map.get(GlobalData.SYS_CONFIG_INVOICEAPPLYTIME).getDc_value());
        sysConfig1.setConfig_value(invoiceApplyTime);
        updateById(sysConfig1);

        SysConfig sysConfig2 = getConfig(map.get(GlobalData.SYS_CONFIG_LEAVETIME).getDc_value());
        sysConfig2.setConfig_value(leaveTime);
        updateById(sysConfig2);

        SysConfig sysConfig3 = getConfig(map.get(GlobalData.SYS_CONFIG_MERGETIME).getDc_value());
        sysConfig3.setConfig_value(mergeTime);
        updateById(sysConfig3);

        SysConfig sysConfig4 = getConfig(map.get(GlobalData.SYS_CONFIG_GIFTMONEY).getDc_value());
        sysConfig4.setConfig_value(giftMoney);
        updateById(sysConfig4);

        SysConfig sysConfig5 = getConfig(map.get(GlobalData.DISCOUNTZK).getDc_value());
        sysConfig5.setConfig_value(Discount);
        updateById(sysConfig5);
        return ResultInfo.success(null, "修改系统参数");
    }

    /**
     * create by wp at 2021/12/31 11:24
     * description: 条件查询
     *
     * @param config
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo list(SysConfig config) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(config.getConfig_name())) {
            wrapper.like(SysConfig::getConfig_name, config.getConfig_name());
        }
        wrapper.eq(SysConfig::getIs_del, GlobalData.ISDEL_NO);
        return ResultInfo.success(selectList(wrapper));
    }

    public SysConfig getConfig(String configName) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfig_name_en, configName);
        wrapper.eq(SysConfig::getIs_del, GlobalData.ISDEL_NO);
        return selectOne(wrapper);
    }
}
