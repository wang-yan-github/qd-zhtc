package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.utils.Utils;
import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.service.FileManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Description: 文件管理
 * date: 2022-1-10 10:20
 *
 * @author thr
 */
@Controller
@RequestMapping("fileManage")
public class FileManageController extends BaseController {

    @Value("${jsdc.filePath}")
    public String filePath;
    @Autowired
    private FileManageService fileManageService;

    /**
     * 删除图片
     */
    @RequestMapping(value = "delImgFile.json", method = RequestMethod.POST)
    @ResponseBody
    public String delImgFile(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (notEmpty(id)) {
            FileManage fileManage = fileManageService.selectById(id);
            if (notEmpty(fileManage)) {
                fileManage.setIs_del("1");
                fileManageService.updateById(fileManage);
            }
        }
        jsonObject.put("success", "0");
        jsonObject.put("message", "删除成功！");
        return jsonObject.toJSONString();
    }

    /**
     * 图片
     * 上传
     */
    @RequestMapping(value = "imgFileSave.json")
    @ResponseBody
    public String imgFileSave(@RequestParam("file") MultipartFile[] files) {
        String result = "";
        if (!(files.length == 0)) {
            for (int i = 0; i < files.length; i++) {
                result = fileManageService.uploadFiles(files[i]);
            }
        }
        JSONObject res = new JSONObject();
        if (empty(result)) {
            res.put("status", false);
        } else {
            res = JSONObject.parseObject(result);
            res.put("status", true);
        }
        return res.toJSONString();
    }

    /**
     * 回显图片
     * 输出流
     *
     * @param id
     * @param response
     */
    @RequestMapping(value = "getImgView.do", method = RequestMethod.GET)
    public void getImage(Integer id, HttpServletResponse response) {
        if (notEmpty(id)) {
            FileManage fileManage = fileManageService.selectById(id);
            if (notEmpty(fileManage)) {
                String basePath = fileManage.getStore_name();
                Utils.getImage(filePath + basePath, response);
            }
        }
    }

}
