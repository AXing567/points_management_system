package com.axing.points_ms.utils;

import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.utils
 * @className: Encipher
 * @author: Axing
 * @description: 加密工具类
 * @date: 2023/7/11 15:09
 * @version: 1.0
 */
public class Encipher {

    /**
     * @param name:
     * @return String
     * @author Axing
     * @description md5加密
     * @date 2023/7/6 14:02
     */
    public static String md5(String name) {
        return  DigestUtils.md5DigestAsHex(name.getBytes());
    }


    /**
     * @param length: 字符串的长度
     * @return String 随机的字符串
     * @author Axing
     * @description 生成随机字符串（33%数字 33%小写字母 33%大写字母）
     * @date 2023/7/6 13:28
     */
    public static String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < length; i++) {
            //输出字母还是数字
            int chatType = random.nextInt(3);
            switch (chatType) {
                case 0:
                    //数字
                    val.append(random.nextInt(10));
                    break;
                case 1:
                    //小写字母
                    val.append((char) (random.nextInt(26) + 97));
                    break;
                case 2:
                    //大写字母
                    val.append((char) (random.nextInt(26) + 65));
                    break;
            }
        }
        return val.toString();
    }
}
