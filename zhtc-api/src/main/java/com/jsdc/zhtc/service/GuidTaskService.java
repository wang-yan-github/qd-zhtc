package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.GuidTaskDao;
import com.jsdc.zhtc.model.GuidTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @projectName: zhtc
 * @className: GuidTaskService
 * @author: wp
 * @description:
 * @date: 2022/12/8 10:32
 */
@Service
@Transactional
public class GuidTaskService extends BaseService<GuidTaskDao, GuidTask> {
}
