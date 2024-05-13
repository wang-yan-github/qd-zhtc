package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.service.ParkingReleaseService;
import com.jsdc.zhtc.vo.ParkingReleaseVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClassName: ParkingReleaseController
 * Description:上线收费配置表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/parkingRelease")
public class ParkingReleaseController extends BaseController {

    @Autowired
    private ParkingReleaseService parkingReleaseService;

    //分页查询功能
    @RequestMapping(value = "selectPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo selectPageList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, ParkingReleaseVo bean) {
        List<ParkingReleaseVo> list = parkingReleaseService.selectPageList(pageIndex, pageSize, bean);
        return ResultInfo.success(new PageInfo<>(list));
    }

    //新增、修改保存方法
    @RequestMapping(value = "save.json")
    @ResponseBody
    public ResultInfo save(ParkingReleaseVo bean) {
        return parkingReleaseService.save(bean);
    }


    //删除功能||启用||禁用功能功能
    @RequestMapping(value = "delete.json")
    @ResponseBody
    public Integer delete(ParkingReleaseVo bean, Integer status) {
        return parkingReleaseService.deleteByRelease(bean, status);
    }


}
