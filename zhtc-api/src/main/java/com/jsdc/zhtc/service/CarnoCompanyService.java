package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.CarnoCompanyDao;
import com.jsdc.zhtc.mapper.CarnoCompanyMapper;
import com.jsdc.zhtc.model.CarnoCompany;
import com.jsdc.zhtc.model.MonthlyManagement;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.utils.DcCacheDataUtil;
import com.jsdc.zhtc.vo.BatchCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 类 名: 车牌企业映射
 * 描 述: CarnoCompanyService
 * 作 者: lw
 * 创 建：2022/1/4 14:06
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@Transactional
public class CarnoCompanyService extends BaseService<CarnoCompanyDao, CarnoCompany> {
    @Autowired
    private CarnoCompanyMapper carnoCompanyMapper;
    @Autowired
    private OperateCarnoService operateCarnoService;
    @Autowired
    private MemberManageService memberManageService;
    @Autowired
    private MonthlyManagementService monthlyManagementService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 描 述： TODO(公司绑定车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo bindCompanyCar(CarnoCompany bean) {
        QueryWrapper<CarnoCompany> wrapper = new QueryWrapper<>();
        if (bean.getCompany_id() == null || bean.getCarno_id() == null) {
            return ResultInfo.error("未获取到车牌或公司信息！");
        }
        wrapper.eq("carno_id", bean.getCarno_id());
        wrapper.eq("company_id", bean.getCompany_id());
        CarnoCompany carnoCompany = this.selectOne(wrapper);
        if (carnoCompany != null) {
            return ResultInfo.error("该公司已经绑定过此车牌！");
        } else {
            bean.insert();
            return ResultInfo.success();
        }

    }

    /**
     * 车牌存在则返回ID，不存在新增之后返回ID
     *
     * @param carNo
     * @param carType
     * @return
     */
    private Integer saveOrUpdateCarno(String carNo, String carName, String carPhone, String carType) {
        QueryWrapper<OperateCarno> carnoQueryWrapper = new QueryWrapper<>();
        carnoQueryWrapper.eq("car_no", carNo);
        carnoQueryWrapper.eq("is_del", "0");
        carnoQueryWrapper.eq("car_type", carType);
        OperateCarno operateCarno = operateCarnoService.selectOne(carnoQueryWrapper);

        if (operateCarno != null) {
            return operateCarno.getId();
        } else {
            operateCarno = new OperateCarno();
            operateCarno.setCar_no(carNo);
            operateCarno.setName(carName);
            operateCarno.setPhone(carPhone);
            operateCarno.setCar_type(carType);
            operateCarno.setIs_del("0");
            operateCarno.setRoster_type("1");
            operateCarno.setCreate_time(new Date());
            operateCarno.setCreate_user(sysUserService.getUser().getId());
            operateCarno.insert();
            return operateCarno.getId();
        }
    }

    /**
     * 描 述： TODO(公司绑定车牌)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param batchCarno
     * @return josn
     */
    public ResultInfo batchBindCars(BatchCarno batchCarno) {
        //绑定车牌ID集合
        List<Integer> carIds = new ArrayList<>();
        /**
         * 蓝牌集合
         */
        List<String> blueCars = batchCarno.getBlueCars();
        for (String s : blueCars) {
            //根据-分割 车牌号 姓名 电话
            String[] vals = s.split("-");
            //先查询车牌是否存在，如果不存在。则新增记录,保存集合ID
            int id = saveOrUpdateCarno(vals[0], vals[1], vals[2], GlobalData.CAR_TYPE_BLUE);
            carIds.add(id);
            QueryWrapper<CarnoCompany> wrapper = new QueryWrapper<>();
            wrapper.eq("carno_id", id);
            wrapper.eq("company_id", batchCarno.getCompanyId());
            CarnoCompany carnoCompany = this.selectOne(wrapper);
            if (carnoCompany != null) {
                return ResultInfo.error("车牌号：" + vals[0] + ",已被该公司绑定！");
            }
        }
        /**
         * 黄牌集合
         */
        List<String> yellowCars = batchCarno.getYellowCars();
        for (String s : yellowCars) {
            //根据-分割 车牌号 姓名 电话
            String[] vals = s.split("-");
            //先查询车牌是否存在，如果不存在。则新增记录,保存集合ID
            int id = saveOrUpdateCarno(vals[0], vals[1], vals[2], GlobalData.CAR_TYPE_YELLOW);
            carIds.add(id);
            QueryWrapper<CarnoCompany> wrapper = new QueryWrapper<>();
            wrapper.eq("carno_id", id);
            wrapper.eq("company_id", batchCarno.getCompanyId());
            CarnoCompany carnoCompany = this.selectOne(wrapper);
            if (carnoCompany != null) {
                return ResultInfo.error("车牌号：" + vals[0] + ",已被该公司绑定！");
            }
        }
        /**
         * 绿牌集合
         */
        List<String> greenCars = batchCarno.getGreenCars();
        for (String s : greenCars) {
            //根据-分割 车牌号 姓名 电话
            String[] vals = s.split("-");
            //先查询车牌是否存在，如果不存在。则新增记录,保存集合ID
            int id = saveOrUpdateCarno(vals[0], vals[1], vals[2], GlobalData.CAR_TYPE_GREEN);
            carIds.add(id);
            QueryWrapper<CarnoCompany> wrapper = new QueryWrapper<>();
            wrapper.eq("carno_id", id);
            wrapper.eq("company_id", batchCarno.getCompanyId());
            CarnoCompany carnoCompany = this.selectOne(wrapper);
            if (carnoCompany != null) {
                return ResultInfo.error("车牌号：" + vals[0] + " 已被该公司绑定！");
            }
        }
        //已经筛选完合格的车牌ID集合，可以执行插入绑定关系
        for (Integer carId : carIds) {
            CarnoCompany carnoCompany = new CarnoCompany();
            carnoCompany.setCarno_id(carId);
            carnoCompany.setCompany_id(batchCarno.getCompanyId());
            carnoCompany.insert();
        }
        return ResultInfo.success();

    }

    /**
     * 描 述： TODO(公司解除绑定)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return josn
     */
    public ResultInfo unbindCompanyCar(CarnoCompany bean) {
        QueryWrapper<CarnoCompany> wrapper = new QueryWrapper<>();
        if (bean.getCompany_id() == null || bean.getCarno_id() == null) {
            return ResultInfo.error("未获取到车牌或公司信息！");
        }
        wrapper.eq("carno_id", bean.getCarno_id());
        wrapper.eq("company_id", bean.getCompany_id());
        CarnoCompany carnoCompany = this.selectOne(wrapper);
        if (carnoCompany != null) {
            carnoCompany.deleteById();
            //查询是否有包月信息 如果该车牌有包月信息。也一并删除
            QueryWrapper queryWrapper = new QueryWrapper<MonthlyManagement>();
            queryWrapper.eq("carno_id", bean.getCarno_id());
            queryWrapper.eq("companyId", bean.getCompany_id());
            List<MonthlyManagement> infos = monthlyManagementService.selectList(queryWrapper);
            for (MonthlyManagement m : infos) {
                m.deleteById();
            }
            return ResultInfo.success();
        } else {
            return ResultInfo.error("无此绑定信息，无法解绑！");
        }
    }


    /**
     * 获取该公司绑定的车牌信息
     *
     * @param companyId
     * @return
     */
    public ResultInfo getCompanyCars(Integer companyId, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OperateCarnoVo> operateCarnos = carnoCompanyMapper.getCompanyCars(companyId);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        operateCarnos.forEach(x -> {
            if (null != x) {
                x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
                if (StringUtils.isNotEmpty(x.getRoster_type())) {
                    x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
                }
            }
        });
        PageInfo<OperateCarnoVo> pageInfo = new PageInfo<>(operateCarnos);
        return ResultInfo.success(pageInfo);
    }

    public void exportCompanyCars(Integer companyId, HttpServletResponse response) {
        List<OperateCarnoVo> operateCarnos = carnoCompanyMapper.getCompanyCars(companyId);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        operateCarnos.forEach(x -> {
            if (null != x) {
                x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
                if (StringUtils.isNotEmpty(x.getRoster_type())) {
                    x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
                }
            }
        });
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("car_no", "车牌号");
        writer.addHeaderAlias("carTypeName", "车牌类型");
        writer.addHeaderAlias("rosterTypeName", "名单类型");
        writer.addHeaderAlias("memberName", "所属车主");
        writer.addHeaderAlias("companyName", "公司名称");
        writer.addHeaderAlias("phone", "手机号");

        writer.setOnlyAlias(true);
        writer.write(operateCarnos, true);
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

    public ResultInfo getCompanyCars2(Integer companyId) {
        PageHelper.startPage(1, 10000);
        List<OperateCarnoVo> operateCarnos = carnoCompanyMapper.getCompanyCars(companyId);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        operateCarnos.forEach(x -> {
            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
            if (StringUtils.isNotEmpty(x.getRoster_type())) {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
            }
        });
        PageInfo<OperateCarnoVo> pageInfo = new PageInfo<>(operateCarnos);
        return ResultInfo.success(pageInfo);
    }

    public ResultInfo getByCompanyCars(String monthly_code, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<OperateCarnoVo> operateCarnos = carnoCompanyMapper.getByCompanyCars(monthly_code);
        //获取车牌类型字典
        HashMap<String, SysDict> dictCtMap = DcCacheDataUtil.getMapDicts("car_type");
        //名单类型
        HashMap<String, SysDict> dictRtMap = DcCacheDataUtil.getMapDicts("rosterType");
        operateCarnos.forEach(x -> {
            x.setCarTypeName(dictCtMap.get(x.getCar_type()).getLabel());
            if (StringUtils.isNotEmpty(x.getRoster_type())) {
                x.setRosterTypeName(dictRtMap.get(x.getRoster_type()).getLabel());
            }
        });
        PageInfo<OperateCarnoVo> pageInfo = new PageInfo<>(operateCarnos);
        return ResultInfo.success(pageInfo);
    }


}
