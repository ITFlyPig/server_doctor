package com.wyl.doctor.utils;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 6:17 PM
 * desc   : 文件工具类
 */
public class FileUtils {
    /**
     * 将对象转为字节数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByte(Serializable obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        byte[] buf = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            buf = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf;
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

    public static boolean writeToFile(File targetFile, Serializable obj) {
        boolean result = false;
        if (targetFile == null) {
            return result;
        }
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                //文件创建失败
                return result;
            }
        }
        byte[] buf = toByte(obj);
        int len = buf == null ? 0 : buf.length;
        if (len == 0) {
            return result;
        }
        ByteBuffer buffer = ByteBuffer.allocate(4 + len);
        buffer.putInt(len);//写入长度
        buffer.put(buf);//写数据
        byte[] bytes = FileUtils.int2Bytes(len);

        //写入到文件
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(targetFile, true);
            bos = new BufferedOutputStream(fos);
            //将数据写入到文件
            bos.write(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;

    }

    public static boolean writeToFile(String path, Serializable obj) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return writeToFile(new File(path), obj);
    }

    public static <T> ArrayList<T> parseData(String filePath) {
        ArrayList<T> list = null;
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return list;
        }
        list = new ArrayList<>();
        int size = 1024;
        byte[] buffer = new byte[size];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int len = 0;//记录读取的字节数
            int offset = 0;
            while ((len = fis.read(buffer, offset, buffer.length - offset)) >= 0) {
                int pos = bytesToObj(buffer, len, list);
                //将没消费的字节数据存到新的数组中
                byte[] left = null;
                if (pos < (len - 1)) {
                    size += (len - pos);
                    left = Arrays.copyOfRange(buffer, pos, len - 1);
                }
                buffer = new byte[size];
                //将剩余的数据拷贝到新的数组中
                if (left != null) {
                    System.arraycopy(left, 0, buffer, 0, left.length);
                    offset = left.length;
                } else {
                    offset = 0;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将字节数组解析为对象，同时返回消耗掉的字节指针
     *
     * @param bytes 存数据的字节数组
     * @param len   字节数组中的数据量
     * @param list  存解析后的对象
     * @param <T>   数据类型
     * @return
     */
    private static <T> int bytesToObj(byte[] bytes, int len, List<T> list) {
        int pos = 0;
        if (bytes == null || list == null || len <= 0) {
            return pos;
        }
        boolean stop = false;
        while (!stop) {
            //获取数据的大小
            int size = -1;
            if (len >= 4) {
                size = toInt(Arrays.copyOfRange(bytes, pos, pos + 4));
                pos += 4;
            }
            //获取到data占用的大小
            if (size >= 0) {
                //表示一个完整的对象数据
                if (size + pos <= len) {
                    T t = FileUtils.<T>toObj(Arrays.copyOfRange(bytes, pos, pos + size));
                    list.add(t);
                    //指针往后移动
                    pos += size;
                } else { //表示当前字节数组中只是存了一个对象的一部分
                    //回退长度的指针
                    pos -= 4;
                    stop = true;
                }

            } else {
                stop = true;
            }
        }
        return pos;
    }

    /**
     * 将字节数组转为int
     *
     * @param bytes
     * @return
     */
    public static int toInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("toInt 参数不是4字节");
        }

        return bytes2Int(bytes);
    }

    public static int convertFourBytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    public static long convertFourBytesToInt2(byte b1, byte b2, byte b3, byte b4) {
        return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

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
}
