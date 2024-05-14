package com.jsdc.zhtc.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import com.jsdc.zhtc.service.CacheDataService;
import com.jsdc.zhtc.service.ChargeProgrammeService;
import com.jsdc.zhtc.vo.ChargeProgrammeData;
import com.jsdc.zhtc.vo.ChargeProgrammeVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ChargeProgrammeController <br/>
 * Description: <br/>
 * date: 2021/12/30 11:02<br/>
 *
 * @author bn<br   />
 */
@Controller
@RequestMapping("chargeCrogramme")
public class ChargeProgrammeController extends BaseController {

    @Autowired
    private ChargeProgrammeService chargeProgrammeService;

    @Autowired
    private CacheDataService cacheDataService;


    /**
     *  列表查询
     * @author bn
     * @param pageIndex
     * @param pageSize
     * @param chargeProgrammeVo
     * @return
     */
    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, ChargeProgrammeVo chargeProgrammeVo){


        PageInfo<ChargeProgrammeVo> page=chargeProgrammeService.toList(pageIndex,pageSize,chargeProgrammeVo);

        return ResultInfo.success(page);
    }

    /**
     *  添加收费方案
     * @author bn
     * @return
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_ADDCHARGECROGRAMME)
    public ResultInfo toAdd(@RequestBody String charge_programme){

        JSONObject jsonObject=JSONObject.parseObject(charge_programme);

        ChargeProgrammeData chargeProgrammeData=JSONObject.parseObject(jsonObject.getString("charge_programme"),ChargeProgrammeData.class);

        return chargeProgrammeService.toAdd(chargeProgrammeData);

    }

    /**
     * 收费方案编辑
     * @param charge_programme
     * @return
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_UPDATECHARGECROGRAMME)
    public ResultInfo toEdit(@RequestBody String charge_programme){
        return chargeProgrammeService.toEdit(charge_programme);
    }


    /**
     * 收费方案删除
     * @param chargeProgramme
     * @return
     */
    @RequestMapping(value = "toDel.do",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_DELETECHARGECROGRAMME)
    public ResultInfo toDel(ChargeProgramme chargeProgramme){
        ChargeProgramme original = chargeProgrammeService.selectById(chargeProgramme.getId());
        original.setIs_del(GlobalData.ISDEL_YES);
        chargeProgrammeService.updateById(original);

        cacheDataService.parkChargeInit();
        return ResultInfo.success(null, "删除收费方案， 方案名：" + original.getProgramme_name());
    }

    /**
     *  批量删除收费方案
     * @param programmeIds
     * @return
     */
    @RequestMapping(value = "delProgrammeAll.do",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(LogEnums.LOG_DELETEBATCHCHARGECROGRAMME)
    public ResultInfo delProgrammeAll(String programmeIds){
        return chargeProgrammeService.delAll(programmeIds);
    }

    @RequestMapping("/importChargeProgramme.do")
    @ResponseBody
    public ResultInfo importChargeProgramme(MultipartFile file) throws IOException {
        //fileName 文件名
        String fileName = file.getOriginalFilename();
        boolean xlsx = fileName.endsWith(".xlsx");
        if (!xlsx) {
            return ResultInfo.error("请上传以.xlsx结尾的文件");
        }
        // MultipartFile 转 file
        File toFile = transferToFile(file);
        //定义文件路径
//        String filepath = System.getProperty("user.dir") + File.separator + "excel" + File.separator + "收费配置模版.xlsx";
        //通过工具类创建ExcelReader
        ExcelReader reader = ExcelUtil.getReader(toFile);
        //默认获取第一页
        List<Map<String, Object>> readAll = reader.read(1, 1, Integer.MAX_VALUE);
        List<ChargeProgrammeData> chargeProgrammes = new ArrayList<>();
        for (Map<String, Object> s : readAll) {
            //ChargeProgrammeData
            ChargeProgrammeData chargeProgrammeData = new ChargeProgrammeData();
            for (String k : s.keySet()) {
                if (org.apache.commons.lang.StringUtils.isBlank(s.get(k).toString())) {
                    throw new RuntimeException("必填字段(*)不能为空");
                }
                switch (k) {
                    case "编码(*)":
                        chargeProgrammeData.setExcelCode(s.get(k).toString());
                        break;
                    case "收费方案名称(*)":
                        chargeProgrammeData.setProgramme_name(s.get(k).toString());
                        break;
                    case "设置限定金额(*)":
                        chargeProgrammeData.setIs_limit_price(org.apache.commons.lang.StringUtils.equals(s.get(k).toString(), "是") ? 1 : 0);
                        break;
                    case "设置限价金额(*)":
                        chargeProgrammeData.setLimit_price_amount((s.get(k).toString()));
                        break;
                    default:
                        break;
                }
            }
            chargeProgrammes.add(chargeProgrammeData);
        }
        System.out.println("Excel第一页读取结果：" + chargeProgrammes);
        if (CollectionUtils.isEmpty(chargeProgrammes)) {
            throw new RuntimeException("收费方案不能为空");
        }

        ExcelReader reader1 = ExcelUtil.getReader(toFile, "白天夜晚时间段设置表");
        List<Map<String, Object>> readAlls = reader1.read(2, 2, Integer.MAX_VALUE);
        for (ChargeProgrammeData chargeProgrammeData : chargeProgrammes) {
            // 判断chargeProgrammes的编码在readAlls中是否存在
            for (Map<String, Object> s : readAlls) {
                if (org.apache.commons.lang.StringUtils.equals(chargeProgrammeData.getExcelCode(), s.get("收费方案编码(*)").toString())) {
                    // 设置白天夜晚时间段
                    ChargeIntervalConfig chargeIntervalConfig = new ChargeIntervalConfig();
                    for (String k : s.keySet()) {
                        if (org.apache.commons.lang.StringUtils.isBlank(s.get(k).toString())) {
                            throw new RuntimeException("必填字段(*)不能为空");
                        }
                        switch (k) {
                            case "白天/夜晚时间编码(*)":
                                chargeIntervalConfig.setExcelCode(s.get(k).toString());
                                break;
                            case "白天/夜晚(*)":
                                chargeIntervalConfig.setInteval_type(org.apache.commons.lang.StringUtils.equals(s.get(k).toString(), "白天") ? "0" : "1");
                                break;
                            case "收费时段开始时间(*)":
                                chargeIntervalConfig.setStart_time(s.get(k).toString());
                                break;
                            case "收费时段结束时间(*)":
                                chargeIntervalConfig.setEnd_time(s.get(k).toString());
                                break;
                            case "免费停车时长(分*)":
                                chargeIntervalConfig.setFree_time(Integer.valueOf(s.get(k).toString()));
                                break;
                            case "分段限价设定(元*)":
                                chargeIntervalConfig.setInterval_limit_price(s.get(k).toString());
                                break;
                            case "收费单位(分*)":
                                chargeIntervalConfig.setCharge_unit(Integer.valueOf(s.get(k).toString()));
                                break;
                            default:
                                break;
                        }
                    }
                    if (org.apache.commons.lang.StringUtils.equals(chargeIntervalConfig.getInteval_type(), "0")) {
                        chargeProgrammeData.setDays(chargeIntervalConfig);
                    } else {
                        chargeProgrammeData.setNight(chargeIntervalConfig);
                    }
                }
            }
        }
        System.out.println("Excel第二页读取结果：" + chargeProgrammes);
        // 判断白天夜晚时间段是否存在
        for (ChargeProgrammeData chargeProgrammeData : chargeProgrammes) {
            if (chargeProgrammeData.getDays() == null) {
                throw new RuntimeException("白天时间段不能为空");
            }
            if (chargeProgrammeData.getNight() == null) {
                throw new RuntimeException("夜晚时间段不能为空");
            }
        }
        // 判断白天+黑夜时间是否为一整天24小时
        for (ChargeProgrammeData chargeProgrammeData : chargeProgrammes) {
            ChargeIntervalConfig day = chargeProgrammeData.getDays();
            ChargeIntervalConfig night = chargeProgrammeData.getNight();
            String dayStart = day.getStart_time();
            String dayEnd = day.getEnd_time();
            String nightStart = night.getStart_time();
            String nightEnd = night.getEnd_time();
            if (!(org.apache.commons.lang.StringUtils.equals(dayStart, nightEnd) && org.apache.commons.lang.StringUtils.equals(dayEnd, nightStart))) {
                throw new RuntimeException("白天黑夜不足24小时");
            }
        }

        ExcelReader reader2 = ExcelUtil.getReader(toFile, "时长价格设置表");
        List<Map<String, Object>> readAll3 = reader2.read(1, 1, Integer.MAX_VALUE);
        for (ChargeProgrammeData chargeProgrammeData : chargeProgrammes) {

            ChargeIntervalConfig day = chargeProgrammeData.getDays();
            ChargeIntervalConfig night = chargeProgrammeData.getNight();
            // 白天编码用于时间表判断
            String dayCode = day.getExcelCode();
            // 夜晚编码用于时间表判断
            String nightCode = night.getExcelCode();
            // 白天时间段集合
            List<ChargeTimeConfig> daysTime = new ArrayList<>();
            // 夜晚时间段集合
            List<ChargeTimeConfig> nightTime = new ArrayList<>();
            for (Map<String, Object> s : readAll3) {
                ChargeTimeConfig chargeTimeConfig = new ChargeTimeConfig();
                for (String k : s.keySet()) {
                    if (org.apache.commons.lang.StringUtils.isBlank(s.get(k).toString())) {
                        throw new RuntimeException("必填字段(*)不能为空");
                    }
                    switch (k) {
                        case "白天/夜晚时间编码(*)":
                            chargeTimeConfig.setExcelCode(s.get(k).toString());
                            break;
                        case "开始时间(*)":
                            chargeTimeConfig.setStart_minute(Integer.valueOf(s.get(k).toString()));
                            break;
                        case "结束时间(*)":
                            chargeTimeConfig.setEnd_minute(Integer.valueOf(s.get(k).toString()));
                            break;
                        case "价格(元*)":
                            chargeTimeConfig.setPrice(s.get(k).toString());
                            break;
                        default:
                            break;
                    }
                }
                if (org.apache.commons.lang.StringUtils.equals(chargeTimeConfig.getExcelCode(), dayCode)) {
                    daysTime.add(chargeTimeConfig);
                }
                if (StringUtils.equals(chargeTimeConfig.getExcelCode(), nightCode)) {
                    nightTime.add(chargeTimeConfig);
                }
            }
            // 根据白天的开始时间和结束时间判断时间表的分钟数,累加起来是否等于白天时间段的总分钟数;
            long daySumMin = CalTime(day.getEnd_time(), day.getStart_time());
            long dayMinute = 0;
            for (ChargeTimeConfig chargeTimeConfig : daysTime) {
                dayMinute += (chargeTimeConfig.getEnd_minute() - chargeTimeConfig.getStart_minute());
            }
            if (dayMinute != daySumMin) {
                throw new RuntimeException("白天时间段的总分钟数不等于时间表的分钟数");
            }
            // 根据夜晚的开始时间和结束时间判断时间表的分钟数,累加起来是否等于夜晚时间段的总分钟数;
            long nightSumMin = CalTime(night.getEnd_time(), night.getStart_time());
            long nightMinute = 0;
            for (ChargeTimeConfig chargeTimeConfig : nightTime) {
                nightMinute += (chargeTimeConfig.getEnd_minute() - chargeTimeConfig.getStart_minute());
            }
            if (nightMinute != nightSumMin) {
                throw new RuntimeException("夜晚时间段的总分钟数不等于时间表的分钟数");
            }
            chargeProgrammeData.setDaysTime(daysTime);
            chargeProgrammeData.setNightTime(nightTime);
        }
        System.out.println("校验成功");
        for (ChargeProgrammeData chargeProgrammeData : chargeProgrammes){
            // 实体转json
            chargeProgrammeService.toAdd(chargeProgrammeData);
        }
        return ResultInfo.success("导入成功");
    }

    /**
     * 全数据
     *
     * @param chargeProgramme
     * @return
     * @author bn
     */
    @RequestMapping(value = "getAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getAll(ChargeProgramme chargeProgramme) {


        return ResultInfo.success(chargeProgrammeService.toList(chargeProgramme));
    }

    /**
     * 根据id 获取收费方案
     *
     * @param chargeProgramme
     * @return
     */
    @RequestMapping(value = "getChargeProgramme.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getChargeProgramme(ChargeProgramme chargeProgramme) {

        ChargeProgrammeData chargeProgrammeData = chargeProgrammeService.getChargeProgramme(chargeProgramme);

        return ResultInfo.success(chargeProgrammeData);
    }


    @RequestMapping("downloadWhite")
    public void downloadWhite(HttpServletResponse response) {
        String filename = "收费配置模版.xlsx";
        int len = 0;
        //5.创建数据缓冲区
        byte[] buffer = new byte[1024];
        //通过response对象获取OutputStream流
        try (OutputStream out = response.getOutputStream()) {

            ClassPathResource classPathResource = new ClassPathResource("masterplate/收费配置模版.xlsx");
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
     *  计算两个时间差，返回为分钟。
     */
    private static long CalTime(String endTime, String startTime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        long minutes = 0L;
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            if (d1.getTime() < d2.getTime()) {
                // d1 增加一天
                d1 = new Date(d1.getTime() + 24 * 60 * 60 * 1000);
            }
            // 这样得到的差值是微秒级别
            long diff = d1.getTime() - d2.getTime();
            minutes = diff / (1000 * 60);
        } catch (Exception e) {
            System.out.println("抱歉，时间日期解析出错。");
        }
        return minutes;
    }

    /**
     * MultipartFile 转换为 File 文件
     *
     * @param multipartFile
     * @return
     */
    public final static File transferToFile(MultipartFile multipartFile) {
        //选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件后缀
            String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // String[] filename = originalFilename.split("\\.");
            // file = File.createTempFile(filename[0], filename[1]);
            // 注意下面的 特别注意！！！
            // 创建零食文件
            file = File.createTempFile(originalFilename, prefix);
            multipartFile.transferTo(file);
            //删除
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
