package com.wyl.doctor.tasks;

import com.wyl.doctor.socket.HandleSocketMsgTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/22
 * 描述    : 伴随项目启动，任务的启动器
 */
@Slf4j
@Component
public class TasksRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        startParseTask();

    }

    /**
     * 启动解析客户端传递过来的数据
     */
    private void startParseTask() {
        //开启解析数据的线程
        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("uncaughtException:" + t.getName() + " 线程发生未捕获异常：" + e.getLocalizedMessage());
//                if (e instanceof ThreadDeath) {
//                    //线程死亡，新开一个
//                    log.error("线程：" + t.getName() + " 死亡，新开线程处理");
//                    Thread newThread = new Thread(new HandleSocketMsgTask());
//                    newThread.setUncaughtExceptionHandler(this);
//                    newThread.start();
//
//                }
            }
        };
        Thread handleSocketMsgThread = new Thread(new HandleSocketMsgTask());
        handleSocketMsgThread.setUncaughtExceptionHandler(handler);
        handleSocketMsgThread.start();
        log.debug("启动解析客户端传递过来的字节数据的任务");

    }
}
