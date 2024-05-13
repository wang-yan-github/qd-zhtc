package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.CarnoAppealDao;
import com.jsdc.zhtc.mapper.CarnoAppealMapper;
import com.jsdc.zhtc.model.CarnoAppeal;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.CarnoAppealVo;
import com.jsdc.zhtc.vo.ReportVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Service
@Transactional
public class CarnoAppealService extends BaseService<CarnoAppealDao, CarnoAppeal> {

    @Autowired
    private CarnoAppealMapper carnoAppealMapper;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperateCarnoService operateCarnoService;


    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param vo
     * @return
     */
    public ResultInfo listCarnoAppeal(Integer pageIndex, Integer pageSize, CarnoAppealVo vo) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, String>> list = carnoAppealMapper.listCarnoAppeal(vo);
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 申诉处理
     *
     * @param vo
     * @return
     */
    public ResultInfo saveComplaint(CarnoAppealVo vo) {
        CarnoAppeal carnoAppeal = selectById(vo.getId());
        SysUser sysUser = sysUserService.getUser();
        //同意 车牌管理 先解绑 后绑定
        if (GlobalData.CARNO_UNBIND.equals(vo.getStatus())) {
            // 解绑 绑定
            operateCarnoService.relieveBind(carnoAppeal.getCarno_id());
            operateCarnoService.bindCarPerson(carnoAppeal.getCarno_id(), carnoAppeal.getMember_id());
        }

        //消息
        String logMsg = "";
        if (GlobalData.APPEAL_STATUS_YES.equals(vo.getStatus())) {
            logMsg = "车牌号为" + operateCarnoService.selectById(carnoAppeal.getCarno_id()).getCar_no() + ",车牌申诉成功";
        } else if (GlobalData.APPEAL_STATUS_NO.equals(vo.getStatus())) {
            logMsg = "车牌号为" + operateCarnoService.selectById(carnoAppeal.getCarno_id()).getCar_no() + ",车牌申诉失败";
        }

        carnoAppeal.setUpdate_user(sysUser.getId());
        carnoAppeal.setUpdate_time(new Date());
        carnoAppeal.setVerifier(sysUser.getId());
        carnoAppeal.setStatus(vo.getStatus());
        carnoAppeal.setReject_reason(vo.getRejectReason());
        carnoAppeal.updateById();
        return ResultInfo.success("车牌申诉", logMsg);
    }


    public Integer census() {
        QueryWrapper<CarnoAppeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("status");
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper).size();

    }

    /**
     * 近七日服务类型趋势 车牌申诉 最近7日/数量
     */
    public List<ReportVo> getDaysCount() {
        return carnoAppealMapper.getDaysCount();
    }
}

