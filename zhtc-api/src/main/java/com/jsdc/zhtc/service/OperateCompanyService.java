package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.OperateCompanyDao;
import com.jsdc.zhtc.model.CarnoCompany;
import com.jsdc.zhtc.model.OperateCompany;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 类 名: 单位管理
 * 描 述: OperateCompanyService
 * 作 者: lw
 * 创 建：2022/1/4 11:24
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
public class OperateCompanyService extends BaseService<OperateCompanyDao, OperateCompany> {

    @Autowired
    private CarnoCompanyService carnoCompanyService;

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     */
    public ResultInfo selectAll(PageVo<OperateCompany> data) {

        OperateCompany bean = data.getBean();

        QueryWrapper<OperateCompany> wrapper = new QueryWrapper<>();

        if (bean != null) {
            if (StringUtils.isNotBlank(bean.getCompany_name()))
                wrapper.like("company_name", bean.getCompany_name());
        }
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageNum() != null) {

            PageHelper.startPage(data.getPageNum(), data.getPageSize());

            List<OperateCompany> monthlyManagements = this.selectList(wrapper);
            PageInfo<OperateCompany> page = new PageInfo<>(monthlyManagements);

            return ResultInfo.success(page);
        } else {
            List<OperateCompany> list = this.selectList(wrapper);
            return ResultInfo.success(list);
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
    public ResultInfo saveData(OperateCompany bean) {
        bean.setIs_del("0");
        bean.setCreate_time(new Date());
        if (this.insert(bean) > 0) {
            return ResultInfo.success();
        } else {
            return ResultInfo.error("message", "数据添加失败");
        }
    }

    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     */
    public ResultInfo updateInfo(OperateCompany bean) {

        if (this.updateById(bean) > 0)
            return ResultInfo.success();
        else
            return ResultInfo.error("修改失败！");

    }

    /**
     * 描 述： TODO(根据id删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     */
    public ResultInfo deleById(OperateCompany bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success();
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 删除公司信息和绑定车辆信息
     *
     * @param id
     * @return
     */
    public ResultInfo delCompany(Integer id) {
        OperateCompany oc = this.selectById(id);
        if (id == null || oc == null) {
            return ResultInfo.error("无公司信息！");
        }
        //删除公司
        oc.setIs_del(GlobalData.ISDEL_YES);
        if (oc.updateById()) {
            //删除公司绑定车辆信息
            carnoCompanyService.delete(new QueryWrapper<CarnoCompany>().eq("company_id", id));
        }
        ;
        return ResultInfo.success();
    }


    //公司管理导出
    public void exportCompany(OperateCompany bean, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        QueryWrapper<OperateCompany> wrapper = new QueryWrapper<>();
        if (bean != null) {
            if (StringUtils.isNotBlank(bean.getCompany_name()))
                wrapper.like("company_name", bean.getCompany_name());
        }
        wrapper.eq("is_del", GlobalData.ISDEL_NO);
        List<OperateCompany> list = this.selectList(wrapper);
        writer.addHeaderAlias("company_name", "企业名称");
        writer.addHeaderAlias("address", "企业地址");
        writer.addHeaderAlias("liaison", "联系人");
        writer.addHeaderAlias("phone", "联系方式");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("公司管理.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
