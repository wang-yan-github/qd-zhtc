package com.jsdc.zhtc.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.common.utils.TrafficControlUtils;
import com.jsdc.zhtc.dao.ParkDeviceDao;
import com.jsdc.zhtc.mapper.ParkDeviceMapper;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.utils.ticket.QrCodeUtil;
import com.jsdc.zhtc.vo.ParkDeviceVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: ParkDeviceService <br/>
 * Description: <br/>
 * date: 2022/1/13 11:15<br/>
 *
 * @author bn<br       />
 */
@Transactional
@Service
public class ParkDeviceService extends BaseService<ParkDeviceDao, ParkDevice> {

    @Value("${jsdc.gatePageUrl}")
    private String gatePageUrl;

    @Autowired
    private ParkDeviceMapper parkDeviceMapper;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ParkService parkService;
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param parkDeviceVo
     * @return
     */
    public PageInfo<ParkDeviceVo> toList(Integer pageIndex, Integer pageSize, ParkDeviceVo parkDeviceVo) {
        SysUser sysUser = sysUserService.getUser();


        // 停车场管理员
        if (sysUser.getUser_type().equals("1")) {
            parkDeviceVo.setPark_id(sysUser.getPark_id());
        }


        PageHelper.startPage(pageIndex, pageSize);
        List<ParkDeviceVo> deviceVos = parkDeviceMapper.toList(parkDeviceVo);
        PageInfo<ParkDeviceVo> page = new PageInfo<>(deviceVos);
        return page;
    }

    public List<ParkDeviceVo> getList(ParkDeviceVo parkDeviceVo) {
        SysUser sysUser = sysUserService.getUser();
        // 停车场管理员
        if (sysUser.getUser_type().equals("1")) {
            parkDeviceVo.setPark_id(sysUser.getPark_id());
        }
        List<ParkDeviceVo> list = parkDeviceMapper.toList(parkDeviceVo);
        list.forEach(x -> {
            x.setDevice_type_name(sysDictService.selectOne(Wrappers.<SysDict>lambdaQuery().
                    eq(SysDict::getDict_type, "parkDeviceType").
                    eq(SysDict::getDc_value, x.getDevice_type())).getLabel());
        });
        return list;
    }

    public ParkDeviceVo getParkDevice(ParkDeviceVo parkDeviceVo) {

        ParkDeviceVo deviceVo = parkDeviceMapper.getParkDevice(parkDeviceVo);

        return deviceVo;
    }


