package com.jsdc.zhtc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.zhtc.model.MonthlyManagement;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.MonthlyManagementService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.MonthlyManagementVo;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


/**
 * 类 名: 包月管理
 * 描 述: MonthlyManagementController
 * 作 者: lw
 * 创 建：2022/1/4 10:25
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Controller
@RequestMapping("/monthlymanagement")
public class MonthlyManagementController {

    @Autowired
    private MonthlyManagementService service;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 描 述： TODO(包月充值统计 按日期 按车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getTimeLicenceCensus")
    @ResponseBody
    public ResultInfo getTimeLicenceCensus(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();
        if (data.isFlag()) {
            return service.getTimeCensus(data, park_id);
        } else {
            return service.getLicenceCensus(data, park_id);
        }
    }

    /**
     * 描 述： TODO(包月充值统计 按日期)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getTimeCensus")
    @ResponseBody
    public ResultInfo getTimeCensus(@RequestBody CommonVo data) {

        return service.getTimeCensus(data, null);

    }

    /**
     * 描 述： TODO(包月充值统计 按车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getLicenceCensus")
    @ResponseBody
    public ResultInfo getLicenceCensus(@RequestBody CommonVo data) {

        return service.getLicenceCensus(data, null);

    }


    /**
     * 描 述： TODO(车牌总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getLicenceCount")
    @ResponseBody
    public ResultInfo getLicenceCount(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                return service.getLicenceCount(data);
            } else if (data.getVariance3() == null) {
                data.setVariance3(park_id);
                return service.getLicenceCount(data);
            }
            return ResultInfo.success(0);
        } else {
            return service.getLicenceCount(data);
        }

    }

    /**
     * 描 述： TODO(充值笔数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getRechargeCount")
    @ResponseBody
    public ResultInfo getRechargeCount(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                return service.getRechargeCount(data);
            } else if (data.getVariance3() == null) {
                data.setVariance3(park_id);
                return service.getRechargeCount(data);
            }
            return ResultInfo.success(0);
        } else {
            return service.getRechargeCount(data);
        }

    }

    /**
     * 描 述： TODO(充值总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getRechargeAmount")
    @ResponseBody
    public ResultInfo getRechargeAmount(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();

        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                return service.getRechargeAmount(data);
            } else if (data.getVariance3() == null) {
                data.setVariance3(park_id);
                return service.getRechargeAmount(data);
            }
            return ResultInfo.success("0.0");
        } else {
            return service.getRechargeAmount(data);
        }

    }

    /**
     * 描 述： TODO(查询在包月车辆总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/getNormalMPCount")
    @ResponseBody
    public ResultInfo getNormalMPCount(@RequestBody CommonVo data) {
        SysUser user = sysUserService.getUser();
        Integer park_id = user.getPark_id();
        if (park_id != null && park_id.compareTo(0) > 0) {
            if (data.getVariance3() != null && data.getVariance3().compareTo(park_id) == 0) {
                return service.getNormalMPCount(data);
            } else if (data.getVariance3() == null) {
                data.setVariance3(park_id);
                return service.getNormalMPCount(data);
            }
            return ResultInfo.success(0);
        } else {
            return service.getNormalMPCount(data);
        }

    }


    @PostMapping(value = "/num")
    @ResponseBody
    public ResultInfo num(String monthly_code, Integer strs) {
        ResultInfo json = service.num(monthly_code, strs);
        return json;
    }

    @PostMapping(value = "/allNum")
    @ResponseBody
    public ResultInfo allNum(String monthly_code) {
        ResultInfo json = service.allNum(monthly_code);
        return json;
    }


    /**
     * 描 述： TODO( 用户包月信息 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/findManageInfo")
    @ResponseBody
    public ResultInfo findManageInfo(@RequestBody CommonVo data) {

        ResultInfo json = service.findManageInfo(data);

        return json;
    }


    @PostMapping(value = "/info")
    @ResponseBody
    public ResultInfo info(String monthly_code) {

        ResultInfo json = service.info(monthly_code);

        return json;
    }

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @PostMapping(value = "/getall")
    @ResponseBody
    public ResultInfo getAll(@RequestBody PageVo<MonthlyManagement> data) {
        ResultInfo json = service.selectAll(data);
        return json;
    }


    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/getbyid", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(@RequestBody MonthlyManagement bean) {
        ResultInfo json = service.selectById(bean);
        return json;
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(@RequestBody CommonVo<MonthlyManagement> data) {
        ResultInfo json = service.saveData(data);
        return json;
    }

    /**
     * 包月续费
     * Author wzn
     * Date 2022/2/24 16:18
     */
    @RequestMapping(value = "/xf", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo xf(@RequestBody CommonVo<MonthlyManagement> data) {
        ResultInfo json = service.xf(data);
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
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo update(@RequestBody MonthlyManagement bean) {
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
    @ResponseBody
    public ResultInfo delete(@RequestBody MonthlyManagement bean) {
        if (bean.getIsTheCompany().equals("2")) {
            QueryWrapper<MonthlyManagement> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("monthly_code", bean.getMonthly_code());
            List<MonthlyManagement> monthlyManagement = service.selectList(queryWrapper);
            if (!monthlyManagement.isEmpty()) {
                for (MonthlyManagement m : monthlyManagement) {
                    service.deleteById(m);
                }
            }
        } else if (bean.getIsTheCompany().equals("1")) {
            MonthlyManagement monthlyManagement = service.selectById(bean.getId());
            if (null != monthlyManagement) {
                service.deleById(monthlyManagement);
            }
        }


        return ResultInfo.success();
    }

    /**
     * 描 述： TODO(删除数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteList(@RequestBody CommonVo bean) {
        ResultInfo json = service.deleByIdList(bean);
        return json;
    }


    /**
     * Excel白名单模板下载
     * author wh
     *
     * @param response
     * @return
     */
    @RequestMapping("downloadMonthly")
    public void downloadMonthly(HttpServletResponse response) {
        String filename = "个人包月模板.xlsx";
        int len = 0;
        //5.创建数据缓冲区
        byte[] buffer = new byte[1024];
        //通过response对象获取OutputStream流
        try (OutputStream out = response.getOutputStream()) {

            ClassPathResource classPathResource = new ClassPathResource("masterplate/个人包月模板.xlsx");
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
     * Excel导入白名单
     */
    @RequestMapping(value = "importMonthlyList")
    @ResponseBody
    public ResultInfo importMonthlyList(@RequestParam("file") MultipartFile file,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (file.isEmpty()) {
            return ResultInfo.error("请选择上传文件");
        } else {
            String name = file.getOriginalFilename();
            ResultInfo resultInfo = service.readExcel(file.getInputStream(), name);
            return resultInfo;
        }
    }


    /**
     * 支付流水明细关联包月订单
     *
     * @author thr
     */
    @PostMapping(value = "/getMonthlyDataByPaymentId.do")
    @ResponseBody
    public ResultInfo getMonthlyDataByPaymentId(MonthlyManagementVo vo) {
        List<MonthlyManagementVo> list = service.getMonthlyDataByPaymentId(vo.getId());
        return ResultInfo.success(list);
    }
}
