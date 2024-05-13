package com.jsdc.zhtc.guidResolve;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

import static com.jsdc.zhtc.guidResolve.HexUtil.*;


@Slf4j
public class LesShowYQUtil {
    /**
     * 解码
     *
     * @param data
     * @param mode
     * @param random
     * @return
     */
    public static String decode(String data, int mode, byte random) {
        // 模式4暂时忽略
        if (4 == mode) return "";

        String src = data.replaceAll(" ", "");
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();

        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = (byte) (uniteBytes(tmp[i * 2], tmp[i * 2 + 1]) ^ random);
            if (2 == mode) {
                random = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
            } else if (3 == mode) {
                byte overflow = (byte) ((random >> 7) & 0x01);
                random = (byte) (((random << 1) & 0xff) | overflow);
            } else if (4 == mode) {
                byte overflow = (byte) ((random << 7) & 0x80);
                random = (byte) (((random >> 1) & 0xff) | overflow);
            }
        }

        return new String(ret);
    }

    public static void main(String[] args) {
        String data = "657B226E616D65223A227265626F6F74227D";
        int mode = 1;
        byte randam = (byte) 0X96;
        String ret = decode(data, mode, randam);
        System.out.println(ret);

    }

    /**
     * 编码
     *
     * @param data
     * @param mode
     * @param random
     * @return
     */
    public static byte[] encode(String data, int mode, byte random) {
        String src = data.replaceAll(" ", "");
        byte[] tmp = src.getBytes();

        if (mode == (byte) 0x96) {
            return hexString2Bytes(data);
        }

        byte[] ret = new byte[src.length() / 2];
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = (byte) (uniteBytes(tmp[i * 2], tmp[i * 2 + 1]) ^ random);
            if (2 == mode) {
                random = ret[i];
            } else if (3 == mode) {
                byte overflow = (byte) ((random >> 7) & 0x01);
                random = (byte) (((random << 1) & 0xff) | overflow);
            }
        }

        return ret;
    }

    /**
     * 数据推送
     *
     * @param data
     * @param messageSequence
     * @return
     */
    public static String pushData(String data, String messageSequence, byte mode, String random) {
        BangAnMessage bangAnMessage = new BangAnMessage();
        bangAnMessage.setMagic("42584C43"); // 头部
        bangAnMessage.setFrameLength("D5010000"); // 全部指令长度
        bangAnMessage.setProtocolVersion("07"); // 协议版本
        bangAnMessage.setProtocolType("03"); // 协议类型
        if (mode == (byte) 0x01) {
            bangAnMessage.setEncryptionType("01");
        }
        if (mode == (byte) 0x02) {
            bangAnMessage.setEncryptionType("02");
        }
        if (mode == (byte) 0x03) {
            bangAnMessage.setEncryptionType("03");
        }
        if (mode == (byte) 0x04) {
            return null;
        }
        if (mode == (byte) 0x96) {
            bangAnMessage.setEncryptionType("96");
        }

        bangAnMessage.setEncryptionSeed(random); // 加密种子
        bangAnMessage.setTextLength("B501"); // 未加密指令部分长度
        bangAnMessage.setBinaryLength("00000000"); // 未加密二进制数据部分长度
        bangAnMessage.setReserved("0000000000000000"); // 保留
        bangAnMessage.setMessageSequence(messageSequence); // 服务器发出的报文范围为 0x0000～0x7FFF
        bangAnMessage.setDataCheckSum("45"); // 为指令部分和二进制数据区加密前的各字节异或值
        bangAnMessage.setHeaderCheckSum("FB"); // 为报文头部前 31 字节的异或值
        bangAnMessage.setData(""); // 指令

        // 未加密指令长度
        String cmdData = HexUtil.strTo16(data);
        byte[] encode;
        if (StringUtils.isNotEmpty(bangAnMessage.getEncryptionSeed())) {
            byte[] seed = HexUtil.hexString2Bytes(bangAnMessage.getEncryptionSeed());
            encode = encode(cmdData, mode, seed[0]);
        } else {
            encode = encode(cmdData, mode, (byte) 0x00);
        }

        //byte[] encode = encode(cmdData, mode, seed[0]);
        int cmdLen = encode.length;
        bangAnMessage.setTextLength(HexUtil.bytes2HexString(ValueConvertUtil.toLH(cmdLen)));

        // 指令异或
        int n_m = 0;
        byte[] dataTmp = new byte[0];
        try {
            dataTmp = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        n_m = 0;
        for (int i = 0; i < dataTmp.length; i++) {
            n_m = dataTmp[i] ^ n_m;
        }
        String dataCheckSum = Integer.toHexString(n_m).toUpperCase();
        bangAnMessage.setDataCheckSum(dataCheckSum);
        if (dataCheckSum.length() < 2) {
            bangAnMessage.setDataCheckSum("0" + dataCheckSum);
        }

        // 全文长度
        String frontData = bangAnMessage.toString();
        int frameLength = hexString2Bytes(frontData.concat(cmdData)).length;
        bangAnMessage.setFrameLength(HexUtil.bytes2HexString(ValueConvertUtil.toLH(frameLength)));
        //bangAnMessage.setFrameLength("ED010000");

        // 头部异或
        String[] frontDataArr = strSplit(bangAnMessage.toString());
        n_m = 0;
        for (int i = 0; i < 31; i++) {
            byte[] bytes = hexString2Bytes(frontDataArr[i].replaceAll(" ", ""));
            int i1 = bytes2HexStringToLen(bytes);
            n_m = i1 ^ n_m;
        }
        String headerCheckSum = Integer.toHexString(n_m).toUpperCase();
        bangAnMessage.setHeaderCheckSum(headerCheckSum);
        if (headerCheckSum.length() < 2) {
            bangAnMessage.setHeaderCheckSum("0" + headerCheckSum);
        }

        String allData = bangAnMessage.toString().concat(HexUtil.bytes2HexString(encode));
        String res = HexUtil.bytes2HexString(hexString2Bytes(allData));

        return res;
    }

    public static byte[] hexToByteArray(String inHex) {
        int hexLen = inHex.length();
        byte[] result;
        if (hexLen % 2 == 1) {
            // 奇数
            hexLen++;
            result = new byte[(hexLen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexLen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexLen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }


    public static String[] strSplit(String str) {
        int m = str.length() / 2;
        if (m * 2 < str.length()) {
            m++;
        }
        String[] strs = new String[m];
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) { //每隔两个
                strs[j] = "" + str.charAt(i);
            } else {
                strs[j] = strs[j] + " " + str.charAt(i); // 将字符加上两个空格
                j++;
            }
        }

        return strs;
    }
}
