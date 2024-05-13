package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.InspectFeedbackPictureDao;
import com.jsdc.zhtc.model.InspectFeedbackPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface InspectFeedbackPictureMapper extends BaseMapper<InspectFeedbackPicture> {

    @SelectProvider(type = InspectFeedbackPictureDao.class, method = "selectByInspectFeedbackPid")
    List<String> selectByInspectFeedbackPid(Integer inspect_feedback_id);
}
