package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.service.InvoicesProvideRecordService;
import com.jsdc.zhtc.vo.InvoicesProvideRecordVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: InvoicesProvideRecordController
 * Description: 发票发放记录
 * date: 2022/1/17 11:45
 *
 * @author bn
 */
@Controller
@RequestMapping("invoicesProvideRecord")
public class InvoicesProvideRecordController extends BaseController {

    @Autowired
    private InvoicesProvideRecordService recordService;


    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param record
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, InvoicesProvideRecordVo record) {

        System.out.println(record.getStart_time());
        PageInfo<InvoicesProvideRecordVo> page = recordService.toList(pageIndex, pageSize, record);

        return ResultInfo.success(page);
    }


}
