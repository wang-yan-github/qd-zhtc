package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.CarnoAppealDao;
import com.jsdc.zhtc.model.CarnoAppeal;
import com.jsdc.zhtc.vo.CarnoAppealVo;
import com.jsdc.zhtc.vo.ReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Mapper
public interface CarnoAppealMapper extends BaseMapper<CarnoAppeal> {

    @SelectProvider(type = CarnoAppealDao.class, method = "listCarnoAppeal")
    List<Map<String, String>> listCarnoAppeal(CarnoAppealVo vo);

    /**
     * 微信小程序
     * 车牌申诉分页查询
     *
     * @author thr
     */
    @SelectProvider(type = CarnoAppealDao.class, method = "getPageList")
    List<CarnoAppeal> getPageList(CarnoAppeal bean);

    /**
     * 近七日服务类型趋势 车牌申诉 最近7日/数量
     */
    @SelectProvider(type = CarnoAppealDao.class, method = "getDaysCount")
    List<ReportVo> getDaysCount();

}
