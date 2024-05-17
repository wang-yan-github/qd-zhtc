package com.jsdc.zhtc.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.MD5Utils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.InspectManageDao;
import com.jsdc.zhtc.mapper.InspectManageMapper;
import com.jsdc.zhtc.mapper.ParkingOrderMapper;
import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.model.SectionInspector;
import com.jsdc.zhtc.utils.FileUploadUtils;
import com.jsdc.zhtc.utils.FileUtils;
import com.jsdc.zhtc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ClassName: InspectManageService
 * Description:
 * date: 2021/12/30 10:22
 *
 * @author zln
 */
@Service
@Transactional
@SuppressWarnings("ALL")
public class InspectManageService extends BaseService<InspectManageDao, InspectManage> {
    @Autowired
    private InspectManageMapper manageMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SectionInspectorService sectionInspectorService;

    @Autowired
    private ParkingOrderMapper parkingOrderMapper;
    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Autowired
    private FileManageService fileManageService;
    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;

    /**
     * create by wp at 2022/1/13 11:08
     * description: 登录
     *
     * @param userName
     * @param password
     * @return java.lang.String
     */
    public String login(String userName, String password, String registerId) {
        LambdaQueryWrapper<InspectManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectManage::getLogin_name, userName)
                .eq(InspectManage::getLogpwd, MD5Utils.getMD5(password))
                .eq(InspectManage::getIs_del, GlobalData.ISDEL_NO);
        InspectManage inspectManage = selectOne(wrapper);

