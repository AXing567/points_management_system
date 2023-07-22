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
 * @className: SelectNicknameServlet
 * @author: Axing
 * @description: 根据姓氏查询对应的所有的用户的user_id和nickname
 * @date: 2023/7/16 19:25
 * @version: 1.0
 */
@WebServlet("/selectNickname")
public class SelectNicknameServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(SelectNicknameServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, String> mapReturn = new HashMap<String, String>();
        Map<String, String> mapReceive = new HashMap<String, String>();
        Person person = new Person();
        Gson gson = new Gson();
        String receiveData = null;
        ResultSet rs = null;

//        获取前端传来的数据surname
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new com.google.gson.reflect.TypeToken<Map<String, String>>() {
        }.getType());
        String nicknameReceive = mapReceive.get("nickname");
        logger.info("获取前端数据成功");

//        获取指定surname的用户信息
        try {
            rs = operateDB.select_nickname(nicknameReceive);
            while (rs.next()) {
                String user_id = rs.getString("user_id");
                String nickname = rs.getString("nickname");
                mapReturn.put(user_id, nickname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        logger.info("查询成功");

//        返回数据
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
