package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.TimeLimitDao;
import com.jsdc.zhtc.model.TimeLimit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TimeLimitService extends BaseService<TimeLimitDao, TimeLimit> {

}
