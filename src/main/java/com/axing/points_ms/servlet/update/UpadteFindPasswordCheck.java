package com.axing.points_ms.servlet.update;


import com.axing.points_ms.model.dto.Person;
import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.servlet.user_preview_review.ReviewPointChangesServlet;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.update
 * @className: UpadteFindPasswordCheck
 * @author: Axing
 * @description: 更新找回密码的check字段和user表中password、salt字段
 * @date: 2023/8/19 15:45
 * @version: 1.0
 */
@WebServlet("/updateFindPasswordCheck")
public class UpadteFindPasswordCheck extends HttpServlet {
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
        Person person = new Person();
        Gson gson = new Gson();
        String receiveData = null;
        ResultSet rs = null;

//        接收前端数据 username , check , password
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String username = mapReceive.get("username");
        String check = mapReceive.get("check");
        String password = mapReceive.get("password");
        logger.info("获取前端数据成功");

//        更新密码
        if(check.equals("1")){
            boolean check1 = operateDB.update_find_password_check(username, check);
            boolean check2 = operateDB.update_user_password(username, password);
            if (check1 && check2) {
                result.setSuccess(true);
                result.setMessage("密码修改成功");
                logger.info("密码修改成功");
            } else {
                result.setSuccess(false);
                result.setMessage("密码修改失败");
                logger.error("修改密码功能出现错误（可能出现find_password_review表的check字段修改正确性和user表中的密码修改的正确性有误");
            }
        }
        if(check.equals("2")){
            boolean check1 = operateDB.update_find_password_check(username, check);
            if(check1) {
                result.setSuccess(true);
                result.setMessage("已拒绝" + username +"修改密码");
                logger.info("已拒绝" + username +"修改密码");
            } else {
                result.setSuccess(false);
                result.setMessage("拒绝" + username +"修改密码失败");
                logger.error("拒绝" + username +"修改密码失败（不正常的异常）");
            }
        }

//        返回数据
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
