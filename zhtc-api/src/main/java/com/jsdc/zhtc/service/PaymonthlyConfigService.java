package com.jsdc.zhtc.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.PaymonthlyConfigDao;
import com.jsdc.zhtc.mapper.ParkMapper;
import com.jsdc.zhtc.model.PaymonthlyConfig;
import com.jsdc.zhtc.model.PaymonthlyParkingplace;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.utils.RedisUtils;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: PaymonthlyConfigService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author wp
 */
@Service
@Transactional
public class PaymonthlyConfigService extends BaseService<PaymonthlyConfigDao, PaymonthlyConfig> {


    @Autowired
    private SysUserService userService;
    @Autowired
    private PaymonthlyParkingplaceService ppService;

    @Autowired
    private MonthlyManagementService monthlyManagementService;


    /**
     * create by wp at 2021/12/31 10:14
     * description: 新增包月配置
     *
     * @param paymonthlyConfig
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo add(PaymonthlyConfig paymonthlyConfig) {
        if (!verify(paymonthlyConfig)) {
            return ResultInfo.error("必填字段不能为空！");
        }
        SysUser sysUser = userService.getUser();
        String model = RedisUtils.getBeanValue(sysUser.getLogin_name() + "_sys").toString();
        paymonthlyConfig.setCreate_time(new Date());
        paymonthlyConfig.setCreate_user(sysUser.getId());
        paymonthlyConfig.setUpdate_time(new Date());
        paymonthlyConfig.setUpdate_user(sysUser.getId());
        paymonthlyConfig.setIs_del(GlobalData.ISDEL_NO);
        paymonthlyConfig.setParking_type(model);
        if (insert(paymonthlyConfig) > 0) {
            ppService.addMap(paymonthlyConfig.getId(), paymonthlyConfig.getParkingIds());
            return ResultInfo.success("新增包月配置成功！", "新增包月配置：" + paymonthlyConfig.getName());
        } else {
            return ResultInfo.error("新增包月配置失败！");
        }
    }

    /**
     * create by wp at 2021/12/31 10:38
     * description: 修改包月配置信息
     *
     * @param paymonthlyConfig
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(PaymonthlyConfig paymonthlyConfig) {
        if (null == paymonthlyConfig.getId()) {
            return ResultInfo.error("必填字段不能为空！");
        }
        if (!verify(paymonthlyConfig)) {
            return ResultInfo.error("必填字段不能为空！");
        }
        SysUser sysUser = userService.getUser();
        int id = paymonthlyConfig.getId();
        PaymonthlyConfig originalConfig = selectById(id);
        BeanUtils.copyProperties(paymonthlyConfig, originalConfig);
        originalConfig.setUpdate_user(sysUser.getId());
        originalConfig.setUpdate_time(new Date());
        if (updateById(originalConfig) > 0) {
            ppService.editMap(id, paymonthlyConfig.getParkingIds());
            return ResultInfo.success("修改包月配置成功！", "修改包月配置：" + originalConfig.getName());
        } else {
            return ResultInfo.error("修改包月配置失败！");
        }
    }

    /**
     * 描 述： TODO(查询全部)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link ResultInfo}
     */
    public ResultInfo getAll(CommonVo data) {
        LambdaQueryWrapper<PaymonthlyConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymonthlyConfig::getIs_del, GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr2())) {
            wrapper.eq(PaymonthlyConfig::getParking_type, data.getStr2());
        }
        if (StringUtils.isNotBlank(data.getIsTheCompany())) {
            wrapper.eq(PaymonthlyConfig::getMonthType, data.getIsTheCompany());
        }
        List<PaymonthlyConfig> paymonthlyConfigs = this.selectList(wrapper);
        return ResultInfo.success(paymonthlyConfigs);

    }


    @Autowired
    private ParkMapper parkMapper;


    /**
     * create by wp at 2022/1/10 10:11
     * description: 删除配置方案
     *
     * @param id
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo del(int id) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(id));
        String endTime = DateUtil.formatDateTime(new Date());
        Long count = monthlyManagementService.getCountByConfigIds(list, endTime);
        if (count > 0) {
            return ResultInfo.error("该方案正在被使用，无法删除！");
        }
        UpdateWrapper<PaymonthlyConfig> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(PaymonthlyConfig::getIs_del, GlobalData.ISDEL_YES)
                .eq(PaymonthlyConfig::getId, id);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除配置方案成功！", "删除包月配置，数据id：" + id);
        } else {
            return ResultInfo.error("删除配置方案失败！");
        }
    }

    public ResultInfo delAll(String ids) {
        List<String> list = Arrays.asList(ids.split(","));
        String endTime = DateUtil.formatDateTime(new Date());
        Long count = monthlyManagementService.getCountByConfigIds(list, endTime);
        if (count > 0) {
            return ResultInfo.error("有方案正在被使用，无法删除！");
        }
        UpdateWrapper<PaymonthlyConfig> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(PaymonthlyConfig::getIs_del, GlobalData.ISDEL_YES)
                .in(PaymonthlyConfig::getId, list);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除配置方案成功！", "批量删除包月配置，数据id：" + ids);
        } else {
            return ResultInfo.error("删除配置方案失败！");
        }
    }

    /**
     * create by wp at 2022/1/10 14:18
     * description: 查询配置信息
     *
     * @param configId
     * @return com.jsdc.zhtc.model.PaymonthlyConfig
     */
    public PaymonthlyConfig getConfig(int configId) {
        PaymonthlyConfig config = selectById(configId);
        LambdaQueryWrapper<PaymonthlyParkingplace> wrapper = new LambdaQueryWrapper();
        wrapper.eq(PaymonthlyParkingplace::getPaymonthly_config_id, configId)
                .eq(PaymonthlyParkingplace::getIs_del, GlobalData.ISDEL_NO);
        List<PaymonthlyParkingplace> list = ppService.selectList(wrapper);
        List<Integer> roadIds = list.stream().map(x -> x.getParkingplace_id()).collect(Collectors.toList());
        config.setRoadIds(roadIds);
        return config;
    }


    public List<SysDict> getCategory() {
        return DcCacheDataUtil.getDictByDictType(GlobalData.MONTHLY_CONFIG_CATEGORY);
    }

    /**
     * create by wp at 2021/12/31 10:11
     * description: 验证必填字段是否为空
     *
     * @param paymonthlyConfig
     * @return boolean
     */
    public boolean verify(PaymonthlyConfig paymonthlyConfig) {
        if (StringUtils.isEmpty(paymonthlyConfig.getName()) || StringUtils.isEmpty(paymonthlyConfig.getParking_type())
                || StringUtils.isEmpty(paymonthlyConfig.getCategory()) || StringUtils.isEmpty(paymonthlyConfig.getParkingIds())
                || StringUtils.isEmpty(paymonthlyConfig.getMonthType())) {
            return false;
        }

        return true;
    }


}
