package com.wyl.doctor.socket;

import com.wyl.doctor.model.SocketRecvBean;
import com.wyl.doctor.utils.BytesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/2
 * 描述    : 新开线程，处理socket的输入
 */

@Slf4j
public class SocketRunnable implements Runnable {
    private volatile boolean isStop;
    private Socket socket;

    public SocketRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        log.debug( "run: 有客户端连接上，开始读取数据");
        if (socket == null) return;
        try {
            socket.setSoTimeout(10 * 1000 * 60);//10分钟超时
            socket.setKeepAlive(true);
            InputStream is = socket.getInputStream();

            if (is == null) {
                release(socket);
                return;
            }

            while (!isStop) {
                //读取数据的类型，int，四个字节
                byte[] types = new byte[4];
                int ret = 0;
                ret = is.read(types);
                if (ret == -1) {
                    isStop = true;
                    break;
                }
                int type = BytesUtil.bytes2Int(types);
                log.debug("run: 读取到数据的type:" + type);

                //读取数据的长度，int，四个字节
                byte[] sizes = new byte[4];
                ret = is.read(sizes);
                if (ret == -1) {
                    isStop = true;
                    break;
                }
                int dataSize = BytesUtil.bytes2Int(sizes);

                log.debug( "run: 读取到数据的大小：" + dataSize);

                //读取数据的内容
                if (dataSize <= 0) {
                    //无内容，直接跳出本次循环
                    break;
                }

                byte[] datas = new byte[dataSize];
                ret = is.read(datas);
                if (ret == -1) {
                    isStop = true;
                    break;
                }
                log.debug( "run: 读取到数据，将其放到解析队列中");
                //将获取到的数据添加到待处理队列
                QueuePool.recvQueue.offer(new SocketRecvBean(type, dataSize, datas));
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("run: " + e.getLocalizedMessage());
        } finally {
            release(socket);
        }
    }

    /**
     * 释放socket资源
     *
     * @param socket
     */
    private void release(Socket socket) {
        if (socket == null) return;
        log.debug( "release: 释放socket资源");
        try {
            socket.close();
        } catch (IOException e) {
            log.debug("run: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}
