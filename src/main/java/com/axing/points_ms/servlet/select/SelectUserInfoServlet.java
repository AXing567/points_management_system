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
 * @package: com.axing.points_ms.servlet
 * @className: SelectUserInfoServlet
 * @author: Axing
 * @description: 根据user_id查询对应的用户的所有信息
 * @date: 2023/7/16 9:04
 * @version: 1.0
 */
@WebServlet("/selectUserInfo")
public class SelectUserInfoServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(SelectUserInfoServlet.class);
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

//        接收前端传来的数据user_id
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = mapReceive.get("user_id");
        logger.info("获取前端数据成功");
//        获取指定user_id的用户信息
        try {
            rs = operateDB.select_user_info(user_id);
            if (rs.next()) {
                person.setNickname(rs.getString("nick_name"));
                person.setGender(rs.getString("gender"));
                person.setEthnicity(rs.getString("ethnicity"));
                person.setBirthday(rs.getString("birthday"));
                person.setNative_place(rs.getString("native_place"));
                person.setParty(rs.getString("party"));
                person.setWork_start_date(rs.getString("work_start_date"));
                person.setHealth_status(rs.getString("health_status"));
                person.setEducation(rs.getString("education"));
                person.setGraduate_school_major(rs.getString("graduate_school_major"));
                person.setDegree_technical_title(rs.getString("degree_technical_title"));
                person.setExpertise(rs.getString("expertise"));
                person.setId_card_number(rs.getString("id_card_number"));
                person.setEmployer(rs.getString("employer"));
                person.setCurrent_position(rs.getString("current_position"));
                person.setPhone_number(rs.getString("phone_number"));
                person.setPostal_code(rs.getString("postal_code"));
                person.setResume(rs.getString("resume"));
                person.setToken(rs.getString("token"));
            }
            logger.info("在数据库中查找指定用户成功");
        } catch (SQLException e) {
            logger.error("在数据库中查找指定用户失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
//        响应用户信息至前端
        mapReturn.put("person", person);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
