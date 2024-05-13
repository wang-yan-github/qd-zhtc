package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.AppealHandleVoucherDao;
import com.jsdc.zhtc.mapper.AppealHandleVoucherMapper;
import com.jsdc.zhtc.model.AppealHandleVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ClassName: AppealHandleVoucherService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class AppealHandleVoucherService extends BaseService<AppealHandleVoucherDao, AppealHandleVoucher> {


    @Autowired
    private AppealHandleVoucherMapper voucherMapper;

    //保存功能
    public void save(String fileIds, Integer recordId) {
        if (null != fileIds && !"".equals(fileIds)) {
            String[] ids = fileIds.split(",");
            for (String id : ids) {
                AppealHandleVoucher voucher = new AppealHandleVoucher();
                voucher.setPicture_id(Integer.valueOf(id));
                voucher.setHandle_record_id(recordId);
                voucher.setIs_del(0);
                insert(voucher);
            }
        }
    }


    /**
     * create by zonglina at 2022/1/13 16:27
     * description:
     * //根据id获取所有图片
     *
     * @return : null
     * @param:null
     */
    public List<String> selectByPid(Integer handle_record_id) {
        return voucherMapper.selectByPid(handle_record_id);
    }


}
