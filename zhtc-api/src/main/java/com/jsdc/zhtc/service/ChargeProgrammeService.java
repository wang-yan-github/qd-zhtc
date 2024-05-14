package com.jsdc.zhtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ChargeProgrammeDao;
import com.jsdc.zhtc.mapper.ChargeProgrammeMapper;
import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.vo.ChargeProgrammeData;
import com.jsdc.zhtc.vo.ChargeProgrammeVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: ChargeProgrammeService <br/>
 * Description: <br/>
 * date: 2021/12/30 11:03<br/>
 *
 * @author bn<br       />
 */
@Service
@Transactional
public class ChargeProgrammeService extends BaseService<ChargeProgrammeDao, ChargeProgramme> {

    @Autowired
    private ChargeProgrammeMapper chargeProgrammeMapper;

    @Autowired
    private ParkService parkService;

    @Autowired
    private ChargeIntervalConfigService chargeIntervalConfigService;

    @Autowired
    private ChargeTimeConfigService chargeTimeConfigService;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private CacheDataService cacheDataService;

    /**
     *  收费方案列表数据
     * @param pageIndex
     * @param pageSize
     * @param chargeProgrammeVo
     * @return
     */
    public PageInfo<ChargeProgrammeVo> toList(Integer pageIndex, Integer pageSize, ChargeProgrammeVo chargeProgrammeVo) {
        List<Integer> ids = new ArrayList<>();
        if(null != chargeProgrammeVo.getRoad_id()){
            //0 路侧 1 停车场
            if (chargeProgrammeVo.getMenuType().equals("1")) {
                Park a = parkService.selectById(chargeProgrammeVo.getRoad_id());
                if (null != a){
                    if (null != a.getBlue_charge_id()) {
                        ids.add(a.getBlue_charge_id());
                    }
                    if (null != a.getGreen_charge_id()) {
                        ids.add(a.getGreen_charge_id());
                    }
                    if (null != a.getYellow_charge_id()) {
                        ids.add(a.getYellow_charge_id());
                    }
                }
            }
        }
        ids = ids.stream().distinct().collect(Collectors.toList());
        chargeProgrammeVo.setIds(ids);

        PageHelper.startPage(pageIndex,pageSize);

        List<ChargeProgrammeVo> chargeProgrammeVos=chargeProgrammeMapper.toList(chargeProgrammeVo);

        // 引用当前收费方案的停车场
        chargeProgrammeVos.forEach(x->{
            x.setParks(parkService.selectList(new QueryWrapper<Park>().
                    or().eq("blue_charge_id",x.getId()).
                    or().eq("green_charge_id",x.getId()).
                    or().eq("yellow_charge_id",x.getId()).
                    eq("is_del",0)));
        });



        PageInfo<ChargeProgrammeVo> page=new PageInfo<>(chargeProgrammeVos);


        return page;
    }


    public ResultInfo toEdit(String charge) {
        JSONObject jsonObject=JSONObject.parseObject(charge);

        ChargeProgrammeData chargeProgrammeData=JSONObject.parseObject(jsonObject.getString("charge_programme"),ChargeProgrammeData.class);

        // 收费方案
        ChargeProgramme chargeProgramme=selectById(chargeProgrammeData.getId());
        chargeProgramme.setUpdate_user(sysUserService.getUser().getId());
        chargeProgramme.setUpdate_time(new Date());
        BeanUtils.copyProperties(chargeProgrammeData,chargeProgramme);

        updateById(chargeProgramme);

        // 白天

        ChargeIntervalConfig days=chargeIntervalConfigService.selectById(chargeProgramme.getDay_interval_config_id());
        days.setUpdate_time(new Date());
        BeanUtils.copyProperties(chargeProgrammeData.getDays(),
                days, "id");
        chargeIntervalConfigService.updateById(days);

        // 黑夜
        ChargeIntervalConfig night=chargeIntervalConfigService.selectById(chargeProgramme.getNight_interval_config_id());
        night.setUpdate_time(new Date());
        BeanUtils.copyProperties(chargeProgrammeData.getNight(),night,"id");
        chargeIntervalConfigService.updateById(night);



        // 获取白天收费时段原数据
        LambdaQueryWrapper<ChargeTimeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ChargeTimeConfig::getId)
                .eq(ChargeTimeConfig::getInterval_config_id, days.getId());
        List<Integer> daysId=chargeTimeConfigService.selectList(wrapper).stream().map(x -> x.getId()).collect(Collectors.toList());
        Integer daysIdSize=daysId.size();
        // 白天收费时段
        chargeProgrammeData.getDaysTime().forEach(x->{

            if (x.getId()!=null){
                x.setUpdate_time(new Date());
                chargeTimeConfigService.updateById(x);
                daysId.remove(x.getId());
            }else{
                x.setIs_del("0");
                x.setCreate_time(new Date());
                x.setInterval_config_id(days.getId());
                chargeTimeConfigService.insert(x);
            }

        });

        // 批量删除未排除的数据
        if (!daysId.isEmpty()&&daysId.size()!=daysIdSize){
            UpdateWrapper<ChargeTimeConfig> dayWrapper = new UpdateWrapper<>();
            dayWrapper.lambda().set(ChargeTimeConfig::getIs_del, GlobalData.ISDEL_YES)
                    .in(ChargeTimeConfig::getId, daysId);

            chargeTimeConfigService.update(null, dayWrapper);
        }



