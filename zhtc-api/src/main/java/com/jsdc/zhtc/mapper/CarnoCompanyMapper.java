package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.CarnoCompanyDao;
import com.jsdc.zhtc.model.CarnoCompany;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 类 名: 车牌企业映射
 * 描 述: CarnoCompanyMapper
 * 作 者: lw
 * 创 建：2022/1/4 14:08
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Mapper
public interface CarnoCompanyMapper extends BaseMapper<CarnoCompany> {
    @SelectProvider(type = CarnoCompanyDao.class, method = "getCompanyCars")
    List<OperateCarnoVo> getCompanyCars(Integer companyId);

    @SelectProvider(type = CarnoCompanyDao.class, method = "getByCompanyCars")
    List<OperateCarnoVo> getByCompanyCars(String monthly_code);

}
