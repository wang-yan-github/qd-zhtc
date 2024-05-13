package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.Street;
import com.jsdc.zhtc.service.StreetService;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.StreetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: StreetController <br/>
 * Description: 街道<br/>
 * date: 2021/12/29 10:22<br/>
 *
 * @author bn<br   />
 */
@Controller
@RequestMapping("street")
public class StreetController extends BaseController {

    @Autowired
    private StreetService streetService;


    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param street
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, Street street) {


        PageInfo<StreetVo> page = streetService.toList(pageIndex, pageSize, street);

        return ResultInfo.success(page);
    }

    /**
     * create by wp at 2022/1/6 14:59
     * description: 根据区域id查询所有街道
     *
     * @param areaId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getAll.do")
    @ResponseBody
    public ResultInfo getAll(String areaId) {
        Street street = new Street();
        street.setArea_id(StringUtils.isNotEmpty(areaId) ? Integer.parseInt(areaId) : null);
        return ResultInfo.success(streetService.toList(street));
    }


}
