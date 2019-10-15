package com.innovationai.pigweight.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.innovationai.pigweight.Constants;
import com.innovationai.pigweight.activitys.SplashActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/22
 * 简   述：<功能简述>
 */
public class EventManager {

    private static EventManager eventManager;
    //    private Map<OnEventListener, Object> mEventList = new HashMap<>();
    private Map<Object, Handler> mHandlerMap = new HashMap<>();

    public static class Holder {
        public static EventManager instance = new EventManager();
    }

    public static EventManager getInstance() {
        return Holder.instance;
    }

//    public boolean addEvent(Object object, OnEventListener listener) {
//        if (listener == null) return false;
//        mEventList.put(listener, object);
//        return true;
//    }

    public boolean addEvent(Object object, Handler.Callback callback) {
        mHandlerMap.put(object, new Handler(callback));
        return true;
    }

    public boolean removeHandler(Object object) {
        mHandlerMap.remove(object);
        return true;
    }

//    public boolean removeEvent(OnEventListener listener) {
//        if (listener == null) return false;
//        mEventList.remove(listener);
//        return true;
//    }

    public boolean removeEvent(Object object) {
//        Iterator<Map.Entry<OnEventListener, Object>> it = mEventList.entrySet().iterator();
//        while (it.hasNext()) {
//            if (it.next().getValue().equals(object))
//                it.remove();
//        }
        mHandlerMap.remove(object);
        return true;
    }

    public void clearEvent() {
//        mEventList.clear();
        mHandlerMap.clear();
    }

//    public void postEventEvent(Map<String, Object> map) {
//        for (Map.Entry<OnEventListener, Object> listener : mEventList.entrySet()) {
//            listener.getKey().onReceive(listener.getValue(), map);
//        }
//    }

    public void postEventEvent(Map<String, Object> map) {
        for (Map.Entry<Object, Handler> entry : mHandlerMap.entrySet()) {
            Message msg = new Message();
            msg.obj = map;
            entry.getValue().sendMessage(msg);
        }
    }

    public void requestWeightApi(Context context, Bundle bundle, Handler.Callback callback) {
        Intent it = new Intent(context, SplashActivity.class);
        it.putExtra(Constants.ACTION_BUNDLE, bundle);
        context.startActivity(it);
        if (callback != null)
            addEvent(context, callback);
    }

    public void requestWeightApi(Context context, String appid, String token, Handler.Callback callback) {
        requestWeightApi(context, appid, token, 0, 0, 0, callback);
    }

    public void requestWeightApi(Context context, String appid, String token, float width, float height, float ratio, Handler.Callback callback) {
        Intent it = new Intent(context, SplashActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_APPID, appid);
        bundle.putString(Constants.ACTION_TOKEN, token);
        if (0 != (width)) {
            bundle.putFloat(Constants.ACTION_IMGWIDTH, (width));
        }
        if (0 != (height)) {
            bundle.putFloat(Constants.ACTION_IMGHEIGHT, (height));
        }
        if (0 != (ratio)) {
            bundle.putFloat(Constants.ACTION_IMG_RATIO, (ratio));
        }
//        it.putExtras(bundle);
        it.putExtra(Constants.ACTION_BUNDLE, bundle);
        context.startActivity(it);
        if (callback != null)
            addEvent(context, callback);
    }
}
