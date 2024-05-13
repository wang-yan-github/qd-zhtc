package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ChargeIntervalConfigDao;
import com.jsdc.zhtc.model.ChargeIntervalConfig;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * ClassName: ChargeIntervalConfigService <br/>
 * Description: <br/>
 * date: 2022/1/4 9:24<br/>
 *
 * @author bn<br       />
 */
@Transactional
@Service
public class ChargeIntervalConfigService extends BaseService<ChargeIntervalConfigDao, ChargeIntervalConfig> {
}
