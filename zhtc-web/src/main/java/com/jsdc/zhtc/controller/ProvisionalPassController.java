package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.ProvisionalPass;
import com.jsdc.zhtc.service.ProvisionalPassService;
import com.jsdc.zhtc.vo.ProvisionalPassVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: ProvisionalPassController
 * Description: 临时牌照
 * date: 2022/2/24 9:45
 *
 * @author bn
 */
@Controller
@RequestMapping("pass")
public class ProvisionalPassController extends BaseController {

    @Autowired
    private ProvisionalPassService passService;


    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param passVo
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, ProvisionalPassVo passVo) {


        PageInfo<ProvisionalPassVo> page = passService.toList(pageIndex, pageSize, passVo);

        return ResultInfo.success(page);
    }


    /**
     * 添加临时通行证
     *
     * @param pass
     * @return
     * @author bn
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(ProvisionalPass pass) {

        return passService.toAdd(pass);
    }


    /**
     * 编辑临时通行证
     *
     * @param pass
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(ProvisionalPass pass) {

        return passService.toEdit(pass);
    }

    /**
     * 临时通行证二维码
     *
     * @param passVo
     * @return
     * @author wh
     */
    @RequestMapping(value = "getQrCode.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getQrCode(ProvisionalPassVo passVo, HttpServletResponse response) {
        return passService.getQrCode(passVo, response);
    }


    /**
     * 导出图片
     */
    @RequestMapping(value = "passImg.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportImg(@RequestBody String base64, HttpServletResponse response) {
        passService.exportImg(base64, response);
    }

}
