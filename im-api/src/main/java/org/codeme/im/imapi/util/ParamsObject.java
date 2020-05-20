package org.codeme.im.imapi.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 用于Rest请求获取请求内容,提供默认值的功能
 */
public class ParamsObject extends JSONObject {
    public String getStringOrDefault(String key, String defaultValue) {
        String value = this.getString(key);
        return null == value ? defaultValue : value;
    }


    public int getIntOrDefault(String key, int defaultValue) {
        if (this.containsKey(key)) {
            return this.getIntValue(key);
        } else {
            return defaultValue;
        }
    }

    public Integer getIntegerOrDefault(String key, Integer defaultValue) {
        if (this.containsKey(key)) {
            return this.getInteger(key);
        } else {
            return defaultValue;
        }
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        JSONArray jsonArray = this.getJSONArray(key);
        return JSONObject.parseArray(jsonArray.toJSONString(), clz);
    }
}
