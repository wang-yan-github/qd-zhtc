package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.AttendanceManagementPic;
import com.jsdc.zhtc.service.AttendanceManagementPicService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author thr
 * @create 2022-08-30 10:38:29
 */
@Controller
@RequestMapping("/attendanceManagementPic")
public class AttendanceManagementPicController extends BaseController {

    @Autowired
    private AttendanceManagementPicService attendanceManagementPicService;


    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, AttendanceManagementPic beanParam) {
        PageInfo<AttendanceManagementPic> page = attendanceManagementPicService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     * ID查询
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id) {
        return attendanceManagementPicService.getById(id);
    }

    /**
     * 添加
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addAttendanceManagementPic(AttendanceManagementPic attendanceManagementPic) {

        return attendanceManagementPicService.addAttendanceManagementPic(attendanceManagementPic);
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editAttendanceManagementPic(AttendanceManagementPic attendanceManagementPic) {

        return attendanceManagementPicService.editAttendanceManagementPic(attendanceManagementPic);
    }
}
