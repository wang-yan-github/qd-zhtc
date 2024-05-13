package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.RefundManagementDao;
import com.jsdc.zhtc.model.RefundManagement;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.RefundManagementVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 类 名: 退款管理
 * 描 述: RefundManagementMapper
 * 作 者: lw
 * 创 建：2021/12/30 14:49
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Mapper
public interface RefundManagementMapper extends BaseMapper<RefundManagement> {

    /**
     * 描 述： TODO(查询退款记录)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link List<RefundManagementVo>}
     */
    @SelectProvider(method = "selectAll", type = RefundManagementDao.class)
    List<RefundManagementVo> selectAll(CommonVo data);


    /**
     * 支付流水明细关联退款记录
     *
     * @author thr
     */
    @SelectProvider(method = "getRefundList", type = RefundManagementDao.class)
    List<RefundManagementVo> getRefundList(RefundManagementVo vo);
}
