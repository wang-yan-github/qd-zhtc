package com.jsdc.dubbo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {

    String savePhoto(InputStream file);

    String savePhoto(MultipartFile file);

    String saveVideo(InputStream file);
}
