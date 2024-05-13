package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.Street;
import lombok.Data;

/**
 * ClassName: StreetVo <br/>
 * Description: <br/>
 * date: 2021/12/31 9:42<br/>
 *
 * @author bn<br   />
 */
@Data
public class StreetVo extends Street {

    // 路段数量
    private Integer road_num;
    // 所属城市
    private String city;
    // 区域
    private String area_name;
}
