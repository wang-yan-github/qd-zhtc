package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.Area;
import com.jsdc.zhtc.service.AreaService;
import com.jsdc.zhtc.vo.AreaVo;
import com.jsdc.zhtc.vo.ResultInfo;
import com.jsdc.zhtc.vo.TreeStructureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: AreaController <br/>
 * Description: 区域<br/>
 * date: 2021/12/29 10:20<br/>
 *
 * @author bn<br   />
 */
@Controller
@RequestMapping("area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;


    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param area
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, Area area) {


        PageInfo<AreaVo> areaList = areaService.toList(pageIndex, pageSize, area);

        return ResultInfo.success(areaList);
    }


    /**
     * create by wp at 2022/1/6 14:35
     * description: 获取全部区域
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping("getAll.do")
    @ResponseBody
    public ResultInfo getAll() {
        List<Area> areas = areaService.toList(new Area());
        return ResultInfo.success(areas);
    }


    /**
     * 描 述： TODO(获取区 街道 停车场树形结构)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link List< TreeStructureVo>}
     */
    @PostMapping("/getParkingTreeStructure")
    @ResponseBody
    public ResultInfo getParkingTreeStructure() {
        List<TreeStructureVo> list = areaService.getParkingTreeStructure();
        return ResultInfo.success(list);
    }


}
