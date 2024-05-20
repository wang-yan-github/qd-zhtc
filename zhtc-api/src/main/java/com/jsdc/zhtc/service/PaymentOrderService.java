package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.ParkingGateUtils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.dao.PaymentOrderDao;
import com.jsdc.zhtc.mapper.PaymentOrderMapper;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.pay.WxPay;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.jsdc.core.base.Base.notEmpty;

/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Service
@Transactional
public class PaymentOrderService extends BaseService<PaymentOrderDao, PaymentOrder> {

    @Autowired
    PaymentOrderMapper paymentOrderMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private WxPay wxPay;
    @Autowired
    private WxPayConfigService configService;

    @Autowired
    private ParkingOrderService parkingOrderService;

    @Autowired
    private ParkService parkService;
    @Autowired
    private ParkingGateUtils parkingGateUtils;
    @Autowired
    OperateCarnoService carnoService;
    @Autowired
    SysLogService sysLogService;

    Logger logger = LoggerFactory.getLogger(PaymentOrderService.class);

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param vo
     * @return
     */
    public ResultInfo listPaymentOrder(Integer pageIndex, Integer pageSize, PaymentOrderVo vo) {
        SysUser sysUser = sysUserService.getUser();
        //停车场管理员 筛选数据
        if (sysUser.getUser_type().equals("1")) {
            vo.setPark_id(sysUser.getPark_id());
        }
        vo.setParkingType("1");

        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, String>> list = paymentOrderMapper.listPaymentOrder(vo);
        for (Map<String, String> map : list) {
            String sumAmount = map.get("sum_amount");
            map.put("sum_amount", new BigDecimal(sumAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//            String id = String.valueOf(map.get("id"));
//            Integer count = 0;
//            if (StringUtils.isNotBlank(id)) {
//                Integer paymentId = Integer.valueOf(id);
//                count = paymentOrderMapper.listRoadParkingOrder(PaymentOrderVo.builder().paymentId(paymentId).build()).size();
//            }
//            map.put("count", count.toString());
        }
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap();
        map.put("page", pageInfo);
        map.put("parkingType", "1");
        return ResultInfo.success(map);
    }

    /**
     * 联合支付
     *
     * @param vo
     * @return
     */
    public ResultInfo listRoadParkingOrder(PaymentOrderVo vo) {
        vo.setParkingType("1");
        List<Map<String, String>> list = paymentOrderMapper.listRoadParkingOrder(vo);
        for (Map<String, String> x : list) {
            if (null != x.get("resitime")) {
                String resi = String.valueOf(x.get("resitime"));
                x.put("resitime", TimeUtils.formatTime(Integer.parseInt(resi)));
            }
            x.put("paid_amount", new BigDecimal(x.get("paid_amount")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            x.put("sum_amount", new BigDecimal(x.get("sum_amount")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }

    //根据id查询数据
    public List<PaymentOrder> selectByPayId(Integer payment_id) {
        List<PaymentOrder> payments = selectList(new QueryWrapper<PaymentOrder>().eq("id", payment_id));
        //订单支付方式
        HashMap<String, SysDict> payType_map = DcCacheDataUtil.getMapDicts("pay_type");
        //支付状态
        HashMap<String, SysDict> paymentType_map = DcCacheDataUtil.getMapDicts("paymentType");
        //支付来源
        HashMap<String, SysDict> paymentResource_map = DcCacheDataUtil.getMapDicts("payment_resource");
        if (null != payments && payments.size() > 0) {
            for (PaymentOrder b : payments) {
                b.setPayment_type(payType_map.get(b.getPayment_type()).getLabel());
                if (notEmpty(b.getPayment_resource())) {
                    b.setPayment_resource(paymentResource_map.get(b.getPayment_resource()).getLabel());
                }
                b.setStatus(paymentType_map.get(b.getStatus()).getLabel());

            }
        }
        return payments;
    }


    /**
     * 微信小程序
     * 新增支付订单记录
     *
     * @author thr
     */
    public ResultInfo onSave(PaymentOrder bean) {
//        //支付方式(1包月 2微信 3支付宝 4钱包 5现金)
//        bean.setPayment_type("1");
//        //支付来源(1包月、2会员充值、3停车订单支付)
//        bean.setPayment_resource("1");
//        //支付状态 1待支付 2已支付 3支付失败
//        bean.setStatus("1");
//        //支付金额
//        bean.setAmount(vo.getPrice());
        //支付订单号
//            private String payment_no;
        //支付流水号
//            private String payment_serialno;
//        //备注
//        bean.setRemarks("微信包月");
        //创建时间
        bean.setCreate_time(new Date());
//        //创建人
//        bean.setCreate_user(vo.getMember_id());
        //是否删除
        bean.setIs_del("0");
        if (insert(bean) > 0) {
            return ResultInfo.success("支付成功！");
        } else {
            return ResultInfo.error("支付失败！");
        }
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link Integer}
     */
    public Integer saveData(PaymentOrder bean) {
        bean.setIs_del(GlobalData.ISDEL_NO);
        bean.setCreate_time(new Date());
        int count = this.insert(bean);
        return count;
    }


    /**
     * 柱状图 支付方式/金额
     */
    public List<PaymentOrder> getMoneysByType(RoadParkListVo vo) {
        List<PaymentOrder> list = paymentOrderMapper.getMoneysByType(vo);
        return list;
    }

    /**
     * 营收金额
     * 0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
     */
    public ReportVo getSumMoneys(PaymentOrderVo vo) {
        return paymentOrderMapper.getSumMoneys(vo);
    }

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    public List<DataChartVo> getMoneyDataByHours(PaymentOrderVo vo) {
        List<DataChartVo> list = paymentOrderMapper.getMoneyDataByHours(vo);
        return list;
    }

    /**
     * 支付流水明细分页查询
     */
    public PageInfo<PaymentOrder> toList(Integer pageIndex, Integer pageSize, PaymentOrderVo vo) {
        PageHelper.startPage(pageIndex, pageSize);
        List<PaymentOrder> list = paymentOrderMapper.toList(vo);
        PageInfo<PaymentOrder> page = new PageInfo<>(list);
        return page;
    }

    /**
     * 支付流水明细详情
     */
    public PaymentOrder toView(PaymentOrderVo vo) {
        PaymentOrder bean = paymentOrderMapper.selectById(vo.getPaymentId());
        return bean;
    }

    /**
     * 支付流水明细导出xls
     */
    public void exportPaymentOrder(PaymentOrderVo vo, HttpServletResponse response) {
        List<PaymentOrder> voList = paymentOrderMapper.toList(vo);
        List<Map<String, String>> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        voList.forEach(x -> {
            Map<String, String> map = new HashMap<>();
            map.put("payment_serialno", x.getPayment_serialno());
            map.put("payment_no", x.getPayment_no());
            //支付来源
            if (StringUtils.isNotEmpty(x.getPayment_resource())) {
                if ("1".equals(x.getPayment_resource())) {
                    map.put("payment_resource", "包月");
                }
                if ("2".equals(x.getPayment_resource())) {
                    map.put("payment_resource", "会员充值");
                }
                if ("3".equals(x.getPayment_resource())) {
                    map.put("payment_resource", "停车订单支付");
                }
                if ("4".equals(x.getPayment_resource())) {
                    map.put("payment_resource", "商家充值");
                }
            } else {
                map.put("payment_resource", "");
            }
            //支付方式
            if (StringUtils.isNotEmpty(x.getPayment_type())) {
                if ("1".equals(x.getPayment_type())) {
                    map.put("payment_type", "包月");
                } else if ("2".equals(x.getPayment_type())) {
                    map.put("payment_type", "微信");
                } else if ("3".equals(x.getPayment_type())) {
                    map.put("payment_type", "支付宝");
                } else if ("4".equals(x.getPayment_type())) {
                    map.put("payment_type", "钱包");
                } else if ("5".equals(x.getPayment_type())) {
                    map.put("payment_type", "现金");
                } else if ("6".equals(x.getPayment_type())) {
                    map.put("payment_type", "银行卡");
                } else if ("7".equals(x.getPayment_type())) {
                    map.put("payment_type", "商家支付");
                } else if ("8".equals(x.getPayment_type())) {
                    map.put("payment_type", "聚合支付");
                } else if ("9".equals(x.getPayment_type())) {
                    map.put("payment_type", "交通账户");
                }
            } else {
                map.put("payment_type", "");
            }
            //支付状态
            if (StringUtils.isNotEmpty(x.getStatus())) {
                if ("1".equals(x.getStatus())) {
                    map.put("status", "待支付");
                }
                if ("2".equals(x.getStatus())) {
                    map.put("status", "支付成功");
                }
                if ("3".equals(x.getStatus())) {
                    map.put("status", "支付失败");
                }
            } else {
                map.put("status", "");
            }
            //联合支付
            if (StringUtils.isNotEmpty(x.getIs_union_pay())) {
                if ("1".equals(x.getIs_union_pay())) {
                    map.put("is_union_pay", "是");
                } else {
                    map.put("is_union_pay", "否");
                }
            } else {
                map.put("is_union_pay", "否");
            }
            map.put("amount", x.getAmount());
            if (StringUtils.isNotNull(x.getCreate_time())) {
                map.put("create_time", df.format(x.getCreate_time()));
            } else {
                map.put("create_time", "");
            }
            if (StringUtils.isNotNull(x.getPay_time())) {
                map.put("pay_time", df.format(x.getPay_time()));
            } else {
                map.put("pay_time", "");
            }
            //是否退款
            if (StringUtils.isNotEmpty(x.getIs_refund())) {
                if ("1".equals(x.getIs_refund())) {
                    map.put("is_refund", "是");
                } else {
                    map.put("is_refund", "否");
                }
            } else {
                map.put("is_refund", "否");
            }
            if (StringUtils.isNotNull(x.getRefund_time())) {
                map.put("refund_time", df.format(x.getRefund_time()));
            } else {
                map.put("refund_time", "");
            }
            map.put("refund_amount", x.getRefund_amount());
            list.add(map);
        });

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("payment_serialno", "流水号");
        writer.addHeaderAlias("payment_no", "订单号");
        writer.addHeaderAlias("payment_resource", "支付来源");
        writer.addHeaderAlias("payment_type", "支付方式");
        writer.addHeaderAlias("status", "支付状态");
        writer.addHeaderAlias("is_union_pay", "联合支付");
        writer.addHeaderAlias("amount", "金额");
        writer.addHeaderAlias("create_time", "创建时间");
        writer.addHeaderAlias("pay_time", "支付完成时间");
        writer.addHeaderAlias("is_refund", "是否退款");
        writer.addHeaderAlias("refund_time", "退款时间");
        writer.addHeaderAlias("refund_amount", "退款金额");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("123.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            ResultInfo.success(outputStream);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultInfo.success(null);
    }


}

