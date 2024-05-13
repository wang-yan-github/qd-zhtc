package com.jsdc.zhtc.controller;


import com.jsdc.zhtc.common.upload.FileRepository;
import com.jsdc.zhtc.common.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Controller
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private FileRepository fileRepository;


    @Value("${excel-export-path}")
    private String excelPath;


    //下载本地Excel模板
    @GetMapping("common/exceldownload")
    public void exceldownload(String fileName, Boolean delete, HttpServletRequest request, HttpServletResponse response, String name)
            throws Exception {
        // 本地资源路径
        String localPath = excelPath;
        // 文件路径
        String downloadPath = localPath + "/" + fileName;
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, name));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
        if (delete) {
            FileUtils.deleteFile(downloadPath);
        }
    }

}
