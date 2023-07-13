//package com.axing.points_ms;
//
//import com.axing.points_ms.filter.LoginRegisterFilter;
//import com.axing.points_ms.model.dto.Result;
//import com.axing.points_ms.utils.ObtainData;
//import com.axing.points_ms.utils.OperateDB;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class Test extends HttpServlet {
//    Logger logger = LoggerFactory.getLogger(LoginRegisterFilter.class);
//    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
////        获取前端传来的数据并赋值到map中
//        Map<String, Object> mapReceive;
//        Gson gson = new Gson();
//        String json = ObtainData.obtain_data(request);
//        mapReceive = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
//        }.getType());
//        Result result = new Result();
//
////        存储用户信息
//        OperateDB operateDB = new OperateDB();
//        operateDB.connect2();
//        String username = mapReceive.get("username").toString();
//        String password = mapReceive.get("password").toString();
//        String nickname = mapReceive.get("nickname").toString();
//        String loginTime = mapReceive.get("loginTime").toString();
//        String loginIP = mapReceive.get("loginIP").toString();
//        String register_time = mapReceive.get("register_time").toString();
//        if (operateDB.selectUser(username)) {
//            result.setSuccess(false);
//            result.setMessage("手机号已存在");
//            response.getWriter().write("手机号已存在");
//            logger.info("手机号已存在");
//        } else {
//            if (operateDB.insertUser(nickname, username, password)) {
//                result.setSuccess(true);
//                result.setMessage("注册成功");
//                logger.info("注册成功");
//            } else {
//                result.setSuccess(false);
//                result.setMessage("注册失败");
//                logger.info("注册失败");
//            }
//        }
////        返回注册结果
//        Map<String, Object> mapReturn = new HashMap<>();
//        mapReturn.put("result", result);
//        response.getWriter().write(gson.toJson(mapReturn));
//    }
//
//
//}
