package com.jsdc.zhtc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.InvoicesProvideRecordDao;
import com.jsdc.zhtc.mapper.InvoicesProvideRecordMapper;
import com.jsdc.zhtc.model.InvoicesProvideRecord;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.RedisUtils;
import com.jsdc.zhtc.vo.InvoicesProvideRecordVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ClassName: InvoicesProvideRecordService
 * Description:
 * date: 2022/1/17 11:47
 *
 * @author bn
 */
@Service
@Transactional
public class InvoicesProvideRecordService extends BaseService<InvoicesProvideRecordDao, InvoicesProvideRecord> {

    @Autowired
    private InvoicesProvideRecordMapper recordMapper;

    @Autowired
    private SysUserService sysUserService;


    public PageInfo<InvoicesProvideRecordVo> toList(Integer pageIndex, Integer pageSize, InvoicesProvideRecordVo record) {
        PageHelper.startPage(pageIndex, pageSize);

        SysUser sysUser = sysUserService.getUser();

        String key = sysUser.getLogin_name() + "_sys";

        // 停车场
        if (RedisUtils.getBeanValue(key).equals("1")) {
            record.setParking_type("1");
        } else {
            record.setParking_type("0");
        }


        List<InvoicesProvideRecordVo> recordVos = recordMapper.toList(record);

        PageInfo<InvoicesProvideRecordVo> page = new PageInfo<>(recordVos);

        return page;
    }


    /**
     * 派放发票 已排放发票记录
     *
     * @param pageIndex
     * @param pageSize
     * @param parking_type
     * @return
     */
    public ResultInfo toList(Integer pageIndex, Integer pageSize, String parking_type, String carno) {
        PageHelper.startPage(pageIndex, pageSize);
        InvoicesProvideRecordVo record = new InvoicesProvideRecordVo();
        record.setParking_type(parking_type);
        record.setCar_no(carno);
        List<InvoicesProvideRecordVo> recordVos = recordMapper.toList(record);
        return ResultInfo.success(new PageInfo<>(recordVos));
    }

}
