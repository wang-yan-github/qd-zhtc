package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.TrafficControlUtils;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.service.CacheDataService;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ParkVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ParkController <br/>
 * Description: 停车场<br/>
 * date: 2021/12/30 10:59<br/>
 *
 * @author bn<br                               />
 */
@Controller
@RequestMapping("park")
public class ParkController extends BaseController {


    @Autowired
    private ParkService parkService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CacheDataService cacheDataService;

    @Autowired
    private TrafficControlUtils trafficControlUtils;

    Logger logger = LoggerFactory.getLogger(ParkController.class);

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param park
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, Park park) {


        PageInfo<ParkVo> page = parkService.toList(pageIndex, pageSize, park);

        return ResultInfo.success(page);
    }


    /**
     * 接口传参封装方法
     * 3.1.1 停车场基本信息上报接口
     * postType 上报类型 必填 1-新增 2-修改返回参数
     * 名称	数据类型	说明
     * code	int	成功标识（0/-1）
     */
    public ResultInfo setParkInfo(Park park, Integer postType) {
        Map<String, Object> map = new HashMap<>();
        map.put("postType", postType);
        //停车场编号,必传
        map.put("thirdParkCode", park.getPark_code());
        //停车场名称 必填
        map.put("thirdParkName", park.getPark_name());
        map.put("longitude", park.getLongitude().trim());
        map.put("latitude", park.getLatitude().trim());
        map.put("address", park.getAddress());
        //是否支持包月（1-是；2-否）必填
        map.put("isPayMonth", 1);
        map.put("totalPlaceCount", park.getPark_num());
        //上报交控
        return trafficControlUtils.postParkInfo(map);
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
}
