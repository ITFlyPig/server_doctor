package com.wyl.doctor.model;

import java.util.Arrays;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/2
 * 描述    : Socket读取数据
 */
public class SocketRecvBean {
    public int type;//数据类型
    public int len;//数据长度
    public byte[] data;//数据

    public SocketRecvBean(int type, int len, byte[] data) {
        this.type = type;
        this.len = len;
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketRecvBean{" +
                "type=" + type +
                ", len=" + len +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
