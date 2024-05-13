package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.service.AppealHandleRecordService;
import com.jsdc.zhtc.service.OperateAppealService;
import com.jsdc.zhtc.vo.AppealHandleRecordVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * create by zonglina at 2022/1/4 16:07
 * description:申诉处理记录表
 *
 * @param:null
 * @return : null
 */
@Controller
@RequestMapping("/appealHandleRecord")
public class AppealHandleRecordController {

    @Autowired
    private AppealHandleRecordService recordService;
    @Autowired
    private OperateAppealService operateAppealService;


    //停车场-审批功能(路段不存在此功能)
    @RequestMapping(value = "parkApprove.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo parkApprove(AppealHandleRecordVo bean) {
        return recordService.parkApprove(bean);
    }


    //停车场点击处理申诉更改为锁定状态避免岗亭和运营端同时操作
    @RequestMapping(value = "upApprove.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo upApprove(Integer id) {
        OperateAppeal operateAppeal = operateAppealService.selectById(id);
        if (null != operateAppeal) {
            if (null == operateAppeal.getIs_verify() || "0".equals(operateAppeal.getIs_verify())) {
                operateAppeal.setIs_verify("1");
                Integer count = operateAppealService.updateById(operateAppeal);
                return ResultInfo.success(count);
            } else {
                operateAppeal.setIs_verify("0");
                int count = operateAppealService.updateById(operateAppeal);
                return ResultInfo.success(count);
            }
        } else {
            return ResultInfo.error("申诉处理中....");
        }
    }
}
