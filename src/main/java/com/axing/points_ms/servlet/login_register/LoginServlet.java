package com.axing.points_ms.servlet.login_register;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.axing.points_ms.utils.Encipher.md5;

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
        logger.info("被调用");
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        Map<String, String> mapReceive;
        Map<String, Object> mapReturn = new HashMap<>();
        Gson gson = new Gson();
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();

//        获取前端传来的数据并赋值到map中
        logger.info("正在获取前端传来的数据");
        String receiveData = ObtainData.obtain_data(request); //HttpServletRequest request对象只能调用一次getReader()方法，否则会报错
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        Result result = new Result();

        String username = mapReceive.get("username");
        String password = mapReceive.get("password");
        logger.info("获取了前端传来的数据");


        if (mapReceive.containsKey("token")) {
            String token = mapReceive.get("token");
//            比对token是否正确,如果正确则返回登录成功
            if(!token.equals("") && operateDB.select_boolean_token(mapReceive.get("token"))){
                result.setSuccess(true);
                result.setMessage("登录成功");
                logger.info("登录成功");
                mapReturn.put("result", result);
                response.getWriter().write(gson.toJson(mapReturn));
                logger.info("返回数据成功");
            }
            return;
        }


//        判断账号密码的正确性
        if (mapReceive.isEmpty()) {
            result.setLoginRegisterResult(false, "用户名密码不能为空", "0", "000000000000000000000000000000000000");
            logger.info("用户名密码不能为空");
        } else {
            if (!operateDB.selectUser(username)) {
                result.setLoginRegisterResult(false, "用户名不存在", "0", "000000000000000000000000000000000000");
                logger.info("用户名密码不能为空");
            }
            String user_id = operateDB.select_username_password(username, md5(md5(password) + operateDB.selectSalt(username)));
            String power = operateDB.select_power(user_id);
            if (!user_id.equals("")) {
                String token = UUID.randomUUID().toString();
                operateDB.update_user_token(token, user_id);
                result.setId(user_id);
                result.setSuccess(true);
                result.setMessage("登录成功");
                result.setToken(token);
                result.setPower(power);
                logger.info("登录成功");
            } else {
                result.setSuccess(false);
                result.setMessage("账号或密码错误");
                logger.info("账号或密码错误");
            }
        }
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
    }
}
