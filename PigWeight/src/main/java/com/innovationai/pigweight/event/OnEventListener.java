package com.innovationai.pigweight.event;

import java.util.Map;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/24
 * 简   述：<功能简述>
 */
public interface OnEventListener {

    void onReceive(Map<String, Object> map);
}