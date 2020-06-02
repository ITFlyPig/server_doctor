package com.wyl.doctor.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/2
 * 描述    :
 */
public class BytesUtil {
    public static int bytes2Int(byte[] bytes) {
        int result = 0;
        //将每个byte依次搬运到int相应的位置
        result = bytes[0] & 0xff;
        result = result << 8 | bytes[1] & 0xff;
        result = result << 8 | bytes[2] & 0xff;
        result = result << 8 | bytes[3] & 0xff;
        return result;
    }

    public static byte[] int2Bytes(int num) {
        byte[] bytes = new byte[4];
        //通过移位运算，截取低8位的方式，将int保存到byte数组
        bytes[0] = (byte) (num >>> 24);
        bytes[1] = (byte) (num >>> 16);
        bytes[2] = (byte) (num >>> 8);
        bytes[3] = (byte) num;
        return bytes;
    }

    /**
     * 将字节数组转为对象
     *
     * @param buf
     * @param <T>
     * @return
     */
    public static <T> T toObj(byte[] buf) {
        if (buf == null) {
            return null;
        }
        T t = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            if (obj != null) {
                t = (T) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
