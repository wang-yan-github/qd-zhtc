package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.dao.OperateCarnoDao;
import com.jsdc.zhtc.mapper.OperateCarnoMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.BatchAddWhiteCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;

/**
 * ClassName: OperateCarnoService
 * Description:
 * date: 2021/12/30 10:22
 * 车牌管理表
 *
 * @author zln
 */
@Service
@Transactional
public class OperateCarnoService extends BaseService<OperateCarnoDao, OperateCarno> {

    @Autowired
    private OperateCarnoMapper mapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CarnoBindRecordService recordService;
    @Autowired
    private WhiteCarnoParkService whiteCarnoParkService;
    @Autowired
    private FileManageService fileManageService;

    @Value("${jsdc.loadPicPath2}")
    public String loadPicPath2;


    public List<OperateCarnoVo> toList(OperateCarnoVo operateCarnoVo) {


        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);
        for (OperateCarnoVo ov : operateCarnoVos) {
            CarnoBindRecord carnoBindRecord = recordService.getRecord(ov.getId(), operateCarnoVo.getMember_id());
            if (null != carnoBindRecord) {
                ov.setBindTime(carnoBindRecord.getBind_date());
            }
        }

        return operateCarnoVos;
    }

    /**
     * 描 述： TODO( 查询用户所有车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public List<OperateCarno> getMemberAll(OperateCarno bean) {

        LambdaQueryWrapper<OperateCarno> wrapper = new LambdaQueryWrapper<>();
        if (bean.getMember_id() != null)
            wrapper.eq(OperateCarno::getMember_id, bean.getMember_id());

        wrapper.eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO);

        List<OperateCarno> lists = this.selectList(wrapper);
        return lists;
    }

    /**
     * 车牌列表数据
     *
     * @param pageIndex
     * @param pageSize
     * @param operateCarnoVo
     * @return
     */
    public PageInfo<OperateCarnoVo> toList(Integer pageIndex, Integer pageSize, OperateCarnoVo operateCarnoVo) {
        PageHelper.startPage(pageIndex, pageSize);
        SysUser sysUser = sysUserService.getUser();
        if ("1".equals(sysUser.getUser_type())) {
            List<Integer> carnoIds = mapper.getParkCarno(sysUser.getPark_id());
            operateCarnoVo.setCarnoIds(carnoIds);
        }
        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        //白名单类型
        HashMap<String, SysDict> dictwlMap = DcCacheDataUtil.getMapDicts("whitelist_type");

        operateCarnoVos.forEach(x -> {
            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());

            if ("3".equals(x.getRoster_type()) && StringUtils.isNotEmpty(x.getWhitelist_type())) {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel() + "[" + dictwlMap.get(x.getWhitelist_type()).getLabel() + "]");
            } else {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
            }
            Integer carnoId = x.getId();
            List<String> parkNames = mapper.getWhiteCarnoParks(carnoId);
            x.setParkNames(parkNames);
        });
        PageInfo<OperateCarnoVo> pageInfo = new PageInfo<>(operateCarnoVos);

        return pageInfo;
    }

    public void export(OperateCarnoVo operateCarnoVo, HttpServletResponse response) {
        SysUser sysUser = sysUserService.getUser();
        if ("1".equals(sysUser.getUser_type())) {
            List<Integer> carnoIds = mapper.getParkCarno(sysUser.getPark_id());
            operateCarnoVo.setCarnoIds(carnoIds);
        }
        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        //白名单类型
        HashMap<String, SysDict> dictwlMap = DcCacheDataUtil.getMapDicts("whitelist_type");

        operateCarnoVos.forEach(x -> {
            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());

            if ("3".equals(x.getRoster_type()) && StringUtils.isNotEmpty(x.getWhitelist_type())) {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel() + "[" + dictwlMap.get(x.getWhitelist_type()).getLabel() + "]");
            } else {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
            }
            Integer carnoId = x.getId();
            List<String> parkNames = mapper.getWhiteCarnoParks(carnoId);
            x.setParkNames(parkNames);

            if (notEmpty(x.getMember_id())) {
                x.setMemberName("是");
            } else {
                x.setMemberName("否");
            }
        });

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("phone2", "手机号码");
        writer.addHeaderAlias("company_name", "公司名称");
        writer.addHeaderAlias("car_no", "车牌号");
        writer.addHeaderAlias("carTypeName", "车牌样式");
        writer.addHeaderAlias("memberName", "会员车牌（是/否）");
        writer.addHeaderAlias("bindName", "绑定账号");
        writer.addHeaderAlias("phone", "绑定手机号");
        writer.addHeaderAlias("bind_date", "绑定时间");
        writer.addHeaderAlias("rosterTypeName", "名单类型");
        writer.addHeaderAlias("cut_off_date", "企业(免税)车免费截止时间");
        writer.addHeaderAlias("free_time_start", "内部车免费开始时间");
        writer.addHeaderAlias("free_time_end", "内部车免费截止时间");
        writer.addHeaderAlias("parkNames", "停车场");
        writer.addHeaderAlias("roadNames", "路内");
        writer.addHeaderAlias("userName", "录入人");
        writer.addHeaderAlias("reason", "理由说明");

        writer.setOnlyAlias(true);
        writer.write(operateCarnoVos, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("车牌管理.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解绑操作
     *
     * @param carnoId
     * @return
     */
    public ResultInfo relieveBind(Integer carnoId) {
        //获取当前车牌记录
        OperateCarno operateCarno = this.selectById(carnoId);
        if (operateCarno != null && operateCarno.getMember_id() != null) {
            //生成解绑记录
            CarnoBindRecord carnoBindRecord = new CarnoBindRecord();
            carnoBindRecord.setCar_id(operateCarno.getId());
            carnoBindRecord.setBind_type("1");//解绑状态
            carnoBindRecord.setMember_id(operateCarno.getMember_id());
            carnoBindRecord.setBind_date(new Date());
            carnoBindRecord.setCreate_time(new Date());
            if (carnoBindRecord.insert()) {
                operateCarno.setUpdate_time(new Date());
                operateCarno.setMember_id(null);
                operateCarno.setBind_date(null);
                operateCarno.updateById();
                return ResultInfo.success();
            } else {
                return ResultInfo.error("解绑失败！");
            }
        } else {
            return ResultInfo.error("无绑定信息，无需解绑！");
        }

    }

    /**
     * create by wp at 2022/1/12 14:45
     * description: 订单绑定个人用户
     * 若车牌已绑定用户，需先解绑
     *
     * @param carId
     * @param memberId
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo bindCarPerson(Integer carId, Integer memberId) {
        SysUser sysUser = sysUserService.getUser();
        OperateCarno operateCarno = selectById(carId);
        if (null != operateCarno.getMember_id()) {
            return ResultInfo.error("该车牌已绑定用户，请先解绑");
        }
        operateCarno.setBind_date(new Date());
        operateCarno.setMember_id(memberId);
        operateCarno.setUpdate_time(new Date());
        operateCarno.setCreate_time(new Date());
        operateCarno.setUpdate_user(sysUser.getId());
        operateCarno.setCreate_user(sysUser.getId());
        if (updateById(operateCarno) > 0) {
            CarnoBindRecord record = new CarnoBindRecord();
            record.setCar_id(carId);
            record.setBind_type(GlobalData.CARNO_BIND);
            record.setMember_id(memberId);
            record.setBind_date(new Date());
            record.setCreate_time(new Date());
            record.setCreate_user(sysUser.getId());
            record.setUpdate_time(new Date());
            record.setUpdate_user(sysUser.getId());
            record.setIs_del(0);
            if (recordService.insert(record) > 0) {
                return ResultInfo.success("绑定成功");
            } else {
                return ResultInfo.error("绑定失败");
            }
        } else {
            return ResultInfo.error("绑定失败");
        }
    }

    /**
     * pc端
     * 新增、修改车牌保存方法
     */
    public ResultInfo save(OperateCarno carno) {
        boolean b = TimeUtils.carPlate(carno.getCar_no());
        if (b) {
            if (empty(carno.getId())) {
                //新增车牌
                QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper<>();
                Long count = mapper.selectCount(queryWrapper.eq("car_no", carno.getCar_no()).eq("car_type", carno.getCar_type()).eq("is_del", "0"));
                Long count_no = null;
                if (GlobalData.ROSTER_TYPE_DISABLED.equals(carno.getRoster_type())) {
                    count_no = mapper.selectCount(queryWrapper.eq("deformity_cert", carno.getDeformity_cert()));
                }
                if (count > 0) {
                    return ResultInfo.error("车牌信息已存在！");
                } else if (null != count_no && count_no > 0) {
                    return ResultInfo.error("残疾证编号已存在！");
                } else {
                    carno.setCar_no(carno.getCar_no().toUpperCase());
                    carno.setCreate_time(new Date());
                    carno.setCreate_user(sysUserService.getUser().getId());
                    carno.setIs_del("0");

                    //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
                    if (carno.getRoster_type().equals("3")) {
                        // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                        if (carno.getWhitelist_type().equals("0")) {
                            // 白名单免费类型 1永久 2期限
                            if (Base.notEmpty(carno.getWhite_time_type()) && carno.getWhite_time_type().equals("2")
                                    && Base.notEmpty(carno.getFree_time_start())) {
                                //date1大于date2返回1
                                if (carno.getFree_time_start().compareTo(new DateTime().toDate()) == 1) {
                                    // 免费类型为期限，判断免费开始时间大于今日，若等于则改为普通名单
                                    carno.setRoster_type("1");
                                }

                                if (carno.getFree_time_end().compareTo(new DateTime().toDate()) < 1) {
                                    // 免费类型为期限，判断免费结束时间小于等于今日，若等于则改为普通名单
                                    carno.setRoster_type("1");
//                                carno.setFree_time_start(null);
//                                carno.setFree_time_end(null);
//                                carno.setWhite_time_type("1");
                                }
                            } else {
                                carno.setFree_time_start(null);
                                carno.setFree_time_end(null);
                            }
                        } else {
                            carno.setFree_time_start(null);
                            carno.setFree_time_end(null);
                        }
                    } else {
                        carno.setFree_time_start(null);
                        carno.setFree_time_end(null);
                    }

                    if (insert(carno) > 0) {
                        if (StringUtils.equals(carno.getRoster_type(), "3")) {
                            whiteCarnoParkService.delete(Wrappers.<WhiteCarnoPark>lambdaQuery().eq(WhiteCarnoPark::getCarno_id, carno.getId()));
                            if (CollectionUtils.isNotEmpty(carno.getParkIds())) {
                                for (Integer id : carno.getParkIds()) {
                                    WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                                    whiteCarnoPark.setCarno_id(carno.getId());
                                    whiteCarnoPark.setPark_id(id);
                                    whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_PLAT);
                                    whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                                    whiteCarnoParkService.insert(whiteCarnoPark);
                                }
                            }
                            if (CollectionUtils.isNotEmpty(carno.getRoadIds())) {
                                for (Integer id : carno.getRoadIds()) {
                                    WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                                    whiteCarnoPark.setCarno_id(carno.getId());
                                    whiteCarnoPark.setPark_id(id);
                                    whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_ROAD);
                                    whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                                    whiteCarnoParkService.insert(whiteCarnoPark);
                                }
                            }

                        }
                        return ResultInfo.success("车牌录入成功！");
                    } else {
                        return ResultInfo.error("车牌录入失败！");
                    }
                }
            } else {
                //修改车牌
                QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper<>();
                Long count = mapper.selectCount(queryWrapper.eq("car_no", carno.getCar_no())
                        .eq("car_type", carno.getCar_type())
                        .eq("is_del", "0")
                        .ne("id", carno.getId()));
                Long count_no = null;
                if (GlobalData.ROSTER_TYPE_DISABLED.equals(carno.getRoster_type())) {
                    count_no = mapper.selectCount(queryWrapper.eq("deformity_cert", carno.getDeformity_cert()));
                }
                if (count > 0) {
                    return ResultInfo.error("车牌信息已存在！");
                } else if (null != count_no && count_no > 0) {
                    return ResultInfo.error("残疾证编号已存在！");
                } else {
                    carno.setCar_no(carno.getCar_no().toUpperCase());
                    carno.setUpdate_time(new Date());
                    carno.setUpdate_user(sysUserService.getUser().getId());

                    //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
                    if (carno.getRoster_type().equals("3")) {
                        // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                        if (carno.getWhitelist_type().equals("0")) {
                            // 白名单免费类型 1永久 2期限
                            if (Base.notEmpty(carno.getWhite_time_type()) && carno.getWhite_time_type().equals("2")
                                    && Base.notEmpty(carno.getFree_time_start())) {
                                //date1大于date2返回1
                                if (carno.getFree_time_start().compareTo(new DateTime().toDate()) == 1) {
                                    // 免费类型为期限，判断免费开始时间大于今日，若等于则改为普通名单
                                    carno.setRoster_type("1");
                                }

                                if (carno.getFree_time_end().compareTo(new DateTime().toDate()) < 1) {
                                    // 免费类型为期限，判断免费结束时间小于等于今日，若等于则改为普通名单
                                    carno.setRoster_type("1");
//                                carno.setFree_time_start(null);
//                                carno.setFree_time_end(null);
//                                carno.setWhite_time_type("1");
                                }
                            } else {
                                carno.setFree_time_start(null);
                                carno.setFree_time_end(null);
                            }
                        } else {
                            carno.setFree_time_start(null);
                            carno.setFree_time_end(null);
                        }
                    } else {
                        carno.setFree_time_start(null);
                        carno.setFree_time_end(null);
                    }

                    if (updateById(carno) > 0) {
                        if (StringUtils.equals(carno.getRoster_type(), "3")) {
                            whiteCarnoParkService.delete(Wrappers.<WhiteCarnoPark>lambdaQuery().eq(WhiteCarnoPark::getCarno_id, carno.getId()));
                            if (CollectionUtils.isNotEmpty(carno.getParkIds())) {
                                for (Integer id : carno.getParkIds()) {
                                    WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                                    whiteCarnoPark.setCarno_id(carno.getId());
                                    whiteCarnoPark.setPark_id(id);
                                    whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_PLAT);
                                    whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                                    whiteCarnoParkService.insert(whiteCarnoPark);
                                }
                            }
                            if (CollectionUtils.isNotEmpty(carno.getRoadIds())) {
                                for (Integer id : carno.getRoadIds()) {
                                    WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                                    whiteCarnoPark.setCarno_id(carno.getId());
                                    whiteCarnoPark.setPark_id(id);
                                    whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_ROAD);
                                    whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                                    whiteCarnoParkService.insert(whiteCarnoPark);
                                }
                            }

                        }
                        //如果新增成功，判断数据是否来自交控，如果是 删除交控表数据
//                        QueryWrapper<OperateCarnoTraffic> queryWrapper1 = new QueryWrapper<>();
//                        queryWrapper1.eq("car_no", carno.getCar_no());
//                        queryWrapper1.eq("is_del", "0");
//                        OperateCarnoTraffic operateCarnoTraffic = operateCarnoTrafficService.selectOne(queryWrapper1);
//                        if (null != operateCarnoTraffic) {
//                            operateCarnoTraffic.setIs_del("1");
//                            operateCarnoTrafficService.updateById(operateCarnoTraffic);
//                        }
                        return ResultInfo.success("车牌修改成功！");
                    } else {
                        return ResultInfo.error("车牌修改失败！");
                    }
                }
            }
        } else {
            return ResultInfo.error("车牌号格式不正确");
        }

    }

    public ResultInfo edit(OperateCarno operateCarno) {
        OperateCarno oc = selectById(operateCarno.getId());
        if (oc != null) {
            oc.setDeformity_picture_id(operateCarno.getDeformity_picture_id());
            if (operateCarno.getDeformity_picture_id() != null) {
                oc.setDeformity_picture_id(operateCarno.getDeformity_picture_id());
            }
            oc.setName(operateCarno.getName());
            oc.setPhone(operateCarno.getPhone());
            oc.setCompany_name(operateCarno.getCompany_name());
            oc.setDeformity_cert(operateCarno.getDeformity_cert());

            oc.setCut_off_date(operateCarno.getCut_off_date());

            oc.setRoster_type(operateCarno.getRoster_type());
            oc.setReason(operateCarno.getReason());
            oc.setWhitelist_type(operateCarno.getWhitelist_type());

            oc.setWhite_time_type("");
            oc.setFree_time_start(null);
            oc.setFree_time_end(null);

            //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
            if (operateCarno.getRoster_type().equals("3")) {

                oc.setWhite_time_type(operateCarno.getWhite_time_type());

                // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                if (operateCarno.getWhitelist_type().equals("0")) {
                    // 白名单免费类型 1永久 2期限
                    if (Base.notEmpty(operateCarno.getWhite_time_type()) && operateCarno.getWhite_time_type().equals("2")
                            && Base.notEmpty(operateCarno.getFree_time_start())) {

                        oc.setFree_time_start(operateCarno.getFree_time_start());
                        oc.setFree_time_end(operateCarno.getFree_time_end());

                        //date1大于date2返回1
                        if (operateCarno.getFree_time_start().compareTo(new DateTime().toDate()) == 1) {
                            // 免费类型为期限，判断免费开始时间大于今日，若等于则改为普通名单
                            oc.setRoster_type("1");
                        }

                        if (operateCarno.getFree_time_end().compareTo(new DateTime().toDate()) < 1) {
                            // 免费类型为期限，判断免费结束时间小于等于今日，若等于则改为普通名单
                            oc.setRoster_type("1");
//                                oc.setFree_time_start(null);
//                                oc.setFree_time_end(null);
//                                oc.setWhite_time_type("1");
                        }
                    }
                }
            }

            oc.updateById();

            whiteCarnoParkService.delete(Wrappers.<WhiteCarnoPark>lambdaQuery().eq(WhiteCarnoPark::getCarno_id, operateCarno.getId()));
            //如果车牌类型不是白名单则删除白名单与路段或停车场的关联关系
            if (StringUtils.equals(operateCarno.getRoster_type(), "3")) {
                if (CollectionUtils.isNotEmpty(operateCarno.getParkIds())) {
                    for (Integer id : operateCarno.getParkIds()) {
                        WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                        whiteCarnoPark.setCarno_id(operateCarno.getId());
                        whiteCarnoPark.setPark_id(id);
                        whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_PLAT);
                        whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                        whiteCarnoParkService.insert(whiteCarnoPark);
                    }
                }
                if (CollectionUtils.isNotEmpty(operateCarno.getRoadIds())) {
                    for (Integer id : operateCarno.getRoadIds()) {
                        WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                        whiteCarnoPark.setCarno_id(operateCarno.getId());
                        whiteCarnoPark.setPark_id(id);
                        whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_ROAD);
                        whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                        whiteCarnoParkService.insert(whiteCarnoPark);
                    }
                }
            }
            String roster_type = "";
            //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
            switch (operateCarno.getRoster_type()) {
                case "1":
                    roster_type = "普通名单";
                    break;
                case "2":
                    roster_type = "黑名单";
                    break;
                case "3":
                    roster_type = "白名单";
                    // 白名单类型 0：内部车 1：企业(免税)车
                    switch (operateCarno.getWhitelist_type()) {
                        case "0":
                            roster_type += "[内部车]";
                            break;
                        case "1":
                            roster_type += "[企业(免税)车]";
                            break;
                        case "2":
                            roster_type += "[救护车]";
                            break;
                        case "3":
                            roster_type += "[军用车]";
                            break;
                    }
                    break;
                case "4":
                    roster_type = "残疾人车辆";
                    break;
            }
            String logMsg = "车牌号为" + oc.getCar_no() + "的车牌,已加" + roster_type;
            return ResultInfo.success("编辑成功", logMsg);
        } else {
            return ResultInfo.error("无车牌信息！");
        }
    }

    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    public ResultInfo selectAll(PageVo<OperateCarno> data) {

        OperateCarno bean = data.getBean();

        QueryWrapper<OperateCarno> wrapper = new QueryWrapper<>();

        if (bean != null) {

            if (bean.getIs_del() != null)
                wrapper.eq("is_del", bean.getIs_del());
        }
        wrapper.orderByDesc("id");

        // 判断是否分页查询
        if (data.getPageSize() != null) {
            Page<OperateCarno> page = new Page<>(data.getPageNum(), data.getPageSize());
            IPage<OperateCarno> pageData = selectPage(page, wrapper);

            if (pageData.getRecords().isEmpty())
                pageData.setRecords(new ArrayList<OperateCarno>());

            return ResultInfo.success(pageData);
        } else {
            List<OperateCarno> list = this.selectList(wrapper);
            return ResultInfo.success(list);
        }
    }


    /**
     * 描 述： TODO(根据车牌查询信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param carNo
     * @return {@link OperateCarno}
     */
    public OperateCarno findOperateCarno(String carNo, String car_type) {
        LambdaQueryWrapper<OperateCarno> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OperateCarno::getCar_no, carNo);
        if (Base.notEmpty(car_type)) {
            wrapper.eq(OperateCarno::getCar_type, car_type);
        }
        wrapper.eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO);
        return this.selectOne(wrapper);
    }

    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo selectById(OperateCarno bean) {
        OperateCarno data = this.selectById(bean.getId());
        if (data != null) {
            return ResultInfo.success(data);
        } else {
            return ResultInfo.error("未查询到数据");
        }
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo saveData(OperateCarno bean) {

//        LambdaQueryWrapper<OperateCarno> memberWrapper = new LambdaQueryWrapper<>();
//        memberWrapper.eq(OperateCarno::getMember_id, bean.getMember_id());
//        memberWrapper.eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO);
//        Long count = this.selectCount(memberWrapper);
//        if (count != null && count >= 5) {
//            return ResultInfo.error("error", "绑定车牌数量已达到上限");
//        }

        LambdaQueryWrapper<OperateCarno> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OperateCarno::getCar_no, bean.getCar_no());
        wrapper.eq(OperateCarno::getCar_type, bean.getCar_type());
        wrapper.eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO);
        OperateCarno operateCarno = this.selectOne(wrapper);
        //如改车牌已经存在
        if (operateCarno != null) {
            //该车牌是否被绑定
            if (operateCarno.getMember_id() != null && operateCarno.getMember_id() > 0) {
                if (operateCarno.getMember_id().compareTo(bean.getMember_id()) == 0)
                    return ResultInfo.error("error", "该车牌您已绑定");
                else
                    return ResultInfo.error("error", "该车牌已被他人绑定");
            } else {
                //修改车牌表信息，绑定车主
                operateCarno.setMember_id(bean.getMember_id());
                operateCarno.setBind_date(new Date());
                ResultInfo resultInfo = updateInfo(operateCarno);
                if (resultInfo.getCode() == 0) {
                    //添加绑定记录
                    CarnoBindRecord record = new CarnoBindRecord();
                    record.setCar_id(bean.getId());
                    record.setBind_type(GlobalData.CARNO_BIND);
                    record.setMember_id(bean.getMember_id());
                    record.setBind_date(new Date());
                    record.setCreate_time(new Date());
                    record.setUpdate_time(new Date());
                    if (recordService.insert(record) > 0) {
                        return ResultInfo.success(null, "绑定成功");
                    } else {
                        return ResultInfo.error("error", "绑定失败");
                    }
                } else {
                    return ResultInfo.error("error", "绑定失败");
                }
            }
        } else {
            //添加绑定
            bean.setBind_date(new Date());
            if (this.insert(bean) > 0) {
                //添加绑定记录
                CarnoBindRecord record = new CarnoBindRecord();
                record.setCar_id(bean.getId());
                record.setBind_type(GlobalData.CARNO_BIND);
                record.setMember_id(bean.getMember_id());
                record.setBind_date(new Date());
                record.setCreate_time(new Date());
                record.setUpdate_time(new Date());
                if (recordService.insert(record) > 0) {
                    return ResultInfo.success(null, "绑定成功");
                } else {
                    return ResultInfo.error("error", "绑定失败");
                }
            } else {
                return ResultInfo.error("error", "绑定失败");
            }
        }

    }


    /**
     * 描 述： TODO(根据id更新)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo updateInfo(OperateCarno bean) {
        if (this.updateById(bean) > 0)
            return ResultInfo.success("操作成功");
        else
            return ResultInfo.error("操作失败");

    }

    /**
     * 描 述： TODO(根据id删除)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return {@link ResultInfo}
     */
    public ResultInfo deleById(OperateCarno bean) {

        if (deleteById(bean.getId()) > 0) {
            return ResultInfo.success("删除成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }


    /**
     * 描 述： 批量绑定白名单
     * 作 者： xuaolong
     * 历 史： (版本) 作者 时间 注释
     */

    public ResultInfo addWhiteCarNo(BatchAddWhiteCarno batchAddWhiteCarno) {
        //判断添加的车牌蓝牌集合是否为空
        List<String> blueCars = batchAddWhiteCarno.getBlueCars();
        //如果蓝牌集合不为空 ，添加数据
        int count = 0;
        if (!blueCars.isEmpty()) {
            this.addWhiteCarInfo(batchAddWhiteCarno, blueCars, GlobalData.CAR_TYPE_BLUE);
            count++;
        }
        //判断黄牌集合是否为空
        List<String> yellowCars = batchAddWhiteCarno.getYellowCars();
        //如果黄牌集合不为空 ，添加数据
        if (!yellowCars.isEmpty()) {
            this.addWhiteCarInfo(batchAddWhiteCarno, yellowCars, GlobalData.CAR_TYPE_YELLOW);
            count++;
        }
        //判断绿牌集合是否为空
        List<String> greenCars = batchAddWhiteCarno.getGreenCars();
        //如果绿牌集合不为空 ，添加数据
        if (!greenCars.isEmpty()) {
            this.addWhiteCarInfo(batchAddWhiteCarno, greenCars, GlobalData.CAR_TYPE_GREEN);
            count++;
        }
        if (count > 0) {
            return ResultInfo.success();
        }
        return ResultInfo.error("操作失败！");
    }

    /**
     * 私有方法 实现批量新增白名单
     * 作 者： xuaolong
     *
     * @param batchAddWhiteCarno
     * @param carNo              车牌集合
     * @param car_type           车牌类型
     */
    private ResultInfo addWhiteCarInfo(BatchAddWhiteCarno batchAddWhiteCarno, List<String> carNo, String car_type) {

        //遍历车牌集合
        for (int i = 0; i < carNo.size(); i++) {
            String[] str = carNo.get(i).split("-");
            String temp = null;

            String tempCarNo = str[0];
            String[] str1 = tempCarNo.split(",");
            temp = str1[str1.length - 1];

            QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("car_no", temp);
            queryWrapper.eq("car_type", car_type);
            queryWrapper.eq("is_del", "0");
            long count = mapper.selectCount(queryWrapper);
            OperateCarno operateCarno = new OperateCarno();

            //如果有记录 修改更新时间 和白名单类型
            if (count == 1) {
                operateCarno = mapper.selectOne(queryWrapper);
                operateCarno.setRoster_type(GlobalData.ROSTER_TYPE_WHITE);
                operateCarno.setUpdate_time(new Date());
                operateCarno.setUpdate_user(sysUserService.getUser().getId());

                operateCarno.setName(str[1]);
                operateCarno.setPhone(str[2]);
                if (batchAddWhiteCarno.getCompany_name() != null) {
                    operateCarno.setCompany_name(batchAddWhiteCarno.getCompany_name());
                    operateCarno.setCut_off_date(batchAddWhiteCarno.getCut_off_date());
                }

                operateCarno.setWhitelist_type(batchAddWhiteCarno.getWhitelist_type().toString());
                operateCarno.setReason(batchAddWhiteCarno.getReason());
                operateCarno.setWhite_time_type(batchAddWhiteCarno.getWhite_time_type());

                //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
//                if (operateCarno.getRoster_type().equals("3")) {
                // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                if (operateCarno.getWhitelist_type().equals("0")) {
                    // 白名单免费类型 1永久 2期限
                    if (Base.notEmpty(batchAddWhiteCarno.getWhite_time_type()) && batchAddWhiteCarno.getWhite_time_type().equals("2")
                            && Base.notEmpty(batchAddWhiteCarno.getFree_time_start())) {
                        //date1大于date2返回1
                        if (batchAddWhiteCarno.getFree_time_start().compareTo(new DateTime().toDate()) == 1) {
                            // 免费类型为期限，判断免费开始时间大于今日，若等于则改为普通名单
                            operateCarno.setRoster_type("1");
                        }

                        if (batchAddWhiteCarno.getFree_time_end().compareTo(new DateTime().toDate()) < 1) {
                            // 免费类型为期限，判断免费结束时间小于等于今日，若等于则改为普通名单
                            operateCarno.setRoster_type("1");
//                                carno.setFree_time_start(null);
//                                carno.setFree_time_end(null);
//                                carno.setWhite_time_type("1");
                        }
                    } else {
                        operateCarno.setFree_time_start(null);
                        operateCarno.setFree_time_end(null);
                    }
                } else {
                    operateCarno.setFree_time_start(null);
                    operateCarno.setFree_time_end(null);
                }
//                }

                mapper.updateById(operateCarno);
                //更新白名单车牌与路段的关系
                //先删除已有的所有关联关系
                whiteCarnoParkService.delete(Wrappers.<WhiteCarnoPark>lambdaQuery().eq(WhiteCarnoPark::getCarno_id, operateCarno.getId()));
                //新增关联关系
                saveBatchWhiteCarnoParks(batchAddWhiteCarno, operateCarno);
            } else {
                //如果没有车牌信息 添加数据
                operateCarno.setCar_no(temp);
                operateCarno.setCar_type(car_type);
                operateCarno.setRoster_type(GlobalData.ROSTER_TYPE_WHITE);
                operateCarno.setName(str[1]);
                operateCarno.setPhone(str[2]);

                operateCarno.setCreate_time(new Date());
                operateCarno.setCreate_user(sysUserService.getUser().getId());
                if (batchAddWhiteCarno.getCompany_name() != null) {
                    operateCarno.setCompany_name(batchAddWhiteCarno.getCompany_name());
                    operateCarno.setCut_off_date(batchAddWhiteCarno.getCut_off_date());
                }
                operateCarno.setIs_del("0");
                operateCarno.setWhitelist_type(batchAddWhiteCarno.getWhitelist_type().toString());
                operateCarno.setReason(batchAddWhiteCarno.getReason());

                //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
//                if (operateCarno.getRoster_type().equals("3")) {
                // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                if (operateCarno.getWhitelist_type().equals("0")) {
                    // 白名单免费类型 1永久 2期限
                    if (Base.notEmpty(operateCarno.getWhite_time_type()) && operateCarno.getWhite_time_type().equals("2")
                            && Base.notEmpty(operateCarno.getFree_time_start())) {
                        //date1大于date2返回1
                        if (operateCarno.getFree_time_start().compareTo(new DateTime().toDate()) == 1) {
                            // 免费类型为期限，判断免费开始时间大于今日，若等于则改为普通名单
                            operateCarno.setRoster_type("1");
                        }

                        if (operateCarno.getFree_time_end().compareTo(new DateTime().toDate()) < 1) {
                            // 免费类型为期限，判断免费结束时间小于等于今日，若等于则改为普通名单
                            operateCarno.setRoster_type("1");
//                                carno.setFree_time_start(null);
//                                carno.setFree_time_end(null);
//                                carno.setWhite_time_type("1");
                        }
                    } else {
                        operateCarno.setFree_time_start(null);
                        operateCarno.setFree_time_end(null);
                    }
                } else {
                    operateCarno.setFree_time_start(null);
                    operateCarno.setFree_time_end(null);
                }
//                }

                mapper.insert(operateCarno);
                saveBatchWhiteCarnoParks(batchAddWhiteCarno, operateCarno);

            }

        }
        return ResultInfo.success();
    }

    public void saveBatchWhiteCarnoParks(BatchAddWhiteCarno batchAddWhiteCarno, OperateCarno operateCarno) {
        if (CollectionUtils.isNotEmpty(batchAddWhiteCarno.getParkIds())) {
            for (Integer id : batchAddWhiteCarno.getParkIds()) {
                WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                whiteCarnoPark.setCarno_id(operateCarno.getId());
                whiteCarnoPark.setPark_id(id);
                whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_PLAT);
                whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                whiteCarnoParkService.insert(whiteCarnoPark);
            }
        }
        if (CollectionUtils.isNotEmpty(batchAddWhiteCarno.getRoadIds())) {
            for (Integer id : batchAddWhiteCarno.getRoadIds()) {
                WhiteCarnoPark whiteCarnoPark = new WhiteCarnoPark();
                whiteCarnoPark.setCarno_id(operateCarno.getId());
                whiteCarnoPark.setPark_id(id);
                whiteCarnoPark.setPark_type(GlobalData.PARKING_TYPE_ROAD);
                whiteCarnoPark.setIs_del(GlobalData.ISDEL_NO);
                whiteCarnoParkService.insert(whiteCarnoPark);
            }
        }
    }

    /**
     * create by wp at 2022/2/24 9:26
     * description: 将企业免税截止日期在当前日期之前的数据更新为普通名单
     *
     * @return void
     */
    public void freshCarnoRoster() {
        LambdaQueryWrapper<OperateCarno> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OperateCarno::getRoster_type, GlobalData.ROSTER_TYPE_WHITE);
        wrapper.eq(OperateCarno::getWhitelist_type, GlobalData.WHITE_TYPE_FREEDUTY);
        wrapper.lt(OperateCarno::getCut_off_date, new Date());
        wrapper.eq(OperateCarno::getIs_del, GlobalData.ISDEL_NO);
        List<OperateCarno> list = selectList(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> ids = list.stream().map(x -> x.getId()).collect(Collectors.toList());
            UpdateWrapper<OperateCarno> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(OperateCarno::getRoster_type, GlobalData.ROSTER_TYPE_ORDINARY)
                    .set(OperateCarno::getWhitelist_type, null)
                    .set(OperateCarno::getUpdate_time, new Date())
                    .set(OperateCarno::getReason, "定时任务：白名单" + new DateTime().toString("yyyy年MM月dd日") + "过期失效")
                    .in(OperateCarno::getId, ids);
            update(null, updateWrapper);
        }
    }


    /**
     * 定时任务
     * 内部车白名单 每日0时 修改已到期车牌和待生效白名单车牌
     */
    public void onUpdExpiredWhiteList() {
        //修改内部车白名单 待生效白名单车牌为已生效
        QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper();
        //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
        queryWrapper.eq("roster_type", "1");
        // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
        queryWrapper.eq("whitelist_type", "0");
        // 白名单免费类型 1永久 2期限
        queryWrapper.eq("white_time_type", "2");
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("CONVERT(varchar, free_time_start, 23)", new DateTime().toString("yyyy-MM-dd"));
        List<OperateCarno> list = selectList(queryWrapper);
        for (OperateCarno operateCarno : list) {
            //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
            operateCarno.setRoster_type("3");
            operateCarno.setUpdate_time(new Date());
            operateCarno.setReason("定时任务：白名单" + new DateTime().toString("yyyy年MM月dd日") + "生效");
            mapper.updateById(operateCarno);
        }

        //修改内部车白名单 已到期车牌
        QueryWrapper<OperateCarno> queryWrapper2 = new QueryWrapper();
        //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
        queryWrapper2.eq("roster_type", "3");
        // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
        queryWrapper2.eq("whitelist_type", "0");
        // 白名单免费类型 1永久 2期限
        queryWrapper2.eq("white_time_type", "2");
        queryWrapper2.eq("is_del", "0");
        queryWrapper2.le("CONVERT(varchar, free_time_end, 23)", new DateTime().toString("yyyy-MM-dd"));
        List<OperateCarno> list2 = selectList(queryWrapper2);
        for (OperateCarno operateCarno : list2) {
            operateCarno.setRoster_type("1");
            operateCarno.setUpdate_time(new Date());
            operateCarno.setReason("定时任务：白名单" + new DateTime().toString("yyyy年MM月dd日") + "过期失效");
            mapper.updateById(operateCarno);
        }

//        //修改企业白名单 已到期车牌
//        QueryWrapper<OperateCarno> queryWrapper3 = new QueryWrapper();
//        //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
//        queryWrapper3.eq("roster_type", "3");
//        // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
//        queryWrapper3.eq("whitelist_type", "1");
//        queryWrapper3.eq("is_del", "0");
//        queryWrapper3.le("CONVERT(varchar, cut_off_date, 23)", new DateTime().toString("yyyy-MM-dd"));
//        List<OperateCarno> list3 = selectList(queryWrapper3);
//        for (OperateCarno operateCarno : list3) {
//            operateCarno.setRoster_type("1");
//            operateCarno.setUpdate_time(new Date());
//            operateCarno.setReason("定时任务：白名单" + new DateTime().toString("yyyy年MM月dd日") + "过期失效");
//            mapper.updateById(operateCarno);
//        }

    }

    /**
     * 详情
     */
    public ResultInfo view(OperateCarno carno) {
        OperateCarno bean = selectById(carno.getId());

        //白名单
        LambdaQueryWrapper<WhiteCarnoPark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WhiteCarnoPark::getCarno_id, carno.getId());
        wrapper.eq(WhiteCarnoPark::getIs_del, GlobalData.ISDEL_NO);
        List<WhiteCarnoPark> whiteCarnoParkList = whiteCarnoParkService.selectList(wrapper);
        if (whiteCarnoParkList.size() > 0) {
            //停车场类型  0：路测 1：停车场
            List<Integer> roadIds = whiteCarnoParkList.stream().filter(x -> x.getPark_type().equals("0")).map(x -> x.getPark_id()).collect(Collectors.toList());
            List<Integer> parkIds = whiteCarnoParkList.stream().filter(x -> x.getPark_type().equals("1")).map(x -> x.getPark_id()).collect(Collectors.toList());

            if (roadIds.size() > 0) {
                bean.setRoadIds(roadIds);
            }
            if (parkIds.size() > 0) {
                bean.setParkIds(parkIds);
            }
        }

        if (notEmpty(bean.getDeformity_picture_id())) {
            FileManage fileManage = fileManageService.selectById(bean.getDeformity_picture_id());
            bean.setPicUrl(loadPicPath2 + fileManage.getFile_url());
        }

//        bean.setCreate_time(null);
//        bean.setCreate_user(null);
//        bean.setUpdate_time(null);
//        bean.setUpdate_user(null);
        return ResultInfo.success(bean);
    }


    /**
     * 删除
     */
    public ResultInfo del(OperateCarno carno) {
        carno.setIs_del("1");
        carno.setUpdate_user(sysUserService.getUser().getId());
        carno.setUpdate_time(new Date());
        updateById(carno);
        return ResultInfo.success();
    }
}
