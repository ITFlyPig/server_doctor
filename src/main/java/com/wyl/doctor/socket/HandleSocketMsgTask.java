package com.wyl.doctor.socket;

import com.alibaba.fastjson.JSON;
import com.wyl.doctor.constants.DataType;
import com.wyl.doctor.socket.websocket.WebSocketServer;
import com.wyl.doctor.model.SocketRecvBean;
import com.wyl.doctor.unchanged.MethodBean;
import com.wyl.doctor.utils.BytesUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/2
 * 描述    : 处理socket接受到的消息
 */

@Slf4j
public class HandleSocketMsgTask implements Runnable {
    public static volatile boolean isStop;

    @Override
    public void run() {
        log.debug( "run: 启动处理接受到的字节数据的任务");
        while (!isStop) {
            SocketRecvBean bean = null;
            try {
                bean = QueuePool.recvQueue.take();
                log.debug("run: 从队列中获取到的带解析的数据：" + bean);
                parse(bean);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据解析
     *
     * @param bean
     */
    private void parse(SocketRecvBean bean) {
        if (bean == null) return;
        switch (bean.type) {
            case DataType.ALL_PATH:
                MethodBean call = BytesUtil.<MethodBean>toObj(bean.data);
                log.debug("parse: 解析得到数据：" + (call == null ? "null" : call.toString()));
//                if (call != null) {
//                    QueuePool.callQueue.offer(call);
//                }
                //将解析得到的数据直接写到客户端
                WebSocketServer.write(JSON.toJSONString(call));
                break;
        }
    }
}
