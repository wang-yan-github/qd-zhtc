package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.payPolymerize.TokenUtil;
import com.jsdc.zhtc.payPolymerize.UMSConfig;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.vo.ParkingOrderVo;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.order.AppealRecordVo;
import com.jsdc.zhtc.vo.order.PaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @projectName: zhtc
 * @className: ScanController
 * @author: wp
 * @description:
 * @date: 2023/5/31 9:20
 */
@RequestMapping("scan")
@Controller
public class ScanController {
    @Autowired
    ParkingOrderService parkingOrderService;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Value("${qrcode_url}")
    String qrcodeUrl;

    @RequestMapping("getUrl")
    public void getUrl(Integer parkId, HttpServletResponse response) throws IOException {
        String url = qrcodeUrl + "/scan/toIndex?parkId=" + parkId;
        response.sendRedirect(url);
    }

    @RequestMapping("toIndex")
    public String toIndex(int parkId, Model model) {
        model.addAttribute("parkId", parkId);
        return "/scan/index";
    }

    @RequestMapping("toOrder")
    public String toOrder(Integer parkId, String carNo, Integer carType, Model model) {

        List<ParkingOrderVo> list = parkingOrderService.getOrderByParkAndCarNo(parkId, carType, carNo);
        model.addAttribute("parkId", parkId);
        model.addAttribute("carNo", carNo);
        model.addAttribute("carType", carType);
        model.addAttribute("list", list);
        return "/scan/orderlist";
    }

    @RequestMapping("getOrder")
    @ResponseBody
    public ResultInfo getOrder(Integer parkId, String carNo, Integer carType) {
        List<ParkingOrderVo> list = parkingOrderService.getOrderByParkAndCarNo(parkId, carType, carNo);
        return ResultInfo.success(list);
    }

    @RequestMapping("pay")
    public void pay(Integer orderId, HttpServletResponse response) throws IOException {
        ParkingOrder parkingOrder = parkingOrderService.selectById(orderId);
        PaymentVo pay = parkingOrderService.getParkingCharging(new AppealRecordVo(String.valueOf(parkingOrder.getId()), new Date()));
        BigDecimal sumAmount = new BigDecimal(pay.getPay_money());
        //把应付金额转为分，int型
        int fen = sumAmount.multiply(new BigDecimal(100)).intValue();
        String qrcode = "";
        //获取accesstoken
        String accessToken = redisTemplate.opsForValue().get(UMSConfig.REDIS_TOKEN).toString();
        String result = tokenUtil.getStopAmountQrcode(accessToken, orderId, fen);
        qrcode = JSONObject.parseObject(result).getString("billQRCode");
        response.sendRedirect(qrcode);
    }
}
