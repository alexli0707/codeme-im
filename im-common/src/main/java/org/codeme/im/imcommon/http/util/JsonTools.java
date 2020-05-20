package org.codeme.im.imcommon.http.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * JsonTools
 *
 * @author walker lee
 * @date 2019-07-29
 */


@Slf4j
public class JsonTools {

    //通用响应的返回mapper
    private static final ObjectMapper sResponseMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static final ObjectMapper sObjectMapperFailureTolerate = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static String simpleObjToStr(Object obj) {
        try {
            return sObjectMapperFailureTolerate.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("trans obj to json string error", e);
        }
        return "";
    }


    public static <T> T strToObject(String json, Class<T> clz) {
        try {
            return sObjectMapperFailureTolerate.readValue(json, clz);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("trans obj to json string error", e);
        }
        return null;
    }

    public static <T> T mapToObject(Map map, Class<T> clz) {
        return sObjectMapperFailureTolerate.convertValue(map, clz);
    }

    public static String toResponseStr(Object obj) {
        try {
            return sResponseMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("trans obj to json string error", e);
        }
        return "";
    }

}
