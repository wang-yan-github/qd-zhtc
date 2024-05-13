package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.WhiteCarnoParkDao;
import com.jsdc.zhtc.model.WhiteCarnoPark;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @projectName: zhtc
 * @className: WhiteCarnoParkService
 * @author: wp
 * @description:
 * @date: 2022/8/10 8:43
 */
@Service
@Transactional
public class WhiteCarnoParkService extends BaseService<WhiteCarnoParkDao, WhiteCarnoPark> {
}
