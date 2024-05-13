package com.jsdc.zhtc.guidResolve;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ValueConvertUtil {

    public static byte[] shortToByte(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (0xff & value);
        bytes[1] = (byte) ((0xff00 & value) >> 8);
        return bytes;
    }

    /**
     * @param buf 2字节
     * @return
     */
    public static short byteToShort(byte[] buf, int start) {
        int val = buf[start + 0] & 0xFF;
        val |= ((buf[start + 1] << 8) & 0xFF00);
        return (short) val;
    }


    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

    public static byte[] intToByte(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xff & value);
        bytes[1] = (byte) ((0xff00 & value) >> 8);
        bytes[2] = (byte) ((0xff0000 & value) >> 16);
        bytes[3] = (byte) ((0xff000000 & value) >> 24);
        return bytes;
    }

    /**
     * 将整数按照小端存放，低字节出访低位
     *
     * @param n
     * @return
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为大端，低字节存储高位
     *
     * @param n
     * @return
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * @param buf 4字节
     * @return
     */
    public static int byteToInt(byte[] buf, int start) {
        int val = buf[start + 0] & 0xFF;
        val |= ((buf[start + 1] << 8) & 0xFF00);
        val |= ((buf[start + 2] << 16) & 0xFF0000);
        val |= ((buf[start + 3] << 24) & 0xFF000000);
        return val;
    }

    public static byte[] longToByte(long value) {
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(value & 0xff).byteValue();// 将最低位保存在最低位
            value = value >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * @param buf 8字节
     * @return
     */
    public static long byteToLong(byte[] buf, int start) {
        long val = 0;
        long s0 = buf[start + 0] & 0xff;// 最低位
        long s1 = buf[start + 1] & 0xff;
        long s2 = buf[start + 2] & 0xff;
        long s3 = buf[start + 3] & 0xff;
        long s4 = buf[start + 4] & 0xff;// 最低位
        long s5 = buf[start + 5] & 0xff;
        long s6 = buf[start + 6] & 0xff;
        long s7 = buf[start + 7] & 0xff;
        s1 <<= 8;// s0不变
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        val = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return val;
    }

    public static byte[] doubleToByte(double value) {
        long sum = Double.doubleToLongBits(value);
        byte[] arr = new byte[8];
        arr[0] = (byte) (sum >> 56);
        arr[1] = (byte) (sum >> 48);
        arr[2] = (byte) (sum >> 40);
        arr[3] = (byte) (sum >> 32);
        arr[4] = (byte) (sum >> 24);
        arr[5] = (byte) (sum >> 16);
        arr[6] = (byte) (sum >> 8);
        arr[7] = (byte) (sum & 0xff);
        return arr;
    }

    /**
     * @param buf 8字节
     * @return
     */
    public static double byteToDouble(byte[] buf, int start) {
        long longBits = (((long) (buf[start + 0] & 0xff) << 56) | ((long) (buf[start + 1] & 0xff) << 48)
                | ((long) (buf[start + 2] & 0xff) << 40) | ((long) (buf[start + 3] & 0xff) << 32)
                | ((long) (buf[start + 4] & 0xff) << 24) | ((long) (buf[start + 5] & 0xff) << 16)
                | ((long) (buf[start + 6] & 0xff) << 8) | ((long) (buf[start + 7] & 0xff)));
        double val = Double.longBitsToDouble(longBits);
        return val;
    }

    public static byte[] stringToByte(String value) throws UnsupportedEncodingException {
        if (null == value)
            return new byte[0];
        return value.getBytes("GBK");
    }

    /**
     * 数组转为字符串
     *
     * @param buf    字节数组
     * @param start  开始位置
     * @param length 长度
     * @return
     */
    public static String byteToString(byte[] buf, int start, int length) {
        final byte[] data = Arrays.copyOfRange(buf, start, start + length);
        int len;
        for (len = 0; len < data.length; len++) {
            if (data[len] == 0) {
                break;
            }
        }
        final byte[] strData = Arrays.copyOfRange(data, 0, len);
        String keyword = null;
        try {
            keyword = new String(strData, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyword;
    }

    /**
     * ASCII码转16进制
     *
     * @param str
     * @return
     */
    public static String stringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    /**
     * 16进制转ASCII码
     *
     * @param hex
     * @return
     */
    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {
            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);
            temp.append(decimal);
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        final String s = Integer.toBinaryString(34);
        System.out.println(StringUtils.reverse(s));

        String str = "账号已冻结--09078A";
//        System.out.println(HexUtil.bytes2HexString(str.getBytes("GBK"), " "));
//        final byte[] bytes = stringToByte(str);
//        System.out.println(HexUtil.bytes2HexString(bytes, " "));
//        final String stringVal = byteToString(bytes, 0, bytes.length);
//        System.out.println(stringVal);
//        byte[] x = new byte[]{(byte) 0xD5, (byte) 0xCB, (byte) 0xBA, (byte) 0xC5, (byte) 0xD2, (byte) 0xD1,
//                (byte) 0xB6, (byte) 0xB3, (byte) 0xBD, (byte) 0xE1, (byte) 0x2D, (byte) 0x2D, (byte) 0x30,
//                (byte) 0x39, (byte) 0x30, (byte) 0x37, (byte) 0x38, (byte) 0x41};
//        System.out.println(byteToString(x, 2, 8));
    }


}
