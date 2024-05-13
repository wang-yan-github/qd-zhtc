package com.jsdc.zhtc.controller;

import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.service.ParkingReleasePlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: ParkingReleaseController
 * Description:上线收费配置表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/parkingReleaseplace")
public class ParkingReleasePlaceController extends BaseController {

    @Autowired
    private ParkingReleasePlaceService placeService;
}
