package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.ParkingGateUtils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.service.*;
import com.jsdc.zhtc.vo.ResultInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 临时通信证记录
 */
@Controller
@RequestMapping("/provisionalPassRecord")
public class ProvisionalPassRecordController extends BaseController {

    @Autowired
    private ProvisionalPassService provisionalPassService;
    @Autowired
    private ProvisionalPassRecordService provisionalPassRecordService;
    @Autowired
    private OperateCarnoService operateCarnoService;
    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private ParkService parkService;
    @Autowired
    private ParkingGateUtils parkingGateUtils;

    /**
     * 扫二维码后跳转页面
     */
    @RequestMapping("getUrl")
    public String getUrl(Integer id, Model model) throws IOException {
        if (StringUtils.isNotNull(id)) {
            model.addAttribute("id", id);
        }
        return "provisionalPassIndex";
    }

    /**
     * 验证该临时通行证
     * 单次核销停车券已被使用或多次核销停车券次数为0时，则提示本免费停车券已失效。
     */
    @RequestMapping(value = "checkCode", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo checkCode(@RequestParam Integer id) throws IOException {
        if (StringUtils.isNotNull(id)) {
            ProvisionalPass provisionalPass = provisionalPassService.selectById(id);
            if (notEmpty(provisionalPass)) {
                if (provisionalPass.getIs_del().equals("1") || provisionalPass.getIs_use().equals("0")) {
                    return ResultInfo.error("本免费停车券已失效");
                }
                Date date1 = new Date();
                Date date2 = provisionalPass.getExpire_time();
                int result = date1.compareTo(date2);
                if (result > 0) {
                    System.out.println("当前时间晚于失效时间");
                    return ResultInfo.error("本免费停车券已失效");
                }
                if (provisionalPass.getHxCount() == 0) {
                    System.out.println("剩余核销次数为0");
                    return ResultInfo.error("本免费停车券已失效");
                }
                Park park = parkService.selectById(provisionalPass.getPark_id());
                provisionalPass.setPark_name(park.getPark_name());
                return ResultInfo.success(provisionalPass);
            }
        } else {
            return ResultInfo.error("未找到该临时通行证");
        }
        return ResultInfo.error("本免费停车券已失效");
    }

    /**
     * 根据车牌号码查询停车订单
     */
    @RequestMapping(value = "searchOrder", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo searchOrder(@RequestParam String parkId, @RequestParam String carNo, @RequestParam String type) throws IOException {
        if (StringUtils.isNotEmpty(carNo)) {
            List<OperateCarno> carnoList = operateCarnoService.selectList(new QueryWrapper<OperateCarno>()
                    .eq("car_no", carNo)
                    .eq("car_type", type)
                    .eq("is_del", "0"));
            if (carnoList.size() > 0) {
                List<String> statusList = new ArrayList<>();
                statusList.add("1");
                statusList.add("2");
                List<ParkingOrder> parkingOrderList = parkingOrderService.selectList(new QueryWrapper<ParkingOrder>()
                        .eq("carno_id", carnoList.get(0).getId())
                        .eq("park_id", parkId)
                        .in("status", statusList)
                        .eq("is_del", "0")
                        .orderByDesc("drivein_time"));
                if (parkingOrderList.size() > 0) {
                    ParkingOrder parkingOrder = parkingOrderList.get(0);
                    if (StringUtils.isNotNull(parkingOrder.getResitime())) {
                        parkingOrder.setStayTime(TimeUtils.formatTime(parkingOrderList.get(0).getResitime()));
                    } else {
                        parkingOrder.setStayTime("0");
                    }
                    parkingOrder.setCarNo(carNo);
                    return ResultInfo.success(parkingOrder);
                }
            }
        }
        return ResultInfo.error(carNo + "没有找到入场记录，不能使用优惠券。");
    }

    /**
     * 提交临时通行证绑定车牌号码
     */
    @RequestMapping(value = "onSave", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo onSave(Integer id, Integer orderId) throws IOException {
        if (StringUtils.isNotNull(id)) {
            ProvisionalPass provisionalPass = provisionalPassService.selectById(id);
            if (notEmpty(provisionalPass)) {
                if (provisionalPass.getHxCount() == 0) {
                    return ResultInfo.error("本免费停车券已失效");
                }
                ParkingOrder parkingOrder = parkingOrderService.selectById(orderId);
                if (notEmpty(parkingOrder)) {
                    //重复类型	同一车牌仅可使用一次 0：否 1：是
                    if (provisionalPass.getRepeat_type().equals("1")) {
                        List<ParkingOrder> parkingOrderList = parkingOrderService.selectList(new QueryWrapper<ParkingOrder>()
                                .eq("carno_id", parkingOrder.getCarno_id())
                                .eq("provisionalPassId", provisionalPass.getId())
                                .eq("is_del", "0"));
                        if (parkingOrderList.size() > 0) {
                            return ResultInfo.error("该车牌号码无法重复使用本免费停车券");
                        }
                    }

                    if (notEmpty(parkingOrder.getProvisionalPassId()) && parkingOrder.getProvisionalPassId().equals(provisionalPass.getId())) {
                        return ResultInfo.error("该订单已使用过本免费停车券");
                    }

                    parkingOrder.setProvisionalPassId(provisionalPass.getId());
                    parkingOrder.setTempDriveOutTime(new DateTime().plusMinutes(provisionalPass.getLimit_time()).toDate());
                    parkingOrder.setFree_type(GlobalData.FREETYPELSTX);

                    //更新停车订单
                    parkingOrderService.updateById(parkingOrder);

                    provisionalPass.setHxCount(provisionalPass.getHxCount() - 1);
                    //更新临时通行证剩余次数和有效无效状态
                    provisionalPassService.updateById(provisionalPass);

                    ProvisionalPassRecord provisionalPassRecord = new ProvisionalPassRecord();
                    provisionalPassRecord.setPark_id(parkingOrder.getPark_id());
                    provisionalPassRecord.setParkOrder_id(parkingOrder.getId());
                    provisionalPassRecord.setCar_id(parkingOrder.getCarno_id());
                    provisionalPassRecord.setUse_time(new Date());
                    provisionalPassRecord.setTemporaryInfo_id(provisionalPass.getId());
                    provisionalPassRecord.setCreate_time(new Date());
                    provisionalPassRecord.setUpdate_time(new Date());
                    provisionalPassRecord.setIs_del("0");
                    provisionalPassRecordService.insert(provisionalPassRecord);

                    Park park = parkService.selectById(parkingOrder.getPark_id());
                    if (Base.notEmpty(park)) {
                        if (!park.getBrand().equals("dc")) {
                            //百胜闸机
//                                parkingGateUtils.wxopenGate(park.getPark_code(), parkDeviceList.get(0).getChannel_id());
                        } else {
                            //鼎驰闸机 停车订单待缴费且有驶离时间
                            if (parkingOrder.getStatus().equals("2")
                                    && notEmpty(parkingOrder.getDriveout_time())
                                    && notEmpty(parkingOrder.getDriveout_gate())) {
                                parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                                parkingOrderService.updateById(parkingOrder);
                                parkingGateUtils.mqttOpenGate(parkingOrder.getDriveout_gate(), "一路顺风");
                            }
                        }
                    }
                    return ResultInfo.success();
                } else {
                    return ResultInfo.error("未找到停车订单");
                }
            } else {
                return ResultInfo.error("未找到该临时通行证");
            }
        } else {
            return ResultInfo.error("未找到该临时通行证");
        }
    }

}
