package com.axing.points_ms.servlet.insert;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.insert
 * @className: InsertUserAllServlet
 * @author: Axing
 * @description: 管理员注册新用户（在所有用户表中插入新用户）
 * @date: 2023/7/16 15:32
 * @version: 1.0
 */
@WebServlet("/insertUserAll")
public class InsertUserAllServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(InsertUserAllServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, Object> mapReturn = new HashMap<>();
        Map<String, String> mapReceive;
        Gson gson = new Gson();

//        获取前端传来的数据
        String receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String nickname = mapReceive.get("nickname");
        String username = mapReceive.get("username");
        String password = mapReceive.get("password");

//        检查用户是否存在
        String id = operateDB.select_user_review_id(username);
        if (id != null) {
            logger.info("用户已存在");
            result.setSuccess(false);
            result.setMessage("用户已存在");
            mapReturn.put("result", result);
            String returnData = gson.toJson(mapReturn);
            response.getWriter().write(returnData);
            logger.info("返回数据成功");
            return;
        }

//        存储前端传来的数据及响应返回信息
        if (operateDB.insert_user_all(nickname, username, password)) {
            logger.info("新建用户成功");
            result.setSuccess(true);
            result.setMessage("新建用户成功");
        } else {
            logger.info("新建用户失败");
            result.setSuccess(false);
            result.setMessage("新建用户失败");
        }
        mapReturn.put("result", result);
        String returnData = gson.toJson(mapReturn);
        response.getWriter().write(returnData);
        logger.info("返回数据成功");


    }
}
