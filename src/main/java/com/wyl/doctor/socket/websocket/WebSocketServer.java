package com.wyl.doctor.socket.websocket;

import com.wyl.doctor.model.WebSocketConnect;
import com.wyl.doctor.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {

    private static final String PREFIX = "flutter_draw";//需要数据的sid的前缀

    private static WebSocketConnect webSocketConnect;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        session.setMaxIdleTimeout(10000 * 60);
        if (sid != null && sid.startsWith(PREFIX)) {
            setNewConnect(new WebSocketConnect(session, sid));
        }
        log.info("有新的WebSocket接入:" + sid );
        
    }


    /**
     * 设置一个新的WebSocket连接
     * @param newWebSocketConnect
     */
    private synchronized void setNewConnect(WebSocketConnect newWebSocketConnect) {
      if (newWebSocketConnect == null) return;
      //关闭之前的连接
        closeConnect(WebSocketServer.webSocketConnect);
        WebSocketServer.webSocketConnect = newWebSocketConnect;
    }

    /**
     * 关闭连接
     * @param connect
     */
    private void closeConnect(WebSocketConnect connect) {
        if  (connect == null || connect.getSession() == null) return;
        try {
            connect.getSession().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        closeConnect(WebSocketServer.webSocketConnect);
        log.info("有一连接关闭");
    }

    /**
     * 收到消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到信息:" + message);
    }

    @OnMessage
    public void onMessage(byte[] data, Session session) {
        log.info("onMessage 收到的byte数据");
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

    /**
     * 主动下发消息
     * @param message
     * @param connect
     */
    public static void sendMessage(String message, WebSocketConnect connect) {
        log.debug("sendMessage: ");
        if (TextUtils.isEmpty(message) || connect == null || connect.getSession() == null) return;
        try {
            connect.getSession().getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    /**
     * 将数据写到客户端
     *
     * @param msg
     */
    public static void write(String msg) {
        log.debug("write: 开始往客户端写数据：" + (msg == null ? "" : msg));
       sendMessage(msg, WebSocketServer.webSocketConnect);
    }
}
