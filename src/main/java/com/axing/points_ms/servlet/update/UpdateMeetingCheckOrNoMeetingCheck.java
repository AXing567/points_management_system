package com.axing.points_ms.servlet.update;

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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.update
 * @className: UpdateMeetingCheckOrNoMeetingCheck
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/22 10:24
 * @version: 1.0
 */
@WebServlet("/UpdateMeetingCheckOrNoMeetingCheck")
public class UpdateMeetingCheckOrNoMeetingCheck extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(ReviewPointChangesServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, Object> mapReturn = new HashMap<>();
        Map<String, String> mapReceive;
        Gson gson = new Gson();
        String receiveData;

//        接收前端数据 id , check ,select
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String id = mapReceive.get("id");
        int check = Integer.parseInt(mapReceive.get("check"));
        int select = Integer.parseInt(mapReceive.get("select"));
        logger.info("获取前端数据成功");


//        更新指定数据集
        try {
            if(operateDB.update_meeting_or_no_meeting_check(id,check,select==1)){
                result.setSuccess(true);
                result.setMessage("更改check字段成功");
                logger.info("更改check字段成功");
            }else {
                result.setSuccess(false);
                result.setMessage("更改check字段失败");
                logger.error("更改check字段失败");
            }
        }catch (SQLException e){
            result.setSuccess(false);
            result.setMessage("更改check字段失败");
            logger.error("更改check字段失败");
            e.printStackTrace();
        }




//        返回数据
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
