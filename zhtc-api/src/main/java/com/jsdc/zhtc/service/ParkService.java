package com.jsdc.zhtc.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.Constants;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ParkDao;
import com.jsdc.zhtc.mapper.ParkMapper;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.SecureUtil;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ParkVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ParkService <br/>
 * Description: <br/>
 * date: 2021/12/30 11:00<br/>
 *
 * @author bn<br       />
 */
@Service
@Slf4j
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




    // 上传入场记录
    public String  uploadParkingInfo (Park park) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("parkingCode", park.getPark_code());	// 停车场编号，该编号在场库系统中保证唯一
        paramJson.put("parkingName", park.getPark_name());	// 停车场名称
        paramJson.put("address", park.getAddress());	// 停车场地址
        paramJson.put("regionCode",park.getCity());	// 行政区划
        paramJson.put("areaCode", park.getArea_code());	// 城区编号
        paramJson.put("priceType", park.getPrice_type());	//定价类型
        paramJson.put("feeScale", park.getCharge_remark());	// 收费标准
        paramJson.put("openTime", park.getOpenTime());	// 开放时间
        paramJson.put("parkingLocate", park.getParkingLocate());	// 路内或路外，参见数 据字典 停车位置
        paramJson.put("parkingType", park.getParkingType());	// 停车场类型 公共或配建停车场，参见数据字典
        paramJson.put("totalBerthNum", park.getPark_num());	// 泊位总数
        paramJson.put("openBerthNum", park.getOpenBerthNum());	// 开放泊位数
        paramJson.put("bmapX", park.getLongitude());	// 百度经度坐标
        paramJson.put("bmapY", park.getLatitude());	//百度纬度坐标
        paramJson.put("gmapX", park.getLongitude());	//高德经度坐标
        paramJson.put("gmapY", park.getLatitude());	//高德纬度坐标
        paramJson.put("tempTotalNum",park.getTempTotalNum());	// 临停车位数
        paramJson.put("intrinsicTotalNum", park.getIntrinsicTotalNum());	// 月租车位数
        paramJson.put("visitorTotalNum", park.getVisitorTotalNum());	// 访客车位数
        paramJson.put("chargeTotalNum", park.getChargeTotalNum());	// 充电桩车位数
        paramJson.put("uploadTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));	// 上传时间（格式：yyyy-MM-dd HH:mm:ss ）

        JSONObject requestJson = new JSONObject();
        requestJson.put("data", paramJson.toString());
        log.info("停车场基本信息明文信息：{}", paramJson.toString());

        Map<String, String> paramMap = new HashMap<>() ;
        paramMap.put("accessID", Constants.ACCESSID);
        String cipher = SecureUtil.rsaEncryptPublicKey(requestJson.toString(), Constants.SERVERPUBLICKEY);
        paramMap.put("cipher", cipher);
        String sign = SecureUtil.sign(cipher, Constants.PRIVATEKEY);
        paramMap.put("sign", sign);

        log.info("发送信息：{}", paramMap.toString());
        String result = SecureUtil.doPost(Constants.URL + "/uploadParkingInfo", paramMap);
        log.info("result：" + result);
        return result ;
    }



}
