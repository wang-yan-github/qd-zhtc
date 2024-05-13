package com.jsdc.zhtc.service;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ProvisionalPassDao;
import com.jsdc.zhtc.mapper.ProvisionalPassMapper;
import com.jsdc.zhtc.model.ProvisionalPass;
import com.jsdc.zhtc.utils.ticket.FreeMarkerUtils;
import com.jsdc.zhtc.utils.ticket.QrCodeUtil;
import com.jsdc.zhtc.vo.ProvisionalPassVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ProvisionalPassService
 * Description:
 * date: 2022/2/24 9:46
 *
 * @author bn
 */
@Service
@Transactional
public class ProvisionalPassService extends BaseService<ProvisionalPassDao, ProvisionalPass> {

    @Autowired
    private ProvisionalPassMapper provisionalPassMapper;


    @Autowired
    private SysUserService sysUserService;

    public PageInfo<ProvisionalPassVo> toList(Integer pageIndex, Integer pageSize, ProvisionalPassVo passVo) {

        PageHelper.startPage(pageIndex, pageSize);

        List<ProvisionalPassVo> passVos = provisionalPassMapper.toList(passVo);
        for (ProvisionalPassVo vo : passVos) {
            vo.setStatus("1");

            Date date1 = new Date();
            Date date2 = vo.getExpire_time();
            int result = date1.compareTo(date2);
            if (result > 0) {
                vo.setStatus("0");
            }
            if (vo.getHxCount() == 0) {
                vo.setStatus("0");
            }
        }

        PageInfo<ProvisionalPassVo> pageInfo = new PageInfo<>(passVos);

        return pageInfo;

    }

    public ResultInfo toAdd(ProvisionalPass pass) {

        pass.setCreate_time(new Date());
        pass.setCreate_user(sysUserService.getUser().getId());
        insert(pass);

        return ResultInfo.success();
    }

    public ResultInfo toEdit(ProvisionalPass pass) {
        pass.setUpdate_user(sysUserService.getUser().getId());
        pass.setUpdate_time(new Date());
        updateById(pass);

        return ResultInfo.success();
    }

    /**
     * 临时通信证二维码
     *
     * @param passVo
     * @param response
     * @return
     */
    public ResultInfo getQrCode(ProvisionalPassVo passVo, HttpServletResponse response) {
        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());

//        String encryptHex = aes.encryptHex("http://192.168.0.104:8081/provisionalPassRecord/getUrl?id=" + passVo.getId());
//        String encryptHex = aes.encryptHex(String.valueOf(passVo.getId()));

        String base64 = "";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("pass", passVo);
//            map.put("img", QrCodeUtil.encode_QR_CODE("http://192.168.0.104:8081/provisionalPassRecord/getUrl?id=" + passVo.getId()));
            map.put("img", QrCodeUtil.encode_QR_CODE("https://zhtc.aldwxa.top/api/provisionalPassRecord/getUrl?id=" + passVo.getId()));
            base64 = FreeMarkerUtils.turnImage("provisionalPass.ftl", map, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultInfo.success(base64);
    }

    public ResultInfo exportImg(String code, HttpServletResponse response) {
        JSONObject jsonObject = JSON.parseObject(code);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-jpg");
        try {

//            Map<String,Object> map=new HashMap<>();
//            ProvisionalPassVo passVo=new ProvisionalPassVo();
//            passVo.setCompanyName("852");
//            passVo.setPark_name("456");
//            map.put("pass",passVo);
//            map.put("img", QrCodeUtil.encode_QR_CODE("123"));
//            String base64 = FreeMarkerUtils.turnImage("provisionalPass.ftl", map, response);

            byte[] bytes = DatatypeConverter.parseBase64Binary(jsonObject.getString("base64"));
            ;


            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("临时通行二维码.jpg", "UTF-8"));

            outputStream = response.getOutputStream();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            int readLen = -1;
            byte[] buff = new byte[4096];
            while ((readLen = inputStream.read(buff)) != -1) {

                outputStream.write(buff, 0, readLen);
            }
//            outputStream.write(bytes);

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);


    }
}
