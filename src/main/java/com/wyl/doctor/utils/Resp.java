package com.wyl.doctor.utils;

import com.wyl.doctor.model.Response;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/5/28
 * 描述    : 构建返回数据的工具类
 */
public class Resp {
    private Response response;

    private Resp(){
        response = new Response();

    }

    public static Resp create() {
        return new Resp();
    }

    public Resp code(int code) {
        response.code = code;
        return this;
    }

    public Resp message(String msg) {
        response.message = msg;
        return this;
    }

    public Resp data(Object data) {
        response.data = data;
        return this;
    }

    public Response done() {
        return response;
    }


}
