package com.jsdc.zhtc.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.Constants;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ParkDao;
import com.jsdc.zhtc.mapper.ParkMapper;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.SecureUtil;
import com.jsdc.zhtc.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

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

    @Autowired
    private CacheDataService cacheDataService;


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
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("park_num", "总泊位");
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


    /**
     * 上传车辆入场记录
     * @author wzn
     * @date 2024/5/17 15:17
     */
    public String  uploadCarInData (UploadCarInDataVo uploadCarInDataVo) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("uid", uploadCarInDataVo.getUid());	// 流水号，一次停车的进场、离场、缴费信息等要保证uid相同
        paramJson.put("arriveID",uploadCarInDataVo.getArriveID());	// 入场记录id
        paramJson.put("plateNo", uploadCarInDataVo.getPlateNo());	// 车牌号
        paramJson.put("plateColor", uploadCarInDataVo.getPlateColor());	// 车牌颜色，1：蓝色；2：黄色；3：白色；4：黑色；5：绿色
        paramJson.put("carType",uploadCarInDataVo.getCarType());	// 车型，1：小型车；2：中型车；3：大型车
        paramJson.put("parkingCode", uploadCarInDataVo.getParkingCode());	// 停车场编号
        paramJson.put("entryNum",uploadCarInDataVo.getEntryNum());	// 入口编号
        paramJson.put("totalBerthNum", uploadCarInDataVo.getTotalBerthNum());	// 泊位总数
        paramJson.put("openBerthNum", uploadCarInDataVo.getOpenBerthNum());	// 开放泊位数
        paramJson.put("freeBerthNum", uploadCarInDataVo.getFreeBerthNum());	// 剩余开放泊位数
        paramJson.put("arriveTime", uploadCarInDataVo.getArriveTime());	// 入场时间（格式：yyyy-MM-dd HH:mm:ss）
        paramJson.put("parkingType", uploadCarInDataVo.getParkingType());	// 停车类型，1：临时停车；2：包月停车；3：共享停车；4：特殊停车
        paramJson.put("uploadTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));	// 上传时间（格式：yyyy-MM-dd HH:mm:ss ）

        JSONObject requestJson = new JSONObject();
        requestJson.put("data", paramJson.toString());

        Map<String, String> paramMap = new HashMap<>() ;
        paramMap.put("accessID", Constants.ACCESSID);
        String cipher = SecureUtil.rsaEncryptPublicKey(requestJson.toString(), Constants.SERVERPUBLICKEY);
        paramMap.put("cipher", cipher);
        String sign = SecureUtil.sign(cipher, Constants.PRIVATEKEY);
        paramMap.put("sign", sign);

        String result = SecureUtil.doPost(Constants.URL + "/uploadCarInData", paramMap);
        log.info("result：" + result);
        return result ;
    }



    /**
     *上传车辆离场记录
     * @author wzn
     * @date 2024/5/17 15:26
     */
    public String  uploadCarOutData (UploadCarOutDataVo uploadCarOutDataVo) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("uid", uploadCarOutDataVo.getUid());	// 流水号，一次停车的进场、离场、缴费信息等要保证uid相同
        paramJson.put("endID",uploadCarOutDataVo.getEndID());	// 场库系统给出的车辆离场记录i d
        paramJson.put("plateNo", uploadCarOutDataVo.getPlateNo());	// 车牌号
        paramJson.put("carType",uploadCarOutDataVo.getCarType());	// 车型，1：小型车；2：中型车；3：大型车
        paramJson.put("parkingCode", uploadCarOutDataVo.getParkingCode());	// 停车场编号
        paramJson.put("totalBerthNum", uploadCarOutDataVo.getTotalBerthNum());	// 泊位总数
        paramJson.put("openBerthNum", uploadCarOutDataVo.getOpenBerthNum());	// 开放泊位数
        paramJson.put("freeBerthNum", uploadCarOutDataVo.getFreeBerthNum());	// 剩余开放泊位数
        paramJson.put("arriveTime", uploadCarOutDataVo.getArriveTime());	// 入场时间（格式：yyyy-MM-dd HH:mm:ss）
        paramJson.put("parkingType", uploadCarOutDataVo.getParkingType());	// 停车类型，1：临时停车；2：包月停车；3：共享停车；4：特殊停车
        paramJson.put("endTime", uploadCarOutDataVo.getEndTime());	// 离场时间（格式：yyyy- MM- ddHH: mm: ss ）
        paramJson.put("entryNum", uploadCarOutDataVo.getEntryNum());	// 入口编号
        paramJson.put("outNum", uploadCarOutDataVo.getOutNum());	// 出口编号
        paramJson.put("plateColor", uploadCarOutDataVo.getPlateColor());	// 车牌颜色，1：蓝色；2：黄色；3：白色；4：黑色；5：绿色
        paramJson.put("uploadTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));	// 上传时间（格式：yyyy-MM-dd HH:mm:ss ）
        paramJson.put("chargeFee",uploadCarOutDataVo.getChargeFee());	// 计费金额（单位：分) ，最大不超过1000000000 ，参见名词解释
        paramJson.put("shouldPay",uploadCarOutDataVo.getShouldPay());	// 应收金额（单位：分) ，最大不超过1000000000 ，参见名词解释

        JSONObject requestJson = new JSONObject();
        requestJson.put("data", paramJson.toString());

        Map<String, String> paramMap = new HashMap<>() ;
        paramMap.put("accessID", Constants.ACCESSID);
        String cipher = SecureUtil.rsaEncryptPublicKey(requestJson.toString(), Constants.SERVERPUBLICKEY);
        paramMap.put("cipher", cipher);
        String sign = SecureUtil.sign(cipher, Constants.PRIVATEKEY);
        paramMap.put("sign", sign);

        String result = SecureUtil.doPost(Constants.URL + "/uploadCarOutData", paramMap);
        log.info("result：" + result);
        return result ;
    }


    /**
     * 上传照片  （仅支持J PG 格式的图片文件）
     * @author wzn
     * @date 2024/5/17 15:31
     */
    public String  uploadPhoto (UploadPhotoVo uploadPhotoVo) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("uid", uploadPhotoVo.getUid());	// 流水号，一次停车的进场、离场、缴费信息等要保证uid相同
        paramJson.put("parkingCode", uploadPhotoVo.getParkingCode());	// 停车场编号
        paramJson.put("photoID", uploadPhotoVo.getPhotoID());	// 场库系统给出的照片编号
        paramJson.put("time", uploadPhotoVo.getTime());	// 照片生成时间（格式：yyyy- MM- ddHH: mm: ss ）
        paramJson.put("type", uploadPhotoVo.getType());	// 入场或离场的照片，参见数据字典
        paramJson.put("name", uploadPhotoVo.getName());	//照片名称
        paramJson.put("uploadTime", DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));	// 上传时间（格式：yyyy-MM-dd HH:mm:ss ）

        JSONObject requestJson = new JSONObject();
        requestJson.put("data", paramJson.toString());

        Map<String, String> paramMap = new HashMap<>() ;
        paramMap.put("accessID", Constants.ACCESSID);
        String cipher = SecureUtil.rsaEncryptPublicKey(requestJson.toString(), Constants.SERVERPUBLICKEY);
        paramMap.put("cipher", cipher);
        String sign = SecureUtil.sign(cipher, Constants.PRIVATEKEY);
        paramMap.put("sign", sign);

        paramMap.put("file", "");// Base64 编码字符串（注： 不可空1 9大小不超过512 K）；不参与加密签名，单独key-val ue 传输
        String result = SecureUtil.doPost(Constants.URL + "/uploadPhoto", paramMap);
        log.info("result：" + result);
        return result ;
    }


    public ResultInfo delAll(String parkIds) {

        List<String> parkList = Arrays.asList(parkIds.split(","));
        UpdateWrapper<Park> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(Park::getIs_del, GlobalData.ISDEL_YES)
                .in(Park::getId, parkList);
        if(update(null, wrapper) > 0){

            parkList.forEach(x->{
                Park park=selectById(x);
                cacheDataService.updateLocationCache(park);
                cacheDataService.updChargeByRoad_ParkId(park.getId());

            });
            return ResultInfo.success("删除停车场成功！");
        }else{
            return ResultInfo.error("删除停车场失败！");
        }

    }

}
