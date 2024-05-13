package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.InspectFeedbackDao;
import com.jsdc.zhtc.model.InspectFeedback;
import com.jsdc.zhtc.vo.InspectFeedbackVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface InspectFeedbackMapper extends BaseMapper<InspectFeedback> {

    @SelectProvider(type = InspectFeedbackDao.class, method = "selectPageList")
    List<InspectFeedbackVo> selectPageList(InspectFeedbackVo bean, String redisRoadOrPark);

    @SelectProvider(type = InspectFeedbackDao.class, method = "selectInspectFeedback")
    InspectFeedbackVo selectInspectFeedback(InspectFeedbackVo bean, String redisRoadOrPark);

    @SelectProvider(type = InspectFeedbackDao.class, method = "selectByFeedbackId")
    InspectFeedbackVo selectByFeedbackId(Integer id);
}
