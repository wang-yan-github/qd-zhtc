package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.service.CarnoAppealService;
import com.jsdc.zhtc.vo.CarnoAppealVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Controller
@RequestMapping("/carnoAppeal")
public class CarnoAppealController extends BaseController {

    @Autowired
    private CarnoAppealService carnoAppealService;

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param vo
     * @return
     */
    @RequestMapping("/queryCarnoAppeal.json")
    @ResponseBody
    public ResultInfo queryCarnoAppeal(@RequestParam(defaultValue = "1") Integer pageIndex,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       CarnoAppealVo vo) {
        return carnoAppealService.listCarnoAppeal(pageIndex, pageSize, vo);
    }

    /**
     * 申诉处理
     *
     * @param vo
     * @return
     */
    @RequestMapping("/complaintHandling.json")
    @ResponseBody
    @LogInfo(LogEnums.LOG_UPDCARNO)
    public ResultInfo ComplaintHandling(CarnoAppealVo vo) {
        return carnoAppealService.saveComplaint(vo);
    }
}
