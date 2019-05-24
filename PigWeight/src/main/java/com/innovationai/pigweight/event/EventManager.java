package com.innovationai.pigweight.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/5/22
 * 简   述：<功能简述>
 */
public class EventManager {

    private static EventManager eventManager;
    private List<OnEventListener> mEventList = new ArrayList<>();

    public static EventManager getInstance() {
        if (eventManager == null) {
            synchronized (EventManager.class) {
                if (eventManager == null) {
                    eventManager = new EventManager();
                }
            }
        }
        return eventManager;
    }

    public boolean addEvent(OnEventListener listener) {
        if (listener == null) return false;
        mEventList.add(listener);
        return true;
    }

    public boolean removeEvent(OnEventListener listener) {
        if (listener == null) return false;
        mEventList.remove(listener);
        return true;
    }

    public void clearEvent() {
        mEventList.clear();
    }

    public void postEventEvent(Map<String, Object> map) {
        for (OnEventListener listener : mEventList) {
            listener.onReceive(map);
        }
    }
}
