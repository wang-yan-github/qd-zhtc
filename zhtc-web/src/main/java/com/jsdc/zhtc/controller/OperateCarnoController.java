package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.service.MemberManageService;
import com.jsdc.zhtc.service.OperateCarnoService;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.BatchAddWhiteCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * ClassName: OperateCarnoController
 * Description:车牌管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/operatecarno")
public class OperateCarnoController extends BaseController {

    @Autowired
    private OperateCarnoService carnoService;
    @Autowired
    private MemberManageService memberManageService;

    @PostMapping("/getMemberAll")
    @ResponseBody
    public ResultInfo getMemberAll(OperateCarno bean) {
        List<OperateCarno> operateCarnos = carnoService.getMemberAll(bean);
        return ResultInfo.success(operateCarnos);
    }

    /**
     * 查询车牌全数据，车牌绑定记录可复用
     *
     * @param operateCarnoVo
     * @return
     */
    @RequestMapping(value = "getAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getAll(OperateCarnoVo operateCarnoVo) {

        return ResultInfo.success(carnoService.toList(operateCarnoVo));

    }

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param operateCarnoVo
     * @return
     * @author bn
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, OperateCarnoVo operateCarnoVo) {


        PageInfo<OperateCarnoVo> memberManageList = carnoService.toList(pageIndex, pageSize, operateCarnoVo);

        return ResultInfo.success(memberManageList);
    }

    @RequestMapping(value = "export.do", method = RequestMethod.POST)
    public void export(OperateCarnoVo operateCarnoVo, HttpServletResponse response) {
        carnoService.export(operateCarnoVo, response);

    }

    /**
     * 解除绑定
     *
     * @param carnoId
     * @return
     */
    @RequestMapping(value = "relieveBind")
    @ResponseBody
    public ResultInfo relieveBind(Integer carnoId) {
        ResultInfo resultInfo = carnoService.relieveBind(carnoId);
        return resultInfo;
    }

    /**
     * 编辑车牌信息：如白名单，残疾人等
     *
     * @param operateCarno
     * @return
     * @author bn
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_UPDCARNO)
    public ResultInfo toEdit(@RequestBody OperateCarno operateCarno) {
        return carnoService.edit(operateCarno);
    }

    /**
     * pc端
     * 新增、修改车牌保存方法
     */
    @RequestMapping(value = "save.json")
    @ResponseBody
    public ResultInfo save(@RequestBody OperateCarno bean) {
        return carnoService.save(bean);
    }

    @RequestMapping(value = "getDictsByType")
    @ResponseBody
    public ResultInfo getDictsByType(String type) {
        return ResultInfo.success(DcCacheDataUtil.getDictByDictType(type));
    }

    /**
     * 获取详情
     *
     * @param carno
     * @return
     */
    @RequestMapping(value = "getDetail")
    @ResponseBody
    public ResultInfo getDetail(String carno, String carType) {
        OperateCarno operateCarno = carnoService.selectOne(new QueryWrapper<OperateCarno>().eq("car_no", carno).eq("is_del", 0).eq("car_type", carType));
        OperateCarnoVo operateCarnoVo = new OperateCarnoVo();
        if (operateCarno != null) {
            BeanUtils.copyProperties(operateCarno, operateCarnoVo);
            //获取车牌类型字典
            HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
            //名单类型
            HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
            operateCarnoVo.setCarTypeName(dictCtMap.get(operateCarno.getCar_type()).getLabel());
            operateCarnoVo.setRosterTypeName(dictRtMap.get(operateCarno.getRoster_type()).getLabel());
            if (operateCarno.getMember_id() != null) {
                //获取用户
                MemberManage memberManage = memberManageService.selectById(operateCarno.getMember_id());
                if (memberManage != null)
                    operateCarnoVo.setMemberName(memberManage.getNick_name());
            }
        } else {
            return ResultInfo.error("无此车牌信息，请先录入车牌信息！");
        }
        return ResultInfo.success(operateCarnoVo);
    }

    /**
     * create by wp at 2022/1/12 14:38
     * description: 车辆绑定个人用户
     *
     * @param carId
     * @param memberId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */

    @RequestMapping("bindCarPerson.do")
    @ResponseBody
    public ResultInfo bindCarPerson(Integer carId, Integer memberId) {
        return carnoService.bindCarPerson(carId, memberId);
    }

    /**
     * 描 述： TODO(用户添加车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    @PostMapping("/saveData")
    public ResultInfo saveData(@RequestBody OperateCarno bean) {
        MemberManage inspecter = memberManageService.getInspecter();
        if (inspecter.getId() == null || inspecter.getId() <= 0)
            return ResultInfo.error("error", "请先登录用户");
        bean.setMember_id(inspecter.getId());
        return carnoService.saveData(bean);
    }

    /**
     * description: 批量增加白名单
     * * 作 者： xuaolong
     *
     * @param batchAddWhiteCarno
     * @returncom.jsdc.zhtc.vo.ResultInfo
     */
    @RequestMapping(value = "/saveBeathWhiteCarNo", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveBeathWhiteCarNo(@RequestBody BatchAddWhiteCarno batchAddWhiteCarno) {
        if (batchAddWhiteCarno != null) {
            return carnoService.addWhiteCarNo(batchAddWhiteCarno);
        } else {
            return ResultInfo.error("请正确填写白名单信息");
        }
    }

    /**
     * Excel导入白名单
     * author wh
     *
     * @param file
     * @return
     */

    /**
     * Excel白名单模板下载
     * author wh
     *
     * @param response
     * @return
     */
    @RequestMapping("downloadWhite")
    public void downloadWhite(HttpServletResponse response) {
        String filename = "白名单登记表.xlsx";
        int len = 0;
        //5.创建数据缓冲区
        byte[] buffer = new byte[1024];
        //通过response对象获取OutputStream流
        try (OutputStream out = response.getOutputStream()) {

            ClassPathResource classPathResource = new ClassPathResource("masterplate/白名单登记表.xlsx");
            InputStream in = classPathResource.getInputStream();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            //将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer)) > 0) {
                //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 详情
     */
    @RequestMapping(value = "view.json")
    @ResponseBody
    public ResultInfo view(@RequestBody OperateCarno bean) {
        return carnoService.view(bean);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "del.json")
    @ResponseBody
    public ResultInfo del(@RequestBody OperateCarno bean) {
        return carnoService.del(bean);
    }

}
