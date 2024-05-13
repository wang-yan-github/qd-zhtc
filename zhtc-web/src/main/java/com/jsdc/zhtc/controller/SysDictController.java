package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.service.SysDictService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: SysDictController <br/>
 * Description: 字典<br/>
 * date: 2022/1/6 16:48<br/>
 *
 * @author bn<br   />
 */
@RestController
@RequestMapping("dict")
public class SysDictController extends BaseController {

    @Autowired
    private SysDictService sysDictService;


    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param sysDict
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, SysDict sysDict) {


        PageInfo<SysDict> page = sysDictService.toList(pageIndex, pageSize, sysDict);

        return ResultInfo.success(page);
    }

    /**
     * 获取字典信息
     *
     * @param sysDict
     * @return
     */
    @RequestMapping("toList.do")
    public ResultInfo toList(SysDict sysDict) {

        return ResultInfo.success(sysDictService.toList(sysDict));
    }


    /**
     * 编辑字典信息
     *
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(SysDict sysDict) {

        return sysDictService.toEdit(sysDict);
    }

    /**
     * 添加字典信息
     *
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(SysDict sysDict) {

        return sysDictService.toAdd(sysDict);
    }
}
