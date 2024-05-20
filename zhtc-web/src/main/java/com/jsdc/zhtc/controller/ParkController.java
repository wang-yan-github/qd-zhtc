package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TrafficControlUtils;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.service.*;
import com.jsdc.zhtc.vo.ParkVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: ParkController <br/>
 * Description: 停车场<br/>
 * date: 2021/12/30 10:59<br/>
 *
 * @author bn<br />
 */
@Controller
@RequestMapping("/park")
public class ParkController extends BaseController {


    @Autowired
    private ParkService parkService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CacheDataService cacheDataService;

    @Autowired
    private TrafficControlUtils trafficControlUtils;

    @Autowired
    private WhiteListService whiteListService;

    @Autowired
    private TimeLimitService timeLimitService;

    @Autowired
    private OperateCarnoService operateCarnoService;

    @Autowired
    private SysDictService sysDictService;


    Logger logger = LoggerFactory.getLogger(ParkController.class);

    /**
     * 停车场列表查询
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, Park park) {


        if (StringUtils.isNotEmpty(park.getPark_grades())) {
            park.setPark_grades(this.getsql(park.getPark_grades()));
        }


        PageInfo<ParkVo> page = parkService.toList(pageIndex, pageSize, park);

        return ResultInfo.success(page);
    }


    /**
     * 添加停车场
     *
     * @param park
     * @return
     * @author bn
     */
    @RequestMapping(value = "/toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(Park park) {
        park.setIs_del("0");
        park.setStatus("0");
        //限流开关 0开启 1关闭
        park.setOn_off("1");
        park.setCreate_time(new Date());
        park.setCreate_user(sysUserService.getUser().getId());

        //普通停车场-上传交控
        if ("3".equals(park.getPark_grade())) {
            parkService.uploadParkingInfo(park);
        }
        parkService.insert(park);

        if ("2".equals(park.getPark_grade())) {
                //地下 白名单 先删除之前数据
                QueryWrapper<WhiteList> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("park_id", park.getId());
                whiteListService.delete(queryWrapper);

                //新增数据
                if (CollectionUtils.isNotEmpty(park.getWhiteList())) {
                    for (String s : park.getWhiteList()) {
                        WhiteList whiteList = new WhiteList();
                        whiteList.setPark_id(park.getId());
                        whiteList.setCar_id(Integer.valueOf(s));
                        whiteList.insert();
                    }
                }
        } else if ("1".equals(park.getPark_grade())) {
                //限时 限时类型 先删除之前数据
                QueryWrapper<TimeLimit> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("park_id", park.getId());
                timeLimitService.delete(queryWrapper);

                //新增数据
                if (CollectionUtils.isNotEmpty(park.getTimeLimitList())) {
                    for (String s : park.getTimeLimitList()) {
                        TimeLimit timeLimit = new TimeLimit();
                        timeLimit.setPark_id(park.getId());
                        timeLimit.setDict_id(Integer.valueOf(s));
                        timeLimit.insert();
                    }
                }


        }

        cacheDataService.updateLocationCache(park);
        cacheDataService.updChargeByRoad_ParkId(park.getId());
        String msg = "添加成功";

        return ResultInfo.successMsg(msg);
    }


    /**
     * @param streetId
     * @return
     */
    @RequestMapping("getAll.do")
    @ResponseBody
    public ResultInfo getAll(Integer streetId) {
        Park park = new Park();
        park.setStreet_id(streetId);
        return ResultInfo.success(parkService.toList(park));
    }

    /**
     * 停车场管理导出
     *
     * @return
     */
    @RequestMapping(value = "/exportParking.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportParking(Park vo, HttpServletResponse response) {
        parkService.exportParking(vo, response);
    }


    /**
     * 编辑停车场信息
     *
     * @author wzn
     * @date 2024/5/17 11:42
     */
    @RequestMapping(value = "/toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(@RequestBody Park park) {
        park.setUpdate_time(new Date());
        park.setUpdate_user(sysUserService.getUser().getId());


        //普通停车场-上传交控
        if ("3".equals(park.getPark_grade())) {
            parkService.uploadParkingInfo(park);
        }
        parkService.updateById(park);

        if ("2".equals(park.getPark_grade())) {
                //地下 白名单 先删除之前数据
                QueryWrapper<WhiteList> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("park_id", park.getId());
                whiteListService.delete(queryWrapper);

                //新增数据
                if (CollectionUtils.isNotEmpty(park.getWhiteList())) {
                    for (String s : park.getWhiteList()) {
                        WhiteList whiteList = new WhiteList();
                        whiteList.setPark_id(park.getId());
                        whiteList.setCar_id(Integer.valueOf(s));
                        whiteList.insert();
                    }
                }
        } else if ("1".equals(park.getPark_grade())) {
                //限时 限时类型 先删除之前数据
                QueryWrapper<TimeLimit> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("park_id", park.getId());
                timeLimitService.delete(queryWrapper);

                //新增数据
                if (CollectionUtils.isNotEmpty(park.getTimeLimitList())) {
                    for (String s : park.getTimeLimitList()) {
                        TimeLimit timeLimit = new TimeLimit();
                        timeLimit.setPark_id(park.getId());
                        timeLimit.setDict_id(Integer.valueOf(s));
                        timeLimit.insert();
                    }
                }


        }


        Park parkData = parkService.selectById(park);

        cacheDataService.updateLocationCache(park);
        cacheDataService.updChargeByRoad_ParkId(parkData.getId());

        String msg = "修改成功";

        return ResultInfo.successMsg(msg);
    }


    /**
     * 停车场详情接口
     *
     * @author wzn
     * @date 2024/5/17 11:47
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public Park info(String id) {
        Park park = parkService.selectById(id);
        if (null != park) {
            //查已选白名单
            QueryWrapper<WhiteList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("park_id", park.getId());
            List<WhiteList> whiteLists = whiteListService.selectList(queryWrapper);
            List<OperateCarno> operateCarnos = new ArrayList<>();
            List<SysDict> sysDictList = new ArrayList<>();

            //查限免车辆类型
            QueryWrapper<TimeLimit> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("park_id", park.getId());
            List<TimeLimit> timeLimits = timeLimitService.selectList(queryWrapper2);


            if (CollectionUtils.isNotEmpty(timeLimits)) {
                for (TimeLimit t : timeLimits) {
                    SysDict sysDict = sysDictService.selectById(t.getDict_id());
                    if (null != sysDict) {
                        sysDictList.add(sysDict);
                    }

                }
            }
            park.setOperateCarnoList(operateCarnos);
            park.setSysDictList(sysDictList);
        }
        return park;
    }


    /**
     * 批量删除停车场
     *
     * @param parkIds
     * @return
     */
    @RequestMapping(value = "delParkAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delParkAll(String parkIds) {
        return parkService.delAll(parkIds);
    }


    public String getsql(String str) {
        StringBuffer sb = new StringBuffer();
        String[] temp = str.split(",");
        for (int i = 0; i < temp.length; i++) {
            if (!"".equals(temp[i]) && temp[i] != null)
                sb.append("'" + temp[i] + "',");
        }
        String result = sb.toString();
        String tp = result.substring(result.length() - 1, result.length());
        if (",".equals(tp))
            System.out.println(result.substring(0, result.length() - 1));
        else
            System.out.println(result);
        return result;
    }


}
