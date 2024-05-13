package com.jsdc.zhtc.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.IdUtils;
import com.jsdc.zhtc.common.utils.ParkingOrderUtils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.dao.MonthlyManagementDao;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.mapper.MonthlyManagementMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.pay.WxPay;
import com.jsdc.zhtc.utils.ArithmeticUtils;
import com.jsdc.zhtc.vo.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jsdc.core.base.Base.notEmpty;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * 类 名: 包月管理
 * 描 述: MonthlyManagementService
 * 作 者: lw
 * 创 建：2022/1/4 10:20
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
public class MonthlyManagementService extends BaseService<MonthlyManagementDao, MonthlyManagement> {

    @Autowired
    private MonthlyManagementMapper mapper;
    @Autowired
    private OperateCarnoService operateCarnoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PaymonthlyParkingplaceService ppService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private MemberManageService memberManageService;
    @Autowired
    private MonthlyPaymentRecordService monthlyPaymentRecordService;
    @Autowired
    private WxPayConfigService configService;
    @Autowired
    private PaymonthlyConfigService paymonthlyConfigService;

    @Autowired
    private ParkService parkService;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private WxPay wxPay;
    @Value("${excel-export-path}")
    private String localPath;


    /**
     * 描 述： TODO(包月充值统计 按日期)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getTimeCensus(CommonVo data, Integer park_id) {

        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<CensusVo> timeCensus = new ArrayList<>();

            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    timeCensus = mapper.getTimeCensus(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    timeCensus = mapper.getTimeCensus(data);
                }
            } else {
                timeCensus = mapper.getTimeCensus(data);
            }
            PageInfo<CensusVo> listPage = new PageInfo<>(timeCensus);
            return ResultInfo.success(listPage);
        } else {
            List<CensusVo> timeCensus = new ArrayList<>();
            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    timeCensus = mapper.getTimeCensus(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    timeCensus = mapper.getTimeCensus(data);
                }
            } else {
                timeCensus = mapper.getTimeCensus(data);
            }

            return ResultInfo.success(timeCensus);
        }
    }

    /**
     * 描 述： TODO(包月充值统计 按车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getLicenceCensus(CommonVo data, Integer park_id) {

        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<CensusVo> licenceCensus = new ArrayList<>();

            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    licenceCensus = mapper.getLicenceCensus(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    licenceCensus = mapper.getLicenceCensus(data);
                }
            } else {
                licenceCensus = mapper.getLicenceCensus(data);
            }

            PageInfo<CensusVo> listPage = new PageInfo<>(licenceCensus);
            return ResultInfo.success(listPage);
        } else {
            List<CensusVo> licenceCensus = new ArrayList<>();

            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    licenceCensus = mapper.getLicenceCensus(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    licenceCensus = mapper.getLicenceCensus(data);
                }
            } else {
                licenceCensus = mapper.getLicenceCensus(data);
            }

            return ResultInfo.success(licenceCensus);
        }
    }


    /**
     * 描 述： TODO(车牌总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getLicenceCount(CommonVo data) {
        Integer licenceCount = mapper.getLicenceCount(data);
        return ResultInfo.success(licenceCount);
    }

    /**
     * 描 述： TODO(充值笔数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getRechargeCount(CommonVo data) {
        Integer rechargeCount = mapper.getRechargeCount(data);
        return ResultInfo.success(rechargeCount);
    }

    /**
     * 描 述： TODO(充值总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo getRechargeAmount(CommonVo data) {
        String rechargeAmount = mapper.getRechargeAmount(data);
        return ResultInfo.success(ArithmeticUtils.setScale1(rechargeAmount == null ? "0.0" : rechargeAmount, 2, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 描 述： TODO(查询在包月车辆总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link com.jsdc.zhtc.vo.ResultInfo}
     */
    public ResultInfo getNormalMPCount(CommonVo data) {
        Integer normalMPCount = mapper.getNormalMPCount(data);
        return ResultInfo.success(normalMPCount);
    }


    public ResultInfo num(String monthly_code, Integer strs) {
        Integer num = 0;
        List<MonthlyManagementVo> manageInfo = mapper.num(monthly_code);
        if (strs > manageInfo.get(0).getNum()) {
            num = strs - manageInfo.get(0).getNum();
        }
        return ResultInfo.success(num);
    }

    public ResultInfo allNum(String monthly_code) {
        List<MonthlyManagementVo> manageInfo = mapper.num(monthly_code);
        return ResultInfo.success(manageInfo.get(0).getNum());
    }

    //企业同一套餐下的所有车牌
    public ResultInfo info(String monthly_code) {

        List<MonthlyManagementVo> manageInfo = mapper.info(monthly_code);

        return ResultInfo.success(manageInfo);
    }