        // 黑夜收费时段

        LambdaQueryWrapper<ChargeTimeConfig> wrapperNight = new LambdaQueryWrapper<>();
        wrapperNight.select(ChargeTimeConfig::getId)
                .eq(ChargeTimeConfig::getInterval_config_id, night.getId());
        List<Integer> nightIds=chargeTimeConfigService.selectList(wrapperNight).stream().map(x -> x.getId()).collect(Collectors.toList());

        Integer nightIdSize=nightIds.size();

        chargeProgrammeData.getNightTime().forEach(x->{

            if (x.getId()!=null){
                x.setUpdate_time(new Date());

                chargeTimeConfigService.updateById(x);
                nightIds.remove(x.getId());
            }else{
                x.setIs_del("0");
                x.setCreate_time(new Date());
                x.setInterval_config_id(night.getId());
                chargeTimeConfigService.insert(x);
            }
        });

        // 批量删除未排除的数据
        if (!nightIds.isEmpty()&&nightIds.size()!=nightIdSize){
            UpdateWrapper<ChargeTimeConfig> nightWrapper = new UpdateWrapper<>();
            nightWrapper.lambda().set(ChargeTimeConfig::getIs_del, GlobalData.ISDEL_YES)
                    .in(ChargeTimeConfig::getId, nightIds);

            chargeTimeConfigService.update(null, nightWrapper);
        }
        cacheDataService.parkChargeInit();
        return ResultInfo.success(null, "修改收费方案：" + chargeProgrammeData.getProgramme_name());
    }

    public ResultInfo delAll(String programmeIds) {
        List<String> programmeList = Arrays.asList(programmeIds.split(","));
        UpdateWrapper<ChargeProgramme> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(ChargeProgramme::getIs_del, GlobalData.ISDEL_YES)
                .in(ChargeProgramme::getId, programmeList);
        if(update(null, wrapper) > 0){
            cacheDataService.parkChargeInit();
            return ResultInfo.success("删除收费方案成功！", "批量删除收费方案，方案数据：" + programmeIds);
        }else{
            return ResultInfo.error("删除收费方案失败！");
        }
    }

    public ResultInfo toAdd(ChargeProgrammeData chargeProgrammeData) {

        // 白天
        ChargeIntervalConfig days=chargeProgrammeData.getDays();
        days.setIs_del("0");
        days.setCreate_time(new Date());
        chargeIntervalConfigService.insert(days);
        // 黑夜
        ChargeIntervalConfig night=chargeProgrammeData.getNight();
        night.setIs_del("0");
        night.setCreate_time(new Date());
        chargeIntervalConfigService.insert(night);

        // 白天收费时段
        chargeProgrammeData.getDaysTime().forEach(x->{
            x.setIs_del("0");
            x.setCreate_time(new Date());
            x.setInterval_config_id(days.getId());

            chargeTimeConfigService.insert(x);
        });

        // 黑夜收费时段
        chargeProgrammeData.getNightTime().forEach(x->{
            x.setIs_del("0");
            x.setCreate_time(new Date());
            x.setInterval_config_id(night.getId());

            chargeTimeConfigService.insert(x);
        });

        //
        ChargeProgramme chargeProgramme=new ChargeProgramme();
        chargeProgramme.setCreate_user(sysUserService.getUser().getId());
        chargeProgramme.setIs_del("0");
        chargeProgramme.setCreate_time(new Date());
        chargeProgramme.setNight_interval_config_id(night.getId());
        chargeProgramme.setDay_interval_config_id(days.getId());
        BeanUtils.copyProperties(chargeProgrammeData,chargeProgramme);

        insert(chargeProgramme);

        cacheDataService.parkChargeInit();
        return ResultInfo.success(null, "新增收费方案：" + chargeProgrammeData.getProgramme_name());
    }

    /**
     * 全收费方案列表
     *
     * @param chargeProgramme
     * @return
     */
    public List<ChargeProgramme> toList(ChargeProgramme chargeProgramme) {
        QueryWrapper<ChargeProgramme> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(chargeProgramme.getProgramme_name())) {
            queryWrapper.eq("programme_name", chargeProgramme.getProgramme_name());
        }
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }


    public ChargeProgrammeData getChargeProgramme(ChargeProgramme chargeProgramme) {
        ChargeProgrammeData chargeProgrammeData = new ChargeProgrammeData();

        ChargeProgramme charge = selectById(chargeProgramme.getId());
        BeanUtils.copyProperties(charge, chargeProgrammeData);

        // 白天
        chargeProgrammeData.setDays(chargeIntervalConfigService.selectById(charge.getDay_interval_config_id()));
        // 黑夜
        chargeProgrammeData.setNight(chargeIntervalConfigService.selectById(charge.getNight_interval_config_id()));
        // 白天时段
        chargeProgrammeData.setDaysTime(chargeTimeConfigService.selectList(
                new QueryWrapper<ChargeTimeConfig>().eq("interval_config_id",
                        chargeProgrammeData.getDays().getId()).eq("is_del", 0)));
        // 黑天时段
        chargeProgrammeData.setNightTime(chargeTimeConfigService.selectList(
                new QueryWrapper<ChargeTimeConfig>().eq("interval_config_id",
                        chargeProgrammeData.getNight().getId()).eq("is_del", 0)));

        return chargeProgrammeData;
    }


}
