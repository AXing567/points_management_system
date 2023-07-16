package com.axing.points_ms.servlet.insert;

import com.axing.points_ms.model.dto.Person;
import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.insert
 * @className: InsertUserInfoServlet
 * @author: Axing
 * @description: 添加用户详细信息
 * @date: 2023/7/13 16:18
 * @version: 1.0
 */
@WebServlet("/insertUserInfo")
public class InsertUserInfoServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(InsertUserInfoServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String , Object> mapReturn = new HashMap<String ,Object>();
        Person person = new Person();
        Gson gson = new Gson();

//        获取前端传来的数据
        String frontData = ObtainData.obtain_data(request);

//        存储前端传来的数据
        person = gson.fromJson(frontData, Person.class);
        boolean check = operateDB.update_user_info(frontData);
        if (check){
            result.setSuccess(true);
            result.setMessage("数据上传成功");
        }else {
            result.setSuccess(false);
            result.setMessage("数据上传失败");
        }

//        返回数据给前端
        mapReturn.put("result", result);
        mapReturn.put("person", person);
        String jsonReturn = gson.toJson(mapReturn);
        response.getWriter().write(jsonReturn);
    }
}
