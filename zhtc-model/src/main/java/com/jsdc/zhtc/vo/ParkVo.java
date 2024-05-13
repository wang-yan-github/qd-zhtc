package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.Park;
import lombok.Data;

/**
 * ClassName: ParkVo <br/>
 * Description: <br/>
 * date: 2021/12/31 14:50<br/>
 *
 * @author bn<br   />
 */
@Data
public class ParkVo extends Park {

    // 区域名
    private String area_name;

    // 街道名
    private String street_name;
}
