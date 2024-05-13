package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.InspectManageDao;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.vo.InspectManageParkVo;
import com.jsdc.zhtc.vo.InspectManageVo;
import com.jsdc.zhtc.vo.ParkingOrderResVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface InspectManageMapper extends BaseMapper<InspectManage> {

    @SelectProvider(type = InspectManageDao.class, method = "selectPageList")
    List<InspectManageVo> selectPageList(InspectManageVo bean, String redisRoadOrPark);

    @SelectProvider(type = InspectManageDao.class, method = "getPageForStatistics")
    List<InspectManageVo> getPageForStatistics(Integer areaId, Integer streetId, Integer roadId, String name, String phone);

    @SelectProvider(type = InspectManageDao.class, method = "getPageForParkStatistics")
    List<InspectManageVo> getPageForParkStatistics(Integer areaId, Integer streetId, Integer parkId, String name, String phone);

    @SelectProvider(type = InspectManageDao.class, method = "getRoadCount")
    List<Integer> getRoadCount(Integer inspectId);

    // 根据路段、巡检员统计停车位总数group
    @SelectProvider(type = InspectManageDao.class, method = "selectByGroupBerthnum")
    Integer selectByGroupBerthnum(String parkType, Integer inspect_id);

    @SelectProvider(type = InspectManageDao.class, method = "getParkingOrders")
    List<ParkingOrderResVo> getParkingOrders(String status, String startTime, String endTime, String carno, Integer inspectId);

    @SelectProvider(method = "manageParkDetail", type = InspectManageDao.class)
    InspectManageParkVo manageParkDetail(Integer id);
}
