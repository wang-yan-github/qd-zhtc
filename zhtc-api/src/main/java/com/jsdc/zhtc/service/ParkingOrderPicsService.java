package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ParkingOrderPicsDao;
import com.jsdc.zhtc.mapper.ParkingOrderPicsMapper;
import com.jsdc.zhtc.model.ParkingOrderPics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: ParkingOrderPicsService
 * Description:
 * date: 2022/1/24 14:41
 *
 * @author wp
 */
@Service
@Transactional
public class ParkingOrderPicsService extends BaseService<ParkingOrderPicsDao, ParkingOrderPics> {

    @Autowired
    private ParkingOrderPicsMapper parkingOrderPicsMapper;
    @Value("${jsdc.parkImageUrl}")
    private String parkImageUrl;

    /**
     * create by zonglina at 2022/1/13 16:27
     * description:
     * //根据id获取所有图片
     *
     * @return : null
     * @param:null
     */
    public List<String> selectByPid(Integer parking_order_id, String picture_type) {
        List<String> list = parkingOrderPicsMapper.selectByPid(parking_order_id, picture_type);
        return list;
    }
}
