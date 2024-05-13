package com.jsdc.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service(interfaceClass = FileService.class, version = "1.0.0", protocol = {"hessian"})
public class FileUploadServiceImpl implements FileService {

    @Value("${jsdc.fileUrl}")
    private String url;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String savePhoto(InputStream file) {

        String uuid = UUID.randomUUID().toString();
        String origName = url + uuid + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(origName));
            IOUtils.copy(file, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Failure";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failure";
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
            if (file != null) {
                IOUtils.closeQuietly(file);
            }
        }
        return uuid;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(format.format(date));

    }

    @Override
    public String savePhoto(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String origName = url + uuid + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(origName));
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Failure";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failure";
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
        }
        return uuid;
    }

    @Override
    public String saveVideo(InputStream file) {
        String uuid = UUID.randomUUID().toString();
        String origName = url + uuid + ".mp4";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(origName));
            IOUtils.copy(file, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Failure";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failure";
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
            if (file != null) {
                IOUtils.closeQuietly(file);
            }
        }
        return uuid;
    }
}
