package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.InvoiceUseManagementDao;
import com.jsdc.zhtc.model.InvoiceUseManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: InvoiceUseManagementService
 * Description: 巡检发票使用记录
 * date: 2022/1/13 13:39
 *
 * @author wp
 */
@Service
@Transactional
public class InvoiceUseManagementService extends BaseService<InvoiceUseManagementDao, InvoiceUseManagement> {


}
