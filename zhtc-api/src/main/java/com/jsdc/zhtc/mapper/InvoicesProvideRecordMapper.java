package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.InvoicesProvideRecordDao;
import com.jsdc.zhtc.model.InvoicesProvideRecord;
import com.jsdc.zhtc.vo.InvoicesProvideRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface InvoicesProvideRecordMapper extends BaseMapper<InvoicesProvideRecord> {
    @SelectProvider(method = "toList", type = InvoicesProvideRecordDao.class)
    List<InvoicesProvideRecordVo> toList(InvoicesProvideRecordVo record);
}
