package com.jsdc.zhtc.utils;

import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;

/**
 * @projectName: zhtc
 * @className: Crc16Utils
 * @author: wp
 * @description:
 * @date: 2023/7/20 15:26
 */
@UtilityClass
public class Crc16Utils {
    /**
     * 一个字节包含位的数量 8
     */
    private static final int BITS_OF_BYTE = 8;

    /**
     * 多项式
     */
    private static final int POLYNOMIAL = 0xA001;

    /**
     * 初始值
     */
    private static final int INITIAL_VALUE = 0xFFFF;

    /**
     * CRC16 编码
     *
     * @param bytes 编码内容
     * @return 编码结果
     */
    public static String crc16(int[] bytes) {
        int res = INITIAL_VALUE;
        for (int data : bytes) {
            res = res ^ data;
            for (int i = 0; i < BITS_OF_BYTE; i++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
            }
        }
        return convertToHexString(revert(res));
    }

    /**
     * 翻转16位的高八位和低八位字节
     *
     * @param src 翻转数字
     * @return 翻转结果
     */
    private static int revert(int src) {
        int lowByte = (src & 0xFF00) >> 8;
        int highByte = (src & 0x00FF) << 8;
        return lowByte | highByte;
    }

    private static String convertToHexString(int src) {
        return Integer.toHexString(src);
    }

    public static String stringToGbk(String string) throws Exception {
        byte[] bytes = new byte[(int) (string.length() / 2.5)];
        for (int i = 0; i < bytes.length / 2; i++) {
            byte high1 = Byte.parseByte(string.substring(i * 5, i * 5 + 1), 16);//a
            byte high2 = Byte.parseByte(string.substring(i * 5 + 1, i * 5 + 2), 16);//3
            bytes[2 * i] = (byte) (high1 << 4 | high2);
            byte low1 = Byte.parseByte(string.substring(i * 5 + 2, i * 5 + 3), 16);//c
            byte low2 = Byte.parseByte(string.substring(i * 5 + 3, i * 5 + 4), 16);//1
            bytes[2 * i + 1] = (byte) (low1 << 4 | low2);
        }
        String result = new String(bytes, "gbk");
        return result;
    }

    public static String gb2312ToString(String content) throws UnsupportedEncodingException {
        byte[] bytes = content.getBytes("gb2312");
        StringBuilder gbString = new StringBuilder();

        for (byte b : bytes) {
            // 再用Integer中的方法，把每个byte转换成16进制输出
            String temp = Integer.toHexString(b);
            //判断进行截取
            if (temp.length() >= 8) {
                temp = temp.substring(6, 8);
            }
            gbString.append(temp + " ");
        }
        return gbString.toString().toUpperCase();
    }

    public static void main(String[] args) {
        int[] data = new int[]{0x01, 0x03, 0x01, 0xF4, 0x00, 0x02};
        System.out.println(Crc16Utils.crc16(data));
    }
}
