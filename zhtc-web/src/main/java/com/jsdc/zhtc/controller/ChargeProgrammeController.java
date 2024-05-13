package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.service.ChargeProgrammeService;
import com.jsdc.zhtc.vo.ChargeProgrammeData;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

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


}
