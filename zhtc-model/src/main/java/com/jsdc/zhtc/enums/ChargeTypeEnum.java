package com.jsdc.zhtc.enums;

/**
 * 收费方案类型  普牌（蓝牌）、绿牌(绿牌)、大车(黄牌)
 */

public enum ChargeTypeEnum {
    BLUE("1"),//普牌（蓝牌）
    GREEN("2"),//绿牌(绿牌)
    YELLOW("3");//大车(黄牌)

    private String value;

    ChargeTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
