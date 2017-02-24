package com.chris.async;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/24.
 */
public interface EventHandler {
    //处理事件的方法
    void doHandle(EventModel model);

    //这个handler支持可以处理哪些event
    List<EventType> getSupportEventTypes();

}
