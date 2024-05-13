package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ChargeTimeConfigDao;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * ClassName: ChargeTimeConfigService <br/>
 * Description: <br/>
 * date: 2022/1/4 9:27<br/>
 *
 * @author bn<br       />
 */
@Transactional
@Service
public class ChargeTimeConfigService extends BaseService<ChargeTimeConfigDao, ChargeTimeConfig> {
}
