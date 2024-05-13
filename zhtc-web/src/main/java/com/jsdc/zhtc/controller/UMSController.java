package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.ParkingGateUtils;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.payPolymerize.Token;
import com.jsdc.zhtc.payPolymerize.TokenUtil;
import com.jsdc.zhtc.payPolymerize.UMSConfig;
import com.jsdc.zhtc.payPolymerize.dto.BillPayment;
import com.jsdc.zhtc.payPolymerize.dto.NotifyDto;
import com.jsdc.zhtc.service.*;
import com.jsdc.zhtc.vo.ResultInfo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @projectName: zhtc
 * @className: UMSController
 * @author: wp
 * @description:
 * @date: 2023/5/24 10:47
 */
@Controller
@RequestMapping("/ums")
public class UMSController {

    Logger logger = LoggerFactory.getLogger(UMSController.class);

    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    ParkingOrderService parkingOrderService;
    @Autowired
    ParkService parkService;
    @Autowired
    ParkingGateUtils parkingGateUtils;
    @Autowired
    PaymentOrderService paymentOrderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    OperateCarnoService carnoService;
    @Autowired
    ParkDeviceService parkDeviceService;


    /**
     * 在停订单场内码聚合支付
     * 支付回调接口
     * 示例：
     * 支付宝：billPayment={"buyerUsername":"1873013","payTime":"2023-05-24 11:45:58","buyerCashPayAmt":1,"paySeqId":"34212077876N","invoiceAmount":1,"settleDate":"2023-05-24","buyerId":"2088422404512952","receiptAmount":1,"totalAmount":1,"couponAmount":0,"billBizType":"bills","buyerPayAmount":1,"targetOrderId":"2023052422001412951436771952","payDetail":"支付宝余额支付0.01元。","merOrderId":"34ZZ23052487545531144982950","status":"TRADE_SUCCESS","targetSys":"Alipay 2.0"}&qrCodeId=34ZZ2305247403826114496779&billDesc=徐州经济技术开发区金瑞房地产经营有限公司&aI=EZkv&sign=89ACC056AD15E8D3DE464AA47F944F98870596BA740F4ED73AFB9298E0DC1524&merName=徐州经济技术开发区金瑞房地产经营有限公司&mid=89832536513127K&billDate=2023-05-24&qrCodeType=ANYPAY&mchntUuid=2d9081bd879e548d0187e5cedffc4b5b&tid=X2S0Q7J4&instMid=QRPAYDEFAULT&srcReserve=经开区智慧停车&receiptAmount=1&totalAmount=1&createTime=2023-05-24 11:45:53&billStatus=PAID&cardAttr=BALANCE&signType=SHA256&notifyId=5c903ecb-edea-47ba-9ecd-b0a62611cf06&billNo=34ZZ2305248754553114498295&subInst=100200&seqId=34212077876N&billQRCode=https://qr.chinaums.com/bills/qrCode.doid=34ZZ2305247403826114496779
     * 微信：  billDesc=徐州经济技术开发区金瑞房地产经营有限公司&sign=E6A766C98177709B5D961A3B0FC12BC4F2F204DF3CDBE0A591B37A7CCBC4F2C0&merName=徐州经济技术开发区金瑞房地产经营有限公司&mid=89832536513127K&qrCodeType=ANYPAY&mchntUuid=2d9081bd879e548d0187e5cedffc4b5b&wc=BMPH&tid=X2S0Q7J4&instMid=QRPAYDEFAULT&srcReserve=经开区智慧停车&receiptAmount=1&billStatus=PAID&cardAttr=BALANCE&signType=SHA256&billNo=34ZZ2305249134203132543962&seqId=34213486630N&billQRCode=https://qr.chinaums.com/bills/qrCode.doid=34ZZ2305247403826114496779&bankInfo=OTHERS&billPayment={"payTime":"2023-05-24 13:42:08","buyerCashPayAmt":1,"paySeqId":"34213486630N","invoiceAmount":1,"settleDate":"2023-05-24","buyerId":"otdJ_uA7RTT4rX9eZZgTtGhplAxs","receiptAmount":1,"totalAmount":1,"couponAmount":0,"billBizType":"bills","buyerPayAmount":1,"targetOrderId":"4200001834202305240940548710","payDetail":"现金支付0.01元。","merOrderId":"34ZZ23052491342031325439620","status":"TRADE_SUCCESS","targetSys":"WXPay"}&qrCodeId=34ZZ2305247403826114496779&billDate=2023-05-24&totalAmount=1&createTime=2023-05-24 13:42:03&notifyId=18772162-106a-438a-8434-a26af73df39a&subInst=100200
     */
    @RequestMapping("stopNotify")
    @ResponseBody
    @SneakyThrows
    public String stopNotify(@RequestBody String param) {
        String p = URLDecoder.decode(param, "UTF-8");
        List<String> arr1 = Arrays.asList(p.split("&"));
        JSONObject jsonObject = new JSONObject();
        for (String arr : arr1) {
            String[] arrs = arr.split("=");
            jsonObject.put(arrs[0], arrs[1]);
        }
        logger.info(">>>>>>>>在停订单聚合支付回调：" + jsonObject.toJSONString());
        NotifyDto dto = JSONObject.toJavaObject(jsonObject, NotifyDto.class);
        if (!StringUtils.equals(UMSConfig.BILL_STATUS_PAID, dto.getBillStatus())) {
            return UMSConfig.NOTIFY_RESPONSE_SUCCESS;
        }
        BillPayment payment = JSONObject.parseObject(dto.getBillPayment(), BillPayment.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String srcReserve = dto.getSrcReserve();
        JSONObject jo = JSONObject.parseObject(srcReserve);
        String orderId = jo.getString("orderId");
        ParkingOrder parkingOrder = parkingOrderService.selectById(orderId);
        BigDecimal unpaid = new BigDecimal(parkingOrder.getUnpaid_amount()).multiply(new BigDecimal(100));
        BigDecimal paid = new BigDecimal(payment.getTotalAmount());
        BigDecimal newUnpaid = unpaid.subtract(paid).setScale(2);

        parkingOrder.setUnpaid_amount(newUnpaid.divide(new BigDecimal(100)).setScale(2).toString());
        parkingOrder.setPaid_amount(paid.divide(new BigDecimal(100)).setScale(2).toString());
        parkingOrder.setUpdate_time(new Date());
        parkingOrder.setPay_time(format.parse(payment.getPayTime()));
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPayment_resource("3");
        paymentOrder.setStatus("2");
        paymentOrder.setBillDate(dto.getBillDate());
        paymentOrder.setPayment_type(GlobalData.PARKING_ORDER_TYPE_UNION);
        paymentOrder.setAmount(paid.divide(new BigDecimal(100)).setScale(2).toString());
        paymentOrder.setPayment_no(createOutTradeNo("park", 1));
        paymentOrder.setPayment_serialno(dto.getBillNo());
        paymentOrder.setIs_del(GlobalData.ISDEL_NO);
        paymentOrder.setCreate_time(new Date());
        paymentOrder.setUpdate_time(new Date());
        paymentOrder.setPay_time(format.parse(payment.getPayTime()));
        paymentOrder.setRemarks("stopNotify聚合支付,停车订单号：" + parkingOrder.getOrder_no()
                + ",Unpaid_amount：" + newUnpaid.divide(new BigDecimal(100)).setScale(2).toString()
                + ",Paid_amount：" + paid.divide(new BigDecimal(100)).setScale(2).toString());
        paymentOrderService.insert(paymentOrder);
        parkingOrder.setPayment_id(paymentOrder.getId());
        parkingOrder.setPay_time(new Date());
        parkingOrder.setStatus("3");//3已缴费
        parkingOrder.setPay_type("8");//8聚合支付
        parkingOrderService.updateById(parkingOrder);
        logger.info(">>>>>>>>在停订单聚合支付回调：" + parkingOrder.toString());
        return "SUCCESS";
    }

    public String createOutTradeNo(String code, int type) {
        String orderNo = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatTime = sdf.format(new Date());
        //生成随机数
        String symbols = "0123456789";
        Random random = new SecureRandom();
        char[] nonceChars = new char[10];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = symbols.charAt(random.nextInt(symbols.length()));
        }
        String randomStr = new String(nonceChars);

        //判断是否有前缀
        if (org.apache.commons.lang.StringUtils.isNotBlank(code))
            orderNo = code;
        if (type == 1) {
            return orderNo + formatTime + randomStr;
        } else {
            return orderNo + formatTime + randomStr;
        }
    }

