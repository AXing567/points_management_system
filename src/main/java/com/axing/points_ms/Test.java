package com.axing.points_ms;

import com.google.gson.Gson;
import java.util.LinkedHashMap;

public class Test {
    public static void main(String[] args) {
        // 创建一个LinkedHashMap对象
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // 将LinkedHashMap对象转换为JSON格式的字符串
        Gson gson = new Gson();
        String json = gson.toJson(map);

        // 输出JSON格式的字符串
        System.out.println(json);
    }
}
