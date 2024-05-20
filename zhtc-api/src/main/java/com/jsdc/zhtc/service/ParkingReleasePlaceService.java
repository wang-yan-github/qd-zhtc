package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.ParkingReleasePlaceDao;
import com.jsdc.zhtc.mapper.ParkingReleasePlaceMapper;
import com.jsdc.zhtc.model.ParkingRelease;
import com.jsdc.zhtc.model.ParkingReleasePlace;
import com.jsdc.zhtc.vo.ParkingReleasePlaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ParkingReleasePlaceService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class ParkingReleasePlaceService extends BaseService<ParkingReleasePlaceDao, ParkingReleasePlace> {

    @Autowired
    private ParkingReleasePlaceMapper mapper;
    @Autowired
    private SysUserService sysUserService;

    //上线收费停车区域配置新增功能
    public void save(ParkingRelease bean) {
        if (null != bean.getId()) {
            //查询是否已经有此parking_release_id数据,如有先删除，在新增数据
            Long count = mapper.selectCount(new QueryWrapper<ParkingReleasePlace>().eq("parking_release_id", bean.getId()));
            if (count > 0) {
                ParkingReleasePlace place = new ParkingReleasePlace();
                place.setParking_release_id(bean.getId());
                place.setIs_del(1);
                mapper.update(place, new QueryWrapper<ParkingReleasePlace>().eq("parking_release_id", bean.getId()));
            }
        }
        String[] parkings = bean.getParkingplace_id().split(",");
        for (String p : parkings) {
            ParkingReleasePlace place = new ParkingReleasePlace();
            place.setParking_release_id(bean.getId());
            place.setParkingplace_id(Integer.valueOf(p));
            place.setParking_type(bean.getParking_type());
            place.setIs_del(0);
            insert(place);
        }
    }

    public List<Integer> selectByParkingReleaseId(Integer parking_release_id) {
        LambdaQueryWrapper<ParkingReleasePlace> wrapper = new LambdaQueryWrapper();
        if (null != parking_release_id) {
            wrapper.eq(ParkingReleasePlace::getParking_release_id, parking_release_id);
        }
        wrapper.eq(ParkingReleasePlace::getIs_del, GlobalData.ISDEL_NO);
        wrapper.eq(ParkingReleasePlace::getParking_type, "1");
        List<ParkingReleasePlace> list = selectList(wrapper);
        return list.stream().map(x -> x.getParkingplace_id()).collect(Collectors.toList());
    }
    /**
     * create by zonglina at 2022/1/12 13:29
     * description:根据收费id查询信息
     *
     * @return : null
     * @param:null
     */
    public List<ParkingReleasePlaceVo> selectByPReleaseId(Integer parking_release_id, String redisRoadOrPark) {
        return mapper.selectByPReleaseId(parking_release_id, redisRoadOrPark);
    }
}
