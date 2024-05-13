
package com.jsdc.zhtc.utils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jsdc.dubbo.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


/**
 * ClassName: FileUploadUtils
 * Description:
 * date: 2022/1/10 17:05
 *
 * @author wp
 */


@Service
public class FileUploadUtils {
    @Reference(version = "1.0.0", check = false, parameters = {"protocol", "hessian"})
    private FileService fileService;

    public String savePhoto(InputStream file) {
        return fileService.savePhoto(file);
    }

    public String savePhoto(MultipartFile file) {
        return fileService.savePhoto(file);
    }

    public String saveVideo(InputStream file) {
        return fileService.saveVideo(file);
    }
}

