package com.wyl.doctor.model;

import lombok.Data;

import javax.websocket.Session;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/10
 * 描述    : WebSocket连接信息
 */

@Data
public class WebSocketConnect {
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收sid
    private String sid = "";

    public WebSocketConnect(Session session, String sid) {
        this.session = session;
        this.sid = sid;
    }
}
