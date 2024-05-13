package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.CwZzlDataDao;
import com.jsdc.zhtc.mapper.CwZzlDataMapper;
import com.jsdc.zhtc.model.CwZzlData;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author thr
 * @create 2023-01-18 11:00:08
 */
@Service
@Transactional
public class CwZzlDataService extends BaseService<CwZzlDataDao, CwZzlData> {

    @Autowired
    private CwZzlDataMapper cwZzlDataMapper;


    public PageInfo<CwZzlData> toList(Integer pageIndex, Integer pageSize, CwZzlData beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<CwZzlData> cwZzlDataVos = cwZzlDataMapper.toList(beanParam);

        PageInfo<CwZzlData> page = new PageInfo<>(cwZzlDataVos);

        return page;
    }

    public List<CwZzlData> getList(CwZzlData beanParam) {

        List<CwZzlData> cwZzlDataVos = cwZzlDataMapper.toList(beanParam);

        return cwZzlDataVos;
    }


    /**
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     */
    public List<CwZzlData> getDaysZzlList(CwZzlData beanParam) {

        List<CwZzlData> cwZzlDataVos = cwZzlDataMapper.getDaysZzlList(beanParam);

        return cwZzlDataVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<CwZzlData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        CwZzlData cwZzlData = selectOne(queryWrapper);
        return ResultInfo.success(cwZzlData);
    }

    /**
     * 添加
     */
    public ResultInfo addCwZzlData(CwZzlData bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        if (Base.empty(bean.getBerthCount())) {
            bean.setBerthCount(0);
        }
        insert(bean);
        return ResultInfo.success();
    }


}
