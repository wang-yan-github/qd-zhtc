package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.TimeUtils;
import com.jsdc.zhtc.dao.OperateCarnoDao;
import com.jsdc.zhtc.mapper.OperateCarnoMapper;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.vo.BatchAddWhiteCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import static com.jsdc.core.base.Base.empty;

/**
 * 车牌管理
 *
 * @author thr
 */
@Service
@Transactional
public class OperateCarnoService extends BaseService<OperateCarnoDao, OperateCarno> {

    @Autowired
    private OperateCarnoMapper mapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private OperateCarnoMapper operateCarnoMapper;


    public List<OperateCarnoVo> getList(OperateCarnoVo operateCarnoVo) {
        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);

//        //车牌类型(1蓝牌、2绿牌、3黄牌)
//        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
//        //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
//        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
//
//        operateCarnoVos.forEach(x -> {
//            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
//            x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
//        });

        return operateCarnoVos;
    }

    /**
     * 车牌列表数据
     */
    public PageInfo<OperateCarnoVo> toList(Integer pageIndex, Integer pageSize, OperateCarnoVo operateCarnoVo) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);

//        //车牌类型(1蓝牌、2绿牌、3黄牌)
//        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
//        //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
//        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
//
//        operateCarnoVos.forEach(x -> {
//            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
//            x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
//        });

        PageInfo<OperateCarnoVo> pageInfo = new PageInfo<>(operateCarnoVos);
        return pageInfo;
    }

    public void export(OperateCarnoVo operateCarnoVo, HttpServletResponse response) {
        List<OperateCarnoVo> operateCarnoVos = mapper.toList(operateCarnoVo);

//        //车牌类型(1蓝牌、2绿牌、3黄牌)
//        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
//        //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
//        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
//
//        operateCarnoVos.forEach(x -> {
//            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
//            x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
//        });

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("phone", "手机号码");
        writer.addHeaderAlias("car_no", "车牌号码");
        writer.addHeaderAlias("carTypeName", "车牌类型");
        writer.addHeaderAlias("rosterTypeName", "车辆类型");
        writer.addHeaderAlias("free_time_start", "月租车免费开始时间");
        writer.addHeaderAlias("free_time_end", "月租车免费截止时间");
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
     * pc端
     * 新增、修改车牌保存方法
     */
    public ResultInfo save(OperateCarno carno) {
        boolean b = TimeUtils.carPlate(carno.getCar_no());
        if (b) {
            if (empty(carno.getId())) {
                //新增车牌
                QueryWrapper<OperateCarno> queryWrapper = new QueryWrapper<>();
                Long count = mapper.selectCount(queryWrapper.eq("car_no", carno.getCar_no())
                        .eq("car_type", carno.getCar_type()).eq("is_del", "0"));

                if (count > 0) {
                    return ResultInfo.error("车牌信息已存在！");
                } else {
                    carno.setCar_no(carno.getCar_no().toUpperCase());
                    carno.setCreate_time(new Date());
                    carno.setCreate_user(sysUserService.getUser().getId());
                    carno.setIs_del("0");

                    //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
                    if (!carno.getRoster_type().equals("3")) {
                        carno.setWhite_time_type("");
                        carno.setFree_time_start(null);
                        carno.setFree_time_end(null);
                    }

                    //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌 5黑牌)
                    if (carno.getCar_type().equals("3")) {
                        // 车型，1：小型车；2：中型车；3：大型车
                        carno.setVehicle_type("2");
                    } else {
                        carno.setVehicle_type("1");
                    }

                    if (insert(carno) > 0) {
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
                if (count > 0) {
                    return ResultInfo.error("车牌信息已存在！");
                } else {
                    carno.setCar_no(carno.getCar_no().toUpperCase());
                    carno.setUpdate_time(new Date());
                    carno.setUpdate_user(sysUserService.getUser().getId());

                    //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
                    if (!carno.getRoster_type().equals("3")) {
                        carno.setWhite_time_type("");
                        carno.setFree_time_start(null);
                        carno.setFree_time_end(null);
                    }

                    //车牌类型(1蓝牌、2绿牌、3黄牌、4白牌 5黑牌)
                    if (carno.getCar_type().equals("3")) {
                        // 车型，1：小型车；2：中型车；3：大型车
                        carno.setVehicle_type("2");
                    } else {
                        carno.setVehicle_type("1");
                    }

                    if (updateById(carno) > 0) {
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


    /**
     * 批量绑定白名单
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
     * 批量新增车牌
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
                operateCarno.setUpdate_time(new Date());
                operateCarno.setUpdate_user(sysUserService.getUser().getId());

                operateCarno.setReason(batchAddWhiteCarno.getReason());
                operateCarno.setWhite_time_type(batchAddWhiteCarno.getWhite_time_type());

                //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
                if (!operateCarno.getRoster_type().equals("3")) {
                    operateCarno.setWhite_time_type("");
                    operateCarno.setFree_time_start(null);
                    operateCarno.setFree_time_end(null);
                }

                mapper.updateById(operateCarno);
            } else {
                //如果没有车牌信息 添加数据
                operateCarno.setCar_no(temp);
                operateCarno.setCar_type(car_type);
//                operateCarno.setRoster_type(GlobalData.ROSTER_TYPE_WHITE);

                operateCarno.setCreate_time(new Date());
                operateCarno.setCreate_user(sysUserService.getUser().getId());
                operateCarno.setIs_del("0");
                operateCarno.setReason(batchAddWhiteCarno.getReason());

                mapper.insert(operateCarno);
            }

        }
        return ResultInfo.success();
    }

    /**
     * 详情
     */
    public ResultInfo view(OperateCarno carno) {
        OperateCarno bean = selectById(carno.getId());

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

    public List<OperateCarnoVo> carList(String parkId, String name, String carNo) {
        List<OperateCarnoVo> operateCarnoVoList = operateCarnoMapper.carList(parkId, name, carNo);
        return operateCarnoVoList;
    }

}
