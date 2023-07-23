package com.axing.points_ms.servlet.administrator_review_registration;

import com.axing.points_ms.model.dto.Person;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet
 * @className: UnauditedUserReviewServlet
 * @author: Axing
 * @description: （管理员审核注册用户环节）向客户端发送待审核的用户信息
 * @date: 2023/7/14 11:02
 * @version: 1.0
 */
@WebServlet("/unauditedUserReview")
public class UnauditedUserReviewServlet extends HttpServlet {
    ResultSet rs = null;
    Logger logger = LoggerFactory.getLogger(UnauditedUserReviewServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Map<String, Object> mapReturn = new LinkedHashMap<>();
        Gson gson = new Gson();

//        向客户端发送待审核的用户信息
        rs = operateDB.select_user_review("0");
        try {
            while (rs.next()) {
                Person person = new Person();
                String user_id = rs.getString("user_id");
                person.setUser_id(user_id);
                person.setUsername(rs.getString("username"));
                person.setNickname(rs.getString("nickname"));
                person.setRegister_time(rs.getString("register_time"));
                mapReturn.put(user_id, person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
