package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.InspectFeedbackPicture;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InspectFeedbackDao
 * Description:巡检反馈图片表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class InspectFeedbackPictureDao extends BaseDao<InspectFeedbackPicture> {

    //根据巡检员id获取所有图片
    public String selectByInspectFeedbackPid(Integer inspect_feedback_id) {
        String sql = "select picture_id from inspect_feedback_picture mag";
        if (notEmpty(inspect_feedback_id)) {
            sql += " where mag.inspect_feedback_id  =" + inspect_feedback_id;
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }
}
