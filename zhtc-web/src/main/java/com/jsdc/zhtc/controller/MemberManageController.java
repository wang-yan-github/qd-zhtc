package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.service.MemberManageService;
import com.jsdc.zhtc.vo.MemberManageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClassName: MemberManageController <br/>
 * Description: 车主/会员用户<br/>
 * date: 2022/1/4 11:55<br/>
 *
 * @author bn
 */
@Controller
@RequestMapping("membermanage")
public class MemberManageController extends BaseController {


    @Autowired
    private MemberManageService memberManageService;

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param memberManageVo
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "2") Integer pageSize, MemberManageVo memberManageVo) {


        PageInfo<MemberManageVo> memberManageList = memberManageService.toList(pageIndex, pageSize, memberManageVo);
        memberManageList.getList().forEach(x -> {
            String openid = x.getOpenid();

            if (StringUtils.isNotEmpty(openid) && openid.length() > 20) {
                String encryption = openid.replaceAll("^.{0,20}", "**********");
                x.setOpenid(encryption);
            }
        });
        return ResultInfo.success(memberManageList);
    }

    /**
     * 添加会员数据
     *
     * @param memberManage
     * @return
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(MemberManage memberManage) {
        memberManageService.insert(memberManage);
        return ResultInfo.success();
    }

    /**
     * 编辑会员数据
     *
     * @param memberManage
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(MemberManage memberManage) {
        memberManageService.updateById(memberManage);
        return ResultInfo.success();
    }

    /**
     * 获取车主列表信息
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getList(MemberManage bean) {
        List<MemberManage> memberManageList = memberManageService.toList(bean);
        return ResultInfo.success(memberManageList);
    }

}
