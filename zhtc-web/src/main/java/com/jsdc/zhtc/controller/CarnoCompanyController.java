package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.CarnoCompany;
import com.jsdc.zhtc.service.CarnoCompanyService;
import com.jsdc.zhtc.vo.BatchCarno;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 类 名: 车牌企业映射
 * 描 述: CarnoCompanyController
 * 作 者: lw
 * 创 建：2022/1/5 11:43
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("/carnocompany")
public class CarnoCompanyController {

    @Autowired
    private CarnoCompanyService service;


    /**
     * 描 述： TODO(绑定车牌)
     * 作 者： xj
     * 历 史： (版本) 作者 时间 注释
     *
     * @param carnoCompany
     * @return json
     */
    @RequestMapping(value = "/bindCompanyCar", method = RequestMethod.POST)
    public ResultInfo bindCompanyCar(CarnoCompany carnoCompany) {
        ResultInfo json = service.bindCompanyCar(carnoCompany);
        return json;
    }

    /**
     * 描 述： TODO(批量绑定车牌)
     * 作 者： xj
     * 历 史： (版本) 作者 时间 注释
     *
     * @param batchCarno
     * @return json
     */
    @RequestMapping(value = "/batchBindCars", method = RequestMethod.POST)
    public ResultInfo batchBindCars(@RequestBody BatchCarno batchCarno) {
        ResultInfo json = service.batchBindCars(batchCarno);
        return json;
    }

    /**
     * 描 述： TODO(解除绑定)
     * 作 者： xj
     * 历 史： (版本) 作者 时间 注释
     *
     * @param carnoCompany
     * @return json
     */
    @RequestMapping(value = "/unbindCompanyCar", method = RequestMethod.POST)
    public ResultInfo unbindCompanyCar(CarnoCompany carnoCompany) {
        ResultInfo json = service.unbindCompanyCar(carnoCompany);
        return json;
    }

    /**
     * 描 述： TODO(根据公司ID获取绑定车辆信息)
     * 作 者： xj
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return json
     */
    @RequestMapping(value = "/getCompanyCars", method = RequestMethod.POST)
    public ResultInfo getCompanyCars(Integer companyId, Integer pageIndex, Integer pageSize) {
        ResultInfo json = service.getCompanyCars(companyId, pageIndex, pageSize);
        return json;
    }

    @RequestMapping(value = "/exportCompanyCars", method = RequestMethod.POST)
    public void exportCompanyCars(@RequestBody CarnoCompany carnoCompany, HttpServletResponse response) {
        service.exportCompanyCars(carnoCompany.getCompany_id(), response);
    }

    @RequestMapping(value = "/getCompanyCars2", method = RequestMethod.POST)
    public ResultInfo getCompanyCars2(Integer companyId) {
        ResultInfo json = service.getCompanyCars2(companyId);
        return json;
    }

    @RequestMapping(value = "/getByCompanyCars", method = RequestMethod.POST)
    public ResultInfo getByCompanyCars(String monthly_code, Integer pageIndex, Integer pageSize) {
        ResultInfo json = service.getByCompanyCars(monthly_code, pageIndex, pageSize);
        return json;
    }


}
