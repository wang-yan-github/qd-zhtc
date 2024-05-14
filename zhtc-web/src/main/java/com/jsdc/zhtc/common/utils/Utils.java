package com.jsdc.zhtc.common.utils;

import com.jsdc.core.base.Base;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 常用工具类
 */
public class Utils extends Base {

    /**
     * 回显图片
     * 获取输出文件流
     *
     * @param response
     * @return
     */
    public static void getImage(String basePath, HttpServletResponse response) {
        String pictureUrl = "";

        FileInputStream fis = null;
        response.setContentType("image/png;");
        try {
            OutputStream out = response.getOutputStream();
            File file = new File(basePath + pictureUrl);
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }






}
