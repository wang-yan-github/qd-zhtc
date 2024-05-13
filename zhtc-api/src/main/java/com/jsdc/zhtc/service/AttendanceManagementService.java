package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.AttendanceManagementDao;
import com.jsdc.zhtc.mapper.AttendanceManagementMapper;
import com.jsdc.zhtc.model.AttendanceManagement;
import com.jsdc.zhtc.model.AttendanceManagementPic;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.model.SectionInspector;
import com.jsdc.zhtc.utils.FileUploadUtils;
import com.jsdc.zhtc.vo.ResultInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;

/**
 * 考勤管理
 *
 * @Author thr
 * @create 2022-08-24 14:08:08
 */
@Service
@Transactional
public class AttendanceManagementService extends BaseService<AttendanceManagementDao, AttendanceManagement> {

    @Autowired
    private AttendanceManagementMapper attendanceManagementMapper;
    @Autowired
    private InspectManageService inspectManageService;
    @Autowired
    private SectionInspectorService sectionInspectorService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Autowired
    private AttendanceManagementPicService attendanceManagementPicService;

    public PageInfo<AttendanceManagement> toList(Integer pageIndex, Integer pageSize, AttendanceManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize);
        List<AttendanceManagement> attendanceManagementVos = getList(beanParam);
        attendanceManagementVos.forEach(x -> {
            //上班关联图片
            List<AttendanceManagementPic> rechargeManagementPicList = attendanceManagementPicService.selectList(Wrappers.<AttendanceManagementPic>lambdaQuery().
                    eq(AttendanceManagementPic::getAttendance_management_id, x.getId()).eq(AttendanceManagementPic::getIs_del, "0")
                    .eq(AttendanceManagementPic::getType, "1"));
            if (!CollectionUtils.isEmpty(rechargeManagementPicList)) {
                List<Integer> ids = Collections.singletonList(rechargeManagementPicList.get(rechargeManagementPicList.size() - 1).getPicture_id());
                x.setSbFileList(fileManageService.selectByFileList2(ids));
            }

            //下班关联图片
            List<AttendanceManagementPic> rechargeManagementPicList2 = attendanceManagementPicService.selectList(Wrappers.<AttendanceManagementPic>lambdaQuery().
                    eq(AttendanceManagementPic::getAttendance_management_id, x.getId()).eq(AttendanceManagementPic::getIs_del, "0")
                    .eq(AttendanceManagementPic::getType, "2"));
            if (!CollectionUtils.isEmpty(rechargeManagementPicList2)) {
                List<Integer> ids2 = Collections.singletonList(rechargeManagementPicList2.get(rechargeManagementPicList2.size() - 1).getPicture_id());
                x.setXbFileList(fileManageService.selectByFileList2(ids2));
            }
        });


        PageInfo<AttendanceManagement> page = new PageInfo<>(attendanceManagementVos);

