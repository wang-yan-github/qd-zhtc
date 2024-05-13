package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.PaymonthlyConfig;
import com.jsdc.zhtc.service.PaymonthlyConfigService;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: PaymonthlyConfigController
 * Description: 包月配置管理
 * date: 2022/1/10 10:03
 *
 * @author wp
 */
@RestController
@RequestMapping("paymonthly")
public class PaymonthlyConfigController {
    @Autowired
    private PaymonthlyConfigService configService;

    /**
     * 描 述： TODO(查询全部)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link }
     */
    @RequestMapping("/getAll")
    public ResultInfo getAll(@RequestBody CommonVo data) {

        return configService.getAll(data);
    }

    /**
     * create by wp at 2022/1/10 10:07
     * description: 保存包月配置方案
     *
     * @param config
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("save.do")
    @LogInfo(LogEnums.LOG_ADDMONTHYCONFIG)
    public ResultInfo save(PaymonthlyConfig config) {
        return configService.add(config);
    }

    /**
     * create by wp at 2022/1/10 10:08
     * description: 编辑包月配置方案
     *
     * @param config
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("edit.do")
    @LogInfo(LogEnums.LOG_UPDATEMONTHYCONFIG)
    public ResultInfo edit(PaymonthlyConfig config) {
        return configService.edit(config);
    }

    /**
     * create by wp at 2022/1/10 10:12
     * description: 删除配置方案
     *
     * @param config
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("del.do")
    @LogInfo(LogEnums.LOG_DELETEMONTHYCONFIG)
    public ResultInfo del(PaymonthlyConfig config) {
        return configService.del(config.getId());
    }

    /**
     * create by wp at 2022/1/10 15:38
     * description: 批量删除
     *
     * @param ids
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("delAll.do")
    @LogInfo(LogEnums.LOG_DELETEBATCHMONTHYCONFIG)
    public ResultInfo del(String ids) {
        return configService.delAll(ids);
    }

    /**
     * create by wp at 2022/1/10 14:18
     * description: 查询配置信息
     *
     * @param configId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getConfig.do")
    public ResultInfo getConfig(int configId) {
        return ResultInfo.success(configService.getConfig(configId));
    }


    /**
     * create by wp at 2022/1/10 13:34
     * description: 获取包月配置类型字典
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getCategory.do")
    public ResultInfo getCategory() {
        return ResultInfo.success(configService.getCategory());
    }
}
