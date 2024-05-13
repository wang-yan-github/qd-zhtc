package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkDao;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ParkVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ParkMapper extends BaseMapper<Park> {
    @SelectProvider(method = "toList", type = ParkDao.class)
    List<ParkVo> toList(Park park);

    /**
     * 描 述： TODO(查询获取停车场信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    @SelectProvider(method = "getSumParkNum", type = ParkDao.class)
    String getSumParkNum(CommonVo data);


    //计算总泊位号
    @SelectProvider(method = "berthSumData", type = ParkDao.class)
    String berthSumData(String type);
}
