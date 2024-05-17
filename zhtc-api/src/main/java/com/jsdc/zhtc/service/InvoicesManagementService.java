package com.jsdc.zhtc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.InvoicesManagementDao;
import com.jsdc.zhtc.mapper.InvoicesManagementMapper;
import com.jsdc.zhtc.model.InvoicesManagement;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class InvoicesManagementService extends BaseService<InvoicesManagementDao, InvoicesManagement> {
    @Autowired
    InvoicesManagementMapper invoicesManagementMapper;
    @Autowired
    private ParkingOrderService parkingOrderService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param
     * @return
     */
    public PageInfo listInvoicesManagement(Integer pageIndex, Integer pageSize, String phone, String invoice_mode) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InvoicesManagement> list = invoicesManagementMapper.listInvoicesManagement(phone, invoice_mode, "1");
        //发票类型
        HashMap<String, SysDict> invoiceTypeMap = DcCacheDataUtil.getMapDicts("invoice_type");
        //开票状态
        HashMap<String, SysDict> invoiceModeMap = DcCacheDataUtil.getMapDicts("invoice_mode");
        list.forEach(x -> {
            x.setInvoice_type(invoiceTypeMap.get(x.getInvoice_type()).getLabel());
            x.setInvoice_mode(invoiceModeMap.get(x.getInvoice_mode()).getLabel());
        });
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 微信小程序
     * 分页查询
     *
     * @author thr
     */
    public PageInfo getPageList(Integer pageIndex, Integer pageSize, InvoicesManagement bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InvoicesManagement> list = invoicesManagementMapper.getPageList(bean);
        //发票类型
        HashMap<String, SysDict> invoiceTypeMap = DcCacheDataUtil.getMapDicts("invoice_type");
        //开票状态
        HashMap<String, SysDict> invoiceModeMap = DcCacheDataUtil.getMapDicts("invoice_mode");
        list.forEach(x -> {
            x.setInvoice_type(invoiceTypeMap.get(x.getInvoice_type()).getLabel());
            x.setInvoice_mode(invoiceModeMap.get(x.getInvoice_mode()).getLabel());
        });
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }


    /**
     * 微信小程序
     * 发票详情
     *
     * @author thr
     */
    public ResultInfo onView(InvoicesManagement bean) {
        bean = invoicesManagementMapper.selectById(bean.getId());
        //发票类型
        HashMap<String, SysDict> invoiceTypeMap = DcCacheDataUtil.getMapDicts("invoice_type");
        //开票状态
        HashMap<String, SysDict> invoiceModeMap = DcCacheDataUtil.getMapDicts("invoice_mode");
        bean.setInvoice_type(invoiceTypeMap.get(bean.getInvoice_type()).getLabel());
        bean.setInvoice_mode(invoiceModeMap.get(bean.getInvoice_mode()).getLabel());
        return ResultInfo.success(bean);
    }

    public ResultInfo invoiceDetailsById(String invoicesId, String parkingType) {
        List<Map<String, String>> list = invoicesManagementMapper.invoiceDetailsById(invoicesId, parkingType);
        list.forEach(x -> {
            x.put("paid_amount", new BigDecimal(x.get("paid_amount")).setScale(2).toString());
        });
        return ResultInfo.success(list);
    }
}

