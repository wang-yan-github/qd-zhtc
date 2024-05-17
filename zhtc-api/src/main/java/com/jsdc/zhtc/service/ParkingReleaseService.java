package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.ParkingReleaseDao;
import com.jsdc.zhtc.mapper.ParkingReleaseMapper;
import com.jsdc.zhtc.mapper.ParkingReleasePlaceMapper;
import com.jsdc.zhtc.model.ParkingRelease;
import com.jsdc.zhtc.model.ParkingReleasePlace;
import com.jsdc.zhtc.vo.ParkingReleaseVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * ClassName: ParkingReleaseService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class ParkingReleaseService extends BaseService<ParkingReleaseDao, ParkingRelease> {


    @Autowired
    private ParkingReleasePlaceService placeService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ParkingReleaseMapper releaseMapper;


    /**
     * 分页查询
     *
     * @param bean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<ParkingReleaseVo> selectPageList(Integer pageIndex, Integer pageSize, ParkingReleaseVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ParkingReleaseVo> list = releaseMapper.selectPageList(bean, "1");
//        if (GlobalData.PARKING_TYPE_ROAD.equals(sysUserService.redisRoadOrPark())) {
//            // 引用当前收费上线路段
//            list.forEach(x -> {
//                x.setPlaces(placeService.selectByPReleaseId(x.getId(), sysUserService.redisRoadOrPark()));
//            });
//        } else {
            // 引用当前收费上线路段
            list.forEach(x -> {
                x.setPlaces(placeService.selectByPReleaseId(x.getId(), "1"));
            });
//        }
        return list;
    }

    @Autowired
    private ParkingReleasePlaceMapper parkingReleasePlaceMapper;

    /**
     * 删除功能
     *
     * @param bean
     * @param status
     * @return
     */
    public Integer deleteByRelease(ParkingRelease bean, Integer status) {
        if (null != status) {
            if (status == 1) {//启用
                bean.setStatus(2);
            } else {//禁用
                bean.setStatus(1);
            }
        } else {
            bean.setIs_del(1);
        }
        parkingReleasePlaceMapper.update(null, Wrappers.<ParkingReleasePlace>lambdaUpdate()
                .set(ParkingReleasePlace::getIs_del, 1)
                .eq(ParkingReleasePlace::getParking_release_id, bean.getId())
        );
        return releaseMapper.updateById(bean);
    }


    /**
     * create by zonglina at 2021/12/31 9:07
     * description:
     * 设计两张表:
     * 1、一张是上线收费配置表(parking_release)、
     * 2、一张是上线收费停车区域配置(release_parking_place)
     *
     * @return : null
     * @param:null
     */
    public ResultInfo save(ParkingRelease bean) {
        if (Base.empty(bean.getStart_time())) {
            return ResultInfo.error("请选择时间！");
        } else if (Base.empty(bean.getParkingplace_id())) {
            return ResultInfo.error("请勾选数据！");
        } else {
            //修改
            if (Base.notEmpty(bean.getId())) {
                //上线收费配置
                bean.setUpdate_time(new Date());
                bean.setUpdate_user(sysUserService.getUser().getId());
                bean.setParking_type("1");
                if (updateById(bean) > 0) {
                    //保存上线收费停车区域配置
                    placeService.save(bean);
                    return ResultInfo.success("操作成功！");
                } else {
                    return ResultInfo.error("操作失败！");
                }
            } else {
                //上线收费配置 新增
                bean.setIs_del(0);
                bean.setStatus(1);
                bean.setCreate_time(new Date());
                bean.setCreate_user(sysUserService.getUser().getId());
                bean.setParking_type("1");

                if (insert(bean) > 0) {
                    //保存上线收费停车区域配置
                    placeService.save(bean);
                    return ResultInfo.success("操作成功！");
                } else {
                    return ResultInfo.error("操作失败！");
                }
            }
        }
    }


    //上线收费配置
    public Integer selectByParmCount(String parking_type, Integer parkingplace_id, String start_time) {
        return releaseMapper.selectByParmCount(parking_type, parkingplace_id, start_time);
    }
}
