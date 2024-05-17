package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.InspectFeedbackDao;
import com.jsdc.zhtc.mapper.InspectFeedbackMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.utils.FileUploadUtils;
import com.jsdc.zhtc.vo.InspectFeedbackVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * ClassName: InspectFeedbackService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class InspectFeedbackService extends BaseService<InspectFeedbackDao, InspectFeedback> {
    @Autowired
    private InspectFeedbackMapper mapper;
    @Autowired
    private InspectManageService manageService;
    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Autowired
    private FileManageService fileService;
    @Autowired
    private InspectFeedbackPictureService pictureService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * create by zonglina at 2021/12/31 15:25
     * description:分页查询
     *
     * @return : null
     * @param:null
     */
    public List<InspectFeedbackVo> selectPageList(Integer pageIndex, Integer pageSize, InspectFeedbackVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InspectFeedbackVo> list = mapper.selectPageList(bean, "1");
        list.forEach(a -> {
            HashMap<String, SysDict> feedback_type = DcCacheDataUtil.getMapDicts("feedback_type");
            if (null != a.getFeedback_type()) {
                a.setFeedback_type(feedback_type.get(a.getFeedback_type()).getLabel());
            }
        });
        return list;
    }

    /**
     * 我要上报-上报记录 路段
     *
     * @param pageIndex
     * @param pageSize
     * @param parking_type 路段|停车场类型ss
     * @param bean
     * @return
     */
    public List<InspectFeedbackVo> selectPosPageList(Integer pageIndex, Integer pageSize, String parking_type, InspectFeedbackVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InspectFeedbackVo> list = mapper.selectPageList(bean, parking_type);
        list.forEach(a -> {
            HashMap<String, SysDict> feedback_type = DcCacheDataUtil.getMapDicts("feedback_type");
            if (null != a.getFeedback_type()) {
                a.setFeedback_type(feedback_type.get(a.getFeedback_type()) == null ? "" : feedback_type.get(a.getFeedback_type()).getLabel());
            }
        });
        return list;
    }

    /**
     * 我要上报-上报记录详情 路段
     *
     * @param parking_type 路段|停车场类型ss
     * @param bean
     * @return
     */
    public InspectFeedbackVo selectInspectFeedback(String parking_type, InspectFeedbackVo bean) {
        InspectFeedbackVo vo = mapper.selectInspectFeedback(bean, parking_type);
        HashMap<String, SysDict> feedback_type = DcCacheDataUtil.getMapDicts("feedback_type");
        if (null != vo.getFeedback_type()) {
            vo.setFeedback_type(feedback_type.get(vo.getFeedback_type()).getLabel());
        }
        return vo;
    }

    /**
     * create by zonglina at 2021/12/31 15:25
     * description:详情功能
     *
     * @return : null
     * @param:null
     */
    public ResultInfo details(Integer id) {
        InspectFeedbackVo vo = mapper.selectByFeedbackId(id);
        HashMap<String, SysDict> feedback_type = DcCacheDataUtil.getMapDicts("feedback_type");
        if (null != vo.getFeedback_type()) {
            vo.setFeedback_type(feedback_type.get(vo.getFeedback_type()) == null ? "" : feedback_type.get(vo.getFeedback_type()).getLabel());
        }
        //查询反馈图片
        List<FileManage> fileList = fileManageService.selectByFileList(pictureService.selectByInspectFeedbackPid(id));
        vo.setFileList(fileList);
        return ResultInfo.success(vo);
    }

    /**
     * create by zonglina at 2022/1/13 17:26
     * description: 反馈回复
     *
     * @return : null
     * @param:null
     */
    public ResultInfo feedbackContent(InspectFeedback bean) {
        bean.setCreate_time(new Date());
        bean.setCreate_user(sysUserService.getUser().getId());
        bean.setFkstate(1);
        if (updateById(bean) > 0) {
            InspectFeedback inspectFeedback = selectById(bean.getId());

            return ResultInfo.success("反馈成功！");
        } else {
            return ResultInfo.error("反馈失败");
        }
    }

    /**
     * create by wp at 2021/12/31 15:25
     * description:反馈功能
     *
     * @return : null
     * @param:null
     */
    public ResultInfo feedback(MultipartFile[] files, InspectFeedback bean) {
        InspectManage inspectManage = manageService.getInspecter();
        List<Integer> fileIds = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            //todo 文件上传
            String fileName = multipartFile.getOriginalFilename();
            String storeName = fileUploadUtils.savePhoto(multipartFile);
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            long fileSize = multipartFile.getSize();
            FileManage fileManage = new FileManage();
            fileManage.setFile_name(fileName);
            fileManage.setFile_size(0 == fileSize ? "0" : String.valueOf(fileSize));
            fileManage.setFile_type(suffix);
            fileManage.setStore_name(storeName);
            fileManage.setCreate_user(inspectManage.getId());
            fileManage.setCreate_time(new Date());
            fileManage.setUpdate_user(inspectManage.getId());
            fileManage.setUpdate_time(new Date());
            if (fileService.insert(fileManage) > 0) {
                fileIds.add(fileManage.getId());
            }
        }
        bean.setFeedback_time(new Date());
        bean.setCreate_user(inspectManage.getId());
        bean.setCreate_time(new Date());
        bean.setUpdate_user(inspectManage.getId());
        bean.setUpdate_time(new Date());
        bean.setFkstate(0);
        if (insert(bean) > 0) {
            for (Integer id : fileIds) {
                int feedbackId = bean.getId();
                InspectFeedbackPicture inspectFeedbackPicture = new InspectFeedbackPicture();
                inspectFeedbackPicture.setInspect_feedback_id(feedbackId);
                inspectFeedbackPicture.setPicture_id(id);
                inspectFeedbackPicture.setIs_del(GlobalData.ISDEL_NO);
                inspectFeedbackPicture.setCreate_time(new Date());
                pictureService.insert(inspectFeedbackPicture);

            }
            return ResultInfo.success("反馈成功");
        } else {
            return ResultInfo.success("反馈失败");
        }
    }

    /**
     * create by zonglina at 2021/12/31 15:28
     * description:删除
     *
     * @return : null
     * @param:null
     */
    public ResultInfo delete(InspectFeedback bean) {
        bean.setIs_del(1);
        if (updateById(bean) > 0) {
            return ResultInfo.success("删除成功！");
        } else {
            return ResultInfo.error("删除失败！");
        }
    }

    public Integer census() {
        QueryWrapper<InspectFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fkstate", "0");
        queryWrapper.eq("is_del", 0);

        return selectList(queryWrapper).size();
    }
}
