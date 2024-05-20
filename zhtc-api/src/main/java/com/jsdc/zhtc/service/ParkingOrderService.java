package com.jsdc.zhtc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.*;
import com.jsdc.zhtc.dao.ParkingOrderDao;
import com.jsdc.zhtc.mapper.ParkingOrderMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.pay.AlPay;
import com.jsdc.zhtc.pay.WxPay;
import com.jsdc.zhtc.utils.*;
import com.jsdc.zhtc.vo.*;
import com.jsdc.zhtc.vo.order.AppealRecordVo;
import com.jsdc.zhtc.vo.order.PaymentVo;
import com.jsdc.zhtc.vo.ticket.AlWxPayInfo;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.SelectProvider;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;

/**
 * ClassName: ParkingOrderService
 * Description:停车场停车订单管理表(parking_order)
 * date: 2022/1/4 15:56
 *
 * @author wh
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class ParkingOrderService extends BaseService<ParkingOrderDao, ParkingOrder> {

    Logger logger = LoggerFactory.getLogger(ParkingOrderService.class);

    @Autowired
    private AlPay alPay;
    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;
    @Value("${excel-export-path}")
    private String localPath;
    @Value("${jsdc.misstakeTime}")
    private String misstakeTime;

    @Autowired
    private ParkingReleaseService parkingReleaseService;
    @Autowired
    private ParkingOrderMapper mapper;
    @Autowired
    private OperateCarnoService carnoService;
    @Autowired
    private ParkDeviceService deviceService;
    @Autowired
    private OperateCarnoService operateCarnoService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private OperateAppealService operateAppealService;
    @Autowired
    private RefundManagementService refundManagementService;
    @Autowired
    private ParkingOrderPicsService detailsService;
    @Autowired
    private AlPayConfigService alPayConfigService;
    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private AppealHandleRecordService recordService;
    @Autowired
    private AppealHandleVoucherService handleVoucherService;
    @Autowired
    private AppealNoticeFeedbackService appealNoticeFeedbackService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ParkService parkService;
    @Autowired
    private TrafficControlUtils trafficControlUtils;
    @Autowired
    private ParkingOrderPicsService parkingOrderPicsService;
    @Autowired
    private ParkDeviceService parkDeviceService;
    @Autowired
    private ParkingGateUtils parkingGateUtils;
    @Autowired
    private WxPayConfigService wxPayConfigService;
    @Autowired
    private WxPay wxPay;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WhiteListService whiteListService;

    public Long getCountByAreaId(String areaId, String startTime, String endTime) {
        LambdaQueryWrapper<ParkingOrder> wrapper = new LambdaQueryWrapper();
//        wrapper.eq(ParkingOrder::getArea_id, areaId);
        wrapper.eq(ParkingOrder::getIs_del, GlobalData.ISDEL_NO);
        if (StringUtils.isNotEmpty(startTime)) {
            wrapper.ge(ParkingOrder::getDriveout_time, startTime + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            wrapper.le(ParkingOrder::getDriveout_time, endTime + " 23:59:59");
        }
        return selectCount(wrapper);
    }

    /**
     * create by wp at 2022/1/18 9:22
     * description: 根据区域查询停车场数量
     *
     * @param areaId
     * @return java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.String>>
     */
    public List<Map<String, String>> getSumByArea(String areaId, String startTime, String endTime) {
        return mapper.getSumByArea(areaId, startTime, endTime);
    }

    /**
     * create by wp at 2022/1/18 9:30
     * description: 查询停车场订单数量
     *
     * @param parkId
     * @param startTime
     * @param endTime
     * @return java.lang.Long
     */
    public Long getOrderCount(Integer parkId, String startTime, String endTime) {
        LambdaQueryWrapper<ParkingOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkingOrder::getPark_id, parkId);
        wrapper.gt(ParkingOrder::getCreate_time, startTime + " 00:00:00");
        wrapper.lt(ParkingOrder::getCreate_time, endTime + " 23:59:59");
        wrapper.eq(ParkingOrder::getIs_del, GlobalData.ISDEL_NO);
        return selectCount(wrapper);
    }

    /**
     * create by wp at 2022/1/18 8:50
     * description: 停车总时长
     *
     * @param parkId
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer getSumTimeByPark(Integer parkId, String startTime, String endTime) {
        return mapper.getSumTimeByPark(parkId, startTime, endTime);
    }


    /**
     * create by zonglina at 2022/1/4 15:17
     * description:分页查询路段订单功能
     * 根据车牌号、创建时间查询订单
     *
     * @return : null
     * @param:null
     */
    public ResultInfo selectByPage(RoadOrParkingCommentVo bean, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ParkingOrderVo> list = mapper.selectPageList(bean);
//        list.forEach(a -> {
//            //计算费用
//            if (null == a.getDriveout_time() || "".equals(a.getDriveout_time())) {
//                PaymentVo pay = getParkingCharging(String.valueOf(a.getId()), null);
//                if (null != pay) {
//                    a.setSum_amount(pay.getPay_money());
//                    a.setResTime(TimeUtils.formatTime(Integer.valueOf(pay.getResitime())));
//                } else {
//                    a.setSum_amount("0");
//                    a.setResTime("0");
//                }
//            }
//        });
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * 现金核销
     * 分页查询
     * thr
     *
     * @param bean
     * @return
     */
    public ResultInfo selectHxPageList(ParkingOrderVo bean, int pageIndex, int pageSize) {
        //订单支付方式  字典 1包月、2微信、3支付宝、4钱包、5现金
        bean.setPay_type("5");
        //停车场管理员 筛选数据
        SysUser sysUser = sysUserService.getUser();
        if (sysUser.getUser_type().equals("1")) {
            bean.setPark_id(sysUser.getPark_id());
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<ParkingOrderVo> list = mapper.selectHxPageList(bean);
        list.forEach(l -> {
            l.setResTime(TimeUtils.formatTime(Integer.valueOf(l.getResitime())));
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageInfo", new PageInfo<>(list));
        jsonObject.put("money", mapper.getSumMoney(bean).toString());
        return ResultInfo.success(jsonObject);
    }

    //根据订单查询详情
    public RoadOrParkingVo orderDetails(Integer id) {
        //基本信息
        RoadOrParkingVo orders = mapper.selectByWxId(id);
        orders.setCar_no(orders.getCar_no().toUpperCase());
        if (Base.notEmpty(orders.getCreate_time())) {
            String substring = orders.getCreate_time().substring(0, orders.getCreate_time().indexOf("."));
            orders.setCreate_time(substring);
        }
        if (null != orders.getResitime()) {
            orders.setResitime(TimeUtils.formatTime(Integer.valueOf(orders.getResitime())));
        }
        //进出场信息图片（驶入、离开）
        List<String> sc = detailsService.selectByPid(orders.getId(), GlobalData.PARKING_DIRECTION_IN);
        if (CollectionUtils.isNotEmpty(sc)) {
            List<FileManage> scList = fileManageService.selectByFileList(sc);
            // 根据创建时间 倒序排序
            Collections.sort(scList, new Comparator<FileManage>() {
                @Override
                public int compare(FileManage o1, FileManage o2) {
                    return o2.getCreate_time().compareTo(o1.getCreate_time());
                }
            });
            // 判断scList的数量是否大约2,大于2则只取后两个
            if (scList.size() > 2) {
                scList = scList.subList(0, 2);
            }
            orders.setScList(scList);
        }
        List<String> sl = detailsService.selectByPid(orders.getId(), GlobalData.PARKING_DIRECTION_OUT);
        if (CollectionUtils.isNotEmpty(sl)) {
            List<FileManage> slList = fileManageService.selectByFileList(sl);
            // 根据创建时间 倒序排序
            Collections.sort(slList, new Comparator<FileManage>() {
                @Override
                public int compare(FileManage o1, FileManage o2) {
                    return o2.getCreate_time().compareTo(o1.getCreate_time());
                }
            });
            // 判断scList的数量是否大约2,大于2则只取后两个
            if (slList.size() > 2) {
                slList = slList.subList(0, 2);
            }
            orders.setSlList(slList);
//            // 判断scList的数量是否大约2,大于2则只取后两个
//            if (slList.size() > 2) {
//                slList = slList.subList(slList.size() - 2, slList.size());
//            }
//            orders.setSlList(slList);
        }
        HashMap<String, SysDict> free_type_map = DcCacheDataUtil.getMapDicts("free_type");
//        免费类型
        if (notEmpty(orders.getFree_type())) {
            orders.setFree_type(free_type_map.get(orders.getFree_type()).getLabel());
        }
        return orders;
    }


    /**
     * create by zonglina at 2022/1/4 21:47
     * description:结束计时||订单结束传入出场时间
     * type不为空弄等于结束计时
     *
     * @return : null
     * @param:null
     */
    public ResultInfo closureParkingOrder(ParkingOrder bean) {
        //计算入场时间和出场时间相差多少分钟
        if (null != bean.getDrivein_time() && null != bean.getDriveout_time()) {
            bean.setResitime(TimeUtils.computeMinute(bean.getDrivein_time(), bean.getDriveout_time()));
        }
        if (updateById(bean) > 0) {
            return ResultInfo.success("操作成功！");
        } else {
            return ResultInfo.success("操作失败！");
        }
    }


    /**
     * 停车场驶入
     */
    public ResultInfo entry(ParkingOrderVo bean, Park park, ParkDevice parkDevice) {
        logger.info("===========================entry 停车场驶入:" + bean.getEventType() + "=============================");

        //停车场类型 1.急诊 2地下 3普通停车场
        if (park.getPark_grade().equals("1")) {
            //停车场类型 1.急诊停车场

            //查询车牌号码是否存在
            OperateCarno operateCarno = searchCarNo(bean);

            //判断是否月租、院内车辆停车时长超过累计规定时间
            if (operateCarno.getRoster_type().equals("2") || operateCarno.getRoster_type().equals("3")) {
                //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)

                //若超过，则不允许驶入
//                try {
//                    //语音播报
//                    parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), "无权限不准驶入");
//                } catch (Exception e) {
//                    logger.error("急诊停车场,不允许驶入语音播报异常");
//                }

                //若未超过，则允许驶入
                //生成本次停车订单
                createOrder(bean, park, parkDevice);

                //插入数据
                if (insert(bean) > 0) {
                    /**
                     * 判断该停车场是否开启限流
                     * 限流开关 0开启 1关闭
                     */
                    if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                        if (notEmpty(park.getFree_count()) && park.getFree_count() > 0) {
                            //车辆驶入更新空闲车位-1
                            park.setFree_count(park.getFree_count() - 1);
                            parkService.updateById(park);
                        }
                    }

                    //驶入图片保存操作
                    List<ParkingOrderPics> detailsList = bean.getDetails();
                    handlePictue(detailsList, bean.getId(), GlobalData.PARKING_DIRECTION_IN);

                    //开闸入场并语音播报
                    try {
                        parkingGateUtils.mqttOpenGate(bean.getDrivein_gate(), bean.getCar_no() + ",欢迎光临");
                    } catch (Exception e) {
                        logger.error("急诊停车场开闸异常");
                    }
                    return ResultInfo.success("急诊停车场订单生成成功！");
                } else {
                    //语音播报
                    parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), "急诊停车场订单生成失败");
                    logger.error("急诊停车场订单生成失败");
                    return ResultInfo.success("急诊停车场订单生成失败！");
                }
            }
        } else if (park.getPark_grade().equals("2")) {
            //2地下停车场

            //查询车牌号码是否存在
            OperateCarno operateCarno = searchCarNo(bean);

            //判断车牌是否为白名单
            QueryWrapper<WhiteList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("park_id", park.getId());
            queryWrapper.eq("car_id", operateCarno.getId());
            List<WhiteList> whiteLists = whiteListService.selectList(queryWrapper);
            if (whiteLists.size() == 0) {
                //车牌不是白名单 不允许驶入
                try {
                    //语音播报
                    parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), "无权限不准驶入");
                } catch (Exception e) {
                    logger.error("地下停车场,车牌不是白名单,不允许驶入语音播报异常");
                }
                return ResultInfo.error("地下停车场,车牌不是白名单,不允许驶入");
            }

            //生成本次停车订单
            createOrder(bean, park, parkDevice);

            //插入数据
            if (insert(bean) > 0) {
                /**
                 * 判断该停车场是否开启限流
                 * 限流开关 0开启 1关闭
                 */
                if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                    if (notEmpty(park.getFree_count()) && park.getFree_count() > 0) {
                        //车辆驶入更新空闲车位-1
                        park.setFree_count(park.getFree_count() - 1);
                        parkService.updateById(park);
                    }
                }

                //驶入图片保存操作
                List<ParkingOrderPics> detailsList = bean.getDetails();
                handlePictue(detailsList, bean.getId(), GlobalData.PARKING_DIRECTION_IN);

                //开闸入场并语音播报
                try {
                    parkingGateUtils.mqttOpenGate(bean.getDrivein_gate(), bean.getCar_no() + ",欢迎光临");
                } catch (Exception e) {
                    logger.error("地下停车场开闸异常");
                }
                return ResultInfo.success("地下停车场订单生成成功！");
            } else {
                //语音播报
                parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), "地下停车场订单生成失败");
                logger.error("地下停车场订单生成失败");
                return ResultInfo.success("地下停车场订单生成失败！");
            }

        } else if (park.getPark_grade().equals("3")) {
            // 3普通停车场

            //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌)
            if (bean.getCar_type().equals("4")) {
                //白牌车直接放行

                //开闸入场并语音播报
                try {
                    parkingGateUtils.mqttOpenGate(bean.getDrivein_gate(), bean.getCar_no() + ",欢迎光临");
                } catch (Exception e) {
                    logger.error("普通停车场白牌车开闸异常");
                }
                return ResultInfo.success("白牌车直接放行");
            }

            //查询车牌号码是否存在
            OperateCarno operateCarno = searchCarNo(bean);

            //主停车场驶入规则
            entryPt(bean, park, parkDevice, operateCarno);
        }

        return ResultInfo.success();
    }


    /**
     * 查询车牌号码是否存在
     */
    public OperateCarno searchCarNo(ParkingOrderVo bean) {
        //查询车牌号码
        OperateCarno operateCarno = carnoService.selectOne(new QueryWrapper<OperateCarno>()
                .eq("car_no", bean.getCar_no())
                .eq("car_type", bean.getCar_type())
                .eq("is_del", "0"));
        //判断车牌是否存在
        if (notEmpty(operateCarno)) {
            //车牌id
            bean.setCarno_id(operateCarno.getId());

            return operateCarno;
        } else {
//            logger.info("未查询到车牌号：" + bean.getCar_no());
            //新增车牌
            OperateCarno carno = new OperateCarno();
            carno.setCar_no(bean.getCar_no());
            carno.setCar_type(bean.getCar_type());
            //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
            carno.setRoster_type("5");
            carno.setCreate_time(new Date());
            carno.setIs_del(GlobalData.ISDEL_NO);

            //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌 5黑牌)
            if (carno.getCar_type().equals("3")) {
                // 车型，1：小型车；2：中型车；3：大型车
                carno.setVehicle_type("2");
            } else {
                carno.setVehicle_type("1");
            }

            carnoService.insert(carno);
            //车牌id
            bean.setCarno_id(carno.getId());

            return carno;
        }
    }

    /**
     * 查询是否存在在停和已缴费的订单
     * 删除已存在的在停订单
     * 已缴费订单设置成已完成
     */
    public void updOrder(ParkingOrderVo bean) {
        //删除已存在的在停订单
        UpdateWrapper<ParkingOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(ParkingOrder::getIs_del, "1")
                .set(ParkingOrder::getUpdate_time, "1")
                .eq(ParkingOrder::getPark_id, bean.getPark_id())
                .eq(ParkingOrder::getCarno_id, bean.getCar_id())
                .eq(ParkingOrder::getStatus, "1") //订单状态 1在停、2 待缴费、3已缴费、4已完成
                .eq(ParkingOrder::getIs_del, "0");
        update(null, updateWrapper);

        //已缴费订单设置成已完成
        UpdateWrapper<ParkingOrder> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.lambda()
                .set(ParkingOrder::getStatus, "4")
                .eq(ParkingOrder::getPark_id, bean.getPark_id())
                .eq(ParkingOrder::getCarno_id, bean.getCar_id())
                .eq(ParkingOrder::getStatus, "3") //订单状态 1在停、2 待缴费、3已缴费、4已完成
                .eq(ParkingOrder::getIs_del, "0");
        update(null, updateWrapper2);
    }

    /**
     * 车辆驶入
     * 生成停车订单
     */
    public void createOrder(ParkingOrderVo bean, Park park, ParkDevice parkDevice) {
        //驶入之前对象bean已赋值的变量
//        vo.setCar_no(StringUtils.isEmpty(dcParkInfo.getPlate_num()) ? "" : dcParkInfo.getPlate_num().toUpperCase()); //车牌号码
//        vo.setCar_type("1");  //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌)
//        vo.setParkCode(park.getPark_code());//停车场编号
//        vo.setPark_id(park.getId());//停车场id
//        vo.setDrivein_gate(deviceCode);  //驶入闸机设备编号
//        vo.setDrivein_time(dateFormat.parse(dcParkInfo.getLocal_time())); //进场时间
//        vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);  //订单来源：摄像机
//        bean.setCarno_id(carno.getId()); //车牌id

        bean.setPark_id(park.getId());//停车场id
        bean.setOrder_no(ParkingOrderUtils.getParkingIdByUUId(GlobalData.ROLE_P, park.getId()));//订单号
        bean.setUid(ParkingOrderUtils.createUid(park.getPark_code(), parkDevice.getSerialNo()));  //流水号 上传交控使用的唯一标识
        bean.setCreate_time(new Date());
        bean.setUpdate_time(new Date());
        bean.setSum_amount("0");//应收金额
        bean.setPaid_amount("0");//已付金额/实收金额
        bean.setUnpaid_amount("0");  //待付金额/欠费金额
        bean.setDiscount_amount("0");//优惠金额
        bean.setStatus("1");//订单状态 1在停、2 待缴费、3已缴费、4已完成
        bean.setIs_upload("0");//是否上传交管 0否 1是
        bean.setIs_del("0");
        if (empty(bean.getOperation_type_in())) {
            //0、正常 1、遥控  null未离场
            bean.setOperation_type_in("0");
        }
        bean.setEntry_type("0"); //出入类型 0驶入 1驶出
    }

    /**
     * 车辆驶入
     * 上传市交控数据
     */
    public void setTrafficInData(ParkingOrderVo bean, Park park, OperateCarno operateCarno) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //参数封装
        UploadCarInDataVo vo = new UploadCarInDataVo();
        vo.setUid(bean.getUid());// 流水号，一次停车的进场、离场、缴费信息等要保证uid相同
        vo.setArriveID(bean.getId().toString());    // 入场记录id
        vo.setPlateNo(operateCarno.getCar_no());    // 车牌号
        vo.setPlateColor(setTrafficPlateColor(operateCarno.getCar_type()));// 车牌颜色，1：蓝色；2：黄色；3：白色；4：黑色；5：绿色
        vo.setCarType(operateCarno.getVehicle_type());    // 车型，1：小型车；2：中型车；3：大型车
        vo.setParkingCode(bean.getParkCode());    // 停车场编号
        vo.setEntryNum(bean.getDrivein_gate());    // 入口编号
        vo.setTotalBerthNum(park.getPark_num().toString());    // 泊位总数
        vo.setOpenBerthNum(park.getOpenBerthNum().toString());    // 开放泊位数
        vo.setFreeBerthNum("0");    // 剩余开放泊位数
        vo.setArriveTime(dateFormat.format(bean.getDrivein_time()));    // 入场时间（格式：yyyy-MM-dd HH:mm:ss）

        //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
        if (operateCarno.getRoster_type().equals("3")) {
            vo.setParkingType("2");    // 停车类型，1：临时停车；2：包月停车；3：共享停车；4：特殊停车
        } else {
            vo.setParkingType("1");    // 停车类型，1：临时停车；2：包月停车；3：共享停车；4：特殊停车
        }

        //上传车辆入场记录
        try {
            parkService.uploadCarInData(vo);

            //更新停车订单
            bean.setIs_upload("1");//是否上传交管 0否 1是
            updateById(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统车牌颜色转换为市交控的车牌颜色
     *
     * @param plateColor 系统  车牌类型(1蓝牌、2绿牌、3黄牌、4白牌 5黑牌)
     * @return
     */
    public String setTrafficPlateColor(String plateColor) {
        //市交控 车牌颜色，1：蓝色；2：黄色；3：白色；4：黑色；5：绿色
        if (plateColor.equals("1")) {
            return "1";//1蓝牌
        } else if (plateColor.equals("2")) {
            return "5";//绿牌
        } else if (plateColor.equals("3")) {
            return "2";//黄牌
        } else if (plateColor.equals("4")) {
            return "3";//白牌
        } else if (plateColor.equals("5")) {
            return "4";//白牌
        } else {
            return "1";
        }
    }

    /**
     * 主停车场驶入规则
     */
    public ResultInfo entryPt(ParkingOrderVo bean, Park park, ParkDevice parkDevice, OperateCarno operateCarno) {
        //查询是否存在在停和已缴费的订单
        updOrder(bean);

        //生成本次停车订单
        createOrder(bean, park, parkDevice);

        //插入数据
        if (insert(bean) > 0) {
            //开闸入场并语音播报
            try {
                parkingGateUtils.mqttOpenGate(bean.getDrivein_gate(), bean.getCar_no() + ",欢迎光临");
            } catch (Exception e) {
                logger.error("entryPt开闸异常");
            }

            /**
             * 判断该停车场是否开启限流
             * 限流开关 0开启 1关闭
             */
            if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                if (notEmpty(park.getFree_count()) && park.getFree_count() > 0) {
                    //车辆驶入更新空闲车位-1
                    park.setFree_count(park.getFree_count() - 1);
                    parkService.updateById(park);
                }
            }

            //驶入图片保存操作
            List<ParkingOrderPics> detailsList = bean.getDetails();
            handlePictue(detailsList, bean.getId(), GlobalData.PARKING_DIRECTION_IN);

            /**
             * 上传市交控数据
             */
            setTrafficInData(bean, park, operateCarno);

            return ResultInfo.success("订单生成成功！");
        } else {
            //语音播报
            parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), "订单生成失败");

            logger.error("订单生成失败");
            return ResultInfo.success("订单生成失败！");
        }
    }

    /**
     * 停车场订单出场接口
     */
    public ResultInfo exit(ParkingOrderVo bean) {
        //========================驶出状态不为6
        System.out.println("===========================驶出:" + bean.getEventType() + "=============================");
        Park park = parkService.selectOne(Wrappers.<Park>lambdaQuery().eq(Park::getPark_code, bean.getParkCode()).eq(Park::getIs_del, GlobalData.ISDEL_NO));
        ParkDevice parkDevice = parkDeviceService.selectOne(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getPark_id, park.getId()).eq(ParkDevice::getDevice_code, bean.getOutChannelId()).eq(ParkDevice::getIs_del, GlobalData.ISDEL_NO));

        if (null != parkDevice && StringUtils.equals(parkDevice.getPassageway(), "1")) {
            logger.info("非出口生成订单");
            return ResultInfo.error("非出口生成订单");
        }

        if (!GlobalData.EVENTTYPE.equals(bean.getEventType())) {
            //白牌车辆直接放行，不生成订单
            if (GlobalData.CAR_TYPE_WHITE.equals(bean.getCar_type())) {
                // todo 抬杆
                try {
                    if (StringUtils.equals(park.getBrand(), "dc")) {
                        String content = "";
                        content += bean.getCar_no() + ",";
                        content += "请缴费0元 一路顺风";
                        parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                    }
                } catch (Exception e) {
                    logger.info(e.toString());
                    return ResultInfo.success();
                }
                return ResultInfo.success();
            }
            if (null == parkDevice) {
                logger.error("未查询到设备信息，device:" + bean.getOutChannelId());
                return ResultInfo.error("未查询到设备信息，device:" + bean.getOutChannelId());
            }

            OperateCarno operateCarno = carnoService.selectOne(new QueryWrapper<OperateCarno>()
                    .eq("car_no", bean.getCar_no()).eq("car_type", bean.getCar_type())
                    .eq("is_del", "0"));
            if (null != operateCarno) {
                bean.setCarno_id(operateCarno.getId());
            } else {
                logger.info("未查询到车牌号：" + bean.getCar_no());
                OperateCarno carno = new OperateCarno();
                carno.setCar_no(bean.getCar_no());
                carno.setCar_type(bean.getCar_type());
//                carno.setRoster_type(GlobalData.ROSTER_TYPE_ORDINARY);
                carno.setCreate_time(new Date());
                carno.setIs_del(GlobalData.ISDEL_NO);
                carnoService.insert(carno);
                bean.setCarno_id(carno.getId());
            }
            List<String> statusList = new ArrayList<>();
            statusList.add(GlobalData.PARKING_ORDER_STOP);
            statusList.add(GlobalData.PARKING_ORDER_ALREADYPAY);
            statusList.add(GlobalData.PARKING_ORDER_STAYPAY);
            List<ParkingOrder> parkingOrders = selectList(new QueryWrapper<ParkingOrder>()
                    .in("status", statusList)
                    .eq("is_del", "0")
                    .eq("park_id", park.getId())
                    .eq("carno_id", bean.getCarno_id())
                    .orderByDesc("update_time"));
            //判断是否是临时通行订单，如果是临时通行订单，则判断有没有再规定时间内驶离，
            // 若未驶离，则把这条单据设置为已完成，新生成一条正常收费记录，
            // 如果在规定时间内，则直接放行
            ParkingOrder tempOrder = null;
            if (parkingOrders != null && parkingOrders.size() > 0) {
                if (GlobalData.FREETYPELSTX.equals(parkingOrders.get(0).getFree_type())) {
                    tempOrder = parkingOrders.get(0);
                    //判断当前时间是否在临时通行截止时间之内,驶出时间大于临停截止时间
                    if (bean.getDriveout_time().after(tempOrder.getTempDriveOutTime())) {
                        tempOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                        tempOrder.setUpdate_time(new Date());
                        tempOrder.setSum_amount("0");
                        tempOrder.setDiscount_amount("0");
                        tempOrder.setPaid_amount("0");
                        tempOrder.setDriveout_time(tempOrder.getTempDriveOutTime());
                        tempOrder.setResitime(TimeUtils.computeMinute(tempOrder.getDrivein_time(), tempOrder.getDriveout_time()));
                        tempOrder.setUnpaid_amount("0");
                        tempOrder.updateById();
                        //如果有需要计费的临时通行订单，设置成完成，所以应该把在停订单设置为空
//                        parkingOrders = null;
                    }
                }
            }
            /**
             * 如果在出场时没有查到该车辆任何的在停、待缴费、已缴费订单，则存在以下两种情况
             * 1.无已完成订单：
             *      场景：停车收费建成时车辆就在停车场内。
             *      操作：直接开闸。
             * 2.有已完成订单：
             *      场景：车辆上一次出场后并未开出而是退回了停车场。
             *      操作：取最近一次的已完成订单，用上一个订单的出场时间作为入场时间，用本次出场时间作为出场时间生成一个待支付订单。
             * 3.如果是临时停车扫码之后未驶出，且需要缴费，则生成一条新计费订单，
             */
            if (CollectionUtils.isEmpty(parkingOrders)
                    || (tempOrder != null && GlobalData.PARKING_ORDER_COMPLETE.equals(tempOrder.getStatus()))) {
                List<ParkingOrder> completeOrders = selectList(Wrappers.<ParkingOrder>lambdaQuery()
                        .eq(ParkingOrder::getIs_del, GlobalData.ISDEL_NO)
                        .eq(ParkingOrder::getStatus, GlobalData.PARKING_ORDER_COMPLETE)
                        .eq(ParkingOrder::getPark_id, park.getId())
                        .eq(ParkingOrder::getCarno_id, bean.getCarno_id())
                        .orderByDesc(ParkingOrder::getDriveout_time));
                //找不到在停、代缴费、已缴费停车订单，也找不到已完成的停车订单
                if (CollectionUtils.isEmpty(completeOrders)) {
                    // todo 抬杆
                    try {
                        if (StringUtils.equals(park.getBrand(), "dc")) {
                            String content = "";
                            content += bean.getCar_no() + ",";
                            content += "请缴费0元 一路顺风";
                            parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                        } else {
//                            parkingGateUtils.wxopenGate(park.getPark_code(), parkDevice.getChannel_id());
                        }
                    } catch (Exception e) {
                        logger.info(e.toString());
                    }
                } else {
                    //找不到在停、代缴费、已缴费停车订单，只能找到已完成的停车订单
                    ParkingOrder parkingOrder = completeOrders.get(0);
                    PaymentVo paymentVo = getParkingCharging(new AppealRecordVo(String.valueOf(parkingOrder.getId()), bean.getDriveout_time()));
                    BigDecimal allAmount = new BigDecimal(paymentVo.getPay_money()).subtract(new BigDecimal(paymentVo.getDiscount_money()));
                    BigDecimal paiedAmount = new BigDecimal(parkingOrder.getPaid_amount());
                    //实际产生费用大于已支付费用，生成新订单
                    if (allAmount.compareTo(paiedAmount) == 1) {
                        Integer paymentId = parkingOrder.getPayment_id();
                        //PaymentOrder paymentOrder = paymentOrderService.selectById(paymentId);
                        String parkingIdByUUId = ParkingOrderUtils.getParkingIdByUUId(GlobalData.ROLE_P, park.getId());
                        ParkingOrder newOrder = new ParkingOrder();

                        newOrder.setSum_amount(allAmount.subtract(paiedAmount).toString());
                        newOrder.setDiscount_amount("0");
                        newOrder.setPaid_amount("0");
                        newOrder.setUnpaid_amount(allAmount.subtract(paiedAmount).toString());
                        //免费类型 0：包月 1：白名单 2：免费时段 3：特殊开闸
                        if (parkingOrder.getFree_type().equals("0") || parkingOrder.getFree_type().equals("1")) {
                            newOrder.setFree_type(parkingOrder.getFree_type());
                            newOrder.setSum_amount("0");
                            newOrder.setUnpaid_amount("0");
                        }

                        newOrder.setOrder_no(parkingIdByUUId);
//                        newOrder.setArea_id(park.getArea_id());
//                        newOrder.setStreet_id(park.getStreet_id());
                        newOrder.setPark_id(park.getId());
                        newOrder.setIs_del(GlobalData.ISDEL_NO);

                        //如果上一条是临时停车，则这次新增的记录，使用临停驶离截止时间当做新订单的驶入订单
                        if (GlobalData.FREETYPELSTX.equals(parkingOrder.getFree_type())) {
                            newOrder.setDrivein_time(parkingOrder.getTempDriveOutTime());
                        } else {
                            newOrder.setDrivein_time(parkingOrder.getDriveout_time());
                        }
                        newOrder.setCarno_id(operateCarno.getId());
                        newOrder.setSource(parkingOrder.getSource());
                        //
                        if (GlobalData.FREETYPEBY.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEBMD.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEQYBY.equals(parkingOrder.getFree_type())) {
                            newOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                        } else {
                            newOrder.setStatus(GlobalData.PARKING_ORDER_STAYPAY);
                        }

                        newOrder.setIs_upload(GlobalData.ISUPLOAD);
//                        newOrder.setIs_merge("0");
//                        newOrder.setIs_invoice(GlobalData.ISUPLOAD);
                        newOrder.setDriveout_gate(parkDevice.getDevice_code());
                        newOrder.setDriveout_time(bean.getDriveout_time());
                        newOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDriveout_time(), bean.getDriveout_time()));
                        newOrder.setCreate_time(new Date());
                        newOrder.setUpdate_time(new Date());
                        insert(newOrder);
                        List<ParkingOrderPics> detailsList = bean.getDetails();
                        handlePictue(detailsList, newOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);
                        //路外车辆预出场上报 发送至市交平台
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderNo", newOrder.getOrder_no());
                        map.put("billId", "");
                        Integer tempParkId = newOrder.getPark_id();
                        Park park1 = null;
                        if (null != tempParkId) {
                            park1 = parkService.selectById(tempParkId);
                            if (null != park1) {
                                map.put("parkCode", park1.getTraffic_park_code());
                            }
                        }
                        Integer tempCarno = newOrder.getCarno_id();
                        OperateCarno operateCarno1 = null;
                        if (null != tempCarno) {
                            operateCarno1 = operateCarnoService.selectById(tempCarno);
                            if (null != operateCarno1) {
                                map.put("plateNo", operateCarno1.getCar_no());
                            }
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        map.put("enterTime", simpleDateFormat.format(newOrder.getDrivein_time()));
                        map.put("costTime", simpleDateFormat.format(new Date()));
                        map.put("parkPeriodTime", newOrder.getResitime());
                        map.put("payStatus", 0);
                        BigDecimal bigDecimal = new BigDecimal("100");
                        String tempTotal = new BigDecimal(newOrder.getSum_amount()).multiply(bigDecimal).toString();
                        String temp2 = tempTotal;
                        if (tempTotal.contains(".")) {
                            String[] temp1 = tempTotal.split("\\.");
                            temp2 = temp1[0];
                        }

                        map.put("totalCost", Integer.parseInt(temp2));
                        String freeMoney = new BigDecimal(newOrder.getDiscount_amount()).multiply(bigDecimal).toString();
                        String temp4 = freeMoney;
                        if (freeMoney.contains(".")) {
                            String[] temp3 = freeMoney.split("\\.");
                            temp4 = temp3[0];
                        }

                        map.put("freeMoney", Integer.parseInt(temp4));
                        String preMoney = new BigDecimal(newOrder.getPaid_amount()).multiply(bigDecimal).toString();
                        String temp6 = preMoney;
                        if (preMoney.contains(".")) {
                            String[] temp5 = preMoney.split("\\.");
                            temp6 = temp5[0];
                        }
                        map.put("preMoney", Integer.parseInt(temp6));

                        String payMoney = new BigDecimal(newOrder.getUnpaid_amount()).multiply(bigDecimal).toString();
                        String temp8 = payMoney;
                        if (payMoney.contains(".")) {
                            String[] temp7 = payMoney.split("\\.");
                            temp8 = temp7[0];
                        }

                        map.put("payMoney", Integer.parseInt(temp8));
                        //TODO 上报交控
                        try {
                            TrafficControlUtils.postOuterCarPrePassOutData(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (GlobalData.FREETYPEBY.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEBMD.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEQYBY.equals(parkingOrder.getFree_type())) {
                            // todo 抬杆
                            /**
                             * 判断该停车场是否开启限流
                             * 限流开关 0开启 1关闭
                             */
                            if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                                if (notEmpty(park.getFree_count())) {
                                    //车辆驶离更新空闲车位+1
                                    park.setFree_count(park.getFree_count() + 1);
                                    parkService.updateById(park);
                                }
                            }
                            try {
                                if (StringUtils.equals(park.getBrand(), "dc")) {
                                    String content = "";
                                    content += bean.getCar_no() + ",";
                                    content += "一路顺风";
                                    parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                                }
                            } catch (Exception e) {
                                logger.info(e.toString());
                            }
                        }
                        return ResultInfo.success();

                    } else {
                        // todo 抬杆
                        /**
                         * 判断该停车场是否开启限流
                         * 限流开关 0开启 1关闭
                         */
                        if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                            if (notEmpty(park.getFree_count())) {
                                //车辆驶离更新空闲车位+1
                                park.setFree_count(park.getFree_count() + 1);
                                parkService.updateById(park);
                            }
                        }
                        try {
                            if (StringUtils.equals(park.getBrand(), "dc")) {
                                String content = "";
                                content += bean.getCar_no() + ",";
                                content += "请缴费0元 一路顺风";
                                parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                            }
                        } catch (Exception e) {
                            logger.info(e.toString());
                        }
                    }
                }

                return ResultInfo.success();
            }
            ParkingOrder parkingOrder = parkingOrders.get(0);
            //如果该订单的免费类型为包月或白名单或者临时通行，则更新订单状态为已完成，各费用为0，然后开闸
            if (GlobalData.FREETYPEBY.equals(parkingOrder.getFree_type())
                    || GlobalData.FREETYPEBMD.equals(parkingOrder.getFree_type())
                    || GlobalData.FREETYPELSTX.equals(parkingOrder.getFree_type())
                    || GlobalData.FREETYPEQYBY.equals(parkingOrder.getFree_type())) {
                parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                parkingOrder.setUpdate_time(new Date());
                parkingOrder.setSum_amount("0");
                parkingOrder.setDiscount_amount("0");
                parkingOrder.setPaid_amount("0");
                parkingOrder.setDriveout_time(bean.getDriveout_time());
                parkingOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), parkingOrder.getDriveout_time()));
                parkingOrder.setUnpaid_amount("0");

                //上传驶离图片
                List<ParkingOrderPics> detailsList = bean.getDetails();
                handlePictue(detailsList, parkingOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);
                // todo 抬杆
                /**
                 * 判断该停车场是否开启限流
                 * 限流开关 0开启 1关闭
                 */
                if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                    if (notEmpty(park.getFree_count())) {
                        //车辆驶离更新空闲车位+1
                        park.setFree_count(park.getFree_count() + 1);
                        parkService.updateById(park);
                    }
                }
                try {
                    if (StringUtils.equals(park.getBrand(), "dc")) {
                        //包月车辆定制化语音播报
                        if (GlobalData.FREETYPEBY.equals(parkingOrder.getFree_type())) {

//                            if (CollectionUtils.isNotEmpty(monthlyManagements)) {
//                                Long start = bean.getDriveout_time().getTime();
//                                Long diffNm = end - start;
//                                Long diffDay = diffNm / 24 / 60 / 60 / 1000;
//
//                                String content = "";
//                                content += bean.getCar_no() + ",";
//                                content += "月租车 剩余" + (diffDay > 0 ? diffDay : 0) + "天 ";
//                                content += "一路顺风";
//                                parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
//                            } else {
                            String content = "";
                            content += bean.getCar_no() + ",";
                            content += "请缴费0元 一路顺风";
                            parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
//                            }
                        } else {
                            String content = "";
                            content += bean.getCar_no() + ",";
                            content += "请缴费0元 一路顺风";
                            parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                        }

                    }
                    parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
                } catch (Exception e) {
                    String content = "";
                    content += bean.getCar_no() + ",";
                    content += "请缴费0元 一路顺风";
                    parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                    logger.info(e.toString());
                }
                updateById(parkingOrder);
                //路外车辆预出场上报 发送至市交平台
                Map<String, Object> map = new HashMap<>();
                map.put("orderNo", parkingOrder.getOrder_no());
                map.put("billId", parkingOrder.getId());
                Integer tempParkId = parkingOrder.getPark_id();
                Park park1 = null;
                if (null != tempParkId) {
                    park1 = parkService.selectById(tempParkId);
                    if (null != park1) {
                        map.put("parkCode", park1.getTraffic_park_code());
                    }
                }
                Integer tempCarno = parkingOrder.getCarno_id();
                OperateCarno operateCarno1 = null;
                if (null != tempCarno) {
                    operateCarno1 = operateCarnoService.selectById(tempCarno);
                    if (null != operateCarno1) {
                        map.put("plateNo", operateCarno1.getCar_no());
                    }
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("enterTime", simpleDateFormat.format(parkingOrder.getDrivein_time()));
                map.put("costTime", simpleDateFormat.format(new Date()));
                map.put("parkPeriodTime", parkingOrder.getResitime());
                map.put("payStatus", 1);
                map.put("totalCost", 0);
                map.put("freeMoney", 0);
                map.put("preMoney", 0);
                map.put("payMoney", 0);
                //TODO 上报交控
                try {
                    TrafficControlUtils.postOuterCarPrePassOutData(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //路外车辆出场记录 对接市交接口 同步数据
                Map<String, Object> map1 = new HashMap<>();
                map1.put("actTime", simpleDateFormat.format(new Date()));
                map1.put("actType", 2);
                if (null != park) {
                    map1.put("parkCode", park.getTraffic_park_code());
                }
                map1.put("deviceCode", parkingOrder.getDriveout_gate());
                if (null != operateCarno1) {
                    map1.put("plateNo", operateCarno1.getCar_no());
                }

                Integer tempColor = 0;
                if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_BLUE)) {
                    tempColor = 1;
                } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_GREEN)) {
                    tempColor = 5;
                } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_YELLOW)) {
                    tempColor = 2;
                } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_WHITE)) {
                    tempColor = 4;
                }
                map1.put("plateColor", tempColor);
                List<ParkingOrderPics> detailsListTemp = bean.getDetails();
                if (CollectionUtils.isNotEmpty(detailsListTemp)) {
                    String url = detailsListTemp.get(0).getPicture_url();
                    map1.put("picUrl", loadPicPath2 + url);
                }
                //TODO 上报交控
                try {
                    TrafficControlUtils.postOuterCarPassOutData(map1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //上报泊位 对接市交接口
                Map<String, Object> map2 = new HashMap<>();
                map2.put("parkCode", park.getTraffic_park_code());
                String tempTime = simpleDateFormat.format(new Date());
                map2.put("createTime", tempTime);
                map2.put("totalPlaceCount", park.getPark_num());
                //停车场剩余泊位数
                map2.put("reducePlaceCount", "");
                //TODO 上报交控
                try {
                    TrafficControlUtils.postPlaceData(map2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ResultInfo.success();
            }
            /**
             * 车辆驶离时停车订单有可能存在两种状态：
             * 1.在停状态，处于该状态说明车主并没有预付费，则在出厂时计算费用即可。
             * 2.已缴费状态，处于该状态说明车主已进行预付费，该情况下会产生两种变化：
             *      a.停车时长产生的实际费用超出预付费用；需生成新的停车订单，新订单的费用来源于停车总时长费用-预付费用。
             *      b.停车时长产生的实际费用未超出预付费用；无需产生新的停车订单。
             */
            if (GlobalData.PARKING_ORDER_STOP.equals(parkingOrder.getStatus()) || GlobalData.PARKING_ORDER_STAYPAY.equals(parkingOrder.getStatus())) {//车辆在停或待缴费
                BeanUtil.copyProperties(bean, parkingOrder, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                PaymentVo paymentVo = getParkingCharging(new AppealRecordVo(String.valueOf(parkingOrder.getId()), bean.getDriveout_time()));
                //查询是否有商家优惠(需要单独增加商家优惠多少和商家是否进行了优惠)
//                PaymentVo vos = roadParkingOrderService.selectBusinessCarno(operateCarno.getId(), operateCarno.getCar_no(),
//                        operateCarno.getCar_type(), String.valueOf(parkingOrder.getId()), 1, bean.getDriveout_time(), paymentVo.getPay_money());
//                if ("1".equals(vos.getStatus()) || "2".equals(vos.getStatus())) {
//                    paymentVo.setPay_money(vos.getZf_money());
//                    parkingOrder.setIs_discount("0");//是
//                    parkingOrder.setDiscount_money(vos.getDiscount_money());//优惠金额
//                }
                parkingOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), bean.getDriveout_time()));
                //收费金额计算
                if (new BigDecimal(paymentVo.getPay_money()).compareTo(BigDecimal.ZERO) == 0) {
                    parkingOrder.setFree_type(GlobalData.FREETYPETIME);
                    parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                    parkingOrder.setUpdate_time(new Date());
                    parkingOrder.setSum_amount("0");
                    parkingOrder.setDiscount_amount("0");
                    parkingOrder.setPaid_amount("0");
                    parkingOrder.setUnpaid_amount("0");
                    //处理图片
                    List<ParkingOrderPics> detailsList = bean.getDetails();
                    handlePictue(detailsList, parkingOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);
                    // todo 抬杆
                    /**
                     * 判断该停车场是否开启限流
                     * 限流开关 0开启 1关闭
                     */
                    if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                        if (notEmpty(park.getFree_count())) {
                            //车辆驶离更新空闲车位+1
                            park.setFree_count(park.getFree_count() + 1);
                            parkService.updateById(park);
                        }
                    }
                    try {
                        if (StringUtils.equals(park.getBrand(), "dc")) {
                            String content = "";
                            content += bean.getCar_no() + ",";
                            content += "请缴费0元 一路顺风";
                            parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                        }
                        parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
                    } catch (Exception e) {
                        logger.info(e.toString());
                    }
                    updateById(parkingOrder);


                    //路外车辆出场记录 对接市交接口 同步数据
                    Map<String, Object> map1 = new HashMap<>();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    map1.put("actTime", simpleDateFormat.format(new Date()));
                    map1.put("actType", 2);
                    Park park1 = null;
                    Integer tempParkId = parkingOrder.getPark_id();
                    if (null != tempParkId) {
                        park1 = parkService.selectById(tempParkId);
                    }
                    if (null != park1) {
                        map1.put("parkCode", park1.getTraffic_park_code());
                    }
                    Integer tempCarno = parkingOrder.getCarno_id();
                    OperateCarno operateCarno1 = null;
                    if (null != tempCarno) {
                        operateCarno1 = operateCarnoService.selectById(tempCarno);
                    }
                    map1.put("deviceCode", parkingOrder.getDriveout_gate());
                    if (null != operateCarno1) {
                        map1.put("plateNo", operateCarno1.getCar_no());
                    }
                    Integer tempColor = 0;
                    if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_BLUE)) {
                        tempColor = 1;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_GREEN)) {
                        tempColor = 5;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_YELLOW)) {
                        tempColor = 2;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_WHITE)) {
                        tempColor = 4;
                    }
                    map1.put("plateColor", tempColor);
                    List<ParkingOrderPics> detailsListTemp = bean.getDetails();
                    if (CollectionUtils.isNotEmpty(detailsListTemp)) {
                        String url = detailsListTemp.get(0).getPicture_url();
                        map1.put("picUrl", loadPicPath2 + url);
                    }
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postOuterCarPassOutData(map1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //上报泊位 对接市交接口
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("parkCode", park.getTraffic_park_code());
                    String tempTime = simpleDateFormat.format(new Date());
                    map2.put("createTime", tempTime);
                    map2.put("totalPlaceCount", park.getPark_num());
                    //停车场剩余泊位数
                    map2.put("reducePlaceCount", "");
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postPlaceData(map2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return ResultInfo.success();
                }
                parkingOrder.setSum_amount(paymentVo.getPay_money());
                parkingOrder.setDiscount_amount(paymentVo.getDiscount_money());
                parkingOrder.setPaid_amount("0");
                parkingOrder.setUnpaid_amount(new BigDecimal(paymentVo.getPay_money()).subtract(new BigDecimal(paymentVo.getDiscount_money())).toString());
                parkingOrder.setStatus(GlobalData.PARKING_ORDER_STAYPAY);
                parkingOrder.setUpdate_time(new Date());
                parkingOrder.setDriveout_gate(parkDevice.getDevice_code());
                parkingOrder.setDriveout_time(bean.getDriveout_time());
                if (updateById(parkingOrder) > 0) {
                    //图片保存操作
                    //处理图片
                    List<ParkingOrderPics> detailsList = bean.getDetails();
                    handlePictue(detailsList, parkingOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);

                    //路外车辆预出场上报 发送至市交平台
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderNo", parkingOrder.getOrder_no());
                    map.put("billId", parkingOrder.getId());
                    Integer tempParkId = parkingOrder.getPark_id();
                    Park park1 = null;
                    if (null != tempParkId) {
                        park1 = parkService.selectById(tempParkId);
                        if (null != park1) {
                            map.put("parkCode", park1.getTraffic_park_code());
                        }
                    }
                    Integer tempCarno = parkingOrder.getCarno_id();
                    OperateCarno operateCarno1 = null;
                    if (null != tempCarno) {
                        operateCarno1 = operateCarnoService.selectById(tempCarno);
                        if (null != operateCarno1) {
                            map.put("plateNo", operateCarno1.getCar_no());
                        }
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("enterTime", simpleDateFormat.format(parkingOrder.getDrivein_time()));
                    map.put("costTime", simpleDateFormat.format(new Date()));
                    map.put("parkPeriodTime", parkingOrder.getResitime());
                    map.put("payStatus", 0);
                    BigDecimal bigDecimal = new BigDecimal("100");
                    String tempTotal = new BigDecimal(parkingOrder.getSum_amount()).multiply(bigDecimal).toString();
                    String temp2 = tempTotal;
                    if (tempTotal.contains(".")) {
                        String[] temp1 = tempTotal.split("\\.");
                        temp2 = temp1[0];
                    }

                    map.put("totalCost", Integer.parseInt(temp2));

                    String freeMoney = new BigDecimal(parkingOrder.getDiscount_amount()).multiply(bigDecimal).toString();
                    String temp4 = freeMoney;
                    if (freeMoney.contains(".")) {
                        String[] temp3 = freeMoney.split("\\.");
                        temp4 = temp3[0];
                    }

                    map.put("freeMoney", Integer.parseInt(temp4));

                    String preMoney = new BigDecimal(parkingOrder.getPaid_amount()).multiply(bigDecimal).toString();
                    String temp6 = preMoney;
                    if (preMoney.contains(".")) {
                        String[] temp5 = preMoney.split("\\.");
                        temp6 = temp5[0];
                    }
                    map.put("preMoney", Integer.parseInt(temp6));

                    String payMoney = new BigDecimal(parkingOrder.getUnpaid_amount()).multiply(bigDecimal).toString();
                    String temp8 = payMoney;
                    if (payMoney.contains(".")) {
                        String[] temp7 = payMoney.split("\\.");
                        temp8 = temp7[0];
                    }
                    map.put("payMoney", Integer.parseInt(temp8));
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postOuterCarPrePassOutData(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String unpaid = new BigDecimal(paymentVo.getPay_money()).subtract(new BigDecimal(paymentVo.getDiscount_money())).toString().toString();
                    if (StringUtils.equals(park.getBrand(), "dc")) {
                        String content = "";
                        content += bean.getCar_no() + ",";
                        content += "请缴费" + unpaid + "元";
                        parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), content);
                    }

                    return ResultInfo.success();
                } else {
                    return ResultInfo.success();
                }
            } else if (GlobalData.PARKING_ORDER_ALREADYPAY.equals(parkingOrder.getStatus()) || GlobalData.PARKING_ORDER_COMPLETE.equals(parkingOrder.getStatus())) {//已缴费
                PaymentVo paymentVo = getParkingCharging(new AppealRecordVo(String.valueOf(parkingOrder.getId()), bean.getDriveout_time()));
                BigDecimal allAmount = new BigDecimal(paymentVo.getPay_money()).subtract(new BigDecimal(paymentVo.getDiscount_money()));
                BigDecimal paiedAmount = new BigDecimal(parkingOrder.getPaid_amount());
                //实际产生费用大于已支付费用，生成新订单
                if (allAmount.compareTo(paiedAmount) == 1) {
                    Integer paymentId = parkingOrder.getPayment_id();
                    PaymentOrder paymentOrder = paymentOrderService.selectById(paymentId);
                    String parkingIdByUUId = ParkingOrderUtils.getParkingIdByUUId(GlobalData.ROLE_P, park.getId());
                    ParkingOrder newOrder = new ParkingOrder();
                    newOrder.setOrder_no(parkingIdByUUId);
//                    newOrder.setArea_id(park.getArea_id());
//                    newOrder.setStreet_id(park.getStreet_id());
                    newOrder.setPark_id(park.getId());
                    newOrder.setIs_del(GlobalData.ISDEL_NO);

                    newOrder.setDrivein_time(paymentOrder.getCreate_time());
                    newOrder.setCarno_id(operateCarno.getId());
                    newOrder.setSource(parkingOrder.getSource());
                    newOrder.setStatus(GlobalData.PARKING_ORDER_STAYPAY);
                    newOrder.setSum_amount(allAmount.subtract(paiedAmount).toString());
                    newOrder.setDiscount_amount("0");
                    newOrder.setPaid_amount("0");
                    newOrder.setUnpaid_amount(allAmount.subtract(paiedAmount).toString());
                    newOrder.setIs_upload(GlobalData.ISUPLOAD);
//                    newOrder.setIs_merge("0");
//                    newOrder.setIs_invoice(GlobalData.ISUPLOAD);
                    newOrder.setDriveout_gate(parkDevice.getDevice_code());
                    newOrder.setDriveout_time(bean.getDriveout_time());
                    newOrder.setResitime(TimeUtils.computeMinute(paymentOrder.getCreate_time(), bean.getDriveout_time()));
                    newOrder.setCreate_time(new Date());
                    newOrder.setUpdate_time(new Date());
                    insert(newOrder);


                    //路外车辆预出场上报 发送至市交平台
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderNo", newOrder.getOrder_no());
                    map.put("billId", parkingOrder.getId());
                    Integer tempParkId = newOrder.getPark_id();
                    Park park1 = null;
                    if (null != tempParkId) {
                        park1 = parkService.selectById(tempParkId);
                        if (null != park1) {
                            map.put("parkCode", park1.getTraffic_park_code());
                        }
                    }
                    Integer tempCarno = newOrder.getCarno_id();
                    OperateCarno operateCarno1 = null;
                    if (null != tempCarno) {
                        operateCarno1 = operateCarnoService.selectById(tempCarno);
                        if (null != operateCarno1) {
                            map.put("plateNo", operateCarno1.getCar_no());
                        }
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("enterTime", simpleDateFormat.format(newOrder.getDrivein_time()));
                    map.put("costTime", simpleDateFormat.format(new Date()));
                    map.put("parkPeriodTime", newOrder.getResitime());
                    map.put("payStatus", 0);
                    BigDecimal bigDecimal = new BigDecimal("100");
                    String tempTotal = new BigDecimal(newOrder.getSum_amount()).multiply(bigDecimal).toString();
                    String temp2 = tempTotal;
                    if (tempTotal.contains(".")) {
                        String[] temp1 = tempTotal.split("\\.");
                        temp2 = temp1[0];
                    }

                    map.put("totalCost", Integer.parseInt(temp2));
                    String freeMoney = new BigDecimal(newOrder.getDiscount_amount()).multiply(bigDecimal).toString();
                    String temp4 = freeMoney;
                    if (freeMoney.contains(".")) {
                        String[] temp3 = freeMoney.split("\\.");
                        temp4 = temp3[0];
                    }

                    map.put("freeMoney", Integer.parseInt(temp4));
                    String preMoney = new BigDecimal(newOrder.getPaid_amount()).multiply(bigDecimal).toString();
                    String temp6 = preMoney;
                    if (preMoney.contains(".")) {
                        String[] temp5 = preMoney.split("\\.");
                        temp6 = temp5[0];
                    }
                    map.put("preMoney", Integer.parseInt(temp6));

                    String payMoney = new BigDecimal(newOrder.getUnpaid_amount()).multiply(bigDecimal).toString();
                    String temp8 = payMoney;
                    if (payMoney.contains(".")) {
                        String[] temp7 = payMoney.split("\\.");
                        temp8 = temp7[0];
                    }

                    map.put("payMoney", Integer.parseInt(temp8));
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postOuterCarPrePassOutData(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                    parkingOrder.setDriveout_time(paymentOrder.getCreate_time());
                    parkingOrder.setUpdate_time(new Date());
                    parkingOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), parkingOrder.getDriveout_time()));
                    //处理图片
                    List<ParkingOrderPics> detailsList = bean.getDetails();
                    handlePictue(detailsList, parkingOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);
                    updateById(parkingOrder);

                    if (StringUtils.equals(park.getBrand(), "dc")) {
                        String unpaid = allAmount.subtract(paiedAmount).toString().toString();
                        String content = "";
                        content += bean.getCar_no() + ",";
                        content += "请缴费" + unpaid + "元";
                        parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), content);
                    }
                    return ResultInfo.success();

                } else {
                    //订单置为已完成
                    BeanUtil.copyProperties(bean, parkingOrder, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                    parkingOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), parkingOrder.getDriveout_time()));
                    parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                    parkingOrder.setUpdate_time(new Date());
                    //处理图片
                    List<ParkingOrderPics> detailsList = bean.getDetails();
                    handlePictue(detailsList, parkingOrder.getId(), GlobalData.PARKING_DIRECTION_OUT);


                    //路外车辆出场记录 对接市交接口 同步数据
                    Map<String, Object> map1 = new HashMap<>();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map1.put("actTime", simpleDateFormat.format(new Date()));
                    map1.put("actType", 2);
                    Integer tempParkId = parkingOrder.getPark_id();
                    Park park1 = null;
                    if (null != tempParkId) {
                        park1 = parkService.selectById(tempParkId);
                    }
                    if (null != park1) {
                        map1.put("parkCode", park1.getTraffic_park_code());
                    }
                    map1.put("deviceCode", parkingOrder.getDriveout_gate());

                    Integer tempCarno = parkingOrder.getCarno_id();
                    OperateCarno operateCarno1 = null;
                    if (null != tempCarno) {
                        operateCarno1 = operateCarnoService.selectById(tempCarno);
                    }

                    if (null != operateCarno1) {
                        map1.put("plateNo", operateCarno1.getCar_no());
                    }

                    Integer tempColor = 0;
                    if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_BLUE)) {
                        tempColor = 1;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_GREEN)) {
                        tempColor = 5;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_YELLOW)) {
                        tempColor = 2;
                    } else if (operateCarno1.getCar_type().equals(GlobalData.CAR_TYPE_WHITE)) {
                        tempColor = 4;
                    }
                    map1.put("plateColor", tempColor);
                    List<ParkingOrderPics> detailsListTemp = bean.getDetails();
                    if (CollectionUtils.isNotEmpty(detailsListTemp)) {
                        String url = detailsListTemp.get(0).getPicture_url();
                        map1.put("picUrl", loadPicPath2 + url);
                    }
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postOuterCarPassOutData(map1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //上报泊位 对接市交接口
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("parkCode", park.getTraffic_park_code());
                    String tempTime = simpleDateFormat.format(new Date());
                    map2.put("createTime", tempTime);
                    map2.put("totalPlaceCount", park.getPark_num());
                    //停车场剩余泊位数
                    map2.put("reducePlaceCount", "");
                    //TODO 上报交控
                    try {
                        TrafficControlUtils.postPlaceData(map2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // todo 开闸
                    try {
                        if (StringUtils.equals(park.getBrand(), "dc")) {
                            String content = "";
                            content += bean.getCar_no() + ",";
                            content += "已缴费 一路顺风";
                            parkingGateUtils.mqttOpenGate(parkDevice.getDevice_code(), content);
                        }
                        parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
                    } catch (Exception e) {
                        logger.info(e.toString());
                    }
                    updateById(parkingOrder);
                    return ResultInfo.success();
                }
            }
            return ResultInfo.success();
        } else {
//            return opengateSave(bean.getBerth(), GlobalData.PARKING_DIRECTION_OUT);
        }
        return null;
    }

    private void handlePictue(List<ParkingOrderPics> detailsList, Integer id, String pictureType) {
        for (ParkingOrderPics details : detailsList) {
            String base64 = details.getBase64();
            String storeName = fileUploadUtils.savePhoto(FileUtils.BaseToInputStream(base64));
            FileManage fileManage = new FileManage();
            fileManage.setStore_name(storeName);
            fileManage.setFile_name(storeName);
            fileManage.setFile_type("jpg");
            fileManage.setIs_del(GlobalData.ISDEL_NO);
            fileManage.setFile_url(storeName + ".jpg");
            fileManage.setCreate_time(new Date());
            if (fileManageService.insert(fileManage) > 0) {
                details.setPicture_id(fileManage.getId());
                details.setParking_order_id(id);
                details.setPicture_type(pictureType);//图片类型 1驶入 2驶出
                details.setIs_del("0");
                detailsService.insert(details);
            }
        }
    }


    /**
     * 描 述： TODO(查询订单数)rechargemanagement
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data variance2 1.应收  2.实收  空为全部
     * @return {@link Long}
     */
    public Long getTodayOrderForm(CommonVo data) {
        QueryWrapper<ParkingOrder> wrapper = new QueryWrapper<>();

        wrapper.apply("1=1");

        if (StringUtils.isNotBlank(data.getStr()))
            wrapper.eq("CONVERT(varchar, create_time, 23)", data.getStr());
        //排除在停状态
        if (data.getVariance2() != null) {
            wrapper.ne("status", GlobalData.PARKING_ORDER_STOP);
            if (data.getVariance2() == 1)
                wrapper.gt("convert(decimal(15, 2), sum_amount)", 0);
            if (data.getVariance2() == 2)
                wrapper.gt("convert(decimal(15, 2), paid_amount)", 0);
        }

        wrapper.eq("is_del", 0);

        Long count = this.selectCount(wrapper);
        return count;
    }


    /**
     * 描 述： TODO(应收款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link String}
     */
    public String getShouldCharge(CommonVo data) {
        String shouldCharge = mapper.getShouldCharge(data);
        return shouldCharge;
    }

    /**
     * 描 述： TODO(实收款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getActualCharge(CommonVo data) {
        String sum = mapper.getActualCharge(data);
        return sum;
    }

    /**
     * create by wh at 2022/1/7 10:17
     * description:订单统计基础数据
     *
     * @return : ResultInfo
     * @param:area_id
     * @param:street_id
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingOrderBasicData(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OrderBasicDataVo> list = mapper.getParkingOrderBasicData(area_id, street_id, road_id, startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/8 9:12
     * description:订单统计基础数据导出excel
     *
     * @param startTime
     * @param endTime
     * @param areaId
     * @param streetId
     * @param roadId
     * @param response
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo exportOrderBasicData(String startTime, String endTime, Integer areaId, Integer streetId, Integer roadId, HttpServletResponse response) {

        List<OrderBasicDataVo> list = mapper.getParkingOrderBasicData(areaId, streetId, roadId, startTime, endTime);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("cityName", "区域");
        writer.addHeaderAlias("areaName", "街道");
        writer.addHeaderAlias("roadName", "停车场");
        writer.addHeaderAlias("totalOrders", "订单总量");
        writer.addHeaderAlias("totalPayment", "缴费总量");
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
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);
    }

    /**
     * create by wh at 2022/1/7 11:17
     * description:订单统计订单增长
     *
     * @return : ResultInfo
     * @param:area_id
     * @param:street_id
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingOrderGrowth(Integer pageIndex, Integer pageSize, Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OrderGrowthVo> list = mapper.getParkingOrderGrowth(area_id, street_id, road_id, startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/7 11:17
     * description:近八天订单增长分析
     *
     * @return : ResultInfo
     * @param:area_id
     * @param:street_id
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingNearly8DaysOrderGrowth(Integer area_id, Integer street_id, Integer road_id, String user_type) {
        List<OrderGrowthVo> list = mapper.getParkingNearly8DaysOrderGrowth(area_id, street_id, road_id, user_type);
        List<String> time = new ArrayList<>();
        time = list.stream().map(x -> x.getTime()).collect(Collectors.toList());
        List<Integer> orderNum = new ArrayList<>();
        orderNum = list.stream().map(x -> x.getOrderNum()).collect(Collectors.toList());
        List<Integer> payNum = new ArrayList<>();
        payNum = list.stream().map(x -> x.getPayNum()).collect(Collectors.toList());
        JSONObject res = new JSONObject();
        res.put("timeList", time);
        res.put("orderNumList", orderNum);
        res.put("payNumList", payNum);
        return ResultInfo.success(res);
    }

    /**
     * create by wh at 2022/1/8 9:12
     * description:订单统计订单增长导出excel
     *
     * @param startTime
     * @param endTime
     * @param areaId
     * @param streetId
     * @param roadId
     * @param response
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo exportOrderGrowth(String startTime, String endTime, Integer areaId, Integer streetId, Integer roadId, HttpServletResponse response) {

        List<OrderGrowthVo> list = mapper.getParkingOrderGrowth(areaId, streetId, roadId, startTime, endTime);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("time", "时间");
        writer.addHeaderAlias("orderNum", "订单数量");
        writer.addHeaderAlias("payNum", "缴费数量");
        writer.addHeaderAlias("Proportion", "缴费占比");
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
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);
    }

    /**
     * create by wh at 2022/1/7 11:17
     * description:订单统计订单状态占比
     *
     * @return : ResultInfo
     * @param:area_id
     * @param:street_id
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingOrderStatus(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        Map<String, List> map = new HashMap<>();
        List<OrderStatusVo> list = mapper.getParkingOrderStatus(area_id, street_id, road_id, startTime, endTime);
        map.put("data", list);
        List<JSONObject> list2 = new ArrayList();
        for (OrderStatusVo orderStatusVo : list) {
            JSONObject res = new JSONObject();
            res.put("value", orderStatusVo.getNum());
            res.put("name", orderStatusVo.getStatus());
            list2.add(res);
        }
        map.put("view", list2);
        return ResultInfo.success(map);
    }

    /**
     * create by wh at 2022/1/7 10:17
     * description:资金统计收入报表
     *
     * @return : ResultInfo
     * @param:area_id
     * @param:street_id
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingOrderIncome(Integer area_id, Integer street_id, Integer park_id, String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OrderIncomeVo> list = mapper.getParkingOrderIncome(area_id, street_id, park_id, startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/8 9:12
     * description:资金统计收入报表导出excel
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo exporOrderIncome(String startTime, String endTime, Integer areaId, Integer streetId, Integer parkId, HttpServletResponse response) {

        List<OrderIncomeVo> list = mapper.getParkingOrderIncome(areaId, streetId, parkId, startTime, endTime);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("areaName", "区域");
        writer.addHeaderAlias("streetName", "街道");
        writer.addHeaderAlias("roadName", "停车场");
        writer.addHeaderAlias("receivable", "应收");
        writer.addHeaderAlias("netReceipts", "实收");
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
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);
    }

    /**
     * create by wh at 2022/1/7 10:17
     * description:资金统计充值增长
     *
     * @return : ResultInfo
     * @param:road_id
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingRechargeGrowth(String timeType, String startTime, String endTime) {
        List<OrderRechargeGrowthVo> list = mapper.getParkingRechargeGrowth(timeType, startTime, endTime);
        List<String> time = new ArrayList<>();
        time = list.stream().map(x -> x.getTime()).collect(Collectors.toList());
        List<String> rechargeAmount = new ArrayList<>();
        rechargeAmount = list.stream().map(x -> x.getRechargeAmount()).collect(Collectors.toList());
        JSONObject res = new JSONObject();
        res.put("data", list);
        res.put("time", time);
        res.put("rechargeAmount", rechargeAmount);
        return ResultInfo.success(res);
    }

    /**
     * create by wh at 2022/1/7 11:02
     * description:资金统计缴费方式统计
     *
     * @return : ResultInfo
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingPaymentMethod(String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OrderPaymentMethodVo> list = mapper.getParkingPaymentMethod(startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/7 11:06
     * description:资金统计免单统计
     *
     * @return : ResultInfo
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingFree(String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OrderFreeVo> list = mapper.getParkingFree(startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/7 1:36
     * description:开票统计
     *
     * @return : ResultInfo
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingInvoicing(String phone, Integer area_id, String invoice_type, String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InvoicingVo> list = mapper.getParkingInvoicing(phone, area_id, invoice_type, startTime, endTime);
        return ResultInfo.success(new PageInfo<>(list));
    }

    /**
     * create by wh at 2022/1/7 1:40
     * description:大额欠费
     *
     * @return : ResultInfo
     * @param:startTime
     * @param:endTime
     */
    public ResultInfo getParkingLargeArrears(String phone, Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<LargeArrearsVo> list = mapper.getParkingLargeArrears(phone, area_id, street_id, road_id, startTime, endTime);
        JSONObject res = new JSONObject();
        res.put("list", new PageInfo<>(list));
        res.put("typeName", "欠费停车场");
        return ResultInfo.success(res);
    }

    /**
     * create by wh at 2022/1/8 9:12
     * description:大额欠费导出excel
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo exporLargeArrears(String money, Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime, HttpServletResponse response) {

        List<LargeArrearsVo> list = mapper.getParkingLargeArrears(money, area_id, street_id, road_id, startTime, endTime);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("carNo", "车牌号");
        writer.addHeaderAlias("inTime", "最近的驶入时间");
        writer.addHeaderAlias("outTime", "最近的驶出时间");
        writer.addHeaderAlias("totalArrears", "欠费总额(元)");
        writer.addHeaderAlias("name", "车主姓名");
        writer.addHeaderAlias("phone", "车主电话");
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
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);
    }

    /**
     * create by wh at 2022/1/7 1:40
     * description:大额欠费欠费订单
     *
     * @return : ResultInfo
     * @param:startTime
     * @param:endTime
     */
    public List<ArrearsOrderVo> selectParkingLargeArrearsOrders(Integer carno_id, String money, Integer areaId, Integer streetId, Integer roadId, String startTime, String endTime) {
        return mapper.getParkingLargeArrearsOrders(carno_id, money, areaId, streetId, roadId, startTime, endTime);
    }

    public ResultInfo getParkingLargeArrearsOrders(Integer carno_id, String money, Integer areaId, Integer streetId, Integer roadId, String startTime, String endTime) {
        List<ArrearsOrderVo> list = selectParkingLargeArrearsOrders(carno_id, money, areaId, streetId, roadId, startTime, endTime);
        list.forEach(a -> {
            if (null != a.getResitime() && !"0".equals(a.getResitime())) {
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            } else {
                a.setResitime("");
            }
            LambdaQueryWrapper<ParkingOrder> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ParkingOrder::getOrder_no, a.getOrderNo());
            wrapper.eq(ParkingOrder::getIs_del, "0");
            ParkingOrder parkingOrder = selectOne(wrapper);
            if (notEmpty(parkingOrder.getPark_id())) {
                Park park = parkService.selectById(parkingOrder.getPark_id());
                if (notEmpty(park)) {
                    a.setRoadName(park.getPark_name());
                }
            }
            a.setType("1");
//            a.setTypeName("欠费停车场");
        });
        JSONObject res = new JSONObject();
        res.put("list", list);
        return ResultInfo.success(res);
    }

    /**
     * create by wh at 2022/1/12 11:33
     * description: 修改车牌
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo changOrdereCarNo(Integer orderId, String carNo, String type) {
        QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_no", carNo);
        queryWrapper.eq("is_del", GlobalData.ISDEL_NO);
        OperateCarno operateCarno = operateCarnoService.selectOne(queryWrapper);
        if (operateCarno == null) {
            operateCarno = new OperateCarno();
            operateCarno.setCar_no(carNo);
            operateCarno.setCar_type(type);
            operateCarno.setRoster_type("1");
            operateCarno.setCreate_time(new Date());
            operateCarno.setCreate_user(sysUserService.getUser().getId());
            operateCarno.setIs_del("0");
            operateCarnoService.insert(operateCarno);
        }
        ParkingOrder parkingOrder = selectById(orderId);
        parkingOrder.setCarno_id(operateCarno.getId());
        updateById(parkingOrder);
        return ResultInfo.success("修改车牌成功！");

    }

    /**
     * 结束订单
     *
     * @param id
     * @return
     */
    public ResultInfo finishOrder(Integer id, Date driveout_time) {
        if (null == driveout_time) {
            driveout_time = new Date();
        }
        ParkingOrder parkingOrder = this.selectById(id);
        if (parkingOrder != null && GlobalData.PARKING_ORDER_STOP.equals(parkingOrder.getStatus())) {
            //修改停车技术时间 和订单状态
            long l = betweenMin(parkingOrder.getDrivein_time(), driveout_time);
            parkingOrder.setResitime((int) l);
            //计费
            if (GlobalData.FREETYPEBY.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEBMD.equals(parkingOrder.getFree_type()) || GlobalData.FREETYPEQYBY.equals(parkingOrder.getFree_type())) {
                parkingOrder.setStatus(GlobalData.PARKING_ORDER_ALREADYPAY);
                parkingOrder.setUpdate_time(new Date());
                parkingOrder.setSum_amount("0");
                parkingOrder.setDiscount_amount("0");
                parkingOrder.setPaid_amount("0");
                parkingOrder.setUnpaid_amount("0");
                parkingOrder.setDriveout_time(driveout_time);
                updateById(parkingOrder);
            } else {
                PaymentVo parkingCharging = this.getParkingCharging(new AppealRecordVo(id + "", driveout_time));
                if (new BigDecimal(parkingCharging.getPay_money()).compareTo(BigDecimal.ZERO) == 0) {
                    parkingOrder.setStatus(GlobalData.PARKING_ORDER_ALREADYPAY);
                    parkingOrder.setFree_type(GlobalData.FREETYPETIME);
                    parkingOrder.setUpdate_time(new Date());
                    parkingOrder.setSum_amount("0");
                    parkingOrder.setDiscount_amount("0");
                    parkingOrder.setPaid_amount("0");
                    parkingOrder.setUnpaid_amount("0");
                    parkingOrder.setDriveout_time(driveout_time);
                    updateById(parkingOrder);
                } else {
                    parkingOrder.setSum_amount(parkingCharging.getPay_money());
                    parkingOrder.setDiscount_amount(parkingCharging.getDiscount_money());
                    parkingOrder.setUnpaid_amount(new BigDecimal(parkingOrder.getSum_amount()).subtract(new BigDecimal(parkingOrder.getDiscount_amount())).setScale(2).toString());
                    parkingOrder.setStatus(GlobalData.PARKING_ORDER_STAYPAY);
                    parkingOrder.setUpdate_time(new Date());
//                    parkingOrder.setDriveout_time(driveout_time);
                    updateById(parkingOrder);
                }
            }
            return ResultInfo.success();
        } else {
            return ResultInfo.error("只有在停状态的可以结束订单！");
        }

    }

    /**
     * 现金收费
     *
     * @param id
     * @return
     */
    public ResultInfo cashShoufei(Integer id) {
        ParkingOrder parkingOrder = parkingOrderService.selectById(id);
        if (parkingOrder != null && GlobalData.PARKING_ORDER_STAYPAY.equals(parkingOrder.getStatus())) {
            //生成支付记录
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setPayment_type("5");//现金
            paymentOrder.setPayment_resource("3");//订单支付
            paymentOrder.setPay_time(new Date());
            paymentOrder.setStatus("2");//已支付
            paymentOrder.setAmount(parkingOrder.getUnpaid_amount());
//            InspectManage inspecter = inspectManageService.getInspecter();
//            paymentOrder.setInspect_id(inspecter.getId());
            paymentOrder.setIs_del("0");
//            paymentOrder.setCreate_user(inspecter.getId());
            paymentOrder.setCreate_time(new Date());
            if (paymentOrder.insert()) {
                if (parkingOrder.getDriveout_time() != null && StringUtils.isNotEmpty(parkingOrder.getDriveout_gate())) {
                    parkingOrder.setStatus("4");
                    //执行开闸操作,获取当前
                    try {
                        //开闸
                        Integer parkId = parkingOrder.getPark_id();
                        Park park = parkService.selectById(parkId);
//                        parkingGateUtils.openGate(park.getPark_code(), parkingOrder.getDriveout_gate());
                        parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
                    } catch (Exception e) {

                    }
                } else {
                    parkingOrder.setStatus("3");
                }
                parkingOrder.setPay_type("5");//现金
                parkingOrder.setPaid_amount(parkingOrder.getUnpaid_amount());
                parkingOrder.setUnpaid_amount("0");
                parkingOrder.setPay_time(paymentOrder.getPay_time());
                parkingOrder.setPayment_id(paymentOrder.getId());
                parkingOrder.setUpdate_time(new Date());
                if (parkingOrder.updateById()) {
                    return ResultInfo.success();
                } else {
                    return ResultInfo.error("收费失败！");
                }
            } else {
                return ResultInfo.error("收费失败！");
            }

        } else {
            return ResultInfo.error("只有待缴费的订单可以现金收费！");
        }
    }


    /**
     * 停车场现金收费
     * Author wzn
     * Date 2022/1/29 9:37
     */
    public ResultInfo tccCashShoufei(Integer id) {
        ParkingOrder parkingOrder = parkingOrderService.selectById(id);
        if (parkingOrder != null && "2".equals(parkingOrder.getStatus())) {
            //生成支付记录
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setPayment_type("5");//现金
            paymentOrder.setPayment_resource("3");//订单支付
            paymentOrder.setStatus("2");//已支付
            paymentOrder.setAmount(parkingOrder.getUnpaid_amount());
            SysUser inspecter = sysUserService.getUser();
            paymentOrder.setInspect_id(inspecter.getId());
            paymentOrder.setIs_del("0");
            paymentOrder.setCreate_user(inspecter.getId());
            paymentOrder.setCreate_time(new Date());
            paymentOrder.setPay_time(new Date());
            if (paymentOrder.insert()) {
                parkingOrder.setPay_type("5");//现金
                parkingOrder.setPaid_amount(parkingOrder.getUnpaid_amount());
                parkingOrder.setUnpaid_amount("0");
                parkingOrder.setPayment_id(paymentOrder.getId());
                parkingOrder.setPay_time(new Date());
                parkingOrder.setStatus("3");
                if (parkingOrder.updateById()) {
                    //执行开闸操作,获取当前
                    try {
                        //开闸
                        int parkId = parkingOrder.getPark_id();
                        Park park = parkService.selectById(parkId);
//                        parkingGateUtils.openGate(park.getPark_code(), parkingOrder.getDriveout_gate());
                        parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
                    } catch (Exception e) {

                    }
                    return ResultInfo.success();
                } else {
                    return ResultInfo.error("收费失败！");
                }
            } else {
                return ResultInfo.error("收费失败！");
            }

        } else {
            return ResultInfo.error("只有待缴费的订单可以现金收费！");
        }
    }


    /**
     * create by wh at 2022/1/12 11:33
     * description: 订单收费统计
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public PaymentVo getParkingCharging(AppealRecordVo vo) {
        PaymentVo charging = new PaymentVo();
        ParkingOrder parkingOrder = selectOne(new QueryWrapper<ParkingOrder>().eq("id", vo.getId()));
        if (null != vo.getCar_id()) {
            parkingOrder.setCarno_id(vo.getCar_id());
        }
        OperateCarno operateCarno = operateCarnoService.selectById(Integer.valueOf(parkingOrder.getCarno_id()));
        List<Integer> dayList = new ArrayList<>();
        List<Double> dayList2 = new ArrayList<>();
        List<Integer> nightList = new ArrayList<>();
        List<Double> nightList2 = new ArrayList<>();
        if (notEmpty(operateCarno)) {
            if (null == parkingOrder.getDriveout_time() || "".equals(parkingOrder.getDriveout_time())) {
                if (null == vo.getEndTime() || "".equals(vo.getEndTime())) {
                    parkingOrder.setDriveout_time(new Date());
                } else {
                    parkingOrder.setDriveout_time(vo.getEndTime());
                }
            } else {
                if (null != vo.getEndTime() && !"".equals(vo.getEndTime())) {
                    parkingOrder.setDriveout_time(vo.getEndTime());
                }
            }
            String drivein_time = "";
            //如果是临时停车，且过了临停时间，则驶入时间应该是临停驶离截止时间
            if (GlobalData.FREETYPELSTX.equals(parkingOrder.getFree_type())) {
                drivein_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parkingOrder.getTempDriveOutTime());
            } else {
                drivein_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parkingOrder.getDrivein_time());
            }
            String driveout_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parkingOrder.getDriveout_time());
            Integer status_count = parkingReleaseService.selectByParmCount(GlobalData.PARKING_TYPE_PLAT, parkingOrder.getPark_id(), null);
            Integer driveout_count = parkingReleaseService.selectByParmCount(GlobalData.PARKING_TYPE_PLAT, parkingOrder.getPark_id(), driveout_time);
            //根据当前路段、停车场查询当前是否在启用状态或者当前上线收费方案是否存在
            if (status_count > 0 && driveout_count > 0) {
                ChargeVo chargeVo = DcCacheDataUtil.getChargeById(parkingOrder.getPark_id().toString(), "1", operateCarno.getCar_type());
                if (notEmpty(chargeVo)) {
                    if (notEmpty(chargeVo.getDayConfig())) {
                        List<ChargeTimeConfig> dayChargeTimeConfigs = chargeVo.getDayConfig().getChargeTimeConfigs();
                        for (ChargeTimeConfig chargeTimeConfig : dayChargeTimeConfigs) {
                            dayList.add(chargeTimeConfig.getStart_minute());
                            dayList.add(chargeTimeConfig.getEnd_minute());
                            if (notEmpty(chargeTimeConfig.getPrice())) {
                                dayList2.add(Double.valueOf(chargeTimeConfig.getPrice()));
                            }
                        }
                    }
                    if (notEmpty(chargeVo.getNightConfig())) {
                        List<ChargeTimeConfig> nightChargeTimeConfigs1 = chargeVo.getNightConfig().getChargeTimeConfigs();
                        for (ChargeTimeConfig chargeTimeConfig : nightChargeTimeConfigs1) {
                            nightList.add(chargeTimeConfig.getStart_minute());
                            nightList.add(chargeTimeConfig.getEnd_minute());
                            if (notEmpty(chargeTimeConfig.getPrice())) {
                                nightList2.add(Double.valueOf(chargeTimeConfig.getPrice()));
                            }
                        }
                    }
                    Integer[] dayArray = dayList.toArray(new Integer[dayList.size()]);
                    Double[] dayArray2 = dayList2.toArray(new Double[dayList2.size()]);
                    Integer[] nightArray = nightList.toArray(new Integer[nightList.size()]);
                    Double[] nightArray2 = nightList2.toArray(new Double[nightList2.size()]);
                    charging = ChargingUtils.charging(Double.valueOf(chargeVo.getChargeProgramme().getLimit_price_amount()), chargeVo.getDayConfig().getStart_time(), chargeVo.getDayConfig().getEnd_time(),
                            chargeVo.getDayConfig().getFree_time(), chargeVo.getNightConfig().getFree_time(), Double.valueOf(chargeVo.getDayConfig().getInterval_limit_price()),
                            Double.valueOf(chargeVo.getNightConfig().getInterval_limit_price()), chargeVo.getDayConfig().getCharge_unit(),
                            chargeVo.getNightConfig().getCharge_unit(), dayArray, dayArray2, nightArray, nightArray2, drivein_time, driveout_time,
                            chargeVo.getNightConfig().getStart_time(), chargeVo.getNightConfig().getEnd_time());
                    charging.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), parkingOrder.getDriveout_time()).toString());
                    //是残疾人时，按50%收费
//                    if (GlobalData.ROSTER_TYPE_DISABLED.equals(operateCarno.getRoster_type())) {
//                        //查詢系統參數內的殘疾人折扣
//                        SysConfig configs = sysConfigService.selectOne(new QueryWrapper<SysConfig>().eq("config_name_en", GlobalData.DISCOUNTZK));
//                        String pay_money = new BigDecimal(charging.getPay_money()).multiply(new BigDecimal(configs.getConfig_value())).divide(new BigDecimal(100)).toString();
//                        charging.setPay_money(pay_money);
//                    }
                } else {
                    charging.setPay_money("0");
                    charging.setResitime("0");
                    charging.setDiscount_money("0");
                }
            } else {
                charging.setPay_money("0");
                charging.setResitime("0");
                charging.setDiscount_money("0");
            }
        } else {
            charging.setPay_money("0");
            charging.setResitime("0");
            charging.setDiscount_money("0");
        }
        charging.setInTime(DateUtil.formatDateTime(parkingOrder.getDrivein_time()));
        return charging;
    }

    /**
     * 描 述： TODO(根据条件查询订单)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkingOrder>}
     */

    public List<ParkingOrder> getParkingOrder(CommonVo data) {
        List<ParkingOrder> parkingOrder = mapper.getParkingOrder(data);
        return parkingOrder;
    }

    //根据条件生成吊起地址
    public ResultInfo payPlaceOrder(CommonVo data) throws Exception {
        List<ParkingOrder> parkingOrder = mapper.getParkingOrder(data);
        if (parkingOrder != null && parkingOrder.size() > 0) {


            BigDecimal bigAmount = new BigDecimal("0");

            for (ParkingOrder poBean : parkingOrder) {
                String sum_amount = poBean.getSum_amount();
                bigAmount = ArithmeticUtils.add(bigAmount, sum_amount == null ? "0" : sum_amount);
            }
            String amount = bigAmount.toString();
            AlPayConfig alPayConfig = alPayConfigService.findAlPayConfig();
            AlWxOrder alWxOrder = new AlWxOrder();

            //wap下单
            alWxOrder.createOutTradeNo(null, 1);
            alWxOrder.setTotalAmount(amount);
            alWxOrder.setSubject("停车缴费");
            alPay.wapPay(alWxOrder);

            if (alWxOrder.getAlWxPayInfo().getCode() == 200) {
                PaymentOrder paymentOrder = new PaymentOrder();
                paymentOrder.setPayment_type(GlobalData.PARKING_ORDER_TYPE_ZFB);
                paymentOrder.setPayment_resource("3");
                paymentOrder.setStatus("1");
                paymentOrder.setAmount(amount);
                paymentOrder.setPayment_no(alWxOrder.getOutTradeNo());
                paymentOrder.setIs_del(GlobalData.ISDEL_NO);
                paymentOrder.setCreate_time(new Date());
                paymentOrder.setPay_time(new Date());
                if (paymentOrderService.insert(paymentOrder) > 0) {
                    for (ParkingOrder item : parkingOrder) {
                        LambdaUpdateWrapper<ParkingOrder> wrapper = new LambdaUpdateWrapper<>();
                        wrapper.set(ParkingOrder::getPayment_id, paymentOrder.getId());
                        wrapper.eq(ParkingOrder::getId, item.getId());

                        if (this.update(item, wrapper) <= 0) {
                            throw new Exception("修改失败");
                        }
                    }
                    return ResultInfo.success(alWxOrder);
                } else {
                    throw new Exception("订单添加失败");
                }
            } else {
                throw new Exception("获取支付吊起失败");
            }

        } else {
            throw new Exception("订单不存在");
        }

    }

    /**
     * 描 述： TODO(获取订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< UnpaidOrderVo>}
     */
    public List<UnpaidOrderVo> getUnpaidOrder(CommonVo data) {
        List<UnpaidOrderVo> unpaidOrder = mapper.getUnpaidOrder(data);
        return unpaidOrder;
    }

    private static long betweenMin(Date beginDate, Date endDate) {
        long diff = endDate.getTime() - beginDate.getTime();
        long min = diff / 60 / 1000;
        //long min = diff / (1000 * 60);
        return min;
    }

    /**
     * create by wh at 2022/1/14 11:33
     * description: 根据泊位号查询订单详情
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getOrderByBerth(String berth) {
        QueryWrapper<ParkingOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("berth", berth).eq("is_del", GlobalData.ISDEL_NO).eq("status", GlobalData.PARKING_ORDER_STOP);
        ParkingOrder parkingOrder = selectOne(queryWrapper);
        return ResultInfo.success(parkingOrder);
    }

    /**
     * create by zonglina at 2022/1/20 9:33
     * description:
     * 路段和停车场订单信息
     *
     * @return : String
     * @param:RoadParkListVo
     */
    public PageInfo selectWXPage(Integer pageIndex, Integer pageSize, RoadParkListVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<RoadParkListVo> list = mapper.selectWXPage(bean);

        HashMap<String, SysDict> free_type_map = DcCacheDataUtil.getMapDicts("free_type");
        //计算停车场费用(在停、未缴费的结算，反之取订单内价格)
        list.forEach(a -> {
            if (null != a.getResitime() && !"0".equals(a.getResitime())) {
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            } else {
                a.setResitime("");
            }
            //免费类型
            if (notEmpty(a.getFree_type()) && !CollectionUtils.isEmpty(free_type_map)) {
                a.setFree_type(free_type_map.get(a.getFree_type()).getLabel());
            }
        });
        return new PageInfo<>(list);
    }

    /**
     * create by zonglina at 2022/1/20 9:33
     * description:
     * 路段和停车场订单信息
     *
     * @return : String
     * @param:RoadParkListVo
     */
    public List<RoadParkListVo> selectWXList(RoadParkListVo bean) {
        List<RoadParkListVo> list = mapper.selectWXPage(bean);
        //计算停车场费用(在停、未缴费的结算，反之取订单内价格)
        list.forEach(a -> {
            if (null != a.getResitime() && !"0".equals(a.getResitime())) {
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            } else {
                a.setResitime("");
            }
            HashMap<String, SysDict> free_type_map = DcCacheDataUtil.getMapDicts("free_type");
            //免费类型
            if (notEmpty(a.getFree_type())) {
                a.setFree_type(free_type_map.get(a.getFree_type()).getLabel());
            }
        });
        return list;
    }

    /**
     * 描 述： TODO(查询各停车场在停订单统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param status
     * @return {@link List< StatisticsVo>}
     */
    public List<StatisticsVo> getParkingOrderCount(String status) {
        List<StatisticsVo> parkingOrderCount = mapper.getParkingOrderCount(status);
        return parkingOrderCount;
    }

    /**
     * 微信小程序路段和停车场订单查询
     * 待开发票数据
     * 分页查询
     *
     * @author thr
     */
    public PageInfo selectFpPage(Integer pageIndex, Integer pageSize, RoadParkListVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<RoadParkListVo> list = mapper.selectFpPage(bean);
        list.forEach(a -> {
            if (StringUtils.isNotBlank(a.getResitime()) && null != TimeUtils.formatTime(Integer.valueOf(a.getResitime()))) {
                //停留时长 分钟转换小时
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            }
        });
        return new PageInfo<>(list);
    }

    /**
     * 微信小程序路段订单ids,停车场订单ids
     * 待开发票数据
     * 停车场、路段标识（0.路段1停车场）
     *
     * @author thr
     */
    public ResultInfo getOrderIds(RoadParkListVo vo) {
        JSONObject jsonObject = new JSONObject();
        RoadParkListVo roadParkListVo = mapper.getRoadParkOrderIds(vo);
        RoadParkListVo parkingOrderVo = mapper.getParkOrderIds(vo);
        String roadOrderIds = roadParkListVo.getRoadOrderIds();
        String parkOrderIds = parkingOrderVo.getParkOrderIds();
        Integer roadOrderCount = 0;
        Integer parkOrderCount = 0;
        String roadOrderMoney = "0";
        String parkOrderMoney = "0";
        if (notEmpty(roadParkListVo)) {
            //路段停车订单ids
            if (notEmpty(roadParkListVo.getRoadOrderCount())) {
                roadOrderCount = roadParkListVo.getRoadOrderCount();
            }
            if (notEmpty(roadParkListVo.getRoadOrderMoney())) {
                roadOrderMoney = roadParkListVo.getRoadOrderMoney();
            }
        }
        if (notEmpty(parkingOrderVo)) {
            //停车场订单ids
            if (notEmpty(parkingOrderVo.getParkOrderCount())) {
                parkOrderCount = parkingOrderVo.getParkOrderCount();
            }
            if (notEmpty(parkingOrderVo.getParkOrderMoney())) {
                parkOrderMoney = parkingOrderVo.getParkOrderMoney();
            }
        }

        //路段停车订单ids
        jsonObject.put("roadOrderIds", roadOrderIds);
        //停车场订单ids
        jsonObject.put("parkOrderIds", parkOrderIds);
        jsonObject.put("orderCount", roadOrderCount + parkOrderCount);
        jsonObject.put("orderMoney", Double.parseDouble(roadOrderMoney) + Double.parseDouble(parkOrderMoney));
        return ResultInfo.success(jsonObject);
    }

    /**
     * 描 述： TODO(查询流动订单总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public Integer getStreamingOrderCount(String dateStr, Integer park_id) {
        return mapper.getStreamingOrderCount(dateStr, park_id);

    }

    /**
     * 描 述： TODO(查询流动订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public String getStreamingOrderSumAmount(String dateStr, Integer park_id) {
        String sumAmount = mapper.getStreamingOrderSumAmount(dateStr, park_id);
        return sumAmount == null ? "0" : sumAmount;
    }

    /**
     * 描 述： TODO(查询流动订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public String getStreamingOrderReceivable(String dateStr, Integer park_id) {
        String sumAmount = mapper.getStreamingOrderReceivable(dateStr, park_id);
        return sumAmount == null ? "0" : sumAmount;
    }

    /**
     * 描 述： TODO(今日停车订单数排名)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     */
    public List<ShouldBillRankingVo> getParkingOrderRanking(CommonVo data) {
        List<ShouldBillRankingVo> list = mapper.getParkingOrderRanking(data);
        return list;
    }

    /**
     * 补缴金额
     */
    public String getParkingOrderBjje(CommonVo data) {
        return mapper.getParkingOrderBjje(data);
    }

    /**
     * 停车订单列表
     */
    public PageInfo getParkingOrderData(CommonVo data) {
        PageHelper.startPage(data.getPageNum(), data.getPageSize());
        List<ParkingOrderVo> list = mapper.getParkingOrderData(data);

        HashMap<String, SysDict> free_type_map = DcCacheDataUtil.getMapDicts("free_type");
        //计算停车场费用(在停、未缴费的结算，反之取订单内价格)
        list.forEach(a -> {
            if (null != a.getResitime() && !"0".equals(a.getResitime())) {
                a.setResTime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            } else {
                a.setResTime("");
            }
            //免费类型
            if (notEmpty(a.getFree_type()) && !CollectionUtils.isEmpty(free_type_map)) {
                a.setFree_type(free_type_map.get(a.getFree_type()).getLabel());
            }
        });
        return new PageInfo<>(list);
    }

    /**
     * 临停收入、临停欠费金额
     */
    public ShouldBillRankingVo getSumLtsr(CommonVo data) {
        return mapper.getSumLtsr(data);
    }


    /**
     * 描 述： TODO(查询订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getOrderReceivable", type = ParkingOrderDao.class)
    public String getOrderReceivable(CommonVo data) {
        String orderReceivable = mapper.getOrderReceivable(data);
        if (StringUtils.isNotBlank(orderReceivable))
            return ArithmeticUtils.setScale1(orderReceivable, 2, BigDecimal.ROUND_HALF_UP).toString();
        else
            return "0.0";
    }

    /**
     * 描 述： TODO(查询订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getOrderSumAmount", type = ParkingOrderDao.class)
    public String getOrderSumAmount(CommonVo data) {
        String orderSumAmount = mapper.getOrderSumAmount(data);
        if (StringUtils.isNotBlank(orderSumAmount))
            return ArithmeticUtils.setScale1(orderSumAmount, 2, BigDecimal.ROUND_HALF_UP).toString();
        else
            return "0.0";
    }

    /**
     * 描 述： TODO(获取在停订单总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Long}
     */
    public Long getParkingOrderCount(CommonVo data) {

        QueryWrapper<ParkingOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        wrapper.and(QueryWrapper -> QueryWrapper.isNull("operation_type_in").or().eq("operation_type_in", GlobalData.OPERATION_TYPE_REGULAR)
                .or().isNull("operation_type_out").or().eq("operation_type_out", GlobalData.OPERATION_TYPE_REGULAR));
        if (data.getVariance() != null)
            wrapper.eq("area_id", data.getVariance());
        if (data.getVariance2() != null)
            wrapper.eq("street_id", data.getVariance2());
        if (data.getVariance3() != null)
            wrapper.eq("park_id", data.getVariance3());
//
//        if (StringUtils.isNotBlank(data.getStr()))
//            wrapper.eq("CONVERT(varchar, create_time, 23)", data.getStr());

        if (StringUtils.isNotBlank(data.getStr2()))
            wrapper.eq("status", data.getStr2());

        if (StringUtils.isNotBlank(data.getCarNo())) {
            StringBuffer sqlbd = new StringBuffer();
            sqlbd.append(" select id from operate_carno ");
            sqlbd.append(" where car_no = '").append(data.getCarNo()).append("' ");
            wrapper.inSql("carno_id", sqlbd.toString());
        }


        Long count = this.selectCount(wrapper);

        return count;
    }

    /**
     * 描 述： TODO(查询订单数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Long}
     */
    public List<StatisticsVo> getMemberIdCount(CommonVo data) {
        List<StatisticsVo> memberIdCount = mapper.getMemberIdCount(data);
        return memberIdCount;
    }


    /**
     * 描 述： TODO( 收款统计 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public List<Map<String, Object>> getCollectionsStatistics(CommonVo data, Integer park_id) {
        List<Map<String, Object>> retList = new ArrayList<>();

        //初始化
        List<String> typeNames = new ArrayList<String>() {{
            //add("包月");
            add("微信");
            add("支付宝");
            add("钱包");
            add("现金");
        }};

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) != 0) {
                for (String str : typeNames) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("value", 0);
                    map.put("name", str);
                    retList.add(map);
                }
                return retList;
            }
            if (data.getVariance3() == null) {
                data.setVariance3(park_id);
            }
        }

        List<StatisticsVo> lists = mapper.getCollectionsStatistics(data);
        Map<String, String> countMap = new HashMap<String, String>();

        if (lists != null && lists.size() > 0)
            countMap = lists.stream().filter(item -> !item.getPayTypeName().equals("其他")).collect(Collectors.toMap(StatisticsVo::getPayTypeName, StatisticsVo::getSumAmount));
        for (String str : typeNames) {
            Map<String, Object> map = new HashMap<>();
            String sumAmount = countMap.get(str);
            map.put("value", StringUtils.isBlank(sumAmount) ? "0.0" : ArithmeticUtils.setScale1(sumAmount, 2, BigDecimal.ROUND_HALF_UP));
            map.put("name", str);
            retList.add(map);
            //typeCounts.add( count==null?"0":count+"" );
        }
        return retList;
    }

    /**
     * 描 述： TODO(获取欠费停车数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getArrearageParkCount(CommonVo data) {
        String count = mapper.getArrearageParkCount(data);
        return count == null ? "0" : count;
    }


    /**
     * 描 述： TODO(查询停车场订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link  ResultInfo}
     */
    public ResultInfo getParkDeviceInfo(CommonVo data, Integer park_id) {
        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());

            List<DeviceInfoVo> deviceInfo = new ArrayList<>();

            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    deviceInfo = mapper.getParkDeviceInfo(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    deviceInfo = mapper.getParkDeviceInfo(data);
                }
                deviceInfo = mapper.getParkDeviceInfo(data);
            } else {
                deviceInfo = mapper.getParkDeviceInfo(data);
            }

            PageInfo<DeviceInfoVo> page = new PageInfo<>(deviceInfo);

            return ResultInfo.success(page);
        } else {
            List<DeviceInfoVo> deviceInfo = new ArrayList<>();

            if (park_id != null && park_id.compareTo(0) > 0) {
                if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                    deviceInfo = mapper.getParkDeviceInfo(data);
                } else if (data.getVariance3() == null) {
                    data.setVariance3(park_id);
                    deviceInfo = mapper.getParkDeviceInfo(data);
                }
                deviceInfo = mapper.getParkDeviceInfo(data);
            } else {
                deviceInfo = mapper.getParkDeviceInfo(data);
            }
            return ResultInfo.success(deviceInfo);
        }
    }

    public ResultInfo getDetailEWM(Integer id, String gateCode, String parkCode, String type) {
        //查询订单

        ParkingOrder parkingOrder = null;
        if (id != null) {
            parkingOrder = parkingOrderService.selectById(id);
        } else if (StringUtils.isNotEmpty(gateCode)) {
            List<ParkingOrder> pos = parkingOrderService.selectList(new QueryWrapper<ParkingOrder>().eq("is_del", "0").eq("park_id", parkCode).eq("driveout_gate", gateCode).eq("status", "2").orderByDesc("update_time"));
            if (pos != null && pos.size() > 0) {
                parkingOrder = pos.get(0);
            }
        }
        if (parkingOrder == null) {
            return ResultInfo.error("未查询到待支付的订单！");
        }
        AlWxOrder alWxOrder = new AlWxOrder();
        if ("0".equals(type)) {
            //支付宝
            alWxOrder.createOutTradeNo("park", 1);
            alWxOrder.setTotalAmount(parkingOrder.getUnpaid_amount());
            alPay.wapPay(alWxOrder);
            //回写
            AlWxPayInfo alWxPayInfo = alWxOrder.getAlWxPayInfo();
            if (alWxPayInfo.getCode() == 200) {
                PaymentOrder p = alWxPayInfo.getPaymentOrder();
                parkingOrder.setPayment_id(p.getId());
                parkingOrder.setPay_time(new Date());
                parkingOrder.setPay_type("3");//支付宝支付
                parkingOrder.updateById();
            } else {
                return ResultInfo.error("调用支付宝支付失败！");
            }
        } else {
//            //微信
            alWxOrder.setTotalAmount(parkingOrder.getUnpaid_amount());
            alWxOrder.setSubject("停车订单支付");
            alWxOrder.createOutTradeNo(null, 1);
            // 调用支付接口
            WxPayConfig payConfig = wxPayConfigService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
            //String codeurl = wxPay.nativePprepay(alWxOrder, payConfig);
            //WxPay.jsapiPay(alWxOrder, payConfig);
            String url = wxPay.h5pay(alWxOrder, payConfig);
            //回写
            AlWxPayInfo alWxPayInfo = alWxOrder.getAlWxPayInfo();
            //alWxPayInfo.setPayURL(codeurl);
            return ResultInfo.error("调用微信支付失败！");
        }
        return ResultInfo.success(alWxOrder.getAlWxPayInfo().getPayURL());
    }

    /**
     * 出场收费
     * Author wzn
     * Date 2022/1/28 10:20
     */
    public PageInfo<RoadParkListVo> appearanceFee(Integer pageIndex, Integer pageSize, RoadParkListVo roadParkListVo) {
        PageHelper.startPage(pageIndex, pageSize);
        List<RoadParkListVo> roadParkInfo = mapper.appearanceFee(roadParkListVo);
        if (!roadParkInfo.isEmpty()) {
            for (RoadParkListVo r : roadParkInfo) {
                r.setResitime(TimeUtils.minConvertDayHourMin(Integer.valueOf(r.getResitime())));
            }
        }
        PageInfo<RoadParkListVo> page = new PageInfo<>(roadParkInfo);
        return page;
    }

    public RoadParkListVo appearanceFeeInfo(RoadParkListVo roadParkListVo) {
        RoadParkListVo roadParkInfo = mapper.appearanceFeeInfo(roadParkListVo);
        if (null != roadParkInfo) {
            roadParkInfo.setResitime(TimeUtils.minConvertDayHourMin(Integer.valueOf(roadParkInfo.getResitime())));
        }
        return roadParkInfo;
    }


    /**
     * create by wp at 2022/1/27 13:21
     * description: 打开闸机
     *
     * @param deviceId
     * @return java.lang.String
     */
    public String opengate(String deviceId) {
        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        String body = HttpRequest.post("http://192.168.0.7:7778/api/service/openGate")
                .body(params.toJSONString())
                .execute().body();
        return body;
    }


    /**
     * 接口传参封装方法
     * 3.3.1 支付账单同步
     * payType	int	支付类型（1-现金；2-支付宝；3-微信；4-银联） 必填
     * 返回参数
     * 名称	数据类型	说明
     * code	int	0 成功  其他失败
     * msg	string	success 其他失败原因
     * data	object
     */
    public ResultInfo setPayInfo(ParkingOrder parkingOrder, PaymentOrder paymentOrder) {
        Map<String, Object> map = new HashMap<>();
        if (notEmpty(parkingOrder.getPark_id())) {
            Park park = parkService.selectById(parkingOrder.getPark_id());
            if (notEmpty(park)) {
                //停车场编号  必填
                map.put("parkCode", park.getPark_code());
            }
        }
        if (notEmpty(parkingOrder.getCarno_id())) {
            OperateCarno operateCarno = operateCarnoService.selectById(parkingOrder.getCarno_id());
            if (notEmpty(operateCarno)) {
                //车牌号 必填
                map.put("carNo", operateCarno.getCar_no());
            }
        }
        //入车时间 (yyyy-MM-dd HH:mm:ss)  必填
        map.put("entryTime", new DateTime(parkingOrder.getDrivein_time()).toString("yyyy-MM-dd HH:mm:ss"));
        int payAmount = new BigDecimal(paymentOrder.getAmount()).multiply(new BigDecimal(100)).intValue();
        //订单总金额，单位分  必填
        map.put("amount", payAmount);
        //支付金额，单位分    必填
        map.put("payAmount", payAmount);
        //优惠金额，单位分    必填
        map.put("discount", 0);
        int payType = 1;
        //支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡)
        if (paymentOrder.getPayment_type().equals("2")) {
            payType = 3;
        } else if (paymentOrder.getPayment_type().equals("3")) {
            payType = 2;
        } else if (paymentOrder.getPayment_type().equals("4")) {
            payType = 1;
        } else if (paymentOrder.getPayment_type().equals("5")) {
            payType = 1;
        } else if (paymentOrder.getPayment_type().equals("6")) {
            payType = 4;
        }
        //支付类型（1-现金；2-支付宝；3-微信；4-银联） 必填
        map.put("payType", payType);
        //支付时间 (yyyy-MM-dd HH:mm:ss) 必填
        map.put("payTime", new DateTime(paymentOrder.getPay_time()).toString("yyyy-MM-dd HH:mm:ss"));
        //TODO 上报交控
        try {
            ResultInfo resultInfo = trafficControlUtils.payBillInfo(map);
            System.out.println("3.3.1 支付账单同步返回结果：" + resultInfo.getCode());
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public PageInfo<ParkingOrderVo> toList(Integer pageIndex, Integer pageSize, ParkingOrderVo parkOrderVo) {
        PageHelper.startPage(pageIndex, pageSize);

        List<ParkingOrderVo> parkingOrderVos = mapper.toList(parkOrderVo);

        PageInfo<ParkingOrderVo> page = new PageInfo<>(parkingOrderVos);

        return page;
    }

    public ParkingOrderVo toCount(ParkingOrderVo parkingOrderVo) {

        ParkingOrderVo parkingOrderVoData = mapper.toCount(parkingOrderVo);

        return parkingOrderVoData;

    }

    /**
     * 描 述： TODO(内部车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link PageInfo< ParkingOrderIDetailsVo>}
     */
    public PageInfo<ParkingOrderIDetailsVo> toInnerList(CommonVo data) {
        PageHelper.startPage(data.getPageNum(), data.getPageSize());
        List<ParkingOrderIDetailsVo> parkingOrderIDetailsVos = mapper.toInnerFreeListQyNb(data, "内部车", "0");
        PageInfo<ParkingOrderIDetailsVo> page = new PageInfo<>(parkingOrderIDetailsVos);
        return page;
    }

    /**
     * 描 述： TODO(免税车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link PageInfo< ParkingOrderIDetailsVo>}
     */
    public PageInfo<ParkingOrderIDetailsVo> toFreeEnterpriseList(CommonVo data) {
        PageHelper.startPage(data.getPageNum(), data.getPageSize());
        List<ParkingOrderIDetailsVo> parkingOrderIDetailsVos = mapper.toInnerFreeListQyNb(data, "企业免税车", "1");
        PageInfo<ParkingOrderIDetailsVo> page = new PageInfo<>(parkingOrderIDetailsVos);
        return page;
    }

    /**
     * 描 述： TODO(内部车 免费车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Map< String, ShouldBillRankingVo>}
     */
    public Map<String, ShouldBillRankingVo> toInnerFreeListQyStatistics(CommonVo data) {


        HashMap<String, ShouldBillRankingVo> retMap = new HashMap<>();
        // 内部车
        ShouldBillRankingVo nbcData = mapper.toInnerFreeListQyStatistics(data, "0");
        retMap.put("nbc", nbcData);
        // 免费车
        ShouldBillRankingVo mfcData = mapper.toInnerFreeListQyStatistics(data, "1");
        retMap.put("mfc", mfcData);
        return retMap;

    }

    /**
     * create by wh at 2022/1/28 15:40
     * description:人工抬杆/车辆总数
     *
     * @return : ResultInfo
     */
    public ResultInfo getOpenGateCarNums(Integer park_id, String startTime, String endTime) {
        String list = mapper.getOpenGateCarNums(park_id, startTime, endTime);
        return ResultInfo.success(list);
    }

    /**
     * create by wh at 2022/1/28 15:40
     * description:人工抬杆/车辆订单欠费总额
     *
     * @return : ResultInfo
     */
    public ResultInfo getOpenGateOrderArrears(Integer park_id, String startTime, String endTime) {
        String list = mapper.getOpenGateOrderArrears(park_id, startTime, endTime);
        return ResultInfo.success(list);
    }


    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    public List<ParkRsVo> getMobileCartCount(CommonVo data) {
        List<ParkRsVo> list = mapper.getMobileCartCount(data);
        return list;
    }

    public List<ParkRsVo> getAllCartCount(CommonVo data) {
        List<ParkRsVo> list = mapper.getAllCartCount(data);
        return list;
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车各收费方式收费总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    public List<ParkRsVo> getMobileCartPayStatistics(CommonVo data) {
        List<ParkRsVo> list = mapper.getMobileCartPayStatistics(data);
        return list;
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 人工抬杆统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    public List<ParkRsVo> getArtificialLiftRodCount(CommonVo data) {
        List<ParkRsVo> list = mapper.getArtificialLiftRodCount(data);
        return list;
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 逃费统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public List<ParkRsVo> getEscapeFeeStatistics(CommonVo data) {
        List<ParkRsVo> list = mapper.getEscapeFeeStatistics(data);
        return list;
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    public List<ParkRsVo> getFreeParkingCount(CommonVo data) {
        List<ParkRsVo> list = mapper.getFreeParkingCount(data);
        return list;
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List< ParkRsVo >}
     */
    public List<ParkRsVo> getWhiteCarnoCount(CommonVo data) {
        List<ParkRsVo> list = mapper.getWhiteCarnoCount(data);
        return list;
    }


    /**
     * create by zonglina at 2022/1/4 21:30
     * description:修改车牌号
     * 根据当前车牌id，修改车牌信息
     *
     * @return : null
     * @param:id 当前订单id
     * @param:car_id 当前订单car_id的车牌号
     * @param:car_no 输入的车牌号
     * @param:car_type 输入的车牌类型
     */
    public ResultInfo updateByCar(Integer id, Integer car_id, String car_no, String car_type) {
        //根据修改的车牌号去车牌号查询，查询到了则把订单信息中的car_id进行更改,如果未查到，提示去车牌信息库中维护
        OperateCarno carno = carnoService.selectOne(new QueryWrapper<OperateCarno>().eq("car_no", car_no)
                .eq("car_type", car_type).eq("is_del", "0"));
        if (null != carno) {
            ParkingOrder order = parkingOrderService.selectById(id);
            String oldPlateNo = carno.getCar_no();
            if (notEmpty(order.getPark_id())) {
                Park park = parkService.selectById(order.getPark_id());
                if (notEmpty(park)) {
                    //3.3.2 岗亭修改入车车牌号
                    ResultInfo resultInfo = setModifyCarNo(park.getPark_code(), oldPlateNo, car_no);
                    if (resultInfo.getCode() != 0) {
                        return ResultInfo.error(resultInfo.getMsg());
                    }
                }
            }
            order.setId(id);
            order.setCarno_id(carno.getId());
            if (updateById(order) > 0) {
                //修改车牌类型
                carno.setCar_type(car_type);
                carnoService.updateById(carno);
                String logMsg = "订单号为" + order.getOrder_no() + "的订单,车牌号已修改为" + car_no;
                return ResultInfo.success("操作成功！", logMsg);
            } else {
                return ResultInfo.success("操作失败！");
            }
        } else {
            return ResultInfo.success("车牌库中未查询到此车牌信息,请先去车牌库维护！");
        }
    }

    /**
     * 接口传参封装方法
     * 3.3.2 岗亭修改入车车牌号
     * 返回参数
     * 名称	数据类型	说明
     * code	int	0 成功  其他失败
     * msg	string	success 其他失败原因
     * data	object
     */
    public ResultInfo setModifyCarNo(String parkCode, String oldPlateNo, String plateNo) {
        Map<String, Object> map = new HashMap<>();
        //停车场编号  必填
        map.put("parkCode", parkCode);
        //原始车牌号，必填
        map.put("oldPlateNo", oldPlateNo);
        //车牌号 必填
        map.put("plateNo", plateNo);
        //TODO 上报交控
        try {
            ResultInfo resultInfo = trafficControlUtils.postModifyCarNo(map);
            System.out.println(" 3.3.2 岗亭修改入车车牌号返回结果：" + resultInfo.getCode());
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultInfo.success();
    }


    public ResultInfo exportExcel(ParkingOrderVo parkingOrderVo, HttpServletResponse response) {
        List<ParkingOrderVo> parkingOrderVos = mapper.toList(parkingOrderVo);

        List<ParkingOrderVo> wx = parkingOrderVos.stream().
                filter(x -> "2".equals(x.getPay_type())).collect(Collectors.toList());

        ExcelWriter writer = ExcelUtil.getWriter().renameSheet("流动车流水明细（微信）");

        writer.addHeaderAlias("pay_time", "支付时间");
        writer.addHeaderAlias("order_type", "缴费类型");
        writer.addHeaderAlias("paid_amount", "支付金额");
        writer.addHeaderAlias("type_name", "支付方式");
        writer.addHeaderAlias("pay_channel", "支付通道");
        writer.addHeaderAlias("pay_terminal", "支付终端");
        writer.addHeaderAlias("pay_scene", "支付场景");
        writer.addHeaderAlias("park_name", "停车场名称");
        writer.addHeaderAlias("payment_serialno", "移动支付编号");
        writer.addHeaderAlias("account_type", "结算账户类型");
        writer.addHeaderAlias("inspect_name", "收费员");
        writer.addHeaderAlias("shopee", "运营商户");

        writer.setOnlyAlias(true);
        writer.write(wx, true);


        List<ParkingOrderVo> zfb = parkingOrderVos.stream().
                filter(x -> "3".equals(x.getPay_type())).collect(Collectors.toList());
        writer.setSheet("流动车流水明细（支付宝）");

        writer.clearHeaderAlias();
        writer.addHeaderAlias("pay_time", "支付时间");
        writer.addHeaderAlias("order_type", "缴费类型");
        writer.addHeaderAlias("paid_amount", "支付金额");
        writer.addHeaderAlias("type_name", "支付方式");
        writer.addHeaderAlias("pay_channel", "支付通道");
        writer.addHeaderAlias("pay_terminal", "支付终端");
        writer.addHeaderAlias("pay_scene", "支付场景");
        writer.addHeaderAlias("park_name", "停车场名称");
        writer.addHeaderAlias("payment_serialno", "移动支付编号");
        writer.addHeaderAlias("account_type", "结算账户类型");
        writer.addHeaderAlias("inspect_name", "收费员");
        writer.addHeaderAlias("shopee", "运营商户");


        writer.setOnlyAlias(true);
        writer.write(zfb, true);


        List<ParkingOrderVo> other = parkingOrderVos.stream().
                filter(x -> "1".equals(x.getPay_type()) || "4".equals(x.getPay_type())).collect(Collectors.toList());
        writer.setSheet("流动车流水明细（第三方）");

        writer.clearHeaderAlias();
        writer.addHeaderAlias("pay_time", "支付时间");
        writer.addHeaderAlias("order_type", "缴费类型");
        writer.addHeaderAlias("paid_amount", "支付金额");
        writer.addHeaderAlias("type_name", "支付方式");
        writer.addHeaderAlias("pay_channel", "支付通道");
        writer.addHeaderAlias("pay_terminal", "支付终端");
        writer.addHeaderAlias("pay_scene", "支付场景");
        writer.addHeaderAlias("park_name", "停车场名称");
        writer.addHeaderAlias("payment_serialno", "移动支付编号");
        writer.addHeaderAlias("account_type", "结算账户类型");
        writer.addHeaderAlias("inspect_name", "收费员");
        writer.addHeaderAlias("shopee", "运营商户");


        writer.setOnlyAlias(true);
        writer.write(other, true);

        List<ParkingOrderVo> xj = parkingOrderVos.stream().
                filter(x -> "5".equals(x.getPay_type())).collect(Collectors.toList());
        writer.setSheet("流动车流水明细（现金）");

        writer.clearHeaderAlias();
        writer.addHeaderAlias("pay_time", "支付时间");
        writer.addHeaderAlias("order_type", "缴费类型");
        writer.addHeaderAlias("paid_amount", "支付金额");
        writer.addHeaderAlias("type_name", "支付方式");
        writer.addHeaderAlias("pay_channel", "支付通道");
        writer.addHeaderAlias("pay_terminal", "支付终端");
        writer.addHeaderAlias("pay_scene", "支付场景");
        writer.addHeaderAlias("park_name", "停车场名称");
        writer.addHeaderAlias("payment_serialno", "移动支付编号");
        writer.addHeaderAlias("account_type", "结算账户类型");
        writer.addHeaderAlias("inspect_name", "收费员");
        writer.addHeaderAlias("shopee", "运营商户");

        writer.setOnlyAlias(true);
        writer.write(xj, true);

//        List<ParkingOrderVo> jhzf = parkingOrderVos.stream().
//                filter(x -> StringUtils.isEmpty(x.getPay_type()) ).collect(Collectors.toList());
        List<ParkingOrderVo> jhzf = parkingOrderVos.stream().
                filter(x -> "8".equals(x.getPay_type())).collect(Collectors.toList());
        writer.setSheet("流动车流水明细（聚合支付）");

        writer.clearHeaderAlias();
        writer.addHeaderAlias("pay_time", "支付时间");
        writer.addHeaderAlias("order_type", "缴费类型");
        writer.addHeaderAlias("paid_amount", "支付金额");
        writer.addHeaderAlias("type_name", "支付方式");
        writer.addHeaderAlias("pay_channel", "支付通道");
        writer.addHeaderAlias("pay_terminal", "支付终端");
        writer.addHeaderAlias("pay_scene", "支付场景");
        writer.addHeaderAlias("park_name", "停车场名称");
        writer.addHeaderAlias("payment_serialno", "移动支付编号");
        writer.addHeaderAlias("account_type", "结算账户类型");
        writer.addHeaderAlias("inspect_name", "收费员");
        writer.addHeaderAlias("shopee", "运营商户");

        writer.setOnlyAlias(true);
        writer.write(jhzf, true);


        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("流动车支付流水.xls", "UTF-8"));
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

    public ResultInfo exportExcelNm(CommonVo data, HttpServletResponse response) {

        // List<ParkingOrderIDetailsVo> parkingOrderIDetailsVos = mapper.toInnerFreeList(parkingOrderIDetailsVo, isPark, "0");
        List<ParkingOrderIDetailsVo> parkingOrderIDetailsVos = mapper.toInnerFreeListQyNb(data, "内部车", "0");

        ExcelWriter writer = ExcelUtil.getWriter().renameSheet("内部车明细");

        writer.addHeaderAlias("prType", "业务类型");
        writer.addHeaderAlias("prName", "停车场/路内");
        writer.addHeaderAlias("carNo", "车牌号码");
        writer.addHeaderAlias("driveinTime", "驶入日期");
        writer.addHeaderAlias("driveoutTime", "驶出日期");
        writer.addHeaderAlias("carOwner", "车主/单位名称");
        writer.addHeaderAlias("phone", "联系电话");
        writer.addHeaderAlias("enterpriseName", "企业/单位名称");
        writer.setOnlyAlias(true);
        writer.write(parkingOrderIDetailsVos, true);

        //List<ParkingOrderIDetailsVo> ss = mapper.toInnerFreeList(parkingOrderIDetailsVo, isPark, "1");
        List<ParkingOrderIDetailsVo> ss = mapper.toInnerFreeListQyNb(data, "企业免税车", "1");

        writer.setSheet("企业（税免）车明细");

        writer.clearHeaderAlias();
        writer.addHeaderAlias("prType", "业务类型");
        writer.addHeaderAlias("prName", "停车场/路内");
        writer.addHeaderAlias("carNo", "车牌号码");
        writer.addHeaderAlias("driveinTime", "驶入日期");
        writer.addHeaderAlias("driveoutTime", "驶出日期");
        writer.addHeaderAlias("carOwner", "车主/单位名称");
        writer.addHeaderAlias("phone", "联系电话");
        writer.addHeaderAlias("enterpriseName", "企业/单位名称");

        writer.setOnlyAlias(true);
        writer.write(ss, true);

        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("内部车、免费车明细.xls", "UTF-8"));
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
     * 描 述： TODO(停车场 运营报表 营收总览)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<  ParkRsVo >}
     */
    public List<ParkRsVo> getSummaryOfRevenue(CommonVo data) {


        List<ParkRsVo> relist = new ArrayList<>();
        //获取全部停车场
        HashMap<Integer, Park> mapParks = DcCacheDataUtil.getMapParks();

        //停车场 运营报表 营收总览 流动车统计
        List<ParkRsVo> mobileCart = getMobileCartCount(data);
        Map<Integer, ParkRsVo> mobileCartMap = mobileCart.stream()
                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

//        //停车场 运营报表 营收总览 包月统计 车辆
//        data.setParking_type("1");
//        List<ParkRsVo> monthlyRent = monthlyManagementService.getMonthlyRentCount(data);
//        Map<Integer, ParkRsVo> monthlyRentMap = monthlyRent.stream()
//                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));
//        //停车场 运营报表 营收总览 包月统计 营收
//        List<ParkRsVo> monthlyRent2 = monthlyManagementService.getMonthlyRentCountParkAndRoad(data);
//        Map<Integer, ParkRsVo> monthlyRentMap2 = monthlyRent2.stream()
//                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

        //停车场 运营报表 营收总览 流动车各收费方式收费总额
        List<ParkRsVo> mobileCartPayStatistics = getMobileCartPayStatistics(data);
        Map<Integer, ParkRsVo> mobileCartPayStatisticsMap = mobileCartPayStatistics.stream()
                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

        //停车场 运营报表 营收总览 人工抬杆统计
        List<ParkRsVo> artificialLiftRod = getArtificialLiftRodCount(data);
        Map<Integer, ParkRsVo> artificialLiftRodMap = new HashMap<>();
        if (artificialLiftRod != null && artificialLiftRod.size() > 0) {
            artificialLiftRodMap = artificialLiftRod.stream()
                    .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));
        }


        //停车场 运营报表 营收总览 逃费统计
        List<ParkRsVo> escapeFeeStatistics = getEscapeFeeStatistics(data);
        Map<Integer, ParkRsVo> escapeFeeStatisticsMap = escapeFeeStatistics.stream()
                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

        //停车场 运营报表 营收总览 免费停车统计
        List<ParkRsVo> freeParking = getWhiteCarnoCount(data);
        Map<Integer, ParkRsVo> freeParkingMap = freeParking.stream()
                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

        List<ParkRsVo> allCartCount = getAllCartCount(data);
        Map<Integer, ParkRsVo> allCartCountMap = allCartCount.stream()
                .collect(Collectors.toMap(ParkRsVo::getParkId, Function.identity()));

        ParkRsVo totalBean = new ParkRsVo();

        for (Map.Entry<Integer, Park> entry : mapParks.entrySet()) {

            Integer key = entry.getKey();
            Park park = entry.getValue();
            if (data.getVariance() != null && data.getVariance() > 0) {
                if (data.getVariance().compareTo(key) != 0)
                    continue;
            }
            ParkRsVo bean = new ParkRsVo();

            bean.setParkId(park.getId());
            bean.setParkName(park.getPark_name());

            totalBean.setParkName("合计");

//            // 月租卡 车辆
//            bean.setYzCarNum(monthlyRentMap.get(key) == null ? 0 : monthlyRentMap.get(key).getYzCarNum());
//            if (notEmpty(totalBean.getYzCarNum())) {
//                Integer count = totalBean.getYzCarNum() + bean.getYzCarNum();
//                totalBean.setYzCarNum(count);
//            } else {
//                totalBean.setYzCarNum(bean.getYzCarNum());
//            }
//            // 月租卡 营收
//            bean.setYzTotalCost(monthlyRentMap2.get(key) == null ? null : monthlyRentMap2.get(key).getYzTotalCost());
//            if (notEmpty(totalBean.getYzTotalCost())) {
//                Double ldSumPaidAmount = Double.valueOf(totalBean.getYzTotalCost()) + Double.valueOf(bean.getYzTotalCost());
//                totalBean.setYzTotalCost(ldSumPaidAmount.toString());
//            } else {
//                totalBean.setYzTotalCost(bean.getYzTotalCost());
//            }
//            bean.setYzCounts(monthlyRentMap2.get(key) == null ? 0 : monthlyRentMap2.get(key).getYzCounts());
//            if (notEmpty(totalBean.getYzCounts())) {
//                Integer count = totalBean.getYzCounts() + bean.getYzCounts();
//                totalBean.setYzCounts(count);
//            } else {
//                totalBean.setYzCounts(bean.getYzCounts());
//            }
            // 流动车 车次
//            bean.setLdCarNum(mobileCartMap.get(key) == null ? 0 : mobileCartMap.get(key).getLdCarNum());
            // 流动车营收
            bean.setLdSumPaidAmount(mobileCartMap.get(key) == null ? null : mobileCartMap.get(key).getLdSumPaidAmount());
            if (notEmpty(totalBean.getLdSumPaidAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdSumPaidAmount()) + Double.valueOf(bean.getLdSumPaidAmount());
                totalBean.setLdSumPaidAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdSumPaidAmount(bean.getLdSumPaidAmount());
            }
            // 流动车 微信
            bean.setLdWxAmount(mobileCartPayStatisticsMap.get(key) == null ? null : mobileCartPayStatisticsMap.get(key).getLdWxAmount());
            if (notEmpty(totalBean.getLdWxAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdWxAmount()) + Double.valueOf(bean.getLdWxAmount());
                totalBean.setLdWxAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdWxAmount(bean.getLdWxAmount());
            }
            // 流动车 支付宝
            bean.setLdZfbAmount(mobileCartPayStatisticsMap.get(key) == null ? null : mobileCartPayStatisticsMap.get(key).getLdZfbAmount());
            if (notEmpty(totalBean.getLdZfbAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdZfbAmount()) + Double.valueOf(bean.getLdZfbAmount());
                totalBean.setLdZfbAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdZfbAmount(bean.getLdZfbAmount());
            }
            // 流动车 现金
            bean.setLdXjAmount(mobileCartPayStatisticsMap.get(key) == null ? null : mobileCartPayStatisticsMap.get(key).getLdXjAmount());
            if (notEmpty(totalBean.getLdXjAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdXjAmount()) + Double.valueOf(bean.getLdXjAmount());
                totalBean.setLdXjAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdXjAmount(bean.getLdXjAmount());
            }
            // 流动车 第三方
            bean.setLdDsfAmount(mobileCartPayStatisticsMap.get(key) == null ? "0.0" : mobileCartPayStatisticsMap.get(key).getLdDsfAmount());
            if (notEmpty(totalBean.getLdDsfAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdDsfAmount()) + Double.valueOf(bean.getLdDsfAmount());
                totalBean.setLdDsfAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdDsfAmount(bean.getLdDsfAmount());
            }
            // 流动车 钱包
            bean.setLdQbAmount(mobileCartPayStatisticsMap.get(key) == null ? "0.0" : mobileCartPayStatisticsMap.get(key).getLdQbAmount());
            if (notEmpty(totalBean.getLdQbAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdQbAmount()) + Double.valueOf(bean.getLdQbAmount());
                totalBean.setLdQbAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdQbAmount(bean.getLdQbAmount());
            }
            // 公务 单数
            bean.setLdGwOrderCount(artificialLiftRodMap.get(key) == null ? 0 : artificialLiftRodMap.get(key).getLdGwOrderCount());
            if (notEmpty(totalBean.getLdGwOrderCount())) {
                Integer count = totalBean.getLdGwOrderCount() + bean.getLdGwOrderCount();
                totalBean.setLdGwOrderCount(count);
            } else {
                totalBean.setLdGwOrderCount(bean.getLdGwOrderCount());
            }
            // 公务 费用
            bean.setLdGwAmount(artificialLiftRodMap.get(key) == null ? null : artificialLiftRodMap.get(key).getLdGwAmount());
            if (notEmpty(totalBean.getLdGwAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdGwAmount()) + Double.valueOf(bean.getLdGwAmount());
                totalBean.setLdGwAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdGwAmount(bean.getLdGwAmount());
            }
            // 故障 单数
            bean.setLdGzOrderCount(artificialLiftRodMap.get(key) == null ? 0 : artificialLiftRodMap.get(key).getLdGzOrderCount());
            if (notEmpty(totalBean.getLdGzOrderCount())) {
                Integer count = totalBean.getLdGzOrderCount() + bean.getLdGzOrderCount();
                totalBean.setLdGzOrderCount(count);
            } else {
                totalBean.setLdGzOrderCount(bean.getLdGzOrderCount());
            }
            // 故障 费用
            bean.setLdGzAmount(artificialLiftRodMap.get(key) == null ? null : artificialLiftRodMap.get(key).getLdGzAmount());
            if (notEmpty(totalBean.getLdGzAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getLdGzAmount()) + Double.valueOf(bean.getLdGzAmount());
                totalBean.setLdGzAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setLdGzAmount(bean.getLdGzAmount());
            }
            // 逃费 单数
            bean.setTdCounts(escapeFeeStatisticsMap.get(key) == null ? 0 : escapeFeeStatisticsMap.get(key).getTdCounts());
            if (notEmpty(totalBean.getTdCounts())) {
                Integer count = totalBean.getTdCounts() + bean.getTdCounts();
                totalBean.setTdCounts(count);
            } else {
                totalBean.setTdCounts(bean.getTdCounts());
            }
            // 逃费 费用
            bean.setTdSumAmount(escapeFeeStatisticsMap.get(key) == null ? null : escapeFeeStatisticsMap.get(key).getTdSumAmount());
            if (notEmpty(totalBean.getTdSumAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getTdSumAmount()) + Double.valueOf(bean.getTdSumAmount());
                totalBean.setTdSumAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setTdSumAmount(bean.getTdSumAmount());
            }
            //免费 内部车
            bean.setMfNbCount(freeParkingMap.get(key) == null ? 0 : freeParkingMap.get(key).getMfNbCount());
            if (notEmpty(totalBean.getMfNbCount())) {
                Integer count = totalBean.getMfNbCount() + bean.getMfNbCount();
                totalBean.setMfNbCount(count);
            } else {
                totalBean.setMfNbCount(bean.getMfNbCount());
            }
            //免费 企业
            bean.setMfQyCount(freeParkingMap.get(key) == null ? 0 : freeParkingMap.get(key).getMfQyCount());
            if (notEmpty(totalBean.getMfQyCount())) {
                Integer count = totalBean.getMfQyCount() + bean.getMfQyCount();
                totalBean.setMfQyCount(count);
            } else {
                totalBean.setMfQyCount(bean.getMfQyCount());
            }

            //总计 车次
            //BigDecimal carNum = ArithmeticUtils.add(bean.getYzCounts() == null ? "0" : bean.getYzCounts(), bean.getLdCarNum() == null ? "0" : bean.getLdCarNum());
            //bean.setZjCarNum(carNum.intValue());
//            bean.setZjCarNum(allCartCountMap.get(key) == null ? 0 : allCartCountMap.get(key).getZjCarNum());
            //总计 营收
            BigDecimal sumAmount = ArithmeticUtils.add(bean.getYzTotalCost() == null ? "0" : bean.getYzTotalCost(), bean.getLdSumPaidAmount() == null ? "0" : bean.getLdSumPaidAmount());
            bean.setZjAmount(sumAmount.toString());
            if (notEmpty(totalBean.getZjAmount())) {
                Double ldSumPaidAmount = Double.valueOf(totalBean.getZjAmount()) + Double.valueOf(bean.getZjAmount());
                totalBean.setZjAmount(ldSumPaidAmount.toString());
            } else {
                totalBean.setZjAmount(bean.getZjAmount());
            }
            relist.add(bean);
        }
        relist.add(totalBean);
        return relist;
    }

    //分页
    public PageInfo<ParkRoadRsVo> getSummaryOfRevenuePage(CommonVo data) {
        List<ParkRsVo> relist = getSummaryOfRevenue(data);

        //合计
        ParkRsVo totalVo = relist.get(relist.size() - 1);

        relist.remove(relist.size() - 1);
        List<ParkRsVo> list = relist.stream().skip((data.getPageNum() - 1) * data.getPageSize())
                .limit(data.getPageSize()).collect(Collectors.toList());
        //封装分页
        PageInfo page = new PageInfo();
        list.add(totalVo);
        page.setList(list);
        page.setTotal(relist.size());
        page.setPageNum(data.getPageNum());
        page.setPageSize(data.getPageSize());
        return page;
    }

    /**
     * 停车场
     * 申诉订单详情
     */
    public JSONObject appealsDetails(Integer id) {
        JSONObject object = new JSONObject();
        //申述信息
        OperateAppeal operateAppeal = operateAppealService.selectById(id);

//        LambdaQueryWrapper<AppealNoticeFeedback> appealNoticeFeedbackQuery = new LambdaQueryWrapper<>();
//        appealNoticeFeedbackQuery.eq(AppealNoticeFeedback::getParking_order_id, operateAppeal.getParking_order_id());
//        AppealNoticeFeedback appealNoticeFeedback = appealNoticeFeedbackService.selectOne(appealNoticeFeedbackQuery);

        RoadOrParkingVo orders = orderDetails(operateAppeal.getParking_order_id());
        //状态转换
        HashMap<String, SysDict> roadStatus = DcCacheDataUtil.getMapDicts("roadStatus");
        HashMap<String, SysDict> appeal_status = DcCacheDataUtil.getMapDicts("appeal_status");
        operateAppeal.setAppeal_order_status(roadStatus.get(operateAppeal.getAppeal_order_status()).getLabel());
        operateAppeal.setAppeal_status(appeal_status.get(operateAppeal.getAppeal_status()).getLabel());

        orders.setType(GlobalData.PARKING_TYPE_ROAD);
        //订单收费记录
        if (GlobalData.PARKING_ORDER_ALREADYPAY.equals(orders.getStatus()) || GlobalData.PARKING_ORDER_COMPLETE.equals(orders.getStatus())) {
            //支付记录
            if (null != orders.getPayment_id()) {
                orders.setPayments(paymentOrderService.selectByPayId(orders.getPayment_id()));
                //退款记录
                orders.setRefunds(refundManagementService.selectById(orders.getPayment_id(), orders.getId()));
            }
            //开票信息
        }
        //计算费用
        if (GlobalData.PARKING_ORDER_STOP.equals(orders.getStatus())) {
            PaymentVo pay = getParkingCharging(new AppealRecordVo(String.valueOf(orders.getId())));
            if (null != pay) {
                orders.setSum_amount(pay.getPay_money());
                orders.setResitime(TimeUtils.formatTime(TimeUtils.computeMinute(orders.getDrivein_time(), new Date())));
            } else {
                orders.setSum_amount("0");
                orders.setResitime("0");
            }
        }
        orders.setOperateAppeal(operateAppeal);
//        orders.setAppealNoticeFeedback(appealNoticeFeedback);

//        //反馈信息
//        List<AppealNoticeFeedback> feedbacks = appealNoticeFeedbackService.selectList(new QueryWrapper<AppealNoticeFeedback>().eq("operate_appeal_id", id));
//        feedbacks.forEach(a -> {
//            if (notEmpty(a.getInspect_id())) {
//                InspectManage inspectManage = inspectManageService.selectById(a.getInspect_id());
//                if (notEmpty(inspectManage)) {
//                    a.setInspect_name(inspectManage.getName());
//                }
//            }
//        });
//        if (null != feedbacks && feedbacks.size() > 0) {
//            object.put("feedbacks", feedbacks);
//        }

        object.put("orders", orders);
        return object;
    }

    /**
     * 描 述： TODO(查询停车场在停车数量)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link List< StatisticsVo>}
     */
    public List<StatisticsVo> getParkOrderCount() {
        // GlobalData.ISDEL_NO;
        List<StatisticsVo> parkOrderCount = mapper.getParkOrderCount();

        return parkOrderCount;
    }


    /**
     * 各区域收费总额排名TOP5
     * 0 路段 1停车场
     * 作 者： thr
     */
    public List<DataChartVo> getTotalChargesTop5Data(String type, String timeType) {
        List<DataChartVo> list = mapper.getTotalChargesTop5Data(type, timeType);
        return list;
    }

    /**
     * 今日收费总额趋势分析
     * type 0 路段 1停车场
     * dayType 0 今日 -1 昨日
     * 作 者： thr
     */
    public List<DataChartVo> getTotalChargesData(String type, int dayType) {
        List<DataChartVo> list = mapper.getTotalChargesData(type, dayType);
        return list;
    }

    //各区域在停车辆总数排名TOP5
    public List<DataChartVo> getCarTop5Data(String type, String timeType) {
        return mapper.getCarTop5Data(type, timeType);
    }

    //昨日停车占比
    public List<String> getParkTimeData(String type) {
        return mapper.getParkTimeData(type);
    }

    //昨日停车占比总次数
    public String getParkTimeCountData(String type) {
        return mapper.getParkTimeCountData(type);
    }

    //近五日车位占用量及趋势
    public List<String> getParticipatingData(String type) {
        return mapper.getParticipatingData(type);
    }

    //今年总收费额
    public DataChartVo chargesYear(String type, String year) {
        return mapper.chargesYear(type, year);
    }

    //占用车位
    public DataChartVo occupationData(String type) {
        return mapper.occupationData(type);
    }

    //停车实际收费
    public DataChartVo actualParkingChargeData(String type) {
        return mapper.actualParkingChargeData(type);
    }

    //停车应该收费
    public DataChartVo parkingShouldChargedData(String type) {
        return mapper.parkingShouldChargedData(type);
    }

    //路段订单导出
    public void exportRoad(RoadParkListVo vo, HttpServletResponse response) {
        List<RoadParkListVo> list = mapper.selectWXPage(vo);
        ExcelWriter writer = ExcelUtil.getWriter();
        for (RoadParkListVo temp : list) {
            if (null != temp.getResitime() && !"0".equals(temp.getResitime())) {
                temp.setResitime(TimeUtils.formatTime(Integer.valueOf(temp.getResitime())));
            } else {
                temp.setResitime("0");
            }
            if (null != temp.getIs_discount()) {
                if ("0".equals(temp.getIs_discount())) {
                    temp.setIs_discount("是");
                } else {
                    temp.setIs_discount("否");
                }
            }
            HashMap<String, SysDict> free_type_map = DcCacheDataUtil.getMapDicts("free_type");
            //免费类型
            if (notEmpty(temp.getFree_type())) {
                temp.setFree_type(free_type_map.get(temp.getFree_type()).getLabel());
            }
        }
        writer.addHeaderAlias("orderNo", "订单号");
        writer.addHeaderAlias("placeName", "路段");
        writer.addHeaderAlias("berth", "泊位编码");
        writer.addHeaderAlias("carNo", "车牌号");
        writer.addHeaderAlias("driveinTime", "停入时间");
        writer.addHeaderAlias("driveoutTime", "离场时间");
        writer.addHeaderAlias("resitime", "停留时间");
        writer.addHeaderAlias("sum_amount", "停车费用");
        writer.addHeaderAlias("is_discount", "商家优惠");
        writer.addHeaderAlias("discount_money", "优惠金额");
        writer.addHeaderAlias("free_type", "免单类型");
        writer.addHeaderAlias("status_name", "状态");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("路段订单.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultInfo nativePay(Integer id) {
        //查询订单
        ParkingOrder parkingOrder = selectById(id);
        if (parkingOrder == null) {
            return ResultInfo.error("未查询到待支付的订单！");
        }
        AlWxOrder alWxOrder = new AlWxOrder();
        //微信支付
        AlWxOrder wxOrder = new AlWxOrder();
        wxOrder.setTotalAmount(parkingOrder.getUnpaid_amount());
        wxOrder.setSubject("停车订单支付");
        wxOrder.createOutTradeNo(null, 1);
        // 调用支付接口
        WxPayConfig payConfig = wxPayConfigService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
        String codeUrl = wxPay.nativePprepay(wxOrder, payConfig);
        System.out.println(codeUrl);
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPayment_type(GlobalData.PARKING_ORDER_TYPE_WX);
        paymentOrder.setPayment_resource("3");
        paymentOrder.setStatus(GlobalData.ORDER_STAPAY);
        paymentOrder.setAmount(wxOrder.getTotalAmount());
        paymentOrder.setPayment_no(wxOrder.getOutTradeNo());
        paymentOrder.setCreate_time(new Date());
        paymentOrder.setUpdate_time(new Date());
        paymentOrder.setPay_time(new Date());
        paymentOrder.setIs_del(GlobalData.ISDEL_NO);
        paymentOrderService.insert(paymentOrder);
        parkingOrder.setPayment_id(paymentOrder.getId());
        parkingOrder.setPay_time(new Date());
        parkingOrder.setUpdate_time(new Date());
        parkingOrder.setPay_type(GlobalData.PARKING_ORDER_TYPE_WX);
        updateById(parkingOrder);
        return ResultInfo.success(codeUrl);
    }

    @SneakyThrows
    public ResultInfo queryPaySucess(Integer orderId) {
        //查询订单支付状态
        ParkingOrder parkingOrder = selectById(orderId);
        if (!"2".equals(parkingOrder.getStatus())) {
            return ResultInfo.error("该订单已支付！");
        } else {
            //查询支付记录
            if (parkingOrder.getPayment_id() != null) {
                PaymentOrder paymentOrder = paymentOrderService.selectById(parkingOrder.getPayment_id());
                if (paymentOrder != null) {
                    WxPayConfig payConfig = wxPayConfigService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
                    String result = wxPay.queryPayStatus(paymentOrder.getPayment_no(), payConfig);
                    JSONObject jsonObject = JSON.parseObject(result);
                    String status = jsonObject.getString("trade_state");
                    switch (status) {
                        case "SUCCESS":
                            paymentOrder.setStatus("2");
                            paymentOrder.setUpdate_time(new Date());
                            paymentOrder.setPayment_serialno(jsonObject.getString("transaction_id"));
                            paymentOrderService.updateById(paymentOrder);

                            String paiedAmount = new BigDecimal(parkingOrder.getSum_amount()).subtract(new BigDecimal(parkingOrder.getDiscount_amount())).toString();
                            parkingOrder.setPaid_amount(paiedAmount);
                            parkingOrder.setUnpaid_amount("0");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = format.parse(jsonObject.getString("success_time").substring(0, 18).replaceAll("T", " "));
                            parkingOrder.setPay_time(date);
                            parkingOrder.setPay_type("2");
                            parkingOrder.setStatus(GlobalData.PARKING_ORDER_COMPLETE);
                            updateById(parkingOrder);
                            //TODO 开闸
//                            ResultInfo resultInfo = parkingGateUtils.openGate(parkingOrder.getPark_id(), parkingOrder.getDriveout_gate());
//                            //============== 正常驶出======================================
//                            String code = JSONObject.parseObject(String.valueOf(resultInfo.getData())).getString("state");
//                            if ("1".equals(code)) {
//                                parkingOrder.setOperation_type_out(GlobalData.OPERATION_TYPE_REGULAR);
//                            }
                            return ResultInfo.success("支付成功");
                        case "REVOKED":
                            paymentOrder.setStatus("3");
                            paymentOrder.setUpdate_time(new Date());
                            paymentOrderService.updateById(paymentOrder);
                            return ResultInfo.error("支付失败");
                        case "PAYERROR":
                            paymentOrder.setStatus("3");
                            paymentOrder.setUpdate_time(new Date());
                            paymentOrderService.updateById(paymentOrder);
                            return ResultInfo.error("支付失败");
                        default:
                            ResultInfo.error("支付失败");
                    }
                } else {
                    return ResultInfo.error("无支付记录！");
                }
            } else {
                return ResultInfo.error("该订单未支付！");
            }
        }
        return ResultInfo.error("支付失败");
    }

    /**
     * 根据停车场、车牌号、车牌类型查询停车场在停订单
     *
     * @param parkId
     * @param carType
     * @param carNo
     * @return
     */
    public List<ParkingOrderVo> getOrderByParkAndCarNo(Integer parkId, Integer carType, String carNo) {
        List<ParkingOrderVo> orders = mapper.getOrderByParkAndCarNo(parkId, carType, carNo);
        orders.forEach(x -> {
            //getParkingCharging()
            switch (x.getStatus()) {
                case "1":
                    x.setState_name("在停");
                    break;
                case "2":
                    x.setState_name("待缴费");
                    break;
            }
        });
        return orders;
    }

    /**
     * 总优惠金额
     */
    public ParkingOrderVo sumDiscountMoney(ParkingOrderVo vo) {
        return mapper.sumDiscountMoney(vo);
    }

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    public List<DataChartVo> getMoneyDataByHours(ParkingOrderVo vo) {
        return mapper.getMoneyDataByHours(vo);
    }

    /**
     * 欠费分析 折线图
     * 数据分析页面 路侧/停车场详情页面使用
     * 今日0时至当前时间各个小时整点欠费金额统计
     */
    public List<DataChartVo> getArrearsMoneyDataByHours(ParkingOrderVo vo) {
        return mapper.getArrearsMoneyDataByHours(vo);
    }

    /**
     * 在停车辆
     */
    public ReportVo getZtCount(ParkingOrderVo vo) {
        return mapper.getZtCount(vo);
    }

    /**
     * 在停车辆
     * 各个路侧/停车场
     */
    public List<ReportVo> getZtCountByIds(ParkingOrderVo vo) {
        return mapper.getZtCountByIds(vo);
    }

    /**
     * 在停数量排名TOP5
     */
    public List<ReportVo> getZtCountTop5(ParkingOrderVo vo) {
        return mapper.getZtCountTop5(vo);
    }

    /**
     * 今日停车时长占比
     */
    public List<ReportVo> getTodayParkTimeData(String type, Integer id) {
        return mapper.getTodayParkTimeData(type, id);
    }

    /**
     * 统计今日停车占比总次数
     */
    public String getTodayParkTimeCount(String type, Integer id) {
        return mapper.getTodayParkTimeCount(type, id);
    }

    /**
     * 折线图： 出入趋势双折线：7天内出入次数
     */
    public List<DataChartVo> getInOutCount(String type, String inOutType) {
        return mapper.getInOutCount(type, inOutType);
    }

    /**
     * 自助停车率（排除人工订单和人工抬杆的所有状态订单）
     */
    public List<DataChartVo> getCountBySource(RoadParkListVo vo) {
        return mapper.getCountBySource(vo);
    }

    /**
     * 订单收费率（已缴费和已完成的订单数量/总数量）
     */
    public List<DataChartVo> getCountByStatus(RoadParkListVo vo) {
        return mapper.getCountByStatus(vo);
    }

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 缴费时间-出场时间<15分钟的数量
     */
    public String getCountByLt15Min(RoadParkListVo vo) {
        return mapper.getCountByLt15Min(vo);
    }

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 总已缴费、总已完成状态的订单
     */
    public String getYjfYwcCount(RoadParkListVo vo) {
        return mapper.getYjfYwcCount(vo);
    }

    /**
     * 超24H占位率（已缴费+已完成的超24H停车时长/总订单数量）
     */
    public String getCountByGt24h(RoadParkListVo vo) {
        return mapper.getCountByGt24h(vo);
    }

    /**
     * 发票发放率
     */
    public String getFpCount(RoadParkListVo vo) {
        return mapper.getFpCount(vo);
    }

    /**
     * 欠费车辆排名TOP10
     */
    public List<ParkingOrderVo> getSumAmountTop10ByCarNo(String type) {
        return mapper.getSumAmountTop10ByCarNo(type);
    }

    /**
     * 路段和停车场订单信息
     * 查询视图
     */
    public List<RoadParkListVo> selectRoadParkList(RoadParkListVo vo) {
        return mapper.selectRoadParkList(vo);
    }

    /**
     * 应收金额、已付金额/营收金额、待付金额/欠费金额
     */
    public RoadParkListVo getSumAmounts(RoadParkListVo vo) {
        return mapper.getSumAmounts(vo);
    }

    /**
     * 今日进出 折线图
     * 数据分析页面
     * 今日0时至当前时间各个小时整点进出次数统计
     */
    public List<DataChartVo> getInOutDataByHours(RoadParkListVo vo) {
        return mapper.getInOutDataByHours(vo);
    }

    /**
     * 近30日进出 折线图
     */
    public List<DataChartVo> getInOutDaysData(RoadParkListVo vo) {
        return mapper.getInOutDaysData(vo);
    }

    /**
     * 支付记录关联 路段和停车场订单信息
     */
    public List<RoadParkListVo> getOrderDataByPaymentId(RoadParkListVo vo) {
        List<RoadParkListVo> list = mapper.getOrderDataByPaymentId(vo);
        list.forEach(x -> {
            x.setResitime(TimeUtils.formatTime(Integer.valueOf(x.getResitime())));
        });
        return mapper.getOrderDataByPaymentId(vo);
    }

    /**
     * 删除订单
     */
    public ResultInfo parkOrderDel(RoadParkListVo vo) {
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setId(vo.getId());
        parkingOrder.setIs_del("1");
        parkingOrder.setUpdate_time(new Date());
        parkingOrderService.updateById(parkingOrder);

        String logMsg = "删除停车场订单：" + vo.getOrderNo();
        return ResultInfo.success("删除成功！", logMsg);
    }

    /**
     * 人工放行
     */
    public ResultInfo updStatus(RoadParkListVo vo) {
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setId(vo.getId());
        //订单状态 1在停、2 待缴费、3已缴费、4已完成
        parkingOrder.setStatus("4");
        parkingOrder.setUpdate_time(new Date());
        parkingOrder.setFxReason(vo.getFxReason());
        parkingOrderService.updateById(parkingOrder);
        return ResultInfo.success();
    }

    /**
     * 停车场概览统计分析 导出
     */
    public void export(CommonVo data, HttpServletResponse response) {
        List<ShouldBillRankingVo> shouldBillRanking = parkingOrderService.getParkingOrderRanking(data);

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("title", "停车场名称");
        writer.addHeaderAlias("stopcarnum", "泊位数");
        writer.addHeaderAlias("counts", "订单数");
        writer.addHeaderAlias("counts2", "非零订单数");
        writer.addHeaderAlias("sumAmount", "应收金额");
        writer.addHeaderAlias("paidAmount", "实收金额");
        writer.addHeaderAlias("unpaidAmount", "在停金额");
        writer.addHeaderAlias("unpaidAmount2", "待缴费金额");
        writer.addHeaderAlias("jfs", "缴费数");
        writer.addHeaderAlias("jfl", "缴费率");
        writer.addHeaderAlias("zzl", "周转率");

        writer.setOnlyAlias(true);
        writer.write(shouldBillRanking, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("停车场概览.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
