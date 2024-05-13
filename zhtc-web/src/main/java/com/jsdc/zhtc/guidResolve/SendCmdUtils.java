package com.jsdc.zhtc.guidResolve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.nettyServer.YtJSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

/**
 * @projectName: zhtc
 * @className: SendCmdUtils
 * @author: wp
 * @description:
 * @date: 2022/12/1 10:38
 */
@Component
@Slf4j
public class SendCmdUtils {

    private static final String XML_PATH = "D:/application/nginx-1.18.0-web/html";
    //private static final String XML_PATH = "E:/upload";

    // 素材目录
    private static final String SC_PATH = "share/";

    private static final String TYPE1 = "programs";
    private static final String TYPE2 = "lists";
    // 节目目录
    private static final String JP_PATH = "programs/";

    // 播放列表目录
    private static final String BPL_PATH = "lists/";

    @Value("${file_review_url}")
    private String fileReviewUrl;

    /**
     * 设置语言
     *
     * @param pid 编码
     */
    public void setLanguage(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        String cmd = "{\"name\":\"setLanguage\",\"input\":{\"language\":\"zh_CN\"}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 获取控制卡属性
     *
     * @param pid 编码
     */
    public void getProperty(String pid, String fileName) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        String cmd = "{\"name\":\"getProperty\",\"input\":{\"controllertype\":\"\",\"screenonoffstatus\":\"\",\"firmwareversion\":\"\"}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    public void queryDiskList(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        cmdJson.put("name", "queryDiskList");
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    public void queryDisk(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject input = new JSONObject();
        cmdJson.put("name", "queryDisk");
        input.put("storagemedia", "emmc");
        cmdJson.put("input", input);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 下发头节目
     *
     * @param pid
     */
    public void downloadHeader(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        SendCmdUtils.createHealPanel("headerProgram1.xml");
        SendCmdUtils.createListXml("programs/headerProgram1.xml", "headerList.xml");
        JSONObject inputJson = new YtJSONObject().put("failonwaitingtimeout", "0").put("downloadmode", "batch").put("downloadtimes", "1").put("updatetime", "0").getHome();
        JSONArray items = new JSONArray();
        items.add(new YtJSONObject().put("srcurl", fileReviewUrl + "headerProgram1.xml").put("dstpath", "programs").put("dstfilename", "headerProgram1.xml").put("size", "4096").put("signature", "").put("digest", "").put("offset", "").put("length", "").put("fingerprint", "").getHome());
        items.add(new YtJSONObject().put("srcurl", fileReviewUrl + "headerList.xml").put("dstpath", "lists").put("dstfilename", "headerList.xml").put("size", "4096").put("signature", "").put("digest", "").put("offset", "").put("length", "").put("fingerprint", "").getHome());
        inputJson.put("items", items);
        JSONObject obj = new YtJSONObject().put("name", "downloadFileFromURL").put("input", inputJson).getHome();
        String r = LesShowYQUtil.pushData(obj.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 文件查询
     *
     * @param pid 编码
     */
    public void findFile(String pid, String fileName) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject input = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("filename", fileName);
        items.add(item);
        input.put("delay", "0");
        input.put("items", items);

        cmdJson.put("name", "findFile");
        cmdJson.put("input", input);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 文件下载
     *
     * @param pid
     */
    public void downloadFile(String pid, String fileUrl, String fileName, String path) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            System.out.println("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        cmdJson.put("name", "downloadFileFromURL");

        JSONObject inputJson = new JSONObject();
        inputJson.put("failonwaitingtimeout", "10");
        inputJson.put("downloadmode", "queue");
        inputJson.put("downloadtimes", "1");
        inputJson.put("updatetime", "0");
        JSONObject item = new JSONObject();
        JSONArray items = new JSONArray();
        item.put("srcurl", fileUrl);
        item.put("dstpath", path);
        item.put("dstfilename", fileName);
        item.put("size", "4096");
        item.put("signature", "");
        item.put("digest", "");
        item.put("offset", "");
        item.put("length", "");
        item.put("fingerprint", "");
        items.add(item);
        inputJson.put("items", items);
        cmdJson.put("input", inputJson);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 文件下载
     *
     * @param pid
     */
    public void downloadFile(String pid, String fileUrl, String fileName, String path, String fileType) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        // url 地址
//        String substring = fileUrl.substring(0, fileUrl.lastIndexOf("/"));
        String substring = "https://zhtc.aldwxa.top";
        // 创建节目xml
        String fileUrl1 = substring + "/" + JP_PATH + "/" + "project.xml";
        createProgramXml(JP_PATH + "project.xml", path + "/" + fileName, fileType);
        // 创建播放列表xml
        String fileUrl2 = substring + "/" + BPL_PATH + "/" + "list.xml";
        createListXml(JP_PATH + "program.xml", BPL_PATH + "list.xml");

        JSONObject inputJson = new YtJSONObject().put("failonwaitingtimeout", "0").put("downloadmode", "batch").put("downloadtimes", "1").put("updatetime", "0").getHome();
        JSONArray items = new JSONArray();
        items.add(new YtJSONObject().put("srcurl", fileUrl).put("dstpath", path).put("dstfilename", fileName).put("size", "4096").put("signature", "").put("digest", "").put("offset", "").put("length", "").put("fingerprint", "").getHome());
        items.add(new YtJSONObject().put("srcurl", fileUrl1).put("dstpath", TYPE1).put("dstfilename", "program.xml").put("size", "4096").put("signature", "").put("digest", "").put("offset", "").put("length", "").put("fingerprint", "").getHome());
        items.add(new YtJSONObject().put("srcurl", fileUrl2).put("dstpath", TYPE2).put("dstfilename", "list.xml").put("size", "4096").put("signature", "").put("digest", "").put("offset", "").put("length", "").put("fingerprint", "").getHome());
        inputJson.put("items", items);

        JSONObject obj = new YtJSONObject().put("name", "downloadFileFromURL").put("input", inputJson).getHome();

        String r = LesShowYQUtil.pushData(obj.toJSONString(), "0200", (byte) 0X01, "23");

        // 给channelFuture注册监听器
        ChannelFuture a = channel.write(r).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                // 如果操作成功
                if (channelFuture.isSuccess()) {
                    log.info(" 成功");
                } else {
                    log.error(" 失败");
                }
            }
        });
    }

    /**
     * 创建节目
     * 生成xml 文件 到本地路径中
     */
    public void createProgramXml(String fileName, String fileUrl, String fileType) {
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            // 创建根节点
            Element program = document.createElement("program");
            // 为program节点添加属性
            program.setAttribute("bgColor", "0xff000000");
            program.setAttribute("name", "programe_0");
            // 向program根节点中添加子节点picturepanel
            if (StringUtils.equals("1", fileType)) {
                Element picturepanel = document.createElement("picturepanel");
                picturepanel.setAttribute("h", "640");
                picturepanel.setAttribute("transparency", "100");
                picturepanel.setAttribute("w", "960");
                picturepanel.setAttribute("x", "0");
                picturepanel.setAttribute("y", "0");
                picturepanel.setAttribute("zOrder", "0");

                // 得到fileName的后缀名
                String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);

                Element picUnit = document.createElement("picUnit");
                picUnit.setAttribute("order", "0");
                picUnit.setAttribute("file", fileUrl);
                picUnit.setAttribute("fileType", suffix);
                picUnit.setAttribute("stuntType", "2");
                picUnit.setAttribute("stuntSpeed", "16");
                picUnit.setAttribute("stayTime", "");

                picturepanel.appendChild(picUnit);
                program.appendChild(picturepanel);
            } else if (StringUtils.equals("2", fileType)) {
                Element videopanel = document.createElement("videopanel");
                videopanel.setAttribute("h", "640");
                videopanel.setAttribute("transparency", "100");
                videopanel.setAttribute("w", "960");
                videopanel.setAttribute("x", "0");
                videopanel.setAttribute("y", "0");
                videopanel.setAttribute("zOrder", "0");
                videopanel.setAttribute("videoType", "0");
                videopanel.setAttribute("volumeMode", "0");
                videopanel.setAttribute("rotationMode", "0");
                videopanel.setAttribute("scaleMode", "1");

                Element videoUnit = document.createElement("videoUnit");
                videoUnit.setAttribute("order", "0");
                videoUnit.setAttribute("file", fileUrl);
                videoUnit.setAttribute("source", "");
                videoUnit.setAttribute("startTime", "0");
                videoUnit.setAttribute("playTime", "0");
                videoUnit.setAttribute("volume", "1");
                videopanel.appendChild(videoUnit);
                program.appendChild(videopanel);
            }


            document.appendChild(program);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 判断file是否存在,如果存在则删除
            File file = new File(XML_PATH + File.separator + fileName);
            // 判断文件夹是否存在,如果不存在则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(file));
            System.out.println("生成节目.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成节目.xml失败");
        }
    }

    public static void createHealPanel(String fileName) {
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            // 创建根节点
            Element program = document.createElement("program");
            // 为program节点添加属性
            program.setAttribute("bgColor", "0xff000000");
            program.setAttribute("name", "programe_0");
            // 向program根节点中添加子节点picturepanel

            //图片分区begin
//            Element picturepanel = document.createElement("picturepanel");
//            picturepanel.setAttribute("x", "0");
//            picturepanel.setAttribute("y", "0");
//            picturepanel.setAttribute("w", "90");
//            picturepanel.setAttribute("h", "90");
//            picturepanel.setAttribute("z", "0");
//            picturepanel.setAttribute("transparency", "100");
//
//            Element pictureUnit = document.createElement("picUnit");
//            pictureUnit.setAttribute("order", "0");
//            pictureUnit.setAttribute("file", "share/logoo.png");
//            pictureUnit.setAttribute("fileType", "png");
//            pictureUnit.setAttribute("stuntType", "2");
//            pictureUnit.setAttribute("stuntSpeed", "16");
//            pictureUnit.setAttribute("stayTime", "5");
            //图片分区end

            //字幕分区begin
            Element textpannel = document.createElement("textpanel");
            textpannel.setAttribute("transparency", "100");
            textpannel.setAttribute("w", "381");
            textpannel.setAttribute("h", "90");
            textpannel.setAttribute("x", "100");
            textpannel.setAttribute("y", "0");
            textpannel.setAttribute("zOrder", "0");
            textpannel.setAttribute("stuntType", "2");
            textpannel.setAttribute("unitType", "text");

            Element textUnit = document.createElement("textUnit");
            textUnit.setAttribute("order", "1");
            textUnit.setAttribute("content", "经开区智慧停车");
            textUnit.setAttribute("stuntSpeed", "16");
            textUnit.setAttribute("stayTime", "5");
            textUnit.setAttribute("fontColor", "0xFFFFFFFF");
            textUnit.setAttribute("fontName", "宋体");
            textUnit.setAttribute("fontSize", "48");
            //字幕分区end

            //时间分区begin
            Element timepanel = document.createElement("timepanel");
            timepanel.setAttribute("x", "470");
            timepanel.setAttribute("y", "0");
            timepanel.setAttribute("w", "560");
            timepanel.setAttribute("h", "90");
            timepanel.setAttribute("zOrder", "0");

            Element timeUnit = document.createElement("dateTime");
            timeUnit.setAttribute("content", "%Y年%m月%d日" + "%H时%M分%S秒");
            timeUnit.setAttribute("contentX", "0");
            timeUnit.setAttribute("contentY", "63");
            timeUnit.setAttribute("fontName", "宋体");
            timeUnit.setAttribute("fontSize", "36");

//            Element timeUnit2 = document.createElement("dateTime");
//            timeUnit2.setAttribute("content", "%H时%M分%S秒");
//            timeUnit2.setAttribute("contentX", "0");
//            timeUnit2.setAttribute("contentY", "80");
//            timeUnit2.setAttribute("fontName", "宋体");
//            timeUnit2.setAttribute("fontSize", "24");
            //时间分区end

//            picturepanel.appendChild(pictureUnit);
            textpannel.appendChild(textUnit);
            timepanel.appendChild(timeUnit);
//            timepanel.appendChild(timeUnit2);
//            program.appendChild(picturepanel);
            program.appendChild(textpannel);
            program.appendChild(timepanel);
            document.appendChild(program);
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 判断file是否存在,如果存在则删除
            File file = new File(XML_PATH + File.separator + fileName);
            // 判断文件夹是否存在,如果不存在则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(file));
            System.out.println("生成节目.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成节目.xml失败");
        }
    }


    /**
     * 创建清单
     * 生成xml 文件 到本地路径中
     */
    public static void createListXml(String programFileName, String fileName) {
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            // 创建根节点
            Element list = document.createElement("list");
            // 为list节点添加属性
            list.setAttribute("deviceType", "9048");
            list.setAttribute("screenHeight", "640");
            list.setAttribute("screenWidth", "960");
            // 向list根节点中添加子节点program
            Element program = document.createElement("program");
            program.setAttribute("order", "0");
            program.setAttribute("playMode", "1");
            program.setAttribute("playTime", "1000");
            program.setAttribute("programFile", programFileName);
            program.setAttribute("startDate", "1970-01-01");
            program.setAttribute("startTime", "00:00:00");
            program.setAttribute("stopDate", "2099-12-31");
            program.setAttribute("stopTime", "23:59:59");
            program.setAttribute("weekFlg", "127");

            list.appendChild(program);
            document.appendChild(list);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();
            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 判断file是否存在,如果存在则删除
            File file = new File(XML_PATH + File.separator + fileName);
            // 判断文件夹是否存在,如果不存在则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(file));
            log.info("生成播放记录.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("生成播放记录.xml失败");
        }
    }

    /**
     * 查询下载进度
     *
     * @param pid
     * @param id  文件下载返回的任务id
     */
    public void queryDownloadProgress(String pid, String id) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject inputJson = new JSONObject();
        cmdJson.put("name", "queryDownloadProgress");
        inputJson.put("delay", "0");
        inputJson.put("id", id);
        cmdJson.put("input", inputJson);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 文件删除
     *
     * @param pid
     */
    public void deleteFile(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject inputJson = new JSONObject();
        JSONArray items = new JSONArray();
        cmdJson.put("name", "deleteFile");
        items.add("lists/list.xml");
        items.add("programs/program.xml");
        //items.add("programs/program.xml");
        items.add("programs/headerProgram1.xml");
        items.add("lists/headerList.xml");
        inputJson.put("items", items);
        cmdJson.put("input", inputJson);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    public void deleteHeaderFile(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject inputJson = new JSONObject();
        JSONArray items = new JSONArray();
        cmdJson.put("name", "deleteFile");
        items.add("programs/headerProgram1.xml");
        items.add("lists/headerList.xml");
        items.add("share/logo.png");
        inputJson.put("items", items);
        cmdJson.put("input", inputJson);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 播放
     *
     * @param pid
     */
    public void play(String pid, String fileName) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject input = new JSONObject();
        cmdJson.put("name", "play");
        input.put("type", "program");
        input.put("playlist", fileName);
        cmdJson.put("input", input);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    public void stop(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        JSONObject input = new JSONObject();
        cmdJson.put("name", "stopPlay");
        input.put("type", "all");
        cmdJson.put("input", input);
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    public void checkplayer(String pid) {
        Channel channel = getChannelByPid(pid);
        if (null == channel) {
            log.error("通道不存在");
            return;
        }
        JSONObject cmdJson = new JSONObject();
        cmdJson.put("name", "checkplayer");
        String r = LesShowYQUtil.pushData(cmdJson.toJSONString(), "0200", (byte) 0X01, "23");
        channel.write(r);
    }

    /**
     * 根据诱导屏编码获取通道
     *
     * @param pid
     * @return
     */
    public Channel getChannelByPid(String pid) {
        Channel channel = null;
        for (Map.Entry<String, Channel> map : GlobalData.CHANNEL_MAP.entrySet()) {
            String key = map.getKey();
            if (key.contains(pid + "_")) {
                channel = map.getValue();
            }
        }
        return channel;
    }
}
