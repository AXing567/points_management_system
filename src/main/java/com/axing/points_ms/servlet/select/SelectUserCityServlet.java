package com.axing.points_ms.servlet.select;

import com.axing.points_ms.model.dto.Person;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.select
 * @className: SelectUserCityServlet
 * @author: Axing
 * @description: 查询指定市、区的代表的user_id和nick_name
 * @date: 2023/7/17 14:13
 * @version: 1.0
 */
@WebServlet("/selectUserCity")
public class SelectUserCityServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(SelectUserCityServlet.class);

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

//        获取前端数据 city,district
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String city = mapReceive.get("city");
        String district = mapReceive.get("district");

//        根据city,district查询数据库
        rs = operateDB.select_city_district(city, district);
        try {
            while (rs.next()) {
                mapReturn.put(rs.getString("user_id"), rs.getString("nick_name"));
            }
            logger.info("查询成功");
        } catch (SQLException e) {
            logger.error("出现异常SQLException");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        返回对应结果 user_id,nick_name
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
