package com.jsdc.zhtc.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.service.AttendanceManagementService;
import com.jsdc.zhtc.service.OperateCarnoService;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.vo.order.AppealRecordVo;
import com.jsdc.zhtc.vo.order.PaymentVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 定时任务
 * 停车订单
 * 1.查询出闸待支付订单状态
 * 2.刷新路测设备在线状态
 * 3.每天刷新一次车牌是否超期免税时间，若超期则置为普通车辆
 * 4.生成停车时长
 */
@Component
public class OrderTask {
    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private OperateCarnoService carnoService;
    @Autowired
    private AttendanceManagementService attendanceManagementService;

    @Value("${heartBeat.period}")
    private int period;

    @Value("${heartBeat.timeout_period_times}")
    private int timeoutPeriodTimes;

    /**
     * 停车场
     * 刷停车场停车时长和费用
     * 3分钟执行一次
     */
    @Scheduled(cron = "0 */3 * * * ?")
    public void freshParkOrder() {
//        System.out.println("刷停车场的停车时长和费用！！");
        /**
         * 查询停车场在停订单
         */
        QueryWrapper<ParkingOrder> qw = new QueryWrapper<>();
        qw.eq("is_del", "0");
        qw.eq("status", GlobalData.PARKING_ORDER_STOP);
        List<ParkingOrder> parkingOrderList = parkingOrderService.selectList(qw);
        //计算停车时长和金额
        for (ParkingOrder p : parkingOrderList) {
            PaymentVo pay = parkingOrderService.getParkingCharging(new AppealRecordVo(String.valueOf(p.getId()), new Date()));
            //刷新订单数据
            ParkingOrder parkingOrder = new ParkingOrder();
            parkingOrder.setId(p.getId());
            parkingOrder.setResitime(TimeUtils.computeMinute(p.getDrivein_time(), new Date()));
            parkingOrder.setSum_amount(pay.getPay_money());
            parkingOrder.setDiscount_amount(pay.getDiscount_money());
//            parkingOrder.setPaid_amount("0");
            parkingOrder.setUnpaid_amount(new BigDecimal(pay.getPay_money()).subtract(new BigDecimal(pay.getDiscount_money())).setScale(2).toString());
            parkingOrder.updateById();
        }
    }

    /**
     * 每天刷新一次车牌是否超期免税时间，若超期则置为普通车辆
     */
    @Scheduled(cron = "59 59 11 * * ?")
    public void freshCarnoRoster() {
        System.out.println("----------------------免税车定时任务开始-------------------------------");
        carnoService.freshCarnoRoster();
        System.out.println("----------------------免税车定时任务结束-------------------------------");
    }

    /**
     * 定时任务
     * 1.每日0时生成打卡记录
     * 修改昨日未打卡数据为旷工状态
     * 收费员、巡检员
     * 0时 1分
     * 2.个人白名单车辆 定时任务 修改已到期车牌和待生效白名单车牌
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void onCreateSignRecord() {
        //1.修改昨日未打卡数据为旷工状态
        attendanceManagementService.onCreateRecord();

        //2.个人白名单车辆 定时任务 修改已到期车牌和待生效白名单车牌
        carnoService.onUpdExpiredWhiteList();

        System.out.println(new DateTime().toString("yyyy-MM-dd") + "------------------------生成收费员/巡检员打卡记录----------------------------");
    }


    /**
     * 5分钟执行一次
     * 处理有驶离时间，状态还是在停状态停车订单
     * 若为免费时段、包月、白名单免单类型停车订单，状态变更为已完成
     * 若为付费订单，状态变更为待缴费
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void updParkingOrder() {
        //订单状态 1在停、2 待缴费、3已缴费、4已完成
        List<ParkingOrder> list = parkingOrderService.selectList(new QueryWrapper<ParkingOrder>().eq("is_del", "0")
                .eq("status", "1").isNotNull("driveout_time"));
        String now = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        //计算停车时长和金额
        for (ParkingOrder parkingOrder : list) {
            //免费类型 0：包月 1：白名单 2：免费时段 3：特殊开闸
            if (StringUtils.isNotEmpty(parkingOrder.getFree_type())) {
                //订单状态 1在停、2 待缴费、3已缴费、4已完成
                parkingOrder.setStatus("4");
                parkingOrder.setSum_amount("0");
                parkingOrder.setDiscount_amount("0");
                parkingOrder.setPaid_amount("0");
                parkingOrder.setUnpaid_amount("0");
            } else {
                //订单状态 1在停、2 待缴费、3已缴费、4已完成
                parkingOrder.setStatus("2");
            }
            parkingOrder.setResitime(TimeUtils.computeMinute(parkingOrder.getDrivein_time(), parkingOrder.getDriveout_time()));
            parkingOrder.setRemark("定时任务：订单状态1变更为" + parkingOrder.getStatus() + ",修改时间：" + now);
            parkingOrderService.updateById(parkingOrder);
        }

    }
}
