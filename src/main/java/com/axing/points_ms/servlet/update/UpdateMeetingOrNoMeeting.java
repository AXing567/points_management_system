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
import java.util.HashMap;
import java.util.Map;


/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.update
 * @className: UpdateMeetingOrNoMeeting
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/22 19:52
 * @version: 1.0
 */
@WebServlet("/updateMeetingOrNoMeeting")
public class UpdateMeetingOrNoMeeting extends HttpServlet {
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
        boolean check;

//        接收前端数据 id , check
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        int select = Integer.parseInt(mapReceive.get("select"));
        logger.info("获取前端数据成功");

//        更新数据
        if (select == 1) {
            check = operateDB.update_meeting(receiveData);
        } else {
            check = operateDB.update_no_meeting(receiveData);
        }


//        返回数据
        if (check) {
            result.setSuccess(true);
            result.setMessage("更新成功");
            mapReturn.put("result", result);
            logger.info("更新数据成功");
        } else {
            result.setSuccess(false);
            result.setMessage("更新失败");
            mapReturn.put("result", result);
            logger.info("更新数据失败");
        }

        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
