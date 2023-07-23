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
 * @author Axing
 * @description 找回密码
 * @date 2023/7/13 13:15
 */
@WebServlet("/findPassword")
public class FindPasswordServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(FindPasswordServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String> mapReceive;
        Map<String, String> mapReturn = new HashMap<>();
        Result result = new Result();
        Gson gson = new Gson();
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();

//        获取前端数据
        mapReceive = gson.fromJson(ObtainData.obtain_data(request), new TypeToken<Map<String, String>>() {
        }.getType());

        String username = mapReceive.get("username");
        String nickname = mapReceive.get("nickname");
        String password = mapReceive.get("password");

//        将数据存储至密码找回表
        boolean check = operateDB.insert_find_password(username, nickname, password);
        if (check) {
            result.setSuccess(true);
            result.setMessage("新密码已提交审核");
            logger.info("新密码已提交审核");
        } else {
            result.setSuccess(false);
            result.setMessage("新密码提交失败");
            logger.info("新密码提交失败");
        }

//        返回数据
        mapReturn.put("result", gson.toJson(result));
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
