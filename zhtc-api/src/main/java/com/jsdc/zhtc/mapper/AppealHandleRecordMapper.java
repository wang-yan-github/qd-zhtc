package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.AppealHandleRecordDao;
import com.jsdc.zhtc.model.AppealHandleRecord;
import com.jsdc.zhtc.vo.ReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


@Mapper
public interface AppealHandleRecordMapper extends BaseMapper<AppealHandleRecord> {

    /**
     * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 5.修正车牌处理
     */
    @SelectProvider(method = "getCountByType", type = AppealHandleRecordDao.class)
    List<ReportVo> getCountByType();

}
