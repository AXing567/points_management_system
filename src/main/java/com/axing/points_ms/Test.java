package com.axing.points_ms;


import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, String> mapReceive = new HashMap<>();


        String username = mapReceive.get("username");
        String password = mapReceive.get("password");

        if (mapReceive.containsKey("token")) {
//            比对token是否正确
//            返回验证结果
        }else {
//            比对账号密码是否正确
        }
    }
}
