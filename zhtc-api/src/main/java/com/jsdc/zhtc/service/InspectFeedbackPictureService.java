package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.InspectFeedbackPictureDao;
import com.jsdc.zhtc.mapper.InspectFeedbackPictureMapper;
import com.jsdc.zhtc.model.InspectFeedbackPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: InspectFeedbackPictureService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class InspectFeedbackPictureService extends BaseService<InspectFeedbackPictureDao, InspectFeedbackPicture> {

    @Autowired
    private InspectFeedbackPictureMapper pictureMapper;

    /**
     * create by zonglina at 2022/1/13 16:27
     * description:
     * //根据巡检员id获取所有图片
     *
     * @return : null
     * @param:null
     */
    public List<String> selectByInspectFeedbackPid(Integer inspect_feedback_id) {
        return pictureMapper.selectByInspectFeedbackPid(inspect_feedback_id);
    }
}
