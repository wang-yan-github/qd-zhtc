package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ParkingOrderVo;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.RoadParkListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ClassName: RoadParkingOrderController
 * Description:停车场停车订单管理表(parking_order)
 * date: 2020/01/13 10:00
 *
 * @author thr
 */
@Controller
@RequestMapping("/parkingOrder")
public class ParkingOrderController extends BaseController {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 流动车支付流水
     *
     * @param pageIndex
     * @param pageSize
     * @param parkOrderVo
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, ParkingOrderVo parkOrderVo) {


        PageInfo<ParkingOrderVo> page = parkingOrderService.toList(pageIndex, pageSize, parkOrderVo);

        return ResultInfo.success(page);
    }

    /**
     * 流动车流水统计
     *
     * @param parkingOrderVo
     * @return
     */
    @RequestMapping(value = "toCount.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toCount(ParkingOrderVo parkingOrderVo) {
        return ResultInfo.success(parkingOrderService.toCount(parkingOrderVo));
    }

    /**
     * 导出excel
     *
     * @param parkingOrderVo
     */
    @RequestMapping(value = "exportExcel.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportExcel(ParkingOrderVo parkingOrderVo, HttpServletResponse response) {
        parkingOrderService.exportExcel(parkingOrderVo, response);
    }

    // 停车场订单信息
    @RequestMapping(value = "/page.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo page(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, RoadParkListVo vo) {
        vo.setType(GlobalData.PARKING_TYPE_PLAT); //默认停车场
        vo.setId(sysUserService.getUser().getPark_id());
        vo.setUserType(sysUserService.getUser().getUser_type());
        PageInfo pageInfo = parkingOrderService.selectWXPage(pageIndex, pageSize, vo);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 出场收费列表
     * Author wzn
     * Date 2022/1/28 15:24
     */
    @RequestMapping(value = "/appearanceFee", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo appearanceFee(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, RoadParkListVo roadParkListVo) {
        SysUser sysUser = sysUserService.getUser();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", sysUser.getId());
        SysUser sysUser1 = sysUserService.selectOne(queryWrapper);
        roadParkListVo.setPlaceId(sysUser1.getPark_id());
        PageInfo pageInfo = parkingOrderService.appearanceFee(pageIndex, pageSize, roadParkListVo);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 出场收费详情
     * Author wzn
     * Date 2022/1/28 15:24
     */
    @RequestMapping(value = "/appearanceFeeInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo appearanceFeeInfo(RoadParkListVo roadParkListVo) {
        RoadParkListVo parkListVo = parkingOrderService.appearanceFeeInfo(roadParkListVo);
        return ResultInfo.success(parkListVo);
    }


    //详情
    @RequestMapping(value = "/orderDetails.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo orderDetails(Integer id) {
        return ResultInfo.success(parkingOrderService.orderDetails(id));
    }

    /**
     * 根据订单id查询与之相关的合并订单信息
     *
     * @param orderId
     * @return
     */
    @RequestMapping("getMergeOrder.do")
    @ResponseBody
    public ResultInfo getMergeOrder(Integer orderId) {
        return ResultInfo.success(parkingOrderService.getMergeOrder(orderId));
    }


    /**
     * 现金核销列表
     * 分页查询
     * thr
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "selectHxPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo selectHxPageList(Integer pageIndex, Integer pageSize, ParkingOrderVo bean) {
        return parkingOrderService.selectHxPageList(bean, pageIndex, pageSize);
    }

    /**
     * 现金核销
     * 全部核销
     * thr
     *
     * @return
     */
    @RequestMapping("hxAll.do")
    @ResponseBody
    @LogInfo(LogEnums.LOG_INVOICECASH)
    public ResultInfo hxAll(Integer xjyId, String startDate, String endDate) {
        return parkingOrderService.hxAll(xjyId, startDate, endDate);
    }


    /**
     * 描 述： TODO(根据条件获取订单)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link com.jsdc.zhtc.vo.ResultInfo}
     */
    @PostMapping("/getParkingOrder")
    @ResponseBody
    public ResultInfo getParkingOrder(@RequestBody CommonVo data) {

        List<ParkingOrder> lists = parkingOrderService.getParkingOrder(data);
        return ResultInfo.success(lists);
    }

    /**
     * 描 述： TODO(根据条件生成吊起地址)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping("/payPlaceOrder")
    @ResponseBody
    public ResultInfo payPlaceOrder(@RequestBody CommonVo data) {

        try {
            return parkingOrderService.payPlaceOrder(data);
        } catch (Exception e) {
            return ResultInfo.error(null, e.getMessage());
        }

    }

    /**
     * 停车场现金收费
     *
     * @param
     * @return
     */
    @RequestMapping("/cashShoufei")
    @ResponseBody
    public ResultInfo cashShoufei(Integer id) {
        ResultInfo resultInfo = parkingOrderService.tccCashShoufei(id);
        return resultInfo;
    }

    //修改车牌号
    @RequestMapping(value = "updateByCar.json", method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_UPDCARNO)
    public ResultInfo updateByCar(Integer id, Integer car_id, String car_no, String car_type) {
        return parkingOrderService.updateByCar(id, car_id, car_no, car_type);
    }


    //结束计时
    @RequestMapping(value = "closureParkingOrder.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo closureParkingOrder(ParkingOrder bean) {
        return parkingOrderService.finishOrder(bean.getId(), bean.getDriveout_time());
    }

    /**
     * 停车场订单删除
     */
    @RequestMapping(value = "del.json", method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_DELETEPARKINGORDER)
    public ResultInfo del(RoadParkListVo vo) {
        return parkingOrderService.parkOrderDel(vo);
    }

    /**
     * 人工放行
     */
    @RequestMapping(value = "updStatus.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updStatus(RoadParkListVo vo) {
        return parkingOrderService.updStatus(vo);
    }

    /**
     * 支付记录关联 路段和停车场订单信息
     */
    @RequestMapping(value = "getOrderDataByPaymentId.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getOrderDataByPaymentId(RoadParkListVo vo) {
        List<RoadParkListVo> list = parkingOrderService.getOrderDataByPaymentId(vo);
        return ResultInfo.success(list);
    }
}
