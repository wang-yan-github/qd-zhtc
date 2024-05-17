package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.dao.OperateAppealDao;
import com.jsdc.zhtc.mapper.OperateAppealMapper;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;


/**
 * ClassName: OperateAppealService
 * Description:
 * date: 2021/12/30 10:22
 * 申诉管理表
 *
 * @author zln
 */
@Service
@Transactional
public class OperateAppealService extends BaseService<OperateAppealDao, OperateAppeal> {


    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private OperateAppealMapper operateAppealMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * create by zonglina at 2022/1/4 15:37
     * description:根据车牌号、创建时间查询，当前订单号信息
     * 1、用户是停车场时,传入当前用户的停车场id
     *
     * @return : ResultInfo
     * @param:bean 参数
     * @param:pageIndex
     * @param:pageSize
     */
    public ResultInfo selectByParkingOrderPage(RoadParkListVo bean, Integer pageIndex, Integer pageSize) {
        if (Base.notEmpty(bean.getCarNo()) || Base.notEmpty(bean.getStartTime())) {
            if (GlobalData.PARKING_TYPE_PLAT.equals(sysUserService.getUser().getUser_type())) {
                bean.setType(GlobalData.PARKING_TYPE_PLAT); //停车场
                bean.setId(sysUserService.getUser().getPark_id());
                bean.setUserType(sysUserService.getUser().getUser_type());
                return ResultInfo.success(parkingOrderService.selectWXPage(pageIndex, pageSize, bean));
            } else {
//                if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.redisRoadOrPark())) {
//                    bean.setType(GlobalData.PARKING_TYPE_ROAD); //运营
//                    return ResultInfo.success(parkingOrderService.selectWXPage(pageIndex, pageSize, bean));
//                } else {
                    bean.setType(GlobalData.PARKING_TYPE_PLAT); //停车场
                    return ResultInfo.success(parkingOrderService.selectWXPage(pageIndex, pageSize, bean));
//                }
            }
        } else {
            return ResultInfo.error("暂无数据");
        }
    }


    /**
     * create by zonglina at 2022/1/4 16:15
     * description:申诉订单分页
     *
     * @return : null
     * @param:null
     */
    public ResultInfo selectByPage(RoadOrParkingCommentVo bean, Integer pageIndex, Integer pageSize) {
        JSONObject object = new JSONObject();
//        QueryWrapper<OperateAppeal> queryWrapper = new QueryWrapper<OperateAppeal>()
//                .eq("appeal_status", GlobalData.OPERATE_APPEAL_DCL)
//                .eq("is_del", "0")
//                .eq("appeal_type", sysUserService.redisRoadOrPark()
//                );
//        if (notEmpty(bean.getKeys())) {
//            queryWrapper.eq("order_no", bean.getKeys()).or().eq("order_no", bean.getKeys());
//        }
//
//        Long count = selectCount(queryWrapper);
        PageHelper.startPage(pageIndex, pageSize);
        List<OperateAppealVo> list = operateAppealMapper.selectByPage(bean, "1");
        list.forEach(a -> {
            HashMap<String, SysDict> roadStatus = DcCacheDataUtil.getMapDicts("roadStatus");
            HashMap<String, SysDict> appeal_status = DcCacheDataUtil.getMapDicts("appeal_status");
            if (null != a.getAppeal_order_status()) {
                a.setAppeal_order_status(roadStatus.get(a.getAppeal_order_status()).getLabel());
            }
            if (null != a.getAppeal_status()) {
                a.setAppeal_status(appeal_status.get(a.getAppeal_status()).getLabel());
            }
            if (Base.notEmpty(a.getResitime())) {
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            }
            String amount = StringUtils.isNotBlank(a.getUnpaid_amount()) ? a.getUnpaid_amount() : "0";
            a.setUnpaid_amount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).toString());

        });
//        object.put("wcl_count", count);
        object.put("page", new PageInfo<>(list));
        List<ReportVo> voList = operateAppealMapper.getAppealStatusCount(bean, "1");

        int zs_count = 0;
        int ycl_count = 0;
        int wcl_count = 0;
        for (ReportVo vo : voList) {
            //申诉状态 1 待处理 2通过 3驳回 4完成
            if (vo.getName().equals("1")) {
                wcl_count = vo.getCounts();
            } else {
                ycl_count += vo.getCounts();
            }
            zs_count += vo.getCounts();
        }
        object.put("zs_count", zs_count);
        object.put("ycl_count", ycl_count);
        object.put("wcl_count", wcl_count);
        return ResultInfo.success(object);
    }


    // 统计
    public Integer census(MsgManagerVo msgManagerVo) {
        QueryWrapper<OperateAppeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("appeal_status", "1");
        queryWrapper.eq("is_del", 0);
        if (StringUtils.isNotEmpty(msgManagerVo.getModel())) {
            queryWrapper.eq("appeal_type", msgManagerVo.getModel());
        }
        if (null != msgManagerVo.getPark_id()) {
            queryWrapper.eq("parking_order_id", msgManagerVo.getPark_id());
        }

        return selectList(queryWrapper).size();
    }


    //申诉订单导出
    public void exportOperateAppeal(RoadOrParkingCommentVo bean, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<OperateAppealVo> list = operateAppealMapper.selectByPage(bean, "1");
        list.forEach(a -> {
            HashMap<String, SysDict> roadStatus = DcCacheDataUtil.getMapDicts("roadStatus");
            HashMap<String, SysDict> appeal_status = DcCacheDataUtil.getMapDicts("appeal_status");
            if (null != a.getAppeal_order_status()) {
                a.setAppeal_order_status(roadStatus.get(a.getAppeal_order_status()).getLabel());
            }
            if (null != a.getAppeal_status()) {
                a.setAppeal_status(appeal_status.get(a.getAppeal_status()).getLabel());
            }
            if (Base.notEmpty(a.getResitime())) {
                a.setResitime(TimeUtils.formatTime(Integer.valueOf(a.getResitime())));
            }
        });
        writer.addHeaderAlias("appeal_time", "申诉时间");
        writer.addHeaderAlias("order_no", "订单号");
        writer.addHeaderAlias("car_no", "车牌号");
        writer.addHeaderAlias("resitime", "停留时长");
        writer.addHeaderAlias("unpaid_amount", "停车费用(元)");
        writer.addHeaderAlias("appeal_order_status", "申诉时订单状态");
        writer.addHeaderAlias("appeal_status", "申诉状态");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("申诉订单.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 近七日服务类型趋势 订单申诉 最近7日/数量
     */
    public List<ReportVo> getDaysCount() {
        return operateAppealMapper.getDaysCount();
    }
}
