package com.innovationai.pigweight.net.bean;

import java.io.Serializable;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/23
 * 简   述：<功能简述>
 */
public class BaseBean<T> implements Serializable {

    private static final long serialVersionUID = -6033387371058405078L;

    /**
     * msg : 成功
     * status : 1
     * data : {}
     */

    private String msg;
    private int status;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