    @RequestMapping("getToken")
    @ResponseBody
    public ResultInfo getToken() {
        Token token = tokenUtil.getToken();
        return ResultInfo.success(token);
    }

    @RequestMapping("getQrcode")
    @ResponseBody
    public ResultInfo getQrcode(String accessToken, Integer parkId, Integer channelId) {
        String qrcode = tokenUtil.getQrcode(accessToken, parkId, channelId);
        return ResultInfo.success(qrcode);
    }

//    public static void main(String[] args) {
//        String json = "{'billDesc':'徐州经济技术开发区金瑞房地产经营有限公司','sign':'395862058CD566F61CB7DF22C4415C81B0F9C8A3662CCFA4970C51C0E0B545A6','merName':'徐州经济技术开发区金瑞房地产经营有限公司','mid':'89832536513127K','qrCodeType':'BILLPAY','mchntUuid':'2d9081bd879e548d0187e5cedffc4b5b','tid':'X2S0Q7J4','instMid':'QRPAYDEFAULT','srcReserve':'{\'orderId\':76287}','receiptAmount':'500','fy':'uAzT','billStatus':'PAID','cardAttr':'BALANCE','signType':'SHA256','billNo':'34ZZ202312261624119873b9f363','seqId':'38572186403N','billQRCode':'https://qr.chinaums.com/bills/qrCode.doid','bankInfo':'OTHERS','billPayment':'{\'payTime\':\'2023-12-26 16:24:22\',\'buyerCashPayAmt\':500,\'connectSys\':\'UNIONPAY\',\'paySeqId\':\'38572186403N\',\'invoiceAmount\':500,\'settleDate\':\'2023-12-26\',\'buyerId\':\'otdJ_uMqhYUD4kdrDUDVuxuE1ANQ\',\'receiptAmount\':500,\'totalAmount\':500,\'couponAmount\':0,\'billBizType\':\'bills\',\'buyerPayAmount\':500,\'targetOrderId\':\'4200002095202312260139841113\',\'payDetail\':\'现金支付5.00元。\',\'merOrderId\':\'34ZZ202312261624119873b9f3630\',\'status\':\'TRADE_SUCCESS\',\'targetSys\':\'WXPay\'}','qrCodeId':'34ZZ2312265642413168451249','billDate':'2023-12-26','totalAmount':'500','createTime':'2023-12-26 16:24:13','notifyId':'c820aa13-9672-4143-b422-a57627097be4','subInst':'100200'}";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("billDesc","徐州经济技术开发区金瑞房地产经营有限公司");
//        jsonObject.put("sign","395862058CD566F61CB7DF22C4415C81B0F9C8A3662CCFA4970C51C0E0B545A6");
//        jsonObject.put("merName","徐州经济技术开发区金瑞房地产经营有限公司");
//        jsonObject.put("mid","89832536513127K");
//        jsonObject.put("qrCodeType","BILLPAY");
//        jsonObject.put("mchntUuid","2d9081bd879e548d0187e5cedffc4b5b");
//        jsonObject.put("tid","X2S0Q7J4");
//        jsonObject.put("instMid","QRPAYDEFAULT");
//        jsonObject.put("srcReserve","{\"orderId\":76287}");
//        jsonObject.put("receiptAmount","500");
//        jsonObject.put("fy","uAzT");
//        jsonObject.put("billStatus","PAID");
//        jsonObject.put("cardAttr","BALANCE");
//        jsonObject.put("signType","SHA256");
//        jsonObject.put("billNo","34ZZ202312261624119873b9f363");
//        jsonObject.put("seqId","38572186403N");
//        jsonObject.put("billQRCode","https://qr.chinaums.com/bills/qrCode.doid");
//        jsonObject.put("bankInfo","OTHERS");
//        jsonObject.put("billPayment","{\"payTime\":\"2023-12-26 16:24:22\",\"buyerCashPayAmt\":500,\"connectSys\":\"UNIONPAY\",\"paySeqId\":\"38572186403N\",\"invoiceAmount\":500,\"settleDate\":\"2023-12-26\",\"buyerId\":\"otdJ_uMqhYUD4kdrDUDVuxuE1ANQ\",\"receiptAmount\":500,\"totalAmount\":500,\"couponAmount\":0,\"billBizType\":\"bills\",\"buyerPayAmount\":500,\"targetOrderId\":\"4200002095202312260139841113\",\"payDetail\":\"现金支付5.00元。\",\"merOrderId\":\"34ZZ202312261624119873b9f3630\",\"status\":\"TRADE_SUCCESS\",\"targetSys\":\"WXPay\"}");
//        jsonObject.put("qrCodeId","34ZZ2312265642413168451249");
//        jsonObject.put("billDate","2023-12-26");
//        jsonObject.put("totalAmount","500");
//        jsonObject.put("createTime","2023-12-26 16:24:13");
//        jsonObject.put("notifyId","c820aa13-9672-4143-b422-a57627097be4");
//        jsonObject.put("subInst","100200");
//
//        NotifyDto dto = JSONObject.toJavaObject(jsonObject, NotifyDto.class);
//        if(!StringUtils.equals(UMSConfig.BILL_STATUS_PAID, dto.getBillStatus())){
////            return UMSConfig.NOTIFY_RESPONSE_SUCCESS;
//        }
//        BillPayment payment = JSONObject.parseObject(dto.getBillPayment(), BillPayment.class);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String srcReserve = dto.getSrcReserve();
//        JSONObject jo = JSONObject.parseObject(srcReserve);
//        String orderId = jo.getString("orderId");
////        ParkingOrder parkingOrder = parkingOrderService.selectById(orderId);
//
//        ParkingOrder parkingOrder = new ParkingOrder();
//        parkingOrder.setUnpaid_amount("5.0");
//        parkingOrder.setId(Integer.parseInt(orderId));
//        BigDecimal unpaid = new BigDecimal(parkingOrder.getUnpaid_amount()).multiply(new BigDecimal(100));
//        BigDecimal paid = new BigDecimal(payment.getTotalAmount());
//        BigDecimal newUnpaid = unpaid.subtract(paid).setScale(2);
//
//        parkingOrder.setUnpaid_amount(newUnpaid.divide(new BigDecimal(100)).setScale(2).toString());
//        parkingOrder.setPaid_amount(paid.divide(new BigDecimal(100)).setScale(2).toString());
//        parkingOrder.setUpdate_time(new Date());
//
//        PaymentOrder paymentOrder = new PaymentOrder();
//        paymentOrder.setPayment_resource("3");
//        paymentOrder.setStatus("2");
//        paymentOrder.setBillDate(dto.getBillDate());
//        paymentOrder.setPayment_type(GlobalData.PARKING_ORDER_TYPE_UNION);
//        paymentOrder.setAmount(paid.divide(new BigDecimal(100)).setScale(2).toString());
////        paymentOrder.setPayment_no(createOutTradeNo("park", 1));
//        paymentOrder.setPayment_serialno(dto.getBillNo());
//        paymentOrder.setIs_del(GlobalData.ISDEL_NO);
//        paymentOrder.setCreate_time(new Date());
//        paymentOrder.setUpdate_time(new Date());
//        try {
//            System.out.println(format.parse(payment.getPayTime()));
//            paymentOrder.setPay_time(format.parse(payment.getPayTime()));
//            parkingOrder.setPay_time(format.parse(payment.getPayTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
////        paymentOrder.setRemarks("stopNotify聚合支付,停车订单号：" + parkingOrder.getOrder_no()
////                + ",Unpaid_amount："+ parkingOrder.getUnpaid_amount()
////                + ",Paid_amount：" + parkingOrder.getPaid_amount());
////        paymentOrderService.insert(paymentOrder);
//        parkingOrder.setPayment_id(paymentOrder.getId());
//        parkingOrder.setPay_time(new Date());
//        parkingOrder.setStatus(GlobalData.PARKING_ORDER_ALREADYPAY);
//        parkingOrder.setPay_type(GlobalData.PARKING_ORDER_TYPE_UNION);
////        parkingOrderService.updateById(parkingOrder);
//
//    }
}
