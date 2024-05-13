package com.jsdc.core.common.utils;

import java.io.*;

public class SerializableUtils {

    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        if (null != obj) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                bytes = bos.toByteArray ();
                oos.close();
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public static Object toObject (byte[] bytes) {
        Object obj = null;
        if (bytes.length > 0){
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
                ObjectInputStream ois = new ObjectInputStream (bis);
                obj = ois.readObject();
                ois.close();
                bis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return obj;
    }
}