    /**
     * 描 述： TODO( 用户包月信息 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo findManageInfo(CommonVo data) {

        MonthlyManagementVo manageInfo = mapper.findManageInfo(data);

        return ResultInfo.success(manageInfo);
    }

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll(PageVo<MonthlyManagement> data) {

        MonthlyManagement bean = data.getBean();

        QueryWrapper<MonthlyManagement> wrapper = new QueryWrapper<>();

        if (bean != null) {

            if (bean.getIs_del() != null)
                wrapper.eq("is_del", bean.getIs_del());
        }
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());

            List<MonthlyManagement> monthlyManagements = this.selectList(wrapper);
            PageInfo<MonthlyManagement> page = new PageInfo<>(monthlyManagements);

            return ResultInfo.success(page);
        } else {
            List<MonthlyManagement> list = this.selectList(wrapper);
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
    public ResultInfo selectById(MonthlyManagement bean) {
        MonthlyManagement data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error(null, "未查询到数据");
        }
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return josn
     */
    public ResultInfo saveData(CommonVo<MonthlyManagement> data) {
        String[] result = data.getCarNos().split(",");
        String code = ParkingOrderUtils.getParkingIdByUUId("BY", null);
        Map<String, MonthlyManagement> arrayListMap = new HashMap<>();
        Integer num = 0;
        if (data.getIsTheCompany().equals("2")) {
            //修改操作
            if (StringUtils.isNotEmpty(data.getMonthly_code())) {
                QueryWrapper<MonthlyManagement> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("monthly_code", data.getMonthly_code());
                List<MonthlyManagement> monthlyManagement = this.selectList(queryWrapper);
                if (!monthlyManagement.isEmpty()) {
                    code = data.getMonthly_code();
                    for (MonthlyManagement m : monthlyManagement) {
                        //包含直接删，然后新增  。否则  修改为个人包月
//                            map.put(m.getCarno_id(),m.getPayment_id()) ;
//                            strings
                        this.deleteById(m);
                        arrayListMap.put(m.getCarno_id() + "", m);
                    }
                    if (result.length > monthlyManagement.get(0).getNum()) {
                        num = result.length;
                    } else {
                        num = monthlyManagement.get(0).getNum();
                    }
                }
            } else {
                num = result.length;
            }
        } else {
            num = 1;
        }

        for (int i = 0; i < result.length; i++) {
            SysUser user = sysUserService.getUser();
            MonthlyManagement bean = data.getBean();
            if (data.getIsTheCompany().equals("1")) {
                MonthlyManagement monthlyManagement = this.selectById(bean.getId());
                if (null != monthlyManagement) {
                    this.deleById(monthlyManagement);
                }
            }
            OperateCarno operateCarno = null;
            if ("1".equals(data.getIsTheCompany())) {
                operateCarno = operateCarnoService.findOperateCarno(data.getCarNo(), data.getCarType());
            } else if ("2".equals(data.getIsTheCompany())) {
                operateCarno = operateCarnoService.selectById(result[i]);
            }

            if (operateCarno != null)
                bean.setCarno_id(operateCarno.getId());
            else
                return ResultInfo.error(null, "车牌不存在 请先添加车牌");

            // 添加支付订单
            PaymentOrder paymentOrder = new PaymentOrder();
            MonthlyManagement monthlyManagement = arrayListMap.get(result[i]);
            if (monthlyManagement == null) {

                paymentOrder.setStatus("2");//订单状态
                paymentOrder.setPayment_type(data.getStr());//支付方式
                paymentOrder.setPayment_resource("1");//支付来源 包月
                paymentOrder.setAmount(data.getAmount());//支付金额
                paymentOrder.setTransferSerialNumber(data.getTransferSerialNumber());//支付金额
                paymentOrder.setPay_time(new Date());//支付时间
                //设置人id
                paymentOrder.setUpdate_user(user.getId());
                //创建人 创建时间 删除状态
                paymentOrder.setCreate_user(user.getId());
                paymentOrder.setCreate_time(new Date());
                paymentOrder.setIs_del(GlobalData.ISDEL_NO);

                paymentOrder.setMonths(bean.getMonths());
                PaymonthlyConfig paymonthlyConfig = paymonthlyConfigService.selectById(bean.getPaymonthly_config_id());
                paymentOrder.setMonths_unit_price(paymonthlyConfig.getPrice());

                paymentOrderService.insert(paymentOrder);
//                map.put(Integer.valueOf(result[i]),paymentOrder.getId()) ;
            }
            //先删再增
            //设置支付订单id
            if (monthlyManagement != null) {
                bean.setPayment_id(monthlyManagement.getPayment_id());
            } else {
                bean.setPayment_id(paymentOrder.getId());

            }
            bean.setNum(num);
            //设置结束时间
            bean.setEnd_time(ArithmeticUtils.subMonth(bean.getStart_time(), bean.getMonths()));
            //设置人id
            bean.setUpdate_user(user.getId());
            //创建人 创建时间 删除状态
            bean.setCreate_user(user.getId());
            bean.setCreate_time(new Date());
            bean.setIs_del(GlobalData.ISDEL_NO);
            bean.setTotal_cost(data.getAmount());
            bean.setIsTheCompany(data.getIsTheCompany());
            bean.setCompanyId(data.getCompanyId());
            bean.setIs_del(GlobalData.ISDEL_NO);
            bean.setId(null);
            bean.setMonthly_code(code);
            bean.setName(data.getName());
            bean.setPhone(data.getPhone());
            bean.setDwName(data.getDwName());
            if (this.insert(bean) < 0) {
                return ResultInfo.error(null, "包月办理失败");
            }
            bean.setId(null);
        }
        return ResultInfo.success(null, "包月办理成功");
    }

