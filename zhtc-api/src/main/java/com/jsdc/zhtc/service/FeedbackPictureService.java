package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.FeedbackPictureDao;
import com.jsdc.zhtc.model.FeedbackPicture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: FeedbackPictureService
 * Description:反馈图片表
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class FeedbackPictureService extends BaseService<FeedbackPictureDao, FeedbackPicture> {


    /**
     * create by zonglina at 2021/12/31 14:41
     * description:反馈图片存储
     *
     * @return : null
     * @param:null
     */
    public void save(Integer feedback_id, String files) {
        //图片多个需要循环，实际情况结合页面（还需要图片id的保存，在进行存储图片id）
        if (null != files && !"".equals(files)) {
            String[] ids = files.split(",");
            for (String id : ids) {
                FeedbackPicture pic = new FeedbackPicture();
                pic.setPicture_id(Integer.valueOf(id));
                pic.setFeedback_id(feedback_id);
                insert(pic);
            }
        }
    }
}
