package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.AppealNoticeFeedback;
import com.jsdc.zhtc.service.AppealNoticeFeedbackService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: AppealNoticeFeedbackService
 * Description:申诉通知反馈
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Controller
@RequestMapping("/appealNoticeFeedback")
public class AppealNoticeFeedbackController extends BaseController {

    @Autowired
    private AppealNoticeFeedbackService feedbackService;


    /**
     * create by wp at 2022/1/14 14:00
     * description:  根据巡检员id或状态分页查询
     *
     * @param inspectId
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getList.json")
    @ResponseBody
    public ResultInfo getList(Integer inspectId,
                              String status,
                              @RequestParam(defaultValue = "1") Integer pageIndex,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return feedbackService.getList(inspectId, status, pageIndex, pageSize);
    }

    /**
     * create by wp at 2022/1/14 14:18
     * description: 巡检员反馈
     *
     * @param feedback
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("edit.json")
    @ResponseBody
    public ResultInfo edit(AppealNoticeFeedback feedback) {
        return feedbackService.edit(feedback);
    }
}