        return page;
    }

    public List<AttendanceManagement> getList(AttendanceManagement beanParam) {
        List<AttendanceManagement> attendanceManagementVos = attendanceManagementMapper.toList(beanParam);
        attendanceManagementVos.forEach(a -> {
            if (a.getType().equals("0")) {
                //路段巡检员关联
                List<SectionInspector> sectionInspectorList = sectionInspectorService.selectByRoadOrParkList("0", a.getCreate_user());
                StringBuilder sb = new StringBuilder();
                sectionInspectorList.forEach(b -> {
                    if (StringUtils.isNotEmpty(b.getRoad_name())) {
                        sb.append(",").append(b.getRoad_name());
                    }
                });
                a.setRoadParkNames(CollectionUtils.isEmpty(sectionInspectorList) ? "" : sb.toString().substring(1));
            } else {
                //停车场收费员关联
                List<SectionInspector> sectionInspectorList = sectionInspectorService.selectByRoadOrParkList("1", a.getCreate_user());
                StringBuilder sb = new StringBuilder();
                sectionInspectorList.forEach(b -> {
                    if (StringUtils.isNotEmpty(b.getRoad_name())) {
                        sb.append(",").append(b.getRoad_name());
                    }
                });
                a.setRoadParkNames(CollectionUtils.isEmpty(sectionInspectorList) ? "" : sb.toString().substring(1));
            }

        });
        return attendanceManagementVos;
    }

    /**
     * 考勤统计
     */
    public PageInfo<AttendanceManagement> toPageReport(Integer pageIndex, Integer pageSize, AttendanceManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize);
        List<AttendanceManagement> attendanceManagementVos = toReport(beanParam);
        PageInfo<AttendanceManagement> page = new PageInfo<>(attendanceManagementVos);
        return page;
    }

    public List<AttendanceManagement> toReport(AttendanceManagement beanParam) {
        List<AttendanceManagement> attendanceManagementVos = attendanceManagementMapper.toReport(beanParam);
        return attendanceManagementVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<AttendanceManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        AttendanceManagement attendanceManagement = selectOne(queryWrapper);
        InspectManage inspectManage = inspectManageService.selectById(attendanceManagement.getCreate_user());
        attendanceManagement.setUserName(inspectManage.getName());
        return ResultInfo.success(attendanceManagement);
    }


    /**
     * 定时任务
     * 每日0时生成打卡记录
     */
    public void onCreateRecord() {
        //修改昨日未打卡的记录未旷工状态
        QueryWrapper<AttendanceManagement> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.eq("CONVERT(varchar, create_time, 23)", new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        queryWrapper4.eq("is_del", "0");
        queryWrapper4.isNull("start_time").or().isNull("end_time");
        List<AttendanceManagement> list2 = selectList(queryWrapper4);
        if (notEmpty(list2) || list2.size() > 0) {
            list2.forEach(a -> {
//                if (empty(a.getStart_time()) || empty(a.getEnd_time())) {
                // 是否旷工 0否 1是
                a.setIs_absenteeism("1");
                updateById(a);
//                }
            });
        }

        //查询今日打卡的记录
        QueryWrapper<AttendanceManagement> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("CONVERT(varchar, create_time, 23)", new DateTime().toString("yyyy-MM-dd"));
        queryWrapper3.eq("is_del", "0");
        List<AttendanceManagement> list = selectList(queryWrapper3);

        //巡检员、收费员列表
        QueryWrapper<InspectManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        List<InspectManage> inspectManageList = inspectManageService.selectList(queryWrapper);
        inspectManageList.forEach(a -> {
            //路段巡检员、停车场收费员关联
            QueryWrapper<SectionInspector> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select(" top 1 * ");
            queryWrapper2.eq("inspect_id", a.getId());
            queryWrapper2.eq("is_del", "0");
            queryWrapper2.orderByDesc("id");
            SectionInspector sectionInspector = sectionInspectorService.selectOne(queryWrapper2);
            if (notEmpty(sectionInspector)) {
                if (empty(list) || list.size() == 0) {
                    AttendanceManagement bean = new AttendanceManagement();
                    //上班打卡时间 分配
                    bean.setFp_start_time(sectionInspector.getBegin_time());
                    //下班打卡时间 分配
                    bean.setFp_end_time(sectionInspector.getEnd_time());
                    // 类型 0 路段 1停车场
                    bean.setType(a.getPersonType());
                    // 删除状态
                    bean.setIs_del(String.valueOf(0));
                    // 创建时间
                    bean.setCreate_time(new Date());
                    // 创建者
                    bean.setCreate_user(a.getId());
                    bean.setIs_absenteeism("0");
                    bean.setIs_late("0");
                    bean.setIs_leave_early("0");
                    insert(bean);
                }
            }
        });
    }

    /**
     * 导出考勤记录
     */
    public ResultInfo downExcel(AttendanceManagement beanParam, HttpServletResponse response) {

        List<AttendanceManagement> list = getList(beanParam);
        list.forEach(a -> {
            a.setIs_leave_early(a.getIs_leave_early().equals("0") ? "否" : "是");
            a.setIs_late(a.getIs_late().equals("0") ? "否" : "是");
            a.setIs_absenteeism(a.getIs_absenteeism().equals("0") ? "否" : "是");
        });

        ExcelWriter writer = ExcelUtil.getWriter();
        if (beanParam.getType().equals("1")) {
            writer.addHeaderAlias("userName", "收费员");
            writer.addHeaderAlias("roadParkNames", "收费停车场");
        } else {
            writer.addHeaderAlias("userName", "巡检员");
            writer.addHeaderAlias("roadParkNames", "收费路段");
        }
        writer.addHeaderAlias("create_time", "考勤日期");
        writer.addHeaderAlias("start_time", "上班打卡时间");
        writer.addHeaderAlias("end_time", "下班打卡时间");
        writer.addHeaderAlias("is_late", "是否迟到");
        writer.addHeaderAlias("is_leave_early", "是否早退");
        writer.addHeaderAlias("is_absenteeism", "是否旷工");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");

        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("123456.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultInfo.success(null);
    }


    /**
     * 导出考勤记录
     */
    public ResultInfo reportDownExcel(AttendanceManagement beanParam, HttpServletResponse response) {
        List<AttendanceManagement> list = toReport(beanParam);

        ExcelWriter writer = ExcelUtil.getWriter();
        if (beanParam.getType().equals("1")) {
            writer.addHeaderAlias("userName", "收费员");
        } else {
            writer.addHeaderAlias("userName", "巡检员");
        }
        writer.addHeaderAlias("days", "考勤天数");
        writer.addHeaderAlias("lateDays", "早退天数");
        writer.addHeaderAlias("leaveEarlyDays", "迟到天数");
        writer.addHeaderAlias("absenteeismDays", "旷工天数");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");

        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("123456.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultInfo.success(null);
    }
}
