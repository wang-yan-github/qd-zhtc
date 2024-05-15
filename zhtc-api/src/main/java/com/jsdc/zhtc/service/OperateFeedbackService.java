package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.OperateFeedbackDao;
import com.jsdc.zhtc.mapper.OperateFeedbackMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.vo.OperateFeedbackVo;
import com.jsdc.zhtc.vo.ReportVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: OperateFeedbackService
 * Description:反馈管理表
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class OperateFeedbackService extends BaseService<OperateFeedbackDao, OperateFeedback> {
    @Autowired
    private FeedbackPictureService feedbackPictureService;
    @Autowired
    private OperateFeedbackMapper mapper;
    @Autowired
    private FeedbackReplyService replyService;
    @Autowired
    private FileManageService fileManageService;
    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;

    /**
     * create by zonglina at 2021/12/31 14:31
     * description:分页查询功能
     *
     * @return : null
     * @param:null
     */
    //分页查询
    public PageInfo<OperateFeedbackVo> selectPageList(Integer pageIndex, Integer pageSize, OperateFeedbackVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        return new PageInfo<>(mapper.selectPageList(bean));
    }


    /**
     * 微信小程序
     * 新增反馈信息
     * 1、待回复 2、已回复
     *
     * @author thr
     */
    public ResultInfo save(OperateFeedback bean) {
        bean.setCreate_time(new Date());
        bean.setFeedback_time(new Date());
        bean.setFeedback_status(1);
        bean.setCreate_user(bean.getMember_id());
        bean.setIs_del(0);
        if (insert(bean) > 0) {
            //反馈图片存储
            feedbackPictureService.save(bean.getId(), bean.getFiles());
            return ResultInfo.success("反馈成功,等待回复！");
        } else {
            return ResultInfo.error("反馈失败！");
        }
    }


    /**
     * create by zonglina at 2021/12/31 14:30
     * 根据id修改回复状态,并生成反馈回复信息feedback_reply
     *
     * @param id
     * @param reply_content
     * @return
     */
    public ResultInfo updFeedBack(Integer id, String reply_content) {
        OperateFeedback feedback = selectById(id);
        feedback.setFeedback_status(2);//已回复
        feedback.setUpdate_time(new Date());
        if (updateById(feedback) > 0) {
            replyService.save(new FeedbackReply(id, reply_content), feedback.getMember_id());
            return ResultInfo.success("操作成功！");
        } else {
            return ResultInfo.error("操作失败！");
        }
    }

    /**
     * create by zonglina at 2021/12/31 14:31
     * description:详情展示
     *
     * @return : null
     * @param:null
     */
    public ResultInfo detailsByFeedBack(OperateFeedback bean) {
        bean = selectById(bean.getId());

        QueryWrapper<FeedbackPicture> wrapper = new QueryWrapper<>();
        wrapper.eq("feedback_id", bean.getId());
        List<FeedbackPicture> piclist = feedbackPictureService.selectList(wrapper);
        List<String> fileList = new ArrayList<>();
        for (FeedbackPicture feedbackPicture : piclist) {
            FileManage fileManage = fileManageService.selectById(feedbackPicture.getPicture_id());
            if (Base.notEmpty(fileManage)) {
                fileList.add(loadPicPath2 + fileManage.getFile_url());
            }
        }
        //文件路径列表
        bean.setFileList(fileList);
        //回复内容列表
        List<FeedbackReply> replies = replyService.selectList(new QueryWrapper<FeedbackReply>().eq("feedback_id", bean.getId()));
        bean.setReplyList(replies);
        return ResultInfo.success(bean);
    }

    public Integer census() {
        QueryWrapper<OperateFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("feedback_status", 1);
        queryWrapper.eq("is_del", 0);

        return selectList(queryWrapper).size();
    }


    //反馈导出
    public void exportOperateAppeal(OperateFeedbackVo bean, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<OperateFeedbackVo> list = mapper.selectPageList(bean);
        list.forEach(a -> {
            if (1 == a.getFeedback_status()) {
                a.setFeedback_status_name("待回复");
            } else {
                a.setFeedback_status_name("已回复");
            }
        });
        writer.addHeaderAlias("feedback_content", "内容");
        writer.addHeaderAlias("phone", "用户账号");
        writer.addHeaderAlias("carno", "车牌号码");
        writer.addHeaderAlias("feedback_time", "反馈时间");
        writer.addHeaderAlias("feedback_status_name", "状态");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("反馈建议.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 近七日服务类型趋势 车主反馈 最近7日/数量
     */
    public List<ReportVo> getDaysCount() {
        return mapper.getDaysCount();
    }

}
