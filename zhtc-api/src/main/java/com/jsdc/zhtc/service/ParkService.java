package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ParkDao;
import com.jsdc.zhtc.mapper.ParkMapper;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ParkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * ClassName: ParkService <br/>
 * Description: <br/>
 * date: 2021/12/30 11:00<br/>
 *
 * @author bn<br       />
 */
@Service
@Transactional
public class ParkService extends BaseService<ParkDao, Park> {

    @Autowired
    private ParkMapper parkMapper;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 全部停车场信息
     *
     * @param park
     * @return
     */
    public List<Park> toList(Park park) {
        QueryWrapper<Park> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(park.getPark_name())) {
            queryWrapper.like("park_name", park.getPark_name());
        }
        if (park.getStreet_id() != null) {
            queryWrapper.eq("street_id", park.getStreet_id());
        }

        queryWrapper.eq("status", 0);
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }

    /**
     * 列表展示停车场信息
     *
     * @param pageIndex
     * @param pageSize
     * @param park
     * @return
     */
    public PageInfo<ParkVo> toList(Integer pageIndex, Integer pageSize, Park park) {
        SysUser sysUser = sysUserService.getUser();


        // 停车场管理员
        if (sysUser.getUser_type().equals("1")) {
            park.setId(sysUser.getPark_id());
        }
        PageHelper.startPage(pageIndex, pageSize);

        List<ParkVo> parks = parkMapper.toList(park);

        PageInfo<ParkVo> page = new PageInfo<>(parks);
        return page;
    }


    /**
     * 描 述： TODO(查询获取停车场总位数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link Integer}
     */
    public String getBerthCount(CommonVo data) {
        String sumParkNum = parkMapper.getSumParkNum(data);

        return sumParkNum == null ? "0" : sumParkNum;
    }


    //计算总泊位号
    public String berthSumData(String type) {
        return parkMapper.berthSumData(type);
    }


    //停车场管理导出
    public void exportParking(Park bean, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        SysUser sysUser = sysUserService.getUser();
        // 停车场管理员
        if (sysUser.getUser_type().equals("1")) {
            bean.setId(sysUser.getPark_id());
        }
        List<ParkVo> list = parkMapper.toList(bean);
        list.forEach(a -> {
            a.setLongitude(a.getLongitude() + "，" + a.getLatitude());
            if ("0".equals(a.getStatus())) {
                a.setStatus("启用");
            } else {
                a.setStatus("禁用");
            }
        });
        writer.addHeaderAlias("park_code", "停车场编码");
        writer.addHeaderAlias("park_name", "名称");
        writer.addHeaderAlias("area_name", "区域");
        writer.addHeaderAlias("street_name", "街道");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("park_num", "总泊位");
        writer.addHeaderAlias("longitude", "坐标");
        writer.addHeaderAlias("create_time", "时间");
        writer.setOnlyAlias(true);
        writer.write(list, true);
        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("反馈建议.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
