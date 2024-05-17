package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysDictDao;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: SysDictService
 * Description:
 * date: 2021/12/30 10:34
 *
 * @author wp
 */
@Service
@Transactional
public class SysDictService extends BaseService<SysDictDao, SysDict> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CacheDataService cacheDataService;

    /**
     * 查看字典数据
     *
     * @param sysDict
     * @return
     * @author bn
     */
    public List<SysDict> toList(SysDict sysDict) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(sysDict.getDict_type())) {
            queryWrapper.eq("dict_type", sysDict.getDict_type());
        }

        if (StringUtils.isNotEmpty(sysDict.getDc_value())) {
            queryWrapper.eq("dc_value", sysDict.getDc_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getDq_value())) {
            queryWrapper.eq("dq_value", sysDict.getDq_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getJk_value())) {
            queryWrapper.eq("jk_value", sysDict.getJk_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getJs_value())) {
            queryWrapper.eq("js_value", sysDict.getJs_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getParent_id())) {
            queryWrapper.eq("parent_id", sysDict.getParent_id());
        }

        if (StringUtils.isNotEmpty(sysDict.getLabel())) {
            queryWrapper.eq("label", sysDict.getLabel());
        }
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }


    /**
     * 查看字典数据
     *
     * @param sysDict
     * @return
     * @author bn
     */
    public PageInfo<SysDict> toList(Integer pageIndex, Integer pageSize, SysDict sysDict) {
        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(sysDict.getDict_type())) {
            queryWrapper.eq("dict_type", sysDict.getDict_type());
        }

        if (StringUtils.isNotEmpty(sysDict.getDc_value())) {
            queryWrapper.eq("dc_value", sysDict.getDc_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getDq_value())) {
            queryWrapper.eq("dq_value", sysDict.getDq_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getJk_value())) {
            queryWrapper.eq("jk_value", sysDict.getJk_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getJs_value())) {
            queryWrapper.eq("js_value", sysDict.getJs_value());
        }

        if (StringUtils.isNotEmpty(sysDict.getParent_id())) {
            queryWrapper.eq("parent_id", sysDict.getParent_id());
        }

        if (StringUtils.isNotEmpty(sysDict.getLabel())) {
            queryWrapper.like("label", sysDict.getLabel());
        }
        queryWrapper.eq("is_del", 0);
        List<SysDict> sysDictList = selectList(queryWrapper);
        PageInfo<SysDict> page = new PageInfo<>(sysDictList);
        return page;
    }


    /**
     * 编辑字典
     *
     * @param sysDict
     * @return
     */
    public ResultInfo toEdit(SysDict sysDict) {

        SysUser sysUser = sysUserService.getUser();
        sysDict.setUpdate_user(sysUser.getId());
        sysDict.setUpdate_time(new Date());
        updateById(sysDict);

        SysDict sysDictData = selectById(sysDict);

        // 更新缓存
        cacheDataService.updateDictCache(sysDictData);


        return ResultInfo.success();
    }


    /**
     * 增加字典
     *
     * @param sysDict
     * @return
     */
    public ResultInfo toAdd(SysDict sysDict) {
        SysUser sysUser = sysUserService.getUser();
        sysDict.setCreate_time(new Date());
        sysDict.setIs_del("0");
        sysDict.setCreate_user(sysUser.getId());
        insert(sysDict);

        // 更新缓存
        cacheDataService.updateDictCache(sysDict);

        return ResultInfo.success();
    }

    public Map<String,String> getLabel(String dict_type){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dict_type);
        queryWrapper.eq("is_del", 0);
        List<SysDict> sysDictList = selectList(queryWrapper);
        Map<String,String> sysDictMap = sysDictList.stream().collect(Collectors.toMap(SysDict::getDc_value,SysDict::getLabel));
        return sysDictMap;
    }

    public Map<String,String> getValue(String dict_type){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dict_type);
        queryWrapper.eq("is_del", 0);
        List<SysDict> sysDictList = selectList(queryWrapper);
        Map<String,String> sysDictMap = sysDictList.stream().collect(Collectors.toMap(SysDict::getLabel,SysDict::getDc_value));
        return sysDictMap;
    }
}
