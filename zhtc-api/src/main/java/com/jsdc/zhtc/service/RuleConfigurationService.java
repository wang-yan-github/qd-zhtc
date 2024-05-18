package com.jsdc.zhtc.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.RuleConfigurationDao;
import com.jsdc.zhtc.model.RuleConfiguration;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RuleConfigurationService extends BaseService<RuleConfigurationDao, RuleConfiguration> {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictService sysDictService;


    /**
     * 查询
     */
    public PageInfo<RuleConfiguration> selectAll(PageVo<RuleConfiguration> data) {
        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());

        }
        LambdaQueryWrapper<RuleConfiguration> wrapper = new LambdaQueryWrapper<>();
        RuleConfiguration bean = data.getBean();
        if (null != bean) {
            wrapper.eq(StringUtils.isNotEmpty(bean.getType()), RuleConfiguration::getType, bean.getType());
            wrapper.like(StringUtils.isNotEmpty(bean.getLabel()), RuleConfiguration::getLabel, bean.getLabel());
            wrapper.like(StringUtils.isNotEmpty(bean.getValue()), RuleConfiguration::getValue, bean.getValue());
        }

        wrapper.eq(RuleConfiguration::getIs_del, GlobalData.ISDEL_NO);
        wrapper.orderByDesc(RuleConfiguration::getSort);

        List<RuleConfiguration> lists = selectList(wrapper);

        Map<String, String> sysDictMap = sysDictService.getLabel(GlobalData.RULE_CONFIGURATION_TYPE);
        for (RuleConfiguration RuleConfiguration : lists) {
            RuleConfiguration.setType_name(sysDictMap.get(RuleConfiguration.getType()));
        }

        PageInfo<RuleConfiguration> listPage = new PageInfo<>(lists);
        return listPage;
    }

    /**
     * 根据id查询
     */
    public ResultInfo selectById(RuleConfiguration bean) {
        RuleConfiguration data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error(null, "未查询到数据");
        }
    }

    /**
     * 新增数据
     */
    public ResultInfo saveData(RuleConfiguration bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setId(IdUtil.simpleUUID());
        bean.setIs_del(GlobalData.ISDEL_NO);
        bean.setCreate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setCreate_user_name(sysUser.getUser_name());

        if (this.insert(bean) > 0) {
            return ResultInfo.success(null, "数据添加成功");
        } else {
            return ResultInfo.success("message", "数据添加失败");
        }
    }

    /**
     * 根据id更新
     */
    public ResultInfo updateInfo(RuleConfiguration bean) {
        SysUser sysUser = sysUserService.getUser();
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(sysUser.getId());
        bean.setUpdate_user_name(sysUser.getUser_name());

        if (this.updateById(bean) > 0) {
            return ResultInfo.success(null, "操作成功");
        } else {
            return ResultInfo.error(null, "操作成功");
        }

    }

    /**
     * 根据id删除
     */
    public ResultInfo deleById(RuleConfiguration bean) {
        bean.setIs_del(GlobalData.ISDEL_YES);
        if (this.updateById(bean) > 0) {
            return ResultInfo.success(null, "删除成功");
        } else {
            return ResultInfo.error(null, "操作失败");
        }
    }


}
