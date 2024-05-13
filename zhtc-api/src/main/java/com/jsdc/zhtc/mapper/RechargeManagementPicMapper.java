package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.RechargeManagementPicDao;
import com.jsdc.zhtc.model.RechargeManagementPic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author thr
 * @create 2022-08-24 17:54:50
 */
@Mapper
public interface RechargeManagementPicMapper extends BaseMapper<RechargeManagementPic> {

    @SelectProvider(method = "toList", type = RechargeManagementPicDao.class)
    List<RechargeManagementPic> toList(RechargeManagementPic beanParam);
}
