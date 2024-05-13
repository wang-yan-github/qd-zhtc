package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.AjaxResult;
import com.jsdc.zhtc.model.InspectFeedback;
import com.jsdc.zhtc.service.InspectFeedbackService;
import com.jsdc.zhtc.vo.InspectFeedbackVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName: InspectManageController
 * Description:巡检反馈管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/inspectFeedback")
public class InspectFeedbackController extends BaseController {

    @Autowired
    private InspectFeedbackService feedbackService;

    //分页查询功能
    @RequestMapping(value = "selectPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult selectPageList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, InspectFeedbackVo bean) {
        List<InspectFeedbackVo> list = feedbackService.selectPageList(pageIndex, pageSize, bean);
        return AjaxResult.success(new PageInfo<>(list));
    }

    //详情功能
    @RequestMapping(value = "details.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo details(Integer id) {
        return feedbackService.details(id);
    }

    //反馈回复功能
    @RequestMapping(value = "feedbackContent.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo feedbackContent(InspectFeedback bean) {
        return feedbackService.feedbackContent(bean);
    }

    //反馈功能
    @RequestMapping(value = "feedback.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo feedback(@RequestParam("file") MultipartFile[] files, InspectFeedback bean) {
        return feedbackService.feedback(files, bean);
    }

    //删除
    @RequestMapping(value = "delete.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delete(InspectFeedback bean) {
        return feedbackService.delete(bean);
    }
}
