package com.wyl.doctor.socket;

import com.wyl.doctor.model.SocketRecvBean;
import com.wyl.doctor.unchanged.MethodBean;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/2
 * 描述    : 存放socket接收到的数据队列
 */
public class QueuePool {
    public static LinkedBlockingQueue<SocketRecvBean> recvQueue = new LinkedBlockingQueue<>();//存放socket接受到的字节数据
    public static LinkedBlockingQueue<MethodBean> callQueue = new LinkedBlockingQueue<>();//存放字节解析为对象后的数据
}
