package com.chris.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YaoQi on 2017/2/19.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

    public Object get(String key) {
        return objs.get(key);
    }

    public void set(String key, Object obj) {
        objs.put(key, obj);
    }
}
