package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.PatrolRechargeManagement;
import com.jsdc.zhtc.service.PatrolRechargeManagementService;
import com.jsdc.zhtc.vo.PatrolRechargeManagementVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Controller
@RequestMapping("/patrolRechargeManagement")
public class PatrolRechargeManagementController extends BaseController {

    @Autowired
    private PatrolRechargeManagementService patrolRechargeManagementService;

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param vo
     * @return
     */
    @RequestMapping("/queryPatrolRechargeManagement.json")
    @ResponseBody
    public ResultInfo queryPatrolRechargeManagement(@RequestParam(defaultValue = "1") Integer pageIndex,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    PatrolRechargeManagementVo vo) {
        return patrolRechargeManagementService.listPatrolRechargeManagement(pageIndex, pageSize, vo);
    }

    /**
     * 页面表头统计
     *
     * @param vo
     * @return
     */
    @RequestMapping("/count.json")
    @ResponseBody
    public ResultInfo count(PatrolRechargeManagementVo vo) {
        return patrolRechargeManagementService.countPatrolRechargeManagement(vo);
    }

    /**
     * 充值验证
     *
     * @param password 密码
     * @param code     验证码
     * @return
     */
    @RequestMapping("/rechargeVerification.json")
    @ResponseBody
    public ResultInfo rechargeVerification(String password, String code) {
        return patrolRechargeManagementService.getInspectPwdCode(password, code);
    }

    /**
     * 巡检充值
     *
     * @param vo
     * @return
     */
    @RequestMapping("patrolRecharge.json")
    @ResponseBody
    @LogInfo(LogEnums.LOG_INVOICECASH)
    public ResultInfo patrolRecharge(PatrolRechargeManagementVo vo) {
        return patrolRechargeManagementService.insertPatrolRechargeManagement(vo);
    }

    /**
     * 发票领取管理
     *
     * @param PatrolRechargeManagementId
     * @return
     */
    @RequestMapping("invoiceReceiving.json")
    @ResponseBody
    @LogInfo(LogEnums.LOG_INVOICECASH)
    public ResultInfo invoiceReceiving(Integer PatrolRechargeManagementId) {
        return patrolRechargeManagementService.saveInvoiceReceiving(PatrolRechargeManagementId);
    }

    /**
     * 编辑
     *
     * @param patrolRechargeManagement
     * @return
     */
    @RequestMapping("/editPatrolRechargeManagement.json")
    @ResponseBody
    public ResultInfo editPatrolRechargeManagement(PatrolRechargeManagement patrolRechargeManagement) {
        return patrolRechargeManagementService.savePatrolRechargeManagement(patrolRechargeManagement);
    }

    /**
     * 删除
     *
     * @param PatrolRechargeManagementId
     * @return
     */
    @RequestMapping("/deletePatrolRechargeManagement.json")
    @ResponseBody
    public ResultInfo deletePatrolRechargeManagement(@RequestParam(defaultValue = "0") Integer PatrolRechargeManagementId) {
        return patrolRechargeManagementService.removePatrolRechargeManagement(PatrolRechargeManagementId);
    }

    /**
     * 巡检充值统计
     *
     * @param vo
     * @return
     */
    @RequestMapping("/InspectionRechargeStatistics.json")
    @ResponseBody
    public ResultInfo InspectionRechargeStatistics(@RequestParam(defaultValue = "1") Integer pageIndex,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   PatrolRechargeManagementVo vo) {
        if (StringUtils.isNotEmpty(vo.getGroup()) && vo.getGroup().equals("people")) {
            return patrolRechargeManagementService.countPatrolRechargeManagementInject(pageIndex, pageSize, vo);
        } else {
            return patrolRechargeManagementService.countPatrolRechargeManagementTime(pageIndex, pageSize, vo);
        }
    }

}
