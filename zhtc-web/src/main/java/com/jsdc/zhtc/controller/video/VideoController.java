package com.jsdc.zhtc.controller.video;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @projectName: zhtc
 * @className: VideoController
 * @author: wp
 * @description:
 * @date: 2023/8/7 11:06
 */
@Controller
@RequestMapping("video")
public class VideoController {

    @RequestMapping("toVideo.do")
    public String toVideo(String deviceCode, Model model) {
        model.addAttribute("deviceCode", deviceCode);
        return "video/video";
    }
}
