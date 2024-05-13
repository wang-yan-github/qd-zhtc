package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.AppealHandleVoucherDao;
import com.jsdc.zhtc.model.AppealHandleVoucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface AppealHandleVoucherMapper extends BaseMapper<AppealHandleVoucher> {


    @SelectProvider(type = AppealHandleVoucherDao.class, method = "selectByPid")
    List<String> selectByPid(Integer handle_record_id);

}
