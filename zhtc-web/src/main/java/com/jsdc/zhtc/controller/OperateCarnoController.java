package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.model.SysDict;
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

/**
 * 车牌管理
 *
 * @author thr
 */
@Controller
@RequestMapping("/operatecarno")
public class OperateCarnoController extends BaseController {

    @Autowired
    private OperateCarnoService carnoService;

    /**
     * 车牌列表分页查询
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             OperateCarnoVo operateCarnoVo) {
        PageInfo<OperateCarnoVo> memberManageList = carnoService.toList(pageIndex, pageSize, operateCarnoVo);
        return ResultInfo.success(memberManageList);
    }

    /**
     * 车牌列表查询
     */
    @RequestMapping(value = "getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getList(OperateCarnoVo operateCarnoVo) {
        return ResultInfo.success(carnoService.getList(operateCarnoVo));
    }

    /**
     *白名单已选车辆列表
     * @author wzn
     * @date 2024/5/18 10:18
     */
    @RequestMapping(value = "carList", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo carList(@RequestBody OperateCarnoVo operateCarnoVo) {
        return ResultInfo.success(carnoService.carList(operateCarnoVo.getParkId(),operateCarnoVo.getName(),operateCarnoVo.getCarNo()));
    }


    /**
     * 车牌列表导出
     */
    @RequestMapping(value = "export.do", method = RequestMethod.POST)
    public void export(OperateCarnoVo operateCarnoVo, HttpServletResponse response) {
        carnoService.export(operateCarnoVo, response);
    }

    /**
     * 新增、修改车牌保存方法
     */
    @RequestMapping(value = "save.json")
    @ResponseBody
    public ResultInfo save(@RequestBody OperateCarno bean) {
        return carnoService.save(bean);
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

    /**
     * 获取详情
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

        } else {
            return ResultInfo.error("无此车牌信息，请先录入车牌信息！");
        }
        return ResultInfo.success(operateCarnoVo);
    }

    /**
     * 获取字典数据列表
     */
    @RequestMapping(value = "getDictsByType")
    @ResponseBody
    public ResultInfo getDictsByType(String type) {
        return ResultInfo.success(DcCacheDataUtil.getDictByDictType(type));
    }

    /**
     * 批量增加白名单
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
     * Excel白名单模板下载
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

}
