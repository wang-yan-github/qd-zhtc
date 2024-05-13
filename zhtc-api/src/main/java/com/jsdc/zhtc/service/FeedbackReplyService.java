package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.FeedbackReplyDao;
import com.jsdc.zhtc.model.FeedbackReply;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ClassName: FeedbackReplyService
 * Description:反馈回复表
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class FeedbackReplyService extends BaseService<FeedbackReplyDao, FeedbackReply> {


    /**
     * create by zonglina at 2021/12/31 14:37
     * description:反馈回复表
     * 插入，标题，消息内容，类型，接收人（微信车主id）
     *
     * @return : null
     * @param:null
     */
    public ResultInfo save(FeedbackReply bean, Integer member_id) {
        bean.setReply_time(new Date());
        if (insert(bean) > 0) {

            return ResultInfo.success("回复成功！");
        } else {
            return ResultInfo.error("回复失败！");
        }
    }
}
