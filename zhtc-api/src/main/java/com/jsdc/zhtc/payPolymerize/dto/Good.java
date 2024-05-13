package com.jsdc.zhtc.payPolymerize.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @projectName: zhtc
 * @className: Good
 * @author: wp
 * @description:
 * @date: 2023/5/23 11:06
 */
@Data
@Builder
public class Good {
    private String goodsId;

    private String goodsName;

    private String quantity;

    private String price;

    private String goodsCategory;

    private String body;

    private String subMerchantId;

    private String merOrderId;

    private String subOrderAmount;
}
