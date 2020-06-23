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

@Slf4j
public class SocketServer implements CommandLineRunner {

    private int port = 8086;
    private boolean started;
    private ServerSocket serverSocket;
    private SocketRunnable socketRunnable;
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
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("uncaughtException:" + t.getName() + " 线程发生未捕获异常：" + e.getLocalizedMessage());
                if (e instanceof ThreadDeath) {
                    //线程死亡，新开一个
                    log.error("线程：" + t.getName() + " 死亡，新开线程处理");
                    Thread newThread = new Thread(new HandleSocketMsgTask());
                    newThread.setUncaughtExceptionHandler(this);
                    newThread.start();

                }
            }
        };
        Thread handleSocketMsgThread = new Thread(new HandleSocketMsgTask());
        handleSocketMsgThread.setUncaughtExceptionHandler(uncaughtExceptionHandler );
        handleSocketMsgThread.start();

        while (started) {
            try {

                log.debug("start: 等待客户端连接");
                Socket socket = serverSocket.accept();
                //将之前的结束了
                if (socketRunnable != null) {
                    socketRunnable.isStop = true;
                }
                socketRunnable = new SocketRunnable(socket);
                executorService.submit(socketRunnable);
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
