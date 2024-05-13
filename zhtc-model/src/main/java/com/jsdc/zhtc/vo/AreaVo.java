package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.Area;
import lombok.Data;

import java.util.List;

/**
 * ClassName: AreaVo <br/>
 * Description: <br/>
 * date: 2021/12/31 9:22<br/>
 *
 * @author bn<br   />
 */
@Data
public class AreaVo extends Area {
    // 街道数量
    private Integer street_num;
    //
    private List<StreetVo> streets;
}
