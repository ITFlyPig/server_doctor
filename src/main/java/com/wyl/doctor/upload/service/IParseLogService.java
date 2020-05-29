package com.wyl.doctor.upload.service;

import com.wyl.doctor.model.MethodCall;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/5/29
 * 描述    : log解析接口
 */
public interface IParseLogService {
    /**
     * 解析log文件
     * @param logPath
     * @return
     */
    ArrayList<MethodCall> parse(String logPath);
}
