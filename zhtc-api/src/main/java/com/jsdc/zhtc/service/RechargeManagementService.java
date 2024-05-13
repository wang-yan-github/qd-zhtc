package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.wxpay.sdk.WXPayUtil;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.RechargeManagementDao;
import com.jsdc.zhtc.mapper.RechargeManagementMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.pay.WxPay;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.utils.pay.HttpsPostUtil;
import com.jsdc.zhtc.utils.pay.WxAPIV3SignUtils;
import com.jsdc.zhtc.vo.*;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类 名: 充值管理
 * 描 述: RechargeManagementService
 * 作 者: lw
 * 创 建：2022/1/4 9:37
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class RechargeManagementService extends BaseService<RechargeManagementDao, RechargeManagement> {
    @Autowired
    private RechargeManagementMapper mapper;
    @Autowired
    private MemberManageService manageService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private RechargeManagementPicService rechargeManagementPicService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private WxPayConfigService configService;
    @Autowired
    private WxPay wxPay;


    /**
     * 描 述： TODO(充值管理详细信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getRechargeMInfo(CommonVo data) {
        //订单支付方式
        HashMap<String, SysDict> payType_map = DcCacheDataUtil.getMapDicts("pay_type");
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<RechargeManagementVO> censusVo = mapper.getRechargeMInfo(data);
            censusVo.forEach(x -> {
                //关联图片
                List<RechargeManagementPic> rechargeManagementPicList = rechargeManagementPicService.selectList(Wrappers.<RechargeManagementPic>lambdaQuery().
                        eq(RechargeManagementPic::getRecharge_management_id, x.getId()).eq(RechargeManagementPic::getIs_del, "0"));
                List<Integer> ids = rechargeManagementPicList.stream().map(RechargeManagementPic::getPicture_id).collect(Collectors.toList());
                x.setFileList(fileManageService.selectByFileList2(ids));
                //订单支付方式
                x.setPayment_type(payType_map.get(x.getPayment_type()).getLabel());
            });

            PageInfo<RechargeManagementVO> monthlyManagementVoPage = new PageInfo<>(censusVo);
            return ResultInfo.success(monthlyManagementVoPage);

        } else {
            List<RechargeManagementVO> censusVo = mapper.getRechargeMInfo(data);
            censusVo.forEach(x -> {
                //关联图片
                List<RechargeManagementPic> rechargeManagementPicList = rechargeManagementPicService.selectList(Wrappers.<RechargeManagementPic>lambdaQuery().
                        eq(RechargeManagementPic::getRecharge_management_id, x.getId()).eq(RechargeManagementPic::getIs_del, "0"));
                List<Integer> ids = rechargeManagementPicList.stream().map(RechargeManagementPic::getPicture_id).collect(Collectors.toList());
                x.setFileList(fileManageService.selectByFileList2(ids));
                //订单支付方式
                x.setPayment_type(payType_map.get(x.getPayment_type()).getLabel());
            });
            return ResultInfo.success(censusVo);
        }
    }

    /**
     * 描 述： TODO(充值管理详细信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo downRechargeMInfoExcel(CommonVo data, HttpServletResponse response) {

        List<RechargeManagementVO> list = mapper.getRechargeMInfo(data);

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("nickName", "微信昵称");
        writer.addHeaderAlias("phone", "账号");
        writer.addHeaderAlias("recharge_amount", "充值金额");
        writer.addHeaderAlias("typeName", "类型");
        writer.addHeaderAlias("recharge_time", "充值时间");
        writer.addHeaderAlias("paymentNo", "交易号");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");

        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("123456.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultInfo.success(null);
    }

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll(PageVo<RechargeManagement> data) {

        RechargeManagement bean = data.getBean();

        QueryWrapper<RechargeManagement> wrapper = new QueryWrapper<>();

        if (bean != null) {

            if (bean.getIs_del() != null)
                wrapper.eq("is_del", bean.getIs_del());
        }
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageSize() != null) {
            Page<RechargeManagement> page = new Page<>(data.getPageNum(), data.getPageSize());
            IPage<RechargeManagement> pageData = selectPage(page, wrapper);

            if (pageData.getRecords().isEmpty())
                pageData.setRecords(new ArrayList<RechargeManagement>());

            return ResultInfo.success(pageData);
        } else {
            List<RechargeManagement> list = this.selectList(wrapper);
            return ResultInfo.success(list);
        }
    }


    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo selectById(RechargeManagement bean) {
        RechargeManagement data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error("未查询到数据");
        }
    }

    /**
     * 线下充值
     * 现金充值
     */
    public ResultInfo onSave(RechargeManagement bean) {
        PaymentOrder paymentOrder = new PaymentOrder();
        //支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡)
        paymentOrder.setPayment_type("5");
        //支付来源(1包月、2会员充值、3停车订单支付)
        paymentOrder.setPayment_resource("2");
        paymentOrder.setPayment_no(new DateTime().toString("yyyyMMddHHmmssSSS") + String.format("%04d", new Random().nextInt(9999)));
        paymentOrder.setPay_time(new Date());
        //支付状态 1待支付 2已支付 3支付失败
        paymentOrder.setStatus("2");
        //支付金额
        //         BigDecimal balance = new BigDecimal(bean.getRecharge_amount()).add(new BigDecimal(bean.getGift_amount()));
        BigDecimal balance = new BigDecimal(bean.getRecharge_amount());
        paymentOrder.setAmount(balance.toString());
        //支付流水号 当前时间+4位随机数
        // paymentOrder.setPayment_serialno(new DateTime().toString("yyyyMMddHHmmssSSS") + String.format("%04d", new Random().nextInt(9999)));
        //备注
//         paymentOrder.setRemarks("钱包付款");
        paymentOrderService.insert(paymentOrder);
        //更会员钱包
        Integer count = manageService.rechargeUp(bean.getMember_id(), bean.getRecharge_amount(), "0");
        //增加充值记录
        bean.setRecharge_time(new Date());
        bean.setPayment_id(paymentOrder.getId());
        bean.setIs_del("0");
        if (insert(bean) > 0) {
            if (Base.notEmpty(bean.getFileIds())) {
                String[] ids = bean.getFileIds().split(",");
                for (String id : ids) {
                    RechargeManagementPic rechargeManagementPic = new RechargeManagementPic();
                    rechargeManagementPic.setPicture_id(Integer.valueOf(id));
                    rechargeManagementPic.setRecharge_management_id(bean.getId());
                    rechargeManagementPic.setIs_del(0);
                    rechargeManagementPicService.insert(rechargeManagementPic);
                }
            }
            return ResultInfo.success("充值成功");
        } else {
            return ResultInfo.error("充值失败");
        }
    }

    /**
     * 描 述： TODO(充值功能)
     * 作 者： zln
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo startRecharge(RechargeManagement bean) {
        //微信支付功能（暂写）先支付返回订单id
        //微信支付
        AlWxOrder wxOrder = new AlWxOrder();
        wxOrder.setTotalAmount(bean.getRecharge_amount());
        wxOrder.setSubject("钱包充值");
        wxOrder.setOpenid(bean.getOpenId());
        wxOrder.createOutTradeNo(null, 1);
        // 调用支付接口
        WxPayConfig payConfig = configService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
        String prepayId = wxPay.prepay(wxOrder, payConfig, "");
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = WXPayUtil.generateNonceStr();
        String paySign = WxAPIV3SignUtils.getPrepaySign(payConfig.getApp_id(), prepayId, nonceStr, String.valueOf(timestamp), payConfig);

        PaymentOrder paymentOrder = new PaymentOrder();
        //支付方式(1包月 2微信 3支付宝 4钱包 5现金)
        paymentOrder.setPayment_type("2");
        //支付来源(1包月、2会员充值、3停车订单支付)
        paymentOrder.setPayment_resource("2");
        paymentOrder.setPayment_no(wxOrder.getOutTradeNo());
        paymentOrder.setPay_time(new Date());
        //支付状态 1待支付 2已支付 3支付失败
        paymentOrder.setStatus("1");
        //支付金额
        //         BigDecimal balance = new BigDecimal(bean.getRecharge_amount()).add(new BigDecimal(bean.getGift_amount()));
        BigDecimal balance = new BigDecimal(bean.getRecharge_amount());
        paymentOrder.setAmount(balance.toString());
        //支付流水号 当前时间+4位随机数
        // paymentOrder.setPayment_serialno(new DateTime().toString("yyyyMMddHHmmssSSS") + String.format("%04d", new Random().nextInt(9999)));
        //备注
        paymentOrder.setRemarks("钱包充值");
        paymentOrder.setIs_del(GlobalData.ISDEL_NO);
        paymentOrder.setCreate_time(new Date());
        paymentOrderService.insert(paymentOrder);
        //更会员钱包
        //Integer count = manageService.rechargeUp(bean.getMember_id(), bean.getRecharge_amount(), bean.getGift_amount());
        //增加充值记录
        bean.setRecharge_time(new Date());
        bean.setPayment_id(paymentOrder.getId());
        bean.setIs_del("0");
        MemberManage memberManage = manageService.selectOne(Wrappers.<MemberManage>lambdaQuery()
                .eq(MemberManage::getOpenid, bean.getOpenId()).eq(MemberManage::getIs_del, GlobalData.ISDEL_NO));
        bean.setMember_id(memberManage.getId());
        bean.setPayment_id(paymentOrder.getId());
        bean.setIs_del(GlobalData.ISDEL_NO);
        bean.setCreate_time(new Date());
        bean.setUpdate_time(new Date());
        insert(bean);
        JSONObject result = new JSONObject();
        result.put("timeStamp", String.valueOf(timestamp));
        result.put("nonceStr", nonceStr);
        result.put("signType", "RSA");
        result.put("paySign", paySign);
        result.put("prepayId", prepayId);
        result.put("appId", payConfig.getApp_id());
        result.put("paymentNo", wxOrder.getOutTradeNo());
        return ResultInfo.success(result);
    }

    @SneakyThrows
    public ResultInfo updateRecharge(PaymentOrder paymentOrder) {
        String openid = paymentOrder.getOpenid();
        String outTradNo = paymentOrder.getPayment_no();
        PaymentOrder po = paymentOrderService.selectOne(Wrappers.<PaymentOrder>lambdaQuery()
                .eq(PaymentOrder::getPayment_no, outTradNo).eq(PaymentOrder::getIs_del, GlobalData.ISDEL_NO));
        WxPayConfig payConfig = configService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + outTradNo + "?mchid=" + payConfig.getMch_id();
        String postForObject = HttpsPostUtil.doGet("GET", url, "", payConfig);
        System.out.println(postForObject);
        JSONObject result = JSON.parseObject(postForObject);
        if (org.apache.commons.lang.StringUtils.equals("SUCCESS", result.getString("trade_state"))) {
            po.setStatus("2");
            po.setUpdate_time(new Date());
            po.setPayment_serialno(result.getString("transaction_id"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(result.getString("success_time").substring(0, 18).replaceAll("T", " "));
            po.setPay_time(date);
            paymentOrderService.updateById(po);
            RechargeManagement rechargeManagement = selectOne(Wrappers.<RechargeManagement>lambdaQuery()
                    .eq(RechargeManagement::getPayment_id, po.getId()).eq(RechargeManagement::getIs_del, GlobalData.ISDEL_NO));
            MemberManage memberManage = manageService.selectOne(Wrappers.<MemberManage>lambdaQuery()
                    .eq(MemberManage::getOpenid, openid).eq(MemberManage::getIs_del, GlobalData.ISDEL_NO));

            BigDecimal balance = new BigDecimal(rechargeManagement.getRecharge_amount()).add(new BigDecimal(rechargeManagement.getGift_amount()));
            String amount = memberManage.getBalance();
            memberManage.setBalance(new BigDecimal(amount).add(balance).toString());
            memberManage.setUpdate_time(new Date());
            manageService.updateById(memberManage);
            return ResultInfo.success(memberManage);
        } else if (org.apache.commons.lang.StringUtils.equals("CLOSED", result.getString("trade_state"))) {
            po.setStatus("3");
            po.setUpdate_time(new Date());
            paymentOrderService.updateById(po);
            return ResultInfo.error("支付失败");
        }
        return ResultInfo.error("支付失败");

    }

    /**
     * 描 述： TODO(根据会员id查询充值记录)
     * 作 者： zln
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public String sumRecharge(Integer memberId) {
        return mapper.sumRecharge(memberId);
    }

    /**
     * 充值收入
     */
    public String getSumCzMoney(CommonVo vo) {
        String sumCost = mapper.getSumCzMoney(vo);
        return ArithmeticUtils.setScale1(sumCost == null ? "0" : sumCost, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo updateInfo(RechargeManagement bean) {

        if (this.updateById(bean) > 0)
            return ResultInfo.success(null, "操作成功");
        else
            return ResultInfo.error(null, "操作成功");

    }

    /**
     * 描 述： TODO(根据id删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo deleById(RechargeManagement bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success(null, "删除成功");
        } else {
            return ResultInfo.error(null, "操作失败");
        }
    }

    /**
     * create by wp at 2022/1/10 19:02
     * description: 根据会员id查询充值记录
     *
     * @param memberId
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getRechargeRecords(int memberId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<RechargeManagementVO> list = mapper.getrechargeRecords(memberId);
        list.forEach(x -> {
            if (StringUtils.isNotEmpty(x.getRecharge_amount()) && StringUtils.isNotEmpty(x.getGift_amount())) {
                BigDecimal sum = new BigDecimal(x.getRecharge_amount()).add(new BigDecimal(x.getGift_amount()));
                x.setRecharge_amount(sum.toString());
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 充值记录列表
     */
    public List<RechargeManagementVO> getRechargeDataByPaymentId(Integer paymentId) {
        return mapper.getRechargeDataByPaymentId(paymentId);
    }

}
