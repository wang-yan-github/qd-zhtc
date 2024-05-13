package com.jsdc.zhtc.service;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.upload.FileRepository;
import com.jsdc.zhtc.dao.FileManageDao;
import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.utils.FileUploadUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jsdc.core.base.Base.notEmpty;

/**
 * ClassName: FileManageService
 * Description:
 * date: 2021/12/30 10:55
 *
 * @author wh
 */
@Service
@Transactional
public class FileManageService extends BaseService<FileManageDao, FileManage> {

    //    @Value("${jsdc.filePath}")
//    public String filePath;
//    @Value("${jsdc.loadPicPath}")
//    public String loadPicPath;
    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Autowired
    private SysUserService sysUserService;


    /**
     * create by zonglina at 2022/1/13 16:29
     * description:
     * 根据图片id，获取图片信息String接收
     *
     * @return : null
     * @param:null
     */
    public List<FileManage> selectByFiles(String ids) {
        List<FileManage> picture = new ArrayList<>();
        if (notEmpty(ids)) {
            String[] strings = ids.split(",");
            for (String str : strings) {
                FileManage fileManage = selectById(Integer.parseInt(str));
                if (StringUtils.isNotBlank(fileManage.getFile_url()) && fileManage.getFile_url().contains("http")) {
                    fileManage.setUrl(fileManage.getFile_url());
                    fileManage.setFile_url(fileManage.getFile_url());
                } else {
                    fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
                    fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                }
//                fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
//                fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                fileManage.setName(fileManage.getFile_name());
                if (notEmpty(fileManage)) {
                    picture.add(fileManage);
                }
            }
        }
        return picture;
    }

    /**
     * create by zonglina at 2022/1/13 16:29
     * description:
     * 根据图片id，获取图片信息List接收
     *
     * @return : null
     * @param:null
     */
    public List<FileManage> selectByFileList(List<String> list) {
        List<FileManage> picture = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (String str : list) {
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                FileManage fileManage = selectById(Integer.parseInt(str));
                if (StringUtils.isNotBlank(fileManage.getFile_url()) && fileManage.getFile_url().contains("http")) {
                    fileManage.setUrl(fileManage.getFile_url());
                    fileManage.setFile_url(fileManage.getFile_url());
                } else {
                    fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
                    fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                }
//                fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
//                fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                if (notEmpty(fileManage)) {
                    picture.add(fileManage);
                }
            }
        }
        return picture;
    }

    public List<FileManage> selectByFileList2(List<Integer> list) {
        List<FileManage> picture = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (Integer str : list) {
                FileManage fileManage = selectById(str);
                if (null != fileManage) {
                    if (StringUtils.isNotBlank(fileManage.getFile_url()) && fileManage.getFile_url().contains("http")) {
                        fileManage.setUrl(fileManage.getFile_url());
                        fileManage.setFile_url(fileManage.getFile_url());
                    } else {
                        fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
                        fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                    }
//                    fileManage.setUrl(loadPicPath2 + fileManage.getFile_url());
//                    fileManage.setFile_url(loadPicPath2 + fileManage.getFile_url());
                    if (notEmpty(fileManage)) {
                        picture.add(fileManage);
                    }
                }
            }
        }
        return picture;
    }

    /**
     * 图片上传保存
     *
     * @param files
     * @return
     */
    public String uploadFiles(MultipartFile files) {
        JSONObject jsonObject = new JSONObject();
//        SysUser sysUser = sysUserService.getUser();
        //todo 文件上传
        String fileName = files.getOriginalFilename();
        String fileF = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);//获取文件后缀名称
        try {
            //转换成流对象
            InputStream file = files.getInputStream();
            //保存图片到文件夹
            String storeName = "";
            if (StringUtils.equals("mp4", fileF)) {
                storeName = fileUploadUtils.saveVideo(file);
            } else {
                storeName = fileUploadUtils.savePhoto(file);
            }

            //新增记录
//            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            long fileSize = files.getSize();
            FileManage bean = new FileManage();
            bean.setFile_name(fileName);
            bean.setFile_size(0 == fileSize ? "0" : String.valueOf(fileSize));
            bean.setFile_type(fileF);
            bean.setStore_name(storeName);
            bean.setFile_url(storeName + "." + fileF);
//        bean.setCreate_user(sysUser.getId());
            bean.setCreate_time(new Date());
//        bean.setUpdate_user(sysUser.getId());
            bean.setUpdate_time(new Date());
            bean.setIs_del("0");
            if (insert(bean) > 0) {
                jsonObject.put("id", bean.getId());
                jsonObject.put("url", bean.getFile_url());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String origName1 = files.getOriginalFilename();
//        String fileF = FilenameUtils.getExtension(origName1).toLowerCase(Locale.ENGLISH);//获取文件后缀名称
//        String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileF;
//        String file_name = origName1.substring(0, origName1.lastIndexOf(".")) + new DateTime().getMillis() + getFourRandom().replaceAll("-", "") + "." + fileF;
//        String file_name = origName1.substring(0, origName1.lastIndexOf(".")).replaceAll("-", "") + "." + fileF;
//        String file_path = "/upload/" + file_name;
//        DateFormat df = new SimpleDateFormat("/yyyy/MM/dd");
//        String url = this.filePath;
//        //生成图片路径
//        String path = url + df.format(new Date()) + "/upload/";
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String file_path = path + file_name;
//        String file_name = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileF;
//        try {
//            fileRepository.storeFileByExt2(files, file_path);
//            FileManage bean = new FileManage();
//            bean.setIs_del("0");
//            bean.setFile_url(file_path.replace(this.filePath, "").replace(this.filePath, ""));
//            bean.setFile_type("系统资源图片");
//            insert(bean);
//
//            jsonObject.put("id", bean.getId());
//            jsonObject.put("url", bean.getFile_url());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        jsonObject.put("name", origName1);
//        jsonObject.put("url", path);
//        jsonObject.put("size", files.getSize());
        return jsonObject.toJSONString();
    }


}
