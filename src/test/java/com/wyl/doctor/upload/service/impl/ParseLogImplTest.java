package com.wyl.doctor.upload.service.impl;

import com.wyl.doctor.model.MethodCall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/5/29
 * 描述    :
 */
class ParseLogImplTest {

    @Test
    void parse() {
        String path = "/Users/yuelinwang/Documents/服务端/doctor/uploads/test.log";
        ParseLogImpl parseLog = new ParseLogImpl();
        ArrayList<MethodCall> list = parseLog.parse(path);
        System.out.printf("llll");
    }
}