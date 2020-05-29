package com.wyl.doctor.model;

import com.wyl.doctor.utils.TimeUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 3:38 PM
 * desc   : 方法信息
 */
public class MethodCall implements Serializable {
    public String name;//方法名字
    public Object[] args;//方法的参数
    public String classFullName;//所属的类：全类名
    public long startTime;//开始调用的时间
    public long endTime;//结束调用的时间


    //下面的表示父子关系
    public List<MethodCall> childs;
    public MethodCall parent;

    public ThreadInfo threadInfo;

    @Override
    public String toString() {
        return "MethodCall{" +
                "name='" + name + '\'' +
                ", args=" + Arrays.toString(args) +
                ", classFullName='" + classFullName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", childs=" + childs +
                ", parent=" + parent +
                ", threadInfo=" + threadInfo +
                '}';
    }
}
