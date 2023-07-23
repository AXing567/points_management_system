package com.axing.points_ms.servlet.login_register;

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
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

//        获取前端传来的数据并赋值到map中
        Map<String, Object> mapReceive;
        Gson gson = new Gson();
        String receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, Object>>() {
        }.getType());
        Result result = new Result();

//        存储用户信息
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        String username = mapReceive.get("username").toString();
        String password = mapReceive.get("password").toString();
        String nickname = mapReceive.get("nickname").toString();
//        String register_ip = mapReceive.get("register_ip").toString();
        if (operateDB.selectUser(username)) {
            result.setSuccess(false);
            result.setMessage("手机号已存在");
            logger.info("手机号已存在");
        } else {
            if (operateDB.insert_user_review(nickname, username, password )) {
                result.setSuccess(true);
                result.setMessage("账号注册已提交审核");
                logger.info("账号注册已提交审核");
            } else {
                result.setSuccess(false);
                result.setMessage("账号注册提交审核失败");
                logger.info("账号注册提交审核失败");
            }
        }
//        返回注册结果
        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
