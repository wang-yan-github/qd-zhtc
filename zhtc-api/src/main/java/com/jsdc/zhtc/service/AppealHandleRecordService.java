package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.AppealHandleRecordDao;
import com.jsdc.zhtc.mapper.AppealHandleRecordMapper;
import com.jsdc.zhtc.model.AppealHandleRecord;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.AppealHandleRecordVo;
import com.jsdc.zhtc.vo.ReportVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * ClassName: AppealHandleRecordService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class AppealHandleRecordService extends BaseService<AppealHandleRecordDao, AppealHandleRecord> {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private OperateCarnoService carnoService;
    @Autowired
    private OperateAppealService appealService;
    @Autowired
    private AppealHandleVoucherService voucherService;
    @Autowired
    private AppealHandleRecordMapper mapper;

    String logMsg = "";

    /**
     * create by zonglina at 2022/1/4 19:53
     * description:保存功能
     * 停车场-审批功能(路段不存在此功能)
     *
     * @return : null
     * @param:null
     */
    public ResultInfo parkApprove(AppealHandleRecordVo bean) {
        //当等于运营端和
        if (null == bean.getId()) {
            if (StringUtils.isNotEmpty(bean.getCar_no()) && StringUtils.isNotEmpty(bean.getCarno_type())) {
                OperateCarno carno = carnoService.selectOne(Wrappers.<OperateCarno>lambdaQuery().eq(OperateCarno::getCar_no, bean.getCar_no()).eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO).eq(OperateCarno::getCar_type, bean.getCarno_type()));
                if (null != carno) {
                    bean.setCarno_id(String.valueOf(carno.getId()));
                    bean.setCar_no(carno.getCar_no());
                    bean.setCarno_type(carno.getCar_type());
                } else {
                    OperateCarno car = new OperateCarno();
                    car.setCar_no(bean.getCar_no());
                    car.setCar_type(bean.getCarno_type());
                    car.setRoster_type("1");
                    car.setCreate_time(new Date());
                    car.setUpdate_time(new Date());
                    car.setIs_del(GlobalData.ISDEL_NO);
                    carnoService.insert(car);
                    bean.setCarno_id(String.valueOf(carno.getId()));
                    bean.setCar_no(carno.getCar_no());
                    bean.setCarno_type(carno.getCar_type());
                }
            }
            bean.setCreate_time(new Date());
            bean.setCreate_user(sysUserService.getUser().getId());
            bean.setIs_del(0);
            if (insert(bean) > 0) {
                //驳回新增图片
                if (GlobalData.HANDLE_STATUS_REJECT.equals(bean.getHandle_status())) {
                    voucherService.save(bean.getFileIds(), bean.getId());
                }
                //改变申诉状态
                if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.getUser().getUser_type())) {
                    if (!"4".equals(bean.getHandle_type())) {
                        OperateAppeal appeal = appealService.selectById(bean.getOperate_appeal_id());
                        appeal.setAppeal_order_status(bean.getAppeal_order_status());
                        if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.getUser().getUser_type())) {
                            appeal.setAppeal_status(bean.getHandle_status());
                        }
                        appealService.updateById(appeal);
                    }
                } else {
                    if (!"4".equals(bean.getHandle_type())) {
                        OperateAppeal appeal = appealService.selectById(bean.getOperate_appeal_id());
                        appeal.setAppeal_order_status(bean.getAppeal_order_status());
                        if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.getUser().getUser_type())) {
                            appeal.setAppeal_status(bean.getHandle_status());
                        }
                        appealService.updateById(appeal);
                    }
                }
                return ResultInfo.success("操作成功！", logMsg);
            } else {
                return ResultInfo.success("操作失败！");
            }
        } else {
            bean.setUpdate_time(new Date());
            bean.setUpdate_user(sysUserService.getUser().getId());
            if (updateById(bean) > 0) {
                //改变申诉状态
                AppealHandleRecord appealHandleRecord = selectById(bean.getId());
                if (Base.notEmpty(appealHandleRecord)) {
                    if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.getUser().getUser_type())) {
                        OperateAppeal appeal = appealService.selectOne(new QueryWrapper<OperateAppeal>().eq("id", appealHandleRecord.getOperate_appeal_id()));
                        appeal.setAppeal_status(bean.getHandle_status());
                        appealService.updateById(appeal);

                    }
                }
                return ResultInfo.success("操作成功！", logMsg);
            } else {
                return ResultInfo.success("操作失败！");
            }
        }
    }

    /**
     * create by zonglina at 2022/1/26 11:08
     * description:
     * 根据申诉ID查询数据
     *
     * @return : list
     * @param:operate_appeal_id 申诉订单id
     * @param:approve_status 运营人员=审批通过=3
     */
    public AppealHandleRecord selectByOperateAppealId(Integer operate_appeal_id, String approve_status) {
        QueryWrapper<AppealHandleRecord> queryWrapper = new QueryWrapper<AppealHandleRecord>().eq("operate_appeal_id", operate_appeal_id).
                eq("is_del", 0);
        //查询当前单据，待审批和审批通过的
        if (Base.notEmpty(approve_status)) {
//            List<String> statusList = new ArrayList<>();
//            statusList.add(GlobalData.OPERATE_APPEAL_DCL);
//            statusList.add(approve_status);
            queryWrapper.in("approve_status", approve_status);
        }
        AppealHandleRecord appealHandleRecord = selectOne(queryWrapper);
        if (Base.notEmpty(appealHandleRecord)) {
            HashMap<String, SysDict> appeal_status_map = DcCacheDataUtil.getMapDicts("appeal_status");
            HashMap<String, SysDict> car_type_map = DcCacheDataUtil.getMapDicts("car_type");
            if (Base.notEmpty(appealHandleRecord.getHandle_status())) {
                appealHandleRecord.setHandle_status(appeal_status_map.get(appealHandleRecord.getHandle_status()).getLabel());
            }
            if (Base.notEmpty(appealHandleRecord.getApprove_status())) {
                appealHandleRecord.setApprove_status_name(appeal_status_map.get(appealHandleRecord.getApprove_status()).getLabel());
            }
            if (Base.notEmpty(appealHandleRecord.getOrder_status())) {
                appealHandleRecord.setOrder_status(appeal_status_map.get(appealHandleRecord.getOrder_status()).getLabel());
            }
            if (Base.notEmpty(appealHandleRecord.getCarno_type())) {
                appealHandleRecord.setCarno_type(car_type_map.get(appealHandleRecord.getCarno_type()).getLabel());
            }
        }
        return appealHandleRecord;
    }

    /**
     * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 5.修正车牌处理
     */
    public List<ReportVo> getCountByType() {
        return mapper.getCountByType();
    }


}
