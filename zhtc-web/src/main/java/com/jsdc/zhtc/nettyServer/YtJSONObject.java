package com.jsdc.zhtc.nettyServer;

import com.alibaba.fastjson.JSONObject;

public class YtJSONObject {
    private JSONObject home;

    public YtJSONObject() {
        this.home = new JSONObject();
    }

    public YtJSONObject put(String key, Object value) {
        home.put(key, value);
        return this;
    }

    public JSONObject getHome() {
        return home;
    }
}