    /**
     * 包月续费
     * Author wzn
     * Date 2022/2/24 15:54
     */
    public ResultInfo xf(CommonVo<MonthlyManagement> data) {
        MonthlyManagement bean = data.getBean();
        String code = ParkingOrderUtils.getParkingIdByUUId("BY", null);
        //新增订单
        SysUser user = sysUserService.getUser();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStatus("2");//订单状态
        paymentOrder.setPayment_type(data.getStr());//支付方式
        paymentOrder.setPayment_resource("1");//支付来源 包月
        paymentOrder.setAmount(data.getAmount());//支付金额
        paymentOrder.setPay_time(new Date());//支付时间
        //设置人id
        paymentOrder.setUpdate_user(user.getId());
        //创建人 创建时间 删除状态
        paymentOrder.setCreate_user(user.getId());
        paymentOrder.setCreate_time(new Date());
        paymentOrder.setIs_del(GlobalData.ISDEL_NO);

        paymentOrder.setMonths(bean.getMonths());
        PaymonthlyConfig paymonthlyConfig = paymonthlyConfigService.selectById(bean.getPaymonthly_config_id());
        paymentOrder.setMonths_unit_price(paymonthlyConfig.getPrice());

        paymentOrderService.insert(paymentOrder);

        QueryWrapper<MonthlyManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monthly_code", data.getMonthly_code());
        List<MonthlyManagement> monthlyManagementList = this.selectList(queryWrapper);
        if (!monthlyManagementList.isEmpty()) {
            for (MonthlyManagement m : monthlyManagementList) {
                m.setPayment_id(paymentOrder.getId());
                m.setMonthly_code(code);
                m.setStart_time(m.getEnd_time());
                m.setEnd_time(ArithmeticUtils.subMonth(m.getEnd_time(), data.getBean().getMonths()));
                m.setMonths(data.getBean().getMonths());
                m.setCreate_user(user.getId());
                m.setCreate_time(new Date());
                m.setTotal_cost(data.getAmount());
                m.setId(null);
                this.insert(m);
            }
        }
        return ResultInfo.success(null, "包月办理成功");
    }


    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo updateInfo(MonthlyManagement bean) {

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
    public ResultInfo deleById(MonthlyManagement bean) {
        bean.setIs_del(GlobalData.ISDEL_YES);
        return updateInfo(bean);
        /*if ( deleteById(bean.getId())>0 ) {
            return ResultInfo.success("删除成功");
        } else {
            return ResultInfo.error("操作失败");
        }*/
    }

    /**
     * 描 述： TODO(批量删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo deleByIdList(CommonVo data) {
        String[] ids = data.getStr().split(",");

        for (int i = 0; i < ids.length; i++) {
            MonthlyManagement bean = new MonthlyManagement();
            bean.setId(Integer.parseInt(ids[i]));
            bean.setIs_del(GlobalData.ISDEL_YES);
            if (this.updateById(bean) > 0)
                continue;
            else
                return ResultInfo.error(null, "操作失败");
        }
        return ResultInfo.success(null, "操作成功");
    }

    /**
     * create by wp at 2022/1/10 15:15
     * description: 判断当前是否存在已绑定包月配置的用户
     *
     * @param ids
     * @return java.lang.Long
     */
    public Long getCountByConfigIds(List<String> ids, String endTime) {
        LambdaQueryWrapper<MonthlyManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MonthlyManagement::getPaymonthly_config_id, ids)
                .eq(MonthlyManagement::getIs_del, GlobalData.ISDEL_NO)
                .lt(MonthlyManagement::getEnd_time, endTime);
        return selectCount(wrapper);
    }


