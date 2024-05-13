package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.model.InspectFeedback;
import lombok.Data;

import java.util.List;

/**
 * ClassName: InspectFeedbackVo
 * Description:
 * date: 2021/12/30 14:55
 *
 * @author zonglina
 */
@Data
public class InspectFeedbackVo extends InspectFeedback {

    //关键字
    private String keyword;
    //区域
    private Integer area_id;
    //街道
    private Integer street_id;
    //路段
    private Integer road_id;
    //状态
    private String status;
    //开始时间
    private String start;
    //结束时间
    private String end;
    //巡检员名称
    private String name;
    //巡检员工号
    private String job_no;
    //图片路径
    private List<FileManage> fileList;

    /**
     * 巡检员ID
     */
    private Integer inspect_id;
}
