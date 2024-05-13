package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.OperateCompany;
import com.jsdc.zhtc.service.OperateCompanyService;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 类 名: 单位管理
 * 描 述: OperateCompanyController
 * 作 者: lw
 * 创 建：2022/1/4 11:25
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("/operatecompany")
public class OperateCompanyController {

    @Autowired
    private OperateCompanyService service;


    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @PostMapping(value = "/getall")
    public ResultInfo getAll(@RequestBody PageVo<OperateCompany> data) {
        ResultInfo json = service.selectAll(data);
        return json;
    }


    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param id
     * @return json
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public ResultInfo getDetail(Integer id) {
        return ResultInfo.success(service.selectById(id));
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/save")
    public ResultInfo save(OperateCompany bean) {
        ResultInfo json = service.saveData(bean);
        return json;
    }

    /**
     * 描 述： TODO(更新数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/update")
    public ResultInfo update(OperateCompany bean) {
        ResultInfo json = service.updateInfo(bean);
        return json;
    }

    /**
     * 描 述： TODO(删除数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultInfo delete(OperateCompany bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }

    /**
     * 描 述： TODO(删除公司和绑定车辆信息)
     * 作 者： xj
     * 历 史： (版本) 作者 时间 注释
     *
     * @param id
     * @return json
     */
    @RequestMapping(value = "/delCompany", method = RequestMethod.POST)
    public ResultInfo delCompany(Integer id) {
        return service.delCompany(id);
    }

    /**
     * 公司管理导出
     *
     * @return
     */
    @RequestMapping(value = "/exportCompany.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportCompany(OperateCompany vo, HttpServletResponse response) {
        service.exportCompany(vo, response);
    }
}
