package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.OperateAppealDao;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.vo.OperateAppealVo;
import com.jsdc.zhtc.vo.ReportVo;
import com.jsdc.zhtc.vo.RoadOrParkingCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface OperateAppealMapper extends BaseMapper<OperateAppeal> {


    //分页
    @SelectProvider(type = OperateAppealDao.class, method = "selectByPage")
    List<OperateAppealVo> selectByPage(RoadOrParkingCommentVo bean, String redisRoadOrPark);


    /**
     * 近七日服务类型趋势 订单申诉 最近7日/数量
     */
    @SelectProvider(type = OperateAppealDao.class, method = "getDaysCount")
    List<ReportVo> getDaysCount();

    /**
     * 申诉订单申诉状态统计
     */
    @SelectProvider(type = OperateAppealDao.class, method = "getAppealStatusCount")
    List<ReportVo> getAppealStatusCount(RoadOrParkingCommentVo bean, String redisRoadOrPark);
}
