package com.sorry.band.utils;

import com.alibaba.fastjson.JSON;


import java.util.List;

/**
 * Created by sorry on 8/28/16.
 */
public class Json {
    public static String encodeList(List jsonList){
        return JSON.toJSONString(jsonList);
    }

}
