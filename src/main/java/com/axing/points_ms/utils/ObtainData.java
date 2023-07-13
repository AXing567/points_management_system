package com.axing.points_ms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.utils
 * @className: ObtainData
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/10 16:10
 * @version: 1.0
 */
public class ObtainData {
    Logger logger = LoggerFactory.getLogger(ObtainData.class);

    /**
     * @param request: Servlet的request对象
     * @return String 来自前端的Json串
     * @author Axing
     * @description 获取前端传来的数据，并以Json的形式返回。调用时可以通过Gson下的fromJson方法将其转换为Map
     * @date 2023/7/5 18:26
     */
    public static String obtain_data(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }
}
