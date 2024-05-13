package com.jsdc.zhtc.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: JsEquipmentController
 * Description: 捷顺闸机设备对接
 * date: 2022/1/24 13:34
 *
 * @author wp
 */
@RestController
public class JsEquipmentController {
//
//    @Autowired
//    private ParkingOrderService parkingOrderService;
//    @Autowired
//    private ParkDeviceService deviceService;
//    @Autowired
//    private OperateCarnoService carnoService;
//    //停车场闸机车辆图片url
//    @Value("${jsdc.parkImageUrl}")
//    public String parkImageUrl;
//    @Autowired
//    private TrafficControlUtils trafficControlUtils;
//
//    /**
//     * 停车场车辆驶入
//     * @param json
//     * @return
//     * @throws ParseException
//     * @throws UnsupportedEncodingException
//     * @throws FileNotFoundException
//     */
//    @RequestMapping("entry.do")
//    @SneakyThrows
//    public JSONObject testentry(@RequestBody String json){
//        BsParkInfo parkInfo = JSON.parseObject(json, BsParkInfo.class);
//        BsData bsData = parkInfo.getData();
//        ParkingOrderVo vo = new ParkingOrderVo();
//        String deviceId = parkInfo.getPark_id();
//
//        //处理图片
//        List<ParkingOrderPics> list = new ArrayList<>();
//        String fileUrl = bsData.getPic_addr();
//        ParkingOrderPics parkingOrderPics = new ParkingOrderPics();
//        parkingOrderPics.setPicture_type(GlobalData.PARKING_DIRECTION_IN);
//        HttpClient client = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(fileUrl);
//        HttpResponse response = client.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//        InputStream is = entity.getContent();
//        parkingOrderPics.setIn(is);
//        list.add(parkingOrderPics);
//
//        vo.setDetails(list);
//        vo.setCar_no(StringUtils.isEmpty(bsData.getCar_number())?"":bsData.getCar_number().toUpperCase());
//        vo.setParkCode(parkInfo.getPark_id());
//        vo.setCar_type(StringUtils.equals("大车", bsData.getCar_type()) ? "3" : "1" );
//        vo.setDrivein_time(new Date(bsData.getIn_time() * 1000L));
//        vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);
//        vo.setDrivein_gate(deviceId);
//        vo.setDrivein_gate(deviceId);
//        ResultInfo resultInfo = parkingOrderService.entry(vo);
//        JSONObject result = new JSONObject();
//        if(resultInfo.getCode() == 0){
//            result.put("state", 1);
//            result.put("order_id", bsData.getOrder_id());
//            result.put("park_id", parkInfo.getPark_id());
//            result.put("service_name", "in_park");
//            result.put("errmsg", "send success!");
//        }else{
//            result.put("state", 0);
//            result.put("order_id", bsData.getOrder_id());
//            result.put("park_id", parkInfo.getPark_id());
//            result.put("service_name", "in_park");
//            result.put("errmsg", "send error!");
//        }
//        return result;
//    }
//
////    /**
////     * test
////     */
////    public void test1(@RequestBody String params){
////        /**
////         * 车辆入场向市交系统发送数据 进行数据同步
////         */
////        Map<String, Object> map = new HashMap<>();
////        //发生时间
////        map.put("actTime", bean.getDrivein_time());
////        //进/出场标识（1：进场，2：出场）
////        map.put("actType", 1);
////        //平台停车场编号 必填
////        Integer parkId1 = bean.getPark_id();
////        Park park1 = null;
////        if (null != parkId1) {
////            park1 = parkService.selectById(parkId1);
////            if (null != park1) {
////                map.put("parkCode", park1.getTraffic_park_code());
////            }
////        }
////        //对接方订单号（用以关联对接双方订单）必填
////        map.put("orderNo", bean.getOrder_no());
////
////        //车牌号 必填
////        map.put("plateNo", bean.getCar_no());
////        //车牌颜色 必填 详见附件
////        Integer tempColor = 0;
////        if (bean.getCar_type().equals(GlobalData.CAR_TYPE_BLUE)) {
////            tempColor = 1;
////        } else if (bean.getCar_type().equals(GlobalData.CAR_TYPE_GREEN)) {
////            tempColor = 5;
////        } else if (bean.getCar_type().equals(GlobalData.CAR_TYPE_YELLOW)) {
////            tempColor = 2;
////        } else if (bean.getCar_type().equals(GlobalData.CAR_TYPE_WHITE)) {
////            tempColor = 4;
////        }
////        map.put("plateColor", tempColor);
////        //抓拍图片的 Url 走图片上报接口时选填， imageId 为空 必填
////        List<ParkingOrderPics> list = bean.getDetails();
////        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(list)) {
////            String url = list.get(0).getPicture_url();
////            map.put("picUrl", loadPicPath2 + url);
////        }
////
////        //第三方平台出车图片id 走图片上报接口时必填
////        map.put("imageId","");
////        //TODO 上报交控
////        TrafficControlUtils.postOuterCarPassInData(map);
////    }
//
//    /**
//     * 接口传参封装方法
//     * 3.2.2 图片上报接口
//     * parkType 停车场类型：1-路内；2-路外 必填 【传2路外】
//     * type	string	到达或离开的照片（0-入车；1-出车）
//     * 返回参数
//     * code	int	成功标识（0/-1）
//     * msg	string	返回说明信息
//     * data	Object	null
//     */
//    public ResultInfo setPicInfo(String imageId, String file, String type) {
//        Map<String, Object> map = new HashMap<>();
//        //第三方平台图片id（与出入车记录保持一致）必填
//        map.put("imageId", imageId);
//        //停车场类型：1-路内；2-路外 必填【传2路外】
//        map.put("parkType", 2);
//        //照片生成时间(格式：yyyy-MM-dd HH:mm:ss)
//        map.put("picTime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
//        //到达或离开的照片（0-入车；1-出车）
//        map.put("type", type);
//        //Base64编码字符串（注：大小不超过3M）或可访问的图片url 必填
//        map.put("file", file);
//        ResultInfo resultInfo = trafficControlUtils.postPic(map);
//        System.out.println("3.2.2 图片上报接口返回结果：" + resultInfo.getCode());
//        return resultInfo;
//    }
//
//    //停车场车辆驶出
//    @RequestMapping("exit.do")
//    public ResultInfo testexit(@RequestBody String json) throws ParseException, UnsupportedEncodingException, FileNotFoundException {
//
//        JShunVo params = JSON.parseObject(json, JShunVo.class);
//        System.out.println(json);
//        ParkingOrderVo vo = new ParkingOrderVo();
//        String deviceId = params.getDeviceId();
//        //ParkDevice parkDevice = deviceService.getEntity(deviceId);
//        // todo 捷顺上报车牌号需GBK编码
//        //String carnotest = new String(params.getCarNo().getBytes("GBK"));
//        String carnotest = params.getCarNo();
//        List<PicData> pictures = params.getPicdata();
//        List<ParkingOrderPics> list = new ArrayList<>();
//        for(PicData picture : pictures){
//            ParkingOrderPics parkingOrderPics = new ParkingOrderPics();
//            parkingOrderPics.setPicture_type(GlobalData.PARKING_DIRECTION_OUT);
//            parkingOrderPics.setBase64(picture.getImageFile());
//            list.add(parkingOrderPics);
//        }
//
//        vo.setDetails(list);
//        vo.setCar_no(carnotest);
//        vo.setBerth(deviceId);
//        vo.setEventType(params.getEventType());
//        String carType = "1";
//        switch (params.getCarColor()){
//            case "1": carType = "4"; break;
//            case "2": carType = "1"; break;
//            case "3": carType = "1"; break;
//            case "4": carType = "3"; break;
//            case "5": carType = "2"; break;
//        }
//        vo.setCar_type(carType);
//        vo.setDriveout_time(new Date(params.getPicdata().get(params.getPicnum()-1).getTimeStamp().getSec()*1000L));
//        vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);
//        vo.setDriveout_gate(params.getDeviceId());
//        return parkingOrderService.exit(vo);
//    }
//
//    public String voicebroadcast(String carno, String type, String deviceId){
//        JSONObject params = new JSONObject();
//        params.put("deviceId",deviceId);
//        params.put("sound","");
//        params.put("sound","");
//        JSONObject pctLcdDisplayData = new JSONObject();
//
//
//        String body = HttpRequest.post("http://192.168.0.7:7778/api/service/openGate")
//                .body(params.toJSONString())
//                .execute().body();
//        return body;
//    }
//    /**
//    * create by wp at 2022/1/28 8:18
//    * description: 下方注释代码为防止双通道闸机误拍
//    * @param json
//    * @return com.jsdc.zhtc.vo.ResultInfo
//    */
//    /*@RequestMapping("entry.do")
//    public ResultInfo testentry(@RequestBody String json) throws ParseException, UnsupportedEncodingException {
//        JSONArray jsonArray = JSONArray.parseArray(json);
//        JShunVo params = JSON.parseObject(jsonArray.getJSONObject(0).toJSONString(), JShunVo.class);
//        System.out.println(json);
//        ParkingOrderVo vo = new ParkingOrderVo();
//        String deviceId = params.getDeviceId();
//        ParkDevice parkDevice = deviceService.getEntity(deviceId);
//        String isDoubleWay = parkDevice.getIs_double_way();
//        String carnotest = new String(params.getCarNo().getBytes("GBK"));
//
//        if(GlobalData.IS_DOUBLE_WAY_YES.equals(isDoubleWay)){//如果是双通道设备
//            List<String> statusList = new ArrayList<>();
//            statusList.add(GlobalData.PARKING_ORDER_STOP);
//            statusList.add(GlobalData.PARKING_ORDER_ALREADYPAY);
//            OperateCarno operateCarno = carnoService.selectOne(new QueryWrapper<OperateCarno>().eq("car_no", carnotest).eq("is_del", "0"));
//            if(operateCarno != null){
//                ParkingOrder parkingOrder = parkingOrderService.selectOne(new QueryWrapper<ParkingOrder>().in("status", statusList).eq("is_del", "0").eq("carno_id", operateCarno.getId()));
//                if(null != parkingOrder){
//                    if(parkingOrder.getEntry_type().equals("1")){
//                        //不做开闸操作，此时为车辆出场时误拍的入场行为
//                        System.out.println("误拍入场行为,设备号：" + deviceId);
//                        return ResultInfo.success("误拍入场行为");
//                    }
//                }
//            }
//        }
//        //开闸
//        String openGateResult = opengate(deviceId);
//        System.out.println(openGateResult);
//        vo.setCar_no(carnotest);
//        vo.setBerth(deviceId);
//        vo.setDrivein_time(StringUtils.isNotEmpty(params.getTime().getTime())?String2Date(params.getTime().getTime()): null);
//        vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);
//        vo.setDirection(GlobalData.PARKING_DIRECTION_IN);
//        vo.setDrivein_gate(params.getDeviceId());
//        parkingOrderService.save(vo);
//
//        return ResultInfo.success("上报信息成功");
//    }*/
//
//    /*@RequestMapping("exit.do")
//    public ResultInfo testexit(@RequestBody String json) throws ParseException, UnsupportedEncodingException {
//
//        JSONArray jsonArray = JSONArray.parseArray(json);
//        JShunVo params = JSON.parseObject(jsonArray.getJSONObject(0).toJSONString(), JShunVo.class);
//        System.out.println(json);
//        ParkingOrderVo vo = new ParkingOrderVo();
//        String deviceId = params.getDeviceId();
//        ParkDevice parkDevice = deviceService.getEntity(deviceId);
//        String isDoubleWay = parkDevice.getIs_double_way();
//        String carnotest = new String(params.getCarNo().getBytes("GBK"));
//
//        //开闸
//        // todo 这一步开闸操作应该在支付完成后进行
//        String openGateResult = opengate(deviceId);
//        System.out.println(openGateResult);
//
//        if(GlobalData.IS_DOUBLE_WAY_YES.equals(isDoubleWay)){//如果是双通道设备
//            List<String> statusList = new ArrayList<>();
//            statusList.add(GlobalData.PARKING_ORDER_STOP);
//            statusList.add(GlobalData.PARKING_ORDER_ALREADYPAY);
//            OperateCarno operateCarno = carnoService.selectOne(new QueryWrapper<OperateCarno>().eq("car_no", carnotest));
//            if(null != operateCarno){
//                ParkingOrder parkingOrder = parkingOrderService.selectOne(new QueryWrapper<ParkingOrder>().in("status", statusList).eq("is_del", "0").eq("carno_id", operateCarno.getId()));
//                if(null != parkingOrder){
//                    if(parkingOrder.getEntry_type().equals("0")){
//                        //不做开闸操作，此时为车辆入场时误拍的出场行为
//                        System.out.println("误拍出场行为,设备号：" + deviceId);
//                        parkingOrder.setEntry_type("1");
//                        parkingOrderService.updateById(parkingOrder);
//                        return ResultInfo.success("误拍入场行为");
//                    }
//                }
//            }
//        }
//        vo.setCar_no(carnotest);
//        vo.setBerth(deviceId);
//        vo.setDriveout_time(StringUtils.isNotEmpty(params.getTime().getTime())?String2Date(params.getTime().getTime()): null);
//        vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);
//        vo.setDirection(GlobalData.PARKING_DIRECTION_OUT);
//        vo.setDriveout_gate(params.getDeviceId());
//        parkingOrderService.save(vo);
//
//        return ResultInfo.success("上报信息成功");
//    }*/
//
//    /**
//    * create by wp at 2022/1/27 13:21
//    * description: 打开闸机
//    * @param deviceId
//    * @return java.lang.String
//    */
//    public String opengate(String deviceId){
//        JSONObject params = new JSONObject();
//        params.put("deviceId",deviceId);
//        String body = HttpRequest.post("http://192.168.0.7:7778/api/service/openGate")
//                .body(params.toJSONString())
//                .execute().body();
//        return body;
//    }
//
//    private Date String2Date(String dateString) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = format.parse(dateString);
//        return date;
//    }
}
