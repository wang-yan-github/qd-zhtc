package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.RechargeManagementPicDao;
import com.jsdc.zhtc.mapper.RechargeManagementPicMapper;
import com.jsdc.zhtc.model.RechargeManagementPic;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author thr
 * @create 2022-08-24 17:54:50
 */
@Service
@Transactional
public class RechargeManagementPicService extends BaseService<RechargeManagementPicDao, RechargeManagementPic> {

    @Autowired
    private RechargeManagementPicMapper rechargeManagementPicMapper;

    public PageInfo<RechargeManagementPic> toList(Integer pageIndex, Integer pageSize, RechargeManagementPic beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<RechargeManagementPic> rechargeManagementPicVos = rechargeManagementPicMapper.toList(beanParam);

        PageInfo<RechargeManagementPic> page = new PageInfo<>(rechargeManagementPicVos);

        return page;
    }

    public List<RechargeManagementPic> getList(RechargeManagementPic beanParam) {

        List<RechargeManagementPic> rechargeManagementPicVos = rechargeManagementPicMapper.toList(beanParam);

        return rechargeManagementPicVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<RechargeManagementPic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        RechargeManagementPic rechargeManagementPic = selectOne(queryWrapper);
        return ResultInfo.success(rechargeManagementPic);
    }


}
