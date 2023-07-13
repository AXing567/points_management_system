package com.axing.points_ms.servlet;

import com.axing.points_ms.filter.LoginRegisterFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet
 * @className: RegisterServlet
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/9 15:43
 * @version: 1.0
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet{

    Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Register已启动");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        LoginRegisterFilter loginRegisterFilter = new LoginRegisterFilter();

//        接收前端传来的数据并将数据存储至注册预览表
        loginRegisterFilter.register(request, response);
        logger.info("用户状态信息已返回（注册页）");
    }
}
