package com.jsdc.zhtc.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.jsdc.core.base.Base;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.model.WxPayConfig;
import com.jsdc.zhtc.service.WxPayConfigService;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.utils.pay.HttpsPostUtil;
import com.jsdc.zhtc.utils.pay.KeyPairFactory;
import com.jsdc.zhtc.vo.AlWxOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPay {

    @Autowired
    private WxPayConfigService service;

    /**
     * JSPI统一下单接口
     *
     * @param order:
     * @return String
     * @author wp
     * @description
     * @date 2022/11/12 16:35
     */
    public String prepay(AlWxOrder order, WxPayConfig payConfig, String notify_url) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", payConfig.getApp_id());
            map.put("mchid", payConfig.getMch_id());//商户号
            map.put("description", order.getSubject()); //商品描述
            map.put("out_trade_no", order.getOutTradeNo()); //商户订单号
            if (Base.notEmpty(notify_url)) {
                map.put("notify_url", notify_url); //回调地址
            } else {
                map.put("notify_url", "http://f7ehkp.natappfree.cc/pay/wxPayNotify.do"); //假回调地址，调不通
            }

//            map.put("notify_url", "https://zhtc.aldwxa.top/app/roadparklist/WxPayCallBack"); //回调地址

            System.out.println("订单号: " + order.getOutTradeNo());
            //订单金额信息
            //TODO 金额暂时写死为0.01元
            Map<String, Object> amountMap = new HashMap<>();
            int totlaAmount = new BigDecimal(order.getTotalAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
            System.out.println("************************" + totlaAmount);
            amountMap.put("total", totlaAmount); //总金额
            amountMap.put("currency", "CNY");//货币类型
            map.put("amount", amountMap);
            //支付者
            Map<String, Object> payerMap = new HashMap<>();
            payerMap.put("openid", order.getOpenid());//用户标识
            map.put("payer", payerMap); //openid

            String url = payConfig.getPay_url() + "/v3/pay/transactions/jsapi";

            String jsonStr = JSON.toJSONString(map);
            String postForObject = HttpsPostUtil.doPost("POST", url, jsonStr, payConfig);

            String prepayId = JSON.parseObject(postForObject).getString("prepay_id");


            return prepayId;
        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("下单失败");
        }
        return null;
    }

    public String h5pay(AlWxOrder order, WxPayConfig payConfig) {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", payConfig.getApp_id());
        map.put("mchid", payConfig.getMch_id());//商户号
        map.put("description", order.getSubject()); //商品描述
        map.put("out_trade_no", order.getOutTradeNo()); //商户订单号
        map.put("notify_url", "http://f7ehkp.natappfree.cc/pay/wxPayNotify.do"); //回调地址

        System.out.println("订单号: " + order.getOutTradeNo());
        //订单金额信息
        //TODO 金额暂时写死为0.01元
        Map<String, Object> amountMap = new HashMap<>();
        int totlaAmount = new BigDecimal(order.getTotalAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
        System.out.println("************************" + totlaAmount);
        amountMap.put("total", totlaAmount); //总金额
        amountMap.put("currency", "CNY");//货币类型
        map.put("amount", amountMap);

        JSONObject scene_info = new JSONObject();
        scene_info.put("payer_client_ip", "127.0.0.1");
        JSONObject h5_info = new JSONObject();
        h5_info.put("type", "Wap");
        h5_info.put("app_name", "智慧停车");
        h5_info.put("app_url", "https://zhtc.aldwxa.top/");
//        h5_info.put("app_url", "https://zhtc.aldwxa.top/");
        scene_info.put("h5_info", h5_info);
        map.put("scene_info", scene_info);
        String url = payConfig.getPay_url() + "/v3/pay/transactions/h5";

        String jsonStr = JSON.toJSONString(map);
        String postForObject = HttpsPostUtil.doPost("POST", url, jsonStr, payConfig);
        String h5url = JSONObject.parseObject(postForObject).getString("h5_url");
        return h5url;
    }

    public static void jsapiPay(AlWxOrder order, WxPayConfig payConfig) {
        try {
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, Object> map = new HashMap<>();

            map.put("appid", payConfig.getApp_id());
            map.put("mchid", payConfig.getMch_id());//商户号

            map.put("description", order.getSubject()); //商品描述
            map.put("out_trade_no", order.getOutTradeNo()); //商户订单号

            map.put("notify_url", order.getUrl() + "/notice/alNotify"); //回调地址

            //订单金额信息
            Map<String, Object> amountMap = new HashMap<>();
            int totlaAmount = new BigDecimal(order.getTotalAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
            amountMap.put("total", totlaAmount); //总金额
            amountMap.put("currency", "CNY");//货币类型
            map.put("amount", amountMap);
            //支付者
            Map<String, Object> payerMap = new HashMap<>();
            payerMap.put("openid", order.getOpenid());//用户标识
            map.put("payer", payerMap); //openid

            String url = payConfig.getPay_url() + "/v3/pay/transactions/jsapi";

            String jsonStr = JSON.toJSONString(map);
            String postForObject = HttpsPostUtil.doPost("POST", url, jsonStr, payConfig);

            System.out.println(postForObject);
            String prepayId = "";//预支付id
            if (postForObject.indexOf("SUCCESS") != -1) {
                Map<String, String> yzfMap = WXPayUtil.xmlToMap(postForObject);
                prepayId = (String) yzfMap.get("prepay_id");
            }
            //生成签名
            JSONObject qm = new JSONObject();
            qm.put("appId", payConfig.getApp_id());
            long currentTimestamp = WXPayUtil.getCurrentTimestamp();
            qm.put("timeStamp", currentTimestamp + "");
            String generateNonceStr = WXPayUtil.generateNonceStr();
            qm.put("nonceStr", generateNonceStr);
            qm.put("package", "prepay_id=" + prepayId);
            KeyPairFactory keyPairFactory = new KeyPairFactory();
            //KeyPair pkcs12 = keyPairFactory.createPKCS12("", "", "");
            //String sign = keyPairFactory.sign("POST", "", currentTimestamp, generateNonceStr, qm.toJSONString( ), pkcs12);

            order.getAlWxPayInfo().setJsonData(qm);
            order.getAlWxPayInfo().setCode(200);

        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("下单失败");
        }
    }


    /**
     * @param order:
     * @param payConfig:
     * @return String
     * @author wp
     * @description NATIVE统一下单接口
     * @date 2022/11/15 15:03
     */
    public String nativePprepay(AlWxOrder order, WxPayConfig payConfig) {

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", payConfig.getApp_id());
            map.put("mchid", payConfig.getMch_id());//商户号
            map.put("description", order.getSubject()); //商品描述
            map.put("out_trade_no", order.getOutTradeNo()); //商户订单号
            map.put("notify_url", "http://f7ehkp.natappfree.cc/pay/wxPayNotify.do"); //回调地址

            System.out.println("订单号: " + order.getOutTradeNo());
            //订单金额信息
            //TODO 金额暂时写死为0.01元
            Map<String, Object> amountMap = new HashMap<>();
            int totlaAmount = new BigDecimal(order.getTotalAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
            amountMap.put("total", totlaAmount); //总金额
            //amountMap.put( "total", 1); //总金额
            amountMap.put("currency", "CNY");//货币类型
            map.put("amount", amountMap);
            //支付者
            Map<String, Object> payerMap = new HashMap<>();

            String url = payConfig.getPay_url() + "/v3/pay/transactions/native";
            String jsonStr = JSON.toJSONString(map);
            String postForObject = HttpsPostUtil.doPost("POST", url, jsonStr, payConfig);
            String codeUrl = JSON.parseObject(postForObject).getString("code_url");


            return codeUrl;
        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("下单失败");
        }
        return null;
    }

    public String refund(AlWxOrder order, WxPayConfig payConfig, PaymentOrder paymentOrder, int refund) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> amount = new HashMap<>();
        String transactionId = paymentOrder.getPayment_serialno();
        String outTradNo = paymentOrder.getPayment_no();
        String outRefundNo = order.getOut_request_no();
        // TODO 支付金额0.01
        int total = new BigDecimal(paymentOrder.getAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
        //int total = 1;
        refund = new BigDecimal(order.getRefund_amount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
        String currency = "CNY";
        params.put("out_trade_no", outTradNo);
        params.put("out_refund_no", outRefundNo);
        amount.put("refund", refund);
        amount.put("total", total);
        amount.put("currency", currency);
        params.put("amount", amount);

        String url = payConfig.getPay_url() + "/v3/refund/domestic/refunds";
        String jsonStr = JSON.toJSONString(params);

        String postForObject = HttpsPostUtil.doPost("POST", url, jsonStr, payConfig);
        System.out.println("退款数据：" + jsonStr);
        System.out.println("退款地址：" + url);
        System.out.println("配置：" + payConfig.getId());
        System.out.println("退款结果：" + postForObject);
        return postForObject;
    }

    public String queryPayStatus(String outTradNo, WxPayConfig payConfig) {
        String url = payConfig.getPay_url() + "/v3/pay/transactions/out-trade-no/" + outTradNo + "?mchid=" + payConfig.getMch_id();
        String result = HttpsPostUtil.doGet("GET", url, "", payConfig);
        return result;
    }

    public String queryRefundStatue(String outRefundNo, WxPayConfig wxPayConfig) {
        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/" + outRefundNo;
        String result = HttpsPostUtil.doGet("GET", url, "", wxPayConfig);
        return result;
    }

    /**
     * 描 述： TODO(H5下单支付对接)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return
     */
    public void nwabPay(AlWxOrder order) {
        WxPayConfig payConfig = service.findWxPayConfig();
        WXPay wxpay = null;
        MyConfig config = null;
        try {
            config = new MyConfig();
            wxpay = new WXPay(config, "", true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.appID = payConfig.getApp_id();
            config.mchID = payConfig.getMch_id();
            config.key = payConfig.getApp_key();

            HashMap<String, String> map = new HashMap<>();
            map.put("body", order.getSubject()); //商品描述
            map.put("out_trade_no", order.getOutTradeNo()); //商户订单号
            map.put("total_fee", ArithmeticUtils.multiply(order.getTotalAmount(), "100").toString()); //总金额
            map.put("spbill_create_ip", order.getIp()); //终端ip
            map.put("trade_type", "MWEB"); //交易类型
            map.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"停车缴费\"}}"); //场景信息
            Map<String, String> retMap = wxpay.unifiedOrder(map);
            if (retMap.get("return_code").equals("SUCCESS") && retMap.get("result_code").equals("SUCCESS")) {
                System.out.println(retMap);
                order.getAlWxPayInfo().setCode(200);
                order.getAlWxPayInfo().setPayURL(retMap.get("mweb_url"));
            } else {
                order.getAlWxPayInfo().setCode(500);
                order.getAlWxPayInfo().setPayURL(retMap.get("mweb_url"));
                System.out.println(retMap.get("return_msg"));
            }
        } catch (Exception e) {

        }
    }


    /**
     * 描 述： TODO(查询订单)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return
     */
    public void findOrder(AlWxOrder order) {
        WxPayConfig payConfig = service.findWxPayConfig();
        WXPay wxpay = null;
        MyConfig config = null;
        try {
            config = new MyConfig();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.appID = payConfig.getApp_id();
            config.mchID = payConfig.getMch_id();
            config.key = payConfig.getApp_key();
            Map<String, String> data = new HashMap<String, String>();

            if (StringUtils.isNotBlank(order.getOutTradeNo()))
                data.put("out_trade_no", order.getOutTradeNo()); //订单号
            if (StringUtils.isNotBlank(order.getTrade_no()))
                data.put("transaction_id", order.getTrade_no()); //订单号

            Map<String, String> map = wxpay.orderQuery(data);
            if (map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS")) {
                if (map.get("trade_state").equals("SUCCESS")) {
                    order.getAlWxPayInfo().setCode(200);
                    order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(JSONObject.toJSONString(map)));
                } else {
                    order.getAlWxPayInfo().setCode(202);
                    order.getAlWxPayInfo().setSubCode(map.get("trade_state"));
                    order.getAlWxPayInfo().setMsg(map.get("trade_state_desc"));
                }
            } else {
                order.getAlWxPayInfo().setCode(500);
                order.getAlWxPayInfo().setMsg(map.get("err_code_des"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("支付查询错误！");
        }
    }

    /**
     * 描 述： TODO(申请退款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return
     */
    public void refundOrder(AlWxOrder order) {

        //WxPayConfig payConfig = service.findWxPayConfig( );
        WxPayConfig payConfig = new WxPayConfig();
        WXPay wxpay = null;
        MyConfig config = null;
        try {
            config = new MyConfig();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.appID = payConfig.getApp_id();
            config.mchID = payConfig.getMch_id();
            config.key = payConfig.getApp_key();

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", order.getOutTradeNo()); //商户订单号
            data.put("out_refund_no", order.getOut_request_no()); //退款请求号
            data.put("refund_desc", order.getRefund_reason()); //退款原因
            data.put("refund_fee", ArithmeticUtils.multiply(order.getRefund_amount(), "100").toString()); //退款金额


            Map<String, String> map = wxpay.refund(data);
            if (map.get("status").equals("SUCCESS")) {
                order.getAlWxPayInfo().setCode(200);
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(JSONObject.toJSONString(map)));
            } else {
                order.getAlWxPayInfo().setCode(202);
                order.getAlWxPayInfo().setSubCode(map.get("status"));
                order.getAlWxPayInfo().setMsg("退款失败");
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(JSONObject.toJSONString(map)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单撤销错误！");

        }
    }

    /**
     * 描 述： TODO(查询退款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return
     */
    public void refundQueryOrder(AlWxOrder order) {

        WxPayConfig payConfig = service.findWxPayConfig();
        WXPay wxpay = null;
        MyConfig config = null;
        try {
            config = new MyConfig();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.appID = payConfig.getApp_id();
            config.mchID = payConfig.getMch_id();
            config.key = payConfig.getApp_key();

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_refund_no", order.getOut_request_no()); //退款请求号
            Map<String, String> map = wxpay.refundQuery(data);
            if (map.get("status").equals("SUCCESS")) {
                order.getAlWxPayInfo().setCode(200);
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(JSONObject.toJSONString(map)));
            } else {
                order.getAlWxPayInfo().setCode(202);
                order.getAlWxPayInfo().setSubCode(map.get("status"));
                order.getAlWxPayInfo().setMsg("退款失败");
                order.getAlWxPayInfo().setJsonData(JSONObject.parseObject(JSONObject.toJSONString(map)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单撤销错误！");

        }
    }

    /**
     * 描 述： TODO(撤销订单)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param order
     * @return
     */
    public void reverseOrder(AlWxOrder order) {
        WxPayConfig payConfig = service.findWxPayConfig();
        WXPay wxpay = null;
        MyConfig config = null;
        try {
            config = new MyConfig();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            config.appID = payConfig.getApp_id();
            config.mchID = payConfig.getMch_id();
            config.key = payConfig.getApp_key();

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", order.getOutTradeNo()); //订单号
            Map<String, String> map = wxpay.reverse(data);
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));

        } catch (Exception e) {
            e.printStackTrace();
            order.getAlWxPayInfo().setCode(500);
            order.getAlWxPayInfo().setMsg("订单撤销错误！");

        }
    }

    public static void main(String[] args) {
        String APPID = "wx7d7b0c3c9086b462";
        String MERID = "1608882256";
        String SIGNKEY = "XZJJJSKFQjrzhtc12345678912345678";

    }


//    public static void main(String [] aegs){
//        //jsapiPay( new AlWxOrder());
//        nwabPay(new AlWxOrder());
//        //pay(new WxPayConfig() , new AlWxOrder());
//    }
}
