package com.jsdc.zhtc.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.AlPayConfig;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.service.AlPayConfigService;
import com.jsdc.zhtc.service.PaymentOrderService;
import com.jsdc.zhtc.vo.AlWxOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AlPay {
    @Autowired
    private AlPayConfigService service;

    @Autowired
    private PaymentOrderService paymentOrderServcie;

    @Value("${paymentOrder.al-notify}")
    private String alNotify;

    public static void main(String[] args) throws Exception {

        AlPayConfig pay = new AlPayConfig();
        AlWxOrder alWxOrder = new AlWxOrder();
        //wap下单
        /*alWxOrder.createOutTradeNo(null , 1);
        alWxOrder.setTotalAmount("20");
        alWxOrder.setSubject("停车缴费");
        wapPay( alWxOrder  );
*/
        //订单查询
        /*alWxOrder.setOutTradeNo("202201274978232961");
        new AlPay().findOrder(alWxOrder);*/

        //退款
        alWxOrder.setOutTradeNo("159147156789549789456");
        alWxOrder.setRefund_amount("10");
        alWxOrder.setRefund_reason("部分退款");
        alWxOrder.createOutTradeNo(null, 2);
        new AlPay().refund(alWxOrder);
        // System.out.println(json.get("alipay_trade_refund_response"));
        System.out.println(alWxOrder.getAlWxPayInfo().getMsg());
        //退款查询
        /*alWxOrder.setTrade_no("2021122922001424030501744484");
        alWxOrder.setOut_request_no("aztk1554475");
        refundQuery( alWxOrder);*/
        // JSONObject json = refundQuery(pay, alWxOrder);
        System.out.println(alWxOrder.getAlWxPayInfo().getPayURL());
        System.out.println(alWxOrder.getAlWxPayInfo().getJsonData());
    }


    /**
     * 描 述： TODO(wap支付统一下单 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @return {@link JSONObject}
     * out_trade_no
     */
    public void wapPay(AlWxOrder alWxOrder) {

        AlPayConfig payConfig = service.findAlPayConfig();
        //AlPayConfig payConfig = new AlPayConfig( );

        JSONObject json = new JSONObject();
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(payConfig.getPay_url(),
                    payConfig.getApp_id(), payConfig.getApp_private_id(), "json",
                    payConfig.getCharset(), payConfig.getAlipay_public_key(), "RSA2");

            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

            //异步通知（服务器）
            request.setNotifyUrl(alNotify + "/notice/alNotify");
            //同步通知（客户端）
            request.setReturnUrl(alNotify + "/notice/alReturn");

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", alWxOrder.getOutTradeNo());
            bizContent.put("total_amount", alWxOrder.getTotalAmount());
            bizContent.put("subject", alWxOrder.getSubject());
            bizContent.put("product_code", "QUICK_WAP_WAY");
            //bizContent.put("quit_url", alWxOrder.getUrl()+"/notice/zctc");


            request.setBizContent(bizContent.toString());
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {

                String strJson = JSON.toJSONString(response);
                JSONObject jsonstr = JSONObject.parseObject(strJson);

                if (jsonstr.getBoolean("success")) {
                    //订单持久化
                    PaymentOrder paymentOrder = new PaymentOrder();
                    paymentOrder.setPayment_no(alWxOrder.getOutTradeNo());
                    paymentOrder.setAmount(alWxOrder.getTotalAmount());
                    paymentOrder.setPayment_type(GlobalData.PARKING_ORDER_TYPE_ZFB);
                    paymentOrder.setStatus("1");
                    paymentOrder.setPayment_resource("3");
                    paymentOrder.setPay_time(new Date());
                    Integer count = paymentOrderServcie.saveData(paymentOrder);
                    //判定持久化是否成功
                    if (count > 0) {
                        alWxOrder.getAlWxPayInfo().setCode(200);
                        alWxOrder.getAlWxPayInfo().setPayURL(jsonstr.getString("body"));
                        alWxOrder.getAlWxPayInfo().setJsonData(jsonstr);
                        alWxOrder.getAlWxPayInfo().setPaymentOrder(paymentOrder);
                    } else {
                        alWxOrder.getAlWxPayInfo().setCode(500);
                        alWxOrder.getAlWxPayInfo().setMsg("订单记录失败");
                    }

                } else {
                    alWxOrder.getAlWxPayInfo().setCode(500);
                    alWxOrder.getAlWxPayInfo().setMsg("支付调起获取失败");
                }
            } else {
                alWxOrder.getAlWxPayInfo().setCode(500);
                alWxOrder.getAlWxPayInfo().setMsg("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            alWxOrder.getAlWxPayInfo().setCode(500);
            alWxOrder.getAlWxPayInfo().setMsg("请求失败");
        }
    }


    /**
     * 描 述： TODO(订单查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return {@link JSONObject}
     */
    public void findOrder(AlWxOrder order) {
        AlPayConfig payConfig = service.findAlPayConfig();
        //AlPayConfig payConfig = new AlPayConfig();
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(payConfig.getPay_url(),
                    payConfig.getApp_id(), payConfig.getApp_private_id(), "json",
                    payConfig.getCharset(), payConfig.getAlipay_public_key(), "RSA2");

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();

            if (StringUtils.isNotBlank(order.getOutTradeNo()))
                bizContent.put("out_trade_no", order.getOutTradeNo());

            if (StringUtils.isNotBlank(order.getTrade_no()))
                bizContent.put("trade_no", order.getTrade_no());

            request.setBizContent(bizContent.toString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String body = response.getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (response.isSuccess()) {
                JSONObject json = jsonObject.getJSONObject("alipay_trade_query_response");
                if (json.getString("code").equals("10000")) {
                    order.getAlWxPayInfo().setCode(200);
                    order.getAlWxPayInfo().setMsg("调用成功");
                    order.getAlWxPayInfo().setJsonData(json);
                } else {
                    order.getAlWxPayInfo().setCode(202);
                    order.getAlWxPayInfo().setMsg(json.getString("sub_msg"));
                    order.getAlWxPayInfo().setSubCode(json.getString("sub_code"));
                }
                order.getAlWxPayInfo().setJsonData(json);
            } else {
                JSONObject atqr = jsonObject.getJSONObject("alipay_trade_query_response");
                order.getAlWxPayInfo().setCode(202);
                order.getAlWxPayInfo().setSubCode(atqr.getString("sub_code"));
                order.getAlWxPayInfo().setMsg(atqr.getString("sub_msg"));
                order.getAlWxPayInfo().setJsonData(atqr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单查询出错");
        }

    }

    /**
     * 描 述： TODO(订单退款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return {@link JSONObject}
     * out_trade_no 商户订单号
     * refund_fee 退款金额
     * gmt_refund_pay 退款时间
     * send_back_fee 本次商户实际退回金额
     * trade_no 支付宝交易号
     * buyer_logon_id 用户的登录id 支付商户平台的账号
     * buyer_user_id 买家在支付宝的用户id
     * fund_change Y变化 N没变化
     */
    public void refund(AlWxOrder order) {
        AlPayConfig payConfig = service.findAlPayConfig();
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(payConfig.getPay_url(),
                    payConfig.getApp_id(), payConfig.getApp_private_id(), "json",
                    payConfig.getCharset(), payConfig.getAlipay_public_key(), "RSA2");

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order.getOutTradeNo());//商户订单号
            // bizContent.put("trade_no", order.getTrade_no());//支付宝交易号
            bizContent.put("out_request_no", order.getOut_request_no());//退款请求号
            bizContent.put("refund_reason", order.getRefund_reason());//退款原因
            bizContent.put("refund_amount", order.getRefund_amount());//退款金额

            request.setBizContent(bizContent.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                String body = response.getBody();
                Object object = JSONObject.parseObject(body).get("alipay_trade_refund_response");
                JSONObject json = JSONObject.parseObject(object.toString());

                if (!json.getString("code").equals("10000")) {
                    order.getAlWxPayInfo().setCode(200);
                    order.getAlWxPayInfo().setMsg(json.getString("sub_msg"));
                    order.getAlWxPayInfo().setSubCode(json.getString("sub_code"));
                } else {
                    if (json.getString("fund_change").equals("N")) {
                        order.getAlWxPayInfo().setCode(201);
                        order.getAlWxPayInfo().setMsg("订单未产，退款单号预计重复");
                    } else {
                        order.getAlWxPayInfo().setCode(200);
                    }
                }
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(object.toString()));
            } else {
                order.getAlWxPayInfo().setCode(500);
                order.getAlWxPayInfo().setMsg("服务调用失败！");
            }


        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单退款查询出错！");
        }
    }

    /**
     * 描 述： TODO(退款查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return {@link JSONObject}
     * out_trade_no 支付订单号
     * total_amount 支付总金额
     * refund_amount 退款金额
     * trade_no 支付宝交易号
     * out_request_no 退款请求号
     */
    public void refundQuery(AlWxOrder order) {
        AlPayConfig payConfig = service.findAlPayConfig();
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(payConfig.getPay_url(),
                    payConfig.getApp_id(), payConfig.getApp_private_id(), "json", payConfig.getCharset(), payConfig.getAlipay_public_key(), "RSA2");
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("trade_no", order.getTrade_no());
            bizContent.put("out_request_no", order.getOut_request_no());

            request.setBizContent(bizContent.toString());
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {

                String body = response.getBody();
                Object object = JSONObject.parseObject(body).get("alipay_trade_fastpay_refund_query_response");
                order.getAlWxPayInfo().setCode(200);
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(object.toString()));
            } else {
                order.getAlWxPayInfo().setCode(500);
                order.getAlWxPayInfo().setMsg("服务调用失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单退款查询出错！");
        }
    }


}
