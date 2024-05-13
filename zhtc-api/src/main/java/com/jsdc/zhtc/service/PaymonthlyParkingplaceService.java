package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.PaymonthlyParkingplaceDao;
import com.jsdc.zhtc.mapper.PaymonthlyParkingplaceMapper;
import com.jsdc.zhtc.model.PaymonthlyParkingplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: PaymonthlyParkingplaceService
 * Description:
 * date: 2021/12/30 10:26
 *
 * @author wp
 */
@Service
@Transactional
public class PaymonthlyParkingplaceService extends BaseService<PaymonthlyParkingplaceDao, PaymonthlyParkingplace> {

    @Autowired
    private PaymonthlyParkingplaceMapper mapper;

    /**
     * create by wp at 2021/12/31 10:37
     * description: 新增包月配置与包月配置停车区的关联关系
     *
     * @param payConfigId
     * @param ids
     * @return void
     */
    public void addMap(int payConfigId, String ids) {
        List<String> idList = Arrays.asList(StringUtils.split(ids, GlobalData.SPLIT_COMMA));
        for (String id : idList) {
            PaymonthlyParkingplace pp = new PaymonthlyParkingplace();
            pp.setParkingplace_id(Integer.parseInt(id));
            pp.setPaymonthly_config_id(payConfigId);
            pp.setIs_del(GlobalData.ISDEL_NO);
            insert(pp);
        }
    }

    /**
     * create by wp at 2021/12/31 10:37
     * description: 修改包月配置与包月配置停车区的关联关系
     *
     * @param payConfigId
     * @param ids
     * @return void
     */
    public void editMap(int payConfigId, String ids) {
        UpdateWrapper<PaymonthlyParkingplace> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(PaymonthlyParkingplace::getIs_del, GlobalData.ISDEL_YES)
                .eq(PaymonthlyParkingplace::getPaymonthly_config_id, payConfigId);
        update(null, wrapper);
        addMap(payConfigId, ids);
    }


}
