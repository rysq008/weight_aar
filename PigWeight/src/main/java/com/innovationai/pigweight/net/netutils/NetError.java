package com.innovationai.pigweight.net.netutils;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/28
 * 简   述：<功能简述>
 */
public class NetError {
    private int code;
    private String message;

    public NetError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NetError{" +
                "code=" + code +
                ", message=" + message +
                '}';
    }
}
