package com.wyl.doctor.model;

import java.io.Serializable;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：线程的信息
 */
public class ThreadInfo implements Serializable {
    public String name;//线程的名称
    public long id;//线程id

    public ThreadInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
