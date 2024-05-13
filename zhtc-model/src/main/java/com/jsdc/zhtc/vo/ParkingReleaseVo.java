package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ParkingRelease;
import lombok.Data;

import java.util.List;


/**
 * ClassName: ParkingReleasePlaceVo
 * Description:
 * date: 2021/12/31 9:12
 *
 * @author zonglina
 */
@Data
public class ParkingReleaseVo extends ParkingRelease {
    //路段id
    private String road_id;

    List<ParkingReleasePlaceVo> places;
}
