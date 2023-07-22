package com.axing.points_ms.servlet.select;

import com.axing.points_ms.model.dto.Person;
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
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.select
 * @className: SelectTokenServlet
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/19 19:53
 * @version: 1.0
 */
@WebServlet("/selectToken")
public class SelectTokenServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(SelectTokenServlet.class);
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

//        获取前端数据 token
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String token = mapReceive.get("token");


//        验证token正确性
        String user_id = operateDB.select_token(token);
        if(!user_id.equals("")){
            result.setMessage("token正确");
            result.setSuccess(true);
            result.setId(user_id);
        }else {
            result.setMessage("token错误");
            result.setSuccess(false);
        }

//        返回数据
        mapReturn.put("result", result);
        String returnData = gson.toJson(mapReturn);
        response.getWriter().write(returnData);

    }
}
