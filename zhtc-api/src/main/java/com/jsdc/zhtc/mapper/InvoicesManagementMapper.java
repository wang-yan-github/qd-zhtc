package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.InvoicesManagementDao;
import com.jsdc.zhtc.model.InvoicesManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Mapper
public interface InvoicesManagementMapper extends BaseMapper<InvoicesManagement> {
    @SelectProvider(type = InvoicesManagementDao.class, method = "listInvoicesManagement")
    List<InvoicesManagement> listInvoicesManagement(String phone, String invoice_mode, String parking_type);

    @SelectProvider(type = InvoicesManagementDao.class, method = "getPageList")
    List<InvoicesManagement> getPageList(InvoicesManagement bean);

    @SelectProvider(type = InvoicesManagementDao.class, method = "invoicesDetailsById")
    List<Map<String, String>> invoiceDetailsById(String invoicesId, String parkingType);
}
