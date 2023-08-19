package com.axing.points_ms.servlet.select;


import com.axing.points_ms.model.dto.PasswordReview;
import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.servlet.user_preview_review.ReviewPointChangesServlet;
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
 * @className: SelectPasswordUpdate
 * @author: Axing
 * @description: TODO
 * @date: 2023/8/19 15:20
 * @version: 1.0
 */
@WebServlet("/selectPasswordUpdate")
public class SelectPasswordUpdate extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(ReviewPointChangesServlet.class);

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
        Gson gson = new Gson();
        String receiveData = null;
        ResultSet rs = null;

//        查找所有提交修改密码的用户信息
        rs = operateDB.select_find_password_review();
        try {
            while (rs.next()) {

                PasswordReview passwordReview = new PasswordReview();
                String username = rs.getString("username");
                passwordReview.setUsername(username);
                passwordReview.setId(rs.getString("id"));
                passwordReview.setNickname(rs.getString("nickname"));
                passwordReview.setPassword(rs.getString("password"));
                passwordReview.setCheck_find(rs.getString("check_find"));

                mapReturn.put(username, passwordReview);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        返回数据
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
