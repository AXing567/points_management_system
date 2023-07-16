package com.axing.points_ms.servlet;

import com.axing.points_ms.filter.LoginRegisterFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: points_management_system
 * @package: com.example.points_management_system
 * @className: Login
 * @author: Axing
 * @description: 插入大会相关信息
 * @date: 2023/7/5 15:51
 * @version: 1.0
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(LoginServlet.class);


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Login已启动");
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");



        LoginRegisterFilter loginRegisterFilter = new LoginRegisterFilter();
        loginRegisterFilter.login(request, response); // 进行登录成功与否的判断与返回相应对象
        logger.info("用户状态信息已返回（登录页）");

    }
}
