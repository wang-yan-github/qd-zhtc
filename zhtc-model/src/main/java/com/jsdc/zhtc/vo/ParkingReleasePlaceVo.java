package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ParkingReleasePlace;
import lombok.Data;

/**
 * ClassName: AppealHandleRecord
 * Description:上线收费停车区域配置
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Data
public class ParkingReleasePlaceVo extends ParkingReleasePlace {

    private String road_name;

}
