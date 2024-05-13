
package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.guidResolve.SendCmdUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: zhtc
 * @className: TestController
 * @author: wp
 * @description:
 * @date: 2022/12/1 15:11
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private SendCmdUtils sendCmdUtils;

    @PostMapping(value = "download.do")
    public void download(String pid, String url, String fileName, String path, String fileType) {
        sendCmdUtils.downloadFile(pid, url, fileName, path, fileType);
    }

    @PostMapping(value = "download1.do")
    public void download1(String pid, String url, String fileName, String path) {
        sendCmdUtils.downloadFile(pid, url, fileName, path);
    }

    @PostMapping(value = "findFile.do")
    public void findFile(String pid, String fileName) {
        sendCmdUtils.findFile(pid, fileName);
    }

    @PostMapping(value = "queryDownloadProgress.do")
    public void queryDownloadProgress(String pid, String id) {
        sendCmdUtils.queryDownloadProgress(pid, id);
    }

    @PostMapping(value = "play.do")
    public void play(String pid, String fileName) {
        sendCmdUtils.play(pid, fileName);
    }

    @PostMapping(value = "deleteFile.do")
    public void deleteFile(String pid) {
        sendCmdUtils.deleteFile(pid);
    }

    @PostMapping(value = "deleteHeaderFile.do")
    public void deleteHeaderFile(String pid) {
        sendCmdUtils.deleteHeaderFile(pid);
    }

    @PostMapping(value = "checkplayer.do")
    public void checkplayer(String pid) {
        sendCmdUtils.checkplayer(pid);
    }

    @PostMapping(value = "stop.do")
    public void stop(String pid) {
        sendCmdUtils.stop(pid);
    }

    @PostMapping(value = "queryDiskList.do")
    public void queryDiskList(String pid) {
        sendCmdUtils.queryDiskList(pid);
    }

    @PostMapping(value = "queryDisk.do")
    public void queryDisk(String pid) {
        sendCmdUtils.queryDisk(pid);
    }

    @PostMapping(value = "downloadHeader.do")
    public void downloadHeader(String pid) {
        sendCmdUtils.downloadHeader(pid);
    }

    @RequestMapping(value = "getChannelMap.do")
    public String getChannelMap() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Channel> map : GlobalData.CHANNEL_MAP.entrySet()) {
            String key = map.getKey();
            result.append(key).append(",");
        }
        return result.toString();
    }

    @RequestMapping(value = "openGate.do")
    public String openGate(String parkId, String channelId) {
        Channel channel = getChannel(parkId, channelId);
        if (channel == null) {
            return "未找到通道";
        }
        JSONObject res = new JSONObject();
        res.put("name", "openGate");
        JSONObject input = new JSONObject();
        input.put("park_id", parkId);
        input.put("channel_id", channelId);
        res.put("input", input);
        String body = res.toJSONString() + "\r\n\r\n";
        System.out.println(body);
        channel.writeAndFlush(body);
        return body;
    }

    @RequestMapping(value = "closeGate.do")
    public String closeGate(String parkId, String channelId) {
        Channel channel = getChannel(parkId, channelId);
        if (channel == null) {
            return "未找到通道";
        }
        JSONObject res = new JSONObject();
        res.put("name", "closeGate");
        JSONObject input = new JSONObject();
        input.put("park_id", parkId);
        input.put("channel_id", channelId);
        res.put("input", input);
        String body = res.toJSONString() + "\r\n\r\n";
        channel.writeAndFlush(body);
        return body;
    }

    public Channel getChannel(String parkId, String channelId) {
        Channel channel = null;
        for (Map.Entry<String, Channel> entry : GlobalData.CHANNEL_MAP.entrySet()) {
            String key = entry.getKey();
            String id = parkId + "_" + channelId;
            if (key.contains(id)) {
                channel = entry.getValue();
                break;
            }
        }
        return channel;
    }

}
