package com.wyl.doctor;

import com.wyl.doctor.socket.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DoctorApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DoctorApplication.class, args);
    }

}
