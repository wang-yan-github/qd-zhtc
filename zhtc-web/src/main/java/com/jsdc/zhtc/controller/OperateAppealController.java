package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.*;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.RoadOrParkingCommentVo;
import com.jsdc.zhtc.vo.RoadParkListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * create by zonglina at 2022/1/4 16:07
 * description:申诉管理表
 *
 * @param:null
 * @return : null
 */
@Controller
@RequestMapping("/operateappeal")
public class OperateAppealController extends BaseController {

    @Autowired
    private OperateAppealService appealService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AppealNoticeFeedbackService noticeFeedbackService;
    @Autowired
    private InspectManageService inspectManageService;


    @Autowired
    private ParkingOrderService parkingOrderService;

    /**
     * create by zonglina at 2022/1/14 11:05
     * description:根据车牌号、创建时间查询，当前订单号信息
     * 申诉录入分页
     *
     * @return : null
     * @param:null
     */
    @RequestMapping(value = "selectPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo selectPageList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, RoadParkListVo bean) {
        return appealService.selectByParkingOrderPage(bean, pageIndex, pageSize);
    }


    /**
     * create by zonglina at 2022/1/14 14:05
     * description:
     * 分页列表
     *
     * @return : ResultInfo
     * @param:null
     */
    @RequestMapping(value = "selectByPage.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo selectByPage(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, RoadOrParkingCommentVo bean) {
//        if (GlobalData.PARKING_TYPE_PLAT.equals(sysUserService.redisRoadOrPark())) {
            SysUser user = sysUserService.getUser();
            bean.setPark_id(sysUserService.getUser().getPark_id());
            bean.setUserType(sysUserService.getUser().getUser_type());//停车场
//        }
        return appealService.selectByPage(bean, pageIndex, pageSize);
    }


    /**
     * 申诉订单导出功能
     *
     * @return
     */
    @RequestMapping(value = "/exportOperateAppeal.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportOperateAppeal(RoadOrParkingCommentVo bean, HttpServletResponse response) {
        appealService.exportOperateAppeal(bean, response);
    }
}
