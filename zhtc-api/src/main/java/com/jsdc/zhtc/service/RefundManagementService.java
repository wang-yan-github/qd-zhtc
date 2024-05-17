package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.RefundManagementDao;
import com.jsdc.zhtc.mapper.RefundManagementMapper;
import com.jsdc.zhtc.model.RefundManagement;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.RefundManagementVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类 名: 退款管理
 * 描 述: RefundManagementService
 * 作 者: lw
 * 创 建：2021/12/30 14:53
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
public class RefundManagementService extends BaseService<RefundManagementDao, RefundManagement> {

    @Autowired
    private RefundManagementMapper mapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 描 述： TODO(分页查询详情)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll2(CommonVo data) {
        SysUser sysUser = sysUserService.getUser();
        if ("1".equals(sysUser.getUser_type())) {
            data.setParking_type("2");
            data.setParkId(sysUser.getPark_id());
        } else {
            String road_park = "1";
            data.setParking_type(road_park);
        }
        // 判断是否分页查询
        if (data.getPageNum() != null) {
            PageHelper.startPage(data.getPageNum(), data.getPageSize());
            List<RefundManagementVo> list = mapper.selectAll(data);
            list.stream().forEach(item -> {
                item.setTkTypeName();
            });
            PageInfo<RefundManagementVo> listPage = new PageInfo<>(list);
            return ResultInfo.success(listPage);
        } else {
            List<RefundManagementVo> list = mapper.selectAll(data);
            list.stream().forEach(item -> {
                item.setTkTypeName();
            });
            return ResultInfo.success(list);
        }
    }

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll(PageVo<RefundManagement> data) {

        RefundManagement bean = data.getBean();

        QueryWrapper<RefundManagement> wrapper = new QueryWrapper<>();

        if (bean != null) {

            if (bean.getIs_del() != null)
                wrapper.eq("is_del", bean.getIs_del());
        }
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageSize() != null) {
            Page<RefundManagement> page = new Page<>(data.getPageNum(), data.getPageSize());
            IPage<RefundManagement> pageData = selectPage(page, wrapper);

            if (pageData.getRecords().isEmpty())
                pageData.setRecords(new ArrayList<RefundManagement>());

            return ResultInfo.success(pageData);
        } else {
            List<RefundManagement> list = this.selectList(wrapper);
            return ResultInfo.success(list);
        }
    }


    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo selectById(RefundManagement bean) {
        RefundManagement data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error("未查询到数据");
        }
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo saveData(RefundManagement bean) {

        if (this.insert(bean) > 0) {
            return ResultInfo.success("数据添加成功");
        } else {
            return ResultInfo.success("message", "数据添加失败");
        }
    }

    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo updateInfo(RefundManagement bean) {

        if (this.updateById(bean) > 0)
            return ResultInfo.success("操作成功");
        else
            return ResultInfo.error("操作成功");

    }

    /**
     * 描 述： TODO(根据id删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo deleById(RefundManagement bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success("删除成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    //根据id查询数据
    public List<RefundManagement> selectById(Integer payment_id, Integer parking_order_id) {
        List<RefundManagement> refunds = selectList(new QueryWrapper<RefundManagement>().eq("payment_id", payment_id).eq("parking_order_id", parking_order_id));
        HashMap<String, SysDict> pay_type = DcCacheDataUtil.getMapDicts("pay_type");
        for (RefundManagement b : refunds) {
            b.setRefund_channel(pay_type.get(b.getRefund_channel()).getLabel());
        }
        return refunds;
    }


    //退款记录导出
    public void exportRefund(CommonVo bean, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        SysUser sysUser = sysUserService.getUser();
        if ("1".equals(sysUser.getUser_type())) {
            bean.setParking_type("2");
            bean.setParkId(sysUser.getPark_id());
        } else {
            String road_park = "1";
            bean.setParking_type(road_park);
        }
        List<RefundManagementVo> list = mapper.selectAll(bean);
        list.stream().forEach(item -> {
            item.setTkTypeName();
        });
        writer.addHeaderAlias("payment_serialno", "流水号");
        writer.addHeaderAlias("orderNo", "订单号");
        writer.addHeaderAlias("carNo", "车牌号");
        writer.addHeaderAlias("phone", "用户手机号");
        writer.addHeaderAlias("refund_amount", "金额");
        writer.addHeaderAlias("tkTypeName", "退款渠道");
        writer.addHeaderAlias("refund_time", "退款时间");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("退款记录.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付流水明细关联退款记录
     *
     * @author thr
     */
    public List<RefundManagementVo> getRefundList(RefundManagementVo vo) {
        List<RefundManagementVo> list = mapper.getRefundList(vo);
        return list;
    }
}
