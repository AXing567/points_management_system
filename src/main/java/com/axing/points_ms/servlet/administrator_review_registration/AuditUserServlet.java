package com.axing.points_ms.servlet.administrator_review_registration;

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
 * @className: AuditUserServlet
 * @author: Axing
 * @description: （管理员审核注册用户环节）接收客户端发送的审核结果
 * @date: 2023/7/15 14:57
 * @version: 1.0
 */
@WebServlet("/auditUser")
public class AuditUserServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(AuditUserServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, String> mapReceive;
        Map<String, Object> mapReturn = new HashMap<>();
        Gson gson = new Gson();

//        接收客户端发送的审核结果
        String receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = mapReceive.get("user_id");


//        将审核结果存入数据库并返回结果给客户端
        boolean check = operateDB.update_user_review(user_id, 1);
//        将用户信息存入用户表
        boolean check2 = operateDB.insert_user(user_id);
        boolean check3 = operateDB.insert_user_info(user_id);
        if (check && check2 && check3) {
            result.setSuccess(true);
            result.setMessage("用户" + user_id + "审核成功");
        } else {
            result.setSuccess(false);
            result.setMessage("用户" + user_id + "审核失败");
        }
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");

    }
}
