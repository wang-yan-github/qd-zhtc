package com.jsdc.zhtc.payPolymerize;

import lombok.Data;

/**
 * @projectName: zhtc
 * @className: Token
 * @author: wp
 * @description: 银联聚合支付token bean
 * @date: 2023/5/23 9:06
 */
@Data
public class Token {
    private String errCode;

    private String errInfo;

    private String accessToken;

    private Integer expiresIn;
}
