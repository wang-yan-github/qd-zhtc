package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ProvisionalPass;
import lombok.Data;

/**
 * ClassName: ProvisionalPassVo
 * Description:
 * date: 2022/2/24 9:55
 *
 * @author bn
 */
@Data
public class ProvisionalPassVo extends ProvisionalPass {


    // 停车场名称
    private String park_name;

    // 地址
    private String address;


    private String base64;
}
