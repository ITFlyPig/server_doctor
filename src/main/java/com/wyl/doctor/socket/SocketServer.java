package com.wyl.doctor.socket;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/1
 * 描述    :
 */

@Component
@Slf4j
public class SocketServer implements CommandLineRunner {

    private int port = 8086;
    private boolean started;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new SocketServer().start(8068);
    }

    public void start() {
        start(port);
    }


    public void start(int port) {
        log.debug("start:开始启动socket");

        try {
            serverSocket = new ServerSocket(port);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //开启解析数据的线程
        executorService.submit(new HandleSocketMsgTask());

        while (started) {
            try {

                log.debug("start: 等待客户端连接");
                Socket socket = serverSocket.accept();
                executorService.submit(new SocketRunnable(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        start(8088);
        
        
    }
}
