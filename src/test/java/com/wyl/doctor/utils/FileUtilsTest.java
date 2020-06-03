package com.wyl.doctor.utils;

import com.wyl.doctor.unchanged.MethodBean;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/5/29
 * 描述    :
 */
class FileUtilsTest {

    @Test
    void writeToFile() {
        String path = "/Users/yuelinwang/Documents/服务端/doctor/uploads/test.log";
        MethodBean call = new MethodBean();
        call.name = "测试名称";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileUtils.writeToFile(path, call);

    }

    @Test
    void testWriteToFile() {
    }
}