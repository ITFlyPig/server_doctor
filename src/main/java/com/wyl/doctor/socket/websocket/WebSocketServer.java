package com.wyl.doctor.socket.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ServerEndpoint(value = "/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static List<WebSocketServer> webSocketList = Collections.synchronizedList(new ArrayList<WebSocketServer>());
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收sid
    private String sid = "";

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static final String PREFIX = "flutter_draw";//需要数据的sid的前缀

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        session.setMaxIdleTimeout(10000 * 60);
        webSocketList.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        webSocketList.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
    }


    /**
     * 发生错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Throwable error, Session session) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public String getSid() {
        return sid;
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    /**
     * 将数据写到客户端
     * @param msg
     */
    public static void write(String msg) {
        try {
            for (WebSocketServer webSocketServer : webSocketList) {
                //找到需要写的连接，然后写数据
                if (webSocketServer.getSid().contains(PREFIX)) {
                    webSocketServer.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
