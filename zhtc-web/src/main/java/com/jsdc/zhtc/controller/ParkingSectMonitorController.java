package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.StatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类 名: 停车场实时监控
 * 描 述: ParkingSectMonitorController
 * 作 者: lw
 * 创 建：2022/1/24 17:33
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("/parkingsectmonitor")
public class ParkingSectMonitorController {

    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ParkService parkService;

    /**
     * 描 述： TODO(查询订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/getOrderReceivable")
    public ResultInfo getOrderReceivable(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        String orderReceivable = parkingOrderService.getOrderReceivable(data);

        return ResultInfo.success(orderReceivable);
    }


    /**
     * 描 述： TODO(查询订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/getOrderSumAmount")
    public ResultInfo getOrderSumAmount(@RequestBody CommonVo data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) != 0) {
                return ResultInfo.success("0.0");
            }
            if (data.getVariance3() == null) {
                data.setVariance3(park_id);
            }
        }
        String orderReceivable = parkingOrderService.getOrderSumAmount(data);
        return ResultInfo.success(orderReceivable);
    }

    /**
     * 描 述： TODO(查询车位总数、剩余数、使用率、周转率)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/berthUtilizeInfo")
    public ResultInfo berthUtilizeInfo(@RequestBody CommonVo data) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        HashMap<String, String> map = new HashMap<>();

        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) != 0) {
                map.put("arrearsBerthNum", "0");
                map.put("berthCount", "0");
                map.put("utilizeRatio", "--");
                map.put("ky", "--");
                map.put("turnover", "--");
                return ResultInfo.success(map);
            }
            if (data.getVariance3() == null) {
                data.setVariance3(park_id);
            }
        }

        //查询欠费停车数
        data.setStr(dateStr);
        String arrearsBerthNum = parkingOrderService.getArrearageParkCount(data);
        map.put("arrearsBerthNum", arrearsBerthNum);

        //查询停车位总数
        String berthCount = parkService.getBerthCount(data);
        map.put("berthCount", berthCount);

        //查询在停总车数
        data.setStr2(GlobalData.PARKING_ORDER_STOP);
        Long parkingOrderCount = parkingOrderService.getParkingOrderCount(data);
        //判断设置使用率、空余车位数
        if (berthCount.equals("0")) {
            map.put("utilizeRatio", "--");
            map.put("ky", "--");
        } else {
            if (parkingOrderCount <= 0) {
                map.put("utilizeRatio", "0.0");
                map.put("ky", "0");
            } else {
                BigDecimal divide = ArithmeticUtils.divide(parkingOrderCount, berthCount, 4, BigDecimal.ROUND_HALF_UP);
                map.put("utilizeRatio", ArithmeticUtils.setScale1(ArithmeticUtils.multiply(divide, "100"), 2, BigDecimal.ROUND_HALF_UP).toString());
                BigDecimal subtract = ArithmeticUtils.subtract(berthCount, parkingOrderCount);
                map.put("ky", subtract.toString());
            }
        }
        //查询今日订单总数
        data.setStr2(null);
        Long parkingOrderCount1 = parkingOrderService.getParkingOrderCount(data);
        //判断设置周转率
        if (berthCount.equals("0")) {
            map.put("turnover", "--");
        } else {
            if (parkingOrderCount <= 0) {
                map.put("turnover", "0.0");
            } else {
                BigDecimal divide = ArithmeticUtils.divide(parkingOrderCount1, berthCount, 2, BigDecimal.ROUND_HALF_UP);
                map.put("turnover", divide.toString());
            }
        }
        return ResultInfo.success(map);
    }

    /**
     * 描 述： TODO(查询会员停车书非会员停车数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/getHyFhyParkingOrderCount")
    public ResultInfo getHyFhyParkingOrderCount(@RequestBody CommonVo data) {
        HashMap<String, Object> map = new HashMap<>();
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();
        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) != 0) {
                map.put("orderCount", "0");
                map.put("memberOrderNum", "0");
                map.put("outsiderOrderNum", "0");
                return ResultInfo.success(map);
            }
            if (data.getVariance3() == null) {
                data.setVariance3(park_id);
            }
        }
        data.setStr2(GlobalData.PARKING_ORDER_STOP);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data.setStr(sdf.format(new Date()));
        List<StatisticsVo> memberIdCount = parkingOrderService.getMemberIdCount(data);
        //会员
        StatisticsVo hy = memberIdCount.get(0);
        //非会员
        StatisticsVo fhy = memberIdCount.get(1);
        map.put("memberOrderNum", hy.getSumAmount());
        map.put("outsiderOrderNum", fhy.getSumAmount());
//        map.put("outsiderOrderNum" , ArithmeticUtils.subtract(hy , fhy));
        return ResultInfo.success(map);
    }


    /**
     * 描 述： TODO( 收款统计 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/getCollectionsStatistics")
    public ResultInfo getCollectionsStatistics(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        data.setStr(dateStr);
        List<Map<String, Object>> lists = parkingOrderService.getCollectionsStatistics(data, park_id);
        return ResultInfo.success(lists);
    }

    /**
     * 描 述： TODO(查询车位总数、在停车总数、剩余数、使用率)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/getParkingStatistics")
    public ResultInfo getParkingStatistics(@RequestBody CommonVo data) {

        HashMap<String, Object> map = new HashMap<>();

        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) != 0) {
                map.put("berthCount", "0");
                map.put("utilizeRatio", "--");
                map.put("ky", "0");
                map.put("orderCount", "0");
                return ResultInfo.success(map);
            }
            if (data.getVariance3() == null) {
                data.setVariance3(park_id);
            }
        }

        //查询停车位总数
        String berthCount = parkService.getBerthCount(data);
        map.put("berthCount", berthCount);

        //查询在停总车数
        data.setStr2(GlobalData.PARKING_ORDER_STOP);
        Long parkingOrderCount = parkingOrderService.getParkingOrderCount(data);
        //判断设置使用率、空余车位数
        if (berthCount.equals("0")) {
            map.put("utilizeRatio", "--");
            map.put("ky", "0");
            map.put("orderCount", "0");
        } else {
            if (parkingOrderCount <= 0) {
                map.put("utilizeRatio", "0.0");
                map.put("ky", "0");
                map.put("orderCount", "0");
            } else {
                BigDecimal divide = ArithmeticUtils.divide(parkingOrderCount, berthCount, 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal multiply = ArithmeticUtils.multiply(divide, "100");
                map.put("utilizeRatio", ArithmeticUtils.setScale1(multiply, 2, BigDecimal.ROUND_HALF_UP).toString());
                BigDecimal ky = ArithmeticUtils.subtract(berthCount, parkingOrderCount);
                map.put("ky", ky.toString());
                map.put("orderCount", new BigDecimal(berthCount).subtract(ky));
            }
        }
        //在停数量
        data.setStr2(GlobalData.PARKING_ORDER_STOP);
//        Long orderCount = parkingOrderService.getOrderCount(data);
//        map.put("orderCount" , orderCount);

        return ResultInfo.success(map);
    }


    /**
     * 描 述： TODO(查询停车场订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link  ResultInfo}
     */
    @PostMapping("/getParkDeviceInfo")
    public ResultInfo getParkDeviceInfo(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();
        ResultInfo parkDeviceInfo = parkingOrderService.getParkDeviceInfo(data, park_id);
        return parkDeviceInfo;
    }
}
