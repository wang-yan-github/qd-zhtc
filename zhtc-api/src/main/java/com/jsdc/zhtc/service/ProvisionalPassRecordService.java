package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ProvisionalPassRecordDao;
import com.jsdc.zhtc.model.ProvisionalPassRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProvisionalPassRecordService extends BaseService<ProvisionalPassRecordDao, ProvisionalPassRecord> {


}
