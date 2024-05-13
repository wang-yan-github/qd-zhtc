package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.OperateFeedback;
import com.jsdc.zhtc.service.OperateFeedbackService;
import com.jsdc.zhtc.vo.OperateFeedbackVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: OperateFeedbackController
 * Description:反馈管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/operatefeedback")
public class OperateFeedbackController extends BaseController {

    @Autowired
    private OperateFeedbackService feedbackService;

    //分页查询功能
    @RequestMapping(value = "selectPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo selectPageList(OperateFeedbackVo bean, @RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResultInfo.success(feedbackService.selectPageList(pageIndex, pageSize, bean));
    }


    //反馈新增功能
    @RequestMapping(value = "save.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(OperateFeedback bean) {
        return feedbackService.save(bean);
    }

    //详情获取
    @RequestMapping(value = "details.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo details(OperateFeedback bean) {
        return feedbackService.detailsByFeedBack(bean);
    }


    /**
     * create by zonglina at 2022/1/10 16:33
     * description:反馈回复新增功能
     * 反馈表状态更新，反馈回复表新增
     *
     * @return : null
     * @param:null
     */
    @RequestMapping(value = "updFeedBack.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updFeedBack(Integer id, String reply_content) {
        return feedbackService.updFeedBack(id, reply_content);
    }


    /**
     * 反馈导出
     *
     * @return
     */
    @RequestMapping(value = "/exportOperateAppeal.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportOperateAppeal(OperateFeedbackVo vo, HttpServletResponse response) {
        feedbackService.exportOperateAppeal(vo, response);
    }

}
