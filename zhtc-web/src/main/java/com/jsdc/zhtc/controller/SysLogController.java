package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.SysLog;
import com.jsdc.zhtc.service.SysLogService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: SysLogController
 * Description:
 * date: 2022/1/8 20:58
 *
 * @author wp
 */
@RestController
@RequestMapping("log")
public class SysLogController {
    @Autowired
    private SysLogService logService;

    /**
     * create by wp at 2022/1/10 8:43
     * description: 分页查询日志信息
     *
     * @param log
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getPage.do")
    public ResultInfo getPage(SysLog log,
                              @RequestParam(defaultValue = "1") Integer pageIndex,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return logService.getPage(log, pageIndex, pageSize);
    }

    @RequestMapping("getUserList.do")
    public ResultInfo getUserList() {
        return logService.getUserList();
    }
}
