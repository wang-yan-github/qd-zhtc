package com.jsdc.zhtc.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jsdc.jpush.api.excelderive.ExcelDeriveModel;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.mapper.PaymentOrderMapper;
import com.jsdc.zhtc.service.CarnoCompanyService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.BatchCarno;
import com.jsdc.zhtc.vo.PaymentOrderVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangy
 * @version 1.0
 * @description: excel导出
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    PaymentOrderMapper paymentOrderMapper;
    @Autowired
    private CarnoCompanyService service;

    /**
     * 支付订单导出
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exportPaymentOrder")
    // paymentNoOrSerialNo: "",
    //     areaId: "",
    //     streetId: "",
    //     roadId: "",
    //     paymentType: "",
    //     startTime: "",
    //     endTime: "",
    public void exportPaymentOrder(HttpServletResponse response, String parkingType,
                                   @RequestParam(value = "paymentNoOrSerialNo", required = false) String paymentNoOrSerialNo,
                                   @RequestParam(value = "areaId", required = false) String areaId,
                                   @RequestParam(value = "streetId", required = false) String streetId,
                                   @RequestParam(value = "roadId", required = false) String roadId,
                                   @RequestParam(value = "paymentType", required = false) String paymentType,
                                   @RequestParam(value = "startTime", required = false) String startTime,
                                   @RequestParam(value = "endTime", required = false) String endTime) throws IOException {
        PaymentOrderVo vo = new PaymentOrderVo();
        vo.setParkingType(parkingType);
        vo.setPaymentNoOrSerialNo(StringUtils.isBlank(paymentNoOrSerialNo) ? "" : paymentNoOrSerialNo);
        vo.setAreaId(StringUtils.isNotBlank(areaId) ? Integer.valueOf(areaId) : null);
        vo.setStreetId(StringUtils.isNotBlank(streetId) ? Integer.valueOf(streetId) : null);
        vo.setRoadId(StringUtils.isNotBlank(roadId) ? Integer.valueOf(roadId) : null);
        vo.setPaymentType(StringUtils.isNotBlank(paymentType) ? paymentType : null);
        vo.setStartTime(StringUtils.isNotBlank(startTime) ? startTime : null);
        vo.setEndTime(StringUtils.isNotBlank(endTime) ? endTime : null);
        List<Map<String, String>> list = paymentOrderMapper.listPaymentOrder(vo);
        list.forEach(l -> {
            if (StringUtils.isNotEmpty(l.get("payment_type"))) {
                if ("1".equals(l.get("payment_type"))) {
                    l.put("payment_type", "包月");
                } else if ("2".equals(l.get("payment_type"))) {
                    l.put("payment_type", "微信");
                } else if ("3".equals(l.get("payment_type"))) {
                    l.put("payment_type", "支付宝");
                } else if ("4".equals(l.get("payment_type"))) {
                    l.put("payment_type", "钱包");
                } else if ("5".equals(l.get("payment_type"))) {
                    l.put("payment_type", "现金");
                } else if ("6".equals(l.get("payment_type"))) {
                    l.put("payment_type", "银行卡");
                } else if ("7".equals(l.get("payment_type"))) {
                    l.put("payment_type", "商家支付");
                } else if ("8".equals(l.get("payment_type"))) {
                    l.put("payment_type", "聚合支付");
                }
            }
        });
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //自定义标题别名
        writer.addHeaderAlias("payment_serialno", "流水号");
        writer.addHeaderAlias("payment_no", "订单号");
        writer.addHeaderAlias("payment_type", "支付方式");
        writer.addHeaderAlias("amount", "金额");
        writer.addHeaderAlias("pay_time", "营收时间");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        //out为OutputStream，需要写出到的目标流
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //弹出下载对话框的文件名，不能为中文，中文请自行编码
//        response.setHeader("Content-Disposition", "attachment;filename=营收管理.xls");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("营收管理.xls", "UTF-8"));
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 车牌管理模板导出
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exportOperateCarno")
    public void exportOperateCarno(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
            // 文件存储路径
            String path = ExcelDeriveModel.class.getResource("/masterplate/").getPath();
            // 从请求中获取文件名
            String fileName = "cppldr.xlsx";
            // 创建输出流对象
            ServletOutputStream outputStream = response.getOutputStream();
            //以字节数组的形式读取文件
            byte[] bytes = FileUtil.readBytes(path + fileName);
            // 设置返回内容格式
            response.setContentType("application/octet-stream");
            // 把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码
            // 中文不要太多，最多支持17个中文，因为header有150个字节限制。
            // 这一步一定要在读取文件之后进行，否则文件名会乱码，找不到文件
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            // 设置下载弹窗的文件名和格式（文件名要包括名字和文件格式）
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            // 返回数据到输出流对象中
            outputStream.write(bytes);
            // 关闭流对象
            IoUtil.close(outputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 车牌管理模板导入
     *
     * @throws IOException
     */
    @RequestMapping("/importOperateCarno")
    @ResponseBody
    public ResultInfo importOperateCarno(MultipartFile file, Integer companyId) throws IOException {
        //fileName 文件名
        String fileName = file.getOriginalFilename();
        boolean xlsx = fileName.endsWith(".xlsx");
        if (!xlsx) {
            return ResultInfo.error("请上传以.xlsx结尾的文件");
        }
        BatchCarno batchCarno = new BatchCarno();
        batchCarno.setCompanyId(companyId);
        List<String> blueCars = new ArrayList<>();
        List<String> yellowCars = new ArrayList<>();
        List<String> greenCars = new ArrayList<>();
        //得到文件流
        InputStream inputStream = file.getResource().getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Map<String, Object>> readAll = reader.readAll();
        if (readAll.size() == 0) {
            return ResultInfo.error("空白模板，请填写内容！");
        }
        for (int i = 0; i < readAll.size(); i++) {
            Map<String, Object> quMap = readAll.get(i);
            //获取表格中的数据
            String type = String.valueOf(quMap.get("车牌类型"));
            if (StringUtils.isNotEmpty(type)) {
                if (!type.equals("蓝牌") && type.equals("黄牌") && type.equals("绿牌")) {
                    return ResultInfo.error("请选择正确选择车牌,第" + (i + 1) + "行");
                }
                String no = String.valueOf(quMap.get("车牌号"));
                String name = String.valueOf(quMap.get("姓名"));
                String phone = String.valueOf(quMap.get("电话"));
                if (StringUtils.isEmpty(no)) {
                    return ResultInfo.error("请输入车牌号,第" + (i + 1) + "行");
                }
                if (StringUtils.isEmpty(name)) {
                    return ResultInfo.error("请输入名字,第" + (i + 1) + "行");
                }
                if (StringUtils.isEmpty(phone)) {
                    return ResultInfo.error("请输入手机号,第" + (i + 1) + "行");
                }
                if (type.equals("蓝牌")) {
                    blueCars.add(no + "-" + name + "-" + phone);
                }
                if (type.equals("黄牌")) {
                    yellowCars.add(no + "-" + name + "-" + phone);
                }
                if (type.equals("绿牌")) {
                    greenCars.add(no + "-" + name + "-" + phone);
                }
            } else {
                return ResultInfo.error("请选择车牌类型,第" + i + "行");
            }

        }
        batchCarno.setBlueCars(blueCars);
        batchCarno.setYellowCars(yellowCars);
        batchCarno.setGreenCars(greenCars);
        return service.batchBindCars(batchCarno);
    }

}
