package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.AttendanceManagement;
import com.jsdc.zhtc.service.AttendanceManagementService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 考勤管理
 *
 * @Author thr
 * @create 2022-08-24 14:42:07
 */
@Controller
@RequestMapping("/attendanceManagement")
public class AttendanceManagementController extends BaseController {

    @Autowired
    private AttendanceManagementService attendanceManagementService;
    @Autowired
    private SysUserService sysUserService;


    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, AttendanceManagement beanParam) {
        // 类型 0 路段 1停车场
        beanParam.setType("1");
        PageInfo<AttendanceManagement> page = attendanceManagementService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     * ID查询
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id) {
        return attendanceManagementService.getById(id);
    }

    /**
     * 导出考勤记录
     */
    @RequestMapping(value = "/downExcel")
    public void downExcel(AttendanceManagement beanParam, HttpServletResponse response) {
        attendanceManagementService.downExcel(beanParam, response);
    }

    /**
     * 考勤统计
     */
    @RequestMapping(value = "toPageReport.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toReport(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, AttendanceManagement beanParam) {
        PageInfo<AttendanceManagement> page = attendanceManagementService.toPageReport(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

    /**
     * 导出考勤记录
     */
    @RequestMapping(value = "/reportDownExcel")
    public void reportDownExcel(AttendanceManagement beanParam, HttpServletResponse response) {
        attendanceManagementService.reportDownExcel(beanParam, response);
    }


}
