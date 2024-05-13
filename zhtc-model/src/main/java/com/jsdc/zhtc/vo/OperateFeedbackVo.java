package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.OperateFeedback;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: OperateFeedbackVo
 * Description:
 * date: 2021/12/30 14:55
 *
 * @author zonglina
 */
@Data
public class OperateFeedbackVo extends OperateFeedback {

    //手机号
    private String phone;
    //反馈内容
    private String reply_content;
    //反馈时间
    private Date reply_time;
    //反馈名称
    private String feedback_status_name;
}