    public ResultInfo delAll(String deviceIds) {

        List<String> deviceList = Arrays.asList(deviceIds.split(","));
        UpdateWrapper<ParkDevice> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(ParkDevice::getIs_del, GlobalData.ISDEL_YES)
                .in(ParkDevice::getId, deviceList);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除设备成功！");
        } else {
            return ResultInfo.error("删除设备失败！");
        }
    }

    public ResultInfo toAdd(ParkDevice device) {
        List<String> array = Arrays.asList(device.getDevice_code().split(","));

        List<ParkDevice> deviceList = selectList(new QueryWrapper<ParkDevice>().eq("is_del", "0"));
        for (int i = 0; i < deviceList.size(); i++) {
            if (array.contains(deviceList.get(i).getDevice_code())) {
                return ResultInfo.error("泊位号或设备编号已被占用!");
            }
            if (device.getChannel_id().equals(deviceList.get(i).getChannel_id())) {
                return ResultInfo.error("通道号已被占用!");
            }
        }

        array.forEach(x -> {
            if (StringUtils.isNotEmpty(x)) {
                ParkDevice deviceData = new ParkDevice();
                BeanUtils.copyProperties(device, deviceData);
                deviceData.setDevice_code(x);
                deviceData.setBerth_code(x);
                deviceData.setCreate_time(new Date());
                deviceData.setCreate_user(sysUserService.getUser().getId());
                deviceData.setIs_del("0");
                deviceData.setStatus("1");
                deviceData.setIs_use("1");
                insert(deviceData);

                Integer id = deviceData.getId();
                ParkDevice parkDevice = parkDeviceMapper.selectById(id);
                Map<String, Object> map = new HashMap<>();
                map.put("postType", 1);
                Integer parkId = parkDevice.getPark_id();
                if (null != parkId) {
                    Park park = parkService.selectById(parkId);
                    if (park != null) {
                        map.put("parkCode", park.getTraffic_park_code());
                    }
                }
                map.put("deviceIp", parkDevice.getDevice_ip());
                map.put("deviceSN", parkDevice.getSn_no());
                map.put("status", 0);

                try {
                    //上报交控
                    TrafficControlUtils.postParkDevice(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return ResultInfo.success("添加成功！");
    }

    /**
     * 返回支付二维码
     *
     * @param gateCode
     * @return
     * @author wh
     */
    public ResultInfo getQrCode(String gateCode, String parkCode) {
//        String url = gatePageUrl+"/pos/parkingOrder/getDetailEWM?gateCode="+gateCode+"&parkCode="+parkCode;
        String url = gatePageUrl + "/api/ums/getUrl?parkId=" + parkCode + "&channelId=" + gateCode;
        String base64 = "";
        try {
            base64 = QrCodeUtil.encode_QR_CODE(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultInfo.success(base64);
    }

    public ResultInfo toEdit(ParkDevice device) {
        ParkDevice deviceData = selectById(device);
        List<ParkDevice> deviceList =
                selectList(new QueryWrapper<ParkDevice>().eq("is_del", "0")
                        .ne("device_code", deviceData.getDevice_code()));

        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getDevice_code().equals(device.getDevice_code())) {
                return ResultInfo.error("泊位号或设备编号已被占用!");
            }
            if (deviceList.get(i).getChannel_id().equals(device.getChannel_id())) {
                return ResultInfo.error("通道号已被占用!");
            }
        }
        device.setBerth_code(device.getDevice_code());
        device.setUpdate_user(sysUserService.getUser().getId());
        device.setUpdate_time(new Date());
        updateById(device);

        Integer id = device.getId();
        ParkDevice parkDevice = parkDeviceMapper.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("postType", 2);
        Integer parkId = parkDevice.getPark_id();
        if (null != parkId) {
            Park park = parkService.selectById(parkId);
            if (park != null) {
                map.put("parkCode", park.getTraffic_park_code());
            }
        }
        map.put("deviceIp", parkDevice.getDevice_ip());
        map.put("deviceSN", parkDevice.getSn_no());
        if ("3".equals(parkDevice.getStatus())) {
            map.put("status", 1);
        } else {
            map.put("status", 0);
        }

        try {
            //上报交控
            TrafficControlUtils.postParkDevice(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultInfo.success("修改成功！");
    }


    //停车场设备导出
    public void exportEquipment(ParkDeviceVo parkDeviceVo, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter();
        SysUser sysUser = sysUserService.getUser();
        // 停车场管理员
        if (sysUser.getUser_type().equals("1")) {
            parkDeviceVo.setPark_id(sysUser.getPark_id());
        }
        List<ParkDeviceVo> list = parkDeviceMapper.toList(parkDeviceVo);
        list.forEach(a -> {
            if (Base.notEmpty(a.getLongitude()) && Base.notEmpty(a.getLatitude())) {
                a.setLongitude(a.getLongitude() + "," + a.getLatitude());
            }
            if ("1".equals(a.getIs_use())) {
                a.setIs_use("启用");
            } else {
                a.setIs_use("禁用");
            }
            if ("1".equals(a.getDevice_type())) {
                a.setDevice_type("闸机");
            }
            if ("0".equals(a.getPassageway())) {
                a.setPassageway("出口闸机");
            } else {
                a.setPassageway("入口闸机");
            }
        });
        writer.addHeaderAlias("device_type", "设备类型");
        writer.addHeaderAlias("passageway", "通道口");
        writer.addHeaderAlias("device_code", "设备编号");
        writer.addHeaderAlias("berth_code", "泊位编号");
        writer.addHeaderAlias("street_name", "街道");
        writer.addHeaderAlias("park_name", "停车场");
        writer.addHeaderAlias("longitude", "坐标");
        writer.addHeaderAlias("is_use", "状态");
        writer.addHeaderAlias("agrver", "软件版本号");
        writer.addHeaderAlias("create_time", "添加时间");
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

    public JSONObject getDeviceTree() {
        JSONArray tree = new JSONArray();
        JSONArray videoList = new JSONArray();
        List<Park> parks = parkService.selectList(Wrappers.<Park>lambdaQuery().eq(Park::getIs_del, GlobalData.IS_DEL_NO).eq(Park::getBrand, "dc"));
        if (CollectionUtils.isNotEmpty(parks)) {
            List<Integer> parkIds = parks.stream().map(x -> x.getId()).collect(Collectors.toList());
            List<ParkDevice> devices = selectList(Wrappers.<ParkDevice>lambdaQuery().in(ParkDevice::getPark_id, parkIds).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
            parks.forEach(x -> {
                JSONObject node = new JSONObject();
                node.put("id", x.getId());
                node.put("label", x.getPark_name());
                List<ParkDevice> list = devices.stream().filter(w -> w.getPark_id() == x.getId()).collect(Collectors.toList());
                JSONArray children = new JSONArray();
                list.forEach(z -> {
                    JSONObject child = new JSONObject();
                    child.put("id", z.getId());
                    child.put("label", z.getChannel_name());
                    child.put("code", z.getDevice_code());
                    child.put("serialNo", z.getSerialNo());
                    children.add(child);
                    JSONObject video = new JSONObject();
                    video.put("rtsp", "https://zhtc.aldwxa.top/live?port=1935&app=live&stream=" + z.getDevice_code());
                    video.put("player", null);
                    video.put("channelName", z.getChannel_name());
                    video.put("code", z.getDevice_code());
                    video.put("serialNo", z.getSerialNo());
                    videoList.add(video);
                });
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                tree.add(node);
            });
        }
        JSONObject result = new JSONObject();
        result.put("treeData", tree);
        result.put("rtsplist", videoList);
        return result;
    }
}
