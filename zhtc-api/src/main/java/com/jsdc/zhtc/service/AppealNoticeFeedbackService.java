package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.AppealNoticeFeedbackDao;
import com.jsdc.zhtc.mapper.AppealNoticeFeedbackMapper;
import com.jsdc.zhtc.model.AppealNoticeFeedback;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * ClassName: AppealNoticeFeedbackService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
public class AppealNoticeFeedbackService extends BaseService<AppealNoticeFeedbackDao, AppealNoticeFeedback> {

    @Autowired
    private AppealNoticeFeedbackMapper appealNoticeFeedbackMapper;

    /**
     * create by wp at 2022/1/14 13:57
     * description: 根据巡检员id或状态分页查询
     *
     * @param inspectId
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo getList(Integer inspectId, String status, Integer pageIndex, Integer pageSize) {
        LambdaQueryWrapper<AppealNoticeFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppealNoticeFeedback::getRp_id, inspectId);
        wrapper.eq(AppealNoticeFeedback::getIs_del, GlobalData.ISDEL_NO);
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq(AppealNoticeFeedback::getStatus, status);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<AppealNoticeFeedback> list = selectList(wrapper);
        PageInfo pageInfo = new PageInfo(list);
        return ResultInfo.success(pageInfo);
    }


    /**
     * create by wp at 2022/1/14 14:17
     * description: 反馈
     *
     * @param appealNoticeFeedback
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(AppealNoticeFeedback appealNoticeFeedback) {
        appealNoticeFeedback.setUpdate_time(new Date());
        appealNoticeFeedback.setStatus(GlobalData.FEEDBACK);
        if (updateById(appealNoticeFeedback) > 0) {
            return ResultInfo.success("反馈成功");
        } else {
            return ResultInfo.error("反馈失败");
        }
    }


}