        if (null == inspectManage) {
            return null;
        }
        LambdaQueryWrapper<SectionInspector> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SectionInspector::getInspect_id, inspectManage.getId());
        wrapper1.eq(SectionInspector::getIs_del, GlobalData.ISDEL_NO);
        List<SectionInspector> list = sectionInspectorService.selectList(wrapper1);
        if (CollectionUtils.isEmpty(list)) {
            if (StringUtils.equals(inspectManage.getPersonType(), "0")) {
                return "-1";
            } else if (StringUtils.equals(inspectManage.getPersonType(), "1")) {
                return "-2";
            }
        }
        StpUtil.login(inspectManage.getLogin_name());
        StpUtil.getSession().set(inspectManage.getLogin_name(), inspectManage);
        /**
         * 登陆成功后记录该巡检人员/收费员 关联的手持机注册的极光推送注册id,
         * 之所以在登录时绑定注册id,是为防止调换手持机但是消息推送无法随之切换对象人员。
         */
        inspectManage.setRegisterId(registerId);
        updateById(inspectManage);
        return StpUtil.getTokenValue();
    }

    /**
     * create by wp at 2022/1/13 11:09
     * description: 退出
     *
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return ResultInfo.success("已退出");
        }
        return ResultInfo.error("退出失败");
    }

    /**
     * create by wp at 2022/1/13 11:39
     * description: 修改密码
     *
     * @param cypher
     * @param enSureCypher
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo editPassword(String cypher, String enSureCypher) {
        InspectManage inspectManage = getInspecter();
        if (StringUtils.equals(MD5Utils.getMD5(cypher), MD5Utils.getMD5(enSureCypher))) {
            inspectManage.setLogpwd(MD5Utils.getMD5(cypher));
            if (updateById(inspectManage) > 0) {
                return ResultInfo.success("更新密码成功");
            } else {
                return ResultInfo.error("更新密码失败");
            }
        } else {
            return ResultInfo.error("密码与确认密码不一致");
        }

    }

    public InspectManage getInspecter() {
        return (InspectManage) StpUtil.getSession().get(StpUtil.getLoginIdByToken(StpUtil.getTokenValue()).toString());
    }

    //分页查询
    public List<InspectManageVo> selectPageList(Integer pageIndex, Integer pageSize, InspectManageVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InspectManageVo> list = manageMapper.selectPageList(bean, "1");
        for (InspectManageVo a : list) {
            Integer berth_num = manageMapper.selectByGroupBerthnum("1", a.getId());
            if (null != berth_num) {
                a.setBerth_num(berth_num);
            } else {
                a.setBerth_num(0);
            }
        }
        return list;
    }

    //新增、修改保存方法
    public ResultInfo save(InspectManage bean) {
        QueryWrapper<InspectManage> queryWrapper = new QueryWrapper<InspectManage>().eq("is_del", 0).eq("job_no", bean.getJob_no());
        if (null != bean.getId()) {
            queryWrapper.ne("id", bean.getId());
        }
        Long count = manageMapper.selectCount(queryWrapper);
        QueryWrapper<InspectManage> wrapper = new QueryWrapper<InspectManage>().eq("is_del", 0).eq("login_name", bean.getLogin_name());
        if (null != bean.getId()) {
            wrapper.ne("id", bean.getId());
        }
        Long loginCount = manageMapper.selectCount(wrapper);
        if (count == 0) {
            if (loginCount == 0) {
                if (null != bean.getId()) {
                    bean.setLogpwd(MD5Utils.getMD5(bean.getLogpwd()));
                    if (updateById(bean) > 0) {
                        return ResultInfo.success("操作成功！");
                    } else {
                        return ResultInfo.error("操作失败！");
                    }
                } else {
                    bean.setIs_del(0);
                    bean.setCreate_time(new Date());
                    bean.setCreate_user(sysUserService.getUser().getId());
                    bean.setPersonType("1");
                    bean.setLogpwd(MD5Utils.getMD5(bean.getLogpwd()));
                    if (insert(bean) > 0) {
                        return ResultInfo.success("操作成功！");
                    } else {
                        return ResultInfo.error("操作失败！");
                    }
                }
            } else {
                return ResultInfo.error("登录名已存在！");
            }
        } else {
            return ResultInfo.error("工号已存在！");
        }
    }

    /**
     * create by wp at 2022/1/5 14:20
     * description: 路段巡检统计查询巡检人员
     *
     * @param areaId
     * @param streetId
     * @param roadId
     * @param name
     * @param phone
     * @param pageIndex
     * @param pageSize
     */
    public PageInfo<InspectManageVo> getPageForStatistics(Integer areaId, Integer streetId, Integer roadId, String name, String phone, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InspectManageVo> list = manageMapper.getPageForStatistics(areaId, streetId, roadId, name, phone);
        PageInfo<InspectManageVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * create by wp at 2022/1/18 13:39
     * description: 停车场统计查询收费员
     *
     * @param areaId
     * @param streetId
     * @param roadId
     * @param name
     * @param phone
     * @param pageIndex
     * @param pageSize
     */
    public PageInfo<InspectManageVo> getPageForParkStatistics(Integer areaId, Integer streetId, Integer parkId, String name, String phone, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<InspectManageVo> list = manageMapper.getPageForParkStatistics(areaId, streetId, parkId, name, phone);
        PageInfo<InspectManageVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * create by wp at 2022/1/5 14:36
     * description: 查询巡检人员绑定的路段数量
     *
     * @param inspectId
     */
    public List<Integer> getRoadCount(Integer inspectId) {
        return manageMapper.getRoadCount(inspectId);
    }

    /**
     * create by wp at 2022/1/24 10:47
     * description: 查询当前收费员可查看的所有订单
     * 可以按照是否一开发票、车牌号、时间检索
     *
     * @param status
     * @param caono
     * @param startTime
     * @param endTime
     */
    public PageInfo getparkingOrders(String status, String caono, String startTime, String endTime, Integer pageIndex, Integer pageSize) {
        InspectManage inspectManage = getInspecter();
        PageHelper.startPage(pageIndex, pageSize);
        List<ParkingOrderResVo> list = manageMapper.getParkingOrders(status, startTime, endTime, caono, inspectManage.getId());
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public ResultInfo manageParkDetail() {

        //获取当前停车场
        InspectManage inspectManage = getInspecter();
        RoadOrParkingCommentVo parkingOrder = new RoadOrParkingCommentVo();
        List<SectionInspector> sectionInspector = sectionInspectorService.selectList(new QueryWrapper<SectionInspector>().eq("inspect_id", inspectManage.getId()).eq("is_del", "0"));
        if (sectionInspector != null && sectionInspector.size() > 0) {
            parkingOrder.setPark_id(sectionInspector.get(0).getAllocated_section_id());
        } else {
            return ResultInfo.success(new PageInfo<>());
        }

        InspectManageParkVo inspectManageParkVo = manageMapper.manageParkDetail(inspectManage.getId());

        if (inspectManageParkVo.getPark_num() != null && inspectManageParkVo.getOccupy_num() != null)
            inspectManageParkVo.setFree_num(inspectManageParkVo.getPark_num() - inspectManageParkVo.getOccupy_num());

        if (inspectManageParkVo.getOccupy_num() != null && inspectManageParkVo.getPark_num() != null) {
            BigDecimal occupy_num = new BigDecimal(inspectManageParkVo.getOccupy_num());
            BigDecimal park_num = new BigDecimal(inspectManageParkVo.getPark_num());
            BigDecimal occupancy_rate = occupy_num.divide(park_num, 2, BigDecimal.ROUND_CEILING);
            BigDecimal occupancy_rate_value = occupancy_rate.multiply(BigDecimal.valueOf(100)).setScale(0);
            inspectManageParkVo.setOccupancy_rate(String.valueOf(occupancy_rate_value));
        }

        PageHelper.startPage(1, 5);
        parkingOrder.setStatus("1");
        inspectManageParkVo.setList(parkingOrderMapper.selectPageList(parkingOrder));

        return ResultInfo.success(inspectManageParkVo);
    }

    /**
     * 修改头像
     */
    public ResultInfo updPic(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        InspectManage inspecter = getInspecter();
        String base64 = jsonObject.getString("base64");
        if (Base.notEmpty(base64)) {
            String[] img = base64.split(",");
            for (int i = 0; i < img.length; i++) {
                //去除 ata:image/jpeg;base64
                if (i % 2 != 0) {
                    String storeName = fileUploadUtils.savePhoto(FileUtils.BaseToInputStream(img[i]));
                    FileManage fileManage = new FileManage();
                    fileManage.setStore_name(storeName);
                    fileManage.setFile_name(storeName);
                    fileManage.setFile_type("jpg");
                    fileManage.setCreate_time(new Date());
                    fileManage.setFile_url(storeName + ".jpg");
                    fileManage.setIs_del("0");
                    if (fileManageService.insert(fileManage) > 0) {
                        inspecter.setHead_portrait(fileManage.getFile_url());
                        updateById(inspecter);
                        inspecter.setHead_portrait(loadPicPath2 + fileManage.getFile_url());
                        return ResultInfo.success(inspecter);
                    }
                }
            }
        }
        return ResultInfo.success(inspecter);
    }
}
