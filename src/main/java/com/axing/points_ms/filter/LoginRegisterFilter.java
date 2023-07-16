package com.axing.points_ms.filter;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.axing.points_ms.utils.Encipher.md5;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.filter
 * @className: LoginRegisterFilter
 * @author: Axing
 * @description: 为登录注册提供过滤器
 * @date: 2023/7/5 18:20
 * @version: 1.0
 */
public class LoginRegisterFilter {
    Result result = new Result();
    Logger logger = LoggerFactory.getLogger(LoginRegisterFilter.class);
    int time = 0;

    /**
     * @param request:
     * @param response:
     * @return void
     * @author Axing
     * @description 根据用户输入的账号密码进行登录，登录成功或失败，响应一个对应的Json串
     * @date 2023/7/6 13:07
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> mapReceive;
        Map<String, Object> mapReturn = new HashMap<>();
        Gson gson = new Gson();
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();

//        获取前端传来的数据并赋值到map中
        logger.info("正在获取前端传来的数据");
        String json = ObtainData.obtain_data(request); //HttpServletRequest request对象只能调用一次getReader()方法，否则会报错
        mapReceive = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        Result result = new Result();
        logger.info("获取了前端传来的数据");



//        判断账号密码的正确性
        String username = mapReceive.get("username").toString();
        String password = mapReceive.get("password").toString();
        if (mapReceive.isEmpty()) {
            result.setLoginRegisterResult(false, "用户名密码不能为空", "0", "000000000000000000000000000000000000");
            logger.info("用户名密码不能为空");
        } else {
            if (!operateDB.selectUser(username)) {
                result.setLoginRegisterResult(false, "用户名不存在", "0", "000000000000000000000000000000000000");
                logger.info("用户名密码不能为空");
            }
            String token = operateDB.select_token(username, md5(md5(password) + operateDB.selectSalt(username)));
            String user_id = operateDB.selectUserId(username);
            String power = operateDB.select_power(user_id);
            if (token != null) {
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
    }

    /**
     * @param request:
     * @param response:
     * @return void
     * @author Axing
     * @description 注册（仅限手机号注册）
     * @date 2023/7/9 16:13
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        获取前端传来的数据并赋值到map中
        Map<String, Object> mapReceive;
        Gson gson = new Gson();
        String json = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        Result result = new Result();

//        存储用户信息
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        String username = mapReceive.get("username").toString();
        String password = mapReceive.get("password").toString();
        String nickname = mapReceive.get("nickname").toString();
//        String loginTime = mapReceive.get("loginTime").toString();
//        String loginIP = mapReceive.get("loginIP").toString();
        if (operateDB.selectUser(username)) {
            result.setSuccess(false);
            result.setMessage("手机号已存在");
            logger.info("手机号已存在");
        } else {
//            String ip = NetTools.getClientIPAddress(request);
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
    }





}
