package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.CwMwlDataDao;
import com.jsdc.zhtc.mapper.CwMwlDataMapper;
import com.jsdc.zhtc.model.CwMwlData;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author thr
 * @create 2023-01-17 16:42:01
 */
@Service
@Transactional
public class CwMwlDataService extends BaseService<CwMwlDataDao, CwMwlData> {

    @Autowired
    private CwMwlDataMapper cwMwlDataMapper;


    public PageInfo<CwMwlData> toList(Integer pageIndex, Integer pageSize, CwMwlData beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<CwMwlData> cwMwlDataVos = cwMwlDataMapper.toList(beanParam);

        PageInfo<CwMwlData> page = new PageInfo<>(cwMwlDataVos);

        return page;
    }

    public List<CwMwlData> getList(CwMwlData beanParam) {

        List<CwMwlData> cwMwlDataVos = cwMwlDataMapper.toList(beanParam);

        return cwMwlDataVos;
    }

    public List<CwMwlData> getHourMwlList(CwMwlData beanParam) {

        List<CwMwlData> cwMwlDataVos = cwMwlDataMapper.getHourMwlList(beanParam);

        return cwMwlDataVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<CwMwlData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        CwMwlData cwMwlData = selectOne(queryWrapper);
        return ResultInfo.success(cwMwlData);
    }

    /**
     * 添加
     */
    public ResultInfo addCwMwlData(CwMwlData bean) {
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
