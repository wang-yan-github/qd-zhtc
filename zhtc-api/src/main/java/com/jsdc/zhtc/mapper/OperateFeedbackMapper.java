package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.OperateFeedbackDao;
import com.jsdc.zhtc.model.OperateFeedback;
import com.jsdc.zhtc.vo.OperateFeedbackVo;
import com.jsdc.zhtc.vo.ReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface OperateFeedbackMapper extends BaseMapper<OperateFeedback> {


    @SelectProvider(type = OperateFeedbackDao.class, method = "selectPageList")
    List<OperateFeedbackVo> selectPageList(OperateFeedbackVo bean);

    /**
     * 近七日服务类型趋势 车主反馈 最近7日/数量
     */
    @SelectProvider(type = OperateFeedbackDao.class, method = "getDaysCount")
    List<ReportVo> getDaysCount();
}