    /**
     * 包月信息
     *
     * @param car_id
     * @param parkingplace_id
     * @param parkType
     * @param dateString
     * @return
     */
    public List<MonthlyManagement> selectInfoByCarId(Integer car_id, Integer parkingplace_id, String parkType, String dateString) {
        return mapper.selectInfoByCarId(car_id, parkingplace_id, parkType, dateString);
    }


    public static void main(String[] args) {
//        Date now = new Date();
//        Date date = DateUtil.offsetMonth(now, 12);
//        System.out.println(date);

//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c1.setTime(new DateTime().toDate());
//        c2.setTime(new DateTime().plusMonths(3).toDate());
//        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
//        int month = c2.get(Calendar.MONTH) + year * 12 - c1.get(Calendar.MONTH);
//        System.out.println(month);
//        System.out.println(new DateTime().toString("yyyy-MM-dd"));
//        System.out.println(new DateTime().plusMonths(3).toString("yyyy-MM-dd"));

//        Calendar c1 = Calendar.getInstance();
//
//        Calendar c2 = Calendar.getInstance();
//        c1.setTime(new DateTime().toDate());
//        c2.setTime(new DateTime().plusMonths(3).toDate());
//
//        int year1 = c1.get(Calendar.YEAR);
//        int year2 = c2.get(Calendar.YEAR);
//        int month1 = c1.get(Calendar.MONTH);
//        int month2 = c2.get(Calendar.MONTH);
//
//        int diffYear = year2 - year1;
//        int diffMonth = month2 - month1;
//        int diff = diffYear * 12 + diffMonth;
//        System.out.println(diff);
//        System.out.println(new DateTime().toString("yyyy-MM-dd"));
//        System.out.println(new DateTime().plusMonths(3).toString("yyyy-MM-dd"));

//        System.out.println(new DateTime().getYear());
//        System.out.println(new DateTime().plusMonths(1).getYear());
//        System.out.println(new DateTime().plusMonths(1).getMonthOfYear());
    }


    /**
     * 描 述： TODO(查询包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType
     * @param dateStr
     * @return {@link Integer}
     */
    public Integer getMonthlyPaymentCount(String parkinType, String dateStr, Integer park_id) {
        Integer monthlyPaymentCount = mapper.getMonthlyPaymentCount(parkinType, dateStr, park_id);
        return monthlyPaymentCount;
    }

