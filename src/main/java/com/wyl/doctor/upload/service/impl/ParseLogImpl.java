package com.wyl.doctor.upload.service.impl;

import com.wyl.doctor.model.MethodCall;
import com.wyl.doctor.upload.service.IParseLogService;
import com.wyl.doctor.utils.FileUtils;
import com.wyl.doctor.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/5/29
 * 描述    : 解析log
 */
@Slf4j
public class ParseLogImpl implements IParseLogService {
    public static final String TAG = ParseLogImpl.class.getName();

    @Override
    public ArrayList<MethodCall> parse(String logPath) {
        ArrayList<MethodCall> calls = null;
        if (TextUtils.isEmpty(logPath)) {
            log.debug(TAG, "parse: log文件路径为空");
            return calls;
        }
        File logFile = new File(logPath);
        if (!logFile.exists()) {
            log.debug(TAG, "parse: 文件不存在");
            return calls;
        }
        return FileUtils.parseData(logPath);
    }


}
