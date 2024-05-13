package com.jsdc.zhtc.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.bean.InitApplication;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.HttpUtils;
import com.jsdc.zhtc.common.utils.ParkingGateUtils;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.model.SysLog;
import com.jsdc.zhtc.service.ParkDeviceService;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.service.SysLogService;
import com.jsdc.zhtc.vo.ParkDeviceVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ClassName: ParkDeviceController <br/>
 * Description: <br/>
 * date: 2022/1/13 11:15<br/>
 *
 * @author bn<br   />
 */
@Controller
@RequestMapping("parkDevice")
public class ParkDeviceController extends BaseController {

    @Autowired
    private ParkDeviceService parkDeviceService;

    @Autowired
    private ParkingGateUtils parkingGateUtils;

    @Autowired
    private InitApplication initApplication;

    @Autowired
    private ParkService parkService;

    @Autowired
    private SysLogService sysLogService;

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param parkDeviceVo
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, ParkDeviceVo parkDeviceVo) {


        PageInfo<ParkDeviceVo> page = parkDeviceService.toList(pageIndex, pageSize, parkDeviceVo);

        return ResultInfo.success(page);
    }

    /**
     * 获取离线报警设备列表
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getList(ParkDeviceVo parkDeviceVo) {
        return ResultInfo.success(parkDeviceService.getList(parkDeviceVo));
    }

    /**
     * 列表查询
     *
     * @param parkDeviceVo
     * @return
     * @author bn
     */
    @RequestMapping(value = "getParkDevice.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getParkDevice(ParkDeviceVo parkDeviceVo) {


        ParkDeviceVo deviceVo = parkDeviceService.getParkDevice(parkDeviceVo);

        return ResultInfo.success(deviceVo);
    }


    /**
     * 添加设备信息
     *
     * @param device
     * @return
     * @author bn
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(ParkDevice device) {

        return parkDeviceService.toAdd(device);
    }

    /**
     * 编辑设备信息
     *
     * @param device
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(ParkDevice device) {

        return parkDeviceService.toEdit(device);
    }

    /**
     * 批量删除设备
     *
     * @param deviceIds
     * @return
     */
    @RequestMapping(value = "delDeviceAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delDeviceAll(String deviceIds) {
        return parkDeviceService.delAll(deviceIds);
    }


    /**
     * 返回支付二维码
     *
     * @param gateCode
     * @return
     * @author wh
     */
    @RequestMapping(value = "getQrCode.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getQrCode(String gateCode, String parkCode) {
        return parkDeviceService.getQrCode(gateCode, parkCode);
    }


    /**
     * 停车场设备导出
     *
     * @return
     */
    @RequestMapping(value = "/exportEquipment.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportEquipment(ParkDeviceVo parkDeviceVo, HttpServletResponse response) {
        parkDeviceService.exportEquipment(parkDeviceVo, response);
    }

    @RequestMapping(value = "getDeviceTree.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getDeviceTree() {
        return ResultInfo.success(parkDeviceService.getDeviceTree());
    }

    /**
     * 人工开闸
     */
    @RequestMapping(value = "openGate", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo openGate(String deviceCode) {
        try {
            List<ParkDevice> parkDeviceList = parkDeviceService.selectList(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getDevice_code, deviceCode).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
            if (CollectionUtil.isNotEmpty(parkDeviceList)) {
                Park park = parkService.selectById(parkDeviceList.get(0).getPark_id());
                if (Base.notEmpty(park)) {
                    //鼎驰闸机
                    parkingGateUtils.mqttOpenGate(deviceCode, "一路顺风");

                    // 通道口 0：出口 1.入口
                    String content = "人工开闸，" + park.getPark_name();
                    if (parkDeviceList.get(0).getPassageway().equals("1")) {
                        content = content + "，入口闸机设备编号：" + parkDeviceList.get(0).getDevice_code();
                    } else {
                        content = content + "，出口闸机设备编号：" + parkDeviceList.get(0).getDevice_code();
                    }
                    //退款日志
                    SysLog sysLog = new SysLog();
                    sysLog.setLog_type(LogEnums.LOG_GATEOPEN.getValue());
                    sysLog.setLog_content(content);
                    sysLogService.saveLog(sysLog);

                    /**
                     * 判断该停车场是否开启限流
                     * 限流开关 0开启 1关闭
                     */
                    if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                        if (notEmpty(park.getFree_count())) {
                            // 通道口 0：出口 1.入口
                            if (parkDeviceList.get(0).getPassageway().equals("1")) {
                                //车辆驶入更新空闲车位-1
                                if (park.getFree_count() > 0) {
                                    park.setFree_count(park.getFree_count() - 1);
                                }
                            } else {
                                //车辆驶离更新空闲车位+1
                                park.setFree_count(park.getFree_count() + 1);
                            }
                            parkService.updateById(park);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ResultInfo.error("开闸失败");
        }
        return ResultInfo.success("开闸成功");
    }

    /**
     * 动态订阅主题
     */
    @RequestMapping(value = "addTopic.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addTopic(String deviceCode) {
//        List<ParkDevice> parkDeviceList = parkDeviceService.selectList(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getPark_id, bean.getId()).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
//        if (CollectionUtil.isNotEmpty(parkDeviceList)) {
//            List<String> deviceCodeList = parkDeviceList.stream().map(x -> x.getDevice_code()).collect(Collectors.toList());
//            return initApplication.addTopic(deviceCodeList);
//        }

        List<ParkDevice> parkDeviceList = parkDeviceService.selectList(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getDevice_code, deviceCode).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
        if (CollectionUtil.isNotEmpty(parkDeviceList)) {
            Park park = parkService.selectById(parkDeviceList.get(0).getPark_id());
            if (Base.notEmpty(park)) {
                if (!park.getBrand().equals("dc")) {
                    return ResultInfo.error("非鼎驰闸机设备无法订阅主题");
                }
            }
        }
        String param = "deviceCode=" + deviceCode;
        //zhtc1服务器 mqtt订阅接口路径 线上地址：http://2.69.254.21:8083
        String result = HttpUtils.sendPost("http://2.69.254.21:8083" + "/mqtt/addTopic", param);
//        String result = HttpUtils.sendPost( "http://192.168.0.104:8081"+ "/mqtt/addTopic", param);
        ResultInfo resultInfo = JSON.parseObject(result, ResultInfo.class);
        return resultInfo;

//        //本地调试 mqtt负载均衡
//        String result = HttpUtils.sendPost( "http://192.168.0.104:8081"+ "/mqtt/addTopic", param);
//
//        int code = 0;
//        String msg = "订阅主题成功";
//        ResultInfo resultInfo = JSON.parseObject(result, ResultInfo.class);
//        if (resultInfo.getCode() == -1) {
//            code = -1;
//            msg = "104服务器，订阅主题失败";
//        }
//
//        String result2 = HttpUtils.sendPost( "http://192.168.0.172:8081"+ "/mqtt/addTopic", param);
//        ResultInfo resultInfo2 = JSON.parseObject(result2, ResultInfo.class);
//        if (Base.empty(resultInfo2) || resultInfo2.getCode() == -1) {
//            code = -1;
//            msg = " 172服务器，订阅主题失败";
//        }
//
//        ResultInfo resultInfo3 = new ResultInfo();
//        resultInfo3.setCode(code);
//        resultInfo3.setMsg(msg);
//
//        return resultInfo3;
    }

}
