package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.zhtc.model.OperateCarno;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ClassName: OperateCarnoVo <br/>
 * Description: <br/>
 * date: 2022/1/4 14:37<br/>
 *
 * @author bn<br   />
 */
@Data
public class OperateCarnoVo extends OperateCarno {

    private Integer id;

    // 手机号
    private String phone2;
    private String bindName;

    private String carTypeName;

    private String rosterTypeName;

    private String memberName;

    private String roster_type;

    private List<Integer> carnoIds;//车牌ID

    //用户openId
    private String openId;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bindTime;

    List<String> parkNames;

    List<String> roadNames;

    private String companyName;

    //创建人姓名
    private String userName;

    private Integer parkId;
    private Integer roadId;

    private Integer area_id;
    private Integer street_id;

}