    /**
     * 描 述： TODO(合计包月总费用)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param parkinType 统计类型
     * @param dateStr    创建时间 年月日
     * @return {@link String}
     */
    public String getMonthlyPaymentSumCost(String parkinType, String dateStr, Integer park_id) {
        String sumCost = mapper.getMonthlyPaymentSumCost(parkinType, dateStr, park_id);
        return ArithmeticUtils.setScale1(sumCost == null ? "0" : sumCost, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 包月收入
     */
    public String getMonthlyPaymentSum(CommonVo vo) {
        String sumCost = mapper.getMonthlyPaymentSum(vo);
        return ArithmeticUtils.setScale1(sumCost == null ? "0" : sumCost, 2, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 停车场
     * 包月收入明细
     */
    public ResultInfo getMonthlyPaymentData(CommonVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<MonthlyManagementVo> list = mapper.getMonthlyPaymentData(vo);
        PageInfo<MonthlyManagementVo> listPage = new PageInfo<>(list);
        return ResultInfo.success(listPage);
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 包月统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public List<ParkRsVo> getMonthlyRentCount(CommonVo data) {
        List<ParkRsVo> monthlyRentCount = mapper.getMonthlyRentCount(data);
        return monthlyRentCount;
    }

    public List<ParkRsVo> getMonthlyRentCountParkAndRoad(CommonVo data) {
        List<ParkRsVo> parkRsVos = mapper.getMonthlyRentCountParkAndRoad(data);
        return parkRsVos;
    }

    /**
     * 描 述： TODO(月租车明细) 分页
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<MonthCarRentalDetailVo>}
     */
    public PageInfo<MonthCarRentalDetailVo> getMonthCarRentalDetailPage(CommonVo data) {
        PageHelper.startPage(data.getPageNum(), data.getPageSize());
        List<MonthCarRentalDetailVo> list = mapper.getMonthCarRentalDetail(data);
        PageInfo<MonthCarRentalDetailVo> listPage = new PageInfo<>(list);
        return listPage;
    }

    /**
     * 描 述： TODO(月租车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<MonthCarRentalDetailVo>}
     */
    public List<MonthCarRentalDetailVo> getMonthCarRentalDetail(CommonVo data) {

        List<MonthCarRentalDetailVo> monthCarRentalDetail = mapper.getMonthCarRentalDetail(data);
        return monthCarRentalDetail;
    }

    /**
     * 描 述： TODO(月租车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ShouldBillRankingVo}
     */
    public ShouldBillRankingVo getMonthCarRentalStatistics(CommonVo data) {

        ShouldBillRankingVo sbrVo = mapper.getMonthCarRentalStatistics(data);
        if (StringUtils.isNotBlank(sbrVo.getSumAmount()) && ArithmeticUtils.compareTo2(sbrVo.getSumAmount(), "0")) {
            sbrVo.setSumAmount(ArithmeticUtils.setScale1(sbrVo.getSumAmount(), 2, BigDecimal.ROUND_HALF_UP).toString());
        } else {
            sbrVo.setSumAmount("0");
        }
        return sbrVo;
    }

    /**
     * Author wzn
     * Date 2022/2/25 9:41
     */
    public String byExport(String isTheCompany, String parkingType) {
        TemplateExportParams params = null;
        if ("1".equals(isTheCompany)) {
            params = new TemplateExportParams("个人包月导出.xlsx");
        } else if ("2".equals(isTheCompany)) {
            params = new TemplateExportParams("公司包月导出.xlsx");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<MonthlyManagementVo> monthlyManagementVos = mapper.byExport(isTheCompany, parkingType);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < monthlyManagementVos.size(); i++) {
            MonthlyManagementVo bean = monthlyManagementVos.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            if ("1".equals(isTheCompany)) {
                lm.put("name", bean.getName());
                lm.put("phone", bean.getPhone());
                lm.put("carNo", bean.getCarNo());
                //(1蓝牌、2绿牌、3黄牌)
                lm.put("carType", bean.getCarTypeName());
//                if ("1".equals(bean.getCarType())) {
//                    lm.put("carType", "蓝牌");
//                } else if ("2".equals(bean.getCarType())) {
//                    lm.put("carType", "绿牌");
//                } else if ("3".equals(bean.getCarType())) {
//                    lm.put("carType", "黄牌");
//                }
            }
            lm.put("configName", bean.getConfigName());
            lm.put("start_time", bean.getStartTime());
            lm.put("end_time", bean.getEndTime());
            lm.put("months", bean.getMonths().toString());
            //支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付)
            lm.put("paymentType", bean.getPaymentTypeName());
            lm.put("amount", bean.getAmount());
//            lm.put("transferSerialNumber", bean.getTransferSerialNumber());
            lm.put("userName", bean.getUserName());
            lm.put("create_time", bean.getCreateTime());
            if ("2".equals(isTheCompany)) {
                lm.put("company_name", bean.getCompany_name());
            }
            listMap.add(lm);
        }

        map.put("maplist", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File(localPath);
        FileOutputStream fos = null;
        String name = IdUtils.simpleUUID() + ".xlsx";
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        try {
            fos = new FileOutputStream(localPath + name);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }


    public ResultInfo readExcel(InputStream fis, String excelName) throws Exception {
        boolean is03Excel = excelName.matches("^.+\\.(?i)(xlsx)$") ? true : false;
        Workbook workbook = is03Excel ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("Sheet1");//得到工作表
        int count = 0;
        int addNum = 0;//成功
        int notAddNum = 0;//失败
        String carNoMSG = "录入有误的车牌如下：";//车牌错误提示
        int carNoNum = 0;
        Integer code = 0;
        for (Row row : sheet) {
            try {
                // 跳过第一目录
                if (count < 1) {
                    count++;
                    continue;
                }
                //如果当前行没有数据，跳出循环
                MonthlyManagement monthlyManagement = new MonthlyManagement();
                //姓名
                if (row.getCell(0) != null && !row.getCell(0).equals("")) {
                    Cell cell = row.getCell(0);
                    cell.setCellType(STRING);
                    monthlyManagement.setName(cell.getStringCellValue());
                }
                //联系方式
                if (row.getCell(1) != null && !row.getCell(1).equals("")) {
                    Cell cell = row.getCell(1);
                    cell.setCellType(STRING);
                    monthlyManagement.setPhone(cell.getStringCellValue());
                }

                //车牌
                if (row.getCell(2) != null && !row.getCell(2).equals("")) {
                    Cell cell = row.getCell(2);
                    cell.setCellType(STRING);

                    String carNo = cell.getStringCellValue();

                    //车牌类型(1蓝牌、2绿牌、3黄牌)
                    String car_type = "1";
                    if (carNo.length() == 8) {
                        car_type = "2";
                    }
                    if (carNo.equals("沪AAC6872")) {
                        System.out.println(carNo);
                    }
                    OperateCarno operateCarno = operateCarnoService.findOperateCarno(carNo, car_type);
                    if (notEmpty(operateCarno)) {
                        monthlyManagement.setCarno_id(operateCarno.getId());
                        monthlyManagement.setCar_no(operateCarno.getCar_no());
                    } else {
                        OperateCarno operateCarno1 = new OperateCarno();
                        operateCarno1.setCar_no(carNo.toUpperCase());
                        operateCarno1.setCar_type(car_type);
                        operateCarno1.setRoster_type("1");
                        operateCarno1.setName(monthlyManagement.getName());
                        operateCarno1.setPhone(monthlyManagement.getPhone());
                        operateCarno1.setCreate_time(new Date());
                        operateCarno1.setCreate_user(sysUserService.getUser().getId());
                        operateCarno1.setIs_del("0");
                        if (operateCarnoService.insert(operateCarno1) > 0) {
                            monthlyManagement.setCarno_id(operateCarno1.getId());
                            monthlyManagement.setCar_no(operateCarno1.getCar_no());
                        }
                    }
                }
                //包月方案(名称)
                if (row.getCell(3) != null && !row.getCell(3).equals("")) {
                    Cell cell = row.getCell(3);
                    cell.setCellType(STRING);

                    //配置名称
                    String name = cell.getStringCellValue();
                    QueryWrapper<PaymonthlyConfig> wrapper = new QueryWrapper<>();
                    wrapper.eq("is_del", GlobalData.ISDEL_NO);
                    wrapper.eq("name", name);
                    wrapper.orderByDesc("update_time");
                    List<PaymonthlyConfig> monthlyManagementList = paymonthlyConfigService.selectList(wrapper);
                    if (notEmpty(monthlyManagementList) && monthlyManagementList.size() > 0) {
                        monthlyManagement.setPaymonthly_config_id(monthlyManagementList.get(0).getId());
                    }
                }
                //	有效开始日期
                if (row.getCell(4) != null && !row.getCell(4).equals("")) {
                    if (row.getCell(4).getCellType() == NUMERIC) {
                        monthlyManagement.setStart_time(row.getCell(4).getDateCellValue());
                    } else if (row.getCell(4).getCellType() == STRING) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                        Date free_time_start = sdf.parse(row.getCell(4).getStringCellValue() + " 00:00:00");
                        monthlyManagement.setStart_time(free_time_start);
                    }
                }
                //有效结束日期
                if (row.getCell(5) != null && !row.getCell(5).equals("")) {
                    Calendar c = Calendar.getInstance();
                    if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                        c.setTime(row.getCell(5).getDateCellValue());
                        c.add(Calendar.DATE, 1);
                        monthlyManagement.setEnd_time(c.getTime());
                    } else if (row.getCell(5).getCellType() == STRING) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                        Date free_time_end = sdf.parse(row.getCell(5).getStringCellValue() + " 00:00:00");
                        c.setTime(free_time_end);
                        c.add(Calendar.DATE, 1);
                        monthlyManagement.setEnd_time(c.getTime());
                    }
                }
                //支付金额(元)
                if (row.getCell(6) != null && Base.notEmpty(row.getCell(6))) {
                    Cell cell = row.getCell(6);
                    cell.setCellType(STRING);
                    monthlyManagement.setTotal_cost(cell.getStringCellValue());
                }

                //根据车牌和类型查询库中是否已经存在此信息
                if (StringUtils.isEmpty(monthlyManagement.getName()) ||
                        StringUtils.isEmpty(monthlyManagement.getPhone()) ||
                        StringUtils.isEmpty(monthlyManagement.getTotal_cost()) ||
                        StringUtils.isNull(monthlyManagement.getCarno_id()) ||
                        StringUtils.isNull(monthlyManagement.getPaymonthly_config_id()) ||
                        StringUtils.isNull(monthlyManagement.getStart_time()) ||
                        StringUtils.isNull(monthlyManagement.getEnd_time())) {
                    code = -1;
                } else {
                    boolean b = TimeUtils.carPlate(monthlyManagement.getCar_no());
                    if (b) {
                        Integer ncount = saveUpd(monthlyManagement);
                        if (ncount > 0) {
                            addNum++;
                        } else {
                            notAddNum++;
                        }
                    } else {
                        carNoMSG += monthlyManagement.getCar_no() + ",";
                        carNoNum++;
                        notAddNum++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultInfo.error("数据填写有误,请核对输入信息！");
            }
        }
        fis.close();
        fis.close();
        if (code == -1) {
            String msg = "字段操作失败！请按照要求填写,其中执行成功：" + addNum;
            if (carNoNum > 0) {
                msg += "，" + carNoMSG;
            }
            return ResultInfo.error(msg);
        }
        String msg = "成功执行数量" + addNum + "，失败执行数量：" + notAddNum;
        if (carNoNum > 0) {
            msg += "，" + carNoMSG;
        }

        //批量导入个人包月日志
        SysLog sysLog = new SysLog();
        sysLog.setLog_type(LogEnums.LOG_ADDMONTHLYMANAGEMENT.getValue());
        sysLog.setLog_content("批量导入个人包月：" + msg);
        sysLogService.saveLog(sysLog);
        return ResultInfo.success(msg);
    }

    /**
     * 导入保存更新包月
     */
    public Integer saveUpd(MonthlyManagement monthlyManagement) {
        String code = ParkingOrderUtils.getParkingIdByUUId("BY", null);

        QueryWrapper<MonthlyManagement> queryWrapper = new QueryWrapper();
        queryWrapper.eq("carno_id", monthlyManagement.getCarno_id())
                .eq("paymonthly_config_id", monthlyManagement.getPaymonthly_config_id())
                .eq("isTheCompany", "1")
                .eq("is_del", "0")
                .orderByDesc("end_time");
        List<MonthlyManagement> monthlyManagementList = mapper.selectList(queryWrapper);
        if (notEmpty(monthlyManagementList) && monthlyManagementList.size() > 0) {
            MonthlyManagement monthlyManagement1 = monthlyManagementList.get(0);

            String startTime = new DateTime(monthlyManagement.getStart_time()).toString("yyyy-MM-dd");
            String startTime1 = new DateTime(monthlyManagement1.getStart_time()).toString("yyyy-MM-dd");
            String endTime = new DateTime(monthlyManagement.getEnd_time()).toString("yyyy-MM-dd");
            String endTime1 = new DateTime(monthlyManagement1.getEnd_time()).toString("yyyy-MM-dd");

            //若存在相同数据，则更新覆盖数据
            if (startTime.equals(startTime1) && endTime.equals(endTime1)) {
                monthlyManagement1.setUpdate_time(new Date());
                monthlyManagement1.setUpdate_user(sysUserService.getUser().getId());
                ResultInfo re = this.updateInfo(monthlyManagement1);

                int num = 0;
                if (0 == re.getCode()) {
                    num = num + 1;
                }
                return num;
            } else {
                //若截止日期大于数据日期则累加金额和月份以及覆盖截止日期
                if (monthlyManagement.getEnd_time().compareTo(monthlyManagement1.getEnd_time()) > 0) {
//                    System.out.println("date1晚于date2");

                    //数据库开始时间
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(monthlyManagement1.getStart_time());

                    //导入数据结束时间
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(monthlyManagement.getEnd_time());

                    int year1 = c1.get(Calendar.YEAR);
                    int year2 = c2.get(Calendar.YEAR);
                    int month1 = c1.get(Calendar.MONTH);
                    int month2 = c2.get(Calendar.MONTH);

                    int diffYear = year2 - year1;
                    int diffMonth = month2 - month1;
                    int diff = diffYear * 12 + diffMonth;

                    PaymentOrder paymentOrder = new PaymentOrder();
                    paymentOrder.setStatus("2");//订单状态
                    paymentOrder.setPayment_type("5");//支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付)
                    paymentOrder.setPayment_resource("1");//支付来源 包月
                    paymentOrder.setAmount(monthlyManagement.getTotal_cost());//支付金额
                    paymentOrder.setPay_time(new Date());//支付时间
                    //设置人id
                    paymentOrder.setUpdate_user(sysUserService.getUser().getId());
                    //创建人 创建时间 删除状态
                    paymentOrder.setCreate_user(sysUserService.getUser().getId());
                    paymentOrder.setCreate_time(new Date());
                    paymentOrder.setIs_del(GlobalData.ISDEL_NO);
                    paymentOrder.setRemarks("个人包月批量导入");

                    //本次包月月数
                    Calendar c11 = Calendar.getInstance();
                    c11.setTime(monthlyManagement.getStart_time());
                    Calendar c22 = Calendar.getInstance();
                    c22.setTime(monthlyManagement.getEnd_time());

                    int year11 = c11.get(Calendar.YEAR);
                    int year22 = c22.get(Calendar.YEAR);
                    int month11 = c11.get(Calendar.MONTH);
                    int month22 = c22.get(Calendar.MONTH);

                    int diffYear2 = year22 - year11;
                    int diffMonth2 = month22 - month11;
                    int diff2 = diffYear2 * 12 + diffMonth2;
                    paymentOrder.setMonths(diff2);
                    PaymonthlyConfig paymonthlyConfig = paymonthlyConfigService.selectById(monthlyManagement1.getPaymonthly_config_id());
                    paymentOrder.setMonths_unit_price(paymonthlyConfig.getPrice());

                    paymentOrderService.insert(paymentOrder);

                    //续费
                    monthlyManagement1.setUpdate_time(new Date());
                    monthlyManagement1.setUpdate_user(sysUserService.getUser().getId());
//                    monthlyManagement1.setPayment_id(paymentOrder.getId());
                    monthlyManagement1.setEnd_time(monthlyManagement.getEnd_time());
                    monthlyManagement1.setMonths(diff);
                    ResultInfo re = this.updateInfo(monthlyManagement1);

                    MonthlyPaymentRecord monthlyPaymentRecord = new MonthlyPaymentRecord();
                    monthlyPaymentRecord.setMonthly_management_id(monthlyManagement1.getId());
                    monthlyPaymentRecord.setPayment_id(paymentOrder.getId());
                    monthlyPaymentRecord.setIs_complete(1);
                    monthlyPaymentRecord.setExt(JSON.toJSONString(monthlyManagement1));
                    monthlyPaymentRecordService.insert(monthlyPaymentRecord);

                    int num = 0;
                    if (0 == re.getCode()) {
                        num = num + 1;
                    }
                    return num;
                }
                return 0;
            }
        } else {
            //新增包月记录
//            monthlyManagement.setNum(1);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(monthlyManagement.getStart_time());

            Calendar c2 = Calendar.getInstance();
            c2.setTime(monthlyManagement.getEnd_time());

            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int month2 = c2.get(Calendar.MONTH);

            int diffYear = year2 - year1;
            int diffMonth = month2 - month1;
            int diff = diffYear * 12 + diffMonth;

            //设置结束时间
            monthlyManagement.setMonths(diff);

            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setStatus("2");//订单状态
            paymentOrder.setPayment_type("5");//支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付)
            paymentOrder.setPayment_resource("1");//支付来源 包月
            paymentOrder.setAmount(monthlyManagement.getTotal_cost());//支付金额
            paymentOrder.setPay_time(new Date());//支付时间
            //设置人id
            paymentOrder.setUpdate_user(sysUserService.getUser().getId());
            //创建人 创建时间 删除状态
            paymentOrder.setCreate_user(sysUserService.getUser().getId());
            paymentOrder.setCreate_time(new Date());
            paymentOrder.setIs_del(GlobalData.ISDEL_NO);
            paymentOrder.setRemarks("个人包月批量导入");

            paymentOrder.setMonths(diff);
            PaymonthlyConfig paymonthlyConfig = paymonthlyConfigService.selectById(monthlyManagement.getPaymonthly_config_id());
            paymentOrder.setMonths_unit_price(paymonthlyConfig.getPrice());

            paymentOrderService.insert(paymentOrder);

            //设置人id
            monthlyManagement.setUpdate_user(sysUserService.getUser().getId());
            //创建人 创建时间 删除状态
            monthlyManagement.setCreate_user(sysUserService.getUser().getId());
            monthlyManagement.setCreate_time(new Date());
            monthlyManagement.setIs_del(GlobalData.ISDEL_NO);
            monthlyManagement.setIsTheCompany("1"); //是否公司1个人  2公司
            monthlyManagement.setId(null);
            monthlyManagement.setMonthly_code(code);
            monthlyManagement.setPayment_id(paymentOrder.getId());
            monthlyManagement.setTransact_style("0");
            return insert(monthlyManagement);
        }
    }


    /**
     * 今日停车订单数排名
     * 包月收入
     * 微信小程序包月金额统计
     */
    public List<PaymentOrder> getMonthlyAmountGroup(CommonVo data) {
        return mapper.getMonthlyAmountGroup(data);
    }


    /**
     * 支付流水明细关联包月订单
     */
    public List<MonthlyManagementVo> getMonthlyDataByPaymentId(Integer paymentId) {
        return mapper.getMonthlyDataByPaymentId(paymentId);
    }

    public ResultInfo exportExcel(CommonVo vo, HttpServletResponse response) {
        List<MonthlyManagementVo> list;

        //停车场
        list = mapper.getMonthlyPaymentData(vo);

        ExcelWriter writer = ExcelUtil.getWriter();

        writer.addHeaderAlias("carNo", "车牌");
        writer.addHeaderAlias("create_time", "包月时间");
        writer.addHeaderAlias("months_unit_price", "单价");
        writer.addHeaderAlias("months", "月数");
        writer.addHeaderAlias("amount", "总费用");
        writer.addHeaderAlias("configName", "包月方案");

        writer.setOnlyAlias(true);
        writer.write(list, true);

        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("包月收入明细.xls", "UTF-8"));
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

}
