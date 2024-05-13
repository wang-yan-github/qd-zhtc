package com.jsdc.zhtc.utils;

import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * ClassName: FileUtils
 * Description:
 * date: 2022/1/18 15:52
 *
 * @author wp
 */
public class FileUtils {

    public static InputStream BaseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }
}
