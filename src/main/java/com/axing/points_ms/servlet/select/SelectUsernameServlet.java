package com.axing.points_ms.servlet.select;

import com.axing.points_ms.model.dto.Person;
import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.select
 * @className: SelectUsernameServlet
 * @author: Axing
 * @description: 根据username查询对应的用户的详细信息
 * @date: 2023/7/17 9:49
 * @version: 1.0
 */
@WebServlet("/selectUsername")
public class SelectUsernameServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(SelectUsernameServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        Map<String, String> mapReceive = new HashMap<String, String>();
        Person person = new Person();
        Gson gson = new Gson();
        String receiveData = null;
        ResultSet rs = null;

//        获取前端传来的数据username
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new com.google.gson.reflect.TypeToken<Map<String, String>>() {
        }.getType());
        String username = mapReceive.get("username");
        logger.info("获取前端数据成功");

//        获取指定username的用户信息
        rs = operateDB.select_username(username);
        try {
            while (rs.next()) {
                mapReturn.put(rs.getString("user_id"), rs.getString("nickname"));
            }
            logger.info("获取指定username的用户信息成功");
        } catch (SQLException e) {
            logger.error("获取指定username的用户信息失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        响应数据
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
