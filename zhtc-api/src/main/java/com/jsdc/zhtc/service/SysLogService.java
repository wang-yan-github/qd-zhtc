package com.jsdc.zhtc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.SysLogDao;
import com.jsdc.zhtc.mapper.SysLogMapper;
import com.jsdc.zhtc.model.SysLog;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * ClassName: SysLogService
 * Description: 系统日志
 * date: 2021/12/30 10:11
 *
 * @author wp
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class SysLogService extends BaseService<SysLogDao, SysLog> {

    @Autowired
    private SysLogMapper mapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * create by wp at 2022/1/10 8:39
     * description: 查询日志内容
     *
     * @param sysLog
     * @return java.util.List<com.jsdc.zhtc.model.SysLog>
     */
    public ResultInfo getPage(SysLog sysLog, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        if (StringUtils.isNotEmpty(sysLog.getLog_user_type())) {
            String[] strArr = sysLog.getLog_user_type().split("-");
            sysLog.setCreate_user(Integer.valueOf(strArr[0]));
            sysLog.setLog_user_type(strArr[1]);
        }
        PageInfo<SysLog> pageInfo = new PageInfo<>(mapper.getPage(sysLog));
        return ResultInfo.success(pageInfo);
    }

    /**
     * 退款日志生成
     */
    public void saveLog(SysLog sysLog) {
        sysLog.setOperation_time(new Date());
        sysLog.setIs_del("0");
        sysLog.setCreate_time(new Date());
        //用户信息
        SysUser sysUser = sysUserService.getUser();
        sysLog.setCreate_user(sysUser.getId());
        sysLog.setCreate_time(new Date());
        sysLog.setUser_type("1");
        //获取request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        sysLog.setIp(request.getRemoteAddr());
        mapper.insert(sysLog);
    }

    public ResultInfo getUserList() {
        return ResultInfo.success(mapper.getUserList());
    }
}
