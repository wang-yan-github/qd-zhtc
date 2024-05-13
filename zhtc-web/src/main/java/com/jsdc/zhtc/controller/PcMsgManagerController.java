package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.*;
import com.jsdc.zhtc.vo.MsgManagerVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: PcMsgManagerController
 * Description:
 * date: 2022/2/16 11:05
 *
 * @author bn
 */
@Controller
@RequestMapping("pcMsg")
public class PcMsgManagerController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperateAppealService operateAppealService;

    @Autowired
    private CarnoAppealService carnoAppealService;

    @Autowired
    private OperateFeedbackService operateFeedbackService;

    @Autowired
    private InspectFeedbackService inspectFeedbackService;


    @RequestMapping(value = "toManager.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo toMsgManager(MsgManagerVo msgManagerVo) {
        // 判断当前人员身份
        SysUser sysUser = sysUserService.getUser();

        System.out.println(sysUserService.isModel());

        if (sysUserService.isModel().getData().equals("1")) { // 停车场页面
            // 停车场管理员
            if (sysUser.getUser_type().equals("1")) {
                msgManagerVo.setModel("1");
                msgManagerVo.setPark_id(sysUser.getPark_id());
                msgManagerVo.setCard_no("0");
                //申诉订单
                msgManagerVo.setOperate_order_num(operateAppealService.census(msgManagerVo));

            } else {// 停车场巡检员
                msgManagerVo.setModel("1");
                msgManagerVo.setCard_no("2");
                //申诉订单
                msgManagerVo.setOperate_order_num(operateAppealService.census(msgManagerVo));
                // 申诉车牌
                msgManagerVo.setOperate_car_no_num(carnoAppealService.census());
                // 车主反馈建议
                msgManagerVo.setOperate_feedback_num(operateFeedbackService.census());
            }

        } else {
//            if (sysUser.getUser_type().equals("0")){ // 路段页面，路段身份
            msgManagerVo.setModel("0");
            msgManagerVo.setCard_no("1");
            //申诉订单
            msgManagerVo.setOperate_order_num(operateAppealService.census(msgManagerVo));
            // 申诉车牌
            msgManagerVo.setOperate_car_no_num(carnoAppealService.census());
            // 车主反馈建议
            msgManagerVo.setOperate_feedback_num(operateFeedbackService.census());
            // 巡检员上报
            msgManagerVo.setInspect_feedback_num(inspectFeedbackService.census());
//            }
        }


        return ResultInfo.success(msgManagerVo);
    }


}
